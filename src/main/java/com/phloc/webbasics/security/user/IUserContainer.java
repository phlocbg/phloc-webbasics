package com.phloc.webbasics.security.user;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Base read-only interface for objects containing users.
 * 
 * @author philip
 */
public interface IUserContainer
{
  /**
   * @return A non-<code>null</code>but maybe empty set of all assigned user
   *         IDs.
   */
  @Nonnull
  Set <String> getAllContainedUserIDs ();

  /**
   * Check if the passed user is contained in this container.
   * 
   * @param sUserID
   *        The user ID to check. May be <code>null</code>.
   * @return <code>true</code> if the user is contained in this container,
   *         <code>false</code> otherwise.
   */
  boolean containsUserID (String sUserID);
}
