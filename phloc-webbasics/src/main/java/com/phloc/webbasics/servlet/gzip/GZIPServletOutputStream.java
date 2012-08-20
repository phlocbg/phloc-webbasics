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
package com.phloc.webbasics.servlet.gzip;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Nonnull;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.webbasics.http.CHTTPHeader;

/**
 * Copyright 2003 Jayson Falkner (jayson@jspinsider.com) This code is from
 * "Servlets and JavaServer pages; the J2EE Web Tier", http://www.jspbook.com.
 * You may freely use the code both commercially and non-commercially. If you
 * like the code, please pick up a copy of the book and help support the
 * authors, development of more free code, and the JSP/Servlet/J2EE community.
 */
final class GZIPServletOutputStream extends AbstractServletOutputStream
{
  private final HttpServletResponse m_aHttpResponse;
  private final String m_sContentEncoding;
  private final NonBlockingByteArrayOutputStream m_aBAOS = new NonBlockingByteArrayOutputStream ();
  private final GZIPOutputStream m_aGZOS = new GZIPOutputStream (m_aBAOS);

  public GZIPServletOutputStream (@Nonnull final HttpServletResponse aHttpResponse,
                                  @Nonnull final String sContentEncoding) throws IOException
  {
    super ();
    setWrappedOutputStream (m_aGZOS);
    m_aHttpResponse = aHttpResponse;
    m_sContentEncoding = sContentEncoding;
  }

  @Override
  protected void onClose () throws IOException
  {
    // Finish GZIP stream
    m_aGZOS.finish ();
    m_aGZOS.flush ();

    m_aHttpResponse.setContentLength (m_aBAOS.size ());
    m_aHttpResponse.setHeader (CHTTPHeader.CONTENT_ENCODING, m_sContentEncoding);
    m_aHttpResponse.setHeader (CHTTPHeader.VARY, CHTTPHeader.ACCEPT_ENCODING);
    final ServletOutputStream aOS = m_aHttpResponse.getOutputStream ();
    m_aBAOS.writeTo (aOS);
    aOS.flush ();
    aOS.close ();
  }
}
