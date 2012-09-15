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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.scopes.web.servlet.AbstractScopeAwareHttpServlet;
import com.phloc.webbasics.http.EHTTPMethod;
import com.phloc.webbasics.http.EHTTPVersion;
import com.phloc.webbasics.web.RequestHelper;
import com.phloc.webbasics.web.UnifiedResponse;

/**
 * Abstract base class for a servlet performing actions via
 * {@link UnifiedResponse}.
 * 
 * @author philip
 */
public abstract class AbstractUnifiedResponseServlet extends AbstractScopeAwareHttpServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractUnifiedResponseServlet.class);

  public AbstractUnifiedResponseServlet ()
  {}

  /**
   * Check if the passed HTTP method is allowed. If not, a
   * {@link HttpServletResponse#SC_METHOD_NOT_ALLOWED} will be returned. By
   * default only {@link EHTTPMethod#GET} and {@link EHTTPMethod#POST} are
   * allowed.
   * 
   * @param eHTTPMethod
   *        The HTTP method to be checked.
   * @return <code>true</code> if the method is allowed and processing is
   *         continued, <code>false</code> to immediately send and HTTP error.
   */
  @OverrideOnDemand
  protected boolean isAllowedHTTPMethod (@Nonnull final EHTTPMethod eHTTPMethod)
  {
    return eHTTPMethod == EHTTPMethod.GET || eHTTPMethod == EHTTPMethod.POST;
  }

  /**
   * Called before a valid request is handled. This method is not called if HTTP
   * version or HTTP method are not supported.
   * 
   * @param aRequestScope
   *        The request scope that will be used for processing the request
   */
  @OverrideOnDemand
  protected void onRequestBegin (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {}

  /**
   * Called after a valid request was processed. This method is not called if
   * HTTP version or HTTP method are not supported.
   * 
   * @param bExceptionOccurred
   *        if <code>true</code> an exception occurred in request processing
   */
  @OverrideOnDemand
  protected void onRequestEnd (final boolean bExceptionOccurred)
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
   * @throws ServletException
   *         In case of an error
   */
  protected abstract void handleRequest (@Nonnull IRequestWebScopeWithoutResponse aRequestScope,
                                         @Nonnull UnifiedResponse aUnifiedResponse) throws ServletException,
                                                                                   IOException;

  private void _run (@Nonnull final HttpServletRequest aHttpRequest,
                     @Nonnull final HttpServletResponse aHttpResponse,
                     @Nonnull final IRequestWebScope aRequestScope,
                     @Nonnull final EHTTPMethod eHTTPMethod) throws ServletException, IOException
  {
    final EHTTPVersion eHTTPVersion = RequestHelper.getHttpVersion (aHttpRequest);
    if (eHTTPVersion == null)
    {
      s_aLogger.warn ("Request " + aRequestScope.getURL () + " has no valid HTTP version!");
      aHttpResponse.sendError (HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED);
    }
    else
      if (!isAllowedHTTPMethod (eHTTPMethod))
      {
        s_aLogger.warn ("Request " + aRequestScope.getURL () + " uses disallowed HTTP method " + eHTTPMethod + "!");
        // Disallow method
        if (eHTTPVersion == EHTTPVersion.HTTP_11)
          aHttpResponse.sendError (HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        else
          aHttpResponse.sendError (HttpServletResponse.SC_BAD_REQUEST);
      }
      else
      {
        // before-callback
        try
        {
          onRequestBegin (aRequestScope);
        }
        catch (final Throwable t)
        {
          s_aLogger.error ("onRequestBegin failed", t);
        }

        boolean bExceptionOccurred = true;
        try
        {
          // main
          final UnifiedResponse aUnifiedResponse = new UnifiedResponse (eHTTPVersion, aRequestScope);
          handleRequest (aRequestScope, aUnifiedResponse);
          aUnifiedResponse.applyToResponse (aHttpResponse);
          bExceptionOccurred = false;
        }
        finally
        {
          // after-callback
          try
          {
            onRequestEnd (bExceptionOccurred);
          }
          catch (final Throwable t)
          {
            s_aLogger.error ("onRequestEnd failed", t);
          }
        }
      }
  }

  @Override
  protected final void onDelete (@Nonnull final HttpServletRequest aHttpRequest,
                                 @Nonnull final HttpServletResponse aHttpResponse,
                                 @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    _run (aHttpRequest, aHttpResponse, aRequestScope, EHTTPMethod.DELETE);
  }

  @Override
  protected final void onGet (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final HttpServletResponse aHttpResponse,
                              @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    _run (aHttpRequest, aHttpResponse, aRequestScope, EHTTPMethod.GET);
  }

  @Override
  protected final void onHead (@Nonnull final HttpServletRequest aHttpRequest,
                               @Nonnull final HttpServletResponse aHttpResponse,
                               @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    _run (aHttpRequest, aHttpResponse, aRequestScope, EHTTPMethod.HEAD);
  }

  @Override
  protected final void onOptions (@Nonnull final HttpServletRequest aHttpRequest,
                                  @Nonnull final HttpServletResponse aHttpResponse,
                                  @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    _run (aHttpRequest, aHttpResponse, aRequestScope, EHTTPMethod.OPTIONS);
  }

  @Override
  protected final void onPost (@Nonnull final HttpServletRequest aHttpRequest,
                               @Nonnull final HttpServletResponse aHttpResponse,
                               @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    _run (aHttpRequest, aHttpResponse, aRequestScope, EHTTPMethod.POST);
  }

  @Override
  protected final void onPut (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final HttpServletResponse aHttpResponse,
                              @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    _run (aHttpRequest, aHttpResponse, aRequestScope, EHTTPMethod.PUT);
  }

  @Override
  protected final void onTrace (@Nonnull final HttpServletRequest aHttpRequest,
                                @Nonnull final HttpServletResponse aHttpResponse,
                                @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    _run (aHttpRequest, aHttpResponse, aRequestScope, EHTTPMethod.TRACE);
  }
}
