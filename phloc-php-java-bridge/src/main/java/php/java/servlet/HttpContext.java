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
/*-*- mode: Java; tab-width:8 -*-*/

package php.java.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import php.java.bridge.NotImplementedException;
import php.java.bridge.Util;
import php.java.bridge.http.IContext;

/*
 * Copyright (C) 2003-2007 Jost Boekemeier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER(S) OR AUTHOR(S) BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * A custom context which keeps the HttpServletResponse. Used when JSR223 is not
 * available.
 * 
 * @author jostb
 */
public class HttpContext extends php.java.bridge.http.Context
{
  protected HttpServletResponse response;
  protected ServletContext context;
  protected HttpServletRequest request;

  /** {@inheritDoc} */
  @Override
  public Object getAttribute (final String name) throws IllegalArgumentException
  {
    Object result;
    if (name == null)
    {
      throw new IllegalArgumentException ("name cannot be null");
    }

    if ((getEngineScope () != null) && (result = getEngineScope ().get (name)) != null)
    {
      return result;
    }
    else
      if ((getGlobalScope () != null) && (result = getGlobalScope ().get (name)) != null)
      {
        return result;
      }
      else
        if ((result = request.getAttribute (name)) != null)
        {
          return result;
        }
        else
          if ((result = request.getSession ().getAttribute (name)) != null)
          {
            return result;
          }
          else
            if ((result = context.getAttribute (name)) != null)
            {
              return result;
            }
    return null;
  }

  /**
   * Create a new context.
   * 
   * @param kontext
   *        The servlet context
   * @param req
   *        The servlet request
   * @param res
   *        The HttpServletResponse
   */
  public HttpContext (final ServletContext kontext, final HttpServletRequest req, final HttpServletResponse res)
  {
    this.context = kontext;
    this.response = res;
    this.request = req;
  }

  /** {@inheritDoc} */
  @Override
  public Writer getWriter () throws IOException
  {
    return response.getWriter ();
  }

  /**
   * Return the http servlet response
   * 
   * @return The http servlet reponse
   */
  @Override
  public Object getHttpServletResponse ()
  {
    return getAttribute (IContext.SERVLET_RESPONSE);
  }

  /**
   * Return the http servlet request
   * 
   * @return The http servlet request
   */
  @Override
  public Object getHttpServletRequest ()
  {
    return getAttribute (IContext.SERVLET_REQUEST);
  }

  /**
   * Return the http servlet
   * 
   * @return The http servlet
   */
  @Override
  public Object getServlet ()
  {
    return getAttribute (IContext.SERVLET);
  }

  /**
   * Return the servlet config
   * 
   * @return The servlet config
   */
  @Override
  public Object getServletConfig ()
  {
    return getAttribute (IContext.SERVLET_CONFIG);
  }

  /**
   * Return the servlet context
   * 
   * @return The servlet context
   */
  @Override
  public Object getServletContext ()
  {
    return getAttribute (IContext.SERVLET_CONTEXT);
  }

  /**
   * Only for internal use. <br>
   * <br>
   * Used when scripts are running within of a servlet environment: Either
   * php.java.servlet.Context or the JSR223 Context (see
   * PhpSimpleHttpScriptContext).<br>
   * Outside of a servlet environment use the ContextLoaderListener instead:
   * Either the Standalone or the JSR223 Standalone (see PhpScriptContext).
   * 
   * @param closeable
   *        The manageable beforeShutdown(), will be called by the
   *        {@link ContextLoaderListener#contextDestroyed(javax.servlet.ServletContextEvent)}
   * @param ctx
   *        The ServletContext
   */
  public static void handleManaged (final Object closeable, final ServletContext ctx)
  {
    final List <Object> list = ContextLoaderListener.getContextLoaderListener (ctx).getCloseables ();
    list.add (closeable);
  }

  /** {@inheritDoc} */
  @Override
  public Object init (final Object callable) throws Exception
  {
    if (Util.logLevel > 3)
      Util.logDebug ("calling servlet context init");
    return php.java.bridge.http.Context.getManageable (callable);
  }

  /** {@inheritDoc} */
  @Override
  public void onShutdown (final Object closeable)
  {
    if (Util.logLevel > 3)
      Util.logDebug ("calling servlet context register shutdown ");
    handleManaged (closeable, context);
  }

  /**
   * Only for internal use
   * 
   * @param path
   *        the path
   * @param ctx
   *        the servlet context
   * @return the real path
   * @deprecated Use
   *             {@link php.java.servlet.ServletUtil#getRealPath(ServletContext, String)}
   */
  @Deprecated
  public static String getRealPathInternal (final String path, final ServletContext ctx)
  {
    return ServletUtil.getRealPath (ctx, path);
  }

  /** {@inheritDoc} */
  @Override
  public String getRealPath (final String path)
  {
    return ServletUtil.getRealPath (context, path);
  }

  /** @deprecated */
  @Deprecated
  @Override
  public String getRedirectString ()
  {
    throw new NotImplementedException ();
  }

  /** @deprecated */
  @Deprecated
  @Override
  public String getRedirectString (final String webPath)
  {
    throw new NotImplementedException ();
  }

  /** {@inheritDoc} */
  @Override
  public String getSocketName ()
  {
    return String.valueOf (ServletUtil.getLocalPort (request));
  }
}
