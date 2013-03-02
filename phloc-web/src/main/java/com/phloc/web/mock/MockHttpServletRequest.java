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
package com.phloc.web.mock;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.phloc.commons.IHasLocale;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.multimap.IMultiMapSetBased;
import com.phloc.commons.collections.multimap.MultiHashMapLinkedHashSetBased;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.system.SystemHelper;
import com.phloc.commons.url.URLUtils;
import com.phloc.web.CWeb;
import com.phloc.web.http.CHTTPHeader;
import com.phloc.web.http.EHTTPMethod;
import com.phloc.web.http.EHTTPVersion;
import com.phloc.web.servlet.request.RequestHelper;

// ESCA-JAVA0116:
/**
 * Mock implementation of {@link HttpServletRequest}.
 * 
 * @author philip
 */
@NotThreadSafe
public class MockHttpServletRequest implements HttpServletRequest, IHasLocale
{
  public static final boolean DEFAULT_INVOKE_HTTP_LISTENER = true;
  public static final String DEFAULT_PROTOCOL = EHTTPVersion.HTTP_11.getName ();
  public static final String DEFAULT_SCHEME = CWeb.SCHEME_HTTP;
  public static final String DEFAULT_SERVER_ADDR = "127.0.0.1";
  public static final String DEFAULT_SERVER_NAME = "localhost";
  public static final int DEFAULT_SERVER_PORT = CWeb.DEFAULT_PORT_HTTP;
  public static final String DEFAULT_REMOTE_ADDR = "127.0.0.1";
  public static final String DEFAULT_REMOTE_HOST = "localhost";
  public static final EHTTPMethod DEFAULT_METHOD = EHTTPMethod.GET;

  private boolean m_bInvalidated = false;
  private boolean m_bActive = true;
  private final Map <String, Object> m_aAttributes = new HashMap <String, Object> ();
  private String m_sCharacterEncoding;
  private byte [] m_aContent;
  private String m_sContentType;
  private final Map <String, String []> m_aParameters = new LinkedHashMap <String, String []> (16);
  private String m_sProtocol = DEFAULT_PROTOCOL;
  private String m_sScheme = DEFAULT_SCHEME;
  private String m_sServerName = DEFAULT_SERVER_NAME;
  private int m_nServerPort = DEFAULT_SERVER_PORT;
  private final List <Locale> m_aLocales = new ArrayList <Locale> ();
  private boolean m_bSecure = false;
  private final ServletContext m_aServletContext;
  private String m_sRemoteAddr = DEFAULT_REMOTE_ADDR;
  private String m_sRemoteHost = DEFAULT_REMOTE_HOST;
  private int m_nRemotePort = DEFAULT_SERVER_PORT;
  private String m_sLocalName = DEFAULT_SERVER_NAME;
  private String m_sLocalAddr = DEFAULT_SERVER_ADDR;
  private int m_nLocalPort = DEFAULT_SERVER_PORT;
  private String m_sAuthType;
  private Cookie [] m_aCookies;
  private final IMultiMapSetBased <String, Object> m_aHeaders = new MultiHashMapLinkedHashSetBased <String, Object> ();
  private EHTTPMethod m_eMethod;
  private String m_sPathInfo;
  private String m_sContextPath = "";
  private String m_sQueryString;
  private String m_sRemoteUser;
  private final Set <String> m_aUserRoles = new HashSet <String> ();
  private Principal m_aUserPrincipal;
  private String m_sRequestURI;
  private String m_sServletPath = "";
  private HttpSession m_aSession;
  private String m_sSessionID;
  private boolean m_bRequestedSessionIDValid = true;
  private boolean m_bRequestedSessionIDFromCookie = true;
  private boolean m_bRequestedSessionIDFromURL = false;

  /**
   * Create a new MockHttpServletRequest with a default
   * {@link MockServletContext}.
   * 
   * @see MockServletContext
   */
  public MockHttpServletRequest ()
  {
    // No servlet context present -> no listeners
    this (null, false);
  }

