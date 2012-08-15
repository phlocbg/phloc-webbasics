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
package com.phloc.webbasics.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.string.StringHelper;
import com.phloc.webbasics.CWeb;
import com.phloc.webbasics.http.CHTTPHeader;

/**
 * Misc. helper method on {@link HttpServletRequest} objects.
 * 
 * @author philip
 */
@Immutable
public final class RequestHelper
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (RequestHelper.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final RequestHelper s_aInstance = new RequestHelper ();

  private RequestHelper ()
  {}

  /**
   * Return the path within the servlet mapping for the given request, i.e. the
   * part of the request's URL beyond the part that called the servlet, or "" if
   * the whole URL has been used to identify the servlet.
   * <p>
   * Detects include request URL if called within a RequestDispatcher include.
   * <p>
   * E.g.: servlet mapping = "/test/*"; request URI = "/test/a" -> "/a".
   * <p>
   * E.g.: servlet mapping = "/test"; request URI = "/test" -> "".
   * <p>
   * E.g.: servlet mapping = "/*.test"; request URI = "/a.test" -> "".
   * 
   * @param aHttpRequest
   *        current HTTP request
   * @return the path within the servlet mapping, or ""
   */
  @Nonnull
  public static String getPathWithinServlet (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");

    final String sPathWithinApp = getPathWithinServletContext (aHttpRequest);
    final String sServletPath = aHttpRequest.getServletPath ();
    if (sPathWithinApp.startsWith (sServletPath))
      return sPathWithinApp.substring (sServletPath.length ());

    // Special case: URI is different from servlet path.
    // Can happen e.g. with index page: URI="/", servletPath="/index.html"
    // Use servlet path in this case, as it indicates the actual target path.
    return sServletPath;
  }

  /**
   * Get the request URI without an eventually appended session
   * (";jsessionid=...")
   * 
   * @param aHttpRequest
   *        The HTTP request
   * @return The request URI without the optional session ID
   */
  @Nullable
  public static String getRequestURI (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");

    final String sRequestURI = aHttpRequest.getRequestURI ();
    if (StringHelper.hasNoText (sRequestURI))
      return sRequestURI;

    // Strip session ID parameter
    final int nIndex = sRequestURI.indexOf (';');
    return nIndex == -1 ? sRequestURI : sRequestURI.substring (0, nIndex);
  }

  /**
   * Get the request path info without an eventually appended session
   * (";jsessionid=...")
   * 
   * @param aHttpRequest
   *        The HTTP request
   * @return The request path info without the optional session ID
   */
  @Nullable
  public static String getPathInfo (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");

    final String sPathInfo = aHttpRequest.getPathInfo ();
    if (StringHelper.hasNoText (sPathInfo))
      return sPathInfo;

    // Strip session ID parameter
    final int nIndex = sPathInfo.indexOf (';');
    return nIndex == -1 ? sPathInfo : sPathInfo.substring (0, nIndex);
  }

  /**
   * Return the URI of the request within the servlet context.
   * 
   * @param aHttpRequest
   *        The HTTP request. May not be <code>null</code>.
   * @return the path within the web application and never <code>null</code>.
   */
  @Nonnull
  public static String getPathWithinServletContext (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");

    final String sContextPath = aHttpRequest.getContextPath ();
    final String sRequestURI = getRequestURI (aHttpRequest);
    if (StringHelper.hasNoText (sRequestURI))
    {
      // I just want to to know whether we get null or ""
      s_aLogger.info ("Having empty request URI '" + sRequestURI + "' from request " + aHttpRequest);
      return "/";
    }

    if (!sRequestURI.startsWith (sContextPath))
      return sRequestURI;

    // Normal case: URI contains context path.
    final String sPath = sRequestURI.substring (sContextPath.length ());
    return sPath.length () > 0 ? sPath : "/";
  }

  /**
   * Get the full URL (incl. protocol) and parameters of the passed request.<br>
   * 
   * <pre>
   * http://hostname.com/mywebapp/servlet/MyServlet/a/b;c=123?d=789
   * </pre>
   * 
   * @param aHttpRequest
   *        The request to use. May not be <code>null</code>.
   * @return The full URL.
   */
  @Nonnull
  @Nonempty
  public static String getURL (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("request");

    final StringBuilder aReqUrl = new StringBuilder (aHttpRequest.getRequestURL ());
    final String sQueryString = aHttpRequest.getQueryString (); // d=789
    if (sQueryString != null)
      aReqUrl.append ('?').append (sQueryString);
    return aReqUrl.toString ();
  }

  /**
   * Get the full URI (excl. protocol) and parameters of the passed request.<br>
   * Example:
   * 
   * <pre>
   * /mywebapp/servlet/MyServlet/a/b;c=123?d=789
   * </pre>
   * 
   * @param aHttpRequest
   *        The request to use. May not be <code>null</code>.
   * @return The full URI.
   */
  @Nonnull
  @Nonempty
  public static String getURI (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("request");

    final String sReqUrl = getRequestURI (aHttpRequest);
    final String sQueryString = aHttpRequest.getQueryString (); // d=789
    if (sQueryString != null)
      return sReqUrl + "?" + sQueryString;
    return sReqUrl;
  }

  /**
   * Get the full URL (incl. protocol) and parameters of the passed request but
   * by assembling all parameters manually.<br>
   * 
   * <pre>
   * http://hostname.com:80/mywebapp/servlet/MyServlet/a/b;c=123?d=789
   * </pre>
   * 
   * @param aHttpRequest
   *        The request to use. May not be <code>null</code>.
   * @return The full URL.
   */
  @Nonnull
  @Nonempty
  public static String getUrlExt (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("request");

    final String sScheme = aHttpRequest.getScheme (); // http
    final String sServerName = aHttpRequest.getServerName (); // hostname.com
    final int nServerPort = aHttpRequest.getServerPort (); // 80
    final String sContextPath = aHttpRequest.getContextPath (); // /mywebapp
    final String sServletPath = aHttpRequest.getServletPath (); // /servlet/MyServlet
    final String sPathInfo = aHttpRequest.getPathInfo (); // /a/b;c=123
    final String sQueryString = aHttpRequest.getQueryString (); // d=789

    int nDefaultPort = CWeb.DEFAULT_PORT_HTTP;
    if ("https".equals (sScheme))
      nDefaultPort = CWeb.DEFAULT_PORT_HTTPS;

    // Reconstruct original requesting URL
    final StringBuilder aURL = new StringBuilder (sScheme).append ("://").append (sServerName);
    if (nServerPort != nDefaultPort)
      aURL.append (':').append (nServerPort);
    aURL.append (sContextPath).append (sServletPath);
    if (sPathInfo != null)
      aURL.append (sPathInfo);
    if (sQueryString != null)
      aURL.append ('?').append (sQueryString);
    return aURL.toString ();
  }

  @Nullable
  public static String getHttpReferer (@Nonnull final HttpServletRequest aHttpRequest)
  {
    return aHttpRequest.getHeader (CHTTPHeader.REFERER);
  }

  /**
   * This is a utility method which avoids that all map values are enclosed in
   * an array. Jetty seems to create String arrays out of simple string values
   * 
   * @param aHttpRequest
   * @return A Map containing pure strings instead of string arrays with one
   *         item
   */
  @Nonnull
  @ReturnsMutableCopy
  public static Map <String, Object> getParameterMap (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("request");

    final Map <String, Object> aResult = new HashMap <String, Object> ();
    @SuppressWarnings ("unchecked")
    final Map <String, Object> aOriginalMap = aHttpRequest.getParameterMap ();

    for (final Map.Entry <String, Object> aEntry : aOriginalMap.entrySet ())
    {
      final String sKey = aEntry.getKey ();
      final Object aValue = aEntry.getValue ();
      if (aValue instanceof String [])
      {
        final String [] aArrayValue = (String []) aValue;
        if (aArrayValue.length > 1)
          aResult.put (sKey, aArrayValue);
        else
          if (aArrayValue.length == 1)
            aResult.put (sKey, aArrayValue[0]);
          else
            aResult.put (sKey, "");
      }
      else
        aResult.put (sKey, aValue);
    }
    return aResult;
  }
}
