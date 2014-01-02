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
package com.phloc.appbasics.security.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.audit.AuditUtils;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.singleton.GlobalSingleton;
import com.phloc.scopes.singleton.SessionSingleton;

/**
 * This class manages all logged-in users.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class LoggedInUserManager extends GlobalSingleton implements ICurrentUserIDProvider
{
  /**
   * This class manages the user ID of the current session. This is an internal
   * class and should not be used from the outside!
   * 
   * @author Philip Helger
   */
  public static final class SessionUserHolder extends SessionSingleton
  {
    private static final long serialVersionUID = 2322897734799334L;

    private IUser m_aUser;
    private String m_sUserID;
    private transient LoggedInUserManager m_aOwningMgr;

    @Deprecated
    @UsedViaReflection
    public SessionUserHolder ()
    {}

    /**
     * @return The instance of the current session. If none exists, an instance
     *         is created. Never <code>null</code>.
     */
    @Nonnull
    static SessionUserHolder getInstance ()
    {
      return getSessionSingleton (SessionUserHolder.class);
    }

    /**
     * @return The instance of the current session. If none exists,
     *         <code>null</code> is returned.
     */
    @Nullable
    static SessionUserHolder getInstanceIfInstantiated ()
    {
      return getSessionSingletonIfInstantiated (SessionUserHolder.class);
    }

    @Nonnull
    EChange setUser (@Nonnull final LoggedInUserManager aOwningMgr, @Nonnull final IUser aUser)
    {
      if (aUser == null)
        throw new NullPointerException ("user");
      if (aUser.isDeleted ())
        throw new IllegalArgumentException ("Passed user is deleted: " + aUser);

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

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("userID", m_sUserID).toString ();
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggedInUserManager.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  // Set of logged in user IDs
  @GuardedBy ("m_aRWLock")
  private final Map <String, LoginInfo> m_aLoggedInUsers = new HashMap <String, LoginInfo> ();
  @GuardedBy ("m_aRWLock")
  private final List <IUserLoginCallback> m_aUserLoginCallbacks = new ArrayList <IUserLoginCallback> ();
  @GuardedBy ("m_aRWLock")
  private final List <IUserLogoutCallback> m_aUserLogoutCallbacks = new ArrayList <IUserLogoutCallback> ();

  @Deprecated
  @UsedViaReflection
  public LoggedInUserManager ()
  {}

  /**
   * @return The global instance of this class. Never <code>null</code>.
   */
  @Nonnull
  public static LoggedInUserManager getInstance ()
  {
    return getGlobalSingleton (LoggedInUserManager.class);
  }

  /**
   * @return The current user login callbacks. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <IUserLoginCallback> getAllUserLoginCallbacks ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aUserLoginCallbacks);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Add a user login callback.
   * 
   * @param aUserLoginCallback
   *        The new login callback to be added. May not be <code>null</code>.
   */
  public void addUserLoginCallback (@Nonnull final IUserLoginCallback aUserLoginCallback)
  {
    if (aUserLoginCallback == null)
      throw new NullPointerException ("UserLoginCallback");

    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aUserLoginCallbacks.add (aUserLoginCallback);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Remove a user login callback.
   * 
   * @param aUserLoginCallback
   *        The login callback to be removed. May be <code>null</code>.
   * @return {@link EChange}
   */
  @Nonnull
  public EChange removeUserLoginCallback (@Nullable final IUserLoginCallback aUserLoginCallback)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return EChange.valueOf (m_aUserLoginCallbacks.remove (aUserLoginCallback));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return The current user logout callbacks. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <IUserLogoutCallback> getAllUserLogoutCallbacks ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aUserLogoutCallbacks);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Add a user logout callback.
   * 
   * @param aUserLogoutCallback
   *        The new logout callback to be added. May not be <code>null</code>.
   */
  public void addUserLogoutCallback (@Nonnull final IUserLogoutCallback aUserLogoutCallback)
  {
    if (aUserLogoutCallback == null)
      throw new NullPointerException ("UserLogoutCallback");

    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aUserLogoutCallbacks.add (aUserLogoutCallback);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Remove a user logout callback.
   * 
   * @param aUserLogoutCallback
   *        The new logout callback to be removed. May be <code>null</code>.
   */
  @Nonnull
  public EChange removeUserLogoutCallback (@Nullable final IUserLogoutCallback aUserLogoutCallback)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return EChange.valueOf (m_aUserLogoutCallbacks.remove (aUserLogoutCallback));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Login the passed user without much ado.
   * 
   * @param sLoginName
   *        Login name of the user to log-in. May be <code>null</code>.
   * @param sPlainTextPassword
   *        Plain text password to use. May be <code>null</code>.
   * @return Never <code>null</code> login status.
   */
  @Nonnull
  public ELoginResult loginUser (@Nullable final String sLoginName, @Nullable final String sPlainTextPassword)
  {
    return loginUser (sLoginName, sPlainTextPassword, (Collection <String>) null);
  }

  /**
   * Login the passed user and require a set of certain roles, the used needs to
   * have to login here.
   * 
   * @param sLoginName
   *        Login name of the user to log-in. May be <code>null</code>.
   * @param sPlainTextPassword
   *        Plain text password to use. May be <code>null</code>.
   * @param aRequiredRoleIDs
   *        A set of required role IDs, the user needs to have. May be
   *        <code>null</code>.
   * @return Never <code>null</code> login status.
   */
  @Nonnull
  public ELoginResult loginUser (@Nullable final String sLoginName,
                                 @Nullable final String sPlainTextPassword,
                                 @Nullable final Collection <String> aRequiredRoleIDs)
  {
    // Try to resolve the user
    final IUser aUser = AccessManager.getInstance ().getUserOfLoginName (sLoginName);
    if (aUser == null)
    {
      AuditUtils.onAuditExecuteFailure ("login", sLoginName, "no-such-loginname");
      return ELoginResult.USER_NOT_EXISTING;
    }
    return loginUser (aUser, sPlainTextPassword, aRequiredRoleIDs);
  }

  /**
   * Login the passed user and require a set of certain roles, the used needs to
   * have to login here.
   * 
   * @param aUser
   *        The user to log-in. May be <code>null</code>.
   * @param sPlainTextPassword
   *        Plain text password to use. May be <code>null</code>.
   * @param aRequiredRoleIDs
   *        A set of required role IDs, the user needs to have. May be
   *        <code>null</code>.
   * @return Never <code>null</code> login status.
   */
  @Nonnull
  public ELoginResult loginUser (@Nullable final IUser aUser,
                                 @Nullable final String sPlainTextPassword,
                                 @Nullable final Collection <String> aRequiredRoleIDs)
  {
    if (aUser == null)
      return ELoginResult.USER_NOT_EXISTING;

    final String sUserID = aUser.getID ();

    // Deleted user?
    if (aUser.isDeleted ())
    {
      AuditUtils.onAuditExecuteFailure ("login", sUserID, "user-is-deleted");
      return ELoginResult.USER_IS_DELETED;
    }

    // Disabled user?
    if (aUser.isDisabled ())
    {
      AuditUtils.onAuditExecuteFailure ("login", sUserID, "user-is-disabled");
      return ELoginResult.USER_IS_DISABLED;
    }

    final AccessManager aAM = AccessManager.getInstance ();

    // Are all roles present?
    if (!aAM.hasUserAllRoles (sUserID, aRequiredRoleIDs))
    {
      AuditUtils.onAuditExecuteFailure ("login",
                                        sUserID,
                                        "user-is-missing-required-roles",
                                        StringHelper.getToString (aRequiredRoleIDs));
      return ELoginResult.USER_IS_MISSING_ROLE;
    }

    // Check the password
    if (!aAM.areUserIDAndPasswordValid (sUserID, sPlainTextPassword))
    {
      AuditUtils.onAuditExecuteFailure ("login", sUserID, "invalid-password");
      return ELoginResult.INVALID_PASSWORD;
    }

    LoginInfo aInfo;
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aLoggedInUsers.containsKey (sUserID))
      {
        // The user is already logged in
        AuditUtils.onAuditExecuteFailure ("login", sUserID, "user-already-logged-in");
        return ELoginResult.USER_ALREADY_LOGGED_IN;
      }

      aInfo = new LoginInfo (aUser);
      m_aLoggedInUsers.put (sUserID, aInfo);

      if (SessionUserHolder.getInstance ().setUser (this, aUser).isUnchanged ())
      {
        // Another user is already in the current session
        m_aLoggedInUsers.remove (sUserID);
        AuditUtils.onAuditExecuteFailure ("login", sUserID, "session-already-has-user");
        return ELoginResult.SESSION_ALREADY_HAS_USER;
      }
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    s_aLogger.info ("Logged in user '" + sUserID + "'");
    AuditUtils.onAuditExecuteSuccess ("login", sUserID);

    // Execute callback as the very last action
    for (final IUserLoginCallback aUserLoginCallback : getAllUserLoginCallbacks ())
      try
      {
        aUserLoginCallback.onUserLogin (aInfo);
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Failed to invoke onUserLogin callback on " + aUserLoginCallback, t);
      }

    return ELoginResult.SUCCESS;
  }

  /**
   * Manually log out the specified user
   * 
   * @param sUserID
   *        The user to log out. May be <code>null</code>.
   * @param aSessionUserHolder
   *        The session user holder to use - avoid instantiating a singleton
   *        again.
   * @return {@link EChange} if something changed
   */
  @Nonnull
  private EChange _logoutUser (@Nullable final String sUserID, @Nullable final SessionUserHolder aSessionUserHolder)
  {
    m_aRWLock.writeLock ().lock ();
    LoginInfo aInfo;
    try
    {
      aInfo = m_aLoggedInUsers.remove (sUserID);
      if (aInfo == null)
      {
        AuditUtils.onAuditExecuteSuccess ("logout", sUserID, "user-not-logged-in");
        return EChange.UNCHANGED;
      }
      if (aSessionUserHolder != null)
        aSessionUserHolder._reset ();

      // Set logout time
      aInfo.setLogoutDTNow ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    s_aLogger.info ("Logged out user '" +
                    sUserID +
                    "' after " +
                    new Period (aInfo.getLoginDT (), aInfo.getLogoutDT ()).toString ());
    AuditUtils.onAuditExecuteSuccess ("logout", sUserID);

    // Execute callback as the very last action
    for (final IUserLogoutCallback aUserLogoutCallback : getAllUserLogoutCallbacks ())
      try
      {
        aUserLogoutCallback.onUserLogout (aInfo);
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Failed to invoke onUserLogout callback on " + aUserLogoutCallback, t);
      }

    return EChange.CHANGED;
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
    return _logoutUser (sUserID, SessionUserHolder.getInstanceIfInstantiated ());
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
      return m_aLoggedInUsers.containsKey (sUserID);
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
      return ContainerHelper.newSet (m_aLoggedInUsers.keySet ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Get the login details of the specified user.
   * 
   * @param sUserID
   *        The user ID to check. May be <code>null</code>.
   * @return <code>null</code> if the passed user is not logged in.
   */
  @Nullable
  public LoginInfo getLoginInfo (@Nullable final String sUserID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aLoggedInUsers.get (sUserID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A non-<code>null</code> but maybe empty collection with the details
   *         of all currently logged in users.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Collection <LoginInfo> getAllLoginInfos ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aLoggedInUsers.values ());
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
    final SessionUserHolder aSUH = SessionUserHolder.getInstanceIfInstantiated ();
    return aSUH == null ? null : aSUH.m_sUserID;
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
    final SessionUserHolder aSUH = SessionUserHolder.getInstanceIfInstantiated ();
    return aSUH == null ? null : aSUH.m_aUser;
  }

  /**
   * @return <code>true</code> if a user is logged in and is administrator
   */
  public boolean isCurrentUserAdministrator ()
  {
    final IUser aUser = getCurrentUser ();
    return aUser != null && aUser.isAdministrator ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("loggedInUsers", m_aLoggedInUsers).toString ();
  }
}
