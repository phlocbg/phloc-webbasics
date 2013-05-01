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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import php.java.bridge.Util;
import php.java.bridge.http.WriterOutputStream;

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
 * Miscellaneous servlet functions.
 * 
 * @author jostb
 */
public class ServletUtil
{
  private ServletUtil ()
  {}

  /**
   * Identical to context2.getRealPath(pathInfoCGI). On BEA WebLogic, which has
   * a broken getRealPath() implementation, we use
   * context2.getResource(pathInfoCGI)) instead.
   * 
   * @param context2
   *        The servlet context.
   * @param pathInfoCGI
   *        may be "" or "/" for example.
   * @return a valid path or null
   */
  public static String getRealPath (final ServletContext context2, String pathInfoCGI)
  {
    String ret = context2.getRealPath (pathInfoCGI);
    if (ret != null)
      return ret;

    // The following is the workaround for BEA WebLogic
    if (!pathInfoCGI.startsWith ("/"))
    {
      pathInfoCGI = "/" + pathInfoCGI;
    }
    URL url = null;
    try
    {
      url = context2.getResource (pathInfoCGI);
    }
    catch (final MalformedURLException e)
    {
      Util.printStackTrace (e);
    }
    if (url != null && !"file".equals (url.getProtocol ()))
      url = null;
    if (url == null)
      throw new IllegalStateException ("Cannot access " +
                                       pathInfoCGI +
                                       " within the current web application. Please explode it: Unzip the application .war file into a directory and deploy the directory instead.");

    ret = url.getPath ();
    return ret.replace ('/', File.separatorChar);
  }

  /**
   * Only for internal use. Returns the port# of the local port
   * 
   * @param req
   *        The servlet request
   * @return The local port or the value from the server port variable.
   */
  public static int getLocalPort (final ServletRequest req)
  {
    int port = -1;
    try
    {
      port = req.getLocalPort ();
    }
    catch (final Throwable t)
    {/* ignore */}
    if (port <= 0)
      port = req.getServerPort ();
    return port;
  }

  public static boolean isJavaBridgeWc (final String contextPath)
  {
    return (contextPath != null && contextPath.endsWith ("JavaBridge"));
  }

  public static String nullsToBlanks (final String s)
  {
    return ServletUtil.nullsToString (s, "");
  }

  public static String nullsToString (final String couldBeNull, final String subForNulls)
  {
    return (couldBeNull == null ? subForNulls : couldBeNull);
  }

  public static String getHeaders (final StringBuffer buf, final Enumeration enumeration)
  {
    while (enumeration.hasMoreElements ())
    {
      buf.append (enumeration.nextElement ());
      if (enumeration.hasMoreElements ())
        buf.append ("; ");
    }
    final String result = buf.toString ();
    buf.setLength (0);
    return result;
  }

  public static OutputStream getServletOutputStream (final HttpServletResponse response) throws IOException
  {
    try
    {
      return response.getOutputStream ();
    }
    catch (final IllegalStateException e)
    {
      final WriterOutputStream out = new WriterOutputStream (response.getWriter ());
      out.setEncoding (response.getCharacterEncoding ());
      return out;
    }
  }

  /**
   * Return an mbean property. Example:
   * <code>Util.getMBeanProperty("*:type=ThreadPool,name=http*", "maxThreads")</code>
   * or
   * <code>Util.getMBeanProperty("*:ServiceModule=*,J2EEServer=*,name=JettyWebConnector,j2eeType=*", "maxThreads");</code>
   * 
   * @param pattern
   *        the pattern string
   * @param property
   *        the property key
   * @return the property value
   */
  public static int getMBeanProperty (final String pattern, final String property)
  {
    try
    {
      final Class objectNameClazz = Class.forName ("javax.management.ObjectName");
      final Constructor constructor = objectNameClazz.getConstructor (new Class [] { String.class });
      Object objectName = constructor.newInstance (new Object [] { pattern });

      Class clazz = Class.forName ("javax.management.MBeanServerFactory");
      Method method = clazz.getMethod ("findMBeanServer", new Class [] { String.class });
      final ArrayList servers = (ArrayList) method.invoke (clazz, new Object [] { null });
      final Object server = servers.get (0);

      final Class mBeanServerClazz = Class.forName ("javax.management.MBeanServer");
      clazz = Class.forName ("javax.management.QueryExp");
      method = mBeanServerClazz.getMethod ("queryMBeans", new Class [] { objectNameClazz, clazz });

      final Set s = (Set) method.invoke (server, new Object [] { objectName, null });
      final Iterator ii = s.iterator ();

      if (ii.hasNext ())
      {
        clazz = Class.forName ("javax.management.ObjectInstance");
        method = clazz.getMethod ("getObjectName", Util.ZERO_PARAM);
        objectName = method.invoke (ii.next (), Util.ZERO_ARG);

        method = mBeanServerClazz.getMethod ("getAttribute", new Class [] { objectNameClazz, String.class });
        final Object result = method.invoke (server, new Object [] { objectName, property });
        return Integer.parseInt (String.valueOf (result));
      }
    }
    catch (final Exception t)
    {
      if (Util.logLevel > 5)
        Util.printStackTrace (t);
    }
    return 0;
  }
}
