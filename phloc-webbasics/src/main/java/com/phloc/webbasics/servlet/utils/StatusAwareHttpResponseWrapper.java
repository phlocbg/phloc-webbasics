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
package com.phloc.webbasics.servlet.utils;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.phloc.webbasics.http.HTTPHeaderMap;

/**
 * A special {@link HttpServletResponseWrapper} that tracks the used status
 * codes as well as the set HTTP response headers.
 * 
 * @author philip
 */
@NotThreadSafe
public class StatusAwareHttpResponseWrapper extends HttpServletResponseWrapper
{
  private final HTTPHeaderMap m_aHeaderMap = new HTTPHeaderMap ();
  private int m_nStatusCode = SC_OK;

  public StatusAwareHttpResponseWrapper (@Nonnull final HttpServletResponse aHttpResponse)
  {
    super (aHttpResponse);
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public void setStatus (final int sc)
  {
    super.setStatus (sc);
    m_nStatusCode = sc;
  }

  @Override
  @Deprecated
  @OverridingMethodsMustInvokeSuper
  public void setStatus (final int sc, final String sm)
  {
    super.setStatus (sc, sm);
    m_nStatusCode = sc;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public void sendError (final int sc, final String msg) throws IOException
  {
    super.sendError (sc, msg);
    m_nStatusCode = sc;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public void sendError (final int sc) throws IOException
  {
    super.sendError (sc);
    m_nStatusCode = sc;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public void sendRedirect (final String sLocation) throws IOException
  {
    super.sendRedirect (sLocation);
    m_nStatusCode = SC_MOVED_TEMPORARILY;
  }

  public final int getStatusCode ()
  {
    return m_nStatusCode;
  }

  @Override
  public void reset ()
  {
    super.reset ();
    m_aHeaderMap.reset ();
  }

  @Override
  public void setContentLength (final int nLength)
  {
    super.setContentLength (nLength);
    m_aHeaderMap.setContentLength (nLength);
  }

  @Override
  public void setContentType (@Nonnull final String sContentType)
  {
    super.setContentType (sContentType);
    m_aHeaderMap.setContentType (sContentType);
  }

  @Override
  public void setDateHeader (final String sName, final long nMillis)
  {
    super.setDateHeader (sName, nMillis);
    m_aHeaderMap.setDateHeader (sName, nMillis);
  }

  @Override
  public void addDateHeader (final String sName, final long nMillis)
  {
    super.addDateHeader (sName, nMillis);
    m_aHeaderMap.addDateHeader (sName, nMillis);
  }

  @Override
  public void setHeader (final String sName, final String sValue)
  {
    super.setHeader (sName, sValue);
    m_aHeaderMap.setHeader (sName, sValue);
  }

  @Override
  public void addHeader (final String sName, final String sValue)
  {
    super.addHeader (sName, sValue);
    m_aHeaderMap.addHeader (sName, sValue);
  }

  @Override
  public void setIntHeader (final String sName, final int nValue)
  {
    super.setIntHeader (sName, nValue);
    m_aHeaderMap.setIntHeader (sName, nValue);
  }

  @Override
  public void addIntHeader (final String sName, final int nValue)
  {
    super.addIntHeader (sName, nValue);
    m_aHeaderMap.addIntHeader (sName, nValue);
  }

  @Nonnull
  public HTTPHeaderMap getHeaderMap ()
  {
    return m_aHeaderMap;
  }
}
