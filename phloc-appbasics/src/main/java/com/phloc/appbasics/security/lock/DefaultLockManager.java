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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.security.login.ICurrentUserIDProvider;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of a locking manager.
 * 
 * @author philip
 */
@ThreadSafe
public class DefaultLockManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultLockManager.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final ICurrentUserIDProvider m_aCurrentUserIDProvider;

  // Key: lockedObjectID, value: lock-info
  private final Map <String, ILockInfo> m_aLockedObjs = new HashMap <String, ILockInfo> ();

  public DefaultLockManager (@Nonnull final ICurrentUserIDProvider aCurrentUserIDProvider)
  {
    if (aCurrentUserIDProvider == null)
      throw new NullPointerException ("currentUserIDProvider");
    m_aCurrentUserIDProvider = aCurrentUserIDProvider;
  }

  /**
   * Get the lock information of the the given object.
   * 
   * @param sObjID
   *        The object to query for lock owner.
   * @return <code>null</code> if the object is not locked, the lock information
   *         otherwise
   */
  @Nullable
  public final ILockInfo getLockInfo (@Nullable final String sObjID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aLockedObjs.get (sObjID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Get the user ID who locked the given object.
   * 
   * @param sObjID
   *        The object to query for lock owner.
   * @return <code>null</code> if the object is not locked, the user ID
   *         otherwise
   */
  @Nullable
  public final String getLockUserID (@Nullable final String sObjID)
  {
    final ILockInfo aLock = getLockInfo (sObjID);
    return aLock != null ? aLock.getLockUserID () : null;
  }

  /**
   * Get the date and time when the given object was locked.
   * 
   * @param sObjID
   *        The object to query for lock owner.
   * @return <code>null</code> if the object is not locked, the locking date
   *         time otherwise
   */
  @Nullable
  public final DateTime getLockDateTime (@Nullable final String sObjID)
  {
    final ILockInfo aLock = getLockInfo (sObjID);
    return aLock != null ? aLock.getLockDateTime () : null;
  }

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
  public final ELocked lockObject (@Nonnull final String sObjID)
  {
    if (sObjID == null)
      throw new NullPointerException ("objID");

    final String sCurrentUserID = m_aCurrentUserIDProvider.getCurrentUserID ();
    return lockObject (sObjID, sCurrentUserID);
  }

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
  public final ELocked lockObject (@Nonnull final String sObjID, @Nullable final String sUserID)
  {
    if (sObjID == null)
      throw new NullPointerException ("objID");

    if (StringHelper.hasNoText (sUserID))
      return ELocked.NOT_LOCKED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      final ILockInfo aCurrentLock = m_aLockedObjs.get (sObjID);
      if (aCurrentLock != null)
      {
        // Object is already locked.
        // Check whether the current user locked the object
        return ELocked.valueOf (aCurrentLock.getLockUserID ().equals (sUserID));
      }

      // Overwrite any existing lock!
      m_aLockedObjs.put (sObjID, new LockInfo (sUserID));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (s_aLogger.isInfoEnabled ())
      s_aLogger.info ("User '" + sUserID + "' locked object '" + sObjID + "'");
    return ELocked.LOCKED;
  }

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
  public final EChange unlockObject (@Nonnull final String sObjID)
  {
    final String sCurrentUserID = m_aCurrentUserIDProvider.getCurrentUserID ();
    if (StringHelper.hasNoText (sCurrentUserID))
      return EChange.UNCHANGED;

    return unlockObject (sCurrentUserID, sObjID);
  }

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
  public final EChange unlockObject (@Nonnull final String sUserID, @Nonnull final String sObjID)
  {
    if (sUserID == null)
      throw new NullPointerException ("userID");
    if (sObjID == null)
      throw new NullPointerException ("objID");

    final ILockInfo aCurrentLock = getLockInfo (sObjID);

    // Not locked at all?
    if (aCurrentLock == null)
    {
      // This may happen if the user was manually unlocked!
      s_aLogger.warn ("User '" + sUserID + "' could not unlock object '" + sObjID + "' because it is not locked");
      return EChange.UNCHANGED;
    }

    // Not locked by current user?
    if (!aCurrentLock.getLockUserID ().equals (sUserID))
    {
      // This may happen if the user was manually unlocked!
      s_aLogger.warn ("User '" +
                      sUserID +
                      "' could not unlock object '" +
                      sObjID +
                      "' because it is locked by '" +
                      aCurrentLock.getLockUserID () +
                      "'");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      // this user locked the object -> unlock it
      if (m_aLockedObjs.remove (sObjID) == null)
        throw new IllegalStateException ("Internal inconsistency: removing from lock list failed!");
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (s_aLogger.isInfoEnabled ())
      s_aLogger.info ("User '" + sUserID + "' unlocked object '" + sObjID + "'");
    return EChange.CHANGED;
  }

  /**
   * Unlock all objects of the current user.
   * 
   * @return The list of all unlocked object IDs. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public final List <String> unlockAllObjectsOfCurrentUser ()
  {
    return unlockAllObjectsOfCurrentUserExcept (null);
  }

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
  public final List <String> unlockAllObjectsOfCurrentUserExcept (@Nullable final Set <String> aObjectsToKeepLocked)
  {
    return unlockAllObjectsOfUserExcept (m_aCurrentUserIDProvider.getCurrentUserID (), aObjectsToKeepLocked);
  }

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
  public final List <String> unlockAllObjectsOfUser (@Nullable final String sUserID)
  {
    return unlockAllObjectsOfUserExcept (sUserID, null);
  }

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
  public final List <String> unlockAllObjectsOfUserExcept (@Nullable final String sUserID,
                                                           @Nullable final Set <String> aObjectsToKeepLocked)
  {
    final List <String> aObjectsToUnlock = new ArrayList <String> ();
    if (StringHelper.hasText (sUserID))
    {
      m_aRWLock.writeLock ().lock ();
      try
      {
        // determine objects to be removed
        for (final Map.Entry <String, ILockInfo> aEntry : m_aLockedObjs.entrySet ())
        {
          final String sLockUserID = aEntry.getValue ().getLockUserID ();
          if (sLockUserID.equals (sUserID))
          {
            // Object is locked by current user
            final String sObjID = aEntry.getKey ();
            if (aObjectsToKeepLocked == null || !aObjectsToKeepLocked.contains (sObjID))
              aObjectsToUnlock.add (sObjID);
          }
        }

        // remove locks
        for (final String sObjectID : aObjectsToUnlock)
          if (m_aLockedObjs.remove (sObjectID) == null)
            throw new IllegalStateException ("Internal inconsistency: failed to unlock " + aObjectsToUnlock);
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }

      if (!aObjectsToUnlock.isEmpty ())
        if (s_aLogger.isInfoEnabled ())
          s_aLogger.info ("Unlocked all objects of user '" + sUserID + "': " + aObjectsToUnlock);
    }
    return aObjectsToUnlock;
  }

  /**
   * Check if the object with the given ID is locked by the current user.
   * 
   * @param sObjID
   *        The object ID to check.
   * @return <code>true</code> if the object is locked by the current user,
   *         <code>false</code> if the object is either not locked or locked by
   *         another user.
   */
  public final boolean isObjectLockedByCurrentUser (@Nullable final String sObjID)
  {
    final String sLockUserID = getLockUserID (sObjID);
    if (sLockUserID == null)
    {
      // Object is not locked at all
      return false;
    }
    final String sCurrentUserID = m_aCurrentUserIDProvider.getCurrentUserID ();
    return sLockUserID.equals (sCurrentUserID);
  }

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
  public final boolean isObjectLockedByOtherUser (@Nullable final String sObjID)
  {
    final String sLockUser = getLockUserID (sObjID);
    if (sLockUser == null)
    {
      // Object is not locked at all
      return false;
    }
    final String sCurrentUserID = m_aCurrentUserIDProvider.getCurrentUserID ();
    return !sLockUser.equals (sCurrentUserID);
  }

  /**
   * Check if the object with the given ID is locked by any user.
   * 
   * @param sObjID
   *        The object ID to check.
   * @return <code>true</code> if the object is locked by any user,
   *         <code>false</code> if the object is not locked.
   */
  public final boolean isObjectLockedByAnyUser (@Nullable final String sObjID)
  {
    return getLockUserID (sObjID) != null;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Set <String> getAllLockedObjects ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (m_aLockedObjs.keySet ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("currentUserIDProvider", m_aCurrentUserIDProvider)
                                       .append ("lockedObjects", m_aLockedObjs)
                                       .toString ();
  }
}
