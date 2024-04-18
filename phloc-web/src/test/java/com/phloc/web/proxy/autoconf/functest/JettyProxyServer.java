/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.web.proxy.autoconf.functest;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.proxy.ProxyServlet;

import com.phloc.commons.url.URLProtocolRegistry;

public class JettyProxyServer
{
  public static void main (final String... args) throws Exception
  {

    // Create main server
    final Server aServer = new Server ();
    // Create connector on Port 8080
    final ServerConnector aConnector = new ServerConnector(aServer);
    aConnector.setPort (8080);
    aConnector.setIdleTimeout (30000);
    aServer.setConnectors (new Connector [] { aConnector });

    // Create main context at path "/"
    final ServletContextHandler aCtx = new ServletContextHandler (aServer, "/", ServletContextHandler.SESSIONS);
    aCtx.setAllowNullPathInfo (true);

    final ServletHolder aSH1 = new ServletHolder (new ProxyServlet ()
    {
      @Override
      public void service (final ServletRequest req, final ServletResponse res) throws ServletException, IOException
      {
        // Only accept valid URL protocols
        final String sRequestURI = ((HttpServletRequest) req).getRequestURI ();
        if (!URLProtocolRegistry.hasKnownProtocol (sRequestURI) || sRequestURI.contains ("//localhost"))
          ((HttpServletResponse) res).setStatus (HttpServletResponse.SC_NOT_FOUND);
        else
          super.service (req, res);
      }
    });
    aCtx.addServlet (aSH1, "/*");

    // Starting the engines:
    aServer.start ();
    // Running the server!
    aServer.join ();
  }
}
