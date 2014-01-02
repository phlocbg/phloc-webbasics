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

package php.java.servlet;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import php.java.bridge.CBridge;
import php.java.bridge.ILogger;
import php.java.bridge.ThreadPool;
import php.java.bridge.Util;
import php.java.bridge.code.JavaInc;
import php.java.bridge.code.JavaProxy;
import php.java.bridge.code.LauncherUnix;
import php.java.bridge.code.LauncherWindows;
import php.java.bridge.code.LauncherWindows2;
import php.java.bridge.code.LauncherWindows3;
import php.java.bridge.code.LauncherWindows4;
import php.java.bridge.code.PhpDebuggerPHP;
import php.java.bridge.http.AbstractFCGIConnection;
import php.java.bridge.http.AbstractFCGIConnectionFactory;
import php.java.bridge.http.AbstractFCGIIOFactory;
import php.java.bridge.http.ContextServer;
import php.java.bridge.http.FCGIConnectException;
import php.java.bridge.http.FCGIConnectionPool;
import php.java.bridge.http.FCGIInputStream;
import php.java.bridge.http.FCGIOutputStream;
import php.java.bridge.http.FCGIUtil;
import php.java.bridge.http.IFCGIProcess;
import php.java.bridge.http.IFCGIProcessFactory;
import php.java.servlet.fastcgi.FCGIProcess;

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
 * Register the PHP/Java Bridge when the web context starts. Used by
 * java_context()->onShutdown(proc). The WEB-INF/web.xml contains a listener
 * attribute: <blockquote> <code>
 * &lt;listener&gt;
 * &nbsp;&nbsp;&lt;listener-class&gt;php.java.servlet.ContextLoaderListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * </code> </blockquote>
 */
public final class ContextLoaderListener implements ServletContextListener, IFCGIProcessFactory
{
  private final List <Closeable> closeables = new ArrayList <Closeable> ();

  public static final String PEAR_DIR = "/WEB-INF/pear";
  public static final String CGI_DIR = "/WEB-INF/cgi";
  public static final String WEB_INF_DIR = "/WEB-INF";

  private ServletContext context;

  private AbstractFCGIConnectionFactory channelName;

  private String php;
  /** default: true. Switched off when fcgi is not configured */
  @SuppressWarnings ("unused")
  private boolean fcgiIsConfigured;

  private boolean canStartFCGI = false;

  private String php_fcgi_connection_pool_size = FCGIUtil.PHP_FCGI_CONNECTION_POOL_SIZE;
  @SuppressWarnings ("unused")
  private String php_fcgi_connection_pool_timeout = FCGIUtil.PHP_FCGI_CONNECTION_POOL_TIMEOUT;
  private boolean php_include_java;
  private int php_fcgi_connection_pool_size_number = Integer.parseInt (FCGIUtil.PHP_FCGI_CONNECTION_POOL_SIZE);
  private long php_fcgi_connection_pool_timeout_number = Long.parseLong (FCGIUtil.PHP_FCGI_CONNECTION_POOL_TIMEOUT);
  private String php_fcgi_max_requests = FCGIUtil.PHP_FCGI_MAX_REQUESTS;
  private int php_fcgi_max_requests_number = Integer.parseInt (FCGIUtil.PHP_FCGI_MAX_REQUESTS);

  private ILogger logger;

  private FCGIConnectionPool fcgiConnectionPool;

  // workaround for a bug in jboss server, which uses the log4j port 4445 for
  // its internal purposes(!)
  private boolean isJBoss;
  private ContextServer contextServer; // shared with FastCGIServlet

  private ThreadPool fcgiThreadPool;

  /** The key used to store the ContextLoaderListener in the servlet context */
  public static final String CONTEXT_LOADER_LISTENER = ContextLoaderListener.class.getName () + ".ROOT";

  /**
   * Only for internal use
   * 
   * @param ctx
   *        The servlet context
   */
  public void destroyCloseables (final ServletContext ctx)
  {
    final List <Closeable> list = closeables;
    if (list == null)
      return;

    try
    {
      for (final Closeable c : list)
      {
        try
        {
          c.close ();
        }
        catch (final Exception e)
        {
          e.printStackTrace ();
        }
      }
    }
    finally
    {
      closeables.clear ();
    }
  }

  /** {@inheritDoc} */
  public void contextDestroyed (final ServletContextEvent event)
  {
    final ServletContext ctx = event.getServletContext ();
    try
    {
      destroyCloseables (ctx);
    }
    catch (final Exception e)
    {
      e.printStackTrace ();
    }
    if (fcgiConnectionPool != null)
      fcgiConnectionPool.destroy ();
    if (fcgiThreadPool != null)
      fcgiThreadPool.destroy ();

    if (channelName != null)
      channelName.destroy ();
    fcgiConnectionPool = null;

    if (contextServer != null)
      contextServer.destroy ();

    Util.destroy ();
  }

