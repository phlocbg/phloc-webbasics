package com.phloc.webbasics.security.role;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Interface for a role manager.
 * 
 * @author philip
 */
public interface IRoleManager
{
  /**
   * Create a new role.
   * 
   * @param sName
   *        The name of the new role
   * @return The created role and never <code>null</code>.
   */
  @Nonnull
  IRole createNewRole (@Nonnull @Nonempty String sName);

  /**
   * Delete the role with the passed ID
   * 
   * @param sRoleID
   *        The role ID to be deleted
   * @return {@link EChange#CHANGED} if the passed role ID was found and deleted
   */
  @Nonnull
  EChange deleteRole (@Nullable String sRoleID);

  /**
   * Check if the role with the specified ID is contained
   * 
   * @param sRoleID
   *        The role ID to be check
   * @return <code>true</code> if such role exists, <code>false</code> otherwise
   */
  boolean containsRoleWithID (@Nullable String sRoleID);

  /**
   * Get the role with the specified ID
   * 
   * @param sRoleID
   *        The role ID to be resolved
   * @return <code>null</code> if no such role exists.
   */
  @Nullable
  IRole getRoleOfID (@Nullable String sRoleID);

  /**
   * @return A non-<code>null</code> collection of all available roles
   */
  @Nonnull
  @ReturnsMutableCopy
  Collection <? extends IRole> getAllRoles ();

  /**
   * Rename the role with the passed ID
   * 
   * @param sRoleID
   *        The ID of the role to be renamed
   * @param sNewName
   *        The new name of the role
   * @return {@link EChange#CHANGED} if the passed role ID was found, and the
   *         new name is different from the old name of he role
   */
  @Nonnull
  EChange renameRole (@Nullable String sRoleID, @Nonnull @Nonempty String sNewName);
}
