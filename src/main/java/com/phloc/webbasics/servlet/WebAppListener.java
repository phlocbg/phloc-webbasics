/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.servlet;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.SystemProperties;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.idfactory.FileIntIDFactory;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.system.EJVMVendor;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.web.WebFileIO;

/**
 * This class is intended to handle the initial application startup and the
 * final shutdown. It is also responsible for creating the global and session
 * scopes.
 * 
 * @author philip
 */
public class WebAppListener implements ServletContextListener, HttpSessionListener
{
  public static final String INIT_PARAMETER_TRACE = "trace";
  public static final String INIT_PARAMETER_DEBUG = "debug";
  public static final String INIT_PARAMETER_PRODUCTION = "production";
  public static final String INIT_PARAMETER_STORAGE_PATH = "storagePath";

  /** The logger to use. */
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebAppListener.class);

  public WebAppListener ()
  {}

  /**
   * before init
   * 
   * @param aSC
   *        ServletContext
   */
  @OverrideOnDemand
  protected void onContextInitialized (@Nonnull final ServletContext aSC)
  {}

  /**
   * after init
   * 
   * @param aSC
   *        ServletContext
   */
  @OverrideOnDemand
  protected void afterContextInitialized (@Nonnull final ServletContext aSC)
  {}

  public final void contextInitialized (@Nonnull final ServletContextEvent aSCE)
  {
    final ServletContext aSC = aSCE.getServletContext ();

    // Print Java and Server (e.g. Tomcat) info
    s_aLogger.info ("Java " +
                    SystemProperties.getJavaVersion () +
                    " running '" +
                    aSC.getServletContextName () +
                    "' on " +
                    aSC.getServerInfo () +
                    " with " +
                    (Runtime.getRuntime ().maxMemory () / CGlobal.BYTES_PER_MEGABYTE) +
                    "MB max RAM and Servlet API " +
                    aSC.getMajorVersion () +
                    "." +
                    aSC.getMinorVersion ());

    // Tell them to use the server VM if possible:
    final EJVMVendor eJVMVendor = EJVMVendor.getCurrentVendor ();
    if (eJVMVendor.isSun () && eJVMVendor != EJVMVendor.SUN_SERVER)
      s_aLogger.warn ("Consider using the Sun Server Runtime by specifiying '-server' on the commandline!");

    // set global debug/trace mode
    final boolean bTraceMode = Boolean.parseBoolean (aSC.getInitParameter (INIT_PARAMETER_TRACE));
    final boolean bDebugMode = Boolean.parseBoolean (aSC.getInitParameter (INIT_PARAMETER_DEBUG));
    final boolean bProductionMode = Boolean.parseBoolean (aSC.getInitParameter (INIT_PARAMETER_PRODUCTION));
    GlobalDebug.setTraceModeDirect (bTraceMode);
    GlobalDebug.setDebugModeDirect (bDebugMode);
    GlobalDebug.setProductionModeDirect (bProductionMode);

    onContextInitialized (aSC);

    // begin global context
    WebScopeManager.onGlobalBegin (aSC);

    // Get base path!
    {
      String sBasePath = aSC.getInitParameter (INIT_PARAMETER_STORAGE_PATH);
      if (StringHelper.hasNoText (sBasePath))
      {
        s_aLogger.info ("No init-parameter '" + INIT_PARAMETER_STORAGE_PATH + "' found!");
        sBasePath = aSC.getRealPath (".");
      }
      WebFileIO.initBasePath (sBasePath);
    }

    // Set persistent ID provider: file based
    GlobalIDFactory.setPersistentIntIDFactory (new FileIntIDFactory (WebFileIO.getFile ("persistent_id.dat")));

    if (s_aLogger.isInfoEnabled ())
      s_aLogger.info ("Servlet context '" + aSC.getServletContextName () + "' has been initialized");

    afterContextInitialized (aSC);
  }

  public final void contextDestroyed (@Nonnull final ServletContextEvent aSCE)
  {
    // Shutdown global scope
    WebScopeManager.onGlobalEnd ();
  }

  public final void sessionCreated (@Nonnull final HttpSessionEvent aSE)
  {
    // Create the SessionScope
    WebScopeManager.onSessionBegin (aSE.getSession ());
  }

  public final void sessionDestroyed (@Nonnull final HttpSessionEvent aSE)
  {
    // Destroy the SessionScope
    WebScopeManager.onSessionEnd (aSE.getSession ());
  }
}
