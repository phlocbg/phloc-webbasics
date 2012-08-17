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

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.appbasics.app.io.WebIO;
import com.phloc.appbasics.app.io.WebIOIntIDFactory;
import com.phloc.appbasics.app.io.WebIOResourceProviderChain;
import com.phloc.commons.CGlobal;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.SystemProperties;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.name.ComparatorHasDisplayName;
import com.phloc.commons.name.IHasDisplayName;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.system.EJVMVendor;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.thirdparty.IThirdPartyModule;
import com.phloc.commons.thirdparty.ThirdPartyModuleRegistry;
import com.phloc.commons.utils.ClassPathHelper;
import com.phloc.commons.vminit.VirtualMachineInitializer;
import com.phloc.scopes.web.mgr.WebScopeManager;

/**
 * This class is intended to handle the initial application startup and the
 * final shutdown. It is also responsible for creating the global and session
 * scopes.
 * 
 * @author philip
 */
public class WebAppListener implements ServletContextListener, HttpSessionListener
{
  static
  {
    VirtualMachineInitializer.runInitialization ();
  }

  public static final String INIT_PARAMETER_TRACE = "trace";
  public static final String INIT_PARAMETER_DEBUG = "debug";
  public static final String INIT_PARAMETER_PRODUCTION = "production";
  public static final String INIT_PARAMETER_STORAGE_PATH = "storagePath";

