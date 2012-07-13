package com.phloc.appbasics.security.login;

import javax.annotation.Nullable;

/**
 * Callback interface for resolving the current user ID.
 * 
 * @author philip
 */
public interface ICurrentUserIDProvider
{
  /**
   * @return The current user ID or <code>null</code> if no user is logged in.
   */
  @Nullable
  String getCurrentUserID ();
}
