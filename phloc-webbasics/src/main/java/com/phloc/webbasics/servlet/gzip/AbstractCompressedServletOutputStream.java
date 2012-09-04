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
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.string.StringHelper;
import com.phloc.webbasics.http.CHTTPHeader;

/**
 * Special {@link ServletOutputStream} that knows whether it is closed or not
 * 
 * @author philip
 */
public abstract class AbstractCompressedServletOutputStream extends ServletOutputStream
{
  private final HttpServletRequest m_aHttpRequest;
  private final HttpServletResponse m_aHttpResponse;
  private final String m_sContentEncoding;
  private OutputStream m_aOS;
  private NonBlockingByteArrayOutputStream m_aBAOS;
  private DeflaterOutputStream m_aCompressedOS;
  private boolean m_bClosed = false;
  private boolean m_bDoNotCompress = false;
  private long m_nContentLength;
  private final long m_nMinCompressSize;

  public AbstractCompressedServletOutputStream (@Nonnull final HttpServletRequest aHttpRequest,
                                                @Nonnull final HttpServletResponse aHttpResponse,
                                                @Nonnull final String sContentEncoding,
                                                final long nContentLength,
                                                @Nonnegative final int nMinCompressSize) throws IOException
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");
    if (aHttpResponse == null)
      throw new NullPointerException ("httpResponse");
    if (StringHelper.hasNoText (sContentEncoding))
      throw new IllegalArgumentException ("content-encoding");
    m_aHttpRequest = aHttpRequest;
    m_aHttpResponse = aHttpResponse;
    m_sContentEncoding = sContentEncoding;
    m_nContentLength = nContentLength;
    m_nMinCompressSize = nMinCompressSize;
    if (nMinCompressSize == 0)
      doCompress ();
  }

  public final void resetBuffer ()
  {
    if (m_aHttpResponse.isCommitted ())
      throw new IllegalStateException ("Committed");
    m_aOS = null;
    m_aBAOS = null;
    if (m_aCompressedOS != null)
    {
      // Remove header again
      m_aHttpResponse.setHeader (CHTTPHeader.CONTENT_ENCODING, null);
      m_aCompressedOS = null;
    }
    m_bClosed = false;
    m_bDoNotCompress = false;
  }

  public final void setContentLength (final long nLength)
  {
    m_nContentLength = nLength;
    if (m_bDoNotCompress && nLength >= 0)
    {
      if (m_nContentLength < Integer.MAX_VALUE)
        m_aHttpResponse.setContentLength ((int) m_nContentLength);
      else
        m_aHttpResponse.setHeader (CHTTPHeader.CONTENT_LENGTH, Long.toString (m_nContentLength));
    }
  }

  @Nonnull
  protected abstract DeflaterOutputStream createDeflaterOutputStream (@Nonnull OutputStream aOS) throws IOException;

  public final void doCompress () throws IOException
  {
    if (m_aCompressedOS == null)
    {
      if (m_aHttpResponse.isCommitted ())
        throw new IllegalStateException ("Response already committed");

      m_aHttpResponse.setHeader (CHTTPHeader.CONTENT_ENCODING, m_sContentEncoding);
      if (m_aHttpResponse.containsHeader (CHTTPHeader.CONTENT_ENCODING))
      {
        m_aOS = m_aCompressedOS = createDeflaterOutputStream (m_aHttpResponse.getOutputStream ());
        if (m_aBAOS != null)
        {
          // Copy cached content to new OS
          m_aBAOS.writeTo (m_aOS);
          m_aBAOS = null;
        }
      }
      else
        doNotCompress ();
    }
  }

  public final void doNotCompress () throws IOException
  {
    if (m_aCompressedOS != null)
      throw new IllegalStateException ("Compressed output stream is already assigned.");
    if (m_aOS == null || m_aBAOS != null)
    {
      m_bDoNotCompress = true;

      m_aOS = m_aHttpResponse.getOutputStream ();
      setContentLength (m_nContentLength);
      if (m_aBAOS != null)
      {
        // Copy all cached content
        m_aBAOS.writeTo (m_aOS);
        m_aBAOS = null;
      }
    }
  }

  @Override
  public final void flush () throws IOException
  {
    if (m_aOS == null || m_aBAOS != null)
    {
      if (m_nContentLength > 0 && m_nContentLength < m_nMinCompressSize)
        doNotCompress ();
      else
        doCompress ();
    }

    m_aOS.flush ();
  }

  @Override
  public final void close () throws IOException
  {
    if (!m_bClosed)
    {
      if (m_aHttpRequest.getAttribute ("javax.servlet.include.request_uri") != null)
        flush ();
      else
      {
        if (m_aBAOS != null)
        {
          if (m_nContentLength < 0)
            m_nContentLength = m_aBAOS.size ();
          if (m_nContentLength < m_nMinCompressSize)
            doNotCompress ();
          else
            doCompress ();
        }
        else
          if (m_aOS == null)
            doNotCompress ();

        if (m_aCompressedOS != null)
          m_aCompressedOS.close ();
        else
          m_aOS.close ();
        m_bClosed = true;
      }
    }
  }

  public final boolean isClosed ()
  {
    return m_bClosed;
  }

  public final void finish () throws IOException
  {
    if (!m_bClosed)
    {
      if (m_aOS == null || m_aBAOS != null)
      {
        if (m_nContentLength > 0 && m_nContentLength < m_nMinCompressSize)
          doNotCompress ();
        else
          doCompress ();
      }

      if (m_aCompressedOS != null && !m_bClosed)
      {
        m_bClosed = true;
        m_aCompressedOS.close ();
      }
    }
  }

  private void _prepareToWrite (@Nonnegative final int length) throws IOException
  {
    if (m_bClosed)
      throw new IOException ("Already closed");

    if (m_aOS == null)
    {
      if (m_aHttpResponse.isCommitted () || (m_nContentLength >= 0 && m_nContentLength < m_nMinCompressSize))
        doNotCompress ();
      else
        if (length > m_nMinCompressSize)
          doCompress ();
        else
          m_aOS = m_aBAOS = new NonBlockingByteArrayOutputStream (8192);
    }
    else
      if (m_aBAOS != null)
      {
        if (m_aHttpResponse.isCommitted () || (m_nContentLength >= 0 && m_nContentLength < m_nMinCompressSize))
          doNotCompress ();
        else
          if (m_aBAOS.size () + length > m_nMinCompressSize)
            doCompress ();
      }
  }

  @Override
  public final void write (final int nByte) throws IOException
  {
    _prepareToWrite (1);
    m_aOS.write ((byte) nByte);
  }

  @Override
  public final void write (@Nonnull final byte [] aBytes) throws IOException
  {
    write (aBytes, 0, aBytes.length);
  }

  @Override
  public final void write (@Nonnull final byte [] aBytes, @Nonnegative final int nOfs, @Nonnegative final int nLen) throws IOException
  {
    _prepareToWrite (nLen);
    m_aOS.write (aBytes, nOfs, nLen);
  }

  @Nullable
  public final OutputStream getOutputStream ()
  {
    return m_aOS;
  }
}
