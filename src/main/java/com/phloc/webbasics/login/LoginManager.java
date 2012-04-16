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
package com.phloc.webbasics.login;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.domain.ISessionWebScope;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.app.html.HTMLResponseHelper;
import com.phloc.webbasics.app.html.IHTMLProvider;
import com.phloc.webbasics.security.login.LoggedInUserManager;

/**
 * Handle the application login process.
 * 
 * @author philip
 */
public class LoginManager
{
  private static final String SESSION_ATTR_AUTHINPROGRESS = "$authinprogress";

  /**
   * Create the HTML code used to render the login screen
   * 
   * @param bLoginError
   *        If <code>true</code> an error occurred in a previous login action
   * @return Never <code>null</code>.
   */
  @OverrideOnDemand
  protected IHTMLProvider createLoginScreen (final boolean bLoginError)
  {
    return new BasicLoginHTML (bLoginError);
  }

  @Nonnull
  public final EContinue checkUserAndShowLogin (@Nonnull final IRequestWebScope aRequestScope) throws ServletException
  {
    String sSessionUserID = LoggedInUserManager.getInstance ().getCurrentUserID ();
    if (sSessionUserID == null)
    {
      // No use currently logged in -> start login
      boolean bLoginError = false;

      final ISessionWebScope aSessionScope = WebScopeManager.getSessionScope ();
      if (Boolean.TRUE.equals (aSessionScope.getAttributeObject (SESSION_ATTR_AUTHINPROGRESS)))
      {
        // Login screen was already shown
        // -> Check request parameters
        final String sUserID = aRequestScope.getAttributeAsString (CLogin.REQUEST_ATTR_USERID);
        final String sPassword = aRequestScope.getAttributeAsString (CLogin.REQUEST_ATTR_PASSWORD);
        if (LoggedInUserManager.getInstance ().loginUser (sUserID, sPassword).isSuccess ())
        {
          // Credentials are valid
          aSessionScope.removeAttribute (SESSION_ATTR_AUTHINPROGRESS);
          sSessionUserID = sUserID;
        }
        else
        {
          // Credentials are invalid
          // Anyway show the error message only if at least some credential
          // values are passed
          bLoginError = StringHelper.hasText (sUserID) || StringHelper.hasText (sPassword);
        }
      }

      if (sSessionUserID == null)
      {
        // Show login screen
        aSessionScope.setAttribute (SESSION_ATTR_AUTHINPROGRESS, Boolean.TRUE);
        HTMLResponseHelper.createHTMLResponse (aRequestScope, createLoginScreen (bLoginError));
      }
    }

    // Continue only, if a valid user ID is present
    return EContinue.valueOf (sSessionUserID != null);
  }
}
