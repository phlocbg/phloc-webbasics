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
package com.phloc.sysmon.standalone;

import java.io.File;

import javax.annotation.concurrent.Immutable;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.SystemProperties;

/**
 * This file is generated - do NOT edit!
 * 
 * @author com.phloc.pdaf.creator.webdescriptor.ExportWebAppAsJetty7
 */
@Immutable
public final class RunInJettySYSMON
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (RunInJettySYSMON.class);
  private static final String RESOURCE_PREFIX = "target/webapp-classes";

  public static void main (final String... args) throws Exception
  {
    if (System.getSecurityManager () != null)
      throw new IllegalStateException ("Security Manager is set but not supported - aborting!");

    System.setProperty ("org.eclipse.jetty.server.Request.maxFormContentSize", "-1");

    // Create main server
    final Server aServer = new Server ();
    // Create connector on Port 8080
    final Connector aConnector = new SelectChannelConnector ();
    aConnector.setPort (8080);
    aConnector.setMaxIdleTime (30000);
    aConnector.setStatsOn (true);
    aServer.setConnectors (new Connector [] { aConnector });

    final WebAppContext aWebAppCtx = new WebAppContext ();
    aWebAppCtx.setDescriptor (RESOURCE_PREFIX + "/WEB-INF/web.xml");
    aWebAppCtx.setResourceBase (RESOURCE_PREFIX);
    aWebAppCtx.setContextPath ("/");
    aWebAppCtx.setTempDirectory (new File (SystemProperties.getTmpDir () + '/' + RunInJettySYSMON.class.getName ()));
    aWebAppCtx.setParentLoaderPriority (true);
    aWebAppCtx.setThrowUnavailableOnStartupException (true);
    aServer.setHandler (aWebAppCtx);
    final ServletContextHandler aCtx = aWebAppCtx;

    // Setting final properties
    // Stops the server when ctrl+c is pressed (registers to
    // Runtime.addShutdownHook)
    aServer.setStopAtShutdown (true);
    // Send the server version in the response header?
    aServer.setSendServerVersion (true);
    // Send the date header in the response header?
    aServer.setSendDateHeader (true);
    // Allows requests (prior to shutdown) to finish gracefully
    aServer.setGracefulShutdown (1000);
    // Starting shutdown listener thread
    new JettyMonitor ().start ();
    // Starting the engines:
    aServer.start ();
    if (aCtx.isFailed ())
    {
      s_aLogger.error ("Failed to start server - stopping server!");
      aServer.stop ();
      s_aLogger.error ("Failed to start server - stopped server!");
    }
    else
    {
      // Running the server!
      aServer.join ();
    }
  }
}
