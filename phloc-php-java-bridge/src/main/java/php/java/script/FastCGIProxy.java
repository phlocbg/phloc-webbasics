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

package php.java.script;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import php.java.bridge.ILogger;
import php.java.bridge.Util;
import php.java.bridge.code.LauncherUnix;
import php.java.bridge.code.LauncherWindows;
import php.java.bridge.code.LauncherWindows2;
import php.java.bridge.code.LauncherWindows3;
import php.java.bridge.code.LauncherWindows4;
import php.java.bridge.http.AbstractFCGIConnection;
import php.java.bridge.http.AbstractFCGIConnectionFactory;
import php.java.bridge.http.AbstractFCGIIOFactory;
import php.java.bridge.http.AbstractHeaderParser;
import php.java.bridge.http.AbstractOutputStreamFactory;
import php.java.bridge.http.FCGIConnectException;
import php.java.bridge.http.FCGIConnectionPool;
import php.java.bridge.http.FCGIInputStream;
import php.java.bridge.http.FCGIOutputStream;
import php.java.bridge.http.FCGIUtil;
import php.java.bridge.http.IFCGIProcess;
import php.java.bridge.http.IFCGIProcessFactory;

/**
 * This class can be used to run (and to connect to) a FastCGI server.
 * 
 * @author jostb
 * @see php.java.script.servlet.HttpFastCGIProxy
 */

public class FastCGIProxy extends AbstractContinuation implements IFCGIProcessFactory
{
  private static final String PROCESSES = Util.THREAD_POOL_MAX_SIZE; // PROCESSES
                                                                     // must ==
                                                                     // Util.THREAD_POOL_MAX_SIZE
  private static final String MAX_REQUESTS = FCGIUtil.PHP_FCGI_MAX_REQUESTS;
  private static final String CGI_DIR = Util.TMPDIR.getAbsolutePath ();
  private static final boolean PHP_INCLUDE_JAVA = false; // servlet option

  public FastCGIProxy (final Reader reader,
                       final Map <String, String> env,
                       final OutputStream out,
                       final OutputStream err,
                       final AbstractHeaderParser headerParser,
                       final ResultProxy resultProxy,
                       final ILogger logger)
  {
    super (env, out, err, headerParser, resultProxy);
  }

  private AbstractFCGIConnectionFactory channelName;
  static final Map <String, String> PROCESS_ENVIRONMENT = getProcessEnvironment ();

  private static Map <String, String> getProcessEnvironment ()
  {
    return new HashMap <String, String> (Util.COMMON_ENVIRONMENT);
  }

  private final AbstractFCGIIOFactory defaultPoolFactory = new AbstractFCGIIOFactory ()
  {
    @Override
    public InputStream createInputStream ()
    {
      return new FCGIInputStream (FastCGIProxy.this);
    }

    @Override
    public OutputStream createOutputStream ()
    {
      return new FCGIOutputStream ();
    }

    @Override
    public AbstractFCGIConnection connect (final AbstractFCGIConnectionFactory name) throws FCGIConnectException
    {
      return name.connect ();
    }
  };

  private FCGIConnectionPool createConnectionPool (final int children) throws FCGIConnectException
  {
    channelName = AbstractFCGIConnectionFactory.createChannelFactory (this, false);
    channelName.findFreePort (true);
    channelName.initialize ();
    final File cgiOsDir = Util.TMPDIR;
    final File javaIncFile = new File (cgiOsDir, "launcher.sh");
    if (Util.USE_SH_WRAPPER)
    {
      try
      {
        if (!javaIncFile.exists ())
        {
          final byte [] buf = LauncherUnix.bytes;
          final OutputStream out = new FileOutputStream (javaIncFile);
          out.write (buf);
          out.close ();
        }
      }
      catch (final Exception e)
      {
        e.printStackTrace ();
      }
    }
    final File javaProxyFile = new File (cgiOsDir, "launcher.exe");
    if (!Util.USE_SH_WRAPPER)
    {
      try
      {
        if (!javaProxyFile.exists ())
        {
          final byte [] buf = LauncherWindows.bytes;
          final byte [] buf2 = LauncherWindows2.bytes;
          final byte [] buf3 = LauncherWindows3.bytes;
          final byte [] buf4 = LauncherWindows4.bytes;
          final OutputStream out = new FileOutputStream (javaProxyFile);
          out.write (buf);
          out.write (buf2);
          out.write (buf3);
          out.write (buf4);
          out.close ();
        }
      }
      catch (final Exception e)
      {
        e.printStackTrace ();
      }
    }

    // Start the launcher.exe or launcher.sh
    final Map <String, String> map = new HashMap <String, String> (PROCESS_ENVIRONMENT);
    map.put ("PHP_FCGI_CHILDREN", PROCESSES);
    map.put ("PHP_FCGI_MAX_REQUESTS", MAX_REQUESTS);
    channelName.startServer (Util.getLogger ());

    return new FCGIConnectionPool (channelName,
                                   children,
                                   Integer.parseInt (MAX_REQUESTS),
                                   defaultPoolFactory,
                                   Integer.parseInt (FCGIUtil.PHP_FCGI_CONNECTION_POOL_TIMEOUT));
  }

