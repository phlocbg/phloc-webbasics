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

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import php.java.bridge.ILogger;
import php.java.bridge.Util;
import php.java.bridge.Util.UtilProcess;

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

class SocketChannelFactory extends AbstractFCGIConnectionFactory
{
  public static final String LOCAL_HOST = "127.0.0.1";
  private int port;

  private ServerSocket fcgiTestSocket = null;
  private int fcgiTestPort;

  public SocketChannelFactory (final IFCGIProcessFactory processFactory, final boolean promiscuous)
  {
    super (processFactory);
    this.promiscuous = promiscuous;
  }

  @Override
  public void test () throws FCGIConnectException
  {
    Socket testSocket;
    try
    {
      testSocket = new Socket (InetAddress.getByName (getName ()), port);
      testSocket.close ();
    }
    catch (final IOException e)
    {
      if (lastException != null)
      {
        throw new FCGIConnectException (String.valueOf (e), lastException);
      }
      throw new FCGIConnectException (e);
    }
  }

  /**
   * Create a new socket and connect it to the given host/port
   * 
   * @param host
   *        The host, for example 127.0.0.1
   * @param port
   *        The port, for example 9667
   * @return The socket
   * @throws UnknownHostException
   * @throws FCGIConnectionException
   */
  private Socket doConnect (final String host, final int port) throws FCGIConnectException
  {
    Socket s = null;
    try
    {
      s = new Socket (InetAddress.getByName (host), port);
    }
    catch (final IOException e)
    {
      throw new FCGIConnectException (e);
    }
    try
    {
      s.setTcpNoDelay (true);
    }
    catch (final SocketException e)
    {
      Util.printStackTrace (e);
    }
    return s;
  }

  @Override
  public AbstractFCGIConnection connect () throws FCGIConnectException
  {
    final Socket s = doConnect (getName (), getPort ());
    return new SocketChannel (s);
  }

  @Override
  protected void waitForDaemon () throws UnknownHostException, InterruptedException
  {
    final long T0 = System.currentTimeMillis ();
    int count = 15;
    final InetAddress addr = InetAddress.getByName (LOCAL_HOST);
    if (Util.logLevel > 3)
      Util.logDebug ("Waiting for PHP FastCGI daemon");
    while (count-- > 0)
    {
      try
      {
        final Socket s = new Socket (addr, getPort ());
        s.close ();
        break;
      }
      catch (final IOException e)
      {/* ignore */}
      if (System.currentTimeMillis () - 16000 > T0)
        break;
      Thread.sleep (1000);
    }
    if (count == -1)
      Util.logError ("Timeout waiting for PHP FastCGI daemon");
    if (Util.logLevel > 3)
      Util.logDebug ("done waiting for PHP FastCGI daemon");
  }

  /* Start a fast CGI Server process on this computer. Switched off per default. */
  @Override
  protected UtilProcess doBind (final Map <String, String> env, final String php, final boolean includeJava) throws IOException
  {
    if (proc != null)
      return null;
    // bind to all available or loopback only
    final StringBuilder buf = new StringBuilder ((Util.JAVABRIDGE_PROMISCUOUS || promiscuous) ? "" : LOCAL_HOST);
    buf.append (':');
    buf.append (String.valueOf (getPort ()));
    final String port = buf.toString ();

    // Set override hosts so that php does not try to start a VM.
    // The value itself doesn't matter, we'll pass the real value
    // via the (HTTP_)X_JAVABRIDGE_OVERRIDE_HOSTS header field
    // later.
    File home = null;
    if (php != null)
      try
      {
        home = ((new File (php)).getParentFile ());
      }
      catch (final Exception e)
      {
        Util.printStackTrace (e);
      }
    proc = processFactory.createFCGIProcess (new String [] { php, "-b", port }, includeJava, home, env);
    proc.start ();
    return (UtilProcess) proc;
  }

  protected int getPort ()
  {
    return port;
  }

  protected String getName ()
  {
    return LOCAL_HOST;
  }

  @Override
  public String getFcgiStartCommand (final String base, final String php_fcgi_max_requests)
  {
    final String msg = "cd " +
                       base +
                       File.separator +
                       Util.osArch +
                       "-" +
                       Util.osName +
                       "\n" +
                       "REDIRECT_STATUS=200 " +
                       "X_JAVABRIDGE_OVERRIDE_HOSTS=\"/\" " +
                       "PHP_FCGI_CHILDREN=\"5\" " +
                       "PHP_FCGI_MAX_REQUESTS=\"" +
                       php_fcgi_max_requests +
                       "\" /usr/bin/php-cgi -b 127.0.0.1:" +
                       getPort () +
                       "\n\n";
    return msg;
  }

  @Override
  protected void bind (final ILogger logger) throws InterruptedException, IOException
  {
    if (fcgiTestSocket != null)
    {
      fcgiTestSocket.close ();
      fcgiTestSocket = null;
    }// replace the allocated socket# with the real fcgi server
    super.bind (logger);
  }

  @Override
  public void findFreePort (final boolean select)
  {
    fcgiTestPort = FCGIUtil.FCGI_PORT;
    fcgiTestSocket = null;
    for (int i = FCGIUtil.FCGI_PORT + 1; select && (i < FCGIUtil.FCGI_PORT + 100); i++)
    {
      try
      {
        final ServerSocket s = new ServerSocket (i, Util.BACKLOG, InetAddress.getByName (LOCAL_HOST));
        fcgiTestPort = i;
        fcgiTestSocket = s;
        break;
      }
      catch (final IOException e)
      {/* ignore */}
    }
  }

  @Override
  public void setDefaultPort ()
  {
    port = FCGIUtil.FCGI_PORT;
  }

  @Override
  protected void setDynamicPort ()
  {
    port = fcgiTestPort;
  }

  @Override
  public void destroy ()
  {
    super.destroy ();
    if (fcgiTestSocket != null)
      try
      {
        fcgiTestSocket.close ();
        fcgiTestSocket = null;
      }
      catch (final Exception e)
      {/* ignore */}
  }

  /**
   * Return the channel name
   * 
   * @return the channel name
   */
  @Override
  public String toString ()
  {
    return "ChannelName@127.0.0.1:" + port;
  }
}
