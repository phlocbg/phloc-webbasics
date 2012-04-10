package com.phloc.webbasics.security.role;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Base read-only interface for objects containing roles.
 * 
 * @author philip
 */
public interface IRoleContainer
{
  /**
   * @return A non-<code>null</code>but maybe empty set of all assigned role
   *         IDs.
   */
  @Nonnull
  Set <String> getAllContainedRoleIDs ();

  /**
   * Check if the passed role is contained in this container.
   * 
   * @param sRoleID
   *        The role ID to check. May be <code>null</code>.
   * @return <code>true</code> if the role is contained in this container,
   *         <code>false</code> otherwise.
   */
  boolean containsRoleID (String sRoleID);
}
