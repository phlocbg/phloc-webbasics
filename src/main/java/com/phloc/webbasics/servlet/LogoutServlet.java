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

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.servlet.AbstractScopeAwareHttpServlet;
import com.phloc.webbasics.app.LinkUtils;

/**
 * Handles the log-out of a user. Can be called with a user context and without.
 * 
 * @author philip
 */
public class LogoutServlet extends AbstractScopeAwareHttpServlet
{
  public LogoutServlet ()
  {}

  @OverrideOnDemand
  @Nonnull
  protected ISimpleURL getRedirectURL ()
  {
    return LinkUtils.getHomeLink ();
  }

  private void _run (@Nonnull final HttpServletRequest aHttpRequest, @Nonnull final HttpServletResponse aHttpResponse) throws IOException
  {
    // Get the redirect URL before the session is invalidated, in case the code
    // requires the current session
    final ISimpleURL aRedirectURL = getRedirectURL ();

    // Perform the main logout
    // 1. Invalidate the session
    // 2. Triggers the session scope destruction (via the HttpSessionListener)
    // 3. which triggers WebScopeManager.onSessionEnd
    // 4. which triggers WebScopeSessionManager.getInstance ().onSessionEnd
    // 5. which triggers ISessionWebScope.destroyScope
    aHttpRequest.getSession ().invalidate ();

    // Go home
    aHttpResponse.sendRedirect (aRedirectURL.getAsString ());
  }

  @Override
  protected void onGet (@Nonnull final HttpServletRequest aHttpRequest,
                        @Nonnull final HttpServletResponse aHttpResponse,
                        @Nonnull final IRequestWebScope aRequestScope) throws IOException
  {
    _run (aHttpRequest, aHttpResponse);
  }

  @Override
  protected void onPost (@Nonnull final HttpServletRequest aHttpRequest,
                         @Nonnull final HttpServletResponse aHttpResponse,
                         @Nonnull final IRequestWebScope aRequestScope) throws IOException
  {
    _run (aHttpRequest, aHttpResponse);
  }
}
