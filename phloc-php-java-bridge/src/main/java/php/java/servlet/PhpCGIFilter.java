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
/*-*- mode: Java; tab-width:8 -*-*/

package php.java.servlet;

/*
 * Copyright (C) 2003-2010 Jost Boekemeier
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import php.java.bridge.Util;

/**
 * Handles /foo/bar.php/baz?param=value requests.
 * <p>
 * Since the servlet spec doesn't allow
 * &lt;url-pattern&gt;*.php*&lt;/url-pattern&gt;, this filter searches for an
 * embedded PHP_SUFFIX and forwards to the PhpCGIServlet.
 * </p>
 * To enable this filter add <blockquote> <code>
 *     &lt;filter&gt;<br>
 *       &lt;filter-name&gt;PhpCGIFilter&lt;/filter-name&gt;<br>
 *       &lt;filter-class&gt;php.java.servlet.PhpCGIFilter&lt;/filter-class&gt;<br>
 *   &lt;/filter&gt;<br>
 *   &lt;filter-mapping&gt;<br>
 *       &lt;filter-name&gt;PhpCGIFilter&lt;/filter-name&gt;<br>
 *       &lt;url-pattern&gt;/*&lt;/url-pattern&gt;<br>
 *   &lt;/filter-mapping&gt;<br>
 * </code>
 * </blockquote>
 * 
 * @see php.java.servlet.fastcgi.FastCGIServlet
 */
public class PhpCGIFilter implements Filter
{

  /** The default suffix to search for. For example .php/ or .phtml/ */
  public static final String PHP_SUFFIX = ".php/";

  private String DOCUMENT_ROOT;

  /**
   * Return the PHP_SUFFIX. Override this method to return your own suffix.
   * 
   * @return the php suffix, defaults to ".php/"
   */
  public String getPhpSuffix ()
  {
    return PHP_SUFFIX;
  }

  /**
   * {@inheritDoc}
   */
  public void doFilter (final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
                                                                                                              ServletException
  {

    final String uri = ((HttpServletRequest) request).getRequestURI ();

    final String PHP_SUFFIX = getPhpSuffix ();
    final int PHP_SUFFIX_LEN = PHP_SUFFIX.length ();

    int idx = uri.indexOf (PHP_SUFFIX);
    if (idx != -1)
    {
      final String pathInfo = uri.substring (idx + PHP_SUFFIX_LEN - 1);
      final String pathTranslated = DOCUMENT_ROOT + pathInfo;
      final String dispatch = uri.substring (0, idx + PHP_SUFFIX_LEN - 1);

      final String servletPathOrig = ((HttpServletRequest) request).getServletPath ();
      idx = servletPathOrig.indexOf (PHP_SUFFIX);
      if (idx == -1)
      {
        Util.warn ("INTERNAL ERROR: " + servletPathOrig);
        chain.doFilter (request, response);
        return;
      }

      final String servletPath = servletPathOrig.substring (0, idx + PHP_SUFFIX_LEN - 1);
      final HttpServletRequest req = new HttpServletRequestWrapper ((HttpServletRequest) request)
      {

        @Override
        public String getPathInfo ()
        {
          return pathInfo;
        }

        @Override
        public String getPathTranslated ()
        {
          return pathTranslated;
        }

        @Override
        public String getServletPath ()
        {
          return servletPath;
        }

        @Override
        public String getRequestURI ()
        {
          return dispatch;
        }

        @Override
        public StringBuffer getRequestURL ()
        {
          try
          {
            return new StringBuffer (new java.net.URI (getScheme (),
                                                       null,
                                                       getServerName (),
                                                       getServerPort (),
                                                       getRequestURI (),
                                                       null,
                                                       null).toURL ().toExternalForm ());
          }
          catch (final MalformedURLException e)
          {
            e.printStackTrace ();
          }
          catch (final URISyntaxException e)
          {
            e.printStackTrace ();
          }
          return null;
        }
      };
      request.getRequestDispatcher (dispatch).forward (req, response);
    }
    else
    {
      chain.doFilter (request, response);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void init (final FilterConfig config) throws ServletException
  {
    DOCUMENT_ROOT = ServletUtil.getRealPath (config.getServletContext (), "");
  }

  /**
   * {@inheritDoc}
   */
  public void destroy ()
  {}
}
