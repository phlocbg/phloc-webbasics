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
package com.phloc.web.servlet.response;

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
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mime.MimeTypeParser;
import com.phloc.commons.mutable.MutableLong;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.URLUtils;
import com.phloc.datetime.PDTFactory;
import com.phloc.web.encoding.RFC5987Encoder;
import com.phloc.web.http.AcceptCharsetHandler;
import com.phloc.web.http.AcceptCharsetList;
import com.phloc.web.http.AcceptMimeTypeHandler;
import com.phloc.web.http.AcceptMimeTypeList;
import com.phloc.web.http.CHTTPHeader;
import com.phloc.web.http.CacheControlBuilder;
import com.phloc.web.http.EHTTPMethod;
import com.phloc.web.http.EHTTPVersion;
import com.phloc.web.http.HTTPHeaderMap;
import com.phloc.web.http.QValue;
import com.phloc.web.servlet.cookie.CookieHelper;
import com.phloc.web.servlet.request.RequestHelper;
import com.phloc.web.servlet.request.RequestLogger;
import com.phloc.web.useragent.browser.BrowserInfo;
import com.phloc.web.useragent.browser.EBrowserType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class encapsulates all things required to build a HTTP response. It
 * offer warnings and consistency checks if something is missing.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class UnifiedResponse
{
  /** By default content is not allowed on redirect */
  public static final boolean DEFAULT_ALLOW_CONTENT_ON_REDIRECT = false;
  /** By default content is not allowed for status codes */
  public static final boolean DEFAULT_ALLOW_CONTENT_ON_STATUS_CODE = false;
  /** Default content disposition type is Attachment */
  public static final EContentDispositionType DEFAULT_CONTENT_DISPOSITION_TYPE = EContentDispositionType.ATTACHMENT;
  /** Maximum KB a CSS file might have in IE */
  public static final int MAX_CSS_KB_FOR_IE = 288;

  private static final Logger s_aLogger = LoggerFactory.getLogger (UnifiedResponse.class);
  private static final AtomicInteger s_aRequestNum = new AtomicInteger (0);

  // Input fields
  private final EHTTPVersion m_eHTTPVersion;
  private final EHTTPMethod m_eHTTPMethod;
  private final HttpServletRequest m_aHttpRequest;
  private final AcceptCharsetList m_aAcceptCharsetList;
  private final AcceptMimeTypeList m_aAcceptMimeTypeList;
  private BrowserInfo m_aRequestBrowserInfo;

  // Settings
  /**
   * Flag which determines whether content is allow, if a redirect is set. This
   * is rarely used.
   */
  private boolean m_bAllowContentOnRedirect = DEFAULT_ALLOW_CONTENT_ON_REDIRECT;

  /**
   * Flag which determines whether content is allow, if a status code is set.
   * This is rarely used.
   */
  private boolean m_bAllowContentOnStatusCode = DEFAULT_ALLOW_CONTENT_ON_STATUS_CODE;

  // Main response fields
  private Charset m_aCharset;
  private IMimeType m_aMimeType;
  private byte [] m_aContent;
  private IInputStreamProvider m_aContentISP;
  private INonThrowingRunnableWithParameter <ESuccess> m_aOnContentStreamConsumed;
  private EContentDispositionType m_eContentDispositionType = DEFAULT_CONTENT_DISPOSITION_TYPE;
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
   * The request URL, lazily initialized.
   */
  private String m_sRequestURL;

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

  /**
   * Constructor
   *
   * @param aHttpRequest
   *        The main HTTP request
   */
  public UnifiedResponse (@Nonnull final HttpServletRequest aHttpRequest)
  {
    this (RequestHelper.getHttpVersion (aHttpRequest), RequestHelper.getHttpMethod (aHttpRequest), aHttpRequest);
  }

  /**
   * Constructor
   *
   * @param eHTTPVersion
   *        HTTP version of this request (1.0 or 1.1)
   * @param eHTTPMethod
   *        HTTP method of this request (GET, POST, ...)
   * @param aHttpRequest
   *        The main HTTP request
   */
  public UnifiedResponse (@Nonnull final EHTTPVersion eHTTPVersion,
                          @Nonnull final EHTTPMethod eHTTPMethod,
                          @Nonnull final HttpServletRequest aHttpRequest)
  {
    this.m_eHTTPVersion = ValueEnforcer.notNull (eHTTPVersion, "HTTPVersion");
    this.m_eHTTPMethod = ValueEnforcer.notNull (eHTTPMethod, "HTTPMethod");
    this.m_aHttpRequest = ValueEnforcer.notNull (aHttpRequest, "HTTPRequest");
    this.m_aAcceptCharsetList = AcceptCharsetHandler.getAcceptCharsets (aHttpRequest);
    this.m_aAcceptMimeTypeList = AcceptMimeTypeHandler.getAcceptMimeTypes (aHttpRequest);
    this.m_aRequestHeaderMap = RequestHelper.getRequestHeaderMap (aHttpRequest);
  }

  @Nonnull
  @Nonempty
  private String _getPrefix ()
  {
    if (this.m_sRequestURL == null)
      this.m_sRequestURL = RequestHelper.getURL (this.m_aHttpRequest);
    return "UnifiedResponse[" + this.m_nID + "] to [" + this.m_sRequestURL + "]: ";
  }

  private void _info (@Nonnull final String sMsg)
  {
    s_aLogger.info (_getPrefix () + sMsg);
  }

  private void _showRequestInfo ()
  {
    if (!this.m_bEmittedRequestHeaders)
    {
      s_aLogger.warn ("  Request Headers: " +
                      ContainerHelper.getSortedByKey (RequestLogger.getHTTPHeaderMap (this.m_aRequestHeaderMap)));
      if (!this.m_aResponseHeaderMap.isEmpty ())
        s_aLogger.warn ("  Response Headers: " +
                        ContainerHelper.getSortedByKey (RequestLogger.getHTTPHeaderMap (this.m_aResponseHeaderMap)));
      this.m_bEmittedRequestHeaders = true;
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
    return this.m_eHTTPVersion;
  }

  @Nonnull
  public final EHTTPMethod getHTTPMethod ()
  {
    return this.m_eHTTPMethod;
  }

  @Nullable
  public BrowserInfo getRequestBrowserInfo ()
  {
    return this.m_aRequestBrowserInfo;
  }

  @Nonnull
  public UnifiedResponse setRequestBrowserInfo (@Nullable final BrowserInfo aRequestBrowserInfo)
  {
    this.m_aRequestBrowserInfo = aRequestBrowserInfo;
    return this;
  }

  /**
   * @return <code>true</code> if content is allowed even if a redirect is
   *         present.
   */
  public boolean isAllowContentOnRedirect ()
  {
    return this.m_bAllowContentOnRedirect;
  }

  @Nonnull
  public UnifiedResponse setAllowContentOnRedirect (final boolean bAllowContentOnRedirect)
  {
    this.m_bAllowContentOnRedirect = bAllowContentOnRedirect;
    return this;
  }

  /**
   * @return <code>true</code> if content is allowed even if a status code is
   *         present.
   */
  public boolean isAllowContentOnStatusCode ()
  {
    return this.m_bAllowContentOnStatusCode;
  }

  @Nonnull
  public UnifiedResponse setAllowContentOnStatusCode (final boolean bAllowContentOnStatusCode)
  {
    this.m_bAllowContentOnStatusCode = bAllowContentOnStatusCode;
    return this;
  }

  @Nullable
  public Charset getCharset ()
  {
    return this.m_aCharset;
  }

  @Nonnull
  public UnifiedResponse setCharset (@Nonnull final Charset aCharset)
  {
    ValueEnforcer.notNull (aCharset, "Charset");
    if (this.m_aCharset != null)
      _info ("Overwriting charset from " + this.m_aCharset + " to " + aCharset);
    this.m_aCharset = aCharset;
    return this;
  }

  @Nonnull
  public UnifiedResponse removeCharset ()
  {
    this.m_aCharset = null;
    return this;
  }

  @Nullable
  public IMimeType getMimeType ()
  {
    return this.m_aMimeType;
  }

  @Nonnull
  public UnifiedResponse setMimeType (@Nonnull final IMimeType aMimeType)
  {
    ValueEnforcer.notNull (aMimeType, "MimeType");
    if (this.m_aMimeType != null)
      _info ("Overwriting MimeType from " + this.m_aMimeType + " to " + aMimeType);
    this.m_aMimeType = aMimeType;
    return this;
  }

  @Nonnull
  public UnifiedResponse setMimeTypeString (@Nonnull @Nonempty final String sMimeType)
  {
    ValueEnforcer.notEmpty (sMimeType, "MimeType");

    final IMimeType aMimeType = MimeTypeParser.parseMimeType (sMimeType);
    if (aMimeType != null)
      setMimeType (aMimeType);
    else
      _error ("Failed to resolve mime type from '" + sMimeType + "'");
    return this;
  }

  @Nonnull
  public UnifiedResponse removeMimeType ()
  {
    this.m_aMimeType = null;
    return this;
  }

  /**
   * @return <code>true</code> if a content was already set, <code>false</code>
   *         if not.
   */
  public boolean hasContent ()
  {
    return this.m_aContent != null || this.m_aContentISP != null;
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
    ValueEnforcer.notNull (sContent, "Content");
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
  @SuppressFBWarnings ("EI_EXPOSE_REP2")
  public UnifiedResponse setContent (@Nonnull final byte [] aContent)
  {
    ValueEnforcer.notNull (aContent, "Content");
    if (hasContent ())
      _info ("Overwriting content with byte array!");
    this.m_aContent = aContent;
    this.m_aContentISP = null;
    this.m_aOnContentStreamConsumed = null;
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
    return setContent (aISP, null);
  }

  /**
   * Set the response content provider.
   *
   * @param aISP
   *        The content provider to be used. May not be <code>null</code>.
   * @param aOnContentStreamConsumed
   *        An optional callback which will be executed after the input stream
   *        has been delivered (written to the response output stream), may be
   *        <code>null</code>
   * @return this
   */
  @Nonnull
  public UnifiedResponse setContent (@Nonnull final IInputStreamProvider aISP,
                                     @Nullable final INonThrowingRunnableWithParameter <ESuccess> aOnContentStreamConsumed)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");
    if (hasContent ())
      _info ("Overwriting content with content provider!");
    this.m_aContent = null;
    this.m_aContentISP = aISP;
    this.m_aOnContentStreamConsumed = aOnContentStreamConsumed;
    return this;
  }

  @Nonnull
  public UnifiedResponse removeContent ()
  {
    this.m_aContent = null;
    this.m_aContentISP = null;
    this.m_aOnContentStreamConsumed = null;
    return this;
  }

  @Nonnull
  public UnifiedResponse setExpires (@Nonnull final DateTime aDT)
  {
    this.m_aResponseHeaderMap.setDateHeader (CHTTPHeader.EXPIRES, aDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeExpires ()
  {
    this.m_aResponseHeaderMap.removeHeaders (CHTTPHeader.EXPIRES);
    return this;
  }

  @Nonnull
  public UnifiedResponse setLastModified (@Nonnull final DateTime aDT)
  {
    if (this.m_eHTTPMethod != EHTTPMethod.GET && this.m_eHTTPMethod != EHTTPMethod.HEAD)
      _warn ("Setting Last-Modified on a non GET or HEAD request may have no impact!");

    this.m_aResponseHeaderMap.setDateHeader (CHTTPHeader.LAST_MODIFIED, aDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeLastModified ()
  {
    this.m_aResponseHeaderMap.removeHeaders (CHTTPHeader.LAST_MODIFIED);
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
    ValueEnforcer.notEmpty (sETag, "ETag");
    if (!sETag.startsWith ("\"") && !sETag.startsWith ("W/\""))
      throw new IllegalArgumentException ("Etag must start with a '\"' character or with 'W/\"': " + sETag);
    if (!sETag.endsWith ("\""))
      throw new IllegalArgumentException ("Etag must end with a '\"' character: " + sETag);
    if (this.m_eHTTPMethod != EHTTPMethod.GET)
      _warn ("Setting an ETag on a non-GET request may have no impact!");

    this.m_aResponseHeaderMap.setHeader (CHTTPHeader.ETAG, sETag);
    return this;
  }

  /**
   * Set an ETag for the response if this is an HTTP/1.1 response. HTTP/1.0 does
   * not support ETags. The ETag must be a quoted value (being surrounded by
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
    if (this.m_eHTTPVersion == EHTTPVersion.HTTP_11)
      setETag (sETag);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeETag ()
  {
    this.m_aResponseHeaderMap.removeHeaders (CHTTPHeader.ETAG);
    return this;
  }

  @Nonnull
  public UnifiedResponse setContentDispositionType (@Nonnull final EContentDispositionType eContentDispositionType)
  {
    ValueEnforcer.notNull (eContentDispositionType, "ContentDispositionType");

    this.m_eContentDispositionType = eContentDispositionType;
    return this;
  }

  @Nonnull
  public EContentDispositionType getContentDispositionType ()
  {
    return this.m_eContentDispositionType;
  }

  @Nonnull
  public UnifiedResponse setContentDispositionFilename (@Nonnull @Nonempty final String sFilename)
  {
    ValueEnforcer.notEmpty (sFilename, "Filename");

    // Ensure that a valid filename is used
    // -> Strip all paths and replace all invalid characters
    final String sFilenameToUse = FilenameHelper.getWithoutPath (FilenameHelper.getAsSecureValidFilename (sFilename));
    if (!sFilename.equals (sFilenameToUse))
      _warn ("Content-Dispostion filename was internally modified from '" +
             sFilename +
             "' to '" +
             sFilenameToUse +
             "'");

    // Are we overwriting?
    if (this.m_sContentDispositionFilename != null)
      _info ("Overwriting Content-Dispostion filename from '" +
             this.m_sContentDispositionFilename +
             "' to '" +
             sFilenameToUse +
             "'");

    // No URL encoding necessary.
    // Filename must be in ISO-8859-1
    // See http://greenbytes.de/tech/tc2231/
    this.m_sContentDispositionFilename = sFilenameToUse;
    return this;
  }

  @Nullable
  public String getContentDispositionFilename ()
  {
    return this.m_sContentDispositionFilename;
  }

  @Nonnull
  public UnifiedResponse removeContentDispositionFilename ()
  {
    this.m_sContentDispositionFilename = null;
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
    ValueEnforcer.notNull (aCacheControl, "CacheControl");

    if (this.m_aCacheControl != null)
      _info ("Overwriting Cache-Control data from '" +
             this.m_aCacheControl.getAsHTTPHeaderValue () +
             "' to '" +
             aCacheControl.getAsHTTPHeaderValue () +
             "'");
    this.m_aCacheControl = aCacheControl;
    return this;
  }

  @Nullable
  @ReturnsMutableObject (reason = "Design")
  public CacheControlBuilder getCacheControl ()
  {
    return this.m_aCacheControl;
  }

  @Nonnull
  public UnifiedResponse removeCacheControl ()
  {
    this.m_aCacheControl = null;
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
    this.m_aResponseHeaderMap.removeHeaders (CHTTPHeader.PRAGMA);

    switch (this.m_eHTTPVersion)
    {
      case HTTP_10:
      {
        // Set to expire far in the past for HTTP/1.0.
        this.m_aResponseHeaderMap.setHeader (CHTTPHeader.EXPIRES, ResponseHelperSettings.EXPIRES_NEVER_STRING);

        // Set standard HTTP/1.0 no-cache header.
        this.m_aResponseHeaderMap.setHeader (CHTTPHeader.PRAGMA, "no-cache");
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
        // aCacheControlBuilder.addExtension ("post-check=0").addExtension
        // ("pre-check=0");

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
    ValueEnforcer.isGT0 (nSeconds, "Seconds");

    // Remove any eventually set headers
    // Note: don't remove Last-Modified and ETag!
    removeExpires ();
    removeCacheControl ();
    this.m_aResponseHeaderMap.removeHeaders (CHTTPHeader.PRAGMA);

    switch (this.m_eHTTPVersion)
    {
      case HTTP_10:
      {
        this.m_aResponseHeaderMap.setDateHeader (CHTTPHeader.EXPIRES,
                                                 PDTFactory.getCurrentDateTime ().plusSeconds (nSeconds));
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

  private void _setStatus (@Nonnegative final int nStatusCode)
  {
    if (this.m_nStatusCode != CGlobal.ILLEGAL_UINT)
      _info ("Overwriting status code " + this.m_nStatusCode + " with " + nStatusCode);
    this.m_nStatusCode = nStatusCode;
  }

  /**
   * Set the status code to be returned from the response.
   *
   * @param nStatusCode
   *        The status code to be set. Must be a valid HTTP response code.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setStatus (@Nonnegative final int nStatusCode)
  {
    _setStatus (nStatusCode);
    return this;
  }

  /**
   * Special handling for returning status code 401 UNAUTHORIZED.
   *
   * @param sAuthenticate
   *        The string to be used for the {@link CHTTPHeader#WWW_AUTHENTICATE}
   *        response header. May be <code>null</code> or empty.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setStatusUnauthorized (@Nullable final String sAuthenticate)
  {
    _setStatus (HttpServletResponse.SC_UNAUTHORIZED);
    if (StringHelper.hasText (sAuthenticate))
      this.m_aResponseHeaderMap.setHeader (CHTTPHeader.WWW_AUTHENTICATE, sAuthenticate);
    return this;
  }

  @Nonnull
  public UnifiedResponse setRedirect (@Nonnull final ISimpleURL aRedirectTargetUrl)
  {
    ValueEnforcer.notNull (aRedirectTargetUrl, "RedirectTargetUrl");

    return setRedirect (aRedirectTargetUrl.getAsString ());
  }

  @Nonnull
  public UnifiedResponse setRedirect (@Nonnull @Nonempty final String sRedirectTargetUrl)
  {
    ValueEnforcer.notEmpty (sRedirectTargetUrl, "RedirectTargetUrl");

    if (this.m_sRedirectTargetUrl != null)
      _info ("Overwriting redirect target URL '" +
             this.m_sRedirectTargetUrl +
             "' with '" +
             this.m_sRedirectTargetUrl +
             "'");
    this.m_sRedirectTargetUrl = sRedirectTargetUrl;
    return this;
  }

  @Nonnull
  public UnifiedResponse addCookie (@Nonnull final Cookie aCookie)
  {
    ValueEnforcer.notNull (aCookie, "Cookie");

    final String sKey = aCookie.getName ();
    if (this.m_aCookies == null)
      this.m_aCookies = new HashMap <String, Cookie> ();
    else
      if (this.m_aCookies.containsKey (sKey))
        _warn ("Overwriting cookie '" + sKey + "' with the new value '" + aCookie.getValue () + "'");
    this.m_aCookies.put (sKey, aCookie);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeCookie (@Nullable final String sName)
  {
    if (this.m_aCookies != null)
      this.m_aCookies.remove (sName);
    return this;
  }

  /**
   * When specifying <code>false</code>, this method uses a special response
   * header to prevent certain browsers from MIME-sniffing a response away from
   * the declared content-type. When passing <code>true</code>, that header is
   * removed.
   *
   * @param bAllow
   *        Whether or not sniffing should be allowed (default is
   *        <code>true</code>).
   * @return this
   */
  @Nonnull
  public UnifiedResponse setAllowMimeSniffing (final boolean bAllow)
  {
    if (bAllow)
      this.m_aResponseHeaderMap.removeHeaders (CHTTPHeader.X_CONTENT_TYPE_OPTIONS);
    else
      this.m_aResponseHeaderMap.addHeader (CHTTPHeader.X_CONTENT_TYPE_OPTIONS, CHTTPHeader.VALUE_NOSNIFF);
    return this;
  }

  /**
   * Adds a response header to the response according to the passed name and
   * value.<br>
   * <b>ATTENTION:</b> You should only use the APIs that {@link UnifiedResponse}
   * directly offers. Use this method only in emergency and make sure you
   * validate the header field and allowed value!
   *
   * @param sName
   *        Name of the header. May neither be <code>null</code> nor empty.
   * @param sValue
   *        Value of the header. May neither be <code>null</code> nor empty.
   */
  public void addCustomResponseHeader (@Nonnull @Nonempty final String sName, @Nonnull @Nonempty final String sValue)
  {
    ValueEnforcer.notEmpty (sName, "Name");
    ValueEnforcer.notEmpty (sValue, "Value");

    this.m_aResponseHeaderMap.addHeader (sName, sValue);
  }

  /**
   * Removes the response headers matching the passed name from the
   * response.<br>
   * <b>ATTENTION:</b> You should only use the APIs that {@link UnifiedResponse}
   * directly offers. Use this method only in emergency and make sure you
   * validate the header field and allowed value!
   *
   * @param sName
   *        Name of the header to be removed. May neither be <code>null</code>
   *        nor empty.
   * @return the change state
   */
  @Nonnull
  public EChange removeCustomResponseHeaders (@Nonnull @Nonempty final String sName)
  {
    ValueEnforcer.notEmpty (sName, "Name");

    return this.m_aResponseHeaderMap.removeHeaders (sName);
  }

  /**
   * When specifying <code>false</code>, this method uses a special response
   * header to prevent certain browsers from MIME-sniffing a response away from
   * the declared content-type. When passing <code>true</code>, that header is
   * removed.
   *
   * @param nMaxAgeSeconds
   *        number of seconds, after the reception of the STS header field,
   *        during which the UA regards the host (from whom the message was
   *        received) as a Known HSTS Host.
   * @param bIncludeSubdomains
   *        if enabled, this signals the UA that the HSTS Policy applies to this
   *        HSTS Host as well as any sub-domains of the host's domain name.
   * @return this
   */
  @Nonnull
  public UnifiedResponse setStrictTransportSecurity (final int nMaxAgeSeconds, final boolean bIncludeSubdomains)
  {
    this.m_aResponseHeaderMap.addHeader (CHTTPHeader.STRICT_TRANSPORT_SECURITY,
                                         new CacheControlBuilder ().setMaxAgeSeconds (nMaxAgeSeconds)
                                                                   .getAsHTTPHeaderValue () +
                                                                                (bIncludeSubdomains ? ";" +
                                                                                                      CHTTPHeader.VALUE_INCLUDE_SUBDMOAINS
                                                                                                    : ""));
    return this;
  }

  private void _verifyCachingIntegrity ()
  {
    final boolean bIsHttp11 = this.m_eHTTPVersion == EHTTPVersion.HTTP_11;
    final boolean bExpires = this.m_aResponseHeaderMap.containsHeaders (CHTTPHeader.EXPIRES);
    final boolean bCacheControl = this.m_aCacheControl != null;
    final boolean bLastModified = this.m_aResponseHeaderMap.containsHeaders (CHTTPHeader.LAST_MODIFIED);
    final boolean bETag = this.m_aResponseHeaderMap.containsHeaders (CHTTPHeader.ETAG);

    if (bExpires && bIsHttp11)
      _info ("Expires found in HTTP 1.1 response: " + this.m_aResponseHeaderMap.getHeaderValues (CHTTPHeader.EXPIRES));

    if (bExpires && bCacheControl)
      _warn ("Expires and Cache-Control are both present. Cache-Control takes precedence!");

    if (bETag && !bIsHttp11)
      _warn ("Sending an ETag for HTTP version " + this.m_eHTTPVersion + " has no effect!");

    if (!bExpires && !bCacheControl)
    {
      if (bLastModified || bETag)
        _warn ("Validators (Last-Modified and ETag) have no effect if no Expires or Cache-Control is present");
      else
        _warn ("Response has no caching information at all");
    }

    if (this.m_aCacheControl != null)
    {
      if (!bIsHttp11)
        _warn ("Sending a Cache-Control header for HTTP version " +
               this.m_eHTTPVersion +
               " may have no or limited effect!");

      if (this.m_aCacheControl.isPrivate ())
      {
        if (this.m_aCacheControl.isPublic ())
          _warn ("Cache-Control cannot be private and public at the same time");

        if (this.m_aCacheControl.hasMaxAgeSeconds ())
          _warn ("Cache-Control cannot be private and have a max-age definition");

        if (this.m_aCacheControl.hasSharedMaxAgeSeconds ())
          _warn ("Cache-Control cannot be private and have a s-maxage definition");
      }
    }
  }

  @Nonnull
  @Nonempty
  private static String _getAsStringMimeTypes (@Nonnull final Map <IMimeType, QValue> aMap)
  {
    final StringBuilder aSB = new StringBuilder ("{");
    for (final Map.Entry <IMimeType, QValue> aEntry : ContainerHelper.getSortedByValue (aMap).entrySet ())
    {
      if (aSB.length () > 1)
        aSB.append (", ");
      aSB.append (aEntry.getKey ().getAsString ()).append ('=').append (aEntry.getValue ().getQuality ());
    }
    return aSB.append ("}").toString ();
  }

  @Nonnull
  @Nonempty
  private static String _getAsStringText (@Nonnull final Map <String, QValue> aMap)
  {
    final StringBuilder aSB = new StringBuilder ("{");
    for (final Map.Entry <String, QValue> aEntry : ContainerHelper.getSortedByValue (aMap).entrySet ())
    {
      if (aSB.length () > 1)
        aSB.append (", ");
      aSB.append (aEntry.getKey ()).append ('=').append (aEntry.getValue ().getQuality ());
    }
    return aSB.append ("}").toString ();
  }

  public void applyToResponse (@Nonnull final HttpServletResponse aHttpResponse) throws IOException
  {
    ValueEnforcer.notNull (aHttpResponse, "HttpResponse");

    // Apply all collected headers
    for (final Map.Entry <String, List <String>> aEntry : this.m_aResponseHeaderMap)
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

    final boolean bIsRedirect = this.m_sRedirectTargetUrl != null;
    final boolean bHasStatusCode = this.m_nStatusCode != CGlobal.ILLEGAL_UINT;

    if (bIsRedirect)
    {
      if (bHasStatusCode)
        _warn ("Ignoring provided status code because a redirect is specified!");
      if (!this.m_bAllowContentOnRedirect)
      {
        if (this.m_aCacheControl != null)
          _info ("Ignoring provided Cache-Control because a redirect is specified!");
        if (this.m_sContentDispositionFilename != null)
          _warn ("Ignoring provided Content-Dispostion filename because a redirect is specified!");
        if (this.m_aMimeType != null)
          _warn ("Ignoring provided MimeType because a redirect is specified!");
        if (this.m_aCharset != null)
          _warn ("Ignoring provided charset because a redirect is specified!");
        if (hasContent ())
          _warn ("Ignoring provided content because a redirect is specified!");
      }

      // Note: After using this method, the response should be
      // considered to be committed and should not be written to.
      aHttpResponse.sendRedirect (aHttpResponse.encodeRedirectURL (this.m_sRedirectTargetUrl));

      if (!this.m_bAllowContentOnRedirect)
        return;
    }

    if (bHasStatusCode)
    {
      if (bIsRedirect)
        _warn ("Overriding provided redirect because a status code is specified!");
      if (!this.m_bAllowContentOnStatusCode)
      {
        if (this.m_aCacheControl != null)
          _info ("Ignoring provided Cache-Control because a status code is specified!");
        if (this.m_sContentDispositionFilename != null)
          _warn ("Ignoring provided Content-Dispostion filename because a status code is specified!");
        if (this.m_aMimeType != null)
          _warn ("Ignoring provided MimeType because a status code is specified!");
        if (this.m_aCharset != null)
          _warn ("Ignoring provided charset because a status code is specified!");
        if (hasContent ())
          _warn ("Ignoring provided content because a status code is specified!");
      }
      if (this.m_nStatusCode == HttpServletResponse.SC_UNAUTHORIZED &&
          !this.m_aResponseHeaderMap.containsHeaders (CHTTPHeader.WWW_AUTHENTICATE))
        _warn ("Status code UNAUTHORIZED (401) is returned, but no " +
               CHTTPHeader.WWW_AUTHENTICATE +
               " HTTP response header is set!");

      // Content may be present so, sendError is not an option here!
      if (this.m_nStatusCode >= HttpServletResponse.SC_BAD_REQUEST && this.m_aContent == null)
      {
        // It's an error
        // Note: After using this method, the response should be considered
        // to be committed and should not be written to.
        aHttpResponse.sendError (this.m_nStatusCode);
      }
      else
      {
        // It's a status message "only"
        // Note: The container clears the buffer and sets the Location
        // header, preserving cookies and other headers.
        aHttpResponse.setStatus (this.m_nStatusCode);
      }

      if (!this.m_bAllowContentOnStatusCode)
        return;
    }

    // Verify only if is a response with content
    _verifyCachingIntegrity ();

    if (this.m_aCacheControl != null)
    {
      final String sCacheControlValue = this.m_aCacheControl.getAsHTTPHeaderValue ();
      if (StringHelper.hasText (sCacheControlValue))
        aHttpResponse.setHeader (CHTTPHeader.CACHE_CONTROL, sCacheControlValue);
      else
        _warn ("An empty Cache-Control was provided!");
    }

    if (this.m_sContentDispositionFilename != null)
    {
      final StringBuilder aSB = new StringBuilder ();
      if (this.m_aRequestBrowserInfo != null &&
          this.m_aRequestBrowserInfo.getBrowserType () == EBrowserType.IE &&
          this.m_aRequestBrowserInfo.getVersion ().getMajor () <= 8)
      {
        // Special case for IE <= 8
        final Charset aCharsetToUse = this.m_aCharset != null ? this.m_aCharset : CCharset.CHARSET_UTF_8_OBJ;
        aSB.append (this.m_eContentDispositionType.getID ())
           .append ("; filename=")
           .append (URLUtils.urlEncode (this.m_sContentDispositionFilename, aCharsetToUse));
      }
      else
      {
        // Filename needs to be surrounded with double quotes (single quotes
        // don't work).
        aSB.append (this.m_eContentDispositionType.getID ())
           .append ("; filename=\"")
           .append (this.m_sContentDispositionFilename)
           .append ("\"");

        // Check if we need an UTF-8 filename
        // http://stackoverflow.com/questions/93551/how-to-encode-the-filename-parameter-of-content-disposition-header-in-http/6745788#6745788
        final String sRFC5987Filename = RFC5987Encoder.getRFC5987EncodedUTF8 (this.m_sContentDispositionFilename);
        if (!sRFC5987Filename.equals (this.m_sContentDispositionFilename))
          aSB.append ("; filename*=UTF-8''").append (sRFC5987Filename);
      }

      aHttpResponse.setHeader (CHTTPHeader.CONTENT_DISPOSITION, aSB.toString ());
      if (this.m_aMimeType == null)
      {
        _warn ("Content-Disposition is specified but no MimeType is set. Using the default download MimeType.");
        aHttpResponse.setContentType (CMimeType.APPLICATION_FORCE_DOWNLOAD.getAsString ());
      }
    }

    // Mime type
    if (this.m_aMimeType != null)
    {
      final String sMimeType = this.m_aMimeType.getAsString ();

      // Check with request accept mime types
      final QValue aQuality = this.m_aAcceptMimeTypeList.getQValueOfMimeType (this.m_aMimeType);
      if (aQuality.isMinimumQuality ())
      {
        final Map <IMimeType, QValue> aBetterValues = this.m_aAcceptMimeTypeList.getAllQValuesGreaterThan (aQuality.getQuality ());
        _error ("MimeType '" +
                sMimeType +
                "' is not at all supported by the request. Allowed values are: " +
                _getAsStringMimeTypes (aBetterValues));
      }
      else
        if (aQuality.isLowValue ())
        {
          // This might bloat the logfile for text/css MIME types and therefore
          // only in the debug version
          if (GlobalDebug.isDebugMode ())
          {
            // Inform if the quality of the request is <= 50%!
            final Map <IMimeType, QValue> aBetterValues = this.m_aAcceptMimeTypeList.getAllQValuesGreaterThan (aQuality.getQuality ());
            if (!aBetterValues.isEmpty ())
              _warn ("MimeType '" +
                     sMimeType +
                     "' is not best supported by the request (" +
                     aQuality +
                     "). Better MimeTypes are: " +
                     _getAsStringMimeTypes (aBetterValues));
          }
        }

      aHttpResponse.setContentType (sMimeType);
    }
    else
      _warn ("No MimeType present");

    // Charset
    if (this.m_aCharset != null)
    {
      final String sCharset = this.m_aCharset.name ();
      if (this.m_aMimeType == null)
        _warn ("If no MimeType present, the client cannot get notified about the character encoding '" +
               sCharset +
               "'");

      // Check with request charset
      final QValue aQuality = this.m_aAcceptCharsetList.getQValueOfCharset (sCharset);
      if (aQuality.isMinimumQuality ())
      {
        final Map <String, QValue> aBetterValues = this.m_aAcceptCharsetList.getAllQValuesGreaterThan (aQuality.getQuality ());
        _error ("Character encoding '" +
                sCharset +
                "' is not at all supported by the request. Allowed values are: " +
                _getAsStringText (aBetterValues));
      }
      else
        if (aQuality.isLowValue ())
        {
          // Inform if the quality of the request is <= 50%!
          final Map <String, QValue> aBetterValues = this.m_aAcceptCharsetList.getAllQValuesGreaterThan (aQuality.getQuality ());
          if (!aBetterValues.isEmpty ())
            _warn ("Character encoding '" +
                   sCharset +
                   "' is not best supported by the request (" +
                   aQuality +
                   "). Better charsets are: " +
                   _getAsStringText (aBetterValues));
        }

      aHttpResponse.setCharacterEncoding (sCharset);
    }
    else
      if (this.m_aMimeType == null)
        _warn ("Also no character encoding present");
      else
        switch (this.m_aMimeType.getContentType ())
        {
          case TEXT:
          case MULTIPART:
            _warn ("A character encoding for MimeType '" + this.m_aMimeType.getAsString () + "' is appreciated.");
            break;
          default:
            // Do we need character encoding here as well???
            break;
        }

    // Add all cookies
    if (this.m_aCookies != null)
      for (final Cookie aCookie : this.m_aCookies.values ())
        aHttpResponse.addCookie (aCookie);

    CookieHelper.correctCookieHeaders (aHttpResponse);

    // Write the body to the response
    _applyContent (aHttpResponse);
  }

  @Nonnull
  protected HTTPHeaderMap getResponseHeaderMap ()
  {
    return this.m_aResponseHeaderMap;
  }

  private void _applyLengthChecks (final long nContentLength)
  {
    // Source:
    // http://joshua.perina.com/africa/gambia/fajara/post/internet-explorer-css-file-size-limit
    if (this.m_aMimeType != null &&
        this.m_aMimeType.equals (CMimeType.TEXT_CSS) &&
        nContentLength > MAX_CSS_KB_FOR_IE * CGlobal.BYTES_PER_KILOBYTE_LONG)
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
    if (this.m_aContent != null)
    {
      // We're having a fixed byte array of content
      final int nContentLength = this.m_aContent.length;

      // Determine the response stream type to use
      final EResponseStreamType eResponseStreamType = ResponseHelper.getBestSuitableOutputStreamType (this.m_aHttpRequest);
      if (eResponseStreamType.isUncompressed ())
      {
        // Must be set before the content itself arrives
        // Note: Set it only if the content is uncompressed, because we cannot
        // determine the length of the compressed text in advance without
        // computational overhead
        ResponseHelper.setContentLength (aHttpResponse, nContentLength);
      }

      // Don't emit empty content or content for HEAD method
      if (nContentLength > 0 && this.m_eHTTPMethod.isContentAllowed ())
      {
        // Create the correct stream
        final OutputStream aOS = ResponseHelper.getBestSuitableOutputStream (this.m_aHttpRequest, aHttpResponse);

        // Emit main content to stream
        aOS.write (this.m_aContent, 0, nContentLength);
        aOS.flush ();
        aOS.close ();

        _applyLengthChecks (nContentLength);
      }
      // Don't send 204, as this is most likely not handled correctly on the
      // client side
    }
    else
      if (this.m_aContentISP != null)
      {
        // We have a dynamic content input stream
        // -> no content length can be determined!
        final InputStream aContentIS = this.m_aContentISP.getInputStream ();
        if (aContentIS == null)
        {
          s_aLogger.error ("Failed to open input stream from " + this.m_aContentISP);

          // Handle it gracefully with a 404 and not with a 500
          aHttpResponse.setStatus (HttpServletResponse.SC_NOT_FOUND);
          if (this.m_aOnContentStreamConsumed != null)
          {
            this.m_aOnContentStreamConsumed.run (ESuccess.FAILURE);
          }
        }
        else
        {
          // Don't emit content for HEAD method
          if (this.m_eHTTPMethod.isContentAllowed ())
          {
            // We do have an input stream
            // -> copy it to the response
            final OutputStream aOS = aHttpResponse.getOutputStream ();
            final MutableLong aByteCount = new MutableLong ();

            if (StreamUtils.copyInputStreamToOutputStream (aContentIS, aOS, aByteCount).isSuccess ())
            {
              // Copying succeeded
              final long nBytesCopied = aByteCount.longValue ();

              // Don't apply additional Content-Length header after the resource
              // was streamed!
              _applyLengthChecks (nBytesCopied);
              if (this.m_aOnContentStreamConsumed != null)
              {
                this.m_aOnContentStreamConsumed.run (ESuccess.SUCCESS);
              }
            }
            else
            {
              // Copying failed -> this is a 500
              final boolean bResponseCommitted = aHttpResponse.isCommitted ();
              _error ("Copying from " +
                      this.m_aContentISP +
                      " failed after " +
                      aByteCount.longValue () +
                      " bytes! Response is committed: " +
                      bResponseCommitted);

              if (!bResponseCommitted)
              {
                aHttpResponse.sendError (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
              }
              if (this.m_aOnContentStreamConsumed != null)
              {
                this.m_aOnContentStreamConsumed.run (ESuccess.FAILURE);
              }
            }
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
