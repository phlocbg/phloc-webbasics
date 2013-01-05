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
package com.phloc.appbasics.security.usergroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.impl.AbstractSimpleDAO;
import com.phloc.appbasics.security.CSecurity;
import com.phloc.appbasics.security.role.IRoleManager;
import com.phloc.appbasics.security.user.IUserManager;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;

/**
 * This class manages the available users.
 * 
 * @author philip
 */
@ThreadSafe
public final class UserGroupManager extends AbstractSimpleDAO implements IUserGroupManager
{
  private static boolean s_bCreateDefaults = true;

  private final IUserManager m_aUserMgr;
  private final IRoleManager m_aRoleMgr;
  private final Map <String, UserGroup> m_aUserGroups = new HashMap <String, UserGroup> ();

  public static boolean isCreateDefaults ()
  {
    return s_bCreateDefaults;
  }

  public static void setCreateDefaults (final boolean bCreateDefaults)
  {
    s_bCreateDefaults = bCreateDefaults;
  }

  public UserGroupManager (@Nonnull @Nonempty final String sFilename,
                           @Nonnull final IUserManager aUserMgr,
                           @Nonnull final IRoleManager aRoleMgr)
  {
    super (sFilename);
    m_aUserMgr = aUserMgr;
    m_aRoleMgr = aRoleMgr;
    initialRead ();
  }

  @Override
  @Nonnull
  protected EChange onInit ()
  {
    if (!s_bCreateDefaults)
      return EChange.UNCHANGED;

    // Administrators user group
    UserGroup aUG = _addUserGroup (new UserGroup (CSecurity.USERGROUP_ADMINISTRATORS_ID,
                                                  CSecurity.USERGROUP_ADMINISTRATORS_NAME));
    if (m_aUserMgr.containsUserWithID (CSecurity.USER_ADMINISTRATOR_ID))
      aUG.assignUser (CSecurity.USER_ADMINISTRATOR_ID);
    if (m_aRoleMgr.containsRoleWithID (CSecurity.ROLE_ADMINISTRATOR_ID))
      aUG.assignRole (CSecurity.ROLE_ADMINISTRATOR_ID);

    // Users user group
    aUG = _addUserGroup (new UserGroup (CSecurity.USERGROUP_USERS_ID, CSecurity.USERGROUP_USERS_NAME));
    if (m_aUserMgr.containsUserWithID (CSecurity.USER_USER_ID))
      aUG.assignUser (CSecurity.USER_USER_ID);
    if (m_aRoleMgr.containsRoleWithID (CSecurity.ROLE_USER_ID))
      aUG.assignRole (CSecurity.ROLE_USER_ID);

    // Guests user group
    aUG = _addUserGroup (new UserGroup (CSecurity.USERGROUP_GUESTS_ID, CSecurity.USERGROUP_GUESTS_NAME));
    if (m_aUserMgr.containsUserWithID (CSecurity.USER_GUEST_ID))
      aUG.assignUser (CSecurity.USER_GUEST_ID);
    // no role for this user group

    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  protected EChange onRead (@Nonnull final IMicroDocument aDoc)
  {
    for (final IMicroElement eUserGroup : aDoc.getDocumentElement ().getChildElements ())
      _addUserGroup (MicroTypeConverter.convertToNative (eUserGroup, UserGroup.class));
    return EChange.UNCHANGED;
  }

  @Override
  @Nonnull
  protected IMicroDocument createWriteData ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement ("usergroups");
    for (final UserGroup aUserGroup : ContainerHelper.getSortedByKey (m_aUserGroups).values ())
      eRoot.appendChild (MicroTypeConverter.convertToMicroElement (aUserGroup, "usergroup"));
    return aDoc;
  }

  @Nonnull
  private UserGroup _addUserGroup (@Nonnull final UserGroup aUserGroup)
  {
    final String sUserGroupID = aUserGroup.getID ();
    if (m_aUserGroups.containsKey (sUserGroupID))
      throw new IllegalArgumentException ("User group ID " + sUserGroupID + " is already in use!");
    m_aUserGroups.put (sUserGroupID, aUserGroup);
    return aUserGroup;
  }

  @Nonnull
  public IUserGroup createNewUserGroup (@Nonnull @Nonempty final String sName)
  {
    // Create user group
    final UserGroup aUserGroup = new UserGroup (sName);

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Store
      _addUserGroup (aUserGroup);
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    return aUserGroup;
  }

  @Nonnull
  public IUserGroup createPredefinedUserGroup (@Nonnull @Nonempty final String sID,
                                               @Nonnull @Nonempty final String sName)
  {
    // Create user group
    final UserGroup aUserGroup = new UserGroup (sID, sName);

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Store
      _addUserGroup (aUserGroup);
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    return aUserGroup;
  }