  /** {@inheritDoc} */
  public void contextInitialized (final ServletContextEvent event)
  {
    final ServletContext ctx = event.getServletContext ();
    ctx.setAttribute (CONTEXT_LOADER_LISTENER, this);
    this.context = ctx;
    init ();

    boolean promiscuous = true;
    try
    {
      String value = ctx.getInitParameter ("promiscuous");
      if (value == null)
        value = "";
      value = value.trim ();
      value = value.toLowerCase ();

      if (value.equals ("off") || value.equals ("false"))
        promiscuous = false;
    }
    catch (final Exception t)
    {
      t.printStackTrace ();
    }

    final String name = context.getServerInfo ();
    if (name != null && (name.startsWith ("JBoss")))
      isJBoss = true;
    logger = new Util.UtilLogger (!isJBoss, new Logger ());
    Util.setDefaultLogger (logger);

    String servletContextName = ServletUtil.getRealPath (context, "");
    if (servletContextName == null)
      servletContextName = "";
    contextServer = new ContextServer (servletContextName, promiscuous);

    channelName = AbstractFCGIConnectionFactory.createChannelFactory (this, promiscuous);
    channelName.findFreePort (canStartFCGI);

    try
    {
      fcgiConnectionPool = createConnectionPool (php_fcgi_connection_pool_size_number);
    }
    catch (final FCGIConnectException e)
    {
      Util.printStackTrace (e);
    }
    fcgiThreadPool = createThreadPool (php_fcgi_connection_pool_size_number);
  }

  private void init ()
  {
    String value;
    try
    {
      value = context.getInitParameter ("prefer_system_php_exec");
      if (value == null)
        value = "";
      value = value.trim ().toLowerCase ();
      if (value.equals ("on") || value.equals ("true"))
        preferSystemPhp = true;
    }
    catch (final Throwable t)
    {
      t.printStackTrace ();
    }
    String val = null;
    try
    {
      val = context.getInitParameter ("php_fcgi_children");
      if (val == null)
        val = context.getInitParameter ("PHP_FCGI_CHILDREN");
      if (val == null)
        val = System.getProperty ("php.java.bridge.php_fcgi_children");
      if (val == null)
        val = context.getInitParameter ("php_fcgi_connection_pool_size");
      if (val == null)
        val = System.getProperty ("php.java.bridge.php_fcgi_connection_pool_size");
      if (val != null)
        php_fcgi_connection_pool_size_number = Integer.parseInt (val);
    }
    catch (final Throwable t)
    {/* ignore */}
    if (val != null)
      php_fcgi_connection_pool_size = val;

    val = null;
    try
    {
      val = context.getInitParameter ("php_fcgi_connection_pool_timeout");
      if (val == null)
        val = System.getProperty ("php.java.bridge.php_fcgi_connection_pool_timeout");
      if (val != null)
        php_fcgi_connection_pool_timeout_number = Integer.parseInt (val);
    }
    catch (final Throwable t)
    {/* ignore */}
    if (val != null)
      php_fcgi_connection_pool_timeout = val;

    val = null;
    php_include_java = false;
    try
    {
      val = context.getInitParameter ("php_include_java");
      if (val == null)
        val = context.getInitParameter ("PHP_INCLUDE_JAVA");
      if (val == null)
        val = System.getProperty ("php.java.bridge.php_include_java");
      if (val != null && (val.equalsIgnoreCase ("on") || val.equalsIgnoreCase ("true")))
        php_include_java = true;
    }
    catch (final Throwable t)
    {/* ignore */}

    val = null;
    try
    {
      val = context.getInitParameter ("php_fcgi_max_requests");
      if (val == null)
        val = System.getProperty ("php.java.bridge.php_fcgi_max_requests");
      if (val != null)
      {
        php_fcgi_max_requests_number = Integer.parseInt (val);
        php_fcgi_max_requests = val;
      }
    }
    catch (final Throwable t)
    {/* ignore */}
    checkCgiBinary ();
    createPhpFiles ();
  }

