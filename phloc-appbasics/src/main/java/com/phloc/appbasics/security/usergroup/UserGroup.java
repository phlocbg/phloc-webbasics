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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.appbasics.security.CSecurity;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
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
  private final String m_sID;
  private String m_sName;
  private final Set <String> m_aUserIDs = new HashSet <String> ();
  private final Set <String> m_aRoleIDs = new HashSet <String> ();

  public UserGroup (@Nonnull @Nonempty final String sName)
  {
    this (sName, (Map <String, String>) null);
  }

  public UserGroup (@Nonnull @Nonempty final String sName, @Nullable final Map <String, String> aCustomAttrs)
  {
    this (GlobalIDFactory.getNewPersistentStringID (), sName, aCustomAttrs);
  }

  UserGroup (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    this (sID, sName, (Map <String, String>) null);
  }

  UserGroup (@Nonnull @Nonempty final String sID,
             @Nonnull @Nonempty final String sName,
             @Nullable final Map <String, String> aCustomAttrs)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    m_sID = sID;
    m_sName = sName;
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
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
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
  @ReturnsImmutableObject
  public Set <String> getAllContainedUserIDs ()
  {
    return ContainerHelper.makeUnmodifiable (m_aUserIDs);
  }

  public boolean containsUserID (final String sUserID)
  {
    return m_aUserIDs.contains (sUserID);
  }

  @Nonnull
  EChange assignUser (@Nonnull final String sUserID)
  {
    if (StringHelper.hasNoText (sUserID))
      throw new IllegalArgumentException ("userID");

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
  @ReturnsImmutableObject
  public Set <String> getAllContainedRoleIDs ()
  {
    return ContainerHelper.makeUnmodifiable (m_aRoleIDs);
  }

  public boolean containsRoleID (final String sRoleID)
  {
    return m_aRoleIDs.contains (sRoleID);
  }

  @Nonnull
  EChange assignRole (@Nonnull final String sRoleID)
  {
    if (StringHelper.hasNoText (sRoleID))
      throw new IllegalArgumentException ("userID");

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
