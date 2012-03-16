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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;

/**
 * Default implementation of the {@link IRequestScope}.
 * 
 * @author philip
 */
@ThreadSafe
public class RequestScope extends AbstractScope implements IRequestScope
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (RequestScope.class);

  private final HttpServletRequest m_aHttpRequest;
  private final HttpServletResponse m_aHttpResponse;

  public RequestScope (@Nonnull final HttpServletRequest aHttpRequest, @Nonnull final HttpServletResponse aHttpResponse)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");
    if (aHttpResponse == null)
      throw new NullPointerException ("httpResponse");
    m_aHttpRequest = aHttpRequest;
    m_aHttpResponse = aHttpResponse;

    // set parameters as attributes (handles GET and POST parameters)
    final Enumeration <?> aEnum = m_aHttpRequest.getParameterNames ();
    while (aEnum.hasMoreElements ())
    {
      final String sParamName = (String) aEnum.nextElement ();

      // Check if it is a single value or not
      final String [] aParamValues = m_aHttpRequest.getParameterValues (sParamName);
      if (aParamValues.length == 1)
        setAttribute (sParamName, aParamValues[0]);
      else
        setAttribute (sParamName, aParamValues);
    }
  }

  @Nonnull
  public HttpServletRequest getRequest ()
  {
    return m_aHttpRequest;
  }

  @Nonnull
  public HttpServletResponse getResponse ()
  {
    return m_aHttpResponse;
  }

  @Override
  protected void mainDestroyScope ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final Enumeration <?> aEnum = m_aHttpRequest.getAttributeNames ();
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
            s_aLogger.error ("Failed to call destruction method in request scope on " + aValue, ex);
          }
      }
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public Object getAttributeObject (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aHttpRequest.getAttribute (sName);
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
        m_aHttpRequest.removeAttribute (sName);
      else
        m_aHttpRequest.setAttribute (sName, aValue);
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
      m_aHttpRequest.removeAttribute (sName);
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public String getUserAgent ()
  {
    // Note: access to headers does not need to be synchronized, as they are
    // read-only
    // Use non-standard headers first
    String sUserAgent = m_aHttpRequest.getHeader ("UA");
    if (sUserAgent == null)
    {
      sUserAgent = m_aHttpRequest.getHeader ("x-device-user-agent");
      if (sUserAgent == null)
        sUserAgent = m_aHttpRequest.getHeader ("User-Agent");
    }
    return sUserAgent;
  }

  @Override
  public String getFullContextPath ()
  {
    // No need to synchronized, because only information that is read-only is
    // accessed
    final String sScheme = m_aHttpRequest.getScheme ();
    final StringBuilder aSB = new StringBuilder ();
    aSB.append (sScheme).append ("://").append (m_aHttpRequest.getServerName ());
    final int nPort = m_aHttpRequest.getServerPort ();
    if (("http".equals (sScheme) && nPort != 80) || ("https".equals (sScheme) && nPort != 443))
      aSB.append (':').append (nPort);
    return aSB.append (m_aHttpRequest.getContextPath ()).toString ();
  }
}