  /** The logger to use. */
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebAppListener.class);

  public WebAppListener ()
  {}

  @OverrideOnDemand
  protected void logStartupInfo (@Nonnull final ServletContext aSC)
  {
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

    if (SystemProperties.getJavaVersion ().startsWith ("1.6.0_14"))
      s_aLogger.warn ("This Java version is bad for development - breakpoints don't work in the debugger!");
  }

  @OverrideOnDemand
  protected void debugClassPath ()
  {
    // List class path elements in trace mode
    if (GlobalDebug.isTraceMode ())
    {
      final List <String> aCP = ClassPathHelper.getAllClassPathEntries ();
      s_aLogger.info ("Class path [" + aCP.size () + " elements]:");
      for (final String sCP : ContainerHelper.getSortedInline (aCP))
        s_aLogger.info ("  " + sCP);
    }
  }

  @OverrideOnDemand
  protected void debugInitParameters (@Nonnull final ServletContext aSC)
  {
    s_aLogger.info ("Servlet context init-parameters:");
    final Enumeration <?> aEnum = aSC.getInitParameterNames ();
    while (aEnum.hasMoreElements ())
    {
      final String sName = (String) aEnum.nextElement ();
      s_aLogger.info ("  " + sName + "=" + aSC.getInitParameter (sName));
    }
  }

  @OverrideOnDemand
  protected void debugThirdpartyModules ()
  {
    // List all third party modules for later evaluation
    final Set <IThirdPartyModule> aModules = ThirdPartyModuleRegistry.getAllRegisteredThirdPartyModules ();
    if (!aModules.isEmpty ())
    {
      s_aLogger.info ("Using the following third party modules:");
      for (final IThirdPartyModule aModule : ContainerHelper.getSorted (aModules,
                                                                        new ComparatorHasDisplayName <IHasDisplayName> (null)))
        if (!aModule.isOptional ())
        {
          String sMsg = "  " + aModule.getDisplayName ();
          if (aModule.getVersion () != null)
            sMsg += ' ' + aModule.getVersion ().getAsString (true);
          sMsg += " licensed under " + aModule.getLicense ().getDisplayName ();
          if (aModule.getLicense ().getVersion () != null)
            sMsg += ' ' + aModule.getLicense ().getVersion ().getAsString ();
          s_aLogger.info (sMsg);
        }
    }
  }

  @OverrideOnDemand
  protected void debugJMX ()
  {
    if (SystemProperties.getPropertyValue ("com.sun.management.jmxremote") != null)
    {
      final String sPort = SystemProperties.getPropertyValue ("com.sun.management.jmxremote.port");
      final String sSSL = SystemProperties.getPropertyValue ("com.sun.management.jmxremote.ssl");
      final String sAuthenticate = SystemProperties.getPropertyValue ("com.sun.management.jmxremote.authenticate");
      final String sPasswordFile = SystemProperties.getPropertyValue ("com.sun.management.jmxremote.password.file");
      final String sAccessFile = SystemProperties.getPropertyValue ("com.sun.management.jmxremote.access.file");
      s_aLogger.info ("Remote JMX is enabled!");
      if (sPort != null)
        s_aLogger.info ("  Port=" + sPort);
      if (sSSL != null)
        s_aLogger.info ("  SSL enabled=" + sSSL);
      if (sAuthenticate != null)
        s_aLogger.info ("  Authenticate=" + sAuthenticate);
      if (sPasswordFile != null)
        s_aLogger.info ("  Password file=" + sPasswordFile);
      if (sAccessFile != null)
        s_aLogger.info ("  Access file=" + sAccessFile);
    }
  }

  /**
   * Callback before init. By default some relevant debug information is emitted
   * 
   * @param aSC
   *        ServletContext
   */
  @OverrideOnDemand
  protected void beforeContextInitialized (@Nonnull final ServletContext aSC)
  {
    // Some startup debugging
    debugClassPath ();
    debugInitParameters (aSC);
    debugThirdpartyModules ();
    debugJMX ();
  }

  /**
   * Callback after init
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
    logStartupInfo (aSC);

    // set global debug/trace mode
    final boolean bTraceMode = StringParser.parseBool (aSC.getInitParameter (INIT_PARAMETER_TRACE));
    final boolean bDebugMode = StringParser.parseBool (aSC.getInitParameter (INIT_PARAMETER_DEBUG));
    final boolean bProductionMode = StringParser.parseBool (aSC.getInitParameter (INIT_PARAMETER_PRODUCTION));
    GlobalDebug.setTraceModeDirect (bTraceMode);
    GlobalDebug.setDebugModeDirect (bDebugMode);
    GlobalDebug.setProductionModeDirect (bProductionMode);

    // Call callback
    beforeContextInitialized (aSC);

    // begin global context
    WebScopeManager.onGlobalBegin (aSC);

    // Get base storage path
    String sBasePath = aSC.getInitParameter (INIT_PARAMETER_STORAGE_PATH);
    if (StringHelper.hasNoText (sBasePath))
    {
      s_aLogger.info ("No servlet context init-parameter '" +
                      INIT_PARAMETER_STORAGE_PATH +
                      "' found! Using the default.");
      sBasePath = aSC.getRealPath (".");
    }
    final File aBasePath = new File (sBasePath);
    WebFileIO.initBasePath (aBasePath);
    WebIO.init (new WebIOResourceProviderChain (aBasePath));

    // Set persistent ID provider: file based
    GlobalIDFactory.setPersistentIntIDFactory (new WebIOIntIDFactory ("persistent_id.dat"));

    if (s_aLogger.isInfoEnabled ())
      s_aLogger.info ("Servlet context '" + aSC.getServletContextName () + "' was initialized");

    // Callback
    afterContextInitialized (aSC);
  }

  /**
   * before destroy
   * 
   * @param aSC
   *        the servlet context in destruction
   */
  @OverrideOnDemand
  protected void beforeContextDestroyed (@Nonnull final ServletContext aSC)
  {}

  /**
   * after destroy
   * 
   * @param aSC
   *        the servlet context in destruction
   */
  @OverrideOnDemand
  protected void afterContextDestroyed (@Nonnull final ServletContext aSC)
  {}

  public final void contextDestroyed (@Nonnull final ServletContextEvent aSCE)
  {
    final ServletContext aSC = aSCE.getServletContext ();

    // Callback before
    beforeContextDestroyed (aSC);

    if (s_aLogger.isInfoEnabled ())
      s_aLogger.info ("Servlet context '" + aSC.getServletContextName () + "' is being destroyed");

    // Shutdown global scope and destroy all singletons
    WebScopeManager.onGlobalEnd ();

    // Reset base path - mainley for testing
    WebFileIO.resetBasePath ();

    // Clear resource bundle cache - avoid potential class loading issues
    DefaultTextResolver.clearCache ();

    // Callback after
    afterContextDestroyed (aSC);
  }

  public final void sessionCreated (@Nonnull final HttpSessionEvent aSessionEvent)
  {
    // Create the SessionScope
    final HttpSession aHttpSession = aSessionEvent.getSession ();
    WebScopeManager.onSessionBegin (aHttpSession);
  }

  public final void sessionDestroyed (@Nonnull final HttpSessionEvent aSessionEvent)
  {
    // Destroy the SessionScope
    final HttpSession aHttpSession = aSessionEvent.getSession ();
    WebScopeManager.onSessionEnd (aHttpSession);
  }
}
