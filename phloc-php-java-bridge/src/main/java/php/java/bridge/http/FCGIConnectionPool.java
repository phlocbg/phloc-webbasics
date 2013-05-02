/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*-*- mode: Java; tab-width:8 -*-*/

package php.java.bridge.http;

/*
 * Copyright (C) 2003-2007 Jost Boekemeier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER(S) OR AUTHOR(S) BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import php.java.bridge.Util;

/**
 * A connection pool. Example:<br>
 * <br>
 * <code>
 * ConnectionPool pool = new ConnectionPool("127.0.0.1", 8080, 20, 5000, new IOFactory());<br>
 * ConnectionPool.Connection conn = pool.openConnection();<br>
 * InputStream in =  conn.getInputStream();<br>
 * OutputStream out = conn.getOutputStream();<br>
 * ...<br>
 * in.close();<br>
 * out.close();<br>
 * ...<br>
 * pool.destroy();<br>
 * </code>
 * <p>
 * Instead of using delegation (decorator pattern), it is possible to pass a
 * factory which may create custom In- and OutputStreams. Example:<br>
 * <br>
 * <code>
 * new ConnectionPool(..., new IOFactory() {<br>
 * &nbsp;&nbsp;public InputStream getInputStream() {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;return new DefaultInputStream() {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
 * &nbsp;&nbsp;}<br>
 * }<br>
 * </code>
 * </p>
 *
 * @author jostb
 */
public class FCGIConnectionPool
{

  private final int limit;
  private long timeout;
  private int connections = 0;
  private final List <Connection> freeList = new ArrayList <Connection> ();
  private final List <Connection> connectionList = new ArrayList <Connection> ();
  private final AbstractFCGIIOFactory factory;
  private final int maxRequests;
  private final AbstractFCGIConnectionFactory channelName;

  /**
   * Represents the connection kept by the pool.
   *
   * @author jostb
   */
  public final class Connection
  {
    protected int ostate, state; // bit0: input closed, bit1: output closed
    protected AbstractFCGIConnectionFactory nchannelName;
    protected AbstractFCGIConnection channel;
    private boolean isClosed;
    private final AbstractFCGIIOFactory nfactory;
    private final int nmaxRequests;
    private int counter;

    protected void reset ()
    {
      this.state = this.ostate = 0;
    }

    protected void init ()
    {
      counter = nmaxRequests;
      reset ();
    }

    protected Connection reopen () throws FCGIConnectException
    {
      if (isClosed)
        this.channel = nfactory.connect (nchannelName);
      this.isClosed = false;
      return this;
    }

    protected Connection (final AbstractFCGIConnectionFactory channelName, final int maxRequests, final AbstractFCGIIOFactory factory)
    {
      this.nchannelName = channelName;
      this.nfactory = factory;
      this.isClosed = true;
      this.nmaxRequests = maxRequests;
      init ();
    }

    /** Set the closed/abort flag for this connection */
    public void setIsClosed ()
    {
      isClosed = true;
    }

    protected void close ()
    {
      // PHP child terminated: mark as closed, so that reopen() can allocate
      // a new connection for the new PHP child
      if (nmaxRequests > 0 && --counter == 0)
        isClosed = true;

      if (isClosed)
      {
        destroy ();
        init ();
      }
      closeConnection (this);
    }

    private void destroy ()
    {
      try
      {
        channel.close ();
      }
      catch (final IOException e)
      {/* ignore */}
    }

    /**
     * Returns the OutputStream associated with this connection.
     *
     * @return The output stream.
     * @throws FCGIConnectionException
     */
    public OutputStream getOutputStream () throws FCGIConnectionException
    {
      final FCGIConnectionOutputStream outputStream = (FCGIConnectionOutputStream) nfactory.createOutputStream ();
      outputStream.setConnection (this);
      ostate |= 2;
      return outputStream;
    }

    /**
     * Returns the InputStream associated with this connection.
     *
     * @return The input stream.
     * @throws FCGIConnectionException
     */
    public InputStream getInputStream () throws FCGIConnectionException
    {
      final FCGIConnectionInputStream inputStream = (FCGIConnectionInputStream) nfactory.createInputStream ();
      inputStream.setConnection (this);
      ostate |= 1;
      return inputStream;
    }
  }

  /**
   * Create a new connection pool.
   *
   * @param channelName
   *        The channel name
   * @param limit
   *        The max. number of physical connections
   * @param maxRequests
   * @param factory
   *        A factory for creating In- and OutputStreams.
   * @throws FCGIConnectException
   * @see AbstractFCGIIOFactory
   */
  private FCGIConnectionPool (final AbstractFCGIConnectionFactory channelName,
                              final int limit,
                              final int maxRequests,
                              final AbstractFCGIIOFactory factory) throws FCGIConnectException
  {
    if (Util.logLevel > 3)
      Util.logDebug ("Creating new connection pool for: " + channelName);
    this.channelName = channelName;
    this.limit = limit;
    this.factory = factory;
    this.maxRequests = maxRequests;
    this.timeout = -1;
    channelName.test ();
  }

  /**
   * Create a new connection pool.
   *
   * @param channelName
   *        The channel name
   * @param limit
   *        The max. number of physical connections
   * @param maxRequests
   * @param factory
   *        A factory for creating In- and OutputStreams.
   * @param timeout
   *        The pool timeout in milliseconds.
   * @throws FCGIConnectException
   * @see AbstractFCGIIOFactory
   */
  public FCGIConnectionPool (final AbstractFCGIConnectionFactory channelName,
                             final int limit,
                             final int maxRequests,
                             final AbstractFCGIIOFactory factory,
                             final long timeout) throws FCGIConnectException
  {
    this (channelName, limit, maxRequests, factory);
    this.timeout = timeout;
  }

  /* helper for openConnection() */
  private Connection createNewConnection ()
  {
    final Connection connection = new Connection (channelName, maxRequests, factory);
    connectionList.add (connection);
    connections++;
    return connection;
  }

  /**
   * Opens a connection to the back end.
   *
   * @return The connection
   * @throws InterruptedException
   * @throws FCGIConnectException
   */
  public synchronized Connection openConnection () throws InterruptedException, FCGIConnectException
  {
    Connection connection;
    if (freeList.isEmpty () && connections < limit)
    {
      connection = createNewConnection ();
    }
    else
    {
      while (freeList.isEmpty ())
      {
        if (timeout > 0)
        {
          final long t1 = System.currentTimeMillis ();
          wait (timeout);
          final long t2 = System.currentTimeMillis ();
          final long t = t2 - t1;
          if (t >= timeout)
            throw new FCGIConnectException (new IOException ("pool timeout " + timeout + " exceeded: " + t));
        }
        else
        {
          wait ();
        }
      }
      connection = freeList.remove (0);
      connection.reset ();
    }
    return connection.reopen ();
  }

  private synchronized void closeConnection (final Connection connection)
  {
    freeList.add (connection);
    notify ();
  }

  /**
   * Destroy the connection pool. It releases all physical connections.
   */
  public synchronized void destroy ()
  {
    for (final Connection connection : connectionList)
    {
      connection.destroy ();
    }

    if (channelName != null)
      channelName.destroy ();
  }
}
