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

import java.util.Enumeration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;

/**
 * Default implementation of the {@link ISessionScope} interface.
 * 
 * @author philip
 */
@ThreadSafe
public class SessionScope extends AbstractScope implements ISessionScope
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SessionScope.class);

  private final HttpSession m_aHttpSession;

  public SessionScope (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
      throw new NullPointerException ("httpSession");
    m_aHttpSession = aHttpSession;
  }

  @Nonnull
  public HttpSession getSession ()
  {
    return m_aHttpSession;
  }

  @Nullable
  public Object getAttributeObject (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aHttpSession.getAttribute (sName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public void setAttribute (@Nonnull final String sName, @Nullable final Object aValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aValue == null)
        m_aHttpSession.removeAttribute (sName);
      else
        m_aHttpSession.setAttribute (sName, aValue);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange removeAttribute (@Nonnull final String sName)
  {
    if (getAttributeObject (sName) == null)
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aHttpSession.removeAttribute (sName);
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  protected void mainDestroyScope ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final Enumeration <?> aEnum = m_aHttpSession.getAttributeNames ();
      while (aEnum.hasMoreElements ())
      {
        final String sName = (String) aEnum.nextElement ();
        final Object aValue = getAttributeObject (sName);
        if (aValue instanceof IScopeDestructionAware)
          try
          {
            ((IScopeDestructionAware) aValue).onScopeDestruction ();
          }
          catch (final Exception ex)
          {
            s_aLogger.error ("Failed to call destruction method in global scope on " + aValue, ex);
          }
      }
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
