package com.phloc.webbasics.security.lock;

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

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;
import com.phloc.webbasics.security.login.LoggedInUserManager;

/**
 * Default implementation of a locking manager.
 * 
 * @author philip
 */
@ThreadSafe
public class DefaultLockManager extends GlobalSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultLockManager.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  // Key: lockedObjectID
  private final Map <String, ILockInfo> m_aLockedObjs = new HashMap <String, ILockInfo> ();

  @Deprecated
  @UsedViaReflection
  public DefaultLockManager ()
  {}

  @Nonnull
  public static DefaultLockManager getInstance ()
  {
    return getGlobalSingleton (DefaultLockManager.class);
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

  private static String _getCurrentUserID ()
  {
    return LoggedInUserManager.getInstance ().getCurrentUserID ();
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

    final String sUserID = _getCurrentUserID ();

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
    return unlockObject (_getCurrentUserID (), sObjID);
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

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> unlockAllObjectsOfCurrentUser ()
  {
    return unlockAllObjectsOfUser (_getCurrentUserID ());
  }

  /**
   * Unlock all objects of the current user.
   * 
   * @param sUserID
   *        The user ID who's object are to be unlocked.
   * @return The list of all unlocked object IDs.
   */
  @Nonnull
  @ReturnsMutableCopy
  public final List <String> unlockAllObjectsOfUser (@Nullable final String sUserID)
  {
    final List <String> aObjectsToUnlock = new ArrayList <String> ();
    m_aRWLock.writeLock ().lock ();
    try
    {
      // determine objects to be removed
      for (final Map.Entry <String, ILockInfo> aEntry : m_aLockedObjs.entrySet ())
        if (aEntry.getValue ().getLockUserID ().equals (sUserID))
          aObjectsToUnlock.add (aEntry.getKey ());

      // remove locks
      for (final String sObjectID : aObjectsToUnlock)
        if (m_aLockedObjs.remove (sObjectID) == null)
          throw new IllegalStateException ("Internal inconsistency: failed to unlock " + aObjectsToUnlock);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (s_aLogger.isInfoEnabled ())
      if (!aObjectsToUnlock.isEmpty ())
        s_aLogger.info ("Unlocked all objects of user '" + sUserID + "': " + aObjectsToUnlock);
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
    return _getCurrentUserID ().equals (getLockUserID (sObjID));
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
    return sLockUser != null && !_getCurrentUserID ().equals (sLockUser);
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
    return new ToStringGenerator (this).append ("lockedObjects", m_aLockedObjs).toString ();
  }
}