  /**
   * Create a new MockHttpServletRequest.
   * 
   * @param aServletContext
   *        the ServletContext that the request runs in (may be
   *        <code>null</code> to use a default MockServletContext)
   * @see MockServletContext
   */
  public MockHttpServletRequest (@Nullable final ServletContext aServletContext)
  {
    this (aServletContext, DEFAULT_INVOKE_HTTP_LISTENER);
  }

  /**
   * Create a new MockHttpServletRequest.
   * 
   * @param aServletContext
   *        the ServletContext that the request runs in (may be
   *        <code>null</code> to use a default MockServletContext)
   * @param bInvokeHttpListeners
   *        <code>true</code> to invoke the HTTP listeners, <code>false</code>
   *        to not do it
   * @see MockServletContext
   */
  public MockHttpServletRequest (@Nullable final ServletContext aServletContext, final boolean bInvokeHttpListeners)
  {
    this (aServletContext, DEFAULT_METHOD, "", null, bInvokeHttpListeners);
  }

  /**
   * Create a new MockHttpServletRequest.
   * 
   * @param eMethod
   *        the request method (may be <code>null</code>)
   * @param sRequestURI
   *        the request URI (may be <code>null</code>)
   * @see #setMethod
   * @see #setRequestURI
   * @see MockServletContext
   */
  public MockHttpServletRequest (@Nullable final EHTTPMethod eMethod, @Nullable final String sRequestURI)
  {
    // No servlet context present -> no listeners
    this (null, eMethod, sRequestURI, null, false);
  }

  /**
   * Create a new MockHttpServletRequest.
   * 
   * @param aServletContext
   *        the ServletContext that the request runs in (may be
   *        <code>null</code> to use a default MockServletContext)
   * @param eMethod
   *        the request method (may be <code>null</code>)
   * @param sRequestURI
   *        the request URI (may be <code>null</code>)
   * @see #setMethod
   * @see #setRequestURI
   * @see MockServletContext
   */
  public MockHttpServletRequest (@Nullable final ServletContext aServletContext,
                                 @Nullable final EHTTPMethod eMethod,
                                 @Nullable final String sRequestURI)
  {
    this (aServletContext, eMethod, sRequestURI, null, DEFAULT_INVOKE_HTTP_LISTENER);
  }

  /**
   * Create a new MockHttpServletRequest.
   * 
   * @param aServletContext
   *        the ServletContext that the request runs in (may be
   *        <code>null</code> to use a default MockServletContext)
   * @param eMethod
   *        the request method (may be <code>null</code>)
   * @param sRequestURI
   *        the request URI (may be <code>null</code>)
   * @param aParams
   *        request parameters
   * @see #setMethod
   * @see #setRequestURI
   * @see MockServletContext
   */
  public MockHttpServletRequest (@Nullable final ServletContext aServletContext,
                                 @Nullable final EHTTPMethod eMethod,
                                 @Nullable final String sRequestURI,
                                 @Nullable final Map <String, String> aParams)
  {
    this (aServletContext, eMethod, sRequestURI, aParams, DEFAULT_INVOKE_HTTP_LISTENER);
  }

