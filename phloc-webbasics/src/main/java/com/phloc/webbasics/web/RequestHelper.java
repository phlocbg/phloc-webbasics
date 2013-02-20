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
package com.phloc.webbasics.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.CheckForSigned;
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
import com.phloc.commons.string.StringParser;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.CWeb;
import com.phloc.webbasics.http.CHTTPHeader;
import com.phloc.webbasics.http.EHTTPVersion;
import com.phloc.webbasics.http.HTTPHeaderMap;

/**
 * Misc. helper method on {@link HttpServletRequest} objects.
 * 
 * @author philip
 */
@Immutable
public final class RequestHelper
{
  private static final String SCOPE_ATTR_REQUESTHELP_REQUESTPARAMMAP = "$requesthelp.requestparammap";

  private static final Logger s_aLogger = LoggerFactory.getLogger (RequestHelper.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final RequestHelper s_aInstance = new RequestHelper ();

  private RequestHelper ()
  {}

  /**
   * Get the request URI without an eventually appended session
   * (";jsessionid=...")
   * <table summary="Examples of Returned Values">
   * <tr align=left>
   * <th>First line of HTTP request</th>
   * <th>Returned Value</th>
   * <tr>
   * <td>POST /some/path.html;JSESSIONID=4711</td>
   * <td>/some/path.html</td>
   * </tr>
   * <tr>
   * <td>GET http://foo.bar/a.html;JSESSIONID=4711</td>
   * <td>/a.html</td>
   * </tr>
   * <tr>
   * <td>HEAD /xyz;JSESSIONID=4711?a=b</td>
   * <td>/xyz</td>
   * </tr>
   * </table>
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
   * @param aRequestScope
   *        The request web scope to use. May not be <code>null</code>.
   * @return the path within the web application and never <code>null</code>.
   */
  @Nonnull
  public static String getPathWithinServletContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    if (aRequestScope == null)
      throw new NullPointerException ("requestScope");

    return getPathWithinServletContext (aRequestScope.getRequest ());
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

    final String sRequestURI = getRequestURI (aHttpRequest);
    if (StringHelper.hasNoText (sRequestURI))
    {
      // I just want to to know whether we get null or ""
      s_aLogger.info ("Having empty request URI '" + sRequestURI + "' from request " + aHttpRequest);
      return "/";
    }

    final String sContextPath = aHttpRequest.getContextPath ();
    if (!sRequestURI.startsWith (sContextPath))
      return sRequestURI;

    // Normal case: URI contains context path.
    final String sPath = sRequestURI.substring (sContextPath.length ());
    return sPath.length () > 0 ? sPath : "/";
  }

  @Nonnull
  public static String getPathWithinServlet (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    if (aRequestScope == null)
      throw new NullPointerException ("requestScope");

    return getPathWithinServlet (aRequestScope.getRequest ());
  }

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

