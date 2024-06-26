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
package com.phloc.webbasics.servlet;

import java.io.IOException;

import javax.annotation.Nonnull;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.servlets.AbstractUnifiedResponseServlet;

/**
 * Handles the log-out of a user. Can be called with a user context and without.
 * 
 * @author Philip Helger
 */
public class LogoutServlet extends AbstractUnifiedResponseServlet
{
  public LogoutServlet ()
  {}

  /**
   * Get the redirection URL. This method is invoked BEFORE an eventual log-out
   * happens!
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @return The URL to redirect to, after this servlet was invoked. May not be
   *         <code>null</code>.
   */
  @OverrideOnDemand
  @Nonnull
  protected ISimpleURL getRedirectURL (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    // No need for a session ID upon logout
    return LinkUtils.getHomeLinkWithoutSession ();
  }

  @Override
  protected void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException, IOException
  {
    // Get the redirect URL before the session is invalidated, in case the
    // code requires the current session
    final ISimpleURL aRedirectURL = getRedirectURL (aRequestScope);

    // Don't create a session, if none is present
    final HttpSession aHttpSession = aRequestScope.getRequest ().getSession (false);
    if (aHttpSession != null)
    {
      // Perform the main logout
      // 1. Invalidate the session
      // 2. Triggers the session scope destruction (via the HttpSessionListener)
      // 3. which triggers WebScopeManager.onSessionEnd
      // 4. which triggers WebScopeSessionManager.getInstance ().onSessionEnd
      // 5. which triggers ISessionWebScope.destroyScope
      aHttpSession.invalidate ();
    }

    // Go home
    aUnifiedResponse.setRedirect (aRedirectURL);
  }
}
