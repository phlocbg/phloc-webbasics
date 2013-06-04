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
package com.phloc.appbasics.security.role;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Interface for a role manager.
 * 
 * @author Philip Helger
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
   * Create a new role.
   * 
   * @param sName
   *        The name of the new role
   * @param aCustomAttrs
   *        A set of custom attributes. May be <code>null</code>.
   * @return The created role and never <code>null</code>.
   */
  @Nonnull
  IRole createNewRole (@Nonnull @Nonempty String sName, @Nullable Map <String, String> aCustomAttrs);

  /**
   * Create a predefined role.
   * 
   * @param sID
   *        The ID of the new role
   * @param sName
   *        The name of the new role
   * @return The created role and never <code>null</code>.
   */
  @Nonnull
  IRole createPredefinedRole (@Nonnull @Nonempty String sID, @Nonnull @Nonempty String sName);

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
   * Check if all passed role IDs are contained
   * 
   * @param aRoleIDs
   *        The role IDs to be checked. May be <code>null</code>.
   * @return <code>true</code> if the collection is empty or if all contained
   *         role IDs are contained
   */
  boolean containsAllRolesWithID (@Nullable Collection <String> aRoleIDs);

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
