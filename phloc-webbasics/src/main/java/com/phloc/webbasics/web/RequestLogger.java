package com.phloc.webbasics.web;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ComparatorAsString;

/**
 * Helper class to debug information passed to a JSP page or a servlet.
 * 
 * @author philip
 */
@Immutable
public final class RequestLogger
{
  /** The logger to use. */
  private static final Logger s_aLogger = LoggerFactory.getLogger (RequestLogger.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final RequestLogger s_aInstance = new RequestLogger ();

  private RequestLogger ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public static Map <String, String> getRequestFieldMap (@Nonnull final HttpServletRequest aHttpRequest)
  {
    final Map <String, String> ret = new LinkedHashMap <String, String> ();
    ret.put ("AuthType", aHttpRequest.getAuthType ());
    ret.put ("CharacterEncoding", aHttpRequest.getCharacterEncoding ());
    ret.put ("ContentLength", Integer.toString (aHttpRequest.getContentLength ()));
    ret.put ("ContentType", aHttpRequest.getContentType ());
    ret.put ("ContextPath", aHttpRequest.getContextPath ());
    ret.put ("LocalAddr", aHttpRequest.getLocalAddr ());
    ret.put ("LocalName", aHttpRequest.getLocalName ());
    ret.put ("LocalPort", Integer.toString (aHttpRequest.getLocalPort ()));
    ret.put ("Method", aHttpRequest.getMethod ());
    ret.put ("PathInfo", aHttpRequest.getPathInfo ());
    ret.put ("PathTranslated", aHttpRequest.getPathTranslated ());
    ret.put ("Protocol", aHttpRequest.getProtocol ());
    ret.put ("QueryString", aHttpRequest.getQueryString ());
    ret.put ("RemoteAddr", aHttpRequest.getRemoteAddr ());
    ret.put ("RemoteHost", aHttpRequest.getRemoteHost ());
    ret.put ("RemotePort", Integer.toString (aHttpRequest.getRemotePort ()));
    ret.put ("RemoteUser", aHttpRequest.getRemoteUser ());
    ret.put ("Scheme", aHttpRequest.getScheme ());
    ret.put ("ServerName", aHttpRequest.getServerName ());
    ret.put ("ServerPort", Integer.toString (aHttpRequest.getServerPort ()));
    ret.put ("ServletPath", aHttpRequest.getServletPath ());
    final HttpSession aSession = aHttpRequest.getSession (false);
    if (aSession != null)
      ret.put ("SessionID", aSession.getId ());
    return ret;
  }

  @Nonnull
  public static StringBuilder getRequestFields (@Nonnull final HttpServletRequest aHttpRequest)
  {
    return getRequestFields (getRequestFieldMap (aHttpRequest));
  }

  @Nonnull
  public static StringBuilder getRequestFields (@Nonnull final Map <String, String> aRequestFieldMap)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("Request:\n");
    for (final Map.Entry <String, String> aEntry : aRequestFieldMap.entrySet ())
      aSB.append ("  ").append (aEntry.getKey ()).append (" = ").append (aEntry.getValue ()).append ('\n');
    return aSB;
  }

  /**
   * Debug log the most interesting parts of the HTTP request. It logs
   * everything but the HTTP headers.
   * 
   * @param aHttpRequest
   *        the {@link HttpServletRequest} to debug
   */
  public static void logRequestFields (@Nonnull final HttpServletRequest aHttpRequest)
  {
    s_aLogger.info (getRequestFields (aHttpRequest).toString ());
  }

  @Nonnull
  public static Map <String, String> getRequestHeaderMap (@Nonnull final HttpServletRequest aHttpRequest)
  {
    final Map <String, String> ret = new LinkedHashMap <String, String> ();
    final Enumeration <?> e = aHttpRequest.getHeaderNames ();
    while (e.hasMoreElements ())
    {
      final String sName = (String) e.nextElement ();
      ret.put (sName, aHttpRequest.getHeader (sName));
    }
    return ret;
  }

  @Nonnull
  public static StringBuilder getRequestHeader (@Nonnull final HttpServletRequest aHttpRequest)
  {
    return getRequestHeader (getRequestHeaderMap (aHttpRequest));
  }

  @Nonnull
  public static StringBuilder getRequestHeader (@Nonnull final Map <String, String> aRequestHeaderMap)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("Headers:\n");
    for (final Map.Entry <String, String> aEntry : aRequestHeaderMap.entrySet ())
      aSB.append ("  ").append (aEntry.getKey ()).append (" = ").append (aEntry.getValue ()).append ('\n');
    return aSB;
  }

  /**
   * Debug log information about the HTTP header send with a
   * {@link HttpServletRequest}.
   * 
   * @param aHttpRequest
   *        the servlet request to debug the HTTP headers from
   */
  public static void logRequestHeader (@Nonnull final HttpServletRequest aHttpRequest)
  {
    s_aLogger.info (getRequestHeader (aHttpRequest).toString ());
  }

  @Nonnull
  private static CharSequence _toString (@Nullable final Object o)
  {
    if (o == null)
      return "null";
    if (!o.getClass ().isArray ())
      return o.toString ();
    final StringBuilder aSB = new StringBuilder ('[');
    int i = 0;
    for (final Object aValue : (Object []) o)
    {
      if (i++ > 0)
        aSB.append (", ");
      aSB.append (_toString (aValue));
    }
    return aSB.append (']');
  }

  @Nonnull
  public static Map <String, String> getRequestParameterMap (@Nonnull final HttpServletRequest aHttpRequest)
  {
    final Map <String, String> ret = new LinkedHashMap <String, String> ();
    final Map <?, ?> aParams = aHttpRequest.getParameterMap ();
    for (final Entry <?, ?> aEntry : ContainerHelper.getSortedByKey (aParams, new ComparatorAsString ()).entrySet ())
      ret.put (aEntry.getKey ().toString (), _toString (aEntry.getValue ()).toString ());
    return ret;
  }

  @Nonnull
  public static StringBuilder getRequestParameters (@Nonnull final HttpServletRequest aHttpRequest)
  {
    return getRequestParameters (getRequestParameterMap (aHttpRequest));
  }

  @Nonnull
  public static StringBuilder getRequestParameters (@Nonnull final Map <String, String> aRequestParameterMap)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("Request parameters:\n");
    for (final Entry <String, String> aEntry : aRequestParameterMap.entrySet ())
      aSB.append ("  ").append (aEntry.getKey ()).append (" = '").append (aEntry.getValue ()).append ("'\n");
    return aSB;
  }

  public static void logRequestParameters (@Nonnull final HttpServletRequest aHttpRequest)
  {
    s_aLogger.info (getRequestParameters (aHttpRequest).toString ());
  }

  @Nonnull
  public static StringBuilder getRequestComplete (@Nonnull final HttpServletRequest aHttpRequest)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append (getRequestFields (aHttpRequest));
    aSB.append (getRequestHeader (aHttpRequest));
    aSB.append (getRequestParameters (aHttpRequest));
    return aSB;
  }

  public static void logRequestComplete (@Nonnull final HttpServletRequest aHttpRequest)
  {
    s_aLogger.info (getRequestComplete (aHttpRequest).toString ());
  }
}
