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
import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.state.EContinue;
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

  @Nonnull
  protected final LocalDateTime convertMillisToDateTime (final long nMillis)
  {
    // Round down to the nearest second for a proper compare
    return PDTFactory.createLocalDateTimeFromMillis (nMillis / 1000 * 1000);
  }

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
   *        The request scope that will be used for processing the request.
   *        Never <code>null</code>.
   * @param aUnifiedResponse
   *        The response object to be filled. Never <code>null</code>.
   * @return {@link EContinue#BREAK} to stop processing (e.g. because a resource
   *         does not exist), {@link EContinue#CONTINUE} to continue processing
   *         as usual.
   */
  @OverrideOnDemand
  protected EContinue initRequestState (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                        @Nonnull final UnifiedResponse aUnifiedResponse)
  {
    return EContinue.CONTINUE;
  }

  /**
   * Get the last modification date time for the current request. If it was not
   * modified since the last request time, a 304 (not modified) response code is
   * returned. This method is always called for GET and HEAD requests.
   * 
   * @param aRequestScope
   *        The request scope that will be used for processing the request.
   *        Never <code>null</code>.
   * @return <code>null</code> if no last modification date time can be
   *         determined
   */
  @OverrideOnDemand
  @Nullable
  protected LocalDateTime getLastModificationDateTime (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return null;
  }

  /**
   * Get the ETag supported for this request. If an ETag matches, a 304 (not
   * modified) response code is returned. This method is always called for GET
   * and HEAD requests.
   * 
   * @param aRequestScope
   *        The request scope that will be used for processing the request.
   *        Never <code>null</code>.
   * @return <code>null</code> if this servlet does not support ETags
   */
  @OverrideOnDemand
  @Nullable
  protected String getSupportedETag (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return null;
  }

  /**
   * Called before a valid request is handled. This method is only called if
   * HTTP version matches, HTTP method is supported and caching is not an
   * option.
   * 
   * @param aRequestScope
   *        The request scope that will be used for processing the request.
   *        Never <code>null</code>.
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
   *        if <code>true</code> an exception occurred in request processing.
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

    final UnifiedResponse aUnifiedResponse = new UnifiedResponse (eHTTPVersion, eHTTPMethod, aRequestScope);
    if (initRequestState (aRequestScope, aUnifiedResponse).isBreak ())
    {
      // May e.g. be an 404 error for some not-found resource
      aUnifiedResponse.applyToResponse (aHttpResponse);
      return;
    }

    // Check for last-modification on GET and HEAD
    if (eHTTPMethod == EHTTPMethod.GET || eHTTPMethod == EHTTPMethod.HEAD)
    {
      final LocalDateTime aLastModification = getLastModificationDateTime (aRequestScope);
      if (aLastModification != null)
      {
        // Get the If-Modified-Since date header
        final long nRequestIfModifiedSince = aHttpRequest.getDateHeader (CHTTPHeader.IF_MODIFIED_SINCE);
        if (nRequestIfModifiedSince >= 0)
        {
          final LocalDateTime aRequestIfModifiedSince = convertMillisToDateTime (nRequestIfModifiedSince);
          if (aLastModification.isAfter (aRequestIfModifiedSince))
          {
            // Was not modified since the passed time
            aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_MODIFIED).applyToResponse (aHttpResponse);
            return;
          }
        }

        // No If-Modified-Since request header present, set the Last-Modified
        // header for later reuse
        aUnifiedResponse.setLastModified (aLastModification);
      }

      // Handle the ETag
      final String sSupportedETag = getSupportedETag (aRequestScope);
      if (StringHelper.hasText (sSupportedETag))
      {
        // get the request ETag
        final String sRequestETag = aHttpRequest.getHeader (CHTTPHeader.IF_NON_MATCH);
        if (StringHelper.hasText (sRequestETag))
        {
          // Request header may contain several ETag values
          final List <String> aAllETags = RegExHelper.getSplitToList (sRequestETag, ",\\s+");
          if (aAllETags.isEmpty ())
            s_aLogger.warn ("Empty ETag list found (" + sRequestETag + ")");
          else
          {
            // Scan all found ETags for match
            for (final String sCurrentETag : aAllETags)
              if (sSupportedETag.equals (sCurrentETag))
              {
                // We have a matching ETag
                aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_MODIFIED).applyToResponse (aHttpResponse);
                return;
              }
          }
        }

        // Save the ETag for the response
        aUnifiedResponse.setETagIfApplicable (sSupportedETag);
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
      // main servlet handling
      handleRequest (aRequestScope, aUnifiedResponse);
      // Only write to the response, if no error occurred
      aUnifiedResponse.applyToResponse (aHttpResponse);
      // No error occurred
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
