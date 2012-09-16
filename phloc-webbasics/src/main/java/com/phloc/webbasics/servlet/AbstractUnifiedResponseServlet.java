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
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.datetime.PDTFactory;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.scopes.web.servlet.AbstractScopeAwareHttpServlet;
import com.phloc.webbasics.http.CHTTPHeader;
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
  public static final EnumSet <EHTTPMethod> DEFAULT_ALLOWED_METHDOS = EnumSet.of (EHTTPMethod.GET, EHTTPMethod.POST);
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractUnifiedResponseServlet.class);

  public AbstractUnifiedResponseServlet ()
  {}

  /**
   * Check if the passed HTTP method is allowed. If not, a
   * {@link HttpServletResponse#SC_METHOD_NOT_ALLOWED} will be returned. By
   * default only {@link EHTTPMethod#GET} and {@link EHTTPMethod#POST} are
   * allowed.
   * 
   * @return A non-<code>null</code> set of all allowed HTTP methods.
   */
  @OverrideOnDemand
  protected EnumSet <EHTTPMethod> getAllowedHTTPMethods ()
  {
    return DEFAULT_ALLOWED_METHDOS;
  }

  /**
   * This callback method is unconditionally called before the last-modification
   * checks are performed. So this method can be used to determine the requested
   * object from the request. This method is not called if HTTP version or HTTP
   * method are not supported.
   * 
   * @param aRequestScope
   *        The request scope that will be used for processing the request
   */
  @OverrideOnDemand
  protected void initRequestState (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {}

  /**
   * Check if the request was modified since the passed date time. If it was not
   * modified since the passed time, a 304 (not modified) response code is
   * returned. This method is only called if an If-Modified-Since request header
   * is present.
   * 
   * @param aIfModifiedSince
   *        The non-<code>null</code> date time of the request.
   * @return <code>true</code> if the object was modified since the passed time,
   *         <code>false</code> if it was not modified since the passed time.
   */
  @OverrideOnDemand
  protected boolean isModifiedSince (@Nonnull final LocalDateTime aIfModifiedSince)
  {
    return true;
  }

  /**
   * Check if the ETags passed in the request matches the expected ETag. If an
   * ETag matches, a 304 (not modified) response code is returned. This method
   * is only called if an If-None-Match request header is present.
   * 
   * @param aAllETags
   *        The non-<code>null</code> non-empty list of the supplied ETags
   * @return <code>true</code> if the ETag is supported, <code>false</code> if
   *         none of the ETags match.
   */
  @OverrideOnDemand
  protected boolean isSupportedETag (@Nonnull final List <String> aAllETags)
  {
    return true;
  }

  /**
   * Called before a valid request is handled. This method is only called if
   * HTTP version matches, HTTP method is supported and caching is not an
   * option.
   * 
   * @param aRequestScope
   *        The request scope that will be used for processing the request
   */
  @OverrideOnDemand
  protected void onRequestBegin (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
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

  private void _run (@Nonnull final HttpServletRequest aHttpRequest,
                     @Nonnull final HttpServletResponse aHttpResponse,
                     @Nonnull final IRequestWebScope aRequestScope,
                     @Nonnull final EHTTPMethod eHTTPMethod) throws ServletException, IOException
  {
    final EHTTPVersion eHTTPVersion = RequestHelper.getHttpVersion (aHttpRequest);
    if (eHTTPVersion == null)
    {
      // HTTP version disallowed
      s_aLogger.warn ("Request " + aRequestScope.getURL () + " has no valid HTTP version!");
      aHttpResponse.sendError (HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED);
      return;
    }

    final EnumSet <EHTTPMethod> aAllowedHTTPMethods = getAllowedHTTPMethods ();
    if (!aAllowedHTTPMethods.contains (eHTTPMethod))
    {
      // Disallow method

      // Build Allow response header
      final StringBuilder aAllow = new StringBuilder ();
      for (final EHTTPMethod eAllowedHTTPMethod : aAllowedHTTPMethods)
      {
        if (aAllow.length () > 0)
          aAllow.append (", ");
        aAllow.append (eAllowedHTTPMethod.getName ());
      }
      final String sAllow = aAllow.toString ();
      s_aLogger.warn ("Request " +
                      aRequestScope.getURL () +
                      " uses disallowed HTTP method " +
                      eHTTPMethod +
                      "! Allowed methods are: " +
                      sAllow);
      aHttpResponse.setHeader (CHTTPHeader.ALLOW, sAllow);

      if (eHTTPVersion == EHTTPVersion.HTTP_11)
        aHttpResponse.sendError (HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      else
        aHttpResponse.sendError (HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // Now all pre-conditions were checked. As the next step check some last
    // modification issues, for performance reasons. If all optimizations fail,
    // perform the regular request.

    initRequestState (aRequestScope);

    // Check for last-modification on GET
    if (eHTTPMethod == EHTTPMethod.GET || eHTTPMethod == EHTTPMethod.HEAD)
    {
      // Honour the If-Modified-Since date header
      final long nIfModifiedSince = aHttpRequest.getDateHeader (CHTTPHeader.IF_MODIFIED_SINCE);
      if (nIfModifiedSince >= 0)
      {
        final LocalDateTime aIfModifiedSince = PDTFactory.createLocalDateTimeFromMillis (nIfModifiedSince);
        if (!isModifiedSince (aIfModifiedSince))
        {
          // Was not modified since the passed time
          aHttpResponse.setStatus (HttpServletResponse.SC_NOT_MODIFIED);
          return;
        }
      }

      // Honour the If-Non-Match header (ETag)
      final String sETag = aHttpRequest.getHeader (CHTTPHeader.IF_NON_MATCH);
      if (StringHelper.hasText (sETag))
      {
        // Header may contain several ETag values
        final List <String> aAllETags = RegExHelper.getSplitToList (sETag, ",\\s+");
        if (aAllETags.isEmpty ())
          s_aLogger.warn ("Empty ETag list found (" + sETag + ")");
        else
          if (isSupportedETag (aAllETags))
          {
            // Was not modified since the passed time
            aHttpResponse.setStatus (HttpServletResponse.SC_NOT_MODIFIED);
            return;
          }
      }
    }

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
      final UnifiedResponse aUnifiedResponse = new UnifiedResponse (eHTTPVersion, eHTTPMethod, aRequestScope);
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
