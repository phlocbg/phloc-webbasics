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
package com.phloc.webscopes.factory;

import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.webscopes.domain.IApplicationWebScope;
import com.phloc.webscopes.domain.IGlobalWebScope;
import com.phloc.webscopes.domain.IRequestWebScope;
import com.phloc.webscopes.domain.ISessionApplicationWebScope;
import com.phloc.webscopes.domain.ISessionWebScope;
import com.phloc.webscopes.impl.ApplicationWebScope;
import com.phloc.webscopes.impl.GlobalWebScope;
import com.phloc.webscopes.impl.RequestWebScope;
import com.phloc.webscopes.impl.SessionApplicationWebScope;
import com.phloc.webscopes.impl.SessionWebScope;
import com.phloc.webscopes.impl.GlobalWebScope.IContextPathProvider;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Web version of the scope factory.
 * 
 * @author philip
 */
@NotThreadSafe
public class DefaultWebScopeFactory implements IWebScopeFactory
{
  private static final int SERVLET_SPEC_GETCONTEXTPATH_MAJOR = 2;
  private static final int SERVLET_SPEC_GETCONTEXTPATH_MINOR = 5;

  private static final class ConstantContextPathProvider implements IContextPathProvider
  {
    // Store in member to avoid dependency to outer method
    private final String m_sContextPath;

    public ConstantContextPathProvider (@Nonnull final String sContextPath)
    {
      // May not be null, but maybe an empty String!
      if (sContextPath == null)
        throw new NullPointerException ("contextPath may not be null!");
      m_sContextPath = sContextPath;
    }

    @Nonnull
    public String getContextPath ()
    {
      return m_sContextPath;
    }
  }

  private static final class ContextPathProviderFromRequest implements IContextPathProvider
  {
    private String m_sContextPath;

    public ContextPathProviderFromRequest ()
    {}

    public String getContextPath ()
    {
      if (m_sContextPath == null)
      {
        // Get the context path from the request scope
        // May throw an exception if no request scope is present so far
        m_sContextPath = WebScopeManager.getRequestScope ().getRequest ().getContextPath ();
      }
      return m_sContextPath;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultWebScopeFactory.class);

  public DefaultWebScopeFactory ()
  {}

  @Nonnull
  public IGlobalWebScope createGlobalScope (@Nonnull final ServletContext aServletContext)
  {
    if (aServletContext == null)
      throw new NullPointerException ("servletContext");

    IContextPathProvider aContextPathProvider = null;

    // Using getContextPath for Servlet API >= 2.5:
    if ((aServletContext.getMajorVersion () == SERVLET_SPEC_GETCONTEXTPATH_MAJOR && aServletContext.getMinorVersion () >= SERVLET_SPEC_GETCONTEXTPATH_MINOR) ||
        aServletContext.getMajorVersion () > SERVLET_SPEC_GETCONTEXTPATH_MAJOR)
    {
      try
      {
        final Method m = aServletContext.getClass ().getMethod ("getContextPath");
        // Servlet API >= 2.5 -> invoke once and store in member
        final String sContextPath = (String) m.invoke (aServletContext);
        aContextPathProvider = new ConstantContextPathProvider (sContextPath);
      }
      catch (final NoSuchMethodException ex)
      {
        // Ignore
        s_aLogger.error ("Failed to find Method getContextPath on " + aServletContext);
      }
      catch (final Exception ex)
      {
        // Ignore
        s_aLogger.error ("Failed to invoke getContextPath on " + aServletContext, ex);
      }
    }

    if (aContextPathProvider == null)
    {
      // e.g. Servlet API < 2.5
      // -> Take from request scope on first call
      aContextPathProvider = new ContextPathProviderFromRequest ();
    }

    return new GlobalWebScope (aServletContext, aContextPathProvider);
  }

  @Nonnull
  public IApplicationWebScope createApplicationScope (@Nonnull @Nonempty final String sScopeID)
  {
    return new ApplicationWebScope (sScopeID);
  }

  @Nonnull
  public ISessionWebScope createSessionScope (@Nonnull final HttpSession aHttpSession)
  {
    return new SessionWebScope (aHttpSession);
  }

  @Nonnull
  public ISessionApplicationWebScope createSessionApplicationScope (@Nonnull @Nonempty final String sScopeID)
  {
    return new SessionApplicationWebScope (sScopeID);
  }

  @Nonnull
  public IRequestWebScope createRequestScope (@Nonnull final HttpServletRequest aHttpRequest,
                                              @Nonnull final HttpServletResponse aHttpResponse)
  {
    return new RequestWebScope (aHttpRequest, aHttpResponse);
  }
}
