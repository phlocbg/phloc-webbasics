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
package com.phloc.webbasics.security;

import java.util.Collection;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.state.EChange;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;
import com.phloc.webbasics.security.role.IRole;
import com.phloc.webbasics.security.role.IRoleManager;
import com.phloc.webbasics.security.role.RoleManager;
import com.phloc.webbasics.security.user.IUser;
import com.phloc.webbasics.security.user.IUserManager;
import com.phloc.webbasics.security.user.UserManager;
import com.phloc.webbasics.security.usergroup.IUserGroup;
import com.phloc.webbasics.security.usergroup.IUserGroupManager;
import com.phloc.webbasics.security.usergroup.UserGroupManager;

/**
 * This is the central manager that encapsulates all security manages. This
 * class is thread-safe under the assumption that the implementing managers are
 * thread-safe.
 * 
 * @author philip
 */
@ThreadSafe
public final class AccessManager extends GlobalSingleton implements IUserManager, IUserGroupManager, IRoleManager
{
  private final IUserManager m_aUserMgr;
  private final IRoleManager m_aRoleMgr;
  private final IUserGroupManager m_aUserGroupMgr;

  @Deprecated
  @UsedViaReflection
  public AccessManager ()
  {
    m_aUserMgr = new UserManager ();
    m_aRoleMgr = new RoleManager ();
    m_aUserGroupMgr = new UserGroupManager (m_aUserMgr, m_aRoleMgr);
  }

  @Nonnull
  public static AccessManager getInstance ()
  {
    return getGlobalSingleton (AccessManager.class);
  }

  // User API

  @Nonnull
  public IUser createNewUser (@Nonnull @Nonempty final String sEmailAddress,
                              @Nonnull @Nonempty final String sPlainTextPassword,
                              @Nullable final String sFirstName,
                              @Nullable final String sLastName,
                              @Nullable final Locale aDesiredLocale)
  {
    return m_aUserMgr.createNewUser (sEmailAddress, sPlainTextPassword, sFirstName, sLastName, aDesiredLocale);
  }

  /**
   * Ensure to have a check, that no logged in user is deleted!
   */
  @Nonnull
  public EChange deleteUser (@Nullable final String sUserID)
  {
    if (m_aUserMgr.deleteUser (sUserID).isUnchanged ())
    {
      // No such user to delete
      return EChange.UNCHANGED;
    }

    // If something deleted, remove from all user groups
    m_aUserGroupMgr.unassignUserFromAllUserGroups (sUserID);
    return EChange.CHANGED;
  }

  public boolean containsUserWithID (@Nullable final String sUserID)
  {
    return m_aUserMgr.containsUserWithID (sUserID);
  }

  @Nullable
  public IUser getUserOfID (@Nullable final String sUserID)
  {
    return m_aUserMgr.getUserOfID (sUserID);
  }

