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
package com.phloc.webbasics.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.streams.CountingOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mutable.MutableLong;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.datetime.PDTFactory;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.webbasics.http.AcceptCharsetHandler;
import com.phloc.webbasics.http.AcceptCharsetList;
import com.phloc.webbasics.http.AcceptMimeTypeHandler;
import com.phloc.webbasics.http.AcceptMimeTypeList;
import com.phloc.webbasics.http.CHTTPHeader;
import com.phloc.webbasics.http.CacheControlBuilder;
import com.phloc.webbasics.http.EHTTPMethod;
import com.phloc.webbasics.http.EHTTPVersion;
import com.phloc.webbasics.http.HTTPHeaderMap;
import com.phloc.webbasics.http.QValue;

/**
 * This class tries to encapsulate all things required to build a HTTP response.
 * It offer warnings and consistency checks if something is missing.
 * 
 * @author philip
 */
public class UnifiedResponse
{
  public static interface IContentWriter
  {
    @Nonnull
    ESuccess write (@Nonnull @WillNotClose OutputStream aOS) throws IOException;
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (UnifiedResponse.class);
  private static final int MAX_CSS_KB_FOR_IE = 288;
  private static final AtomicInteger s_aRequestNum = new AtomicInteger (0);

  // Input fields
  private final EHTTPVersion m_eHTTPVersion;
  private final EHTTPMethod m_eHTTPMethod;
  private final String m_sRequestURL;
  private final AcceptCharsetList m_aAcceptCharsetList;
  private final AcceptMimeTypeList m_aAcceptMimeTypeList;

  // Main response fields
  private Charset m_aCharset;
  private IMimeType m_aMimeType;
  private byte [] m_aContent;
  private IInputStreamProvider m_aContentISP;
  private IContentWriter m_aContentWriter;
  private String m_sContentDispositionFilename;
  private CacheControlBuilder m_aCacheControl;
  private final HTTPHeaderMap m_aResponseHeaderMap = new HTTPHeaderMap ();
  private int m_nStatusCode = CGlobal.ILLEGAL_UINT;
  private String m_sRedirectTargetUrl;
  private Map <String, Cookie> m_aCookies;

  // Internal status members
  /**
   * Unique internal ID for each response, so that error messages can be more
   * easily aggregated.
   */
  private final int m_nID = s_aRequestNum.incrementAndGet ();
  /**
   * Just avoid emitting the request headers more than once, as they wont change
   * from error to error.
   */
  private boolean m_bEmittedRequestHeaders = false;
  /** This maps keeps all the response headers for later emitting. */
  private final HTTPHeaderMap m_aRequestHeaderMap;
  /**
   * An optional encode to be used to determine if a content-disposition
   * filename can be ISO-8859-1 encoded.
   */
  private CharsetEncoder m_aContentDispositionEncoder;

  public UnifiedResponse (@Nonnull final EHTTPVersion eHTTPVersion,
                          @Nonnull final EHTTPMethod eHTTPMethod,
                          @Nonnull final IRequestWebScope aRequestScope)
  {
    if (eHTTPVersion == null)
      throw new NullPointerException ("httpVersion");
    if (eHTTPMethod == null)
      throw new NullPointerException ("httpMethod");
    if (aRequestScope == null)
      throw new NullPointerException ("requestScope");
    m_eHTTPVersion = eHTTPVersion;
    m_eHTTPMethod = eHTTPMethod;
    m_sRequestURL = aRequestScope.getURL ();
    final HttpServletRequest aHttpRequest = aRequestScope.getRequest ();
    m_aAcceptCharsetList = AcceptCharsetHandler.getAcceptCharsets (aHttpRequest);
    m_aAcceptMimeTypeList = AcceptMimeTypeHandler.getAcceptMimeTypes (aHttpRequest);
    m_aRequestHeaderMap = RequestHelper.getRequestHeaderMap (aHttpRequest);
  }

  @Nonnull
  @Nonempty
  private String _getPrefix ()
  {
    return "UnifiedResponse[" + m_nID + "] to [" + m_sRequestURL + "]: ";
  }

