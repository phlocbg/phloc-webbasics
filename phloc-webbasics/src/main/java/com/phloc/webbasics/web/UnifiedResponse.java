package com.phloc.webbasics.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
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

public class UnifiedResponse
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UnifiedResponse.class);

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

  public UnifiedResponse (@Nullable final EHTTPVersion eHttpVersion)
  {
    m_eHttpVersion = eHttpVersion;
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
      s_aLogger.warn ("UnifiedResponse: Overwriting charset from " + m_aCharset + " to " + aCharset);
    m_aCharset = aCharset;
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
      s_aLogger.warn ("UnifiedResponse: Overwriting MimeType from " + m_aMimeType + " to " + aMimeType);
    m_aMimeType = aMimeType;
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
   * Set an empty response content.
   * 
   * @return this
   */
  @Nonnull
  public UnifiedResponse setEmptyContent ()
  {
    return setContent (new byte [0]);
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
      s_aLogger.warn ("UnifiedResponse: Overwriting content with byte array!");
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
      s_aLogger.warn ("UnifiedResponse: Overwriting content with content provider!");
    m_aContent = null;
    m_aContentISP = aISP;
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
      s_aLogger.warn ("UnifiedResponse: Content-Dispostion filename was modified from '" +
                      sFilename +
                      "' to '" +
                      sFilenameToUse +
                      "'");

    // Check if encoding as ISO-8859-1 is possible
    if (m_aContentDispositionEncoder == null)
      m_aContentDispositionEncoder = CCharset.CHARSET_ISO_8859_1_OBJ.newEncoder ();
    if (!m_aContentDispositionEncoder.canEncode (sFilenameToUse))
      s_aLogger.error ("UnifiedResponse: Content-Dispostion  filename '" +
                       sFilenameToUse +
                       "' cannot be encoded to ISO-8859-1!");

    // Are we overwriting?
    if (m_sContentDispositionFilename != null)
      s_aLogger.warn ("UnifiedResponse: Overwriting Content-Dispostion filename from '" +
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

  @Nonnull
  public UnifiedResponse setCacheControl (@Nonnull final CacheControlBuilder aCacheControl)
  {
    if (aCacheControl == null)
      throw new NullPointerException ("cacheControl");
    if (hasContent ())
      s_aLogger.warn ("UnifiedResponse: Overwriting Cache-Control data");
    m_aCacheControl = aCacheControl;
    return this;
  }

  private void _verifyCachingIntegrity ()
  {
    final boolean bIsHttp11 = isHttp11 ();
    final boolean bExpires = m_aHeaderMap.containsHeaders (CHTTPHeader.EXPIRES);
    final boolean bCacheControl = m_aHeaderMap.containsHeaders (CHTTPHeader.CACHE_CONTROL);
    final boolean bLastModified = m_aHeaderMap.containsHeaders (CHTTPHeader.LAST_MODIFIED);
    final boolean bETag = m_aHeaderMap.containsHeaders (CHTTPHeader.ETAG);

    if (bExpires && bCacheControl)
      s_aLogger.warn ("UnifiedResponse: Expires and Cache-Control are both present. Cache-Control takes precedence!");

    if (bETag && !bIsHttp11)
      s_aLogger.warn ("UnifiedResponse: Sending an ETag for HTTP version " + m_eHttpVersion + " has no effect!");

    if ((bLastModified || bETag) && !bExpires && !bCacheControl)
      s_aLogger.warn ("UnifiedResponse: Validators (Last-Modified and ETag) have no effect if no Expires or Cache-Control is present");

    if (m_aCacheControl != null)
    {
      if (!bIsHttp11)
        s_aLogger.warn ("UnifiedResponse: Sending a Cache-Control header for HTTP version " +
                        m_eHttpVersion +
                        " may have no or limited effect!");

      if (m_aCacheControl.isPrivate () && m_aCacheControl.isPublic ())
        s_aLogger.warn ("UnifiedResponse: Cache-Control cannot be private and public at the same time");

      if (m_aCacheControl.isNoStore ())
      {
        if (m_aCacheControl.isNoCache ())
          s_aLogger.info ("UnifiedResponse: Cache-Control no-store is enabled. So no-cache does not need to be enabled");

        if (m_aCacheControl.isMustRevalidate ())
          s_aLogger.info ("UnifiedResponse: Cache-Control no-store is enabled. So must-revalidate does not need to be enabled");

        if (m_aCacheControl.isProxyRevalidate ())
          s_aLogger.info ("UnifiedResponse: Cache-Control no-store is enabled. So proxy-revalidate does not need to be enabled");
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
      aHttpResponse.setHeader (CHTTPHeader.CACHE_CONTROL, m_aCacheControl.getAsHTTPHeaderValue ());

    if (m_sContentDispositionFilename != null)
    {
      // Filename needs to be surrounded with double quotes (single quotes don't
      // work).
      aHttpResponse.setHeader (CHTTPHeader.CONTENT_DISPOSITION, "attachment; filename=\"" +
                                                                m_sContentDispositionFilename +
                                                                "\"");
      if (m_aMimeType == null)
      {
        s_aLogger.warn ("UnifiedResponse: Content-Disposition is specified but no MimeType is set. Using the default download MimeType.");
        aHttpResponse.setContentType (CMimeType.APPLICATION_FORCE_DOWNLOAD.getAsString ());
      }
    }

    // Mime type
    if (m_aMimeType != null)
      aHttpResponse.setContentType (m_aMimeType.getAsString ());
    else
      s_aLogger.warn ("UnifiedResponse: No MimeType present");

    // Charset
    if (m_aCharset != null)
    {
      if (m_aMimeType == null)
        s_aLogger.warn ("UnifiedResponse: If no MimeType present, the client cannot get notified about the character encoding '" +
                        m_aCharset.name () +
                        "'");
      aHttpResponse.setCharacterEncoding (m_aCharset.name ());
    }
    else
      if (m_aMimeType == null)
        s_aLogger.warn ("UnifiedResponse: Also no character encoding present");
      else
        switch (m_aMimeType.getContentType ())
        {
          case TEXT:
          case MULTIPART:
            s_aLogger.warn ("UnifiedResponse: A character encoding for MimeType '" +
                            m_aMimeType.getAsString () +
                            "' is appreciated.");
            break;
          default:
            // Do we need character encoding here as well???
            break;
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
            s_aLogger.error ("UnifiedResponse: Copying from " +
                             m_aContentISP +
                             " failed after " +
                             aByteCount.longValue () +
                             " bytes!");
            aHttpResponse.resetBuffer ();
            aHttpResponse.sendError (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          }
        }
      }
      else
      {
        // Set status 204 - no content
        aHttpResponse.setStatus (HttpServletResponse.SC_NO_CONTENT);
        s_aLogger.warn ("UnifiedResponse: No content present for the response");
      }
  }
}
