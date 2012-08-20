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
 * Abstract output stream switching {@link HttpServletResponseWrapper}
 * 
 * @author philip
 */
public abstract class AbstractResponseWrapper extends HttpServletResponseWrapper
{
  private ServletOutputStream m_aStream;
  private PrintWriter m_aWriter;

  public AbstractResponseWrapper (@Nonnull final HttpServletResponse aHttpResponse)
  {
    super (aHttpResponse);
  }

  public final void finishResponse ()
  {
    StreamUtils.close (m_aWriter);
    StreamUtils.close (m_aStream);
  }

  @Nonnull
  protected abstract ServletOutputStream createOutputStream () throws IOException;

  @Override
  public final void flushBuffer () throws IOException
  {
    if (m_aWriter != null)
      m_aWriter.flush ();
    else
      if (m_aStream != null)
        m_aStream.flush ();
  }

  @Override
  public final ServletOutputStream getOutputStream () throws IOException
  {
    if (m_aWriter != null)
      throw new IllegalStateException ("getWriter() has already been called!");

    if (m_aStream == null)
      m_aStream = createOutputStream ();
    return m_aStream;
  }

  @Override
  public final PrintWriter getWriter () throws IOException
  {
    if (m_aWriter != null)
      return m_aWriter;

    if (m_aStream != null)
      throw new IllegalStateException ("getOutputStream() has already been called!");

    m_aStream = createOutputStream ();
    m_aWriter = new PrintWriter (new OutputStreamWriter (m_aStream, CCharset.CHARSET_UTF_8));
    return m_aWriter;
  }
}
