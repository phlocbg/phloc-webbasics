/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.webappredirect.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectFilter implements Filter
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (RedirectFilter.class);

  @Override
  public void init (final FilterConfig aFilterConfig) throws ServletException
  {}

  @Override
  public void doFilter (final ServletRequest aRequest, final ServletResponse aResponse, final FilterChain aChain) throws IOException,
                                                                                                                 ServletException
  {
    if (aRequest instanceof HttpServletRequest && aResponse instanceof HttpServletResponse)
    {
      final HttpServletRequest aHttpRequest = (HttpServletRequest) aRequest;
      final HttpServletResponse aHttpResponse = (HttpServletResponse) aResponse;

      // Build including any session ID!
      String sRelativeURI = aHttpRequest.getRequestURI ();
      final String sQueryString = aHttpRequest.getQueryString ();
      if (sQueryString != null && sQueryString.length () > 0)
        sRelativeURI += '?' + sQueryString;

      final String sServletContextPath = aHttpRequest.getContextPath ();
      if (sServletContextPath != null && sServletContextPath.length () > 0)
        if (sRelativeURI.startsWith (sServletContextPath))
          sRelativeURI = sRelativeURI.substring (sServletContextPath.length ());

      final String sTarget = RedirectListener.getTargetURL ().toExternalForm () + sRelativeURI;
      s_aLogger.info ("Redirecting to " + sTarget);
      aHttpResponse.setHeader ("Location", sTarget);
      aHttpResponse.setStatus (HttpServletResponse.SC_MOVED_TEMPORARILY);
    }
    else
      aChain.doFilter (aRequest, aResponse);
  }

  @Override
  public void destroy ()
  {}
}
