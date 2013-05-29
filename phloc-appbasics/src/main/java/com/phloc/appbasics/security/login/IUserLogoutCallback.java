package com.phloc.appbasics.security.login;

import javax.annotation.Nonnull;

/**
 * Callback interface when a user logs out.
 * 
 * @author Philip Helger
 */
public interface IUserLogoutCallback
{
  /**
   * Called when a user is logged out.
   * 
   * @param aInfo
   *        The login info of the user that just logged out. Never
   *        <code>null</code>.
   */
  void onUserLogout (@Nonnull LoginInfo aInfo);
}
