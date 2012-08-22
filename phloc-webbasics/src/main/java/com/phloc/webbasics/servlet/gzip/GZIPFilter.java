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

import com.phloc.webbasics.http.AcceptEncodingHandler;
import com.phloc.webbasics.web.ResponseHelper;

/**
 * A filter that applies GZip compression to all elements, if applicable. It
 * interprets the accept-encoding HTTP header to identify whether GZip is
 * supported or not.
 * 
 * @author philip
 */
public final class GZIPFilter implements Filter
{
  public void init (@Nonnull final FilterConfig filterConfig)
  {
    // As compression is done in the filter, no compression is required there
    ResponseHelper.setResponseCompressionEnabled (false);
  }

  public void doFilter (@Nonnull final ServletRequest aRequest,
                        @Nonnull final ServletResponse aResponse,
                        @Nonnull final FilterChain aChain) throws IOException, ServletException
  {
    if (aRequest instanceof HttpServletRequest)
    {
      final HttpServletRequest aHttpRequest = (HttpServletRequest) aRequest;
      final HttpServletResponse aHttpResponse = (HttpServletResponse) aResponse;

      final String sGZIPEncoding = AcceptEncodingHandler.getAcceptEncodings (aHttpRequest).getUsedGZIPEncoding ();
      if (sGZIPEncoding != null)
      {
        final GZIPResponseWrapper aGZIPResponse = new GZIPResponseWrapper (aHttpResponse, sGZIPEncoding);
        aChain.doFilter (aRequest, aGZIPResponse);
        aGZIPResponse.finishResponse (aHttpRequest.getRequestURL ().toString ());
        return;
      }
    }

    // No GZipping
    aChain.doFilter (aRequest, aResponse);
  }

  public void destroy ()
  {}
}
