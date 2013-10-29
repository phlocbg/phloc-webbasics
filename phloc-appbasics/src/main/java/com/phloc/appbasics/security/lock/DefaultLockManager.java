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
 * @author Philip Helger
 * @param <IDTYPE>
 *        The type of object to be locked. E.g. String or TypedObject. Must
 *        implement equals and hashCode!
 */
@ThreadSafe
public class DefaultLockManager <IDTYPE> implements ILockManager <IDTYPE>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultLockManager.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final ICurrentUserIDProvider m_aCurrentUserIDProvider;

  // Key: lockedObjectID, value: lock-info
  private final Map <IDTYPE, ILockInfo> m_aLockedObjs = new HashMap <IDTYPE, ILockInfo> ();

  public DefaultLockManager (@Nonnull final ICurrentUserIDProvider aCurrentUserIDProvider)
  {
    if (aCurrentUserIDProvider == null)
      throw new NullPointerException ("currentUserIDProvider");
    m_aCurrentUserIDProvider = aCurrentUserIDProvider;
  }

  @Nullable
  public final ILockInfo getLockInfo (@Nullable final IDTYPE aObjID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aLockedObjs.get (aObjID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public final String getLockUserID (@Nullable final IDTYPE aObjID)
  {
    final ILockInfo aLock = getLockInfo (aObjID);
    return aLock != null ? aLock.getLockUserID () : null;
  }

  @Nullable
  public final DateTime getLockDateTime (@Nullable final IDTYPE aObjID)
  {
    final ILockInfo aLock = getLockInfo (aObjID);
    return aLock != null ? aLock.getLockDateTime () : null;
  }

  @Nonnull
  public final ELocked lockObject (@Nonnull final IDTYPE aObjID)
  {
    if (aObjID == null)
      throw new NullPointerException ("objID");

    final String sCurrentUserID = m_aCurrentUserIDProvider.getCurrentUserID ();
    return lockObject (aObjID, sCurrentUserID);
  }

  @Nonnull
  public final ELocked lockObject (@Nonnull final IDTYPE aObjID, @Nullable final String sUserID)
  {
    if (aObjID == null)
      throw new NullPointerException ("objID");

    if (StringHelper.hasNoText (sUserID))
      return ELocked.NOT_LOCKED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      final ILockInfo aCurrentLock = m_aLockedObjs.get (aObjID);
      if (aCurrentLock != null)
      {
        // Object is already locked.
        // Check whether the current user locked the object
        return ELocked.valueOf (aCurrentLock.getLockUserID ().equals (sUserID));
      }

      // Overwrite any existing lock!
      m_aLockedObjs.put (aObjID, new LockInfo (sUserID));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (s_aLogger.isInfoEnabled ())
      s_aLogger.info ("User '" + sUserID + "' locked object '" + aObjID + "'");
    return ELocked.LOCKED;
  }

  @Nonnull
  public final EChange unlockObject (@Nonnull final IDTYPE aObjID)
  {
    final String sCurrentUserID = m_aCurrentUserIDProvider.getCurrentUserID ();
    if (StringHelper.hasNoText (sCurrentUserID))
      return EChange.UNCHANGED;

    return unlockObject (sCurrentUserID, aObjID);
  }

  @Nonnull
  public final EChange unlockObject (@Nonnull final String sUserID, @Nonnull final IDTYPE aObjID)
  {
    if (sUserID == null)
      throw new NullPointerException ("userID");
    if (aObjID == null)
      throw new NullPointerException ("objID");

    final ILockInfo aCurrentLock = getLockInfo (aObjID);

    // Not locked at all?
    if (aCurrentLock == null)
    {
      // This may happen if the user was manually unlocked!
      s_aLogger.warn ("User '" + sUserID + "' could not unlock object '" + aObjID + "' because it is not locked");
      return EChange.UNCHANGED;
    }

    // Not locked by current user?
    if (!aCurrentLock.getLockUserID ().equals (sUserID))
    {
      // This may happen if the user was manually unlocked!
      s_aLogger.warn ("User '" +
                      sUserID +
                      "' could not unlock object '" +
                      aObjID +
                      "' because it is locked by '" +
                      aCurrentLock.getLockUserID () +
                      "'");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      // this user locked the object -> unlock it
      if (m_aLockedObjs.remove (aObjID) == null)
        throw new IllegalStateException ("Internal inconsistency: removing from lock list failed!");
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (s_aLogger.isInfoEnabled ())
      s_aLogger.info ("User '" + sUserID + "' unlocked object '" + aObjID + "'");
    return EChange.CHANGED;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <IDTYPE> unlockAllObjectsOfCurrentUser ()
  {
    return unlockAllObjectsOfCurrentUserExcept (null);
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <IDTYPE> unlockAllObjectsOfCurrentUserExcept (@Nullable final Set <IDTYPE> aObjectsToKeepLocked)
  {
    return unlockAllObjectsOfUserExcept (m_aCurrentUserIDProvider.getCurrentUserID (), aObjectsToKeepLocked);
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <IDTYPE> unlockAllObjectsOfUser (@Nullable final String sUserID)
  {
    return unlockAllObjectsOfUserExcept (sUserID, null);
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <IDTYPE> unlockAllObjectsOfUserExcept (@Nullable final String sUserID,
                                                           @Nullable final Set <IDTYPE> aObjectsToKeepLocked)
  {
    final List <IDTYPE> aObjectsToUnlock = new ArrayList <IDTYPE> ();
    if (StringHelper.hasText (sUserID))
    {
      m_aRWLock.writeLock ().lock ();
      try
      {
        // determine locks to be removed
        for (final Map.Entry <IDTYPE, ILockInfo> aEntry : m_aLockedObjs.entrySet ())
        {
          final String sLockUserID = aEntry.getValue ().getLockUserID ();
          if (sLockUserID.equals (sUserID))
          {
            // Object is locked by current user
            final IDTYPE aObjID = aEntry.getKey ();
            if (aObjectsToKeepLocked == null || !aObjectsToKeepLocked.contains (aObjID))
              aObjectsToUnlock.add (aObjID);
          }
        }

        // remove locks
        for (final IDTYPE aObjID : aObjectsToUnlock)
          if (m_aLockedObjs.remove (aObjID) == null)
            throw new IllegalStateException ("Internal inconsistency: failed to unlock '" +
                                             aObjID +
                                             "' from " +
                                             aObjectsToUnlock);
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

  public final boolean isObjectLockedByCurrentUser (@Nullable final IDTYPE aObjID)
  {
    final String sLockUserID = getLockUserID (aObjID);
    if (sLockUserID == null)
    {
      // Object is not locked at all
      return false;
    }
    final String sCurrentUserID = m_aCurrentUserIDProvider.getCurrentUserID ();
    return sLockUserID.equals (sCurrentUserID);
  }

  public final boolean isObjectLockedByOtherUser (@Nullable final IDTYPE aObjID)
  {
    final String sLockUser = getLockUserID (aObjID);
    if (sLockUser == null)
    {
      // Object is not locked at all
      return false;
    }
    final String sCurrentUserID = m_aCurrentUserIDProvider.getCurrentUserID ();
    return !sLockUser.equals (sCurrentUserID);
  }

  public final boolean isObjectLockedByAnyUser (@Nullable final IDTYPE aObjID)
  {
    return getLockUserID (aObjID) != null;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Set <IDTYPE> getAllLockedObjects ()
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
