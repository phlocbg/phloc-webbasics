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
import com.phloc.web.useragent.UserAgentDatabase;
import com.phloc.webbasics.app.html.IHTMLProvider;
import com.phloc.webbasics.app.html.WebHTMLCreator;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Handle the application login process. This class requires a separate UI.
 * 
 * @author Philip Helger
 */
public class LoginManager
{
  /**
   * Attribute name for the LoginInfo attribute that holds the remote address of
   * the last request. Type: String.
   */
  public static final String LOGIN_INFO_REMOTE_ADDRESS = "remote-address";

  /**
   * Attribute name for the LoginInfo attribute that holds the remote host of
   * the last request. Type: String.
   */
  public static final String LOGIN_INFO_REMOTE_HOST = "remote-host";

  /**
   * Attribute name for the LoginInfo attribute that holds the URI (without the
   * query string) of the last request. Type: String.
   */
  public static final String LOGIN_INFO_REQUEST_URI = "request-uri";

  /**
   * Attribute name for the LoginInfo attribute that holds the query string of
   * the last request. Type: String.
   */
  public static final String LOGIN_INFO_QUERY_STRING = "query-string";

  /**
   * Attribute name for the LoginInfo attribute that holds the user-agent string
   * of the last request. Type: String.
   */
  public static final String LOGIN_INFO_USER_AGENT = "user-agent";

  /**
   * Attribute name for the LoginInfo attribute that holds the number of
   * requests in this session. Type: int.
   * 
   * @since 2.1.12
   */
  public static final String LOGIN_INFO_REQUEST_COUNT = "request-count";

  private static final Logger s_aLogger = LoggerFactory.getLogger (LoginManager.class);

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
   * Modify the passed {@link LoginInfo} object with details of the passed
   * request scope. This method is called for every request!
   * 
   * @param aLoginInfo
   *        Login Info. Never <code>null</code>.
   * @param aRequestScope
   *        The current request scope.
   */
  protected void modifyLoginInfo (@Nonnull final LoginInfo aLoginInfo,
                                  @Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    // Set some debugging details
    aLoginInfo.setAttribute (LOGIN_INFO_REMOTE_ADDRESS, aRequestScope.getRemoteAddr ());
    aLoginInfo.setAttribute (LOGIN_INFO_REMOTE_HOST, aRequestScope.getRemoteHost ());
    aLoginInfo.setAttribute (LOGIN_INFO_REQUEST_URI, aRequestScope.getRequestURI ());
    aLoginInfo.setAttribute (LOGIN_INFO_QUERY_STRING, aRequestScope.getQueryString ());
    aLoginInfo.setAttribute (LOGIN_INFO_USER_AGENT,
                             UserAgentDatabase.getHttpUserAgentStringFromRequest (aRequestScope.getRequest ()));
    aLoginInfo.setAttribute (LOGIN_INFO_REQUEST_COUNT, aLoginInfo.getAttributeAsInt (LOGIN_INFO_REQUEST_COUNT, 0) + 1);
  }

  /**
   * Main login
   * 
   * @param aRequestScope
   *        Request scope
   * @param aUnifiedResponse
   *        Response
   * @return {@link EContinue#BREAK} to indicate that no user is logged in and
   *         therefore the login screen should be shown,
   *         {@link EContinue#CONTINUE} if a user is correctly logged in.
   */
  @Nonnull
  public final EContinue checkUserAndShowLogin (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                @Nonnull final UnifiedResponse aUnifiedResponse)
  {
    final LoggedInUserManager aLUM = LoggedInUserManager.getInstance ();
    String sSessionUserID = aLUM.getCurrentUserID ();
    boolean bLoggedInInThisRequest = false;
    if (sSessionUserID == null)
    {
      // No use currently logged in -> start login
      boolean bLoginError = false;
      ELoginResult eLoginResult = ELoginResult.SUCCESS;

      // Is the special login-check action present?
      if (CLogin.REQUEST_ACTION_VALIDATE_LOGIN_CREDENTIALS.equals (aRequestScope.getAttributeAsString (CLogin.REQUEST_PARAM_ACTION)))
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
          sSessionUserID = aUser.getID ();
          onUserLogin (sLoginName);
          bLoggedInInThisRequest = true;
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

      // Set custom attributes
      modifyLoginInfo (aLoginInfo, aRequestScope);
    }
    else
      if (sSessionUserID != null)
        s_aLogger.error ("Failed to resolve LoginInfo of user ID '" + sSessionUserID + "'");

    if (bLoggedInInThisRequest)
    {
      // Avoid double submit by simply redirecting to the desired destination
      // URL without the login parameters
      aUnifiedResponse.setRedirect (aRequestScope.getURL ());
      return EContinue.BREAK;
    }

    // Continue only, if a valid user ID is present
    return EContinue.valueOf (sSessionUserID != null);
  }
}
