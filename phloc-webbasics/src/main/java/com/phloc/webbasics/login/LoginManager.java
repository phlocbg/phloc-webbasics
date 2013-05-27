/**
 * Copyright (C) 2006-2013 phloc systems
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

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.login.ELoginResult;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.appbasics.security.login.LoginInfo;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.app.html.IHTMLProvider;
import com.phloc.webbasics.app.html.WebHTMLCreator;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.domain.ISessionWebScope;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Handle the application login process.
 * 
 * @author Philip Helger
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
   * @return A list of all role IDs that the user must have so that he can
   *         login! May be <code>null</code> to indicate that any valid user can
   *         login.
   */
  @Nullable
  @OverrideOnDemand
  protected Collection <String> getAllRequiredRoleIDs ()
  {
    return null;
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

  /**
   * Get the user instance of the specified login name.
   * 
   * @param sLoginName
   *        The login name to use. May be <code>null</code>.
   * @return <code>null</code> if no such user exists.
   */
  @Nullable
  @OverrideOnDemand
  protected IUser getUserOfLoginName (@Nullable final String sLoginName)
  {
    return AccessManager.getInstance ().getUserOfLoginName (sLoginName);
  }

  /**
   * Main login
   * 
   * @param aRequestScope
   *        Request scope
   * @param aUnifiedResponse
   *        Response
   * @return {@link EContinue#BREAK} to indicate that no user is logged in and
   *         therefore the login screen should be shown
   */
  @Nonnull
  public final EContinue checkUserAndShowLogin (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                @Nonnull final UnifiedResponse aUnifiedResponse)
  {
    final LoggedInUserManager aLUM = LoggedInUserManager.getInstance ();
    String sSessionUserID = aLUM.getCurrentUserID ();
    if (sSessionUserID == null)
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

        final IUser aUser = getUserOfLoginName (sLoginName);

        // Try main login
        eLoginResult = aLUM.loginUser (aUser, sPassword, getAllRequiredRoleIDs ());
        if (eLoginResult.isSuccess ())
        {
          // Credentials are valid
          aSessionScope.removeAttribute (SESSION_ATTR_AUTHINPROGRESS);
          sSessionUserID = aUser.getID ();
          onUserLogin (sLoginName);
        }

        if (eLoginResult.isFailure ())
        {
          // Credentials are invalid
          if (GlobalDebug.isDebugMode ())
            s_aLogger.warn ("Login of '" + sLoginName + "' failed because " + eLoginResult);

          // Anyway show the error message only if at least some credential
          // values are passed
          bLoginError = StringHelper.hasText (sLoginName) || StringHelper.hasText (sPassword);
        }
      }

      if (sSessionUserID == null)
      {
        // Show login screen
        aSessionScope.setAttribute (SESSION_ATTR_AUTHINPROGRESS, Boolean.TRUE);
        final IHTMLProvider aLoginScreenProvider = createLoginScreen (bLoginError, eLoginResult);
        WebHTMLCreator.createHTMLResponse (aRequestScope, aUnifiedResponse, aLoginScreenProvider);
      }
    }

    // Update details
    final LoginInfo aLoginInfo = aLUM.getLoginInfo (sSessionUserID);
    if (aLoginInfo != null)
    {
      // Update last login info
      aLoginInfo.setLastAccessDTNow ();

      // Set some debugging details
      aLoginInfo.setAttribute ("remote-host", aRequestScope.getRemoteHost ());
      aLoginInfo.setAttribute ("request-uri", aRequestScope.getRequestURI ());
      aLoginInfo.setAttribute ("query-string", aRequestScope.getQueryString ());
    }

    // Continue only, if a valid user ID is present
    return EContinue.valueOf (sSessionUserID != null);
  }
}
