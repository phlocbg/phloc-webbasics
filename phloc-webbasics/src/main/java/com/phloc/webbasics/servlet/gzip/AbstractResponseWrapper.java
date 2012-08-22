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
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.StringHelper;

/**
 * Abstract output stream switching {@link HttpServletResponseWrapper}
 * 
 * @author philip
 */
@NotThreadSafe
public abstract class AbstractResponseWrapper extends HttpServletResponseWrapper
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractResponseWrapper.class);

  private AbstractServletOutputStream m_aStream;
  private PrintWriter m_aWriter;
  private int m_nStatusCode = HttpServletResponse.SC_OK;

  public AbstractResponseWrapper (@Nonnull final HttpServletResponse aHttpResponse)
  {
    super (aHttpResponse);
  }

  public final void finishResponse (@Nonnull final String sRequestURL)
  {
    if (m_aStream != null && !m_aStream.isClosed ())
    {
      if (m_aWriter != null)
        StreamUtils.close (m_aWriter);
      else
        if (m_aStream != null)
          StreamUtils.close (m_aStream);
    }

    // Check if a content type was specified
    final String sContentType = getContentType ();
    if (StringHelper.hasNoText (sContentType))
      s_aLogger.error ("The response has no content type for request '" + sRequestURL + "'");
    else
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("The response has content type '" + sContentType + "' for request '" + sRequestURL + "'");
  }

  @Nonnull
  protected abstract AbstractServletOutputStream createOutputStream () throws IOException;

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
  @Nonnull
  public final ServletOutputStream getOutputStream () throws IOException
  {
    if (m_aWriter != null)
      throw new IllegalStateException ("getWriter() has already been called!");

    if (getResponse ().isCommitted ())
      return getResponse ().getOutputStream ();

    if (m_aStream == null)
      m_aStream = createOutputStream ();
    return m_aStream;
  }

  @Override
  @Nonnull
  public final PrintWriter getWriter () throws IOException
  {
    if (m_aWriter != null)
      return m_aWriter;

    if (getResponse ().isCommitted ())
      return getResponse ().getWriter ();

    if (m_aStream != null)
      throw new IllegalStateException ("getOutputStream() has already been called!");

    m_aStream = createOutputStream ();
    m_aWriter = new PrintWriter (new OutputStreamWriter (m_aStream, CCharset.CHARSET_UTF_8));
    return m_aWriter;
  }

  @Override
  public void setStatus (final int sc)
  {
    super.setStatus (sc);
    m_nStatusCode = sc;
  }

  @Override
  @Deprecated
  public void setStatus (final int sc, final String sm)
  {
    super.setStatus (sc, sm);
    m_nStatusCode = sc;
  }

  @Override
  public void sendError (final int sc, final String msg) throws IOException
  {
    super.sendError (sc, msg);
    m_nStatusCode = sc;
  }

  @Override
  public void sendError (final int sc) throws IOException
  {
    super.sendError (sc);
    m_nStatusCode = sc;
  }

  public int getStatusCode ()
  {
    return m_nStatusCode;
  }
}
