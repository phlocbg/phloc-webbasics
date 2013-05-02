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

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import php.java.bridge.ISession;
import php.java.bridge.http.IContext;

/**
 * Create session contexts for servlets.
 * <p>
 * In addition to the standard ContextFactory this factory keeps a reference to
 * the HttpServletRequest.
 * 
 * @see php.java.bridge.http.ContextFactory
 * @see php.java.bridge.http.ContextServer
 */
public class SimpleServletContextFactory extends php.java.bridge.http.SimpleContextFactory
{
  protected HttpServletRequest proxy;
  protected final HttpServletRequest req;
  protected final HttpServletResponse res;
  protected final ServletContext kontext;
  protected final Servlet servlet;

  protected SimpleServletContextFactory (final Servlet servlet,
                                         final ServletContext ctx,
                                         final HttpServletRequest proxy,
                                         final HttpServletRequest req,
                                         final HttpServletResponse res,
                                         final boolean isManaged)
  {
    super (ServletUtil.getRealPath (ctx, ""), isManaged);
    this.kontext = ctx;
    this.proxy = proxy;
    this.req = req;
    this.res = res;
    this.servlet = servlet;
  }

  /**
   * Set the HttpServletRequest for session sharing. This implementation does
   * nothing, the proxy must have been set in the constructor.
   * 
   * @see php.java.servlet.RemoteServletContextFactory#setSessionFactory(HttpServletRequest)
   * @param req
   *        The HttpServletRequest
   */
  protected void setSessionFactory (final HttpServletRequest req)
  {}

  /** {@inheritDoc} */
  @Override
  public ISession getSimpleSession (final String name, final short clientIsNew, final int timeout)
  {
    throw new IllegalStateException ("Named sessions not supported by servlet.");
  }

  /** {@inheritDoc} */
  @Override
  public ISession getSession (final String name, final short clientIsNew, final int timeout)
  {
    // if name != null return a "named" php session which is not shared with jsp
    if (name != null)
      return getSimpleSession (name, clientIsNew, timeout);

    if (session != null)
      return session;

    if (proxy == null)
      throw new NullPointerException ("This context " + getId () + " doesn't have a session proxy.");
    return session = HttpSessionFacade.getFacade (this, proxy, clientIsNew, timeout);
  }

  /** {@inheritDoc} */
  @Override
  public synchronized void destroy ()
  {
    super.destroy ();
    proxy = null;
  }

  /**
   * Return an emulated JSR223 context.
   * 
   * @return The context.
   * @see php.java.servlet.HttpContext
   */
  @Override
  public IContext createContext ()
  {
    final IContext ctx = new HttpContext (kontext, req, res);
    ctx.setAttribute (IContext.SERVLET_CONTEXT, kontext, IContext.ENGINE_SCOPE);
    ctx.setAttribute (IContext.SERVLET_CONFIG, servlet.getServletConfig (), IContext.ENGINE_SCOPE);
    ctx.setAttribute (IContext.SERVLET, servlet, IContext.ENGINE_SCOPE);

    ctx.setAttribute (IContext.SERVLET_REQUEST, req, IContext.ENGINE_SCOPE);
    ctx.setAttribute (IContext.SERVLET_RESPONSE, new SimpleHttpServletResponse (res), IContext.ENGINE_SCOPE);
    return ctx;
  }

  /** Only for internal use */
  public static void throwJavaSessionException ()
  {
    throw new IllegalStateException ("Cannot call java_session() anymore. Response headers already sent! java_session() must be called at the beginning of the php script. Please add \"java_session();\" to the beginning of your PHP script.");
  }

  /**
   * Return the http session handle;
   * 
   * @throws IllegalStateException
   *         if java_session has not been called at the beginning of the PHP
   *         script
   * @return The session handle
   */
  public HttpSession getSession ()
  {
    if (session != null)
      return ((HttpSessionFacade) session).getCachedSession ();
    throwJavaSessionException ();
    return null;
  }
}
