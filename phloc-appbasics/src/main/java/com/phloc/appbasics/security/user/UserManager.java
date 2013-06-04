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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.impl.AbstractSimpleDAO;
import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.appbasics.security.CSecurity;
import com.phloc.appbasics.security.audit.AuditUtils;
import com.phloc.appbasics.security.user.password.PasswordUtils;
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
 * @author Philip Helger
 */
@ThreadSafe
public final class UserManager extends AbstractSimpleDAO implements IUserManager
{
  private static boolean s_bCreateDefaults = true;
  private final Map <String, User> m_aUsers = new HashMap <String, User> ();

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

  public UserManager (@Nonnull @Nonempty final String sFilename) throws DAOException
  {
    super (sFilename);
    initialRead ();
  }

  @Override
  @Nonnull
  protected EChange onInit ()
  {
    if (!isCreateDefaults ())
      return EChange.UNCHANGED;

    _addUser (new User (CSecurity.USER_ADMINISTRATOR_ID,
                        CSecurity.USER_ADMINISTRATOR_LOGIN,
                        CSecurity.USER_ADMINISTRATOR_EMAIL,
                        PasswordUtils.createUserPasswordHash (CSecurity.USER_ADMINISTRATOR_PASSWORD),
                        CSecurity.USER_ADMINISTRATOR_NAME,
                        null,
                        (Locale) null,
                        (Map <String, String>) null,
                        false));
    _addUser (new User (CSecurity.USER_USER_ID,
                        CSecurity.USER_USER_LOGIN,
                        CSecurity.USER_USER_EMAIL,
                        PasswordUtils.createUserPasswordHash (CSecurity.USER_USER_PASSWORD),
                        CSecurity.USER_USER_NAME,
                        null,
                        (Locale) null,
                        (Map <String, String>) null,
                        false));
    _addUser (new User (CSecurity.USER_GUEST_ID,
                        CSecurity.USER_GUEST_LOGIN,
                        CSecurity.USER_GUEST_EMAIL,
                        PasswordUtils.createUserPasswordHash (CSecurity.USER_GUEST_PASSWORD),
                        CSecurity.USER_GUEST_NAME,
                        null,
                        (Locale) null,
                        (Map <String, String>) null,
                        false));
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  protected EChange onRead (@Nonnull final IMicroDocument aDoc)
  {
    for (final IMicroElement eUser : aDoc.getDocumentElement ().getAllChildElements ())
      _addUser (MicroTypeConverter.convertToNative (eUser, User.class));
    return EChange.UNCHANGED;
  }

  @Override
  @Nonnull
  protected IMicroDocument createWriteData ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement ("users");
    for (final User aUser : ContainerHelper.getSortedByKey (m_aUsers).values ())
      eRoot.appendChild (MicroTypeConverter.convertToMicroElement (aUser, "user"));
    return aDoc;
  }

  private void _addUser (@Nonnull final User aUser)
  {
    final String sUserID = aUser.getID ();
    if (m_aUsers.containsKey (sUserID))
      throw new IllegalArgumentException ("User ID " + sUserID + " is already in use!");
    m_aUsers.put (sUserID, aUser);
  }