  public boolean containsUserGroupWithID (@Nullable final String sUserGroupID)
  {
    if (StringHelper.hasNoText (sUserGroupID))
      return false;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aUserGroups.containsKey (sUserGroupID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean containsAllUserGroupsWithID (@Nullable final Collection <String> aUserGroupIDs)
  {
    if (ContainerHelper.isEmpty (aUserGroupIDs))
      return true;

    m_aRWLock.readLock ().lock ();
    try
    {
      for (final String sUserGroupID : aUserGroupIDs)
        if (!m_aUserGroups.containsKey (sUserGroupID))
          return false;
      return true;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public UserGroup getUserGroupOfID (@Nullable final String sUserGroupID)
  {
    if (StringHelper.hasNoText (sUserGroupID))
      return null;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aUserGroups.get (sUserGroupID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <? extends IUserGroup> getAllUserGroups ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aUserGroups.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public EChange deleteUserGroup (@Nullable final String sUserGroupID)
  {
    if (StringHelper.hasNoText (sUserGroupID))
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aUserGroups.remove (sUserGroupID) == null)
        return EChange.UNCHANGED;
      markAsChanged ();
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange renameUserGroup (@Nullable final String sUserGroupID, @Nonnull @Nonempty final String sNewName)
  {
    // Resolve user group
    final UserGroup aUserGroup = getUserGroupOfID (sUserGroupID);
    if (aUserGroup == null)
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aUserGroup.setName (sNewName).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange assignUserToUserGroup (@Nullable final String sUserGroupID, @Nullable final String sUserID)
  {
    // Resolve user group
    final UserGroup aUserGroup = getUserGroupOfID (sUserGroupID);
    if (aUserGroup == null)
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aUserGroup.assignUser (sUserID).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange unassignUserFromUserGroup (@Nullable final String sUserGroupID, @Nullable final String sUserID)
  {
    // Resolve user group
    final UserGroup aUserGroup = getUserGroupOfID (sUserGroupID);
    if (aUserGroup == null)
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aUserGroup.unassignUser (sUserID).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange unassignUserFromAllUserGroups (@Nullable final String sUserID)
  {
    if (StringHelper.hasNoText (sUserID))
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      EChange eChange = EChange.UNCHANGED;
      for (final UserGroup aUserGroup : m_aUserGroups.values ())
        eChange = eChange.or (aUserGroup.unassignUser (sUserID));
      if (eChange.isUnchanged ())
        return EChange.UNCHANGED;

      markAsChanged ();
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public boolean isUserAssignedToUserGroup (@Nullable final String sUserGroupID, @Nullable final String sUserID)
  {
    if (StringHelper.hasNoText (sUserID))
      return false;

    final IUserGroup aUserGroup = getUserGroupOfID (sUserGroupID);
    return aUserGroup == null ? false : aUserGroup.containsUserID (sUserID);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IUserGroup> getAllUserGroupsWithAssignedUser (@Nullable final String sUserID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final List <IUserGroup> ret = new ArrayList <IUserGroup> ();
      if (StringHelper.hasText (sUserID))
        for (final IUserGroup aUserGroup : m_aUserGroups.values ())
          if (aUserGroup.containsUserID (sUserID))
            ret.add (aUserGroup);
      return ret;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllUserGroupIDsWithAssignedUser (@Nullable final String sUserID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final List <String> ret = new ArrayList <String> ();
      if (StringHelper.hasText (sUserID))
        for (final IUserGroup aUserGroup : m_aUserGroups.values ())
          if (aUserGroup.containsUserID (sUserID))
            ret.add (aUserGroup.getID ());
      return ret;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public EChange assignRoleToUserGroup (@Nullable final String sUserGroupID, @Nullable final String sRoleID)
  {
    // Resolve user group
    final UserGroup aUserGroup = getUserGroupOfID (sUserGroupID);
    if (aUserGroup == null)
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aUserGroup.assignRole (sRoleID).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange unassignRoleFromUserGroup (@Nullable final String sUserGroupID, @Nullable final String sRoleID)
  {
    // Resolve user group
    final UserGroup aUserGroup = getUserGroupOfID (sUserGroupID);
    if (aUserGroup == null)
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aUserGroup.unassignRole (sRoleID).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange unassignRoleFromAllUserGroups (@Nullable final String sRoleID)
  {
    if (StringHelper.hasNoText (sRoleID))
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      EChange eChange = EChange.UNCHANGED;
      for (final UserGroup aUserGroup : m_aUserGroups.values ())
        eChange = eChange.or (aUserGroup.unassignRole (sRoleID));
      if (eChange.isUnchanged ())
        return EChange.UNCHANGED;

      markAsChanged ();
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IUserGroup> getAllUserGroupsWithAssignedRole (@Nullable final String sRoleID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final List <IUserGroup> ret = new ArrayList <IUserGroup> ();
      if (StringHelper.hasText (sRoleID))
        for (final IUserGroup aUserGroup : m_aUserGroups.values ())
          if (aUserGroup.containsRoleID (sRoleID))
            ret.add (aUserGroup);
      return ret;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllUserGroupIDsWithAssignedRole (@Nullable final String sRoleID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final List <String> ret = new ArrayList <String> ();
      if (StringHelper.hasText (sRoleID))
        for (final IUserGroup aUserGroup : m_aUserGroups.values ())
          if (aUserGroup.containsRoleID (sRoleID))
            ret.add (aUserGroup.getID ());
      return ret;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
