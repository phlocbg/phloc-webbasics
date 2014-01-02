/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.web.mock;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.IHasLocale;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.multimap.IMultiMapSetBased;
import com.phloc.commons.collections.multimap.MultiHashMapLinkedHashSetBased;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mime.MimeTypeParser;
import com.phloc.commons.mime.MimeTypeParserException;
import com.phloc.commons.mime.MimeTypeUtils;
import com.phloc.commons.system.SystemHelper;
import com.phloc.web.CWeb;

// ESCA-JAVA0116:
/**
 * Mock implementation of {@link HttpServletResponse}.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class MockHttpServletResponse implements HttpServletResponse, IHasLocale
{
  public static final int DEFAULT_SERVER_PORT = CWeb.DEFAULT_PORT_HTTP;
  public static final String DEFAULT_CHARSET_NAME = CCharset.CHARSET_UTF_8;
  public static final Charset DEFAULT_CHARSET_OBJ = CCharset.CHARSET_UTF_8_OBJ;
  private static final int DEFAULT_BUFFER_SIZE = 4096;
  private static final Logger s_aLogger = LoggerFactory.getLogger (MockHttpServletResponse.class);

  private boolean m_bOutputStreamAccessAllowed = true;
  private boolean m_bWriterAccessAllowed = true;
  private Charset m_aCharacterEncoding = DEFAULT_CHARSET_OBJ;
  private final NonBlockingByteArrayOutputStream m_aContent = new NonBlockingByteArrayOutputStream ();
  private final ServletOutputStream m_aOS = new ServletOutputStream ()
  {
    @Override
    public void write (final int b) throws IOException
    {
      MockHttpServletResponse.this.m_aContent.write (b);
      super.flush ();
      _setCommittedIfBufferSizeExceeded ();
    }

    @Override
    public void flush () throws IOException
    {
      super.flush ();
      setCommitted (true);
    }
  };
  private PrintWriter m_aWriter;
  private int m_nContentLength = 0;
  private String m_sContentType;
  private int m_nBufferSize = DEFAULT_BUFFER_SIZE;
  private boolean m_bCommitted;
  private Locale m_aLocale = Locale.getDefault ();

  // HttpServletResponse properties
  private final List <Cookie> m_aCookies = new ArrayList <Cookie> ();
  private final IMultiMapSetBased <String, Object> m_aHeaders = new MultiHashMapLinkedHashSetBased <String, Object> ();
  private int m_nStatus = HttpServletResponse.SC_OK;
  private String m_sErrorMessage;
  private String m_sRedirectedUrl;
  private String m_sForwardedUrl;
  private String m_sIncludedUrl;

  /**
   * Set whether {@link #getOutputStream()} access is allowed.
   * <p>
   * Default is <code>true</code>.
   * 
   * @param bOutputStreamAccessAllowed
   */
  public void setOutputStreamAccessAllowed (final boolean bOutputStreamAccessAllowed)
  {
    m_bOutputStreamAccessAllowed = bOutputStreamAccessAllowed;
  }

  /**
   * @return whether {@link #getOutputStream()} access is allowed.
   */
  public boolean isOutputStreamAccessAllowed ()
  {
    return m_bOutputStreamAccessAllowed;
  }

  /**
   * Set whether {@link #getWriter()} access is allowed.
   * <p>
   * Default is <code>true</code>.
   */
  public void setWriterAccessAllowed (final boolean bWriterAccessAllowed)
  {
    m_bWriterAccessAllowed = bWriterAccessAllowed;
  }

  /**
   * @return whether {@link #getOutputStream()} access is allowed.
   */
  public boolean isWriterAccessAllowed ()
  {
    return m_bWriterAccessAllowed;
  }

  public void setCharacterEncoding (@Nullable final String sCharacterEncoding)
  {
    setCharacterEncoding (sCharacterEncoding == null ? null : CharsetManager.getCharsetFromName (sCharacterEncoding));
  }

  public void setCharacterEncoding (@Nullable final Charset aCharacterEncoding)
  {
    m_aCharacterEncoding = aCharacterEncoding;
  }

  @Nullable
  public String getCharacterEncoding ()
  {
    return m_aCharacterEncoding == null ? null : m_aCharacterEncoding.name ();
  }

  @Nullable
  public Charset getCharacterEncodingObj ()
  {
    return m_aCharacterEncoding;
  }

  @Nonnull
  @Nonempty
  @Deprecated
  public String getCharacterEncodingOrDefault ()
  {
    String ret = getCharacterEncoding ();
    if (ret == null)
      ret = SystemHelper.getSystemCharsetName ();
    return ret;
  }

  @Nonnull
  public Charset getCharacterEncodingObjOrDefault ()
  {
    Charset ret = getCharacterEncodingObj ();
    if (ret == null)
      ret = SystemHelper.getSystemCharset ();
    return ret;
  }

  @Nonnull
  public ServletOutputStream getOutputStream ()
  {
    if (!m_bOutputStreamAccessAllowed)
      throw new IllegalStateException ("OutputStream access not allowed");

    return m_aOS;
  }

  @Nonnull
  public PrintWriter getWriter ()
  {
    if (!m_bWriterAccessAllowed)
      throw new IllegalStateException ("Writer access not allowed");

    if (m_aWriter == null)
    {
      final Writer aWriter = StreamUtils.createWriter (m_aContent, getCharacterEncodingObjOrDefault ());
      m_aWriter = new ResponsePrintWriter (aWriter);
    }
    return m_aWriter;
  }

  @Nonnull
  @ReturnsMutableCopy
  public byte [] getContentAsByteArray ()
  {
    flushBuffer ();
    return m_aContent.toByteArray ();
  }

  @Nonnull
  public String getContentAsString ()
  {
    return getContentAsString (getCharacterEncodingOrDefault ());
  }

  @Nonnull
  @Deprecated
  public String getContentAsString (@Nonnull @Nonempty final String sCharset)
  {
    flushBuffer ();
    return m_aContent.getAsString (sCharset);
  }

  @Nonnull
  public String getContentAsString (@Nonnull final Charset aCharset)
  {
    flushBuffer ();
    return m_aContent.getAsString (aCharset);
  }

  public void setContentLength (final int nContentLength)
  {
    m_nContentLength = nContentLength;
  }

  public int getContentLength ()
  {
    return m_nContentLength;
  }

  public void setContentType (@Nullable final String sContentType)
  {
    m_sContentType = sContentType;
    if (sContentType != null)
    {
      try
      {
        final IMimeType aContentType = MimeTypeParser.parseMimeType (sContentType);
        final String sEncoding = MimeTypeUtils.getCharsetNameFromMimeType (aContentType);
        if (sEncoding != null)
          setCharacterEncoding (sEncoding);
      }
      catch (final MimeTypeParserException ex)
      {
        s_aLogger.warn ("Passed content type '" + sContentType + "' cannot be parsed as a MIME type");
      }
    }
  }

  @Nullable
  public String getContentType ()
  {
    return m_sContentType;
  }

  public void setBufferSize (final int nBufferSize)
  {
    m_nBufferSize = nBufferSize;
  }

  public int getBufferSize ()
  {
    return m_nBufferSize;
  }

  public void flushBuffer ()
  {
    setCommitted (true);
  }

  /*
   * Throws exception if committed!
   */
  public void resetBuffer ()
  {
    if (isCommitted ())
      throw new IllegalStateException ("Cannot reset buffer - response is already committed");
    m_aContent.reset ();
    m_aWriter = null;
  }

  private void _setCommittedIfBufferSizeExceeded ()
  {
    final int nBufSize = getBufferSize ();
    if (nBufSize > 0 && m_aContent.size () > nBufSize)
      setCommitted (true);
  }

  public void setCommitted (final boolean bCommitted)
  {
    m_bCommitted = bCommitted;
  }

  public boolean isCommitted ()
  {
    return m_bCommitted;
  }

  /*
   * Throws exception if committed!
   */
  public void reset ()
  {
    resetBuffer ();
    m_aCharacterEncoding = null;
    m_nContentLength = 0;
    m_sContentType = null;
    m_aLocale = null;
    m_aCookies.clear ();
    m_aHeaders.clear ();
    m_nStatus = HttpServletResponse.SC_OK;
    m_sErrorMessage = null;
  }

  public void setLocale (@Nullable final Locale aLocale)
  {
    m_aLocale = aLocale;
  }

  @Nullable
  public Locale getLocale ()
  {
    return m_aLocale;
  }

  // ---------------------------------------------------------------------
  // HttpServletResponse interface
  // ---------------------------------------------------------------------

  public void addCookie (@Nonnull final Cookie aCookie)
  {
    if (aCookie == null)
      throw new NullPointerException ("Cookie must not be null");
    m_aCookies.add (aCookie);
  }

  @Nonnull
  public Cookie [] getCookies ()
  {
    return ArrayHelper.newArray (m_aCookies, Cookie.class);
  }

  @Nullable
  public Cookie getCookie (@Nonnull final String sName)
  {
    if (sName == null)
      throw new NullPointerException ("Cookie name must not be null");
    for (final Cookie aCookie : m_aCookies)
      if (sName.equals (aCookie.getName ()))
        return aCookie;
    return null;
  }

  @Nullable
  private static String _unifyHeaderName (@Nullable final String sName)
  {
    // Same as in MockHttpServletRequest
    return sName == null ? null : sName.toLowerCase (Locale.US);
  }

  public boolean containsHeader (@Nullable final String sName)
  {
    return m_aHeaders.containsKey (_unifyHeaderName (sName));
  }

  /**
   * Return the names of all specified headers as a Set of Strings.
   * 
   * @return the <code>Set</code> of header name <code>Strings</code>, or an
   *         empty <code>Set</code> if none
   */
  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getHeaderNames ()
  {
    return ContainerHelper.newSet (m_aHeaders.keySet ());
  }

  /**
   * Return the primary value for the given header, if any.
   * <p>
   * Will return the first value in case of multiple values.
   * 
   * @param sName
   *        the name of the header
   * @return the associated header value, or <code>null<code> if none
   */
  @Nullable
  public Object getHeader (@Nullable final String sName)
  {
    final List <Object> aList = getHeaders (sName);
    return ContainerHelper.getFirstElement (aList);
  }

  /**
   * Return all values for the given header as a List of value objects.
   * 
   * @param sName
   *        the name of the header
   * @return the associated header values, or an empty List if none
   */
  @Nonnull
  public List <Object> getHeaders (@Nullable final String sName)
  {
    return ContainerHelper.newList (m_aHeaders.get (_unifyHeaderName (sName)));
  }

  /**
   * The default implementation returns the given URL String as-is.
   * <p>
   * Can be overridden in subclasses, appending a session id or the like.
   * 
   * @return the encoded URL
   */
  @Nullable
  public String encodeURL (@Nullable final String sUrl)
  {
    return sUrl;
  }

  /**
   * The default implementation delegates to {@link #encodeURL}, returning the
   * given URL String as-is.
   * <p>
   * Can be overridden in subclasses, appending a session id or the like in a
   * redirect-specific fashion. For general URL encoding rules, override the
   * common {@link #encodeURL} method instead, appyling to redirect URLs as well
   * as to general URLs.
   * 
   * @return the encoded URL
   */
  @Nullable
  public String encodeRedirectURL (@Nullable final String sUrl)
  {
    return encodeURL (sUrl);
  }

  @Deprecated
  public String encodeUrl (@Nullable final String sUrl)
  {
    return encodeURL (sUrl);
  }

  @Deprecated
  public String encodeRedirectUrl (@Nullable final String sUrl)
  {
    return encodeRedirectURL (sUrl);
  }

  public void sendError (final int nStatus, @Nullable final String sErrorMessage) throws IOException
  {
    if (isCommitted ())
      throw new IllegalStateException ("Cannot set error status - response is already committed");
    m_nStatus = nStatus;
    m_sErrorMessage = sErrorMessage;
    setCommitted (true);
  }

  public void sendError (final int nStatus) throws IOException
  {
    if (isCommitted ())
      throw new IllegalStateException ("Cannot set error status - response is already committed");
    m_nStatus = nStatus;
    setCommitted (true);
  }

  public void sendRedirect (@Nonnull final String sUrl) throws IOException
  {
    if (isCommitted ())
      throw new IllegalStateException ("Cannot send redirect - response is already committed");
    if (sUrl == null)
      throw new NullPointerException ("Redirect URL must not be null");
    m_sRedirectedUrl = sUrl;
    setCommitted (true);
  }

  @Nullable
  public String getRedirectedUrl ()
  {
    return m_sRedirectedUrl;
  }

  public void setDateHeader (@Nullable final String sName, final long nValue)
  {
    _setHeaderValue (sName, Long.valueOf (nValue));
  }

  public void addDateHeader (@Nullable final String sName, final long nValue)
  {
    _addHeaderValue (sName, Long.valueOf (nValue));
  }

  public void setHeader (@Nullable final String sName, @Nullable final String sValue)
  {
    _setHeaderValue (sName, sValue);
  }

  public void addHeader (@Nullable final String sName, @Nullable final String sValue)
  {
    _addHeaderValue (sName, sValue);
  }

  public void setIntHeader (@Nullable final String sName, final int nValue)
  {
    _setHeaderValue (sName, Integer.valueOf (nValue));
  }

  public void addIntHeader (@Nullable final String sName, final int nValue)
  {
    _addHeaderValue (sName, Integer.valueOf (nValue));
  }

  private void _setHeaderValue (@Nullable final String sName, @Nullable final Object aValue)
  {
    _doAddHeaderValue (sName, aValue, true);
  }

  private void _addHeaderValue (@Nullable final String sName, @Nullable final Object aValue)
  {
    _doAddHeaderValue (sName, aValue, false);
  }

  private void _doAddHeaderValue (@Nullable final String sName, @Nullable final Object aValue, final boolean bReplace)
  {
    if (bReplace || !m_aHeaders.containsSingle (_unifyHeaderName (sName), aValue))
      m_aHeaders.putSingle (_unifyHeaderName (sName), aValue);
  }

  public void setStatus (final int nStatus)
  {
    m_nStatus = nStatus;
  }

  @Deprecated
  public void setStatus (final int nStatus, @Nullable final String sErrorMessage)
  {
    m_nStatus = nStatus;
    m_sErrorMessage = sErrorMessage;
  }

  public int getStatus ()
  {
    return m_nStatus;
  }

  @Nullable
  public String getErrorMessage ()
  {
    return m_sErrorMessage;
  }

  // Methods for MockRequestDispatcher
  public void setForwardedUrl (@Nullable final String sForwardedUrl)
  {
    m_sForwardedUrl = sForwardedUrl;
  }

  @Nullable
  public String getForwardedUrl ()
  {
    return m_sForwardedUrl;
  }

  public void setIncludedUrl (@Nullable final String sIncludedUrl)
  {
    m_sIncludedUrl = sIncludedUrl;
  }

  @Nullable
  public String getIncludedUrl ()
  {
    return m_sIncludedUrl;
  }

  /**
   * Inner class that adapts the PrintWriter to mark the response as committed
   * once the buffer size is exceeded.
   */
  private class ResponsePrintWriter extends PrintWriter
  {
    public ResponsePrintWriter (@Nonnull final Writer aOut)
    {
      super (aOut, true);
    }

    @Override
    public void write (final char aBuf[], final int nOff, final int nLen)
    {
      super.write (aBuf, nOff, nLen);
      super.flush ();
      _setCommittedIfBufferSizeExceeded ();
    }

    @Override
    public void write (final String sStr, final int nOff, final int nLen)
    {
      super.write (sStr, nOff, nLen);
      super.flush ();
      _setCommittedIfBufferSizeExceeded ();
    }

    @Override
    public void write (final int c)
    {
      super.write (c);
      super.flush ();
      _setCommittedIfBufferSizeExceeded ();
    }

    @Override
    public void flush ()
    {
      super.flush ();
      setCommitted (true);
    }
  }
}
