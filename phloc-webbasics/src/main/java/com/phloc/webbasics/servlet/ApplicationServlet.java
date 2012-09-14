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

import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.lang.ServiceLoaderBackport;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.app.html.ApplicationRunner;
import com.phloc.webbasics.app.html.IHTMLProvider;
import com.phloc.webbasics.app.html.LayoutHTMLProvider;
import com.phloc.webbasics.http.EHTTPMethod;
import com.phloc.webbasics.spi.IApplicationRequestListenerSPI;
import com.phloc.webbasics.web.UnifiedResponse;

/**
 * Base servlet for the main application.
 * 
 * @author philip
 */
public class ApplicationServlet extends AbstractUnifiedResponseServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ApplicationServlet.class);

  private final List <IApplicationRequestListenerSPI> m_aListeners;

  public ApplicationServlet ()
  {
    m_aListeners = ContainerHelper.newList (ServiceLoaderBackport.load (IApplicationRequestListenerSPI.class));
  }

  /**
   * Called before the request is handled
   * 
   * @param aRequestScope
   *        The request scope
   */
  @OverrideOnDemand
  protected void onRequestBegin (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {}

  /**
   * @param aRequestScope
   *        The request scope
   * @return The HTML provider that creates the content.
   */
  @OverrideOnDemand
  @Nonnull
  protected IHTMLProvider createHTMLProvider (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return new LayoutHTMLProvider ();
  }

  /**
   * Called after the request was handled
   * 
   * @param aRequestScope
   *        The request scope
   */
  @OverrideOnDemand
  protected void onRequestEnd (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {}

  @Override
  protected final void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                      @Nonnull final EHTTPMethod eHTTPMethod,
                                      @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException
  {
    // Run default request initialization (menu item and locale)
    ApplicationRequestManager.onRequestBegin (aRequestScope);

    // Protected method invocation
    onRequestBegin (aRequestScope);

    // Invoke all "request begin" listener
    for (final IApplicationRequestListenerSPI aListener : m_aListeners)
      try
      {
        aListener.onRequestBegin ();
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Failed to invoke onRequestBegin on " + aListener, t);
      }

    try
    {
      // Who is responsible for creating the HTML?
      final IHTMLProvider aHTMLProvider = createHTMLProvider (aRequestScope);
      ApplicationRunner.createHTMLResponse (aRequestScope, aUnifiedResponse, aHTMLProvider);
    }
    catch (final Throwable t)
    {
      // Do not show the exceptions that occur, when client cancels a request.
      if (!StreamUtils.isKnownEOFException (t))
      {
        s_aLogger.error ("Error running application", t);
        // Catch Exception and re-throw
        if (t instanceof ServletException)
          throw (ServletException) t;
        throw new ServletException (t);
      }
    }
    finally
    {
      // Invoke all "request end" listener
      for (final IApplicationRequestListenerSPI aListener : m_aListeners)
        try
        {
          aListener.onRequestEnd ();
        }
        catch (final Throwable t)
        {
          s_aLogger.error ("Failed to invoke onRequestEnd on " + aListener, t);
        }
    }

    // Protected method invocation
    onRequestEnd (aRequestScope);
  }
}
