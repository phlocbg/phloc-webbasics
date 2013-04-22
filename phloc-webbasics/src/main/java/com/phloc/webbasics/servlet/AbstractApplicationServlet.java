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
package com.phloc.webbasics.servlet;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.app.html.IHTMLProvider;
import com.phloc.webbasics.app.html.WebHTMLCreator;
import com.phloc.webbasics.spi.IApplicationRequestListenerSPI;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.servlets.AbstractUnifiedResponseServlet;

/**
 * Base servlet for the main application.
 * 
 * @author Philip Helger
 */
public abstract class AbstractApplicationServlet extends AbstractUnifiedResponseServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractApplicationServlet.class);

  private final List <IApplicationRequestListenerSPI> m_aListeners;

  public AbstractApplicationServlet ()
  {
    m_aListeners = ServiceLoaderUtils.getAllSPIImplementations (IApplicationRequestListenerSPI.class);
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  protected void onRequestBegin (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    // Run default request initialization (menu item and locale)
    ApplicationRequestManager.getInstance ().onRequestBegin (aRequestScope);

    // Invoke all "request begin" listener
    for (final IApplicationRequestListenerSPI aListener : m_aListeners)
      try
      {
        aListener.onRequestBegin (aRequestScope);
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Failed to invoke onRequestBegin on " + aListener, t);
      }
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  protected void onRequestEnd (final boolean bExceptionOccurred)
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

  /**
   * @param aRequestScope
   *        The request scope
   * @return The HTML provider that creates the content.
   */
  @OverrideOnDemand
  @Nonnull
  protected abstract IHTMLProvider createHTMLProvider (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope);

  @Override
  protected final void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                      @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException
  {
    try
    {
      // Who is responsible for creating the HTML?
      final IHTMLProvider aHTMLProvider = createHTMLProvider (aRequestScope);
      WebHTMLCreator.createHTMLResponse (aRequestScope, aUnifiedResponse, aHTMLProvider);
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
  }
}
