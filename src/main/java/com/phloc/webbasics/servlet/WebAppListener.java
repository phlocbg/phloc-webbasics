package com.phloc.webbasics.servlet;

import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.SystemProperties;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.idfactory.FileIntIDFactory;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.lang.ServiceLoaderBackport;
import com.phloc.commons.system.EJVMVendor;
import com.phloc.webbasics.app.scope.GlobalScope;
import com.phloc.webbasics.app.scope.ScopeManager;
import com.phloc.webbasics.spi.IApplicationStartupListenerSPI;

/**
 * This class is intended to handle the initial application startup and the
 * final shutdown. It is also responsible for creating the global and session
 * scopes.
 * 
 * @author philip
 */
public final class WebAppListener implements ServletContextListener {
  public static final String INIT_PARAMETER_TRACE = "trace";
  public static final String INIT_PARAMETER_DEBUG = "debug";
  public static final String INIT_PARAMETER_PRODUCTION = "production";

  /** The logger to use. */
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebAppListener.class);

  private final List <IApplicationStartupListenerSPI> m_aStartupListeners;

  public WebAppListener () {
    m_aStartupListeners = ContainerHelper.newList (ServiceLoaderBackport.load (IApplicationStartupListenerSPI.class));
  }

  public void contextInitialized (@Nonnull final ServletContextEvent aSCE) {
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

    // We're having problems running with a security manager!
    final SecurityManager aSecurityManager = System.getSecurityManager ();
    if (aSecurityManager != null)
      s_aLogger.warn ("A security manager is present and we don't support it: " + aSecurityManager);

    // set global debug/trace mode
    final boolean bTraceMode = Boolean.parseBoolean (aSC.getInitParameter (INIT_PARAMETER_TRACE));
    final boolean bDebugMode = Boolean.parseBoolean (aSC.getInitParameter (INIT_PARAMETER_DEBUG));
    final boolean bProductionMode = Boolean.parseBoolean (aSC.getInitParameter (INIT_PARAMETER_PRODUCTION));
    GlobalDebug.setTraceModeDirect (bTraceMode);
    GlobalDebug.setDebugModeDirect (bDebugMode);
    GlobalDebug.setProductionModeDirect (bProductionMode);

    // begin global context
    ScopeManager.onGlobalBegin (new GlobalScope (aSC));

    // Save real path!
    WebFileIO.initBaseRealPath (aSC.getRealPath ("."));

    // Set persistent ID provider: file based
    GlobalIDFactory.setPersistentIntIDFactory (new FileIntIDFactory (WebFileIO.getRegistryFile ("persistent_id.dat")));

    // Invoke all startup listeners
    for (final IApplicationStartupListenerSPI aStartupListener : m_aStartupListeners)
      try {
        aStartupListener.onStartup ();
        if (s_aLogger.isDebugEnabled ())
          s_aLogger.debug ("Successfully invoked startup listener " + aStartupListener);
      }
      catch (final Throwable t) {
        s_aLogger.error ("Failed to invoke onStartup on " + aStartupListener, t);
        throw new IllegalStateException ("Failed to invoke onStartup on " + aStartupListener, t);
      }

    if (s_aLogger.isInfoEnabled ())
      s_aLogger.info ("Servlet context '" + aSC.getServletContextName () + "' has been initialized");
  }

  public void contextDestroyed (@Nonnull final ServletContextEvent aSCE) {}
}