    final StringBuffer aReqUrl = aHttpRequest.getRequestURL ();
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
    final String sQueryString = aHttpRequest.getQueryString (); // d=789&x=y
    if (sQueryString != null)
      return sReqUrl + '?' + sQueryString;
    return sReqUrl;
  }

  @Nonnull
  @Nonempty
  public static String getUrlString (@Nonnull final String sScheme,
                                     @Nonnull final String sServerName,
                                     final int nServerPort,
                                     @Nullable final String sPath,
                                     @Nullable final String sQueryString)
  {
    int nDefaultPort = CWeb.DEFAULT_PORT_HTTP;
    if ("https".equals (sScheme))
      nDefaultPort = CWeb.DEFAULT_PORT_HTTPS;

    // Reconstruct URL
    final StringBuilder aURL = new StringBuilder (sScheme).append ("://").append (sServerName);
    if (nServerPort != nDefaultPort)
      aURL.append (':').append (nServerPort);

    if (StringHelper.hasText (sPath))
    {
      if (!StringHelper.startsWith (sPath, '/'))
        aURL.append ('/');
      aURL.append (sPath);
    }
    if (sQueryString != null)
      aURL.append ('?').append (sQueryString);
    return aURL.toString ();
  }

  @Nullable
  public static String getHttpReferer (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");

    return aHttpRequest.getHeader (CHTTPHeader.REFERER);
  }

  @Nullable
  public static EHTTPVersion getHttpVersion (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    if (aRequestScope == null)
      throw new NullPointerException ("requestScope");

    return getHttpVersion (aRequestScope.getRequest ());
  }

  /**
   * Get the HTTP version associated with the given HTTP request
   * 
   * @param aHttpRequest
   *        The http request to query. May not be <code>null</code>.
   * @return <code>null</code> if no supported HTTP version is contained
   */
  @Nullable
  public static EHTTPVersion getHttpVersion (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");

    return EHTTPVersion.getFromNameOrNull (aHttpRequest.getProtocol ());
  }

  /**
   * Get a complete request header map as a copy.
   * 
   * @param aRequestScope
   *        The source request scope. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static HTTPHeaderMap getRequestHeaderMap (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    if (aRequestScope == null)
      throw new NullPointerException ("requestScope");

    return getRequestHeaderMap (aRequestScope.getRequest ());
  }

  /**
   * Get a complete request header map as a copy.
   * 
   * @param aHttpRequest
   *        The source HTTP request. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static HTTPHeaderMap getRequestHeaderMap (@Nonnull final HttpServletRequest aHttpRequest)
  {
    final HTTPHeaderMap ret = new HTTPHeaderMap ();
    final Enumeration <?> eHeaders = aHttpRequest.getHeaderNames ();
    while (eHeaders.hasMoreElements ())
    {
      final String sName = (String) eHeaders.nextElement ();
      final Enumeration <?> eHeaderValues = aHttpRequest.getHeaders (sName);
      while (eHeaderValues.hasMoreElements ())
      {
        final String sValue = (String) eHeaderValues.nextElement ();
        ret.addHeader (sName, sValue);
      }
    }
    return ret;
  }

  /**
   * This is a utility method which avoids that all map values are enclosed in
   * an array. Jetty seems to create String arrays out of simple string values
   * 
   * @param aRequestScope
   *        The request scope to use. May not be <code>null</code>.
   * @return A Map containing pure strings instead of string arrays with one
   *         item
   */
  @Nonnull
  @ReturnsMutableCopy
  public static Map <String, Object> getParameterMap (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    if (aRequestScope == null)
      throw new NullPointerException ("requestScope");

    return getParameterMap (aRequestScope.getRequest ());
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

    // For all parameters
    for (final Map.Entry <String, Object> aEntry : aOriginalMap.entrySet ())
    {
      final String sKey = aEntry.getKey ();
      final Object aValue = aEntry.getValue ();
      if (aValue instanceof String [])
      {
        // It's an array value
        final String [] aArrayValue = (String []) aValue;
        if (aArrayValue.length > 1)
          aResult.put (sKey, aArrayValue);
        else
          if (aArrayValue.length == 1)
          {
            // Flatten array to String
            aResult.put (sKey, aArrayValue[0]);
          }
          else
            aResult.put (sKey, "");
      }
      else
      {
        // It's a single value
        aResult.put (sKey, aValue);
      }
    }
    return aResult;
  }

  @Nonnull
  public static IRequestParamMap getCurrentRequestParamMap ()
  {
    return getRequestParamMap (WebScopeManager.getRequestScope ());
  }

  @Nonnull
  public static IRequestParamMap getRequestParamMap (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    IRequestParamMap aMap = aRequestScope.getCastedAttribute (SCOPE_ATTR_REQUESTHELP_REQUESTPARAMMAP);
    if (aMap == null)
    {
      aMap = RequestParamMap.create (aRequestScope);
      aRequestScope.setAttribute (SCOPE_ATTR_REQUESTHELP_REQUESTPARAMMAP, aMap);
    }
    return aMap;
  }

  /**
   * Get the content length of the passed request. This is not done using
   * <code>request.getContentLength()</code> but instead parsing the HTTP header
   * field {@link CHTTPHeader#CONTENT_LENGTH} manually!
   * 
   * @param aHttpRequest
   *        Source HTTP request. May not be <code>null</code>.
   * @return -1 if no or an invalid content length is set in the header
   */
  @CheckForSigned
  public static long getContentLength (@Nonnull final HttpServletRequest aHttpRequest)
  {
    final String sContentLength = aHttpRequest.getHeader (CHTTPHeader.CONTENT_LENGTH);
    return StringParser.parseLong (sContentLength, -1L);
  }
}
