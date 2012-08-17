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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.annotation.Nonnull;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.StreamUtils;

/**
 * Copyright 2003 Jayson Falkner (jayson@jspinsider.com) This code is from
 * "Servlets and JavaServer pages; the J2EE Web Tier", http://www.jspbook.com.
 * You may freely use the code both commercially and non-commercially. If you
 * like the code, please pick up a copy of the book and help support the
 * authors, development of more free code, and the JSP/Servlet/J2EE community.
 */
public class GZIPResponseWrapper extends HttpServletResponseWrapper
{
  private ServletOutputStream m_aStream;
  private PrintWriter m_aWriter;
  private final String m_sGZIPEncoding;

  public GZIPResponseWrapper (@Nonnull final HttpServletResponse aHttpResponse, @Nonnull final String sGZIPEncoding)
  {
    super (aHttpResponse);
    m_sGZIPEncoding = sGZIPEncoding;
  }

  public void finishResponse ()
  {
    StreamUtils.close (m_aWriter);
    StreamUtils.close (m_aStream);
  }

  @Nonnull
  private ServletOutputStream _createOutputStream () throws IOException
  {
    return new GZIPServletOutputStream ((HttpServletResponse) getResponse (), m_sGZIPEncoding);
  }

  @Override
  public void flushBuffer () throws IOException
  {
    if (m_aStream != null)
      m_aStream.flush ();
  }

  @Override
  public ServletOutputStream getOutputStream () throws IOException
  {
    if (m_aWriter != null)
      throw new IllegalStateException ("getWriter() has already been called!");

    if (m_aStream == null)
      m_aStream = _createOutputStream ();
    return m_aStream;
  }

  @Override
  public PrintWriter getWriter () throws IOException
  {
    if (m_aWriter != null)
      return m_aWriter;

    if (m_aStream != null)
      throw new IllegalStateException ("getOutputStream() has already been called!");

    m_aStream = _createOutputStream ();
    m_aWriter = new PrintWriter (new OutputStreamWriter (m_aStream, CCharset.CHARSET_UTF_8));
    return m_aWriter;
  }
}
