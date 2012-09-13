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

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Abstract output stream switching {@link HttpServletResponseWrapper}
 * 
 * @author philip
 */
@NotThreadSafe
public class StatusAwareHttpResponseWrapper extends HttpServletResponseWrapper
{
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
}