  /**
   * Create a new MockHttpServletRequest.
   * 
   * @param aServletContext
   *        the ServletContext that the request runs in (may be
   *        <code>null</code> to use a default MockServletContext)
   * @param eMethod
   *        the request method (may be <code>null</code>)
   * @param sRequestURI
   *        the request URI (may be <code>null</code>)
   * @param aParams
   *        request parameters
   * @param bInvokeHttpListeners
   *        if <code>true</code> than the HTTP request event listeners from
   *        {@link MockHttpListener} are triggered
   * @see #setMethod
   * @see #setRequestURI
   * @see MockServletContext
   */
  public MockHttpServletRequest (@Nullable final ServletContext aServletContext,
                                 @Nullable final EHTTPMethod eMethod,
                                 @Nullable final String sRequestURI,
                                 @Nullable final Map <String, String> aParams,
                                 final boolean bInvokeHttpListeners)
  {
    m_aServletContext = aServletContext;
    m_eMethod = eMethod;
    m_sRequestURI = sRequestURI;
    m_aLocales.add (Locale.ENGLISH);

    // Add default HTTP header
    addHeader (CHTTPHeader.USER_AGENT, getClass ().getName ());
    // Disable GZip and Deflate!
    addHeader (CHTTPHeader.ACCEPT_ENCODING, "*, gzip;q=0, x-gzip;q=0, deflate;q=0, compress;q=0, x-compress;q=0");
    addHeader (CHTTPHeader.ACCEPT_CHARSET, "*");
    if (aParams != null)
      for (final Map.Entry <String, String> aEntry : aParams.entrySet ())
        addParameter (aEntry.getKey (), aEntry.getValue ());

    if (aServletContext != null && bInvokeHttpListeners)
    {
      // Invoke all HTTP event listener
      final ServletRequestEvent aSRE = new ServletRequestEvent (aServletContext, this);
      for (final ServletRequestListener aListener : MockHttpListener.getAllServletRequestListeners ())
        aListener.requestInitialized (aSRE);
    }
  }

  /**
   * @return the ServletContext that this request is associated with. (Not
   *         available in the standard HttpServletRequest interface for some
   *         reason.). Never <code>null</code>.
   */
  @Nonnull
  public final ServletContext getServletContext ()
  {
    if (m_aServletContext == null)
      throw new IllegalStateException ("No servlet context present!");
    return m_aServletContext;
  }

  /**
   * @return whether this request is still active (that is, not completed yet).
   */
  public boolean isActive ()
  {
    return m_bActive;
  }

  /**
   * Mark this request as completed, keeping its state.
   */
  public void close ()
  {
    m_bActive = false;
  }

  /**
   * Invalidate this request, clearing its state and invoking all HTTP event
   * listener.
   * 
   * @see #close()
   * @see #clearAttributes()
   */
  public void invalidate ()
  {
    if (m_bInvalidated)
      throw new IllegalArgumentException ("Request scope already invalidated!");
    m_bInvalidated = true;

    if (m_aServletContext != null)
    {
      final ServletRequestEvent aSRE = new ServletRequestEvent (m_aServletContext, this);
      for (final ServletRequestListener aListener : MockHttpListener.getAllServletRequestListeners ())
        aListener.requestDestroyed (aSRE);
    }

    close ();
    clearAttributes ();
  }

  /**
   * Check whether this request is still active (that is, not completed yet),
   * throwing an IllegalStateException if not active anymore.
   */
  protected void checkActive ()
  {
    if (!m_bActive)
      throw new IllegalStateException ("Request is not active anymore");
  }

  @Nullable
  public Object getAttribute (@Nullable final String sName)
  {
    checkActive ();
    return m_aAttributes.get (sName);
  }

  @Nonnull
  public Enumeration <String> getAttributeNames ()
  {
    checkActive ();
    return ContainerHelper.getEnumeration (m_aAttributes.keySet ());
  }

  @Nullable
  public String getCharacterEncoding ()
  {
    return m_sCharacterEncoding;
  }

  public void setCharacterEncoding (@Nullable final String sCharacterEncoding)
  {
    m_sCharacterEncoding = sCharacterEncoding;
  }

  public void setContent (@Nullable final byte [] aContent)
  {
    m_aContent = ArrayHelper.getCopy (aContent);
    removeHeader (CHTTPHeader.CONTENT_LENGTH);
    addHeader (CHTTPHeader.CONTENT_LENGTH, Integer.toString (m_aContent.length));
  }

  @CheckForSigned
  public int getContentLength ()
  {
    return m_aContent != null ? m_aContent.length : -1;
  }

  public void setContentType (@Nullable final IMimeType aContentType)
  {
    setContentType (aContentType == null ? null : aContentType.getAsString ());
  }

  public void setContentType (@Nullable final String sContentType)
  {
    m_sContentType = sContentType;
    removeHeader (CHTTPHeader.CONTENT_TYPE);
    if (sContentType != null)
      addHeader (CHTTPHeader.CONTENT_TYPE, sContentType);
  }

