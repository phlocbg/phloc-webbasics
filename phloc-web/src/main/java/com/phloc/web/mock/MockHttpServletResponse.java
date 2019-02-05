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
import com.phloc.commons.ValueEnforcer;
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
import com.phloc.commons.string.StringHelper;
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
  private String m_sEncodeUrlSuffix;
  private String m_sEncodeRedirectUrlSuffix;

  public MockHttpServletResponse ()
  {}

  /**
   * Set whether {@link #getOutputStream()} access is allowed.
   * <p>
   * Default is <code>true</code>.
   *
   * @param bOutputStreamAccessAllowed
   *        Whether or not to allow it
   */
  public void setOutputStreamAccessAllowed (final boolean bOutputStreamAccessAllowed)
  {
    this.m_bOutputStreamAccessAllowed = bOutputStreamAccessAllowed;
  }

  /**
   * @return whether {@link #getOutputStream()} access is allowed.
   */
  public boolean isOutputStreamAccessAllowed ()
  {
    return this.m_bOutputStreamAccessAllowed;
  }

  /**
   * Set whether {@link #getWriter()} access is allowed.
   *
   * @param bWriterAccessAllowed
   *        Whether or not it is allowed. Default is <code>true</code>.
   */
  public void setWriterAccessAllowed (final boolean bWriterAccessAllowed)
  {
    this.m_bWriterAccessAllowed = bWriterAccessAllowed;
  }

  /**
   * @return whether {@link #getOutputStream()} access is allowed.
   */
  public boolean isWriterAccessAllowed ()
  {
    return this.m_bWriterAccessAllowed;
  }

  @Override
  public void setCharacterEncoding (@Nullable final String sCharacterEncoding)
  {
    setCharacterEncoding (sCharacterEncoding == null ? null : CharsetManager.getCharsetFromName (sCharacterEncoding));
  }

  public void setCharacterEncoding (@Nullable final Charset aCharacterEncoding)
  {
    this.m_aCharacterEncoding = aCharacterEncoding;
  }

  @Override
  @Nullable
  public String getCharacterEncoding ()
  {
    return this.m_aCharacterEncoding == null ? null : this.m_aCharacterEncoding.name ();
  }

  @Nullable
  public Charset getCharacterEncodingObj ()
  {
    return this.m_aCharacterEncoding;
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

  @Override
  @Nonnull
  public ServletOutputStream getOutputStream ()
  {
    if (!this.m_bOutputStreamAccessAllowed)
      throw new IllegalStateException ("OutputStream access not allowed");

    return this.m_aOS;
  }

  @Override
  @Nonnull
  public PrintWriter getWriter ()
  {
    if (!this.m_bWriterAccessAllowed)
      throw new IllegalStateException ("Writer access not allowed");

    if (this.m_aWriter == null)
    {
      final Writer aWriter = StreamUtils.createWriter (this.m_aContent, getCharacterEncodingObjOrDefault ());
      this.m_aWriter = new ResponsePrintWriter (aWriter);
    }
    return this.m_aWriter;
  }

  @Nonnull
  @ReturnsMutableCopy
  public byte [] getContentAsByteArray ()
  {
    flushBuffer ();
    return this.m_aContent.toByteArray ();
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
    return this.m_aContent.getAsString (sCharset);
  }

  @Nonnull
  public String getContentAsString (@Nonnull final Charset aCharset)
  {
    flushBuffer ();
    return this.m_aContent.getAsString (aCharset);
  }

  @Override
  public void setContentLength (final int nContentLength)
  {
    this.m_nContentLength = nContentLength;
  }

  public int getContentLength ()
  {
    return this.m_nContentLength;
  }

  @Override
  public void setContentType (@Nullable final String sContentType)
  {
    this.m_sContentType = sContentType;
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

  @Override
  @Nullable
  public String getContentType ()
  {
    return this.m_sContentType;
  }

  @Override
  public void setBufferSize (final int nBufferSize)
  {
    this.m_nBufferSize = nBufferSize;
  }

  @Override
  public int getBufferSize ()
  {
    return this.m_nBufferSize;
  }

  @Override
  public void flushBuffer ()
  {
    setCommitted (true);
  }

  /*
   * Throws exception if committed!
   */
  @Override
  public void resetBuffer ()
  {
    if (isCommitted ())
      throw new IllegalStateException ("Cannot reset buffer - response is already committed");
    this.m_aContent.reset ();
    this.m_aWriter = null;
  }

  private void _setCommittedIfBufferSizeExceeded ()
  {
    final int nBufSize = getBufferSize ();
    if (nBufSize > 0 && this.m_aContent.size () > nBufSize)
      setCommitted (true);
  }

  public void setCommitted (final boolean bCommitted)
  {
    this.m_bCommitted = bCommitted;
  }

  @Override
  public boolean isCommitted ()
  {
    return this.m_bCommitted;
  }

  /*
   * Throws exception if committed!
   */
  @Override
  public void reset ()
  {
    resetBuffer ();
    this.m_aCharacterEncoding = null;
    this.m_nContentLength = 0;
    this.m_sContentType = null;
    this.m_aLocale = null;
    this.m_aCookies.clear ();
    this.m_aHeaders.clear ();
    this.m_nStatus = HttpServletResponse.SC_OK;
    this.m_sErrorMessage = null;
  }

  @Override
  public void setLocale (@Nullable final Locale aLocale)
  {
    this.m_aLocale = aLocale;
  }

  @Override
  @Nullable
  public Locale getLocale ()
  {
    return this.m_aLocale;
  }

  // ---------------------------------------------------------------------
  // HttpServletResponse interface
  // ---------------------------------------------------------------------

  @Override
  public void addCookie (@Nonnull final Cookie aCookie)
  {
    ValueEnforcer.notNull (aCookie, "Cookie");
    this.m_aCookies.add (aCookie);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Cookie [] getCookies ()
  {
    return ArrayHelper.newArray (this.m_aCookies, Cookie.class);
  }

  @Nullable
  public Cookie getCookie (@Nonnull final String sName)
  {
    ValueEnforcer.notNull (sName, "Name");
    for (final Cookie aCookie : this.m_aCookies)
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

  @Override
  public boolean containsHeader (@Nullable final String sName)
  {
    return this.m_aHeaders.containsKey (_unifyHeaderName (sName));
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
    return ContainerHelper.newSet (this.m_aHeaders.keySet ());
  }

  /**
   * Return the primary value for the given header, if any.
   * <p>
   * Will return the first value in case of multiple values.
   *
   * @param sName
   *        the name of the header
   * @return the associated header value, or <code>null</code> if none
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
    return ContainerHelper.newList (this.m_aHeaders.get (_unifyHeaderName (sName)));
  }

  /**
   * The default implementation returns the given URL String as-is. Use
   * {@link #setEncodeUrlSuffix(String)} to define a suffix to be appended.
   *
   * @return the encoded URL
   */
  @Override
  @Nullable
  public String encodeURL (@Nullable final String sUrl)
  {
    if (StringHelper.hasText (this.m_sEncodeUrlSuffix))
      return StringHelper.getNotNull (sUrl) + this.m_sEncodeUrlSuffix;
    return sUrl;
  }

  /**
   * The default implementation returns the given URL String as-is. Use
   * {@link #setEncodeRedirectUrlSuffix(String)} to define a suffix to be
   * appended.
   *
   * @return the encoded URL
   */
  @Override
  @Nullable
  public String encodeRedirectURL (@Nullable final String sUrl)
  {
    if (StringHelper.hasText (this.m_sEncodeRedirectUrlSuffix))
      return StringHelper.getNotNull (sUrl) + this.m_sEncodeRedirectUrlSuffix;
    return sUrl;
  }

  @Override
  @Deprecated
  public String encodeUrl (@Nullable final String sUrl)
  {
    return encodeURL (sUrl);
  }

  @Override
  @Deprecated
  public String encodeRedirectUrl (@Nullable final String sUrl)
  {
    return encodeRedirectURL (sUrl);
  }

  @Override
  public void sendError (final int nStatus, @Nullable final String sErrorMessage) throws IOException
  {
    if (isCommitted ())
      throw new IllegalStateException ("Cannot set error status - response is already committed");
    this.m_nStatus = nStatus;
    this.m_sErrorMessage = sErrorMessage;
    setCommitted (true);
  }

  @Override
  public void sendError (final int nStatus) throws IOException
  {
    if (isCommitted ())
      throw new IllegalStateException ("Cannot set error status - response is already committed");
    this.m_nStatus = nStatus;
    setCommitted (true);
  }

  @Override
  public void sendRedirect (@Nonnull final String sUrl) throws IOException
  {
    if (isCommitted ())
      throw new IllegalStateException ("Cannot send redirect - response is already committed");
    ValueEnforcer.notNull (sUrl, "URL");
    this.m_sRedirectedUrl = sUrl;
    setCommitted (true);
  }

  @Nullable
  public String getRedirectedUrl ()
  {
    return this.m_sRedirectedUrl;
  }

  @Override
  public void setDateHeader (@Nullable final String sName, final long nValue)
  {
    _setHeaderValue (sName, Long.valueOf (nValue));
  }

  @Override
  public void addDateHeader (@Nullable final String sName, final long nValue)
  {
    _addHeaderValue (sName, Long.valueOf (nValue));
  }

  @Override
  public void setHeader (@Nullable final String sName, @Nullable final String sValue)
  {
    _setHeaderValue (sName, sValue);
  }

  @Override
  public void addHeader (@Nullable final String sName, @Nullable final String sValue)
  {
    _addHeaderValue (sName, sValue);
  }

  @Override
  public void setIntHeader (@Nullable final String sName, final int nValue)
  {
    _setHeaderValue (sName, Integer.valueOf (nValue));
  }

  @Override
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
    if (bReplace || !this.m_aHeaders.containsSingle (_unifyHeaderName (sName), aValue))
      this.m_aHeaders.putSingle (_unifyHeaderName (sName), aValue);
  }

  @Override
  public void setStatus (final int nStatus)
  {
    this.m_nStatus = nStatus;
  }

  @Override
  @Deprecated
  public void setStatus (final int nStatus, @Nullable final String sErrorMessage)
  {
    this.m_nStatus = nStatus;
    this.m_sErrorMessage = sErrorMessage;
  }

  public int getStatus ()
  {
    return this.m_nStatus;
  }

  @Nullable
  public String getErrorMessage ()
  {
    return this.m_sErrorMessage;
  }

  // Methods for MockRequestDispatcher
  public void setForwardedUrl (@Nullable final String sForwardedUrl)
  {
    this.m_sForwardedUrl = sForwardedUrl;
  }

  @Nullable
  public String getForwardedUrl ()
  {
    return this.m_sForwardedUrl;
  }

  public void setIncludedUrl (@Nullable final String sIncludedUrl)
  {
    this.m_sIncludedUrl = sIncludedUrl;
  }

  @Nullable
  public String getIncludedUrl ()
  {
    return this.m_sIncludedUrl;
  }

  public void setEncodeUrlSuffix (@Nullable final String sEncodeUrlSuffix)
  {
    this.m_sEncodeUrlSuffix = sEncodeUrlSuffix;
  }

  @Nullable
  public String getEncodeUrlSuffix ()
  {
    return this.m_sEncodeUrlSuffix;
  }

  public void setEncodeRedirectUrlSuffix (@Nullable final String sEncodeRedirectUrlSuffix)
  {
    this.m_sEncodeRedirectUrlSuffix = sEncodeRedirectUrlSuffix;
  }

  @Nullable
  public String getEncodeRedirectUrlSuffix ()
  {
    return this.m_sEncodeRedirectUrlSuffix;
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
