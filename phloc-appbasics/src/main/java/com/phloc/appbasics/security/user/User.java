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
package com.phloc.appbasics.security.user;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.joda.time.DateTime;

import com.phloc.appbasics.security.CSecurity;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;
import com.phloc.datetime.PDTFactory;

/**
 * Default implementation of the {@link IUser} interface.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class User extends MapBasedAttributeContainer implements IUser
{
  private final String m_sID;
  private final DateTime m_aCreationDT;
  private DateTime m_aLastModificationDT;
  private DateTime m_aDeletionDT;
  private String m_sLoginName;
  private String m_sEmailAddress;
  private String m_sPasswordHash;
  private String m_sFirstName;
  private String m_sLastName;
  private Locale m_aDesiredLocale;
  private boolean m_bDeleted;
  private boolean m_bDisabled;

  /**
   * Create a new user
   * 
   * @param sLoginName
   *        Login name of the user. May neither be <code>null</code> nor empty.
   * @param sEmailAddress
   *        Email address of the user. May neither be <code>null</code> nor
   *        empty.
   * @param sPasswordHash
   *        Password hash of the user. May neither be <code>null</code> nor
   *        empty.
   * @param sFirstName
   *        The first name. May be <code>null</code>.
   * @param sLastName
   *        The last name. May be <code>null</code>.
   * @param aDesiredLocale
   *        The desired locale. May be <code>null</code>.
   * @param aCustomAttrs
   *        Custom attributes. May be <code>null</code>.
   * @param bDisabled
   *        <code>true</code> if the user is disabled
   */
  public User (@Nonnull @Nonempty final String sLoginName,
               @Nonnull @Nonempty final String sEmailAddress,
               @Nonnull @Nonempty final String sPasswordHash,
               @Nullable final String sFirstName,
               @Nullable final String sLastName,
               @Nullable final Locale aDesiredLocale,
               @Nullable final Map <String, String> aCustomAttrs,
               final boolean bDisabled)
  {
    this (GlobalIDFactory.getNewPersistentStringID (),
          PDTFactory.getCurrentDateTime (),
          null,
          null,
          sLoginName,
          sEmailAddress,
          sPasswordHash,
          sFirstName,
          sLastName,
          aDesiredLocale,
          aCustomAttrs,
          false,
          bDisabled);
  }

  // Internal use only
  User (@Nonnull @Nonempty final String sID,
        @Nonnull @Nonempty final String sLoginName,
        @Nonnull @Nonempty final String sEmailAddress,
        @Nonnull @Nonempty final String sPasswordHash,
        @Nullable final String sFirstName,
        @Nullable final String sLastName,
        @Nullable final Locale aDesiredLocale,
        @Nullable final Map <String, String> aCustomAttrs,
        final boolean bDisabled)
  {
    this (sID,
          PDTFactory.getCurrentDateTime (),
          null,
          null,
          sLoginName,
          sEmailAddress,
          sPasswordHash,
          sFirstName,
          sLastName,
          aDesiredLocale,
          aCustomAttrs,
          false,
          bDisabled);
  }

  /**
   * For deserialization only.
   * 
   * @param sID
   *        user ID
   * @param aCreationDT
   *        The creation date and time
   * @param aLastModificationDT
   *        The last modification date and time
   * @param aDeletionDT
   *        The deletion date and time
   * @param sLoginName
   *        Login name of the user. May neither be <code>null</code> nor empty.
   * @param sEmailAddress
   *        Email address of the user. May neither be <code>null</code> nor
   *        empty.
   * @param sPasswordHash
   *        Password hash of the user. May neither be <code>null</code> nor
   *        empty.
   * @param sFirstName
   *        The first name. May be <code>null</code>.
   * @param sLastName
   *        The last name. May be <code>null</code>.
   * @param aDesiredLocale
   *        The desired locale. May be <code>null</code>.
   * @param aCustomAttrs
   *        Custom attributes. May be <code>null</code>.
   * @param bDeleted
   *        <code>true</code> if the user is deleted, <code>false</code> if nto
   * @param bDisabled
   *        <code>true</code> if the user is disabled
   */
  User (@Nonnull @Nonempty final String sID,
        @Nonnull final DateTime aCreationDT,
        @Nullable final DateTime aLastModificationDT,
        @Nullable final DateTime aDeletionDT,
        @Nonnull @Nonempty final String sLoginName,
        @Nonnull @Nonempty final String sEmailAddress,
        @Nonnull @Nonempty final String sPasswordHash,
        @Nullable final String sFirstName,
        @Nullable final String sLastName,
        @Nullable final Locale aDesiredLocale,
        @Nullable final Map <String, String> aCustomAttrs,
        final boolean bDeleted,
        final boolean bDisabled)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    if (aCreationDT == null)
      throw new NullPointerException ("creationDT");
    if (StringHelper.hasNoText (sLoginName))
      throw new IllegalArgumentException ("loginName");
    if (StringHelper.hasNoText (sEmailAddress))
      throw new IllegalArgumentException ("emailAddress");
    if (StringHelper.hasNoText (sPasswordHash))
      throw new IllegalArgumentException ("passwordHash");
    m_sID = sID;
    m_aCreationDT = aCreationDT;
    m_aLastModificationDT = aLastModificationDT;
    m_aDeletionDT = aDeletionDT;
    m_sLoginName = sLoginName;
    m_sEmailAddress = sEmailAddress;
    m_sPasswordHash = sPasswordHash;
    m_sFirstName = sFirstName;
    m_sLastName = sLastName;
    m_aDesiredLocale = aDesiredLocale;
    setAttributes (aCustomAttrs);
    m_bDeleted = bDeleted;
    m_bDisabled = bDisabled;
  }

  @Nonnull
  public ObjectType getTypeID ()
  {
    return CSecurity.TYPE_USER;
  }

  public boolean isAdministrator ()
  {
    return CSecurity.USER_ADMINISTRATOR_ID.equals (m_sID);
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public DateTime getCreationDateTime ()
  {
    return m_aCreationDT;
  }

  @Nullable
  public DateTime getLastModificationDateTime ()
  {
    return m_aLastModificationDT;
  }

  void updateLastModified ()
  {
    m_aLastModificationDT = PDTFactory.getCurrentDateTime ();
  }

  @Nullable
  public DateTime getDeletionDateTime ()
  {
    return m_aDeletionDT;
  }

  @Nonnull
  @Nonempty
  public final String getName ()
  {
    return getLoginName ();
  }

  @Nonnull
  @Nonempty
  public String getLoginName ()
  {
    return m_sLoginName;
  }

  @Nonnull
  EChange setLoginName (@Nullable final String sLoginName)
  {
    if (StringHelper.hasNoText (sLoginName))
      throw new IllegalArgumentException ("loginName");

    if (sLoginName.equals (m_sLoginName))
      return EChange.UNCHANGED;
    m_sLoginName = sLoginName;
    return EChange.CHANGED;
  }

  @Nonnull
  @Nonempty
  public String getEmailAddress ()
  {
    return m_sEmailAddress;
  }

  @Nonnull
  EChange setEmailAddress (@Nullable final String sEmailAddress)
  {
    if (StringHelper.hasNoText (sEmailAddress))
      throw new IllegalArgumentException ("emailAddress");

    if (sEmailAddress.equals (m_sEmailAddress))
      return EChange.UNCHANGED;
    m_sEmailAddress = sEmailAddress;
    return EChange.CHANGED;
  }

  @Nonnull
  @Nonempty
  public String getPasswordHash ()
  {
    return m_sPasswordHash;
  }

  @Nonnull
  EChange setPasswordHash (@Nullable final String sPasswordHash)
  {
    if (StringHelper.hasNoText (sPasswordHash))
      throw new IllegalArgumentException ("passwordHash");

    if (sPasswordHash.equals (m_sPasswordHash))
      return EChange.UNCHANGED;
    m_sPasswordHash = sPasswordHash;
    return EChange.CHANGED;
  }

  @Nullable
  public String getFirstName ()
  {
    return m_sFirstName;
  }

  @Nonnull
  EChange setFirstName (@Nullable final String sFirstName)
  {
    if (EqualsUtils.equals (sFirstName, m_sFirstName))
      return EChange.UNCHANGED;
    m_sFirstName = sFirstName;
    return EChange.CHANGED;
  }

  @Nullable
  public String getLastName ()
  {
    return m_sLastName;
  }

  @Nonnull
  EChange setLastName (@Nullable final String sLastName)
  {
    if (EqualsUtils.equals (sLastName, m_sLastName))
      return EChange.UNCHANGED;
    m_sLastName = sLastName;
    return EChange.CHANGED;
  }

  @Nullable
  public Locale getDesiredLocale ()
  {
    return m_aDesiredLocale;
  }

  @Nonnull
  EChange setDesiredLocale (@Nullable final Locale aDesiredLocale)
  {
    if (EqualsUtils.equals (aDesiredLocale, m_aDesiredLocale))
      return EChange.UNCHANGED;
    m_aDesiredLocale = aDesiredLocale;
    return EChange.CHANGED;
  }

  @Nonnull
  public String getDisplayName ()
  {
    return StringHelper.getConcatenatedOnDemand (m_sFirstName, " ", m_sLastName);
  }

  public boolean isDeleted ()
  {
    return m_bDeleted;
  }

  @Nonnull
  EChange setDeleted (final boolean bDeleted)
  {
    if (bDeleted == m_bDeleted)
      return EChange.UNCHANGED;
    m_bDeleted = bDeleted;
    m_aDeletionDT = bDeleted ? PDTFactory.getCurrentDateTime () : null;
    return EChange.CHANGED;
  }

  public boolean isDisabled ()
  {
    return m_bDisabled;
  }

  @Nonnull
  EChange setDisabled (final boolean bDisabled)
  {
    if (bDisabled == m_bDisabled)
      return EChange.UNCHANGED;
    m_bDisabled = bDisabled;
    return EChange.CHANGED;
  }

  public boolean isEnabled ()
  {
    return !m_bDisabled;
  }

  @Nonnull
  EChange setEnabled (final boolean bEnabled)
  {
    return setDisabled (!bEnabled);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final User rhs = (User) o;
    return m_sID.equals (rhs.m_sID);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_sID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("ID", m_sID)
                            .append ("creationDT", m_aCreationDT)
                            .appendIfNotNull ("lastModificationDT", m_aLastModificationDT)
                            .appendIfNotNull ("deletionDT", m_aDeletionDT)
                            .append ("loginName", m_sLoginName)
                            .append ("emailAddress", m_sEmailAddress)
                            .append ("passwordHash", m_sPasswordHash)
                            .append ("firstName", m_sFirstName)
                            .append ("lastName", m_sLastName)
                            .append ("desiredLocale", m_aDesiredLocale)
                            .append ("deleted", m_bDeleted)
                            .append ("disabled", m_bDisabled)
                            .toString ();
  }
}