  private void _info (@Nonnull final String sMsg)
  {
    s_aLogger.info (_getPrefix () + sMsg);
  }

  private void _showRequestInfo ()
  {
    if (!m_bEmittedRequestHeaders)
    {
      s_aLogger.warn ("  Request Headers: " +
                      ContainerHelper.getSortedByKey (RequestLogger.getRequestHeaderMap (m_aRequestHeaderMap)));
      m_bEmittedRequestHeaders = true;
    }
  }

  private void _warn (@Nonnull final String sMsg)
  {
    s_aLogger.warn (_getPrefix () + sMsg);
    _showRequestInfo ();
  }

  private void _error (@Nonnull final String sMsg)
  {
    s_aLogger.error (_getPrefix () + sMsg);
    _showRequestInfo ();
  }

  @Nonnull
  public final EHTTPVersion getHTTPVersion ()
  {
    return m_eHTTPVersion;
  }

  @Nonnull
  public final EHTTPMethod getHTTPMethod ()
  {
    return m_eHTTPMethod;
  }

  @Nullable
  public Charset getCharset ()
  {
    return m_aCharset;
  }

  @Nonnull
  public UnifiedResponse setCharset (@Nonnull final Charset aCharset)
  {
    if (aCharset == null)
      throw new NullPointerException ("charset");
    if (m_aCharset != null)
      _info ("Overwriting charset from " + m_aCharset + " to " + aCharset);
    m_aCharset = aCharset;
    return this;
  }

  @Nonnull
  public UnifiedResponse removeCharset ()
  {
    m_aCharset = null;
    return this;
  }

  @Nullable
  public IMimeType getMimeType ()
  {
    return m_aMimeType;
  }

  @Nonnull
  public UnifiedResponse setMimeType (@Nonnull final IMimeType aMimeType)
  {
    if (aMimeType == null)
      throw new NullPointerException ("mimeType");
    if (m_aMimeType != null)
      _info ("Overwriting MimeType from " + m_aMimeType + " to " + aMimeType);
    m_aMimeType = aMimeType;
    return this;
  }

  @Nonnull
  public UnifiedResponse removeMimeType ()
  {
    m_aMimeType = null;
    return this;
  }

  /**
   * @return <code>true</code> if a content was already set, <code>false</code>
   *         if not.
   */
  public boolean hasContent ()
  {
    return m_aContent != null || m_aContentISP != null || m_aContentWriter != null;
  }

  /**
   * Utility method to set an empty response content.
   * 
   * @return this
   */
  @Nonnull
  public UnifiedResponse setEmptyContent ()
  {
    return setContent (new byte [0]);
  }

