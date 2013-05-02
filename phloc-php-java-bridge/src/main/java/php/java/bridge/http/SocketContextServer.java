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
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import php.java.bridge.AppThreadPool;
import php.java.bridge.ILogger;
import php.java.bridge.ISocketFactory;
import php.java.bridge.JavaBridge;
import php.java.bridge.Util;
import php.java.bridge.Util.UtilThread;

/**
 * This class manages the fallback physical connection for the operating system
 * which doesn't support named pipes, "Windows", or when the System property
 * php.java.bridge.promiscuous is set to true.
 * <p>
 * When isAvailable() returns true, a server socket bound to the local interface
 * (127.0.0.1) has been created on a port in the range [9267,...,[9367 and will
 * be used for further communication, see response header X_JAVABRIDGE_REDIRECT.
 * If this communication channel is not available either, the PHP clients must
 * continue to send all statements via PUT requests.
 * </p>
 * <p>
 * It is possible to switch off this server by setting the VM property
 * php.java.bridge.no_socket_server to true, e.g.:
 * -Dphp.java.bridge.no_socket_server=true.
 * </p>
 * 
 * @see php.java.bridge.http.ContextServer
 */
public final class SocketContextServer implements Runnable, IContextServer
{
  private final AppThreadPool threadPool;
  private ISocketFactory serverSocket = null;
  protected List <Socket> sockets = new Vector <Socket> ();
  private ILogger logger;
  private final String contextName;

  protected class Channel extends AbstractChannel
  {
    protected Socket sock;
    protected InputStream in;
    protected OutputStream out;
    protected String name;

    public Channel (final String name, final InputStream in, final OutputStream out, final Socket sock)
    {
      this.name = name;
      this.in = in;
      this.out = out;
      this.sock = sock;
      sockets.add (sock);
    }

    @Override
    public String getName ()
    {
      return name;
    }

    @Override
    public InputStream getInputStream ()
    {
      return in;
    }

    @Override
    public OutputStream getOuptutStream ()
    {
      return out;
    }

    public Socket getSocket ()
    {
      return sock;
    }

    @Override
    public void shutdown ()
    {
      if (in != null)
        try
        {
          in.close ();
        }
        catch (final IOException e)
        {/* ignore */}
      if (out != null)
        try
        {
          out.close ();
        }
        catch (final IOException e)
        {/* ignore */}
      if (sockets.remove (sock))
        try
        {
          sock.close ();
        }
        catch (final IOException e)
        {/* ignore */}
    }
  }

  /**
   * Create a new ContextServer using the ThreadPool.
   * 
   * @param threadPool
   *        Obtain runnables from this pool. If null, new threads will be
   *        created.
   */
  public SocketContextServer (final AppThreadPool threadPool, final boolean promiscuous, final String contextName)
  {
    this.threadPool = threadPool;
    this.contextName = contextName;
    try
    {
      serverSocket = JavaBridge.bind (promiscuous ? "INET:0" : "INET_LOCAL:0");
      final SecurityManager sec = System.getSecurityManager ();
      if (sec != null)
        sec.checkAccept ("127.0.0.1", Integer.parseInt (serverSocket.getSocketName ()));
      final Thread t = new UtilThread (this, "JavaBridgeSocketContextServer(" + serverSocket.getSocketName () + ")");
      t.start ();
    }
    catch (final Throwable t)
    {
      Util.warn ("Local communication channel not available.");
      Util.printStackTrace (t);
      if (serverSocket != null)
        try
        {
          serverSocket.close ();
        }
        catch (final IOException e)
        {}
      serverSocket = null;
    }
  }

  private boolean accept ()
  {
    InputStream in = null;
    OutputStream out = null;
    Socket socket = null;
    Channel channel = null;
    try
    {
      try
      {
        socket = this.serverSocket.accept ();
      }
      catch (final IOException ex)
      {
        return false;
      } // socket closed
      in = socket.getInputStream ();
      out = socket.getOutputStream ();
      final ContextRunner runner = new ContextRunner (channel = new Channel (getChannelName (), in, out, socket),
                                                      logger);
      if (threadPool != null)
      {
        threadPool.start (runner);
      }
      else
      {
        final Thread t = new UtilThread (runner, "JavaBridgeContextRunner(" + contextName + ")");
        t.start ();
      }
    }
    catch (final SecurityException t)
    {
      if (channel != null)
        channel.shutdown ();
      ContextFactory.destroyAll ();
      Util.printStackTrace (t);
      return false;
    }
    catch (final Throwable t)
    {
      if (channel != null)
        channel.shutdown ();
      Util.printStackTrace (t);
    }
    return true;
  }

  /** {@inheritDoc} */
  public void run ()
  {
    while (serverSocket != null)
    {
      if (!accept ())
        destroy ();
    }
    if (Util.logLevel > 4)
      System.err.println ("SocketContextServer stopped, the local channel is not available anymore.");
  }

  private void closeAllSockets ()
  {
    synchronized (sockets)
    {
      for (final Iterator <Socket> ii = sockets.iterator (); ii.hasNext ();)
      {
        final Socket sock = ii.next ();
        ii.remove ();
        try
        {
          sock.close ();
        }
        catch (final IOException e)
        {}
      }
    }
  }

  /**
   * Destroy the server
   */
  public void destroy ()
  {
    closeAllSockets ();

    if (serverSocket != null)
    {
      try
      {
        serverSocket.close ();
      }
      catch (final IOException e)
      {
        Util.printStackTrace (e);
      }
      serverSocket = null;
    }
  }

  private static boolean checkTestTunnel (final String property)
  {
    try
    {
      return !"true".equals (System.getProperty (property));
    }
    catch (final SecurityException e)
    {
      return false;
    }
    catch (final Throwable t)
    {
      return true;
    }
  }

  public static final boolean SOCKET_SERVER_AVAIL = checkTestTunnel ("php.java.bridge.no_socket_server");

  /**
   * Check if the ContextServer is ready, i.e. it has created a server socket.
   * 
   * @return true if there's a server socket listening, false otherwise.
   */
  public boolean isAvailable ()
  {
    // The standalone runner sets an empty context name, otherwise the
    // promiscuous option means that the servlet engine should use chunked
    // encoding
    return (SOCKET_SERVER_AVAIL && serverSocket != null);
  }

  /**
   * Returns the server port.
   * 
   * @return The server port.
   */
  public String getChannelName ()
  {
    return serverSocket.getSocketName ();
  }

  /** {@inheritDoc} */
  public boolean start (final AbstractChannelName channelName, final ILogger logger)
  {
    this.logger = logger;
    return isAvailable ();
  }
}
