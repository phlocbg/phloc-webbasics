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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import php.java.bridge.http.IContext;

/**
 * A custom context, used when remote PHP scripts access the servlet. In this
 * case the HttpServletRequest, HttpServletResponse and ServletContext objects
 * are not available. However, the session object <em>is</em> available through
 * the RemoteHttpSession.
 * 
 * @author jostb
 */
public class RemoteContext extends HttpContext
{
  protected RemoteContext (final ServletContext kontext, final HttpServletRequest req, final HttpServletResponse res)
  {
    super (kontext, req, res);
  }

  /**
   * Return the response object
   * 
   * @return The response
   */
  @Override
  public Object getHttpServletResponse ()
  {
    return getAttribute (IContext.SERVLET_RESPONSE);
  }

  /**
   * Return the request object
   * 
   * @return The request
   */
  @Override
  public Object getHttpServletRequest ()
  {
    return getAttribute (IContext.SERVLET_REQUEST);
  }

  /**
   * Return the servlet
   * 
   * @return the servlet
   */
  @Override
  public Object getServlet ()
  {
    return getAttribute (IContext.SERVLET);
  }

  /**
   * Return the servlet config
   * 
   * @return the servlet config
   */
  @Override
  public Object getServletConfig ()
  {
    return getAttribute (IContext.SERVLET_CONFIG);
  }

  /**
   * Return the servlet context
   * 
   * @return the servlet context
   */
  @Override
  public Object getServletContext ()
  {
    return getAttribute (IContext.SERVLET_CONTEXT);
  }
}
