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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.security.LoginManager;

public final class LogoutServlet extends AbstractScopeAwareHttpServlet {
  private static final Logger s_aLogger = LoggerFactory.getLogger (LogoutServlet.class);

  public LogoutServlet () {}

  private void _run (final HttpServletResponse aHttpResponse) throws IOException {
    // Remember the current user ID
    final String sUserID = LoginManager.getCurrentUserID ();

    // Perform the main logout
    if (LoginManager.logout ().isChanged ())
      s_aLogger.info ("User " + sUserID + " logged out!");

    // Go home
    aHttpResponse.sendRedirect (LinkUtils.getServletURL ("/").getAsString ());
  }

  @Override
  protected void onGet (final HttpServletRequest aRequest, final HttpServletResponse aResponse) throws IOException {
    _run (aResponse);
  }

  @Override
  protected void onPost (final HttpServletRequest aRequest, final HttpServletResponse aResponse) throws IOException {
    _run (aResponse);
  }
}
