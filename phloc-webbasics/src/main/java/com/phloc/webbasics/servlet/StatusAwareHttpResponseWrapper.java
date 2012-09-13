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
package com.phloc.webbasics.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTWebDateUtils;
import com.phloc.webbasics.http.CHTTPHeader;

/**
 * Abstract output stream switching {@link HttpServletResponseWrapper}
 * 
 * @author philip
 */
@NotThreadSafe
public class StatusAwareHttpResponseWrapper extends HttpServletResponseWrapper
{
  private final Map <String, List <String>> m_aHeaders = new LinkedHashMap <String, List <String>> ();
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
    m_aHeaders.clear ();
  }

  private void _setHeader (final String sName, final String sValue)
  {
    List <String> aValues = m_aHeaders.get (sName);
    if (aValues == null)
    {
      aValues = new ArrayList <String> (1);
      m_aHeaders.put (sName, aValues);
    }
    else
      aValues.clear ();
    aValues.add (sValue);
  }

  private void _addHeader (final String sName, final String sValue)
  {
    List <String> aValues = m_aHeaders.get (sName);
    if (aValues == null)
    {
      aValues = new ArrayList <String> (1);
      m_aHeaders.put (sName, aValues);
    }
    aValues.add (sValue);
  }

  @Override
  public void setContentLength (final int nLength)
  {
    super.setContentLength (nLength);
    _setHeader (CHTTPHeader.CONTENT_LENGTH, Integer.toString (nLength));
  }

  @Override
  public void setContentType (final String sContentType)
  {
    super.setContentType (sContentType);
    _setHeader (CHTTPHeader.CONTENT_TYPE, sContentType);
  }

  @Override
  public void setDateHeader (final String sName, final long nMillis)
  {
    super.setDateHeader (sName, nMillis);
    _setHeader (sName, PDTWebDateUtils.getAsStringRFC822 (PDTFactory.createDateTimeFromMillis (nMillis)));
  }

  @Override
  public void addDateHeader (final String sName, final long nMillis)
  {
    super.addDateHeader (sName, nMillis);
    _addHeader (sName, PDTWebDateUtils.getAsStringRFC822 (PDTFactory.createDateTimeFromMillis (nMillis)));
  }

  @Override
  public void setHeader (final String sName, final String sValue)
  {
    super.setHeader (sName, sValue);
    _setHeader (sName, sValue);
  }

  @Override
  public void addHeader (final String sName, final String sValue)
  {
    super.addHeader (sName, sValue);
    _addHeader (sName, sValue);
  }

  @Override
  public void setIntHeader (final String sName, final int nValue)
  {
    super.setIntHeader (sName, nValue);
    _setHeader (sName, Integer.toString (nValue));
  }

  @Override
  public void addIntHeader (final String sName, final int nValue)
  {
    super.addIntHeader (sName, nValue);
    _addHeader (sName, Integer.toString (nValue));
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, List <String>> getAllHeaders ()
  {
    return ContainerHelper.newMap (m_aHeaders);
  }
}