  private void checkCgiBinary ()
  {
    String value;
    if (php == null)
    {
      try
      {
        value = context.getInitParameter ("php_exec");
        if (value == null || value.trim ().length () == 0)
        {
          value = "php-cgi";
          phpTryOtherLocations = true;
        }
        final File f = new File (value);
        if (!f.isAbsolute ())
        {
          value = ServletUtil.getRealPath (context, CGI_DIR) + File.separator + value;
        }
        php = value;
      }
      catch (final Throwable t)
      {
        Util.printStackTrace (t);
      }
    }
    fcgiIsConfigured = true;
    try
    {
      value = context.getInitParameter ("use_fast_cgi");
      if (value == null)
        try
        {
          value = System.getProperty ("php.java.bridge.use_fast_cgi");
        }
        catch (final Exception e)
        {/* ignore */}
      if ("false".equalsIgnoreCase (value) || "off".equalsIgnoreCase (value))
        fcgiIsConfigured = false;
      else
      {
        value = context.getInitParameter ("use_fast_cgi");
        if (value == null)
          value = "auto";
        value = value.trim ();
        value = value.toLowerCase ();
        final boolean autostart = value.startsWith ("auto");
        final boolean notAvailable = value.equals ("false") || value.equals ("off");
        if (notAvailable)
          fcgiIsConfigured = false;
        if (autostart)
          canStartFCGI = true;
      }
    }
    catch (final Throwable t)
    {
      Util.printStackTrace (t);
    }
  }

  private boolean useSystemPhp (final File f)
  {

    // path hard coded in web.xml
    if (!phpTryOtherLocations)
      return true;

    // no local php exists
    if (!f.exists ())
      return true;

    // local exists
    if (!preferSystemPhp)
      return false;

    // check default locations for preferred system php
    for (final String element : Util.DEFAULT_CGI_LOCATIONS)
    {
      final File location = new File (element);
      if (location.exists ())
        return true;
    }

    return false;
  }

  static final Map <String, String> PROCESS_ENVIRONMENT = getProcessEnvironment ();

  private static void updateProcessEnvironment (final File conf)
  {
    try
    {
      PROCESS_ENVIRONMENT.put ("PHP_INI_SCAN_DIR", conf.getCanonicalPath ());
    }
    catch (final IOException e)
    {
      e.printStackTrace ();
      PROCESS_ENVIRONMENT.put ("PHP_INI_SCAN_DIR", conf.getAbsolutePath ());
    }
  }

  private static Map <String, String> getProcessEnvironment ()
  {
    return new HashMap <String, String> (Util.COMMON_ENVIRONMENT);
  }

