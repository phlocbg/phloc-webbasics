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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;

/**
 * Default implementation of the {@link IRequestScope}.
 * 
 * @author philip
 */
public class RequestScope extends AbstractScope implements IRequestScope
{
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

  public void destroyScope ()
  {}

  @Nullable
  public Object getAttributeObject (@Nullable final String sName)
  {
    return m_aHttpRequest.getAttribute (sName);
  }

  public void setAttribute (@Nonnull final String sName, @Nullable final Object aValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aValue == null)
      m_aHttpRequest.removeAttribute (sName);
    else
      m_aHttpRequest.setAttribute (sName, aValue);
  }

  @Nonnull
  public EChange removeAttribute (@Nonnull final String sName)
  {
    if (getAttributeObject (sName) == null)
      return EChange.UNCHANGED;
    m_aHttpRequest.removeAttribute (sName);
    return EChange.CHANGED;
  }

  @Nullable
  public String getUserAgent ()
  {
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
    final String sScheme = m_aHttpRequest.getScheme ();
    final StringBuilder aSB = new StringBuilder ();
    aSB.append (sScheme).append ("://").append (m_aHttpRequest.getServerName ());
    final int nPort = m_aHttpRequest.getServerPort ();
    if (("http".equals (sScheme) && nPort != 80) || ("https".equals (sScheme) && nPort != 443))
      aSB.append (':').append (nPort);
    return aSB.append (m_aHttpRequest.getContextPath ()).toString ();
  }
}
