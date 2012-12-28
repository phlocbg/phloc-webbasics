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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.security.login.ELoginResult;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.scopes.web.domain.ISessionWebScope;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.app.html.WebHTMLCreator;
import com.phloc.webbasics.app.html.IHTMLProvider;
import com.phloc.webbasics.web.UnifiedResponse;

/**
 * Handle the application login process.
 * 
 * @author philip
 */
public class LoginManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoginManager.class);
  private static final String SESSION_ATTR_AUTHINPROGRESS = "$authinprogress";

  /**
   * Create the HTML code used to render the login screen
   * 
   * @param bLoginError
   *        If <code>true</code> an error occurred in a previous login action
   * @param eLoginResult
   *        The login result - only relevant in case of a login error
   * @return Never <code>null</code>.
   */
  @OverrideOnDemand
  protected IHTMLProvider createLoginScreen (final boolean bLoginError, @Nonnull final ELoginResult eLoginResult)
  {
    return new LoginHTMLProvider (bLoginError, eLoginResult);
  }

  /**
   * Callback method to notify on a successful login
   * 
   * @param sUserLoginName
   *        The login name of the user who just logged in
   */
  @OverrideOnDemand
  protected void onUserLogin (@Nonnull @Nonempty final String sUserLoginName)
  {}

  @Nonnull
  public final EContinue checkUserAndShowLogin (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                @Nonnull final UnifiedResponse aUnifiedResponse)
  {
    String sSessionUserLoginName = LoggedInUserManager.getInstance ().getCurrentUserID ();
    if (sSessionUserLoginName == null)
    {
      // No use currently logged in -> start login
      boolean bLoginError = false;
      ELoginResult eLoginResult = ELoginResult.SUCCESS;

      final ISessionWebScope aSessionScope = WebScopeManager.getSessionScope ();
      if (Boolean.TRUE.equals (aSessionScope.getAttributeObject (SESSION_ATTR_AUTHINPROGRESS)))
      {
        // Login screen was already shown
        // -> Check request parameters
        final String sLoginName = aRequestScope.getAttributeAsString (CLogin.REQUEST_ATTR_USERID);
        final String sPassword = aRequestScope.getAttributeAsString (CLogin.REQUEST_ATTR_PASSWORD);
        eLoginResult = LoggedInUserManager.getInstance ().loginUser (sLoginName, sPassword);
        if (eLoginResult.isSuccess ())
        {
          // Credentials are valid
          aSessionScope.removeAttribute (SESSION_ATTR_AUTHINPROGRESS);
          sSessionUserLoginName = sLoginName;
          onUserLogin (sLoginName);
        }
        else
        {
          // Credentials are invalid

          if (GlobalDebug.isDebugMode ())
            s_aLogger.warn ("Login of '" + sLoginName + "' failed because " + eLoginResult);

          // Anyway show the error message only if at least some credential
          // values are passed
          bLoginError = StringHelper.hasText (sLoginName) || StringHelper.hasText (sPassword);
        }
      }

      if (sSessionUserLoginName == null)
      {
        // Show login screen
        aSessionScope.setAttribute (SESSION_ATTR_AUTHINPROGRESS, Boolean.TRUE);
        WebHTMLCreator.createHTMLResponse (aRequestScope,
                                              aUnifiedResponse,
                                              createLoginScreen (bLoginError, eLoginResult));
      }
    }

    // Continue only, if a valid user ID is present
    return EContinue.valueOf (sSessionUserLoginName != null);
  }
}