  private void createPhpFiles ()
  {
    final String javaDir = ServletUtil.getRealPath (context, "java");
    if (javaDir != null)
    {
      final File javaDirFile = new File (javaDir);
      try
      {
        if (!javaDirFile.exists ())
        {
          javaDirFile.mkdir ();
        }
      }
      catch (final Exception e)
      {/* ignore */}

      final File javaIncFile = new File (javaDir, "Java.inc");
      try
      {
        if (!javaIncFile.exists ())
        {
          final byte [] buf = JavaInc.bytes;
          final OutputStream out = new FileOutputStream (javaIncFile);
          out.write (buf);
          out.close ();
        }
      }
      catch (final Exception e)
      {
        e.printStackTrace ();
      }

      // no longer part of the PHP/Java Bridge
      if (false)
      {
        final File phpDebuggerFile = new File (javaDir, "PHPDebugger.php");
        try
        {
          if (!phpDebuggerFile.exists ())
          {
            final byte [] buf = PhpDebuggerPHP.bytes;
            final OutputStream out = new FileOutputStream (phpDebuggerFile);
            out.write (buf);
            out.close ();
          }
        }
        catch (final Exception e)
        {
          e.printStackTrace ();
        }
      }

      final File javaProxyFile = new File (javaDir, "JavaProxy.php");
      try
      {
        if (!javaProxyFile.exists ())
        {
          final byte [] buf = JavaProxy.bytes;
          final OutputStream out = new FileOutputStream (javaProxyFile);
          out.write (buf);
          out.close ();
        }
      }
      catch (final Exception e)
      {
        e.printStackTrace ();
      }
    }
    final String pearDir = ServletUtil.getRealPath (context, PEAR_DIR);
    if (pearDir != null)
    {
      final File pearDirFile = new File (pearDir);
      try
      {
        if (!pearDirFile.exists ())
        {
          pearDirFile.mkdir ();
        }
      }
      catch (final Exception e)
      {
        e.printStackTrace ();
      }
    }
    final String cgiDir = ServletUtil.getRealPath (context, CGI_DIR);
    final File cgiOsDir = new File (cgiDir, Util.osArch + "-" + Util.osName);
    final File conf = new File (cgiOsDir, "conf.d");
    final File ext = new File (cgiOsDir, "ext");
    final File cgiDirFile = new File (cgiDir);
    try
    {
      if (!cgiDirFile.exists ())
      {
        cgiDirFile.mkdirs ();
      }
    }
    catch (final Exception e)
    {
      e.printStackTrace ();
    }

    try
    {
      if (!conf.exists ())
      {
        conf.mkdirs ();
      }
    }
    catch (final Exception e)
    {
      e.printStackTrace ();
    }

    try
    {
      if (!ext.exists ())
      {
        ext.mkdir ();
      }
    }
    catch (final Exception e)
    {
      e.printStackTrace ();
    }

    final File javaIncFile = new File (cgiOsDir, CBridge.LAUNCHER_SH);
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
    final File javaProxyFile = new File (cgiOsDir, CBridge.LAUNCHER_EXE);
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
    boolean exeExists = true;
    if (Util.USE_SH_WRAPPER)
    {
      try
      {
        final File phpCgi = new File (cgiOsDir, "php-cgi");
        if (!useSystemPhp (phpCgi))
        {
          updateProcessEnvironment (conf);
          final File wrapper = new File (cgiOsDir, "php-cgi.sh");
          if (!wrapper.exists ())
          {
            final byte [] data = ("#!/bin/sh\nchmod +x ./" +
                                  Util.osArch +
                                  "-" +
                                  Util.osName +
                                  "/php-cgi\n" +
                                  "exec ./" +
                                  Util.osArch +
                                  "-" +
                                  Util.osName +
                                  "/php-cgi -c ./" +
                                  Util.osArch +
                                  "-" +
                                  Util.osName + "/php-cgi.ini \"$@\"").getBytes (Util.ISO88591);
            final OutputStream out = new FileOutputStream (wrapper);
            out.write (data);
            out.close ();
          }
          final File ini = new File (cgiOsDir, "php-cgi.ini");
          if (!ini.exists ())
          {
            final byte [] data = (";; -*- mode: Scheme; tab-width:4 -*-\n;; A simple php.ini\n" +
                                  ";; DO NOT EDIT THIS FILE!\n" +
                                  ";; Add your configuration files to the " +
                                  conf +
                                  " instead.\n" +
                                  ";; PHP extensions go to " +
                                  ext +
                                  ". Please see phpinfo() for ABI version details.\n" +
                                  "extension_dir=\"" +
                                  ext +
                                  "\"\n" +
                                  "include_path=\"" +
                                  pearDir + ":/usr/share/pear:.\"\n").getBytes (Util.ISO88591);
            final OutputStream out = new FileOutputStream (ini);
            out.write (data);
            out.close ();
          }
        }
        else
        {
          exeExists = false;
          final File readme = new File (cgiOsDir, "php-cgi.MISSING.README.txt");
          if (!readme.exists ())
          {
            final byte [] data = "You can add \"php-cgi\" to this directory and re-deploy your web application.\n".getBytes (Util.ASCII);
            final OutputStream out = new FileOutputStream (readme);
            out.write (data);
            out.close ();
          }
        }
      }
      catch (final Exception e)
      {
        e.printStackTrace ();
      }
    }
    else
    {
      try
      {
        final File phpCgi = new File (cgiOsDir, "php-cgi.exe");
        if (!useSystemPhp (phpCgi))
        {
          updateProcessEnvironment (conf);
          final File ini = new File (cgiOsDir, "php.ini");
          if (!ini.exists ())
          {
            final byte [] data = (";; -*- mode: Scheme; tab-width:4 -*-\r\n;; A simple php.ini\r\n" +
                                  ";; DO NOT EDIT THIS FILE!\r\n" +
                                  ";; Add your configuration files to the " +
                                  conf +
                                  " instead.\r\n" +
                                  ";; PHP extensions go to " +
                                  ext +
                                  ". Please see phpinfo() for ABI version details.\r\n" +
                                  "extension_dir=\"" +
                                  ext +
                                  "\"\r\n" +
                                  "include_path=\"" +
                                  pearDir + ";.\"\r\n").getBytes (Util.ISO88591);
            final OutputStream out = new FileOutputStream (ini);
            out.write (data);
            out.close ();
          }
        }
        else
        {
          exeExists = false;
          final File readme = new File (cgiOsDir, "php-cgi.exe.MISSING.README.txt");
          if (!readme.exists ())
          {
            final byte [] data = "You can add \"php-cgi.exe\" to this directory and re-deploy your web application.\r\n".getBytes (Util.ASCII);
            final OutputStream out = new FileOutputStream (readme);
            out.write (data);
            out.close ();
          }
        }
      }
      catch (final Exception e)
      {
        e.printStackTrace ();
      }
    }

    final File tmpl = new File (conf, "mysql.ini");
    if (exeExists && !tmpl.exists ())
    {
      String str;
      if (Util.USE_SH_WRAPPER)
      {
        str = ";; -*- mode: Scheme; tab-width:4 -*-\n"
              + ";; Example extension.ini file: mysql.ini.\n"
              + ";; Copy the correct version (see phpinfo()) of the PHP extension \"mysql.so\" to the ./../ext directory and uncomment the following line\n"
              + "; extension = mysql.so\n";
      }
      else
      {
        str = ";; -*- mode: Scheme; tab-width:4 -*-\r\n"
              + ";; Example extension.ini file: mysql.ini.\r\n"
              + ";; Copy the correct version (see phpinfo()) of the PHP extension \"php_mysql.dll\" to the .\\..\\ext directory and uncomment the following line\r\n"
              + "; extension = php_mysql.dll\r\n";
      }
      final byte [] data = str.getBytes (Util.ASCII);
      try
      {
        final OutputStream out = new FileOutputStream (tmpl);
        out.write (data);
        out.close ();
      }
      catch (final Exception e)
      {
        e.printStackTrace ();
      }
    }
  }

