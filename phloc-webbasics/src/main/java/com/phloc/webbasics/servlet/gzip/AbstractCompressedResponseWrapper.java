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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.mime.MimeTypeDeterminator;
import com.phloc.commons.string.StringHelper;
import com.phloc.webbasics.http.AcceptEncodingHandler;
import com.phloc.webbasics.http.CHTTPHeader;

/**
 * Abstract output stream switching {@link HttpServletResponseWrapper}
 * 
 * @author philip
 */
@NotThreadSafe
public abstract class AbstractCompressedResponseWrapper extends HttpServletResponseWrapper
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractCompressedResponseWrapper.class);

  private final HttpServletRequest m_aHttpRequest;
  private final String m_sContentEncoding;
  private long m_nContentLength = -1;
  private final int m_nMinCompressSize = 256;
  private AbstractCompressedServletOutputStream m_aCompressedOS;
  private PrintWriter m_aWriter;
  private int m_nStatusCode = SC_OK;
  private boolean m_bNoCompression = false;

  public AbstractCompressedResponseWrapper (@Nonnull final HttpServletRequest aHttpRequest,
                                            @Nonnull final HttpServletResponse aHttpResponse,
                                            @Nonnull @Nonempty final String sContentEncoding)
  {
    super (aHttpResponse);
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");
    if (StringHelper.hasNoText (sContentEncoding))
      throw new IllegalArgumentException ("content-encoding");
    m_aHttpRequest = aHttpRequest;
    m_sContentEncoding = sContentEncoding;
  }

  public void noCompression ()
  {
    m_bNoCompression = true;
    if (m_aCompressedOS != null)
      try
      {
        m_aCompressedOS.doNotCompress ("requested from response wrapper");
      }
      catch (final IOException e)
      {
        throw new IllegalStateException (e);
      }
  }

  @Override
  public void setContentType (@Nullable final String sContentType)
  {
    super.setContentType (sContentType);

    // Is not output stream present yet?
    if (m_aCompressedOS == null || m_aCompressedOS.getOutputStream () == null)
    {
      // Extract the content type without the charset
      String sRealContentType = StringHelper.getUntilFirstExcl (sContentType, ';');
      if (sRealContentType == null)
        sRealContentType = sContentType;

      if (sRealContentType != null &&
          (sRealContentType.contains (AcceptEncodingHandler.DEFLATE_ENCODING) || sRealContentType.contains (AcceptEncodingHandler.GZIP_ENCODING)))
      {
        // Deflate or Gzip was manually set
        noCompression ();
      }
    }
  }

  private void _updateStatus (final int nStatusCode)
  {
    m_nStatusCode = nStatusCode;
    // sc<200 || sc==204 || sc==205 || sc>=300
    if (nStatusCode < SC_OK ||
        nStatusCode == SC_NO_CONTENT ||
        nStatusCode == SC_RESET_CONTENT ||
        nStatusCode >= SC_MULTIPLE_CHOICES)
    {
      noCompression ();
    }
  }

  @Override
  public void setStatus (final int sc)
  {
    super.setStatus (sc);
    _updateStatus (sc);
  }

  @Override
  @Deprecated
  public void setStatus (final int sc, final String sm)
  {
    super.setStatus (sc, sm);
    _updateStatus (sc);
  }

  protected void setContentLength (final long nLength)
  {
    m_nContentLength = nLength;
    if (m_aCompressedOS != null)
      m_aCompressedOS.setContentLength (nLength);
    else
      if (m_bNoCompression && m_nContentLength >= 0)
      {
        final HttpServletResponse aHttpResponse = (HttpServletResponse) getResponse ();
        if (m_nContentLength < Integer.MAX_VALUE)
          aHttpResponse.setContentLength ((int) m_nContentLength);
        else
          aHttpResponse.setHeader (CHTTPHeader.CONTENT_LENGTH, Long.toString (m_nContentLength));
      }
  }

  @Override
  public void addHeader (final String sHeaderName, final String sHeaderValue)
  {
    if (CHTTPHeader.CONTENT_LENGTH.equalsIgnoreCase (sHeaderName))
    {
      setContentLength (Long.parseLong (sHeaderValue));
    }
    else
      if (CHTTPHeader.CONTENT_TYPE.equalsIgnoreCase (sHeaderName))
      {
        setContentType (sHeaderValue);
      }
      else
        if (CHTTPHeader.CONTENT_ENCODING.equalsIgnoreCase (sHeaderName))
        {
          super.addHeader (sHeaderName, sHeaderValue);
          if (!isCommitted ())
            noCompression ();
        }
        else
          super.addHeader (sHeaderName, sHeaderValue);
  }

  @Override
  public void setHeader (final String sHeaderName, final String sHeaderValue)
  {
    if (CHTTPHeader.CONTENT_LENGTH.equalsIgnoreCase (sHeaderName))
    {
      setContentLength (Long.parseLong (sHeaderValue));
    }
    else
      if (CHTTPHeader.CONTENT_TYPE.equalsIgnoreCase (sHeaderName))
      {
        setContentType (sHeaderValue);
      }
      else
        if (CHTTPHeader.CONTENT_ENCODING.equalsIgnoreCase (sHeaderName))
        {
          super.setHeader (sHeaderName, sHeaderValue);
          if (!isCommitted ())
            noCompression ();
        }
        else
          super.setHeader (sHeaderName, sHeaderValue);
  }

  @Override
  public void setIntHeader (final String sHeaderName, final int nHeaderValue)
  {
    if (CHTTPHeader.CONTENT_LENGTH.equalsIgnoreCase (sHeaderName))
    {
      setContentLength (nHeaderValue);
    }
    else
      super.setIntHeader (sHeaderName, nHeaderValue);
  }

  @Override
  public final void flushBuffer () throws IOException
  {
    if (m_aWriter != null)
      m_aWriter.flush ();
    else
      if (m_aCompressedOS != null)
        m_aCompressedOS.finish ();
      else
        getResponse ().flushBuffer ();
  }

  @Override
  public void reset ()
  {
    super.reset ();
    if (m_aCompressedOS != null)
    {
      m_aCompressedOS.resetBuffer ();
      m_aCompressedOS = null;
    }
    m_aWriter = null;
    m_bNoCompression = false;
    m_nContentLength = -1;
  }

  @Override
  public void resetBuffer ()
  {
    super.resetBuffer ();
    if (m_aCompressedOS != null)
    {
      m_aCompressedOS.resetBuffer ();
      m_aCompressedOS = null;
    }
    m_aWriter = null;
  }

  @Override
  public void sendError (final int sc, final String msg) throws IOException
  {
    resetBuffer ();
    super.sendError (sc, msg);
    m_nStatusCode = sc;
  }

  @Override
  public void sendError (final int sc) throws IOException
  {
    resetBuffer ();
    super.sendError (sc);
    m_nStatusCode = sc;
  }

  @Override
  public void sendRedirect (final String sLocation) throws IOException
  {
    resetBuffer ();
    super.sendRedirect (sLocation);
    m_nStatusCode = SC_MOVED_TEMPORARILY;
  }

  public final void finish () throws IOException
  {
    if (false)
    {
      // Check if a content type was specified
      final String sRequestURL = m_aHttpRequest.getRequestURL ().toString ();
      final String sContentType = getContentType ();
      if (StringHelper.hasNoText (sContentType))
      {
        // Not important for redirects etc.
        if (m_nStatusCode >= SC_OK && m_nStatusCode < SC_MULTIPLE_CHOICES)
        {
          final String sDeterminedMimeType = MimeTypeDeterminator.getMimeTypeFromFilename (sRequestURL);
          if (sDeterminedMimeType == null)
            s_aLogger.error ("The response has no content type for request '" +
                             sRequestURL +
                             "' and failed to determine one");
          else
          {
            if (s_aLogger.isDebugEnabled ())
              s_aLogger.debug ("The response has no content type for request '" +
                               sRequestURL +
                               "' but determined '" +
                               sDeterminedMimeType +
                               "'");
            setContentType (sDeterminedMimeType);
          }
        }
      }
      else
      {
        // Content type is present
        if (s_aLogger.isDebugEnabled ())
          s_aLogger.debug ("The response has content type '" + sContentType + "' for request '" + sRequestURL + "'");
      }
    }

    if (m_aWriter != null && !m_aCompressedOS.isClosed ())
      m_aWriter.flush ();
    if (m_aCompressedOS != null)
      m_aCompressedOS.finish ();
  }

  @Nonnull
  protected abstract AbstractCompressedServletOutputStream createCompressedOutputStream (@Nonnull final HttpServletRequest aHttpRequest,
                                                                                         @Nonnull final HttpServletResponse aHttpResponse,
                                                                                         @Nonnull @Nonempty String sContentEncoding,
                                                                                         long nContentLength,
                                                                                         @Nonnegative int nMinCompressSize) throws IOException;

  @Nonnull
  private AbstractCompressedServletOutputStream _createCompressedOutputStream () throws IOException
  {
    return createCompressedOutputStream (m_aHttpRequest,
                                         (HttpServletResponse) getResponse (),
                                         m_sContentEncoding,
                                         m_nContentLength,
                                         m_nMinCompressSize);
  }

  @Override
  @Nonnull
  public final ServletOutputStream getOutputStream () throws IOException
  {
    if (m_aCompressedOS == null)
    {
      if (getResponse ().isCommitted () || m_bNoCompression)
        return getResponse ().getOutputStream ();

      m_aCompressedOS = _createCompressedOutputStream ();
    }
    else
      if (m_aWriter != null)
        throw new IllegalStateException ("getWriter() has already been called!");

    return m_aCompressedOS;
  }

  private static final class MyPrintWriter extends PrintWriter
  {
    public MyPrintWriter (@Nonnull final OutputStream aOS)
    {
      super (aOS);
    }

    public MyPrintWriter (@Nonnull final Writer aWriter)
    {
      super (aWriter);
    }

    @Override
    public void flush ()
    {
      // Intercept here for debugging as described in
      // http://stackoverflow.com/questions/7513922/jetty-8-gzipfilter-does-not-apply-sometimes
      super.flush ();
    }
  }

  @Override
  @Nonnull
  public final PrintWriter getWriter () throws IOException
  {
    if (m_aWriter == null)
    {
      if (m_aCompressedOS != null)
        throw new IllegalStateException ("getOutputStream() has already been called!");

      if (getResponse ().isCommitted () || m_bNoCompression)
        return getResponse ().getWriter ();

      m_aCompressedOS = _createCompressedOutputStream ();
      final String sEncoding = getCharacterEncoding ();
      if (sEncoding == null)
        m_aWriter = new MyPrintWriter (m_aCompressedOS);
      else
        m_aWriter = new MyPrintWriter (new OutputStreamWriter (m_aCompressedOS, sEncoding));
    }
    return m_aWriter;
  }

  public final int getStatusCode ()
  {
    return m_nStatusCode;
  }
}
