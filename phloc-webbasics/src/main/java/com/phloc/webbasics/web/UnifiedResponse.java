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
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mutable.MutableLong;
import com.phloc.commons.string.StringHelper;
import com.phloc.webbasics.http.CHTTPHeader;
import com.phloc.webbasics.http.CacheControlBuilder;
import com.phloc.webbasics.http.EHTTPVersion;
import com.phloc.webbasics.http.HTTPHeaderMap;

/**
 * This class tries to encapsulate all things required to build a HTTP response.
 * It offer warnings and consistency checks if something is missing.
 * 
 * @author philip
 */
public class UnifiedResponse
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UnifiedResponse.class);
  private static final String LOG_PREFIX = "UnifiedResponse: ";

  private final EHTTPVersion m_eHttpVersion;

  // Main data
  private Charset m_aCharset;
  private IMimeType m_aMimeType;
  private byte [] m_aContent;
  private IInputStreamProvider m_aContentISP;
  private String m_sContentDispositionFilename;
  private CharsetEncoder m_aContentDispositionEncoder;
  private CacheControlBuilder m_aCacheControl;
  private final HTTPHeaderMap m_aHeaderMap = new HTTPHeaderMap ();
  private int m_nErrorCode = CGlobal.ILLEGAL_UINT;

  public UnifiedResponse (@Nullable final EHTTPVersion eHttpVersion)
  {
    m_eHttpVersion = eHttpVersion;
  }

  private static void _info (@Nonnull final String sMsg)
  {
    s_aLogger.info (LOG_PREFIX + sMsg);
  }

  private static void _warn (@Nonnull final String sMsg)
  {
    s_aLogger.warn (LOG_PREFIX + sMsg);
  }

  private static void _error (@Nonnull final String sMsg)
  {
    s_aLogger.error (LOG_PREFIX + sMsg);
  }

  public boolean isHttp10 ()
  {
    return EHTTPVersion.HTTP_10.equals (m_eHttpVersion);
  }

  public boolean isHttp11 ()
  {
    return EHTTPVersion.HTTP_11.equals (m_eHttpVersion);
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
      _warn ("Overwriting charset from " + m_aCharset + " to " + aCharset);
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
      _warn ("Overwriting MimeType from " + m_aMimeType + " to " + aMimeType);
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
    return m_aContent != null || m_aContentISP != null;
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
      _warn ("Overwriting content with byte array!");
    m_aContent = aContent;
    m_aContentISP = null;
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
      _warn ("Overwriting content with content provider!");
    m_aContent = null;
    m_aContentISP = aISP;
    return this;
  }

  @Nonnull
  public UnifiedResponse removeContent ()
  {
    m_aContent = null;
    m_aContentISP = null;
    return this;
  }

  @Nonnull
  public UnifiedResponse setExpires (@Nonnull final LocalDateTime aLDT)
  {
    m_aHeaderMap.setDateHeader (CHTTPHeader.EXPIRES, aLDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse setExpires (@Nonnull final DateTime aDT)
  {
    m_aHeaderMap.setDateHeader (CHTTPHeader.EXPIRES, aDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeExpires ()
  {
    m_aHeaderMap.removeHeaders (CHTTPHeader.EXPIRES);
    return this;
  }

  @Nonnull
  public UnifiedResponse setLastModified (@Nonnull final LocalDateTime aLDT)
  {
    m_aHeaderMap.setDateHeader (CHTTPHeader.LAST_MODIFIED, aLDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse setLastModified (@Nonnull final DateTime aDT)
  {
    m_aHeaderMap.setDateHeader (CHTTPHeader.LAST_MODIFIED, aDT);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeLastModified ()
  {
    m_aHeaderMap.removeHeaders (CHTTPHeader.LAST_MODIFIED);
    return this;
  }

  @Nonnull
  public UnifiedResponse setETag (@Nonnull @Nonempty final String sETag)
  {
    if (StringHelper.hasNoText (sETag))
      throw new IllegalArgumentException ("An empty ETag is not allowed!");
    m_aHeaderMap.setHeader (CHTTPHeader.ETAG, sETag);
    return this;
  }

  @Nonnull
  public UnifiedResponse removeETag ()
  {
    m_aHeaderMap.removeHeaders (CHTTPHeader.ETAG);
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
      _warn ("Overwriting Content-Dispostion filename from '" +
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

  @Nonnull
  public UnifiedResponse setCacheControl (@Nonnull final CacheControlBuilder aCacheControl)
  {
    if (aCacheControl == null)
      throw new NullPointerException ("cacheControl");
    if (m_aCacheControl != null)
      _warn ("Overwriting Cache-Control data");
    m_aCacheControl = aCacheControl.getClone ();
    return this;
  }

  @Nullable
  @ReturnsMutableCopy
  public CacheControlBuilder getCacheControl ()
  {
    return m_aCacheControl == null ? null : m_aCacheControl.getClone ();
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
    if (m_eHttpVersion == null || m_eHttpVersion == EHTTPVersion.HTTP_10)
    {
      // Set to expire far in the past for HTTP/1.0.
      m_aHeaderMap.setHeader (CHTTPHeader.EXPIRES, ResponseHelper.EXPIRES_NEVER_STRING);

      // Set standard HTTP/1.0 no-cache header.
      m_aHeaderMap.setHeader (CHTTPHeader.PRAGMA, "no-cache");
    }

    if (m_eHttpVersion == null || m_eHttpVersion == EHTTPVersion.HTTP_11)
    {
      // No store must be enough
      final CacheControlBuilder aCacheControlBuilder = new CacheControlBuilder ().setNoStore (true);

      // Set IE extended HTTP/1.1 no-cache headers.
      // http://aspnetresources.com/blog/cache_control_extensions
      aCacheControlBuilder.addExtension ("post-check=0").addExtension ("pre-check=0");

      setCacheControl (aCacheControlBuilder);
    }
    return this;
  }

  @Nonnull
  public UnifiedResponse setError (@Nonnegative final int nErrorCode)
  {
    if (nErrorCode < HttpServletResponse.SC_BAD_REQUEST)
      throw new IllegalArgumentException ("Status " + nErrorCode + " is not an error!");
    if (m_nErrorCode != CGlobal.ILLEGAL_UINT)
      _warn ("Overwriting error code " + m_nErrorCode + " with " + nErrorCode);
    m_nErrorCode = nErrorCode;
    return this;
  }

  private void _verifyCachingIntegrity ()
  {
    final boolean bIsHttp11 = isHttp11 ();
    final boolean bExpires = m_aHeaderMap.containsHeaders (CHTTPHeader.EXPIRES);
    final boolean bCacheControl = m_aCacheControl != null;
    final boolean bLastModified = m_aHeaderMap.containsHeaders (CHTTPHeader.LAST_MODIFIED);
    final boolean bETag = m_aHeaderMap.containsHeaders (CHTTPHeader.ETAG);

    if (bExpires && bCacheControl)
      _warn ("Expires and Cache-Control are both present. Cache-Control takes precedence!");

    if (bETag && !bIsHttp11)
      _warn ("Sending an ETag for HTTP version " + m_eHttpVersion + " has no effect!");

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
        _warn ("Sending a Cache-Control header for HTTP version " + m_eHttpVersion + " may have no or limited effect!");

      if (m_aCacheControl.isPrivate () && m_aCacheControl.isPublic ())
        _warn ("Cache-Control cannot be private and public at the same time");

      if (m_aCacheControl.isNoStore ())
      {
        if (m_aCacheControl.isNoCache ())
          _info ("Cache-Control no-store is enabled. So no-cache does not need to be enabled");

        if (m_aCacheControl.isMustRevalidate ())
          _info ("Cache-Control no-store is enabled. So must-revalidate does not need to be enabled");

        if (m_aCacheControl.isProxyRevalidate ())
          _info ("Cache-Control no-store is enabled. So proxy-revalidate does not need to be enabled");
      }
      else
        if (m_aCacheControl.isNoCache ())
        {
          if (m_aCacheControl.isMustRevalidate ())
            _info ("Cache-Control no-cache is enabled. So must-revalidate does not need to be enabled");

          if (m_aCacheControl.isProxyRevalidate ())
            _info ("Cache-Control no-cache is enabled. So proxy-revalidate does not need to be enabled");
        }
    }
  }

  public void applyToResponse (@Nonnull final HttpServletResponse aHttpResponse) throws IOException
  {
    if (aHttpResponse == null)
      throw new NullPointerException ("httpResponse");

    _verifyCachingIntegrity ();

    // Apply all collected headers
    for (final Map.Entry <String, List <String>> aEntry : m_aHeaderMap.getAllHeaders ().entrySet ())
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
      // Filename needs to be surrounded with double quotes (single quotes don't
      // work).
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
      aHttpResponse.setContentType (m_aMimeType.getAsString ());
    else
      _warn ("No MimeType present");

    // Charset
    if (m_aCharset != null)
    {
      if (m_aMimeType == null)
        _warn ("If no MimeType present, the client cannot get notified about the character encoding '" +
               m_aCharset.name () +
               "'");
      aHttpResponse.setCharacterEncoding (m_aCharset.name ());
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

    if (m_nErrorCode != CGlobal.ILLEGAL_UINT)
    {
      // Send the error code, and allow for content (e.g. 404)
      aHttpResponse.sendError (m_nErrorCode);
    }

    // Determine content length
    _applyContent (aHttpResponse);
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
        // Emit main content to stream
        final OutputStream aOS = aHttpResponse.getOutputStream ();
        aOS.write (m_aContent);
        aOS.flush ();
        aOS.close ();
      }
      else
      {
        // Set status 204 - no content
        aHttpResponse.setStatus (HttpServletResponse.SC_NO_CONTENT);
      }
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
          // We do have an input stream
          // -> copy it to the response
          final OutputStream aOS = aHttpResponse.getOutputStream ();
          final MutableLong aByteCount = new MutableLong ();
          if (StreamUtils.copyInputStreamToOutputStream (aContentIS, aOS, aByteCount).isSuccess ())
          {
            // Copying succeeded
            if (aByteCount.longValue () > 0)
            {
              // We had at least one content byte
              aOS.flush ();
              aOS.close ();
            }
            else
            {
              // Set status 204 - no content
              aOS.close ();
              aHttpResponse.setStatus (HttpServletResponse.SC_NO_CONTENT);
            }
          }
          else
          {
            // Copying failed -> this is a 500
            _error ("Copying from " + m_aContentISP + " failed after " + aByteCount.longValue () + " bytes!");
            aHttpResponse.resetBuffer ();
            aHttpResponse.sendError (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          }
        }
      }
      else
      {
        // Set status 204 - no content
        aHttpResponse.setStatus (HttpServletResponse.SC_NO_CONTENT);
        _warn ("No content present for the response");
      }
  }
}