  private final AbstractFCGIIOFactory defaultPoolFactory = new AbstractFCGIIOFactory ()
  {
    @Override
    public InputStream createInputStream ()
    {
      return new FCGIInputStream (ContextLoaderListener.this);
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
    channelName.initialize ();

    // Start the launcher.exe or launcher.sh
    channelName.startServer (logger);
    return new FCGIConnectionPool (channelName,
                                   children,
                                   php_fcgi_max_requests_number,
                                   defaultPoolFactory,
                                   php_fcgi_connection_pool_timeout_number);
  }

  private ThreadPool createThreadPool (final int children)
  {
    return new ThreadPool ("JavaBridgeServletScriptEngineProxy", children);
  }

  public ThreadPool getThreadPool ()
  {
    return fcgiThreadPool;
  }

  public ContextServer getContextServer ()
  {
    return contextServer;
  }

  public ILogger getLogger ()
  {
    return logger;
  }

  public AbstractFCGIConnectionFactory getChannelName ()
  {
    return channelName;
  }

  public List <Closeable> getCloseables ()
  {
    return closeables;
  }

  public FCGIConnectionPool getConnectionPool ()
  {
    return fcgiConnectionPool;
  }

  public static ContextLoaderListener getContextLoaderListener (final ServletContext ctx)
  {
    return (ContextLoaderListener) ctx.getAttribute (ContextLoaderListener.CONTEXT_LOADER_LISTENER);
  }

  /** required by IFCGIProcessFactory */
  protected boolean preferSystemPhp = false; // prefer /usr/bin/php-cgi over
                                             // WEB-INF/cgi/php-cgi?
  protected boolean phpTryOtherLocations = false;

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
                            phpTryOtherLocations,
                            preferSystemPhp);
  }

  /** {@inheritDoc} */
  public String getPhpConnectionPoolSize ()
  {
    return php_fcgi_connection_pool_size;
  }

  /** {@inheritDoc} */
  public String getPhpMaxRequests ()
  {
    return php_fcgi_max_requests;
  }

  /** {@inheritDoc} */
  public String getPhp ()
  {
    return php;
  }

  /** {@inheritDoc} */
  public boolean getPhpIncludeJava ()
  {
    return php_include_java;
  }

  /** {@inheritDoc} */
  public Map <String, String> getEnvironment ()
  {
    return getProcessEnvironment ();
  }

  /** {@inheritDoc} */
  public boolean canStartFCGI ()
  {
    return canStartFCGI;
  }

  private String cgiDir;

  /** {@inheritDoc} */
  public String getCgiDir ()
  {
    if (cgiDir != null)
      return cgiDir;
    return cgiDir = ServletUtil.getRealPath (context, CGI_DIR);
  }

  private String pearDir;

  /** {@inheritDoc} */
  public String getPearDir ()
  {
    if (pearDir != null)
      return pearDir;
    return pearDir = ServletUtil.getRealPath (context, PEAR_DIR);
  }

  private String webInfDir;

  /** {@inheritDoc} */
  public String getWebInfDir ()
  {
    if (webInfDir != null)
      return webInfDir;
    return webInfDir = ServletUtil.getRealPath (context, WEB_INF_DIR);
  }

  /** {@inheritDoc} */
  public void log (final String msg)
  {
    logger.log (ILogger.INFO, msg);
  }
}
