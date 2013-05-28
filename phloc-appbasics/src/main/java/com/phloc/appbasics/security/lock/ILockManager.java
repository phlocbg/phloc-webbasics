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
package com.phloc.appbasics.security.lock;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Base interface for a manager that handles object locking. See
 * {@link DefaultLockManager} for a per-instance implementation and
 * {@link ObjectLockManager} for a singleton version.
 * 
 * @author Philip Helger
 */
public interface ILockManager
{
  /**
   * Get the lock information of the given object.
   * 
   * @param sObjID
   *        The object to query for lock owner.
   * @return <code>null</code> if the object is not locked, the lock information
   *         otherwise
   */
  @Nullable
  ILockInfo getLockInfo (@Nullable String sObjID);

  /**
   * Get the user ID who locked the given object.
   * 
   * @param sObjID
   *        The object to query for lock owner.
   * @return <code>null</code> if the object is not locked, the user ID
   *         otherwise
   */
  @Nullable
  String getLockUserID (@Nullable String sObjID);

  /**
   * Get the date and time when the given object was locked.
   * 
   * @param sObjID
   *        The object to query for lock owner.
   * @return <code>null</code> if the object is not locked, the locking date
   *         time otherwise
   */
  @Nullable
  DateTime getLockDateTime (@Nullable String sObjID);

  /**
   * Lock the object with the given ID. If the passed object is already locked
   * by this user, this method has no effect. This is an atomic action.
   * 
   * @param sObjID
   *        The object ID to lock. May not be <code>null</code>.
   * @return {@link ELocked#LOCKED} if the object is locked by the current user
   *         after the call to this method, {@link ELocked#NOT_LOCKED} if the
   *         object was already locked by another user.
   */
  @Nonnull
  ELocked lockObject (@Nonnull String sObjID);

  /**
   * Lock the object with the given ID. If the passed object is already locked
   * by this user, this method has no effect. This is an atomic action.
   * 
   * @param sObjID
   *        The object ID to lock. May not be <code>null</code>.
   * @param sUserID
   *        The id of the user who locked the object. May be <code>null</code>.
   * @return {@link ELocked#LOCKED} if the object is locked by the specified
   *         user after the call to this method, {@link ELocked#NOT_LOCKED} if
   *         the object was already locked by another user or no user ID was
   *         provided.
   */
  @Nonnull
  ELocked lockObject (@Nonnull String sObjID, @Nullable String sUserID);

  /**
   * Unlock the object with the given ID. Unlocking is only possible, if the
   * current session user locked the object.
   * 
   * @param sObjID
   *        The object ID to unlock.
   * @return <code>true</code> if the object was successfully unlocked,
   *         <code>false</code> if either the object is not locked or the object
   *         is locked by another user than the current session user.
   */
  @Nonnull
  EChange unlockObject (@Nonnull String sObjID);

  /**
   * Manually unlock a special object locked by a special user. This manual
   * version is only required for especially unlocking a user!
   * 
   * @param sUserID
   *        The user who locked the object.
   * @param sObjID
   *        The object to be unlocked.
   * @return <code>true</code> if unlocking succeeded, <code>false</code>
   *         otherwise.
   */
  @Nonnull
  EChange unlockObject (@Nonnull String sUserID, @Nonnull String sObjID);

  /**
   * Unlock all objects of the current user.
   * 
   * @return The list of all unlocked object IDs. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> unlockAllObjectsOfCurrentUser ();

  /**
   * Unlock all objects of the current user except for the passed objects.
   * 
   * @param aObjectsToKeepLocked
   *        An optional set of objects which should not be unlocked. May be
   *        <code>null</code> or empty.
   * @return The list of all unlocked object IDs. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> unlockAllObjectsOfCurrentUserExcept (@Nullable Set <String> aObjectsToKeepLocked);

  /**
   * Unlock all objects of the passed user.
   * 
   * @param sUserID
   *        The user ID who's object are to be unlocked. May be
   *        <code>null</code> or empty.
   * @return The list of all unlocked object IDs. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> unlockAllObjectsOfUser (@Nullable String sUserID);

  /**
   * Unlock all objects of the passed user except for the passed objects.
   * 
   * @param sUserID
   *        The user ID who's object are to be unlocked. May be
   *        <code>null</code> or empty.
   * @param aObjectsToKeepLocked
   *        An optional set of objects which should not be unlocked. May be
   *        <code>null</code> or empty.
   * @return The list of all unlocked object IDs. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> unlockAllObjectsOfUserExcept (@Nullable String sUserID, @Nullable Set <String> aObjectsToKeepLocked);

  /**
   * Check if the object with the given ID is locked by the current user.
   * 
   * @param sObjID
   *        The object ID to check.
   * @return <code>true</code> if the object is locked by the current user,
   *         <code>false</code> if the object is either not locked or locked by
   *         another user.
   */
  boolean isObjectLockedByCurrentUser (@Nullable String sObjID);

  /**
   * Check if the object with the given ID is locked by any but the current
   * user.
   * 
   * @param sObjID
   *        The object ID to check.
   * @return <code>true</code> if the object is locked by any user that is not
   *         the currently logged in user, <code>false</code> if the object is
   *         either not locked or locked by the current user.
   */
  boolean isObjectLockedByOtherUser (@Nullable String sObjID);

  /**
   * Check if the object with the given ID is locked by any user.
   * 
   * @param sObjID
   *        The object ID to check.
   * @return <code>true</code> if the object is locked by any user,
   *         <code>false</code> if the object is not locked.
   */
  boolean isObjectLockedByAnyUser (@Nullable String sObjID);

  /**
   * @return A non-<code>null</code> set of all locked objects. Never
   *         <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <String> getAllLockedObjects ();
}