  @Nullable
  public IUser getUserOfEmailAddress (@Nullable final String sEmailAddress)
  {
    return m_aUserMgr.getUserOfEmailAddress (sEmailAddress);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <? extends IUser> getAllUsers ()
  {
    return m_aUserMgr.getAllUsers ();
  }

  @Nonnull
  public EChange setUserData (@Nullable final String sUserID,
                              @Nullable final String sNewFirstName,
                              @Nullable final String sNewLastName,
                              @Nullable final Locale aNewDesiredLocale)
  {
    return m_aUserMgr.setUserData (sUserID, sNewFirstName, sNewLastName, aNewDesiredLocale);
  }

  public boolean areUserIDAndPasswordValid (@Nullable final String sUserID, @Nullable final String sPlainTextPassword)
  {
    return m_aUserMgr.areUserIDAndPasswordValid (sUserID, sPlainTextPassword);
  }

  /**
   * Check if the passed combination of email address (= login) and plain text
   * password are valid
   * 
   * @param sEmailAddress
   *        The email address for which a user was searched
   * @param sPlainTextPassword
   *        The plain text password to validate
   * @return <code>true</code> if the email address matches a user, and if the
   *         hash of the plain text password matches the stored password hash
   */
  public boolean areUserEmailAndPasswordValid (@Nullable final String sEmailAddress,
                                               @Nullable final String sPlainTextPassword)
  {
    final IUser aUser = getUserOfEmailAddress (sEmailAddress);
    return aUser == null ? false : m_aUserMgr.areUserIDAndPasswordValid (aUser.getID (), sPlainTextPassword);
  }

  // UserGroup API

  @Nonnull
  public IUserGroup createNewUserGroup (@Nonnull @Nonempty final String sName)
  {
    return m_aUserGroupMgr.createNewUserGroup (sName);
  }

  @Nonnull
  public EChange deleteUserGroup (@Nullable final String sUserGroupID)
  {
    return m_aUserGroupMgr.deleteUserGroup (sUserGroupID);
  }

  public boolean containsUserGroupWithID (@Nullable final String sUserGroupID)
  {
    return m_aUserGroupMgr.containsUserGroupWithID (sUserGroupID);
  }

  @Nullable
  public IUserGroup getUserGroupOfID (@Nullable final String sUserGroupID)
  {
    return m_aUserGroupMgr.getUserGroupOfID (sUserGroupID);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <? extends IUserGroup> getAllUserGroups ()
  {
    return m_aUserGroupMgr.getAllUserGroups ();
  }

  @Nonnull
  public EChange renameUserGroup (@Nullable final String sUserGroupID, @Nonnull @Nonempty final String sNewName)
  {
    return m_aUserGroupMgr.renameUserGroup (sUserGroupID, sNewName);
  }

  @Nonnull
  public EChange assignUserToUserGroup (@Nullable final String sUserGroupID, @Nullable final String sUserID)
  {
    return m_aUserGroupMgr.assignUserToUserGroup (sUserGroupID, sUserID);
  }

  @Nonnull
  public EChange unassignUserFromUserGroup (@Nullable final String sUserGroupID, @Nullable final String sUserID)
  {
    return m_aUserGroupMgr.unassignUserFromUserGroup (sUserGroupID, sUserID);
  }

  @Nonnull
  public EChange unassignUserFromAllUserGroups (@Nullable final String sUserID)
  {
    return m_aUserGroupMgr.unassignUserFromAllUserGroups (sUserID);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IUserGroup> getAllUserGroupsWithAssignedUser (@Nullable final String sUserID)
  {
    return m_aUserGroupMgr.getAllUserGroupsWithAssignedUser (sUserID);
  }

  @Nonnull
  public EChange assignRoleToUserGroup (@Nullable final String sUserGroupID, @Nullable final String sRoleID)
  {
    return m_aUserGroupMgr.assignRoleToUserGroup (sUserGroupID, sRoleID);
  }

  @Nonnull
  public EChange unassignRoleFromUserGroup (@Nullable final String sUserGroupID, @Nullable final String sRoleID)
  {
    return m_aUserGroupMgr.unassignRoleFromUserGroup (sUserGroupID, sRoleID);
  }

  @Nonnull
  public EChange unassignRoleFromAllUserGroups (@Nullable final String sRoleID)
  {
    return m_aUserGroupMgr.unassignRoleFromAllUserGroups (sRoleID);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IUserGroup> getAllUserGroupsWithAssignedRole (@Nullable final String sRoleID)
  {
    return m_aUserGroupMgr.getAllUserGroupsWithAssignedRole (sRoleID);
  }

  // Role API

  @Nonnull
  public IRole createNewRole (@Nonnull @Nonempty final String sName)
  {
    return m_aRoleMgr.createNewRole (sName);
  }

  @Nonnull
  public EChange deleteRole (@Nullable final String sRoleID)
  {
    if (m_aRoleMgr.deleteRole (sRoleID).isUnchanged ())
      return EChange.UNCHANGED;

    // Since the role does not exist any more, remove it from all user groups
    m_aUserGroupMgr.unassignRoleFromAllUserGroups (sRoleID);
    return EChange.CHANGED;
  }

  public boolean containsRoleWithID (@Nullable final String sRoleID)
  {
    return m_aRoleMgr.containsRoleWithID (sRoleID);
  }

  @Nullable
  public IRole getRoleOfID (@Nullable final String sRoleID)
  {
    return m_aRoleMgr.getRoleOfID (sRoleID);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <? extends IRole> getAllRoles ()
  {
    return m_aRoleMgr.getAllRoles ();
  }

  @Nonnull
  public EChange renameRole (@Nullable final String sRoleID, @Nonnull @Nonempty final String sNewName)
  {
    return m_aRoleMgr.renameRole (sRoleID, sNewName);
  }

  /**
   * Check if the passed user ID has the passed role by checking all user group
   * role assignments of the user.
   * 
   * @param sUserID
   *        User ID to check
   * @param sRoleID
   *        Role ID to check
   * @return <code>true</code> if the user is in at least one user group that
   *         has the assigned role, <code>false</code> otherwise
   */
  public boolean hasUserRole (@Nullable final String sUserID, @Nullable final String sRoleID)
  {
    for (final IUserGroup aUserGroup : m_aUserGroupMgr.getAllUserGroupsWithAssignedUser (sUserID))
      if (aUserGroup.containsRoleID (sRoleID))
        return true;
    return false;
  }
}
