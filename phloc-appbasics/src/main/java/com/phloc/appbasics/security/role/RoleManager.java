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
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.IReloadableDAO;
import com.phloc.appbasics.app.dao.impl.AbstractSimpleDAO;
import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.appbasics.security.CSecurity;
import com.phloc.appbasics.security.audit.AuditUtils;
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
 * This class manages the available roles.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class RoleManager extends AbstractSimpleDAO implements IRoleManager, IReloadableDAO
{
  private static boolean s_bCreateDefaults = true;
  private final Map <String, Role> m_aRoles = new HashMap <String, Role> ();

  /**
   * @return <code>true</code> if the default built-in roles should be created
   *         if no roles are present, <code>false</code> if not.
   */
  public static boolean isCreateDefaults ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bCreateDefaults;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  public static void setCreateDefaults (final boolean bCreateDefaults)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_bCreateDefaults = bCreateDefaults;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public RoleManager (@Nonnull @Nonempty final String sFilename) throws DAOException
  {
    super (sFilename);
    initialRead ();
  }

  public void reload () throws DAOException
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aRoles.clear ();
      initialRead ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  protected EChange onInit ()
  {
    if (!isCreateDefaults ())
      return EChange.UNCHANGED;

    // Default should be created
    _addRole (new Role (CSecurity.ROLE_ADMINISTRATOR_ID, CSecurity.ROLE_ADMINISTRATOR_NAME));
    _addRole (new Role (CSecurity.ROLE_USER_ID, CSecurity.ROLE_USER_NAME));
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  protected EChange onRead (@Nonnull final IMicroDocument aDoc)
  {
    for (final IMicroElement eRole : aDoc.getDocumentElement ().getAllChildElements ())
      _addRole (MicroTypeConverter.convertToNative (eRole, Role.class));
    return EChange.UNCHANGED;
  }

  @Override
  @Nonnull
  protected IMicroDocument createWriteData ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement ("roles");
    for (final Role aRole : ContainerHelper.getSortedByKey (m_aRoles).values ())
      eRoot.appendChild (MicroTypeConverter.convertToMicroElement (aRole, "role"));
    return aDoc;
  }

  private void _addRole (@Nonnull final Role aRole)
  {
    if (aRole == null)
      throw new NullPointerException ("Role");

    final String sRoleID = aRole.getID ();
    if (m_aRoles.containsKey (sRoleID))
      throw new IllegalArgumentException ("Role ID " + sRoleID + " is already in use!");
    m_aRoles.put (sRoleID, aRole);
  }

  @Nonnull
  public IRole createNewRole (@Nonnull @Nonempty final String sName)
  {
    return createNewRole (sName, (Map <String, String>) null);
  }

  @Nonnull
  public IRole createNewRole (@Nonnull @Nonempty final String sName, @Nullable final Map <String, ?> aCustomAttrs)
  {
    // Create role
    final Role aRole = new Role (sName, aCustomAttrs);

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Store
      _addRole (aRole);
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditCreateSuccess (CSecurity.TYPE_ROLE, aRole.getID (), sName);
    return aRole;
  }

  @Nonnull
  public IRole createPredefinedRole (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    // Create role
    final Role aRole = new Role (sID, sName);

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Store
      _addRole (aRole);
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditCreateSuccess (CSecurity.TYPE_ROLE, aRole.getID (), "predefind-role", sName);
    return aRole;
  }

  public boolean containsRoleWithID (@Nullable final String sRoleID)
  {
    if (StringHelper.hasNoText (sRoleID))
      return false;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aRoles.containsKey (sRoleID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean containsAllRolesWithID (@Nullable final Collection <String> aRoleIDs)
  {
    if (ContainerHelper.isEmpty (aRoleIDs))
      return true;

    m_aRWLock.readLock ().lock ();
    try
    {
      for (final String sRoleID : aRoleIDs)
        if (!m_aRoles.containsKey (sRoleID))
          return false;
      return true;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public Role getRoleOfID (@Nullable final String sRoleID)
  {
    if (StringHelper.hasNoText (sRoleID))
      return null;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aRoles.get (sRoleID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <? extends IRole> getAllRoles ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aRoles.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public EChange deleteRole (@Nullable final String sRoleID)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aRoles.remove (sRoleID) == null)
      {
        AuditUtils.onAuditDeleteFailure (CSecurity.TYPE_ROLE, "no-such-role-id", sRoleID);
        return EChange.UNCHANGED;
      }
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditDeleteSuccess (CSecurity.TYPE_ROLE, sRoleID);
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange renameRole (@Nullable final String sRoleID, @Nonnull @Nonempty final String sNewName)
  {
    // Resolve user group
    final Role aRole = getRoleOfID (sRoleID);
    if (aRole == null)
    {
      AuditUtils.onAuditModifyFailure (CSecurity.TYPE_ROLE, sRoleID, "no-such-id");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aRole.setName (sNewName).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditModifySuccess (CSecurity.TYPE_ROLE, "name", sRoleID, sNewName);
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange setRoleData (@Nullable final String sRoleID,
                              @Nonnull @Nonempty final String sNewName,
                              @Nullable final Map <String, ?> aNewCustomAttrs)
  {
    // Resolve role
    final Role aRole = getRoleOfID (sRoleID);
    if (aRole == null)
    {
      AuditUtils.onAuditModifyFailure (CSecurity.TYPE_ROLE, sRoleID, "no-such-role-id");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      EChange eChange = aRole.setName (sNewName);
      eChange = eChange.or (aRole.setAttributes (aNewCustomAttrs));
      if (eChange.isUnchanged ())
        return EChange.UNCHANGED;

      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditModifySuccess (CSecurity.TYPE_ROLE,
                                     "all",
                                     aRole.getID (),
                                     sNewName,
                                     StringHelper.getToString (aNewCustomAttrs));
    return EChange.CHANGED;
  }
}
