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

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.mime.EMimeContentType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mime.MimeType;
import com.phloc.commons.mime.MimeTypeDeterminator;
import com.phloc.commons.random.VerySecureRandom;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.datetime.PDTFactory;
import com.phloc.html.CHTMLCharset;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.http.CacheControlBuilder;
import com.phloc.webbasics.web.ResponseHelperSettings;
import com.phloc.webbasics.web.UnifiedResponse;

/**
 * Simple servlet to forward requests to pages normally not visible to the
 * Web-Server. It simply reads the content of a file and passes it to an output
 * stream.
 * 
 * @author philip
 */
public abstract class AbstractStreamServlet extends AbstractObjectDeliveryServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractStreamServlet.class);
  private final IStatisticsHandlerCounter m_aStatsRequests = StatisticsManager.getCounterHandler (getClass ().getName () +
                                                                                                  "$requests");
  private final IStatisticsHandlerKeyedCounter m_aStatsSuccess = StatisticsManager.getKeyedCounterHandler (getClass ().getName () +
                                                                                                           "$success");
  private final IStatisticsHandlerKeyedCounter m_aStatsNotFound = StatisticsManager.getKeyedCounterHandler (getClass ().getName () +
                                                                                                            "$notfound");
  private final IStatisticsHandlerKeyedCounter m_aStatsMIMEType = StatisticsManager.getKeyedCounterHandler (getClass ().getName () +
                                                                                                            "$mimetype");

  /**
   * Create a unique value per server startup. This is the ETag value for all
   * resources streamed from this servlet, since it uses only ClassPath
   * resources that may only change upon new initialisation of this class.
   * Therefore the ETag value is calculated only once and used to stream all
   * classpath resources.
   */
  private static final String ETAG_VALUE_STREAMSERVLET = '"' + Long.toString (VerySecureRandom.getInstance ()
                                                                                              .nextLong ()) + '"';

  @Override
  protected final boolean isSupportedETag (@Nonnull final List <String> aAllETags)
  {
    for (final String sETag : aAllETags)
      if (ETAG_VALUE_STREAMSERVLET.equals (sETag))
        return true;
    return false;
  }

  /**
   * Resolve the resource specified by the given file name.
   * 
   * @param aRequestScope
   *        The request to get request values from. Never <code>null</code>.
   * @param sFilename
   *        The name of the file to be resolved.
   * @return The non-<code>null</code> resource provider.
   */
  @Nonnull
  protected abstract IReadableResource getResource (@Nonnull IRequestWebScopeWithoutResponse aRequestScope,
                                                    @Nonnull String sFilename);

  /**
   * Check if the object allows for HTTP caching (by setting the appropriate
   * HTTP headers)
   * 
   * @param aRequestScope
   *        Request scope
   * @param sFilename
   *        request file name
   * @return <code>true</code> if the file allows for caching,
   *         <code>false</code> if not
   */
  @OverrideOnDemand
  protected boolean objectsAllowsForHTTPCaching (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
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
  protected void onDeliverResource (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                    @Nonnull final UnifiedResponse aUnifiedResponse,
                                    @Nonnull final String sFilename) throws IOException
  {
    m_aStatsRequests.increment ();
    final IReadableResource aRes = getResource (aRequestScope, sFilename);

    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Trying to stream " + aRes);

    // determine whether requested resource is a directory
    if (aRes.exists ())
    {
      // Try to set content type no matter whether caching is used or not!
      final String sMimeType = determineMimeType (sFilename, aRes);
      if (sMimeType != null)
      {
        m_aStatsMIMEType.increment (sMimeType);
        final IMimeType aMimeType = MimeType.parseFromStringWithoutEncoding (sMimeType);
        if (aMimeType == null)
          s_aLogger.warn ("Failed to parse MimeType '" + sMimeType + "'");
        else
          aUnifiedResponse.setMimeType (aMimeType);
        if (EMimeContentType.TEXT.isTypeOf (sMimeType))
        {
          // Important for FileBrowser HTML pages
          aUnifiedResponse.setCharset (CHTMLCharset.CHARSET_HTML_OBJ);
        }
      }
      else
        s_aLogger.warn ("Failed to determine MIME type for filename '" + sFilename + "'");

      LocalDateTime aLastModified = null;
      final File aFile = aRes.getAsFile ();
      if (aFile != null)
      {
        final long nLastModified = aFile.lastModified ();
        if (nLastModified > 0)
          aLastModified = PDTFactory.createLocalDateTimeFromMillis (nLastModified);
      }

      if (aLastModified != null)
        aUnifiedResponse.setLastModified (aLastModified);

      // Set ETag in response for next time
      aUnifiedResponse.setETagIfApplicable (ETAG_VALUE_STREAMSERVLET);

      // HTTP caching possible?
      if (objectsAllowsForHTTPCaching (aRequestScope, sFilename))
      {
        aUnifiedResponse.setCacheControl (new CacheControlBuilder ().setMaxAgeSeconds (ResponseHelperSettings.getExpirationSeconds ())
                                                                    .setPublic (true));
      }
      else
      {
        aUnifiedResponse.disableCaching ();
      }

      // Do the main copying from InputStream to OutputStream
      aUnifiedResponse.setContent (aRes);
      m_aStatsSuccess.increment (sFilename);
    }
    else
    {
      s_aLogger.warn ("Trying to stream non-existing resource " + aRes);
      m_aStatsNotFound.increment (sFilename);
      aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_FOUND);
    }
  }
}