  /**
   * Utility method to set content and charset at once.
   * 
   * @param sContent
   *        The response content string. May not be <code>null</code>.
   * @param aCharset
   *        The charset to use. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setContentAndCharset (@Nonnull final String sContent, @Nonnull final Charset aCharset)
  {
    if (sContent == null)
      throw new NullPointerException ("content");
    setCharset (aCharset);
    setContent (CharsetManager.getAsBytes (sContent, aCharset));
    return this;
  }

  /**
   * Set the response content. To return an empty response pass in a new empty
   * array, but not <code>null</code>.
   * 
   * @param aContent
   *        The content to be returned. Is <b>not</b> copied inside! May not be
   *        <code>null</code> but maybe empty.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setContent (@Nonnull final byte [] aContent)
  {
    if (aContent == null)
      throw new NullPointerException ("content");
    if (hasContent ())
      _info ("Overwriting content with byte array!");
    m_aContent = aContent;
    m_aContentISP = null;
    m_aContentWriter = null;
    return this;
  }

  /**
   * Set the response content provider.
   * 
   * @param aISP
   *        The content provider to be used. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setContent (@Nonnull final IInputStreamProvider aISP)
  {
    if (aISP == null)
      throw new NullPointerException ("content");
    if (hasContent ())
      _info ("Overwriting content with content provider!");
    m_aContent = null;
    m_aContentISP = aISP;
    m_aContentWriter = null;
    return this;
  }

  /**
   * Set the response content writer.
   * 
   * @param aContentWriter
   *        The content writer to be used. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setContent (@Nonnull final IContentWriter aContentWriter)
  {
    if (aContentWriter == null)
      throw new NullPointerException ("contentWriter");
    if (hasContent ())
      _info ("Overwriting content with content writer!");
    m_aContent = null;
    m_aContentISP = null;
    m_aContentWriter = aContentWriter;
    return this;
  }

  @Nonnull
  public UnifiedResponse removeContent ()
  {
    m_aContent = null;
    m_aContentISP = null;
    m_aContentWriter = null;
    return this;
  }

  @Nonnull
  public UnifiedResponse setExpires (@Nonnull final LocalDateTime aLDT)
  {
    m_aResponseHeaderMap.setDateHeader (CHTTPHeader.EXPIRES, aLDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse setExpires (@Nonnull final DateTime aDT)
  {
    m_aResponseHeaderMap.setDateHeader (CHTTPHeader.EXPIRES, aDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeExpires ()
  {
    m_aResponseHeaderMap.removeHeaders (CHTTPHeader.EXPIRES);
    return this;
  }

  @Nonnull
  public UnifiedResponse setLastModified (@Nonnull final LocalDateTime aLDT)
  {
    if (m_eHTTPMethod != EHTTPMethod.GET && m_eHTTPMethod != EHTTPMethod.HEAD)
      _warn ("Setting Last-Modified on a non GET or HEAD request may have no impact!");

    m_aResponseHeaderMap.setDateHeader (CHTTPHeader.LAST_MODIFIED, aLDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse setLastModified (@Nonnull final DateTime aDT)
  {
    if (m_eHTTPMethod != EHTTPMethod.GET && m_eHTTPMethod != EHTTPMethod.HEAD)
      _warn ("Setting Last-Modified on a non GET or HEAD request may have no impact!");

    m_aResponseHeaderMap.setDateHeader (CHTTPHeader.LAST_MODIFIED, aDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeLastModified ()
  {
    m_aResponseHeaderMap.removeHeaders (CHTTPHeader.LAST_MODIFIED);
    return this;
  }

  /**
   * Set an ETag for the response. The ETag must be a quoted value (being
   * surrounded by double quotes).
   * 
   * @param sETag
   *        The quoted ETag to be set. May neither be <code>null</code> nor
   *        empty.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setETag (@Nonnull @Nonempty final String sETag)
  {
    if (StringHelper.hasNoText (sETag))
      throw new IllegalArgumentException ("An empty ETag is not allowed!");
    if (!sETag.startsWith ("\""))
      throw new IllegalArgumentException ("Etag must start with a double quote character: " + sETag);
    if (!sETag.endsWith ("\""))
      throw new IllegalArgumentException ("Etag must end with a double quote character: " + sETag);
    if (m_eHTTPMethod != EHTTPMethod.GET)
      _warn ("Setting an ETag on a non-GET request may have no impact!");

    m_aResponseHeaderMap.setHeader (CHTTPHeader.ETAG, sETag);
    return this;
  }

  /**
   * Set an ETag for the response if this is an HTTP/1.1 response. HTTP/1.0 does
   * not supprt ETags. The ETag must be a quoted value (being surrounded by
   * double quotes).
   * 
   * @param sETag
   *        The quoted ETag to be set. May neither be <code>null</code> nor
   *        empty.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setETagIfApplicable (@Nonnull @Nonempty final String sETag)
  {
    if (m_eHTTPVersion == EHTTPVersion.HTTP_11)
      setETag (sETag);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeETag ()
  {
    m_aResponseHeaderMap.removeHeaders (CHTTPHeader.ETAG);
    return this;
  }

  @Nonnull
  public UnifiedResponse setContentDispositionFilename (@Nonnull @Nonempty final String sFilename)
  {
    if (StringHelper.hasNoText (sFilename))
      throw new IllegalArgumentException ("An empty filename is not allowed!");

    // Ensure that a valid filename is used
    // -> Strip all paths and replace all invalid characters
    final String sFilenameToUse = FilenameHelper.getWithoutPath (FilenameHelper.getAsSecureValidFilename (sFilename));
    if (!sFilename.equals (sFilenameToUse))
      _warn ("Content-Dispostion filename was modified from '" + sFilename + "' to '" + sFilenameToUse + "'");

    // Check if encoding as ISO-8859-1 is possible
    if (m_aContentDispositionEncoder == null)
      m_aContentDispositionEncoder = CCharset.CHARSET_ISO_8859_1_OBJ.newEncoder ();
    if (!m_aContentDispositionEncoder.canEncode (sFilenameToUse))
      _error ("Content-Dispostion  filename '" + sFilenameToUse + "' cannot be encoded to ISO-8859-1!");

    // Are we overwriting?
    if (m_sContentDispositionFilename != null)
      _info ("Overwriting Content-Dispostion filename from '" +
             m_sContentDispositionFilename +
             "' to '" +
             sFilenameToUse +
             "'");

    // No URL encoding necessary.
    // Filename must be in ISO-8859-1
    // See http://greenbytes.de/tech/tc2231/
    m_sContentDispositionFilename = sFilenameToUse;
    return this;
  }

  @Nullable
  public String getContentDispositionFilename ()
  {
    return m_sContentDispositionFilename;
  }

  @Nonnull
  public UnifiedResponse removeContentDispositionFilename ()
  {
    m_sContentDispositionFilename = null;
    return this;
  }

  /**
   * Utility method for setting the MimeType application/force-download and set
   * the respective content disposition filename.
   * 
   * @param sFilename
   *        The filename to be used.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setDownloadFilename (@Nonnull @Nonempty final String sFilename)
  {
    setMimeType (CMimeType.APPLICATION_FORCE_DOWNLOAD);
    setContentDispositionFilename (sFilename);
    return this;
  }

  @Nonnull
  public UnifiedResponse setCacheControl (@Nonnull final CacheControlBuilder aCacheControl)
  {
    if (aCacheControl == null)
      throw new NullPointerException ("cacheControl");
    if (m_aCacheControl != null)
      _info ("Overwriting Cache-Control data from '" +
             m_aCacheControl.getAsHTTPHeaderValue () +
             "' to '" +
             aCacheControl.getAsHTTPHeaderValue () +
             "'");
    m_aCacheControl = aCacheControl;
    return this;
  }

  @Nullable
  @ReturnsMutableObject (reason = "Design")
  public CacheControlBuilder getCacheControl ()
  {
    return m_aCacheControl;
  }

  @Nonnull
  public UnifiedResponse removeCacheControl ()
  {
    m_aCacheControl = null;
    return this;
  }

  /**
   * A utility method that disables caching for this response.
   * 
   * @return this
   */
  @Nonnull
  public UnifiedResponse disableCaching ()
  {
    // Remove any eventually set headers
    removeExpires ();
    removeCacheControl ();
    removeETag ();
    removeLastModified ();
    m_aResponseHeaderMap.removeHeaders (CHTTPHeader.PRAGMA);

    switch (m_eHTTPVersion)
    {
      case HTTP_10:
      {
        // Set to expire far in the past for HTTP/1.0.
        m_aResponseHeaderMap.setHeader (CHTTPHeader.EXPIRES, ResponseHelper.EXPIRES_NEVER_STRING);

        // Set standard HTTP/1.0 no-cache header.
        m_aResponseHeaderMap.setHeader (CHTTPHeader.PRAGMA, "no-cache");
        break;
      }
      case HTTP_11:
      {
        final CacheControlBuilder aCacheControlBuilder = new CacheControlBuilder ().setNoStore (true)
                                                                                   .setNoCache (true)
                                                                                   .setMustRevalidate (true)
                                                                                   .setProxyRevalidate (true);

        // Set IE extended HTTP/1.1 no-cache headers.
        // http://aspnetresources.com/blog/cache_control_extensions
        // Disabled because:
        // http://blogs.msdn.com/b/ieinternals/archive/2009/07/20/using-post_2d00_check-and-pre_2d00_check-cache-directives.aspx
        if (false)
          aCacheControlBuilder.addExtension ("post-check=0").addExtension ("pre-check=0");

        setCacheControl (aCacheControlBuilder);
        break;
      }
    }
    return this;
  }

