/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.appbasics.security.login;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;
import com.phloc.scopes.nonweb.singleton.SessionSingleton;

/**
 * This class manages all logged-in users.
 * 
 * @author philip
 */
@ThreadSafe
public final class LoggedInUserManager extends GlobalSingleton
{
  /**
   * This class manages the user ID of the current session
   * 
   * @author philip
   */
  @edu.umd.cs.findbugs.annotations.SuppressWarnings ("SE_NO_SERIALVERSIONID")
  public static final class SessionUserHolder extends SessionSingleton
  {
    private IUser m_aUser;
    private String m_sUserID;
    private transient LoggedInUserManager m_aOwningMgr;

    @Deprecated
    @UsedViaReflection
    public SessionUserHolder ()
    {}

    @Nonnull
    public static SessionUserHolder getInstance ()
    {
      return getSessionSingleton (SessionUserHolder.class);
    }

    @Nonnull
    public EChange setUser (@Nonnull final LoggedInUserManager aOwningMgr, @Nonnull final IUser aUser)
    {
      if (aUser == null)
        throw new NullPointerException ("user");
      if (m_aUser != null)
      {
        s_aLogger.warn ("The session user holder already has the user ID '" +
                        m_sUserID +
                        "' so the new ID '" +
                        aUser.getID () +
                        "' will not be set!");
        return EChange.UNCHANGED;
      }
      m_aOwningMgr = aOwningMgr;
      m_aUser = aUser;
      m_sUserID = aUser.getID ();
      return EChange.CHANGED;
    }

    private void _reset ()
    {
      m_aUser = null;
      m_sUserID = null;
    }

    @Override
    protected void onDestroy ()
    {
      // Called when the session is destroyed
      // -> Ensure the user is logged out!
      if (m_aOwningMgr != null)
      {
        // This method triggers _reset :)
        m_aOwningMgr._logoutUser (m_sUserID, this);
      }
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggedInUserManager.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Set <String> m_aLoggedInUsers = new HashSet <String> ();

  @Deprecated
  @UsedViaReflection
  public LoggedInUserManager ()
  {}

  @Nonnull
  public static LoggedInUserManager getInstance ()
  {
    return getGlobalSingleton (LoggedInUserManager.class);
  }

  @Nonnull
  public ELoginResult loginUser (@Nullable final String sLoginName, @Nullable final String sPlainTextPassword)
  {
    // Try to resolve the user
    final IUser aUser = AccessManager.getInstance ().getUserOfLoginName (sLoginName);
    if (aUser == null)
      return ELoginResult.USER_NOT_EXISTING;
    final String sUserID = aUser.getID ();

    // Check the password
    if (!AccessManager.getInstance ().areUserIDAndPasswordValid (sUserID, sPlainTextPassword))
      return ELoginResult.INVALID_PASSWORD;

    // All checks done!
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (!m_aLoggedInUsers.add (sUserID))
      {
        // The user is already logged in
        return ELoginResult.USER_ALREADY_LOGGED_IN;
      }

      if (SessionUserHolder.getInstance ().setUser (this, aUser).isUnchanged ())
      {
        // Another user is already in the current session
        m_aLoggedInUsers.remove (sUserID);
        return ELoginResult.SESSION_ALREADY_HAS_USER;
      }

      s_aLogger.info ("Logged in user '" + sUserID + "'");
      return ELoginResult.SUCCESS;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Manually log out the current user
   * 
   * @param sUserID
   *        The user to log out
   * @param aSessionUserHolder
   *        The session user holder to use - avoid instantiating a singleton
   *        again
   * @return {@link EChange} if something changed
   */
  @Nonnull
  private EChange _logoutUser (@Nullable final String sUserID, @Nonnull final SessionUserHolder aSessionUserHolder)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (!m_aLoggedInUsers.remove (sUserID))
        return EChange.UNCHANGED;
      aSessionUserHolder._reset ();
      s_aLogger.info ("Logged out user '" + sUserID + "'");
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Manually log out the specified user
   * 
   * @param sUserID
   *        The user ID to log out
   * @return {@link EChange} if something changed
   */
  @Nonnull
  public EChange logoutUser (@Nullable final String sUserID)
  {
    return _logoutUser (sUserID, SessionUserHolder.getInstance ());
  }

  /**
   * Manually log out the current user
   * 
   * @return {@link EChange} if something changed
   */
  @Nonnull
  public EChange logoutCurrentUser ()
  {
    return logoutUser (getCurrentUserID ());
  }

  /**
   * Check if the specified user is logged in or not
   * 
   * @param sUserID
   *        The user ID to check. May be <code>null</code>.
   * @return <code>true</code> if the user is logged in, <code>false</code>
   *         otherwise.
   */
  public boolean isUserLoggedIn (@Nullable final String sUserID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aLoggedInUsers.contains (sUserID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A non-<code>null</code> but maybe empty set with all currently
   *         logged in user IDs.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllLoggedInUserIDs ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (m_aLoggedInUsers);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return The number of currently logged in users. Always &ge; 0.
   */
  @Nonnegative
  public int getLoggedInUserCount ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aLoggedInUsers.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return The ID of the user logged in this session or <code>null</code> if
   *         no user is logged in.
   */
  @Nullable
  public String getCurrentUserID ()
  {
    return SessionUserHolder.getInstance ().m_sUserID;
  }

  /**
   * @return <code>true</code> if a user is currently logged into this session,
   *         <code>false</code> otherwise.
   */
  public boolean isUserLoggedInInCurrentSession ()
  {
    return getCurrentUserID () != null;
  }

  /**
   * @return The user currently logged in this session or <code>null</code> if
   *         no user is logged in.
   */
  @Nullable
  public IUser getCurrentUser ()
  {
    return SessionUserHolder.getInstance ().m_aUser;
  }
}
