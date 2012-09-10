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
package com.phloc.webbasics.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.EMimeContentType;
import com.phloc.commons.mime.MimeTypeDeterminator;
import com.phloc.commons.mutable.MutableLong;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.IStatisticsHandlerSize;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.CHTMLCharset;
import com.phloc.webbasics.web.ResponseHelper;
import com.phloc.webbasics.web.ResponseHelperSettings;

/**
 * Simple servlet to forward requests to pages normally not visible to the
 * Web-Server. It simply reads the content of a file and passes it to an output
 * stream.
 * 
 * @author philip
 */
public abstract class AbstractStreamServlet extends AbstractObjectDeliveryServlet
{
  private static final int MAX_CSS_KB_FOR_IE = 288;
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractStreamServlet.class);
  private final IStatisticsHandlerCounter m_aStatsRequests = StatisticsManager.getCounterHandler (getClass ().getName () +
                                                                                                  "$requests");
  private final IStatisticsHandlerSize m_aStatsDeliveredBytes = StatisticsManager.getSizeHandler (getClass ().getName () +
                                                                                                  "$bytes");
  private final IStatisticsHandlerKeyedCounter m_aStatsSuccess = StatisticsManager.getKeyedCounterHandler (getClass ().getName () +
                                                                                                           "$success");
  private final IStatisticsHandlerKeyedCounter m_aStatsCached = StatisticsManager.getKeyedCounterHandler (getClass ().getName () +
                                                                                                          "$cached");
  private final IStatisticsHandlerKeyedCounter m_aStatsNotFound = StatisticsManager.getKeyedCounterHandler (getClass ().getName () +
                                                                                                            "$notfound");
  private final IStatisticsHandlerKeyedCounter m_aStatsIOError = StatisticsManager.getKeyedCounterHandler (getClass ().getName () +
                                                                                                           "$ioerror");
  private final IStatisticsHandlerKeyedCounter m_aStatsMIMEType = StatisticsManager.getKeyedCounterHandler (getClass ().getName () +
                                                                                                            "$mimetype");

  /**
   * Resolve the resource specified by the given file name.
   * 
   * @param aHttpRequest
   *        The HTTP request to get request values from. Never <code>null</code>
   *        .
   * @param sFilename
   *        The name of the file to be resolved.
   * @return The non-<code>null</code> resource provider.
   */
  @Nonnull
  protected abstract IReadableResource getResource (@Nonnull final HttpServletRequest aHttpRequest,
                                                    @Nonnull String sFilename);

  /**
   * Modify the response headers for caching object.
   * 
   * @param aHttpRequest
   *        The HTTP request to get header values from. Never <code>null</code>.
   * @param aHttpResponse
   *        The HTTP response object to modify. Never <code>null</code>.
   * @return <code>true</code> if the browser cached item should be used and the
   *         response object has been modified to use this cached object (e.g.
   *         by using return code 304).
   */
  @OverrideOnDemand
  protected boolean useAndReturnCachedObject (@Nonnull final HttpServletRequest aHttpRequest,
                                              @Nonnull final HttpServletResponse aHttpResponse)
  {
    return false;
  }

  /**
   * Check if the object allows for HTTP caching (by setting the appropriate
   * HTTP headers)
   * 
   * @param aHttpRequest
   *        HTTP request
   * @param aHttpResponse
   *        HTTP response
   * @param sFilename
   *        request file name
   * @return <code>true</code> if the file allows for caching,
   *         <code>false</code> if not
   */
  @OverrideOnDemand
  protected boolean objectsAllowsForHTTPCaching (@Nonnull final HttpServletRequest aHttpRequest,
                                                 @Nonnull final HttpServletResponse aHttpResponse,
                                                 @Nonnull final String sFilename)
  {
    return true;
  }

  /**
   * Determine the MIME type of the resource to deliver.
   * 
   * @param sFilename
   *        The passed filename to stream. Never <code>null</code>.
   * @param aResource
   *        The resolved resource. Never <code>null</code>.
   * @return <code>null</code> if no MIME type could be determined
   */
  @OverrideOnDemand
  @Nullable
  protected String determineMimeType (@Nonnull final String sFilename, @Nonnull final IReadableResource aResource)
  {
    return MimeTypeDeterminator.getMimeTypeFromFilename (sFilename);
  }

  @Override
  protected void onDeliverResource (@Nonnull final HttpServletRequest aHttpRequest,
                                    @Nonnull final HttpServletResponse aHttpResponse,
                                    @Nonnull final String sFilename) throws IOException
  {
    m_aStatsRequests.increment ();
    final IReadableResource aRes = getResource (aHttpRequest, sFilename);

    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Trying to stream " + aRes);

    // determine whether requested resource is a directory
    final InputStream aIS = aRes.getInputStream ();
    if (aIS != null)
    {
      // Try to set content type no matter whether caching is used or not!
      final String sMimeType = determineMimeType (sFilename, aRes);
      if (sMimeType != null)
      {
        m_aStatsMIMEType.increment (sMimeType);
        aHttpResponse.setContentType (sMimeType);
        if (EMimeContentType.TEXT.isTypeOf (sMimeType))
        {
          // Important for FileBrowser HTML pages
          aHttpResponse.setCharacterEncoding (CHTMLCharset.CHARSET_HTML);
        }
        else
        {
          // If it is not a text/ MIME type, use no encoding!
          aHttpResponse.setCharacterEncoding (null);
        }
      }
      else
        s_aLogger.warn ("Failed to determine MIME type for filename '" + sFilename + "'");

      // Caching (e.g. ETag)?
      if (!useAndReturnCachedObject (aHttpRequest, aHttpResponse))
      {
        // HTTP caching possible?
        if (objectsAllowsForHTTPCaching (aHttpRequest, aHttpResponse, sFilename))
        {
          ResponseHelper.modifyResponseForExpiration (aHttpResponse, ResponseHelperSettings.getExpirationSeconds ());
        }
        else
          ResponseHelper.modifyResponseForNoCaching (aHttpResponse);

        // Can we get resource transfer working with GZIP or deflate?
        final OutputStream aOS = ResponseHelper.getBestSuitableOutputStream (aHttpRequest, aHttpResponse);

        // Do the main copying from InputStream to OutputStream
        final MutableLong aCopyByteCount = new MutableLong (0);
        if (StreamUtils.copyInputStreamToOutputStream (aIS, aOS, aCopyByteCount).isSuccess ())
        {
          m_aStatsSuccess.increment (sFilename);
          m_aStatsDeliveredBytes.addSize (aCopyByteCount.longValue ());

          // Successful copied!
          ResponseHelper.finishReponseOutputStream (aOS);

          // Source:
          // http://joshua.perina.com/africa/gambia/fajara/post/internet-explorer-css-file-size-limit
          if (StringHelper.startsWith (aHttpResponse.getContentType (), CMimeType.TEXT_CSS.getAsString ()) &&
              aCopyByteCount.longValue () > MAX_CSS_KB_FOR_IE * CGlobal.BYTES_PER_KILOBYTE_LONG)
          {
            s_aLogger.warn ("Internet Explorer has problems handling CSS files > " +
                            MAX_CSS_KB_FOR_IE +
                            "KB and this one has " +
                            aCopyByteCount.longValue () +
                            " bytes!");
          }

          if (s_aLogger.isDebugEnabled ())
            s_aLogger.debug ("Delivered " + aRes + " in content type '" + aHttpResponse.getContentType () + "'");
        }
        else
        {
          // This happens e.g. if the client cancelled the request
          s_aLogger.error ("Error streaming file '" + sFilename + "'; possibly known EOF exception");
          m_aStatsIOError.increment (sFilename);

          // Cannot send an error here, because the response is already
          // (partially) committed
          if (false)
            aHttpResponse.sendError (HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving '" +
                                                                                   sFilename +
                                                                                   "'");

          // Finish the request anyway!
          ResponseHelper.finishReponseOutputStream (aOS);
        }
      }
      else
      {
        // Using the cached resource
        m_aStatsCached.increment (sFilename);
        // Close the Input stream!!!!
        StreamUtils.close (aIS);
      }
    }
    else
    {
      s_aLogger.warn ("Trying to stream non-existing resource " + aRes);
      m_aStatsNotFound.increment (sFilename);
      aHttpResponse.sendError (HttpServletResponse.SC_NOT_FOUND, "'" + sFilename + "' does not exist");
    }
  }
}
