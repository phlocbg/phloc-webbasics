/**
 * Copyright (C) 2006-2014 phloc systems
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import php.java.bridge.ILogger;
import php.java.bridge.Util;
import php.java.bridge.Util.UtilProcess;
import php.java.bridge.Util.UtilThread;

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

/**
 * A factory which creates FastCGI channels.
 * 
 * @author jostb
 */
public abstract class AbstractFCGIConnectionFactory
{
  protected boolean promiscuous;
  protected IFCGIProcessFactory processFactory;

  /* The fast CGI Server process on this computer. Switched off per default. */
  protected IFCGIProcess proc = null;
  private boolean fcgiStarted = false;
  private final Object fcgiStartLock = new Object ();
  protected Exception lastException;

  /**
   * Create a new FCGIConnectionFactory using a FCGIProcessFactory
   * 
   * @param processFactory
   *        the FCGIProcessFactory
   */
  public AbstractFCGIConnectionFactory (final IFCGIProcessFactory processFactory)
  {
    this.processFactory = processFactory;
  }

  /**
   * Start the FastCGI server
   * 
   * @return false if the FastCGI server failed to start.
   */
  public final boolean startServer (final ILogger logger)
  {
    /*
     * Try to start the FastCGI server,
     */
    synchronized (fcgiStartLock)
    {
      if (!fcgiStarted)
      {
        if (canStartFCGI ())
          try
          {
            bind (logger);
          }
          catch (final Exception e)
          {/* ignore */}

        fcgiStarted = true; // mark as started, even if start failed
      }
    }
    return fcgiStarted;
  }

  /**
   * Test the FastCGI server.
   * 
   * @throws FCGIConnectException
   *         thrown if a IOException occured.
   */
  public abstract void test () throws FCGIConnectException;

  protected abstract void waitForDaemon () throws UnknownHostException, InterruptedException;

  protected final void runFcgi (final Map <String, String> env, final String php, final boolean includeJava)
  {
    int c;
    final byte buf[] = new byte [Util.BUF_SIZE];
    try
    {
      final UtilProcess proc = doBind (env, php, includeJava);
      if (proc == null || proc.getInputStream () == null)
        return;
      // / make sure that the wrapper script launcher.sh does not output to
      // stdout
      proc.getInputStream ().close ();
      // proc.OutputStream should be closed in shutdown, see
      // PhpCGIServlet.destroy()
      final InputStream in = proc.getErrorStream ();
      while ((c = in.read (buf)) != -1)
        System.err.write (buf, 0, c);
      try
      {
        in.close ();
      }
      catch (final IOException e)
      {/* ignore */}
    }
    catch (final Exception e)
    {
      lastException = e;
      System.err.println ("Could not start FCGI server: " + e);
    }
  }

  protected abstract UtilProcess doBind (Map <String, String> env, String php, boolean includeJava) throws IOException;

  protected void bind (@SuppressWarnings ("unused") final ILogger logger) throws InterruptedException, IOException
  {
    final Thread t = new UtilThread ("JavaBridgeFastCGIRunner")
    {
      @Override
      public void run ()
      {
        final Map <String, String> env = new HashMap <String, String> (processFactory.getEnvironment ());
        env.put ("PHP_FCGI_CHILDREN", processFactory.getPhpConnectionPoolSize ());
        env.put ("PHP_FCGI_MAX_REQUESTS", processFactory.getPhpMaxRequests ());
        runFcgi (env, processFactory.getPhp (), processFactory.getPhpIncludeJava ());
      }
    };
    t.start ();
    waitForDaemon ();
  }

  private boolean canStartFCGI ()
  {
    return processFactory.canStartFCGI ();
  }

  public void destroy ()
  {
    synchronized (fcgiStartLock)
    {
      fcgiStarted = false;
      if (proc == null)
        return;
      try
      {
        final OutputStream out = proc.getOutputStream ();
        if (out != null)
          out.close ();
      }
      catch (final IOException e)
      {
        Util.printStackTrace (e);
      }
      try
      {
        proc.waitFor ();
      }
      catch (final InterruptedException e)
      {
        // ignore
      }
      proc.destroy ();
      proc = null;
    }
  }

  /**
   * Connect to the FastCGI server and return the connection handle.
   * 
   * @return The FastCGI Channel
   * @throws FCGIConnectException
   *         thrown if a IOException occured.
   */
  public abstract AbstractFCGIConnection connect () throws FCGIConnectException;

  /**
   * For backward compatibility the "JavaBridge" context uses the port 9667
   * (Linux/Unix) or <code>\\.\pipe\JavaBridge@9667</code> (Windogs).
   */
  public void initialize ()
  {
    setDynamicPort ();
  }

  protected abstract void setDynamicPort ();

  protected abstract void setDefaultPort ();

  /**
   * Return a command which may be useful for starting the FastCGI server as a
   * separate command.
   * 
   * @param base
   *        The context directory
   * @param php_fcgi_max_requests
   *        The number of requests, see appropriate servlet option.
   * @return A command string
   */
  public abstract String getFcgiStartCommand (String base, String php_fcgi_max_requests);

  /**
   * Find a free port or pipe name.
   * 
   * @param select
   *        If select is true, the default name should be used.
   */
  public abstract void findFreePort (boolean select);

  /**
   * Create a new ChannelFactory.
   * 
   * @return The concrete ChannelFactory (NP or Socket channel factory).
   */
  public static AbstractFCGIConnectionFactory createChannelFactory (final IFCGIProcessFactory processFactory,
                                                                    final boolean promiscuous)
  {
    if (Util.USE_SH_WRAPPER)
      return new SocketChannelFactory (processFactory, promiscuous);
    return new NPChannelFactory (processFactory);
  }

  @Override
  public abstract String toString ();
}
