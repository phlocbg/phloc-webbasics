package com.phloc.webbasics.app.security;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.webbasics.app.html.HTMLCreationManager;
import com.phloc.webbasics.app.html.SimpleWebHTMLManager;
import com.phloc.webbasics.app.scope.ISessionScope;
import com.phloc.webbasics.app.scope.ScopeManager;

/**
 * Handle the application login process.
 * 
 * @author philip
 */
public class SimpleWebLogin {
  private static final String SESSION_ATTR_USERID = "$userid";
  private static final String SESSION_ATTR_AUTHINPROGRESS = "$authinprogress";

  @OverrideOnDemand
  protected HTMLCreationManager createLoginScreen (final boolean bLoginError) {
    return new LoginHTML (bLoginError);
  }

  @Nonnull
  public final EContinue checkUserAndShowLogin (@Nonnull final HttpServletRequest aHttpRequest,
                                                @Nonnull final HttpServletResponse aHttpResponse) throws ServletException {
    final HttpSession aSessionScope = aHttpRequest.getSession ();
    String sSessionUserID = (String) aSessionScope.getAttribute (SESSION_ATTR_USERID);
    if (sSessionUserID == null) {
      // Start login
      boolean bLoginError = false;
      if (Boolean.TRUE.equals (aSessionScope.getAttribute (SESSION_ATTR_AUTHINPROGRESS))) {
        // Login screen was already shown
        // -> Check request parameters
        final String sUserID = aHttpRequest.getParameter (LoginHTML.REQUEST_ATTR_USERID);
        final String sPassword = aHttpRequest.getParameter (LoginHTML.REQUEST_ATTR_PASSWORD);
        if (SimpleWebUserManager.getInstance ().areLoginCredentialsValid (sUserID, sPassword)) {
          // Credentials are valid
          aSessionScope.removeAttribute (SESSION_ATTR_AUTHINPROGRESS);
          aSessionScope.setAttribute (SESSION_ATTR_USERID, sUserID);
          sSessionUserID = sUserID;
        }
        else {
          // Credentials are invalid
          // Anyway show the error message only if at least some credential
          // values are passed
          bLoginError = StringHelper.hasText (sUserID) || StringHelper.hasText (sPassword);
        }
      }

      if (sSessionUserID == null) {
        // Show login screen
        aSessionScope.setAttribute (SESSION_ATTR_AUTHINPROGRESS, Boolean.TRUE);
        SimpleWebHTMLManager.createHTMLResponse (aHttpRequest, aHttpResponse, createLoginScreen (bLoginError));
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
  public static String getCurrentUserID () {
    final ISessionScope aSessionScope = ScopeManager.getSessionScope (false);
    return aSessionScope == null ? null : aSessionScope.getAttributeAsString (SESSION_ATTR_USERID);
  }

  /**
   * Log out the current user.
   * 
   * @return {@link EChange#CHANGED} if the user was successfully loged out,
   *         {@link EChange#UNCHANGED} if no user was logged in!
   */
  @Nonnull
  public static EChange logout () {
    final ISessionScope aSessionScope = ScopeManager.getSessionScope (false);
    return aSessionScope == null ? EChange.UNCHANGED : aSessionScope.removeAttribute (SESSION_ATTR_USERID);
  }
}