  @Nullable
  public String getContentType ()
  {
    return m_sContentType;
  }

  /**
   * Note: do not change the content via {@link #setContent(byte[])}, while an
   * input stream is open, because this may lead to indeterministic results!
   * 
   * @return <code>null</code> if no content is present. If non-
   *         <code>null</code> the caller is responsible for closing the
   *         {@link InputStream}.
   */
  @Nullable
  public ServletInputStream getInputStream ()
  {
    if (m_aContent == null)
      return null;

    return new MockServletInputStream (m_aContent);
  }

  /**
   * Set a single value for the specified HTTP parameter.
   * <p>
   * If there are already one or more values registered for the given parameter
   * name, they will be replaced.
   * 
   * @param sName
   *        Parameter name
   * @param sValue
   *        Parameter value
   */
  public void setParameter (@Nonnull final String sName, @Nullable final String sValue)
  {
    setParameter (sName, new String [] { sValue });
  }

  /**
   * Set an array of values for the specified HTTP parameter.
   * <p>
   * If there are already one or more values registered for the given parameter
   * name, they will be replaced.
   * 
   * @param sName
   *        Parameter name
   * @param aValues
   *        Parameter values
   */
  public void setParameter (@Nonnull final String sName, @Nullable final String [] aValues)
  {
    if (sName == null)
      throw new NullPointerException ("Parameter name must not be null");
    m_aParameters.put (sName, aValues);
  }

  /**
   * Sets all provided parameters <emphasis>replacing</emphasis> any existing
   * values for the provided parameter names. To add without replacing existing
   * values, use {@link #addParameters(Map)}.
   * 
   * @param aParams
   *        Parameter name value map. May be <code>null</code>.
   */
  public void setParameters (@Nullable final Map <String, ? extends Object> aParams)
  {
    if (aParams != null)
      for (final Map.Entry <String, ? extends Object> aEntry : aParams.entrySet ())
      {
        final Object aValue = aEntry.getValue ();
        if (aValue instanceof String)
          setParameter (aEntry.getKey (), (String) aValue);
        else
          if (aValue instanceof String [])
            setParameter (aEntry.getKey (), (String []) aValue);
          else
            throw new IllegalArgumentException ("Unexpected parameter type: " +
                                                CGStringHelper.getSafeClassName (aValue));
      }
  }

  /**
   * Add a single value for the specified HTTP parameter.
   * <p>
   * If there are already one or more values registered for the given parameter
   * name, the given value will be added to the end of the list.
   * 
   * @param sName
   *        Parameter name
   * @param sValue
   *        Parameter value
   */
  public final void addParameter (@Nonnull final String sName, @Nullable final String sValue)
  {
    addParameter (sName, new String [] { sValue });
  }

  /**
   * Add an array of values for the specified HTTP parameter.
   * <p>
   * If there are already one or more values registered for the given parameter
   * name, the given values will be added to the end of the list.
   * 
   * @param sName
   *        Parameter name
   * @param aValues
   *        Parameter values
   */
  public final void addParameter (@Nonnull final String sName, @Nullable final String [] aValues)
  {
    if (sName == null)
      throw new NullPointerException ("Parameter name must not be null");

    final String [] aOldParams = m_aParameters.get (sName);
    m_aParameters.put (sName, ArrayHelper.getConcatenated (aOldParams, aValues));
  }

  /**
   * Adds all provided parameters <emphasis>without</emphasis> replacing any
   * existing values. To replace existing values, use
   * {@link #setParameters(Map)}.
   * 
   * @param aParams
   *        Parameter name value map
   */
  public void addParameters (@Nonnull final Map <String, ? extends Object> aParams)
  {
    if (aParams == null)
      throw new NullPointerException ("params");

    for (final Map.Entry <String, ? extends Object> aEntry : aParams.entrySet ())
    {
      final Object aValue = aEntry.getValue ();
      if (aValue instanceof String)
        addParameter (aEntry.getKey (), (String) aValue);
      else
        if (aValue instanceof String [])
          addParameter (aEntry.getKey (), (String []) aValue);
        else
          throw new IllegalArgumentException ("Unexpected parameter type: " + CGStringHelper.getSafeClassName (aValue));
    }
  }

