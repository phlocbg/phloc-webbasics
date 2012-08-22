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

import javax.annotation.Nonnull;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.webbasics.http.AcceptEncodingHandler;
import com.phloc.webbasics.http.AcceptEncodingList;
import com.phloc.webbasics.web.ResponseHelper;

/**
 * This is a generic filter that first tries to find whether "GZip" is
 * supported, and if this fails, whether "Deflate" is supported. If none is
 * supported, no compression will happen in this filter.
 * 
 * @author philip
 */
public final class CompressFilter implements Filter
{
  static final String REQUEST_ATTR = CompressFilter.class.getName ();
  private static final IStatisticsHandlerCounter s_aStatsGZip = StatisticsManager.getCounterHandler (CompressFilter.class.getName () +
                                                                                                     "$gzip");
  private static final IStatisticsHandlerCounter s_aStatsDeflate = StatisticsManager.getCounterHandler (CompressFilter.class.getName () +
                                                                                                        "$deflate");
  private static final IStatisticsHandlerCounter s_aStatsNone = StatisticsManager.getCounterHandler (CompressFilter.class.getName () +
                                                                                                     "$none");

  public void init (@Nonnull final FilterConfig filterConfig)
  {
    // As compression is done in the filter, no compression is required there
    ResponseHelper.setResponseCompressionEnabled (false);
  }

  public void doFilter (@Nonnull final ServletRequest aRequest,
                        @Nonnull final ServletResponse aResponse,
                        @Nonnull final FilterChain aChain) throws IOException, ServletException
  {
    if (aRequest instanceof HttpServletRequest && aRequest.getAttribute (REQUEST_ATTR) == null)
    {
      aRequest.setAttribute (REQUEST_ATTR, Boolean.TRUE);
      final HttpServletRequest aHttpRequest = (HttpServletRequest) aRequest;
      final HttpServletResponse aHttpResponse = (HttpServletResponse) aResponse;
      final AcceptEncodingList aAEL = AcceptEncodingHandler.getAcceptEncodings (aHttpRequest);

      // GZip
      final String sGZIPEncoding = aAEL.getUsedGZIPEncoding ();
      if (sGZIPEncoding != null)
      {
        s_aStatsGZip.increment ();
        final GZIPResponseWrapper aGZIPResponse = new GZIPResponseWrapper (aHttpResponse, sGZIPEncoding);
        aChain.doFilter (aRequest, aGZIPResponse);
        aGZIPResponse.finishResponse (aHttpRequest.getRequestURL ().toString ());
        return;
      }

      // Deflate
      final String sDeflateEncoding = aAEL.getUsedDeflateEncoding ();
      if (sDeflateEncoding != null)
      {
        s_aStatsDeflate.increment ();
        final DeflateResponseWrapper aDeflateResponse = new DeflateResponseWrapper (aHttpResponse, sDeflateEncoding);
        aChain.doFilter (aRequest, aDeflateResponse);
        aDeflateResponse.finishResponse (aHttpRequest.getRequestURL ().toString ());
        return;
      }

      // No GZip or deflate
      s_aStatsNone.increment ();
    }

    // Perform as is
    aChain.doFilter (aRequest, aResponse);
  }

  public void destroy ()
  {}
}