  /**
   * Enable caching of this resource for the specified number of seconds.
   * 
   * @param nSeconds
   *        The number of seconds caching is allowed. Must be &gt; 0.
   * @return this
   */
  @Nonnull
  public UnifiedResponse enableCaching (@Nonnegative final int nSeconds)
  {
    if (nSeconds <= 0)
      throw new NullPointerException ("seconds");

    // Remove any eventually set headers
    // Note: don't remove Last-Modified and ETag!
    removeExpires ();
    removeCacheControl ();
    m_aResponseHeaderMap.removeHeaders (CHTTPHeader.PRAGMA);

    switch (m_eHTTPVersion)
    {
      case HTTP_10:
      {
        m_aResponseHeaderMap.setDateHeader (CHTTPHeader.EXPIRES, PDTFactory.getCurrentDateTime ()
                                                                           .plusSeconds (nSeconds));
        break;
      }
      case HTTP_11:
      {
        final CacheControlBuilder aCacheControlBuilder = new CacheControlBuilder ().setPublic (true)
                                                                                   .setMaxAgeSeconds (nSeconds);
        setCacheControl (aCacheControlBuilder);
        break;
      }
    }
    return this;
  }

  @Nonnull
  public UnifiedResponse setStatus (@Nonnegative final int nStatusCode)
  {
    if (m_nStatusCode != CGlobal.ILLEGAL_UINT)
      _info ("Overwriting status code " + m_nStatusCode + " with " + nStatusCode);
    m_nStatusCode = nStatusCode;
    return this;
  }

