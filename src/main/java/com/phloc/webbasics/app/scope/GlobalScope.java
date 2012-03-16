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
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;

/**
 * Default implementation of the {@link IGlobalScope} interface.
 * 
 * @author philip
 */
@ThreadSafe
public class GlobalScope extends AbstractScope implements IGlobalScope
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (GlobalScope.class);

  private final ServletContext m_aSC;

  public GlobalScope (@Nonnull final ServletContext aSC)
  {
    if (aSC == null)
      throw new NullPointerException ("servletContext");
    m_aSC = aSC;
  }

  @Nonnull
  public final ServletContext getServletContext ()
  {
    return m_aSC;
  }

  @Nullable
  public Object getAttributeObject (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aSC.getAttribute (sName);
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
        m_aSC.removeAttribute (sName);
      else
        m_aSC.setAttribute (sName, aValue);
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
      m_aSC.removeAttribute (sName);
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
      final Enumeration <?> aEnum = m_aSC.getAttributeNames ();
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
