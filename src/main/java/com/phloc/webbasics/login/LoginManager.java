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
import javax.annotation.Nullable;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.scopes.IScope;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.domain.ISessionWebScope;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.app.html.HTMLResponseHelper;
import com.phloc.webbasics.app.html.IHTMLProvider;
import com.phloc.webbasics.security.AccessManager;

/**
 * Handle the application login process.
 * 
 * @author philip
 */
public class LoginManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoginManager.class);
  private static final String SESSION_ATTR_USERID = "$userid";
  private static final String SESSION_ATTR_AUTHINPROGRESS = "$authinprogress";

  @OverrideOnDemand
  protected IHTMLProvider createLoginScreen (final boolean bLoginError)
  {
    return new BasicLoginHTML (bLoginError);
  }

  @Nonnull
  public final EContinue checkUserAndShowLogin (@Nonnull final IRequestWebScope aRequestScope) throws ServletException
  {
    final ISessionWebScope aSession = WebScopeManager.getSessionScope ();
    String sSessionUserID = aSession.getAttributeAsString (SESSION_ATTR_USERID);
    if (sSessionUserID == null)
    {
      // No use currently logged in -> start login
      boolean bLoginError = false;
      if (Boolean.TRUE.equals (aSession.getAttributeObject (SESSION_ATTR_AUTHINPROGRESS)))
      {
        // Login screen was already shown
        // -> Check request parameters
        final String sUserID = aRequestScope.getAttributeAsString (BasicLoginHTML.REQUEST_ATTR_USERID);
        final String sPassword = aRequestScope.getAttributeAsString (BasicLoginHTML.REQUEST_ATTR_PASSWORD);
        if (AccessManager.getInstance ().areUserEmailAndPasswordValid (sUserID, sPassword))
        {
          // Credentials are valid
          aSession.removeAttribute (SESSION_ATTR_AUTHINPROGRESS);
          aSession.setAttribute (SESSION_ATTR_USERID, sUserID);
          sSessionUserID = sUserID;
          s_aLogger.info ("User '" + sUserID + "' logged in!");
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
        aSession.setAttribute (SESSION_ATTR_AUTHINPROGRESS, Boolean.TRUE);
        HTMLResponseHelper.createHTMLResponse (aRequestScope, createLoginScreen (bLoginError));
      }
    }

    // Continue only, if a valid user ID is present
    return EContinue.valueOf (sSessionUserID != null);
  }

  /**
   * @return The ID of the currently logged in user. May be <code>null</code> if
   *         no user is logged in.
   */
  @Nullable
  public static String getCurrentUserID ()
  {
    final IScope aSessionScope = WebScopeManager.getSessionScope (false);
    return aSessionScope == null ? null : aSessionScope.getAttributeAsString (SESSION_ATTR_USERID);
  }

  /**
   * Log out the current user.
   * 
   * @return {@link EChange#CHANGED} if the user was successfully loged out,
   *         {@link EChange#UNCHANGED} if no user was logged in!
   */
  @Nonnull
  public static EChange logout ()
  {
    final IScope aSessionScope = WebScopeManager.getSessionScope (false);
    return aSessionScope == null ? EChange.UNCHANGED : aSessionScope.removeAttribute (SESSION_ATTR_USERID);
  }
}
