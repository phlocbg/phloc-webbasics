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
package com.phloc.webbasics.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.servlet.request.RequestHelper;
import com.phloc.web.servlet.response.ResponseHelper;
import com.phloc.web.servlet.response.StatusAwareHttpResponseWrapper;

/**
 * This filter is used, to determine if some crucial information is missing in
 * some responses. Checked things are status code, character encoding, content
 * type and some headers. This is mainly for debugging purposes.
 * 
 * @author Boris Gregorcic
 */
public class CheckResponseFilter implements Filter
{
  private static final Logger LOG = LoggerFactory.getLogger (CheckResponseFilter.class);

  @Override
  public void init (@Nonnull final FilterConfig filterConfig) throws ServletException
  {}

  /**
   * @param sRequestURL
   *        request URL
   * @param nStatusCode
   *        status code
   */
  protected void checkStatusCode (@Nonnull final String sRequestURL, final int nStatusCode)
  {
    if (nStatusCode != HttpServletResponse.SC_OK &&
        nStatusCode < HttpServletResponse.SC_MULTIPLE_CHOICES &&
        nStatusCode >= HttpServletResponse.SC_BAD_REQUEST)
      LOG.warn ("Status code {} in response to '{}'", nStatusCode, sRequestURL);
  }

  /**
   * @param sRequestURL
   *        request URL
   * @param sCharacterEncoding
   *        character encoding
   * @param nStatusCode
   *        status code
   */
  @OverrideOnDemand
  protected void checkCharacterEncoding (@Nonnull final String sRequestURL,
                                         @Nullable final String sCharacterEncoding,
                                         final int nStatusCode)
  {
    if (StringHelper.hasNoText (sCharacterEncoding) && !ResponseHelper.isEmptyStatusCode (nStatusCode))
      LOG.warn ("No character encoding on {} response to '{}'", nStatusCode, sRequestURL);
  }

  /**
   * @param sRequestURL
   *        request URL
   * @param sContentType
   *        content type
   * @param nStatusCode
   *        status code
   */
  @OverrideOnDemand
  protected void checkContentType (@Nonnull final String sRequestURL,
                                   @Nullable final String sContentType,
                                   final int nStatusCode)
  {
    if (StringHelper.hasNoText (sContentType) && !ResponseHelper.isEmptyStatusCode (nStatusCode))
      LOG.warn ("No content type on {} response to '{}'", nStatusCode, sRequestURL);
  }

  @OverrideOnDemand
  protected void checkHeaders (@Nonnull final String sRequestURL,
                               @Nonnull final Map <String, List <String>> aHeaders,
                               final int nStatusCode)
  {
    if (nStatusCode != HttpServletResponse.SC_OK && !aHeaders.isEmpty ())
      LOG.warn ("Headers on {} response to '{}': {}", nStatusCode, sRequestURL, aHeaders);
  }

  private void _checkResults (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final StatusAwareHttpResponseWrapper aHttpResponse)
  {
    final String sRequestURL = RequestHelper.getURL (aHttpRequest);
    final int nStatusCode = aHttpResponse.getStatusCode ();
    final Map <String, List <String>> aHeaders = aHttpResponse.getHeaderMap ().getAllHeaders ();
    final String sCharacterEncoding = aHttpResponse.getCharacterEncoding ();
    final String sContentType = aHttpResponse.getContentType ();

    checkStatusCode (sRequestURL, nStatusCode);
    checkCharacterEncoding (sRequestURL, sCharacterEncoding, nStatusCode);
    checkContentType (sRequestURL, sContentType, nStatusCode);
    checkHeaders (sRequestURL, aHeaders, nStatusCode);
  }

  @Override
  public void doFilter (@Nonnull final ServletRequest aRequest,
                        @Nonnull final ServletResponse aResponse,
                        final FilterChain aChain) throws IOException, ServletException
  {
    // Works only for HTTP requests
    if (aRequest instanceof HttpServletRequest && aResponse instanceof HttpServletResponse)
    {
      // Create a response wrapper
      final StatusAwareHttpResponseWrapper aWrapper = new StatusAwareHttpResponseWrapper ((HttpServletResponse) aResponse);
      aChain.doFilter (aRequest, aWrapper);
      // Evaluate the result
      _checkResults ((HttpServletRequest) aRequest, aWrapper);
    }
    else
    {
      // Do nothing
      aChain.doFilter (aRequest, aResponse);
    }
  }

  @Override
  public void destroy ()
  {}
}
