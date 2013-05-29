package com.phloc.appbasics.security.login;

import javax.annotation.Nonnull;

/**
 * Callback interface when a user logs in.
 * 
 * @author Philip Helger
 */
public interface IUserLoginCallback
{
  /**
   * Called when a user is logged in.
   * 
   * @param aInfo
   *        The login info of the user that just logged in. Never
   *        <code>null</code>.
   */
  void onUserLogin (@Nonnull LoginInfo aInfo);
}