  /**
   * Remove already registered values for the specified HTTP parameter, if any.
   * 
   * @param sName
   *        Parameter name
   */
  public void removeParameter (@Nonnull final String sName)
  {
    if (sName == null)
      throw new NullPointerException ("Parameter name must not be null");
    m_aParameters.remove (sName);
  }

  /**
   * Removes all existing parameters.
   */
  public void removeAllParameters ()
  {
    m_aParameters.clear ();
  }

  @Nullable
  public String getParameter (@Nonnull final String sName)
  {
    if (sName == null)
      throw new NullPointerException ("Parameter name must not be null");

    final String [] aParamValues = m_aParameters.get (sName);
    return ArrayHelper.getFirst (aParamValues);
  }

  @Nonnull
  public Enumeration <String> getParameterNames ()
  {
    return ContainerHelper.getEnumeration (m_aParameters.keySet ());
  }

  @Nullable
  @ReturnsMutableCopy
  public String [] getParameterValues (@Nonnull final String sName)
  {
    if (sName == null)
      throw new NullPointerException ("Parameter name must not be null");
    return ArrayHelper.getCopy (m_aParameters.get (sName));
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String []> getParameterMap ()
  {
    return ContainerHelper.newMap (m_aParameters);
  }

  public void setProtocol (@Nullable final String sProtocol)
  {
    m_sProtocol = sProtocol;
  }

  @Nullable
  public String getProtocol ()
  {
    return m_sProtocol;
  }

  public void setScheme (@Nullable final String sScheme)
  {
    m_sScheme = sScheme;
  }

  @Nullable
  public String getScheme ()
  {
    return m_sScheme;
  }

  public void setServerName (@Nullable final String sServerName)
  {
    m_sServerName = sServerName;
  }

  @Nullable
  public String getServerName ()
  {
    return m_sServerName;
  }

  public void setServerPort (final int nServerPort)
  {
    m_nServerPort = nServerPort;
  }

  public int getServerPort ()
  {
    return m_nServerPort;
  }

  @Nullable
  public BufferedReader getReader ()
  {
    if (m_aContent == null)
      return null;

    final InputStream aIS = new NonBlockingByteArrayInputStream (m_aContent);
    final Reader aReader = m_sCharacterEncoding != null ? StreamUtils.createReader (aIS, m_sCharacterEncoding)
                                                       : StreamUtils.createReader (aIS,
                                                                                   SystemHelper.getSystemCharsetName ());
    return new BufferedReader (aReader);
  }

  public void setRemoteAddr (@Nullable final String sRemoteAddr)
  {
    m_sRemoteAddr = sRemoteAddr;
  }

  @Nullable
  public String getRemoteAddr ()
  {
    return m_sRemoteAddr;
  }

  public void setRemoteHost (@Nullable final String sRemoteHost)
  {
    m_sRemoteHost = sRemoteHost;
  }

  @Nullable
  public String getRemoteHost ()
  {
    return m_sRemoteHost;
  }

  public void setAttribute (@Nonnull final String sName, @Nullable final Object aValue)
  {
    checkActive ();
    if (sName == null)
      throw new NullPointerException ("name");
    if (aValue != null)
      m_aAttributes.put (sName, aValue);
    else
      m_aAttributes.remove (sName);
  }

  public void removeAttribute (@Nonnull final String sName)
  {
    if (sName == null)
      throw new NullPointerException ("name");
    checkActive ();
    m_aAttributes.remove (sName);
  }

  /**
   * Clear all of this request's attributes.
   */
  public void clearAttributes ()
  {
    m_aAttributes.clear ();
  }

  /**
   * Add a new preferred locale, before any existing locales.
   * 
   * @param aLocale
   *        preferred locale
   */
  public void addPreferredLocale (@Nonnull final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ("Locale must not be null");
    m_aLocales.add (0, aLocale);
  }

  @Nonnull
  public Locale getLocale ()
  {
    // One element is added in ctor!
    return m_aLocales.get (0);
  }

  @Nonnull
  @Nonempty
  public Enumeration <Locale> getLocales ()
  {
    return ContainerHelper.getEnumeration (m_aLocales);
  }

  public void setSecure (final boolean bSecure)
  {
    m_bSecure = bSecure;
  }

  public boolean isSecure ()
  {
    return m_bSecure;
  }

  @Nonnull
  public RequestDispatcher getRequestDispatcher (@Nonnull final String sPath)
  {
    return new MockRequestDispatcher (sPath);
  }

  @Deprecated
  public String getRealPath (@Nonnull final String sPath)
  {
    return getServletContext ().getRealPath (sPath);
  }

  public void setRemotePort (final int nRemotePort)
  {
    m_nRemotePort = nRemotePort;
  }

  public int getRemotePort ()
  {
    return m_nRemotePort;
  }

  public void setLocalName (@Nullable final String sLocalName)
  {
    m_sLocalName = sLocalName;
  }

  @Nullable
  public String getLocalName ()
  {
    return m_sLocalName;
  }

  public void setLocalAddr (@Nullable final String sLocalAddr)
  {
    m_sLocalAddr = sLocalAddr;
  }

  @Nullable
  public String getLocalAddr ()
  {
    return m_sLocalAddr;
  }

  public void setLocalPort (final int nLocalPort)
  {
    m_nLocalPort = nLocalPort;
  }

  public int getLocalPort ()
  {
    return m_nLocalPort;
  }

  public void setAuthType (@Nullable final String sAuthType)
  {
    m_sAuthType = sAuthType;
  }

  @Nullable
  public String getAuthType ()
  {
    return m_sAuthType;
  }

  public void setCookies (@Nullable final Cookie [] aCookies)
  {
    m_aCookies = ArrayHelper.getCopy (aCookies);
  }

  @Nullable
  public Cookie [] getCookies ()
  {
    return ArrayHelper.getCopy (m_aCookies);
  }

  @Nullable
  private static String _unifyHeaderName (@Nullable final String s)
  {
    return s == null ? null : s.toLowerCase (Locale.US);
  }

  /**
   * Add a header entry for the given name.
   * <p>
   * If there was no entry for that header name before, the value will be used
   * as-is. In case of an existing entry, a String array will be created, adding
   * the given value (more specifically, its toString representation) as further
   * element.
   * <p>
   * Multiple values can only be stored as list of Strings, following the
   * Servlet spec (see <code>getHeaders</code> accessor). As alternative to
   * repeated <code>addHeader</code> calls for individual elements, you can use
   * a single call with an entire array or Collection of values as parameter.
   * 
   * @param sName
   *        header name
   * @param aValue
   *        header value
   * @see #getHeaderNames
   * @see #getHeader
   * @see #getHeaders
   * @see #getDateHeader
   * @see #getIntHeader
   */
  public final void addHeader (@Nullable final String sName, @Nullable final Object aValue)
  {
    m_aHeaders.putSingle (_unifyHeaderName (sName), aValue);
  }

  public void removeHeader (@Nullable final String sName)
  {
    m_aHeaders.remove (_unifyHeaderName (sName));
  }

  @UnsupportedOperation
  @Deprecated
  public long getDateHeader (@Nullable final String sName)
  {
    throw new UnsupportedOperationException ();
  }

  @UnsupportedOperation
  @Deprecated
  public int getIntHeader (@Nullable final String sName)
  {
    throw new UnsupportedOperationException ();
  }

  @Nullable
  public String getHeader (@Nullable final String sName)
  {
    final Collection <Object> aValue = m_aHeaders.get (_unifyHeaderName (sName));
    return aValue == null || aValue.isEmpty () ? null : String.valueOf (aValue.iterator ().next ());
  }

  @Nonnull
  public Enumeration <Object> getHeaders (@Nullable final String sName)
  {
    final Collection <Object> vals = m_aHeaders.get (_unifyHeaderName (sName));
    return ContainerHelper.getEnumeration (vals != null ? vals : ContainerHelper.newUnmodifiableList ());
  }

  @Nonnull
  public Enumeration <String> getHeaderNames ()
  {
    return ContainerHelper.getEnumeration (m_aHeaders.keySet ());
  }

  public void setMethod (@Nullable final EHTTPMethod eMethod)
  {
    m_eMethod = eMethod;
  }

  @Nullable
  public EHTTPMethod getMethodEnum ()
  {
    return m_eMethod;
  }

  @Nullable
  public String getMethod ()
  {
    return m_eMethod == null ? null : m_eMethod.getName ();
  }

  public void setPathInfo (@Nullable final String sPathInfo)
  {
    m_sPathInfo = sPathInfo;
  }

  @Nullable
  public String getPathInfo ()
  {
    return m_sPathInfo;
  }

  @Nullable
  public String getPathTranslated ()
  {
    return m_sPathInfo != null ? getRealPath (m_sPathInfo) : null;
  }

  public void setContextPath (@Nullable final String sContextPath)
  {
    m_sContextPath = sContextPath;
  }

  @Nullable
  public String getContextPath ()
  {
    return m_sContextPath;
  }

  public void setQueryString (@Nullable final String sQueryString)
  {
    m_sQueryString = sQueryString;
  }

  @Nullable
  public String getQueryString ()
  {
    return m_sQueryString;
  }

  public void setRemoteUser (@Nullable final String sRemoteUser)
  {
    m_sRemoteUser = sRemoteUser;
  }

  @Nullable
  public String getRemoteUser ()
  {
    return m_sRemoteUser;
  }

  public void addUserRole (@Nullable final String sRole)
  {
    m_aUserRoles.add (sRole);
  }

  public boolean isUserInRole (@Nullable final String sRole)
  {
    return m_aUserRoles.contains (sRole);
  }

  public void setUserPrincipal (@Nullable final Principal aUserPrincipal)
  {
    m_aUserPrincipal = aUserPrincipal;
  }

  @Nullable
  public Principal getUserPrincipal ()
  {
    return m_aUserPrincipal;
  }

  @Nullable
  public String getRequestedSessionId ()
  {
    return getSession (true).getId ();
  }

  public void setRequestURI (@Nullable final String sRequestURI)
  {
    m_sRequestURI = sRequestURI;
  }

  @Nullable
  public String getRequestURI ()
  {
    return m_sRequestURI;
  }

  @Nonnull
  public StringBuffer getRequestURL ()
  {
    return new StringBuffer ().append (RequestHelper.getFullServerName (m_sScheme, m_sServerName, m_nServerPort))
                              .append (getRequestURI ());
  }

  public void setServletPath (@Nullable final String sServletPath)
  {
    if (sServletPath != null && sServletPath.length () > 0 && !sServletPath.startsWith ("/"))
      throw new IllegalArgumentException ("servletPath must be empty or start with a slash!");
    m_sServletPath = sServletPath;
  }

  @Nullable
  public String getServletPath ()
  {
    return m_sServletPath;
  }

  /**
   * Define the session ID to be used when creating a new session
   * 
   * @param sSessionID
   *        The session ID to be used. If it is <code>null</code> a unique
   *        session ID is generated.
   */
  public void setSessionID (@Nullable final String sSessionID)
  {
    m_sSessionID = sSessionID;
  }

  /**
   * @return The session ID to use or <code>null</code> if a new session ID
   *         should be generated!
   */
  @Nullable
  public String getSessionID ()
  {
    return m_sSessionID;
  }

  public void setSession (@Nullable final HttpSession aHttpSession)
  {
    m_aSession = aHttpSession;
    if (aHttpSession instanceof MockHttpSession)
      ((MockHttpSession) aHttpSession).doAccess ();
  }

  @Nullable
  public HttpSession getSession (final boolean bCreate)
  {
    checkActive ();

    // Reset session if invalidated.
    if (m_aSession instanceof MockHttpSession && ((MockHttpSession) m_aSession).isInvalid ())
      m_aSession = null;

    // Create new session if necessary.
    if (m_aSession == null && bCreate)
      m_aSession = new MockHttpSession (getServletContext (), m_sSessionID);

    // Update last access time
    if (m_aSession instanceof MockHttpSession)
      ((MockHttpSession) m_aSession).doAccess ();

    return m_aSession;
  }

  @Nonnull
  public HttpSession getSession ()
  {
    return getSession (true);
  }

  public void setRequestedSessionIdValid (final boolean bRequestedSessionIdValid)
  {
    m_bRequestedSessionIDValid = bRequestedSessionIdValid;
  }

  public boolean isRequestedSessionIdValid ()
  {
    return m_bRequestedSessionIDValid;
  }

  public void setRequestedSessionIdFromCookie (final boolean bRequestedSessionIdFromCookie)
  {
    m_bRequestedSessionIDFromCookie = bRequestedSessionIdFromCookie;
  }

  public boolean isRequestedSessionIdFromCookie ()
  {
    return m_bRequestedSessionIDFromCookie;
  }

  public void setRequestedSessionIdFromURL (final boolean bRequestedSessionIdFromURL)
  {
    m_bRequestedSessionIDFromURL = bRequestedSessionIdFromURL;
  }

  public boolean isRequestedSessionIdFromURL ()
  {
    return m_bRequestedSessionIDFromURL;
  }

  @Deprecated
  public boolean isRequestedSessionIdFromUrl ()
  {
    return isRequestedSessionIdFromURL ();
  }

  /**
   * Set all path related members to the value to be deduced from the request
   * URI.
   * 
   * @return this
   * @see #setScheme(String)
   * @see #setServerName(String)
   * @see #setServerPort(int)
   * @see #setQueryString(String)
   * @see #setParameters(Map)
   */
  @Nonnull
  public MockHttpServletRequest setPathsFromRequestURI ()
  {
    if (StringHelper.hasText (m_sRequestURI) && m_aServletContext != null)
    {
      final URI aURI = URLUtils.getAsURI (RequestHelper.getWithoutSessionID (m_sRequestURI));
      if (aURI != null)
      {
        setScheme (aURI.getScheme ());
        setServerName (aURI.getHost ());
        setServerPort (RequestHelper.getServerPortToUse (aURI.getScheme (), aURI.getPort ()));
        String sPath = aURI.getPath ();

        // Context path
        final String sServletContextPath = m_aServletContext.getContextPath ();
        if (sServletContextPath.isEmpty () || StringHelper.startsWith (sPath, sServletContextPath))
        {
          setContextPath (sServletContextPath);
          sPath = sPath.substring (sServletContextPath.length ());
        }
        else
        {
          setContextPath ("");
        }

        // Servlet path
        final int nIndex = sPath.indexOf ('/', 1);
        if (nIndex >= 0)
        {
          setServletPath (sPath.substring (0, nIndex));
          sPath = sPath.substring (nIndex);
        }
        else
        {
          setServletPath (sPath);
          sPath = "";
        }

        // Remaining is the path info:
        setPathInfo (sPath);

        // Update request URI
        setRequestURI (getContextPath () + getServletPath () + getPathInfo ());

        setQueryString (aURI.getQuery ());
        removeAllParameters ();
        setParameters (URLUtils.getQueryStringAsMap (aURI.getQuery ()));
        return this;
      }
    }
    setScheme (null);
    setServerName (null);
    setServerPort (DEFAULT_SERVER_PORT);
    setContextPath (null);
    setServletPath (null);
    setPathInfo (null);
    setQueryString (null);
    removeAllParameters ();
    return this;
  }

  @Nonnull
  public static MockHttpServletRequest createWithContent (@Nullable final byte [] aContent)
  {
    return createWithContent (aContent, null);
  }

  @Nonnull
  public static MockHttpServletRequest createWithContent (@Nullable final byte [] aContent,
                                                          @Nullable final String sContentType)
  {
    final MockHttpServletRequest req = new MockHttpServletRequest ();
    req.setContent (aContent);
    req.setContentType (sContentType);
    return req;
  }
}