  private static final Object globalCtxLock = new Object ();
  private static FCGIConnectionPool fcgiConnectionPool = null;

  protected void setupFastCGIServer () throws FCGIConnectException
  {
    synchronized (globalCtxLock)
    {
      if (null == fcgiConnectionPool)
      {
        Util.fcgiConnectionPool = fcgiConnectionPool = createConnectionPool (Integer.parseInt (PROCESSES));
      }
    }

  }

  @Override
  protected void doRun () throws IOException, Util.UtilProcess.PhpException
  {
    final byte [] buf = new byte [FCGIUtil.FCGI_BUF_SIZE];
    setupFastCGIServer ();

    FCGIInputStream natIn = null;
    FCGIOutputStream natOut = null;

    FCGIConnectionPool.Connection connection = null;

    try
    {
      connection = fcgiConnectionPool.openConnection ();
      natOut = (FCGIOutputStream) connection.getOutputStream ();
      natIn = (FCGIInputStream) connection.getInputStream ();

      natOut.writeBegin ();
      natOut.writeParams (env);
      natOut.write (FCGIUtil.FCGI_STDIN, FCGIUtil.FCGI_EMPTY_RECORD);
      natOut.close ();
      natOut = null;
      AbstractHeaderParser.parseBody (buf, natIn, new AbstractOutputStreamFactory ()
      {
        @Override
        public OutputStream getOutputStream () throws IOException
        {
          return out;
        }
      }, headerParser);
      natIn.close ();
      natIn = null;
      connection = null;
    }
    catch (final InterruptedException e)
    {
      /* ignore */
    }
    catch (final Throwable t)
    {
      Util.printStackTrace (t);
    }
    finally
    {
      if (connection != null)
        connection.setIsClosed ();
      if (natIn != null)
        try
        {
          natIn.close ();
        }
        catch (final IOException e)
        {}
      if (natOut != null)
        try
        {
          natOut.close ();
        }
        catch (final IOException e)
        {}
    }
  }

  /** required by IFCGIProcessFactory */
  /** {@inheritDoc} */
  public IFCGIProcess createFCGIProcess (final String [] args,
                                         final boolean includeJava,
                                         final File home,
                                         final Map <String, String> env) throws IOException
  {
    return new FCGIProcess (args,
                            includeJava,
                            getCgiDir (),
                            getPearDir (),
                            getWebInfDir (),
                            home,
                            env,
                            getCgiDir (),
                            true,
                            true);
  }

  /** {@inheritDoc} */
  public boolean canStartFCGI ()
  {
    return true;
  }

  /** {@inheritDoc} */
  public String getCgiDir ()
  {
    return CGI_DIR;
  }

  /** {@inheritDoc} */
  public Map <String, String> getEnvironment ()
  {
    return getProcessEnvironment ();
  }

  /** {@inheritDoc} */
  public String getPearDir ()
  {
    return CGI_DIR;
  }

  /** {@inheritDoc} */
  public String getPhp ()
  {
    return null;
  }

  /** {@inheritDoc} */
  public String getPhpConnectionPoolSize ()
  {
    return PROCESSES;
  }

  /** {@inheritDoc} */
  public boolean getPhpIncludeJava ()
  {
    return PHP_INCLUDE_JAVA;
  }

  /** {@inheritDoc} */
  public String getPhpMaxRequests ()
  {
    return MAX_REQUESTS;
  }

  /** {@inheritDoc} */
  public String getWebInfDir ()
  {
    return CGI_DIR;
  }

  /** {@inheritDoc} */
  public void log (final String msg)
  {
    Util.logMessage (msg);
  }
}
