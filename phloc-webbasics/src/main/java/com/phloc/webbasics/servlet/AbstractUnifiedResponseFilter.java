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

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.state.EContinue;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.scopes.web.servlet.AbstractScopeAwareFilter;
import com.phloc.webbasics.http.EHTTPMethod;
import com.phloc.webbasics.http.EHTTPVersion;
import com.phloc.webbasics.web.RequestHelper;
import com.phloc.webbasics.web.UnifiedResponse;

/**
 * Abstract base class for a filter performing actions via
 * {@link UnifiedResponse}.
 * 
 * @author philip
 */
public abstract class AbstractUnifiedResponseFilter extends AbstractScopeAwareFilter
{
  public AbstractUnifiedResponseFilter ()
  {}

  /**
   * Overwrite this method to fill your response.
   * 
   * @param aRequestScope
   *        The request scope to use. There is no direct access to the
   *        {@link HttpServletResponse}. Everything must be handled with the
   *        unified response! Never <code>null</code>.
   * @param aUnifiedResponse
   *        The response object to be filled. Never <code>null</code>.
   * @return If {@link EContinue#BREAK} is returned, the content of the unified
   *         response is rendered to the HTTP servlet response and the filter
   *         chain stops.
   * @throws ServletException
   *         In case of an error
   */
  @Nonnull
  protected abstract EContinue handleRequest (@Nonnull IRequestWebScopeWithoutResponse aRequestScope,
                                              @Nonnull UnifiedResponse aUnifiedResponse) throws ServletException;

  @Override
  @Nonnull
  protected final EContinue doFilter (@Nonnull final HttpServletRequest aHttpRequest,
                                      @Nonnull final HttpServletResponse aHttpResponse,
                                      @Nonnull final IRequestWebScope aRequestScope) throws IOException,
                                                                                    ServletException
  {
    // Check HTTP version
    final EHTTPVersion eHTTPVersion = RequestHelper.getHttpVersion (aHttpRequest);
    if (eHTTPVersion == null)
    {
      aHttpResponse.sendError (HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED);
      return EContinue.BREAK;
    }

    // Check HTTP Method
    final EHTTPMethod eHTTPMethod = EHTTPMethod.getFromNameOrNull (aHttpRequest.getMethod ());
    if (eHTTPMethod == null)
    {
      if (eHTTPVersion == EHTTPVersion.HTTP_11)
        aHttpResponse.sendError (HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      else
        aHttpResponse.sendError (HttpServletResponse.SC_BAD_REQUEST);
      return EContinue.BREAK;
    }

    final UnifiedResponse aUnifiedResponse = new UnifiedResponse (eHTTPVersion, eHTTPMethod, aRequestScope);
    if (handleRequest (aRequestScope, aUnifiedResponse).isContinue ())
    {
      // Filter passed, without any output -> continue
      return EContinue.CONTINUE;
    }

    // Filter ended chain -> send response
    aUnifiedResponse.applyToResponse (aHttpResponse);
    return EContinue.BREAK;
  }
}
