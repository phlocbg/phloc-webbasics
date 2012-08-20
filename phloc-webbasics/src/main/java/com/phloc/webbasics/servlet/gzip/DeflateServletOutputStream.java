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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Nonnull;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.webbasics.http.CHTTPHeader;

/**
 * Special {@link ServletOutputStream} that does deflate
 * 
 * @author philip
 */
final class DeflateServletOutputStream extends ServletOutputStream
{
  private final HttpServletResponse m_aHttpResponse;
  private final String m_sContentEncoding;
  private final NonBlockingByteArrayOutputStream m_aBAOS = new NonBlockingByteArrayOutputStream ();
  private final ZipOutputStream m_aZOS = new ZipOutputStream (m_aBAOS);
  private boolean m_bClosed = false;

  public DeflateServletOutputStream (@Nonnull final HttpServletResponse aHttpResponse,
                                     @Nonnull final String sContentEncoding) throws IOException
  {
    super ();
    m_aHttpResponse = aHttpResponse;
    m_sContentEncoding = sContentEncoding;
    // A dummy ZIP entry is required!
    m_aZOS.putNextEntry (new ZipEntry ("dummy name"));
  }

  @Override
  public void close () throws IOException
  {
    if (m_bClosed)
      throw new IOException ("This output stream has already been closed");

    // Finish Deflate stream
    m_aZOS.finish ();

    m_aHttpResponse.addHeader (CHTTPHeader.CONTENT_LENGTH, Integer.toString (m_aBAOS.size ()));
    m_aHttpResponse.addHeader (CHTTPHeader.CONTENT_ENCODING, m_sContentEncoding);
    final ServletOutputStream aOS = m_aHttpResponse.getOutputStream ();
    m_aBAOS.writeTo (aOS);
    aOS.flush ();
    aOS.close ();
    m_bClosed = true;
  }

  @Override
  public void flush () throws IOException
  {
    if (m_bClosed)
      throw new IOException ("Cannot flush a closed output stream");
    m_aZOS.flush ();
  }

  @Override
  public void write (final int b) throws IOException
  {
    if (m_bClosed)
      throw new IOException ("Cannot write to a closed output stream");
    m_aZOS.write ((byte) b);
  }

  @Override
  public void write (final byte b[]) throws IOException
  {
    write (b, 0, b.length);
  }

  @Override
  public void write (final byte b[], final int off, final int len) throws IOException
  {
    if (m_bClosed)
      throw new IOException ("Cannot write to a closed output stream");
    m_aZOS.write (b, off, len);
  }
}
