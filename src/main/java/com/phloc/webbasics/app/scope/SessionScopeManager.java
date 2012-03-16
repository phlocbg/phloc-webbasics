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
package com.phloc.webbasics.app.scope;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;

/**
 * This class manages the available session scopes with a static map.
 * 
 * @author philip
 */
@ThreadSafe
public final class SessionScopeManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SessionScopeManager.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final String GLOBAL_SCOPE_ATTR_SESSION_SCOPES = "singleton.sessionscopemap";

  private SessionScopeManager ()
  {}

  /**
   * Get or create the session scope map from the global scope. Note: this
   * method may only accessed within a write lock to avoid that the object in
   * the global scope is created simultaneously!
   * 
   * @param bCreateIfNotExisting
   *        if <code>true</code> and no scope map is available a new one will be
   *        created and the return value is not null
   * @return <code>null</code> if no global scope map exists, and none should be
   *         created
   */
  @Nullable
  private static SessionScopeMap _getSessionScopeMap (final boolean bCreateIfNotExisting)
  {
    final IGlobalScope aGlobalScope = BasicScopeManager.getGlobalScope ();
    SessionScopeMap aScopeMap;
    s_aRWLock.readLock ().lock ();
    try
    {
      // First try with a read-lock only
      aScopeMap = aGlobalScope.getCastedAttribute (GLOBAL_SCOPE_ATTR_SESSION_SCOPES);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }

    if (aScopeMap == null && bCreateIfNotExisting)
    {
      // Nothing found with a read-lock only -> now we need a write lock
      s_aRWLock.writeLock ().lock ();
      try
      {
        // Search again in write lock
        aScopeMap = aGlobalScope.getCastedAttribute (GLOBAL_SCOPE_ATTR_SESSION_SCOPES);
        if (aScopeMap == null)
        {
          // Now we're 100% sure that no scope map exists so far -> create a new
          // one and put it in global scope!
          aScopeMap = new SessionScopeMap ();
          aGlobalScope.setAttribute (GLOBAL_SCOPE_ATTR_SESSION_SCOPES, aScopeMap);
        }
      }
      finally
      {
        s_aRWLock.writeLock ().unlock ();
      }
    }
    return aScopeMap;
  }

  /**
   * Create a new session scope. Should only be called from within a
   * {@link javax.servlet.http.HttpSessionListener}.
   * 
   * @param aHttpSession
   *        The HTTP session for which a scope is to be created. May not be
   *        <code>null</code>.
   */
  public static void createSessionScope (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
      throw new NullPointerException ("HTTPSesssion");

    final String sSessionID = aHttpSession.getId ();
    _getSessionScopeMap (true).addScope (sSessionID, new SessionScope (aHttpSession));

    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Created session scope for ID '" + sSessionID + "'");
  }

  /**
   * Destroy an existing session scope. Should only be called from within a
   * {@link javax.servlet.http.HttpSessionListener}.
   * 
   * @param aHttpSession
   *        The HTTP session for which a scope is to be destroyed. May not be
   *        <code>null</code>.
   */
  public static void destroySessionScope (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
      throw new NullPointerException ("HTTPSesssion");

    final String sSessionID = aHttpSession.getId ();
    final SessionScope aSessionScope = _getSessionScopeMap (true).removeScope (sSessionID);

    // Destroy the scope now (outside the write lock for better performance)
    aSessionScope.destroyScope ();

    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Destroyed session scope for ID '" + sSessionID + "'");
  }

  /**
   * Retrieve the session scope that is matching the passed HTTP session
   * 
   * @param aHttpSession
   *        The HTTP session for which the session scope is to be retrieved.
   * @return <code>null</code> if no such scope exists. This btw. should never
   *         happen, if you are correctly using a
   *         {@link javax.servlet.http.HttpSessionListener}
   */
  @Nullable
  public static ISessionScope getSessionScope (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
      throw new NullPointerException ("HTTPSesssion");

    final String sSessionID = aHttpSession.getId ();
    final SessionScopeMap aSessionScopeMap = _getSessionScopeMap (false);
    if (aSessionScopeMap == null)
    {
      if (GlobalDebug.isDebugMode ())
        s_aLogger.warn ("Failed to resolve session scope for session ID '" + sSessionID + "'");
      return null;
    }
    return aSessionScopeMap.getScope (sSessionID);
  }

  /**
   * @return The number of active sessions.
   */
  @Nonnegative
  public int getSessionCount ()
  {
    final SessionScopeMap aSessionScopeMap = _getSessionScopeMap (false);
    return aSessionScopeMap == null ? 0 : aSessionScopeMap.size ();
  }
}
