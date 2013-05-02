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

import php.java.bridge.http.ContextServer;
import php.java.bridge.http.IContextFactory;

/**
 * Create session contexts for servlets.
 * <p>
 * In addition to the standard ContextFactory this factory keeps a reference to
 * the HttpServletRequest.
 * 
 * @see php.java.bridge.http.ContextFactory
 * @see php.java.bridge.http.ContextServer
 */
public class ServletContextFactory extends SimpleServletContextFactory
{
  protected ServletContextFactory (final Servlet servlet,
                                   final ServletContext ctx,
                                   final HttpServletRequest proxy,
                                   final HttpServletRequest req,
                                   final HttpServletResponse res)
  {
    super (servlet, ctx, proxy, req, res, true);
  }

  /** {@inheritDoc} */
  @Override
  public synchronized void waitFor (final long timeout) throws InterruptedException
  {}

  /**
   * Create and add a new ContextFactory.
   * 
   * @param servlet
   *        The servlet
   * @param kontext
   *        The servlet context
   * @param proxy
   *        The proxied request
   * @param req
   *        The HttpServletRequest
   * @param res
   *        The HttpServletResponse
   * @return The created ContextFactory
   */
  public static IContextFactory addNew (final ContextServer server,
                                        final Servlet servlet,
                                        final ServletContext kontext,
                                        final HttpServletRequest proxy,
                                        final HttpServletRequest req,
                                        final HttpServletResponse res)
  {
    if (server.isAvailable (PhpJavaServlet.getHeader ("X_JAVABRIDGE_CHANNEL", req)))
      return new ServletContextFactory (servlet, kontext, proxy, req, res);

    return RemoteHttpServletContextFactory.addNew (servlet,
                                                   kontext,
                                                   proxy,
                                                   req,
                                                   res,
                                                   new ServletContextFactory (servlet, kontext, proxy, req, res));

  }
}
