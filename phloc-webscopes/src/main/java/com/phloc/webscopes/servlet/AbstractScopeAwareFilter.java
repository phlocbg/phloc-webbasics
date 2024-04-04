/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webscopes.servlet;

import java.io.IOException;

import javax.annotation.Nonnull;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.webscopes.domain.IRequestWebScope;

/**
 * Abstract HTTP servlet filter implementation using the correct scope handling.
 * The scope initialization happens before the main action is executed, and the
 * scope destruction happens after <b>all</b> the whole filter chain finished!
 * If more than one scope aware filter are present in the filter chain, only the
 * filter invoked first creates the request scope. Succeeding scope aware
 * filters wont create a request scope.
 * 
 * @author Boris Gregorcic
 * @author Philip Helger
 */
public abstract class AbstractScopeAwareFilter implements Filter
{
  private static final Logger LOG = LoggerFactory.getLogger (AbstractScopeAwareFilter.class);
  private String m_sApplicationID;

  /**
   * Determine the application ID to be used, based on the passed filter
   * configuration. This method is only invoked once on startup.
   * 
   * @param aFilterConfig
   *        The filter configuration
   * @return The application ID for this filter.
   */
  @OverrideOnDemand
  protected String getApplicationID (@SuppressWarnings ("unused") @Nonnull final FilterConfig aFilterConfig)
  {
    return CGStringHelper.getClassLocalName (getClass ());
  }

  /**
   * Initialize the filter
   * 
   * @param aFilterConfig
   *        Filter configuration from servlet container
   * @throws ServletException
   *         If something goes wrong
   */
  @OverrideOnDemand
  protected void onInit (@SuppressWarnings ("unused") @Nonnull final FilterConfig aFilterConfig) throws ServletException
  {
    // override on demand
  }

  @Override
  public final void init (@Nonnull final FilterConfig aFilterConfig) throws ServletException
  {
    this.m_sApplicationID = getApplicationID (aFilterConfig);
    if (StringHelper.hasNoText (this.m_sApplicationID))
    {
      throw new InitializationException ("Failed retrieve a valid application ID!"); //$NON-NLS-1$
    }
    onInit (aFilterConfig);
  }

  /**
   * Implement this main filtering method in subclasses.
   * 
   * @param aHttpRequest
   *        The HTTP request. Never <code>null</code>.
   * @param aHttpResponse
   *        The HTTP response. Never <code>null</code>.
   * @param aRequestScope
   *        The request scope to be used.
   * @return {@link EContinue#CONTINUE} to indicate that the next filter is to
   *         be called or {@link EContinue#BREAK} to indicate that the next
   *         filter does not need to be called! Never return <code>null</code>!
   * @throws IOException
   *         eventually
   * @throws ServletException
   *         eventually
   */
  @Nonnull
  protected abstract EContinue doFilter (@Nonnull HttpServletRequest aHttpRequest,
                                         @Nonnull HttpServletResponse aHttpResponse,
                                         @Nonnull IRequestWebScope aRequestScope) throws IOException, ServletException;

  @Override
  public final void doFilter (final ServletRequest aRequest,
                              final ServletResponse aResponse,
                              final FilterChain aChain) throws IOException, ServletException
  {
    if (aRequest instanceof HttpServletRequest && aResponse instanceof HttpServletResponse)
    {
      final HttpServletRequest aHttpRequest = (HttpServletRequest) aRequest;
      final HttpServletResponse aHttpResponse = (HttpServletResponse) aResponse;

      RequestScopeInitializer aRequestScopeInitializer = null;
      try
      {
        // Check if a scope needs to be created
        aRequestScopeInitializer = RequestScopeInitializer.create (this.m_sApplicationID, aHttpRequest, aHttpResponse);
      }
      catch (final Throwable aEx)
      {
        onRequestInitializationException (aEx);
        return;
      }
      try
      {
        // Apply any optional filter
        if (doFilter (aHttpRequest, aHttpResponse, aRequestScopeInitializer.getRequestScope ()).isContinue ())
        {
          // Continue as usual
          aChain.doFilter (aRequest, aResponse);
        }
      }
      finally
      {
        // And destroy the scope depending on its creation state
        aRequestScopeInitializer.destroyScope ();
      }
    }
    else
    {
      // No scope handling
      aChain.doFilter (aRequest, aResponse);
    }
  }

  @SuppressWarnings ("static-method")
  @OverrideOnDemand
  protected void onRequestInitializationException (@Nonnull final Throwable aEx)
  {
    LOG.error ("Exception initializing request scope", aEx); //$NON-NLS-1$
  }

  @Override
  @OverrideOnDemand
  public void destroy ()
  {
    // override on demand
  }
}
