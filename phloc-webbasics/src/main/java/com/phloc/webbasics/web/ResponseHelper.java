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
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTWebDateUtils;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.webbasics.http.AcceptEncodingHandler;
import com.phloc.webbasics.http.AcceptEncodingList;
import com.phloc.webbasics.http.CHTTPHeader;
import com.phloc.webbasics.http.EHTTPVersion;

/**
 * Misc. helper methods on {@link HttpServletResponse} objects.<br>
 * Most method got deprecated by the activation of the {@link UnifiedResponse}
 * and the respective servlet and filter.
 * 
 * @author philip
 * @see com.phloc.webbasics.servlet.AbstractUnifiedResponseFilter
 * @see com.phloc.webbasics.servlet.AbstractUnifiedResponseServlet
 */
@Immutable
public final class ResponseHelper
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ResponseHelper s_aInstance = new ResponseHelper ();

  private ResponseHelper ()
  {}

  @Deprecated
  public static void modifyResponseForNoCaching (@Nonnull final IRequestWebScope aRequestScope)
  {
    modifyResponseForNoCaching (null, aRequestScope);
  }

  @Deprecated
  public static void modifyResponseForNoCaching (@Nonnull final HttpServletResponse aHttpResponse)
  {
    modifyResponseForNoCaching (null, aHttpResponse);
  }

  @Deprecated
  public static void modifyResponseForNoCaching (@Nullable final EHTTPVersion eVersion,
                                                 @Nonnull final IRequestWebScope aRequestScope)
  {
    modifyResponseForNoCaching (eVersion, aRequestScope.getResponse ());
  }

  @Deprecated
  public static void modifyResponseForNoCaching (@Nullable final EHTTPVersion eVersion,
                                                 @Nonnull final HttpServletResponse aHttpResponse)
  {
    if (eVersion == null || eVersion == EHTTPVersion.HTTP_10)
    {
      // Set to expire far in the past for HTTP/1.0.
      aHttpResponse.setHeader (CHTTPHeader.EXPIRES, ResponseHelperSettings.EXPIRES_NEVER_STRING);

      // Set standard HTTP/1.0 no-cache header.
      aHttpResponse.setHeader (CHTTPHeader.PRAGMA, "no-cache");
    }

    if (eVersion == null || eVersion == EHTTPVersion.HTTP_11)
    {
      // Set standard HTTP/1.1 no-cache headers.
      aHttpResponse.setHeader (CHTTPHeader.CACHE_CONTROL, "no-store, no-cache, must-revalidate");

      // Set IE extended HTTP/1.1 no-cache headers.
      if (false)
        aHttpResponse.addHeader (CHTTPHeader.CACHE_CONTROL, "post-check=0, pre-check=0");
    }
  }

  @Deprecated
  public static void modifyResponseContentDisposition (@Nonnull final IRequestWebScope aRequestScope,
                                                       @Nonnull final String sFilename)
  {
    modifyResponseContentDisposition (aRequestScope.getResponse (), sFilename);
  }

  @Deprecated
  public static void modifyResponseContentDisposition (@Nonnull final HttpServletResponse aHttpResponse,
                                                       @Nonnull final String sFilename)
  {
    // Ensure that a valid filename is used
    // -> Strip all paths and replace all invalid characters
    final String sFilenameToUse = FilenameHelper.getWithoutPath (FilenameHelper.getAsSecureValidFilename (sFilename));

    // Filename needs to be surrounded with double quotes (single quotes don't
    // work)
    // No URL encoding necessary.
    // Filename must be in ISO-8859-1
    // See http://greenbytes.de/tech/tc2231/
    aHttpResponse.setHeader (CHTTPHeader.CONTENT_DISPOSITION, "attachment; filename=\"" + sFilenameToUse + "\"");
  }

  @Deprecated
  public static void modifyResponseForDownload (@Nonnull final IRequestWebScope aRequestScope,
                                                @Nonnull final String sFilename)
  {
    modifyResponseForDownload (aRequestScope.getResponse (), sFilename);
  }

  @Deprecated
  public static void modifyResponseForDownload (@Nonnull final HttpServletResponse aHttpResponse,
                                                @Nonnull final String sFilename)
  {
    // Use application/force-download to force "save as" in client browser
    modifyResponseForDownload (aHttpResponse, sFilename, CMimeType.APPLICATION_FORCE_DOWNLOAD);
  }

  @Deprecated
  public static void modifyResponseForDownload (@Nonnull final IRequestWebScope aRequestScope,
                                                @Nonnull final String sFilename,
                                                @Nonnull final IMimeType aMimeType)
  {
    modifyResponseForDownload (aRequestScope.getResponse (), sFilename, aMimeType);
  }

  @Deprecated
  public static void modifyResponseForDownload (@Nonnull final HttpServletResponse aHttpResponse,
                                                @Nonnull final String sFilename,
                                                @Nonnull final IMimeType aMimeType)
  {
    modifyResponseForDownload (aHttpResponse, sFilename, aMimeType.getAsString ());
  }

  @Deprecated
  public static void modifyResponseForDownload (@Nonnull final IRequestWebScope aRequestScope,
                                                @Nonnull final String sFilename,
                                                @Nonnull final String sMimeType)
  {
    modifyResponseForDownload (aRequestScope.getResponse (), sFilename, sMimeType);
  }

  @Deprecated
  public static void modifyResponseForDownload (@Nonnull final HttpServletResponse aHttpResponse,
                                                @Nonnull final String sFilename,
                                                @Nonnull final String sMimeType)
  {
    modifyResponseForNoCaching (aHttpResponse);
    modifyResponseContentDisposition (aHttpResponse, sFilename);
    aHttpResponse.setContentType (sMimeType);
  }

  @Deprecated
  public static void modifyResponseForExpiration (@Nonnull final IRequestWebScope aRequestScope,
                                                  @Nonnegative final int nSeconds)
  {
    modifyResponseForExpiration (aRequestScope.getResponse (), nSeconds);
  }

  @Deprecated
  public static void modifyResponseForExpiration (@Nonnull final HttpServletResponse aHttpResponse,
                                                  @Nonnegative final int nSeconds)
  {
    // only use EXPIRES
    // See http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.3
    // See http://www.mnot.net/cache_docs/#CACHE-CONTROL
    aHttpResponse.setHeader (CHTTPHeader.EXPIRES,
                             PDTWebDateUtils.getAsStringRFC822 (PDTFactory.getCurrentLocalDateTime ()
                                                                          .plusSeconds (nSeconds)));
  }

  @Deprecated
  public static void modifyResponseForMovedPermanently (@Nonnull final IRequestWebScope aRequestScope,
                                                        @Nonnull final String sURL)
  {
    modifyResponseForMovedPermanently (aRequestScope.getResponse (), sURL);
  }

  @Deprecated
  public static void modifyResponseForMovedPermanently (@Nonnull final HttpServletResponse aHttpResponse,
                                                        @Nonnull final String sURL)
  {
    aHttpResponse.setStatus (HttpServletResponse.SC_MOVED_PERMANENTLY);
    aHttpResponse.setHeader (CHTTPHeader.LOCATION, sURL);
  }

  /**
   * Get the best suitable output stream for the given combination of request
   * and response. If the request supports gzip, the result is a
   * {@link GZIPOutputStream}, if the request supports deflate or compress, the
   * result will be a {@link ZipOutputStream}. If none of that matches, the
   * regular response output stream is used
   * 
   * @param aRequestScope
   *        Request and response
   * @return The best matching output stream
   * @throws IOException
   */
  @Nonnull
  public static OutputStream getBestSuitableOutputStream (@Nonnull final IRequestWebScope aRequestScope) throws IOException
  {
    return getBestSuitableOutputStream (aRequestScope.getRequest (), aRequestScope.getResponse ());
  }

  /**
   * Get the best suitable output stream for the given combination of request
   * and response. If the request supports gzip, the result is a
   * {@link GZIPOutputStream}, if the request supports deflate or compress, the
   * result will be a {@link ZipOutputStream}. If none of that matches, the
   * regular response output stream is used
   * 
   * @param aHttpRequest
   *        request
   * @param aHttpResponse
   *        Response
   * @return The best matching output stream
   * @throws IOException
   */
  @Nonnull
  public static OutputStream getBestSuitableOutputStream (@Nonnull final HttpServletRequest aHttpRequest,
                                                          @Nonnull final HttpServletResponse aHttpResponse) throws IOException
  {
    OutputStream aOS = aHttpResponse.getOutputStream ();

    if (ResponseHelperSettings.isResponseCompressionEnabled ())
    {
      // Can we get resource transfer working with GZIP or deflate?
      final AcceptEncodingList aAcceptEncodings = AcceptEncodingHandler.getAcceptEncodings (aHttpRequest);

      // Inform caches that responses may vary according to Accept-Encoding
      aHttpResponse.setHeader (CHTTPHeader.VARY, CHTTPHeader.ACCEPT_ENCODING);
      // ResponseHelper used
      aHttpResponse.setHeader ("X-P", "RH");

      final String sGZipEncoding = aAcceptEncodings.getUsedGZIPEncoding ();
      if (sGZipEncoding != null && ResponseHelperSettings.isResponseGzipEnabled ())
      {
        // Use GZip
        aHttpResponse.setHeader (CHTTPHeader.CONTENT_ENCODING, sGZipEncoding);
        aOS = new GZIPOutputStream (aHttpResponse.getOutputStream ());
      }
      else
      {
        final String sDeflateEncoding = aAcceptEncodings.getUsedDeflateEncoding ();
        if (sDeflateEncoding != null && ResponseHelperSettings.isResponseDeflateEnabled ())
        {
          // Use Deflate
          aHttpResponse.setHeader (CHTTPHeader.CONTENT_ENCODING, sDeflateEncoding);
          aOS = new ZipOutputStream (aHttpResponse.getOutputStream ());
          // A dummy ZIP entry is required!
          ((ZipOutputStream) aOS).putNextEntry (new ZipEntry ("dummy name"));
        }
      }
    }

    return aOS;
  }

  public static void finishReponseOutputStream (@Nonnull final OutputStream aOS) throws IOException
  {
    if (aOS == null)
      throw new NullPointerException ("outputStream");

    aOS.flush ();
    aOS.close ();
  }

  /**
   * Write the one and only response bytes. This gets the destination output
   * stream, writes the bytes there and then closes the response output stream
   * so that no further emitting is possible!
   * 
   * @param aRequestScope
   *        request scope
   * @param aBytes
   *        bytes to write
   * @param aMimeType
   *        MIME type to use
   * @param sContentCharset
   *        Optional charset to use
   * @throws IOException
   *         In case of an error
   */
  @Deprecated
  public static void writeResponse (@Nonnull final IRequestWebScope aRequestScope,
                                    @Nonnull final byte [] aBytes,
                                    @Nonnull final IMimeType aMimeType,
                                    @Nullable final String sContentCharset) throws IOException
  {
    writeResponse (aRequestScope.getRequest (), aRequestScope.getResponse (), aBytes, aMimeType, sContentCharset);
  }

  /**
   * Write the one and only response bytes. This gets the destination output
   * stream, writes the bytes there and then closes the response output stream
   * so that no further emitting is possible!
   * 
   * @param aHttpRequest
   *        Source request
   * @param aHttpResponse
   *        Destination response
   * @param aBytes
   *        bytes to write
   * @param aMimeType
   *        MIME type to use
   * @param sContentCharset
   *        Optional charset to use
   * @throws IOException
   *         In case of an error
   */
  @Deprecated
  public static void writeResponse (@Nonnull final HttpServletRequest aHttpRequest,
                                    @Nonnull final HttpServletResponse aHttpResponse,
                                    @Nonnull final byte [] aBytes,
                                    @Nonnull final IMimeType aMimeType,
                                    @Nullable final String sContentCharset) throws IOException
  {
    if (sContentCharset != null)
    {
      aHttpResponse.setContentType (aMimeType.getAsStringWithEncoding (sContentCharset));
      aHttpResponse.setCharacterEncoding (sContentCharset);
    }
    else
    {
      // No charset defined
      aHttpResponse.setContentType (aMimeType.getAsString ());
    }
    final OutputStream aOS = getBestSuitableOutputStream (aHttpRequest, aHttpResponse);

    // Faster than using a PrintWriter!
    aOS.write (aBytes);

    // And close the OS
    finishReponseOutputStream (aOS);
  }

  /**
   * Write a complete response string message.
   * 
   * @param aRequestScope
   *        Base request scope
   * @param sTextContent
   *        The text content to write. May not be <code>null</code>.
   * @param aMimeType
   *        The MIME type to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to use for converting the String into bytes. May not be
   *        <code>null</code>.
   * @throws IOException
   *         If something goes wrong
   */
  @Deprecated
  public static void writeTextResponse (@Nonnull final IRequestWebScope aRequestScope,
                                        @Nonnull final String sTextContent,
                                        @Nonnull final IMimeType aMimeType,
                                        @Nonnull final Charset aCharset) throws IOException
  {
    writeResponse (aRequestScope.getRequest (),
                   aRequestScope.getResponse (),
                   CharsetManager.getAsBytes (sTextContent, aCharset),
                   aMimeType,
                   aCharset.name ());
  }

  /**
   * Write a complete response string message.
   * 
   * @param aHttpRequest
   *        Base request. Is used to determined, whether GZIP can be used for
   *        the output stream. May not be <code>null</code>.
   * @param aHttpResponse
   *        Base response. May not be <code>null</code>.
   * @param sTextContent
   *        The text content to write. May not be <code>null</code>.
   * @param aMimeType
   *        The MIME type to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to use for converting the String into bytes. May not be
   *        <code>null</code>.
   * @throws IOException
   *         If something goes wrong
   */
  @Deprecated
  public static void writeTextResponse (@Nonnull final HttpServletRequest aHttpRequest,
                                        @Nonnull final HttpServletResponse aHttpResponse,
                                        @Nonnull final String sTextContent,
                                        @Nonnull final IMimeType aMimeType,
                                        @Nonnull final Charset aCharset) throws IOException
  {
    writeResponse (aHttpRequest,
                   aHttpResponse,
                   CharsetManager.getAsBytes (sTextContent, aCharset),
                   aMimeType,
                   aCharset.name ());
  }

  /**
   * Write a complete response string message as XHTML.
   * 
   * @param aRequestScope
   *        Base request scope
   * @param sTextContent
   *        The text content to write. May not be <code>null</code>.
   * @param aCharset
   *        The charset to use for converting the String into bytes. May not be
   *        <code>null</code>.
   * @throws IOException
   *         If something goes wrong
   * @see #writeTextResponse(HttpServletRequest, HttpServletResponse, String,
   *      IMimeType, Charset)
   */
  @Deprecated
  public static void writeTextXHTMLResponse (@Nonnull final IRequestWebScope aRequestScope,
                                             @Nonnull final String sTextContent,
                                             @Nonnull final Charset aCharset) throws IOException
  {
    writeTextXHTMLResponse (aRequestScope.getRequest (), aRequestScope.getResponse (), sTextContent, aCharset);
  }

  /**
   * Write a complete response string message as XHTML.
   * 
   * @param aHttpRequest
   *        Base request. Is used to determined, whether GZIP can be used for
   *        the output stream. May not be <code>null</code>.
   * @param aHttpResponse
   *        Base response. May not be <code>null</code>.
   * @param sTextContent
   *        The text content to write. May not be <code>null</code>.
   * @param aCharset
   *        The charset to use for converting the String into bytes. May not be
   *        <code>null</code>.
   * @throws IOException
   *         If something goes wrong
   * @see #writeTextResponse(HttpServletRequest, HttpServletResponse, String,
   *      IMimeType, Charset)
   */
  @Deprecated
  public static void writeTextXHTMLResponse (@Nonnull final HttpServletRequest aHttpRequest,
                                             @Nonnull final HttpServletResponse aHttpResponse,
                                             @Nonnull final String sTextContent,
                                             @Nonnull final Charset aCharset) throws IOException
  {
    writeTextResponse (aHttpRequest, aHttpResponse, sTextContent, CMimeType.TEXT_HTML, aCharset);
  }

  /**
   * Write a complete response string message as XML.
   * 
   * @param aRequestScope
   *        Base request scope
   * @param sTextContent
   *        The text content to write. May not be <code>null</code>.
   * @param aCharset
   *        The charset to use for converting the String into bytes. May not be
   *        <code>null</code>.
   * @throws IOException
   *         If something goes wrong
   * @see #writeTextResponse(HttpServletRequest, HttpServletResponse, String,
   *      IMimeType, Charset)
   */
  @Deprecated
  public static void writeTextXMLResponse (@Nonnull final IRequestWebScope aRequestScope,
                                           @Nonnull final String sTextContent,
                                           @Nonnull final Charset aCharset) throws IOException
  {
    writeTextXMLResponse (aRequestScope.getRequest (), aRequestScope.getResponse (), sTextContent, aCharset);
  }

  /**
   * Write a complete response string message as XML.
   * 
   * @param aHttpRequest
   *        Base request. Is used to determined, whether GZIP can be used for
   *        the output stream. May not be <code>null</code>.
   * @param aHttpResponse
   *        Base response. May not be <code>null</code>.
   * @param sTextContent
   *        The text content to write. May not be <code>null</code>.
   * @param aCharset
   *        The charset to use for converting the String into bytes. May not be
   *        <code>null</code>.
   * @throws IOException
   *         If something goes wrong
   * @see #writeTextResponse(HttpServletRequest, HttpServletResponse, String,
   *      IMimeType, Charset)
   */
  @Deprecated
  public static void writeTextXMLResponse (@Nonnull final HttpServletRequest aHttpRequest,
                                           @Nonnull final HttpServletResponse aHttpResponse,
                                           @Nonnull final String sTextContent,
                                           @Nonnull final Charset aCharset) throws IOException
  {
    writeTextResponse (aHttpRequest, aHttpResponse, sTextContent, CMimeType.TEXT_XML, aCharset);
  }

  @Deprecated
  public static void writeEmptyResponse (@Nonnull final IRequestWebScope aRequestScope) throws IOException
  {
    writeEmptyResponse (aRequestScope.getRequest (), aRequestScope.getResponse ());
  }

  @Deprecated
  public static void writeEmptyResponse (@Nonnull final HttpServletRequest aHttpRequest,
                                         @Nonnull final HttpServletResponse aHttpResponse) throws IOException
  {
    writeResponse (aHttpRequest, aHttpResponse, new byte [0], CMimeType.TEXT_PLAIN, CCharset.CHARSET_ISO_8859_1);
  }

  public static boolean isEmptyStatusCode (final int nSC)
  {
    // 301 || 302 || 304
    return nSC == HttpServletResponse.SC_MOVED_PERMANENTLY ||
           nSC == HttpServletResponse.SC_MOVED_TEMPORARILY ||
           nSC == HttpServletResponse.SC_NOT_MODIFIED;
  }
}