  @Nullable
  public IUser createNewUser (@Nonnull @Nonempty final String sLoginName,
                              @Nonnull @Nonempty final String sEmailAddress,
                              @Nonnull final String sPlainTextPassword,
                              @Nullable final String sFirstName,
                              @Nullable final String sLastName,
                              @Nullable final Locale aDesiredLocale,
                              @Nullable final Map <String, String> aCustomAttrs,
                              final boolean bDisabled)
  {
    if (StringHelper.hasNoText (sLoginName))
      throw new IllegalArgumentException ("loginName");
    if (sPlainTextPassword == null)
      throw new IllegalArgumentException ("plainTextPassword");

    if (getUserOfLoginName (sLoginName) != null)
    {
      // Another user with this login name already exists
      AuditUtils.onAuditCreateFailure (CSecurity.TYPE_USER, "login-name-already-in-use", sLoginName);
      return null;
    }

    // Create user
    final User aUser = new User (sLoginName,
                                 sEmailAddress,
                                 PasswordUtils.createUserPasswordHash (sPlainTextPassword),
                                 sFirstName,
                                 sLastName,
                                 aDesiredLocale,
                                 aCustomAttrs,
                                 bDisabled);

    m_aRWLock.writeLock ().lock ();
    try
    {
      _addUser (aUser);
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditCreateSuccess (CSecurity.TYPE_USER,
                                     aUser.getID (),
                                     sLoginName,
                                     sEmailAddress,
                                     sFirstName,
                                     sLastName,
                                     StringHelper.getToString (aDesiredLocale),
                                     StringHelper.getToString (aCustomAttrs),
                                     Boolean.toString (bDisabled));
    return aUser;
  }

  @Nullable
  public IUser createPredefinedUser (@Nonnull @Nonempty final String sID,
                                     @Nonnull @Nonempty final String sLoginName,
                                     @Nonnull @Nonempty final String sEmailAddress,
                                     @Nonnull final String sPlainTextPassword,
                                     @Nullable final String sFirstName,
                                     @Nullable final String sLastName,
                                     @Nullable final Locale aDesiredLocale,
                                     @Nullable final Map <String, String> aCustomAttrs,
                                     final boolean bDisabled)
  {
    if (StringHelper.hasNoText (sLoginName))
      throw new IllegalArgumentException ("loginName");
    if (sPlainTextPassword == null)
      throw new IllegalArgumentException ("plainTextPassword");

    if (getUserOfLoginName (sLoginName) != null)
    {
      // Another user with this login name already exists
      AuditUtils.onAuditCreateFailure (CSecurity.TYPE_USER, "login-name-already-in-use", sLoginName, "predefined-user");
      return null;
    }

    // Create user
    final User aUser = new User (sID,
                                 sLoginName,
                                 sEmailAddress,
                                 PasswordUtils.createUserPasswordHash (sPlainTextPassword),
                                 sFirstName,
                                 sLastName,
                                 aDesiredLocale,
                                 aCustomAttrs,
                                 bDisabled);

    m_aRWLock.writeLock ().lock ();
    try
    {
      _addUser (aUser);
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditCreateSuccess (CSecurity.TYPE_USER,
                                     aUser.getID (),
                                     "predefined-user",
                                     sLoginName,
                                     sEmailAddress,
                                     sFirstName,
                                     sLastName,
                                     StringHelper.getToString (aDesiredLocale),
                                     StringHelper.getToString (aCustomAttrs),
                                     Boolean.toString (bDisabled));
    return aUser;
  }

  public boolean containsUserWithID (@Nullable final String sUserID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aUsers.containsKey (sUserID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public User getUserOfID (@Nullable final String sUserID)
  {
    if (StringHelper.hasNoText (sUserID))
      return null;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aUsers.get (sUserID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public IUser getUserOfLoginName (@Nullable final String sLoginName)
  {
    if (StringHelper.hasNoText (sLoginName))
      return null;

    m_aRWLock.readLock ().lock ();
    try
    {
      for (final User aUser : m_aUsers.values ())
        if (aUser.getLoginName ().equals (sLoginName))
          return aUser;
      return null;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public IUser getUserOfEmailAddress (@Nullable final String sEmailAddress)
  {
    if (StringHelper.hasNoText (sEmailAddress))
      return null;

    m_aRWLock.readLock ().lock ();
    try
    {
      for (final User aUser : m_aUsers.values ())
        if (sEmailAddress.equals (aUser.getEmailAddress ()))
          return aUser;
      return null;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <User> getAllUsers ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aUsers.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <User> getAllActiveUsers ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final List <User> ret = new ArrayList <User> ();
      for (final User aUser : m_aUsers.values ())
        if (!aUser.isDeleted () && aUser.isEnabled ())
          ret.add (aUser);
      return ret;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <User> getAllDisabledUsers ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final List <User> ret = new ArrayList <User> ();
      for (final User aUser : m_aUsers.values ())
        if (!aUser.isDeleted () && aUser.isDisabled ())
          ret.add (aUser);
      return ret;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <User> getAllNotDeletedUsers ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final List <User> ret = new ArrayList <User> ();
      for (final User aUser : m_aUsers.values ())
        if (!aUser.isDeleted ())
          ret.add (aUser);
      return ret;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <User> getAllDeletedUsers ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final List <User> ret = new ArrayList <User> ();
      for (final User aUser : m_aUsers.values ())
        if (aUser.isDeleted ())
          ret.add (aUser);
      return ret;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public EChange setUserData (@Nullable final String sUserID,
                              @Nonnull @Nonempty final String sNewLoginName,
                              @Nonnull @Nonempty final String sNewEmailAddress,
                              @Nullable final String sNewFirstName,
                              @Nullable final String sNewLastName,
                              @Nullable final Locale aNewDesiredLocale,
                              @Nullable final Map <String, String> aNewCustomAttrs,
                              final boolean bNewDisabled)
  {
    // Resolve user
    final User aUser = getUserOfID (sUserID);
    if (aUser == null)
    {
      AuditUtils.onAuditModifyFailure (CSecurity.TYPE_USER, sUserID, "no-such-user-id");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      EChange eChange = aUser.setLoginName (sNewLoginName);
      eChange = eChange.or (aUser.setEmailAddress (sNewEmailAddress));
      eChange = eChange.or (aUser.setFirstName (sNewFirstName));
      eChange = eChange.or (aUser.setLastName (sNewLastName));
      eChange = eChange.or (aUser.setDesiredLocale (aNewDesiredLocale));
      eChange = eChange.or (aUser.setAttributes (aNewCustomAttrs));
      eChange = eChange.or (aUser.setDisabled (bNewDisabled));
      if (eChange.isUnchanged ())
        return EChange.UNCHANGED;

      aUser.updateLastModified ();
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditModifySuccess (CSecurity.TYPE_USER,
                                     "all",
                                     aUser.getID (),
                                     sNewLoginName,
                                     sNewEmailAddress,
                                     sNewFirstName,
                                     sNewLastName,
                                     StringHelper.getToString (aNewDesiredLocale),
                                     StringHelper.getToString (aNewCustomAttrs),
                                     Boolean.toString (bNewDisabled));
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange setUserPassword (@Nullable final String sUserID, @Nonnull final String sNewPlainTextPassword)
  {
    // Resolve user
    final User aUser = getUserOfID (sUserID);
    if (aUser == null)
    {
      AuditUtils.onAuditModifyFailure (CSecurity.TYPE_USER, sUserID, "no-such-user-id", "password");
      return EChange.UNCHANGED;
    }

    final String sPasswordHash = PasswordUtils.createUserPasswordHash (sNewPlainTextPassword);
    m_aRWLock.writeLock ().lock ();
    try
    {
      final EChange eChange = aUser.setPasswordHash (sPasswordHash);
      if (eChange.isUnchanged ())
        return EChange.UNCHANGED;

      aUser.updateLastModified ();
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditModifySuccess (CSecurity.TYPE_USER, "password", sUserID);
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange deleteUser (@Nullable final String sUserID)
  {
    final User aUser = getUserOfID (sUserID);
    if (aUser == null)
    {
      AuditUtils.onAuditDeleteFailure (CSecurity.TYPE_USER, sUserID, "no-such-user-id");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aUser.setDeleted (true).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditDeleteSuccess (CSecurity.TYPE_USER, sUserID);
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange undeleteUser (@Nullable final String sUserID)
  {
    final User aUser = getUserOfID (sUserID);
    if (aUser == null)
    {
      AuditUtils.onAuditUndeleteFailure (CSecurity.TYPE_USER, sUserID, "no-such-user-id");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aUser.setDeleted (false).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditUndeleteSuccess (CSecurity.TYPE_USER, sUserID);
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange disableUser (@Nullable final String sUserID)
  {
    final User aUser = getUserOfID (sUserID);
    if (aUser == null)
    {
      AuditUtils.onAuditModifyFailure (CSecurity.TYPE_USER, sUserID, "no-such-user-id", "disable");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aUser.setDisabled (true).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditModifySuccess (CSecurity.TYPE_USER, "disable", sUserID);
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange enableUser (@Nullable final String sUserID)
  {
    final User aUser = getUserOfID (sUserID);
    if (aUser == null)
    {
      AuditUtils.onAuditModifyFailure (CSecurity.TYPE_USER, sUserID, "no-such-user-id", "enable");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aUser.setDisabled (false).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditModifySuccess (CSecurity.TYPE_USER, "enable", sUserID);
    return EChange.CHANGED;
  }

  public boolean areUserIDAndPasswordValid (@Nullable final String sUserID, @Nullable final String sPlainTextPassword)
  {
    // No password is not allowed
    if (sPlainTextPassword == null)
      return false;

    // Is there such a user?
    final IUser aUser = getUserOfID (sUserID);
    if (aUser == null)
      return false;

    // Now compare the hashes
    final String sPasswordHash = PasswordUtils.createUserPasswordHash (sPlainTextPassword);
    return aUser.getPasswordHash ().equals (sPasswordHash);
  }
}
