/**
 * Copyright (C) 2006-2014 phloc systems
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.appbasics.security.CSecurity;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;

/**
 * Default implementation of the {@link IUserGroup} interface.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class UserGroup extends MapBasedAttributeContainer implements IUserGroup
{
  @Nonnull
  private final String m_sID;
  @Nonnull
  private String m_sName;
  @Nonnull
  private final Set <String> m_aUserIDs = new HashSet <String> ();
  @Nonnull
  private final Set <String> m_aRoleIDs = new HashSet <String> ();

  public UserGroup (@Nonnull @Nonempty final String sName)
  {
    this (sName, (Map <String, ?>) null);
  }

  public UserGroup (@Nonnull @Nonempty final String sName, @Nullable final Map <String, ?> aCustomAttrs)
  {
    this (GlobalIDFactory.getNewPersistentStringID (), sName, aCustomAttrs);
  }

  UserGroup (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    this (sID, sName, (Map <String, ?>) null);
  }

  UserGroup (@Nonnull @Nonempty final String sID,
             @Nonnull @Nonempty final String sName,
             @Nullable final Map <String, ?> aCustomAttrs)
  {
    m_sID = ValueEnforcer.notEmpty (sID, "ID");
    setName (sName);
    setAttributes (aCustomAttrs);
  }

  @Nonnull
  public ObjectType getTypeID ()
  {
    return CSecurity.TYPE_USERGROUP;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  EChange setName (@Nonnull @Nonempty final String sName)
  {
    ValueEnforcer.notEmpty (sName, "Name");

    if (sName.equals (m_sName))
      return EChange.UNCHANGED;
    m_sName = sName;
    return EChange.CHANGED;
  }

  public boolean hasContainedUsers ()
  {
    return !m_aUserIDs.isEmpty ();
  }

  @Nonnegative
  public int getContainedUserCount ()
  {
    return m_aUserIDs.size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllContainedUserIDs ()
  {
    return ContainerHelper.newSet (m_aUserIDs);
  }

  public boolean containsUserID (final String sUserID)
  {
    return m_aUserIDs.contains (sUserID);
  }

  @Nonnull
  EChange assignUser (@Nonnull final String sUserID)
  {
    ValueEnforcer.notEmpty (sUserID, "UserID");

    return EChange.valueOf (m_aUserIDs.add (sUserID));
  }

  @Nonnull
  EChange unassignUser (@Nonnull final String sUserID)
  {
    return EChange.valueOf (m_aUserIDs.remove (sUserID));
  }

  public boolean hasContainedRoles ()
  {
    return !m_aRoleIDs.isEmpty ();
  }

  @Nonnegative
  public int getContainedRoleCount ()
  {
    return m_aRoleIDs.size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllContainedRoleIDs ()
  {
    return ContainerHelper.newSet (m_aRoleIDs);
  }

  public boolean containsRoleID (final String sRoleID)
  {
    return m_aRoleIDs.contains (sRoleID);
  }

  @Nonnull
  EChange assignRole (@Nonnull final String sRoleID)
  {
    ValueEnforcer.notEmpty (sRoleID, "RoleID");

    return EChange.valueOf (m_aRoleIDs.add (sRoleID));
  }

  @Nonnull
  EChange unassignRole (@Nonnull final String sRoleID)
  {
    return EChange.valueOf (m_aRoleIDs.remove (sRoleID));
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof UserGroup))
      return false;
    final UserGroup rhs = (UserGroup) o;
    return m_sID.equals (rhs.m_sID);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("ID", m_sID)
                            .append ("name", m_sName)
                            .append ("assignedUsers", m_aUserIDs)
                            .append ("assignedRoles", m_aRoleIDs)
                            .toString ();
  }
}
