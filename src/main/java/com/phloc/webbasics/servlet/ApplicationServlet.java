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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ServiceLoaderBackport;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.servlet.AbstractScopeAwareHttpServlet;
import com.phloc.webbasics.app.ApplicationRequestManager;
import com.phloc.webbasics.app.html.LayoutHTMLProvider;
import com.phloc.webbasics.app.html.HTMLResponseHelper;
import com.phloc.webbasics.spi.IApplicationRequestListenerSPI;

/**
 * Base servlet for the main application.
 * 
 * @author philip
 */
public class ApplicationServlet extends AbstractScopeAwareHttpServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ApplicationServlet.class);

  private final List <IApplicationRequestListenerSPI> m_aListeners;

  public ApplicationServlet ()
  {
    m_aListeners = ContainerHelper.newList (ServiceLoaderBackport.load (IApplicationRequestListenerSPI.class));
  }

  @OverrideOnDemand
  @Nonnull
  protected LayoutHTMLProvider createHTMLCreationManager ()
  {
    return new LayoutHTMLProvider ();
  }

  private void _run (@Nonnull final IRequestWebScope aRequestScope) throws ServletException
  {
    // Run default request initialization
    ApplicationRequestManager.onRequestBegin (aRequestScope);

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
      HTMLResponseHelper.createHTMLResponse (aRequestScope, createHTMLCreationManager ());
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
  }

  @Override
  protected void onGet (@Nonnull final HttpServletRequest aRequest,
                        @Nonnull final HttpServletResponse aResponse,
                        @Nonnull final IRequestWebScope aRequestScope) throws ServletException
  {
    _run (aRequestScope);
  }

  @Override
  protected void onPost (@Nonnull final HttpServletRequest aRequest,
                         @Nonnull final HttpServletResponse aResponse,
                         @Nonnull final IRequestWebScope aRequestScope) throws ServletException
  {
    _run (aRequestScope);
  }
}