  @Nonnull
  public UnifiedResponse setRedirect (@Nonnull final ISimpleURL aRedirectTargetUrl)
  {
    if (aRedirectTargetUrl == null)
      throw new NullPointerException ("redirectTargetUrl");
    return setRedirect (aRedirectTargetUrl.getAsString ());
  }

  @Nonnull
  public UnifiedResponse setRedirect (@Nonnull @Nonempty final String sRedirectTargetUrl)
  {
    if (StringHelper.hasNoText (sRedirectTargetUrl))
      throw new IllegalArgumentException ("redirectTargetUrl may not be null");
    if (m_sRedirectTargetUrl != null)
      _info ("Overwriting redirect target URL '" + m_sRedirectTargetUrl + "' with '" + m_sRedirectTargetUrl + "'");
    m_sRedirectTargetUrl = sRedirectTargetUrl;
    return this;
  }

  @Nonnull
  public UnifiedResponse addCookie (@Nonnull final Cookie aCookie)
  {
    if (aCookie == null)
      throw new NullPointerException ("cookie");

    final String sKey = aCookie.getName ();
    if (m_aCookies == null)
      m_aCookies = new HashMap <String, Cookie> ();
    else
      if (m_aCookies.containsKey (sKey))
        _warn ("Overwriting cookie '" + sKey + "' with the new value '" + aCookie.getValue () + "'");
    m_aCookies.put (sKey, aCookie);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeCookie (@Nullable final String sName)
  {
    if (m_aCookies != null)
      m_aCookies.remove (sName);
    return this;
  }

  private void _verifyCachingIntegrity ()
  {
    final boolean bIsHttp11 = m_eHTTPVersion == EHTTPVersion.HTTP_11;
    final boolean bExpires = m_aResponseHeaderMap.containsHeaders (CHTTPHeader.EXPIRES);
    final boolean bCacheControl = m_aCacheControl != null;
    final boolean bLastModified = m_aResponseHeaderMap.containsHeaders (CHTTPHeader.LAST_MODIFIED);
    final boolean bETag = m_aResponseHeaderMap.containsHeaders (CHTTPHeader.ETAG);

    if (bExpires)
      _info ("Expires found: " + m_aResponseHeaderMap.getHeaders (CHTTPHeader.EXPIRES));

    if (bExpires && bCacheControl)
      _warn ("Expires and Cache-Control are both present. Cache-Control takes precedence!");

    if (bETag && !bIsHttp11)
      _warn ("Sending an ETag for HTTP version " + m_eHTTPVersion + " has no effect!");

    if (!bExpires && !bCacheControl)
    {
      if (bLastModified || bETag)
        _warn ("Validators (Last-Modified and ETag) have no effect if no Expires or Cache-Control is present");
      else
        _warn ("Response has no caching information at all");
    }

    if (m_aCacheControl != null)
    {
      if (!bIsHttp11)
        _warn ("Sending a Cache-Control header for HTTP version " + m_eHTTPVersion + " may have no or limited effect!");

      if (m_aCacheControl.isPrivate ())
      {
        if (m_aCacheControl.isPublic ())
          _warn ("Cache-Control cannot be private and public at the same time");

        if (m_aCacheControl.hasMaxAgeSeconds ())
          _warn ("Cache-Control cannot be private and have a max-age definition");

        if (m_aCacheControl.hasSharedMaxAgeSeconds ())
          _warn ("Cache-Control cannot be private and have a s-maxage definition");
      }
    }
  }

  public void applyToResponse (@Nonnull final HttpServletResponse aHttpResponse) throws IOException
  {
    if (aHttpResponse == null)
      throw new NullPointerException ("httpResponse");

    // Apply all collected headers
    for (final Map.Entry <String, List <String>> aEntry : m_aResponseHeaderMap)
    {
      final String sHeaderName = aEntry.getKey ();
      int nIndex = 0;
      for (final String sHeaderValue : aEntry.getValue ())
      {
        if (nIndex == 0)
          aHttpResponse.setHeader (sHeaderName, sHeaderValue);
        else
          aHttpResponse.addHeader (sHeaderName, sHeaderValue);
        ++nIndex;
      }
    }

    if (m_sRedirectTargetUrl != null)
    {
      if (m_aCharset != null)
        _warn ("Ignoring provided charset because a redirect is specified!");
      if (m_aMimeType != null)
        _warn ("Ignoring provided MimeType because a redirect is specified!");
      if (hasContent ())
        _warn ("Ignoring provided content because a redirect is specified!");
      if (m_sContentDispositionFilename != null)
        _warn ("Ignoring provided Content-Dispostion filename because a redirect is specified!");

      // Note: After using this method, the response should be
      // considered to be committed and should not be written to.
      aHttpResponse.sendRedirect (aHttpResponse.encodeRedirectURL (m_sRedirectTargetUrl));
      return;
    }

    if (m_nStatusCode != CGlobal.ILLEGAL_UINT)
    {
      if (m_aCharset != null)
        _warn ("Ignoring provided charset because a status code is specified!");
      if (m_aMimeType != null)
        _warn ("Ignoring provided MimeType because a status code is specified!");
      if (hasContent ())
        _warn ("Ignoring provided content because a status code is specified!");
      if (m_sContentDispositionFilename != null)
        _warn ("Ignoring provided Content-Dispostion filename because a status code is specified!");

      if (m_nStatusCode >= HttpServletResponse.SC_BAD_REQUEST)
      {
        // It's an error
        // Note: After using this method, the response should be considered
        // to be committed and should not be written to.
        aHttpResponse.sendError (m_nStatusCode);
      }
      else
      {
        // It's a status message "only"
        // Note: The container clears the buffer and sets the Location
        // header, preserving cookies and other headers.
        aHttpResponse.setStatus (m_nStatusCode);
      }
      return;
    }

    // Verify only if is a response with content
    _verifyCachingIntegrity ();

    if (m_aCacheControl != null)
    {
      final String sCacheControlValue = m_aCacheControl.getAsHTTPHeaderValue ();
      if (StringHelper.hasText (sCacheControlValue))
        aHttpResponse.setHeader (CHTTPHeader.CACHE_CONTROL, sCacheControlValue);
      else
        _warn ("An empty Cache-Control was provided!");
    }

    if (m_sContentDispositionFilename != null)
    {
      // Filename needs to be surrounded with double quotes (single quotes
      // don't work).
      aHttpResponse.setHeader (CHTTPHeader.CONTENT_DISPOSITION, "attachment; filename=\"" +
                                                                m_sContentDispositionFilename +
                                                                "\"");
      if (m_aMimeType == null)
      {
        _warn ("Content-Disposition is specified but no MimeType is set. Using the default download MimeType.");
        aHttpResponse.setContentType (CMimeType.APPLICATION_FORCE_DOWNLOAD.getAsString ());
      }
    }

    // Mime type
    if (m_aMimeType != null)
    {
      final String sMimeType = m_aMimeType.getAsString ();

      // Check with request accept mime types
      final QValue aQuality = m_aAcceptMimeTypeList.getQValueOfMimeType (m_aMimeType);
      if (aQuality.isMinimumQuality ())
        _warn ("MimeType '" +
               sMimeType +
               "' is not at all supported by the request. Allowed values are: " +
               m_aAcceptMimeTypeList.getAllQValuesGreaterThan (aQuality.getQuality ()));
      else
        if (aQuality.isLowValue ())
        {
          // Inform if the quality of the request is <= 50%!
          final Map <IMimeType, QValue> aBetterValues = m_aAcceptMimeTypeList.getAllQValuesGreaterThan (aQuality.getQuality ());
          if (!aBetterValues.isEmpty ())
            _info ("MimeType '" +
                   sMimeType +
                   "' is not best supported by the request (" +
                   aQuality +
                   "). Better MimeTypes are: " +
                   aBetterValues);
        }

      aHttpResponse.setContentType (sMimeType);
    }
    else
      _warn ("No MimeType present");

    // Charset
    if (m_aCharset != null)
    {
      final String sCharset = m_aCharset.name ();
      if (m_aMimeType == null)
        _warn ("If no MimeType present, the client cannot get notified about the character encoding '" + sCharset + "'");

      // Check with request charset
      final QValue aQuality = m_aAcceptCharsetList.getQValueOfCharset (sCharset);
      if (aQuality.isMinimumQuality ())
        _warn ("Character encoding '" +
               sCharset +
               "' is not at all supported by the request. Allowed values are: " +
               m_aAcceptCharsetList.getAllQValuesGreaterThan (aQuality.getQuality ()));
      else
        if (aQuality.isLowValue ())
        {
          // Inform if the quality of the request is <= 50%!
          final Map <String, QValue> aBetterValues = m_aAcceptCharsetList.getAllQValuesGreaterThan (aQuality.getQuality ());
          if (!aBetterValues.isEmpty ())
            _info ("Character encoding '" +
                   sCharset +
                   "' is not best supported by the request (" +
                   aQuality +
                   "). Better charsets are: " +
                   aBetterValues);
        }

      aHttpResponse.setCharacterEncoding (sCharset);
    }
    else
      if (m_aMimeType == null)
        _warn ("Also no character encoding present");
      else
        switch (m_aMimeType.getContentType ())
        {
          case TEXT:
          case MULTIPART:
            _warn ("A character encoding for MimeType '" + m_aMimeType.getAsString () + "' is appreciated.");
            break;
          default:
            // Do we need character encoding here as well???
            break;
        }

    // Add all cookies
    if (m_aCookies != null)
      for (final Cookie aCookie : m_aCookies.values ())
        aHttpResponse.addCookie (aCookie);

    // Write the body to the response
    _applyContent (aHttpResponse);
  }

  private void _applyLengthChecks (final long nContentLength)
  {
    // Source:
    // http://joshua.perina.com/africa/gambia/fajara/post/internet-explorer-css-file-size-limit
    if (m_aMimeType != null &&
        m_aMimeType.equals (CMimeType.TEXT_CSS) &&
        nContentLength > (MAX_CSS_KB_FOR_IE * CGlobal.BYTES_PER_KILOBYTE_LONG))
    {
      _warn ("Internet Explorer has problems handling CSS files > " +
             MAX_CSS_KB_FOR_IE +
             "KB and this one has " +
             nContentLength +
             " bytes!");
    }
  }

  private void _applyContent (@Nonnull final HttpServletResponse aHttpResponse) throws IOException
  {
    if (m_aContent != null)
    {
      // We're having a fixed byte array of content
      final int nContentLength = m_aContent.length;
      aHttpResponse.setContentLength (nContentLength);

      if (nContentLength > 0)
      {
        // Don't emit content for HEAD method
        if (m_eHTTPMethod.isContentAllowed ())
        {
          // Emit main content to stream
          final OutputStream aOS = aHttpResponse.getOutputStream ();
          aOS.write (m_aContent);
          aOS.flush ();
          aOS.close ();
        }

        _applyLengthChecks (nContentLength);
      }
      // Don't send 204, as this is most likely not handled correctly on the
      // client side
    }
    else
      if (m_aContentISP != null)
      {
        // We have a dynamic content input stream
        // -> no content length can be determined!
        final InputStream aContentIS = m_aContentISP.getInputStream ();
        if (aContentIS == null)
        {
          s_aLogger.error ("Failed to open input stream from " + m_aContentISP);

          // Handle it gracefully with a 204 and not with a 500
          aHttpResponse.setStatus (HttpServletResponse.SC_NO_CONTENT);
        }
        else
        {
          // Don't emit content for HEAD method
          if (m_eHTTPMethod.isContentAllowed ())
          {
            // We do have an input stream
            // -> copy it to the response
            final OutputStream aOS = aHttpResponse.getOutputStream ();
            final MutableLong aByteCount = new MutableLong ();
            if (StreamUtils.copyInputStreamToOutputStream (aContentIS, aOS, aByteCount).isSuccess ())
            {
              // Copying succeeded
              final long nBytesCopied = aByteCount.longValue ();
              aHttpResponse.setHeader (CHTTPHeader.CONTENT_LENGTH, Long.toString (nBytesCopied));
              if (nBytesCopied > 0)
              {
                // We had at least one content byte
                aOS.flush ();

                _applyLengthChecks (nBytesCopied);
              }
              // Else - simply empty content
              aOS.close ();
            }
            else
            {
              // Copying failed -> this is a 500
              _error ("Copying from " + m_aContentISP + " failed after " + aByteCount.longValue () + " bytes!");

              if (!aHttpResponse.isCommitted ())
                aHttpResponse.reset ();

              aHttpResponse.sendError (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
          }
        }
      }
      else
        if (m_aContentWriter != null)
        {
          // Don't emit content for HEAD method
          if (m_eHTTPMethod.isContentAllowed ())
          {
            // We do have an input stream
            // -> copy it to the response
            final OutputStream aOS = aHttpResponse.getOutputStream ();
            final CountingOutputStream aCOS = new CountingOutputStream (aOS);
            if (m_aContentWriter.write (aCOS).isSuccess ())
            {
              // Copying succeeded
              final long nBytesCopied = aCOS.getBytesWritten ();
              aHttpResponse.setHeader (CHTTPHeader.CONTENT_LENGTH, Long.toString (nBytesCopied));
              if (nBytesCopied > 0)
              {
                // We had at least one content byte
                aOS.flush ();
                _applyLengthChecks (nBytesCopied);
              }
              // Else - simply empty content
              aOS.close ();
            }
            else
            {
              // Copying failed -> this is a 500
              _error ("Copying from " + m_aContentWriter + " failed after " + aCOS.getBytesWritten () + " bytes!");

              if (!aHttpResponse.isCommitted ())
                aHttpResponse.reset ();

              aHttpResponse.sendError (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
          }
        }
        else
        {
          // Set status 204 - no content; this is most likely a programming
          // error
          aHttpResponse.setStatus (HttpServletResponse.SC_NO_CONTENT);
          _warn ("No content present for the response");
        }
  }
}
