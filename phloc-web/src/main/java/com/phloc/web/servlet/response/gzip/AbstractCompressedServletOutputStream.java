/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.web.servlet.response.gzip;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.web.http.CHTTPHeader;
import com.phloc.web.servlet.response.ResponseHelper;

/**
 * Special {@link ServletOutputStream} that knows whether it is closed or not
 * 
 * @author Philip Helger
 */
public abstract class AbstractCompressedServletOutputStream extends ServletOutputStream
{
  private static final int DEFAULT_BUFSIZE = 8192;
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractCompressedServletOutputStream.class);
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
    this.m_aHttpRequest = ValueEnforcer.notNull (aHttpRequest, "HttpRequest"); //$NON-NLS-1$
    this.m_aHttpResponse = ValueEnforcer.notNull (aHttpResponse, "HttpResponse"); //$NON-NLS-1$
    this.m_sContentEncoding = ValueEnforcer.notEmpty (sContentEncoding, "ContentEncoding"); //$NON-NLS-1$
    this.m_nContentLength = nContentLength;
    this.m_nMinCompressSize = nMinCompressSize;
    if (nMinCompressSize == 0)
      doCompress ("ctor: no min compress size"); //$NON-NLS-1$
  }

  private static void _debugLog (final boolean bCompress, final String sMsg)
  {
    if (CompressFilterSettings.isDebugModeEnabled ())
      s_aLogger.info ((bCompress ? "Compressing: " : "Not compressing: ") + sMsg); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public final void resetBuffer ()
  {
    if (this.m_aHttpResponse.isCommitted ())
      throw new IllegalStateException ("Committed"); //$NON-NLS-1$
    this.m_aOS = null;
    this.m_aBAOS = null;
    if (this.m_aCompressedOS != null)
    {
      // Remove header again
      this.m_aHttpResponse.setHeader (CHTTPHeader.CONTENT_ENCODING, null);
      this.m_aCompressedOS = null;
    }
    this.m_bClosed = false;
    this.m_bDoNotCompress = false;
  }

  public final void setContentLength (final long nLength)
  {
    if (CompressFilterSettings.isDebugModeEnabled ())
      s_aLogger.info ("Setting content length to " + nLength + "; doNotCompress=" + this.m_bDoNotCompress); //$NON-NLS-1$ //$NON-NLS-2$
    this.m_nContentLength = nLength;
    if (this.m_bDoNotCompress && nLength >= 0)
      ResponseHelper.setContentLength (this.m_aHttpResponse, this.m_nContentLength);
  }

  @Nonnull
  protected abstract DeflaterOutputStream createDeflaterOutputStream (@Nonnull OutputStream aOS) throws IOException;

  public final void doCompress (@Nullable final String sDebugInfo) throws IOException
  {
    if (this.m_aCompressedOS == null)
    {
      if (this.m_aHttpResponse.isCommitted ())
        throw new IllegalStateException ("Response already committed"); //$NON-NLS-1$

      this.m_aHttpResponse.setHeader (CHTTPHeader.CONTENT_ENCODING, this.m_sContentEncoding);

      // Check if header was really set (may e.g. not be the case when something
      // is included like a JSP)
      if (this.m_aHttpResponse.containsHeader (CHTTPHeader.CONTENT_ENCODING))
      {
        _debugLog (true, sDebugInfo);

        this.m_aCompressedOS = createDeflaterOutputStream (this.m_aHttpResponse.getOutputStream ());
        this.m_aOS = this.m_aCompressedOS;
        if (this.m_aBAOS != null)
        {
          // Copy cached content to new OS
          this.m_aBAOS.writeTo (this.m_aOS);
          this.m_aBAOS = null;
        }
      }
      else
        doNotCompress ("from compress: included request"); //$NON-NLS-1$
    }
    else
    {
      if (CompressFilterSettings.isDebugModeEnabled ())
        s_aLogger.info ("doCompress on already compressed stream"); //$NON-NLS-1$
    }
  }

  public final void doNotCompress (final String sDebugInfo) throws IOException
  {
    if (this.m_aCompressedOS != null)
      throw new IllegalStateException ("Compressed output stream is already assigned."); //$NON-NLS-1$

    if (this.m_aOS == null || this.m_aBAOS != null)
    {
      this.m_bDoNotCompress = true;
      _debugLog (false, sDebugInfo);

      this.m_aOS = this.m_aHttpResponse.getOutputStream ();
      setContentLength (this.m_nContentLength);
      if (this.m_aBAOS != null)
      {
        // Copy all cached content
        this.m_aBAOS.writeTo (this.m_aOS);
        this.m_aBAOS = null;
      }
    }
  }

  @Override
  public final void flush () throws IOException
  {
    if (this.m_aOS == null || this.m_aBAOS != null)
    {
      if (this.m_nContentLength > 0 && this.m_nContentLength < this.m_nMinCompressSize)
        doNotCompress ("flush"); //$NON-NLS-1$
      else
        doCompress ("flush"); //$NON-NLS-1$
    }

    this.m_aOS.flush ();
  }

  @Override
  public final void close () throws IOException
  {
    if (!this.m_bClosed)
    {
      final Object aIncluded = this.m_aHttpRequest.getAttribute ("javax.servlet.include.request_uri"); //$NON-NLS-1$
      if (aIncluded != null)
      {
        if (CompressFilterSettings.isDebugModeEnabled ())
          s_aLogger.info ("No close because we're including " + aIncluded); //$NON-NLS-1$
        flush ();
      }
      else
      {
        if (this.m_aBAOS != null)
        {
          if (this.m_nContentLength < 0)
            this.m_nContentLength = this.m_aBAOS.size ();
          if (this.m_nContentLength < this.m_nMinCompressSize)
            doNotCompress ("close with buffer"); //$NON-NLS-1$
          else
            doCompress ("close with buffer"); //$NON-NLS-1$
        }
        else
          if (this.m_aOS == null)
            doNotCompress ("close without buffer"); //$NON-NLS-1$

        if (CompressFilterSettings.isDebugModeEnabled ())
          s_aLogger.info ("Closing stream. compressed=" + (this.m_aCompressedOS != null)); //$NON-NLS-1$
        if (this.m_aCompressedOS != null)
          this.m_aCompressedOS.close ();
        else
          this.m_aOS.close ();
        this.m_bClosed = true;
      }
    }
  }

  public final boolean isClosed ()
  {
    return this.m_bClosed;
  }

  public final void finishAndClose () throws IOException
  {
    if (!this.m_bClosed)
    {
      if (this.m_aOS == null || this.m_aBAOS != null)
      {
        if (this.m_nContentLength > 0 && this.m_nContentLength < this.m_nMinCompressSize)
          doNotCompress ("finish"); //$NON-NLS-1$
        else
          doCompress ("finish"); //$NON-NLS-1$
      }

      if (this.m_aCompressedOS != null && !this.m_bClosed)
      {
        if (CompressFilterSettings.isDebugModeEnabled ())
          s_aLogger.info ("Closing compressed stream in finish!"); //$NON-NLS-1$
        this.m_bClosed = true;
        this.m_aCompressedOS.close ();
      }
      else
      {
        if (CompressFilterSettings.isDebugModeEnabled ())
          s_aLogger.info ("Not closing anything in finish!"); //$NON-NLS-1$
      }
    }
  }

  private void _prepareToWrite (@Nonnegative final int nLength) throws IOException
  {
    if (this.m_bClosed)
      throw new IOException ("Already closed"); //$NON-NLS-1$

    if (this.m_aOS == null)
    {
      if (this.m_aHttpResponse.isCommitted ())
        doNotCompress ("_prepareToWrite new - response already committed"); //$NON-NLS-1$
      else
        if (this.m_nContentLength >= 0 && this.m_nContentLength < this.m_nMinCompressSize)
          doNotCompress ("_prepareToWrite new " + this.m_nContentLength); //$NON-NLS-1$
        else
          if (nLength > this.m_nMinCompressSize)
            doCompress ("_prepareToWrite new " + nLength); //$NON-NLS-1$
          else
          {
            if (CompressFilterSettings.isDebugModeEnabled ())
              s_aLogger.info ("Starting new output buffering!"); //$NON-NLS-1$
            this.m_aBAOS = new NonBlockingByteArrayOutputStream (DEFAULT_BUFSIZE);
            this.m_aOS = this.m_aBAOS;
          }
    }
    else
      if (this.m_aBAOS != null)
      {
        if (this.m_aHttpResponse.isCommitted ())
          doNotCompress ("_prepareToWrite buffered - response already committed"); //$NON-NLS-1$
        else
          if (this.m_nContentLength >= 0 && this.m_nContentLength < this.m_nMinCompressSize)
            doNotCompress ("_prepareToWrite buffered " + this.m_nContentLength); //$NON-NLS-1$
          else
            if (nLength >= this.m_aBAOS.getBufferSize () - this.m_aBAOS.size ())
              doCompress ("_prepareToWrite buffered " + nLength); //$NON-NLS-1$
            else
            {
              if (CompressFilterSettings.isDebugModeEnabled ())
                s_aLogger.info ("Continue buffering!"); //$NON-NLS-1$
            }
      }
    // Else a regular non-buffered OS is present (m_aOS != null)
  }

  @Override
  public final void write (final int nByte) throws IOException
  {
    _prepareToWrite (1);
    this.m_aOS.write ((byte) nByte);
  }

  @Override
  public final void write (@Nonnull final byte [] aBytes) throws IOException
  {
    write (aBytes, 0, aBytes.length);
  }

  @Override
  public final void write (@Nonnull final byte [] aBytes,
                           @Nonnegative final int nOfs,
                           @Nonnegative final int nLen) throws IOException
  {
    _prepareToWrite (nLen);
    this.m_aOS.write (aBytes, nOfs, nLen);
  }

  @Nullable
  public final OutputStream getOutputStream ()
  {
    return this.m_aOS;
  }

  @Override
  public boolean isReady ()
  {
    return !this.m_bClosed;
  }

  @Override
  public void setWriteListener (final WriteListener writeListener)
  {
    // not implemented
  }
}
