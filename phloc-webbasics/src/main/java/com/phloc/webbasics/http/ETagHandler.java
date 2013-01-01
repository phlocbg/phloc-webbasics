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
package com.phloc.webbasics.http;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

/**
 * Utility class that helps handling ETags.<br>
 * See also http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.19
 * 
 * @author philip
 */
@Immutable
@Deprecated
public final class ETagHandler
{
  private ETagHandler ()
  {}

  /**
   * Assign an ETag to the given HTTP servlet response.<br>
   * Note: usually an additional Cache-Control:public header should also be
   * present so that caching is effective!
   * 
   * @param aHttpResponse
   *        The response object. May not be <code>null</code>.
   * @param sETag
   *        The ETag to set. May neither be <code>null</code> nor empty.
   */
  public static void setResponseETag (@Nonnull final HttpServletResponse aHttpResponse,
                                      @Nonnull @Nonempty final String sETag)
  {
    if (aHttpResponse == null)
      throw new NullPointerException ("httpResponse");
    if (StringHelper.hasNoText (sETag))
      throw new IllegalArgumentException ("ETag is empty");
    aHttpResponse.setHeader (CHTTPHeader.ETAG, sETag);
  }

  /**
   * Check if the request wants an ETag Comparison.
   * 
   * @param aHttpRequest
   *        The HTTP request to check. May not be <code>null</code>.
   * @return The ETag to compare to or <code>null</code> if the request does not
   *         want to make an ETag comparison.
   */
  @Nullable
  public static String getRequestETagComparison (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");
    return aHttpRequest.getHeader (CHTTPHeader.IF_NON_MATCH);
  }
}
