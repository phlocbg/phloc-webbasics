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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session scope map that resides in the global scope. Upon scope destruction
 * all open session scopes are destroyed!<br>
 * This class is only to be called from {@link SessionScopeManager} class!
 * 
 * @author philip
 */
final class SessionScopeMap implements IScopeDestructionAware
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SessionScopeMap.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, SessionScope> m_aMap = new HashMap <String, SessionScope> ();

  /**
   * Add a new session scope
   * 
   * @param sSessionID
   *        session ID
   * @param aSessionScope
   *        session scope
   * @throws IllegalStateException
   *         If another scope with the same session ID is already present
   */
  public void addScope (@Nonnull final String sSessionID, @Nonnull final SessionScope aSessionScope)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aMap.containsKey (sSessionID))
        throw new IllegalStateException ("A session scope for session ID '" + sSessionID + "' is already contained!");
      m_aMap.put (sSessionID, aSessionScope);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Remove a session scope
   * 
   * @param sSessionID
   *        session ID to be removed
   * @return The removed session scope object
   * @throws IllegalStateException
   *         If no scope could be removed
   */
  @Nonnull
  public SessionScope removeScope (@Nonnull final String sSessionID)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      final SessionScope aSessionScope = m_aMap.remove (sSessionID);
      if (aSessionScope == null)
        throw new IllegalStateException ("No session scope for session ID '" + sSessionID + "' was found!");
      return aSessionScope;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Find the scope of the given session ID
   * 
   * @param sSessionID
   *        session ID
   * @return <code>null</code> if no such scope exists - should never occur
   */
  @Nonnull
  public SessionScope getScope (@Nonnull final String sSessionID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.get (sSessionID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return The number of session scopes currently active
   */
  @Nonnegative
  public int size ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Called upon destruction of the global scope.
   */
  public void onScopeDestruction () throws Exception
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (!m_aMap.isEmpty ())
      {
        // We do have session scopes left -> call destroy on them manually
        s_aLogger.warn ("Destroying " + m_aMap.size () + " session scopes that were not destroyed previously!");
        for (final SessionScope aSessionScope : m_aMap.values ())
          aSessionScope.destroyScope ();
        m_aMap.clear ();
      }
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }
}
