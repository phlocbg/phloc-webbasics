/**
 * Copyright (C) 2006-2015 phloc systems
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
import java.nio.charset.Charset;
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
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.IHasLocale;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.charset.CharsetManager;
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
import com.phloc.web.http.AcceptCharsetHandler;
import com.phloc.web.http.CHTTPHeader;
import com.phloc.web.http.EHTTPMethod;
import com.phloc.web.http.EHTTPVersion;
import com.phloc.web.servlet.request.RequestHelper;
import com.phloc.web.servlet.session.SessionHelper;

// ESCA-JAVA0116:
/**
 * Mock implementation of {@link HttpServletRequest}.
 *
 * @author Philip Helger
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
  /** The default HTTP method: GET */
  public static final EHTTPMethod DEFAULT_METHOD = EHTTPMethod.GET;
  private static final Logger s_aLogger = LoggerFactory.getLogger (MockHttpServletRequest.class);

  private boolean m_bInvalidated = false;
  private boolean m_bActive = true;
  private final Map <String, Object> m_aAttributes = new HashMap <String, Object> ();
  private Charset m_aCharacterEncoding;
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
    this (null, DEFAULT_METHOD, false);
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
    this (aServletContext, DEFAULT_METHOD, DEFAULT_INVOKE_HTTP_LISTENER);
  }

  /**
   * Create a new MockHttpServletRequest.
   *
   * @param aServletContext
   *        the ServletContext that the request runs in (may be
   *        <code>null</code> to use a default MockServletContext)
   * @param eMethod
   *        the request method (may be <code>null</code>)
   * @see MockServletContext
   */
  public MockHttpServletRequest (@Nullable final ServletContext aServletContext, @Nullable final EHTTPMethod eMethod)
  {
    this (aServletContext, eMethod, DEFAULT_INVOKE_HTTP_LISTENER);
  }

  /**
   * Create a new MockHttpServletRequest.
   *
   * @param aServletContext
   *        the ServletContext that the request runs in (may be
   *        <code>null</code> to use a default MockServletContext)
   * @param eMethod
   *        the request method (may be <code>null</code>)
   * @param bInvokeHttpListeners
   *        if <code>true</code> than the HTTP request event listeners from
   *        {@link MockHttpListener} are triggered
   * @see #setMethod
   * @see MockServletContext
   */
  public MockHttpServletRequest (@Nullable final ServletContext aServletContext,
                                 @Nullable final EHTTPMethod eMethod,
                                 final boolean bInvokeHttpListeners)
  {
    this.m_aServletContext = aServletContext;
    setMethod (eMethod);
    this.m_aLocales.add (Locale.ENGLISH);

    // Add default HTTP header
    addHeader (CHTTPHeader.USER_AGENT, getClass ().getName ());
    // Disable GZip and Deflate!
    addHeader (CHTTPHeader.ACCEPT_ENCODING, "*, gzip;q=0, x-gzip;q=0, deflate;q=0, compress;q=0, x-compress;q=0");
    addHeader (CHTTPHeader.ACCEPT_CHARSET, AcceptCharsetHandler.ANY_CHARSET);

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
    if (this.m_aServletContext == null)
      throw new IllegalStateException ("No servlet context present!");
    return this.m_aServletContext;
  }

  /**
   * @return whether this request is still active (that is, not completed yet).
   */
  public boolean isActive ()
  {
    return this.m_bActive;
  }

  /**
   * Mark this request as completed, keeping its state.
   */
  public void close ()
  {
    this.m_bActive = false;
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
    if (this.m_bInvalidated)
      throw new IllegalStateException ("Request scope already invalidated!");
    this.m_bInvalidated = true;

    if (this.m_aServletContext != null)
    {
      final ServletRequestEvent aSRE = new ServletRequestEvent (this.m_aServletContext, this);
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
    if (!this.m_bActive)
      throw new IllegalStateException ("Request is not active anymore");
  }

  @Override
  @Nullable
  public Object getAttribute (@Nullable final String sName)
  {
    checkActive ();
    return this.m_aAttributes.get (sName);
  }

  @Override
  @Nonnull
  public Enumeration <String> getAttributeNames ()
  {
    checkActive ();
    return ContainerHelper.getEnumeration (this.m_aAttributes.keySet ());
  }

  @Override
  public void setCharacterEncoding (@Nullable final String sCharacterEncoding)
  {
    setCharacterEncoding (sCharacterEncoding == null ? null : CharsetManager.getCharsetFromName (sCharacterEncoding));
  }

  public void setCharacterEncoding (@Nullable final Charset aCharacterEncoding)
  {
    this.m_aCharacterEncoding = aCharacterEncoding;
  }

  @Override
  @Nullable
  public String getCharacterEncoding ()
  {
    return this.m_aCharacterEncoding == null ? null : this.m_aCharacterEncoding.name ();
  }

  @Nullable
  public Charset getCharacterEncodingObj ()
  {
    return this.m_aCharacterEncoding;
  }

  @Nonnull
  public Charset getCharacterEncodingObjOrDefault ()
  {
    Charset ret = getCharacterEncodingObj ();
    if (ret == null)
      ret = SystemHelper.getSystemCharset ();
    return ret;
  }

  @Nonnull
  public MockHttpServletRequest setContent (@Nullable final byte [] aContent)
  {
    this.m_aContent = ArrayHelper.getCopy (aContent);
    removeHeader (CHTTPHeader.CONTENT_LENGTH);
    addHeader (CHTTPHeader.CONTENT_LENGTH, Integer.toString (this.m_aContent.length));
    return this;
  }

  @Override
  @CheckForSigned
  public int getContentLength ()
  {
    return this.m_aContent != null ? this.m_aContent.length : -1;
  }

  @Nonnull
  public MockHttpServletRequest setContentType (@Nullable final IMimeType aContentType)
  {
    return setContentType (aContentType == null ? null : aContentType.getAsString ());
  }

  @Nonnull
  public MockHttpServletRequest setContentType (@Nullable final String sContentType)
  {
    this.m_sContentType = sContentType;
    removeHeader (CHTTPHeader.CONTENT_TYPE);
    if (sContentType != null)
      addHeader (CHTTPHeader.CONTENT_TYPE, sContentType);
    return this;
  }

  @Override
  @Nullable
  public String getContentType ()
  {
    return this.m_sContentType;
  }

  /**
   * Note: do not change the content via {@link #setContent(byte[])}, while an
   * input stream is open, because this may lead to indeterministic results!
   *
   * @return <code>null</code> if no content is present. If non-
   *         <code>null</code> the caller is responsible for closing the
   *         {@link InputStream}.
   */
  @Override
  @Nullable
  public ServletInputStream getInputStream ()
  {
    if (this.m_aContent == null)
      return null;

    return new MockServletInputStream (this.m_aContent);
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
   * @return this
   */
  @Nonnull
  public MockHttpServletRequest setParameter (@Nonnull final String sName, @Nullable final String sValue)
  {
    return setParameter (sName, new String [] { sValue });
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
   * @return this
   */
  @Nonnull
  public MockHttpServletRequest setParameter (@Nonnull final String sName, @Nullable final String [] aValues)
  {
    ValueEnforcer.notNull (sName, "Name");
    this.m_aParameters.put (sName, aValues);
    return this;
  }

  /**
   * Sets all provided parameters <emphasis>replacing</emphasis> any existing
   * values for the provided parameter names. To add without replacing existing
   * values, use {@link #addParameters(Map)}.
   *
   * @param aParams
   *        Parameter name value map. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public MockHttpServletRequest setParameters (@Nullable final Map <String, ? extends Object> aParams)
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
    return this;
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
   * @return this
   */
  @Nonnull
  public final MockHttpServletRequest addParameter (@Nonnull final String sName, @Nullable final String sValue)
  {
    return addParameter (sName, new String [] { sValue });
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
   * @return this
   */
  @Nonnull
  public final MockHttpServletRequest addParameter (@Nonnull final String sName, @Nullable final String [] aValues)
  {
    ValueEnforcer.notNull (sName, "Name");

    final String [] aOldParams = this.m_aParameters.get (sName);
    this.m_aParameters.put (sName, ArrayHelper.getConcatenated (aOldParams, aValues));
    return this;
  }

  /**
   * Adds all provided parameters <emphasis>without</emphasis> replacing any
   * existing values. To replace existing values, use
   * {@link #setParameters(Map)}.
   *
   * @param aParams
   *        Parameter name value map
   * @return this
   */
  @Nonnull
  public MockHttpServletRequest addParameters (@Nullable final Map <String, ? extends Object> aParams)
  {
    if (aParams != null)
      for (final Map.Entry <String, ? extends Object> aEntry : aParams.entrySet ())
      {
        final Object aValue = aEntry.getValue ();
        if (aValue instanceof String)
          addParameter (aEntry.getKey (), (String) aValue);
        else
          if (aValue instanceof String [])
            addParameter (aEntry.getKey (), (String []) aValue);
          else
            throw new IllegalArgumentException ("Unexpected parameter type: " +
                                                CGStringHelper.getSafeClassName (aValue));
      }
    return this;
  }

  /**
   * Remove already registered values for the specified HTTP parameter, if any.
   *
   * @param sName
   *        Parameter name
   * @return this
   */
  @Nonnull
  public MockHttpServletRequest removeParameter (@Nonnull final String sName)
  {
    ValueEnforcer.notNull (sName, "Name");
    this.m_aParameters.remove (sName);
    return this;
  }

  /**
   * Removes all existing parameters.
   *
   * @return this
   */
  @Nonnull
  public MockHttpServletRequest removeAllParameters ()
  {
    this.m_aParameters.clear ();
    return this;
  }

  @Override
  @Nullable
  public String getParameter (@Nonnull final String sName)
  {
    ValueEnforcer.notNull (sName, "Name");

    final String [] aParamValues = this.m_aParameters.get (sName);
    return ArrayHelper.getFirst (aParamValues);
  }

  @Override
  @Nonnull
  public Enumeration <String> getParameterNames ()
  {
    return ContainerHelper.getEnumeration (this.m_aParameters.keySet ());
  }

  @Override
  @Nullable
  @ReturnsMutableCopy
  public String [] getParameterValues (@Nonnull final String sName)
  {
    ValueEnforcer.notNull (sName, "Name");
    return ArrayHelper.getCopy (this.m_aParameters.get (sName));
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String []> getParameterMap ()
  {
    return ContainerHelper.newMap (this.m_aParameters);
  }

  @Nonnull
  public MockHttpServletRequest setProtocol (@Nullable final String sProtocol)
  {
    this.m_sProtocol = sProtocol;
    return this;
  }

  @Override
  @Nullable
  public String getProtocol ()
  {
    return this.m_sProtocol;
  }

  @Nonnull
  public MockHttpServletRequest setScheme (@Nullable final String sScheme)
  {
    this.m_sScheme = sScheme;
    return this;
  }

  @Override
  @Nullable
  public String getScheme ()
  {
    return this.m_sScheme;
  }

  @Nonnull
  public MockHttpServletRequest setServerName (@Nullable final String sServerName)
  {
    this.m_sServerName = sServerName;
    return this;
  }

  @Override
  @Nullable
  public String getServerName ()
  {
    return this.m_sServerName;
  }

  @Nonnull
  public MockHttpServletRequest setServerPort (final int nServerPort)
  {
    this.m_nServerPort = nServerPort;
    return this;
  }

  @Override
  public int getServerPort ()
  {
    return this.m_nServerPort;
  }

  @Override
  @Nullable
  public BufferedReader getReader ()
  {
    if (this.m_aContent == null)
      return null;

    final InputStream aIS = new NonBlockingByteArrayInputStream (this.m_aContent);
    final Reader aReader = StreamUtils.createReader (aIS, getCharacterEncodingObjOrDefault ());
    return new BufferedReader (aReader);
  }

  @Nonnull
  public MockHttpServletRequest setRemoteAddr (@Nullable final String sRemoteAddr)
  {
    this.m_sRemoteAddr = sRemoteAddr;
    return this;
  }

  @Override
  @Nullable
  public String getRemoteAddr ()
  {
    return this.m_sRemoteAddr;
  }

  @Nonnull
  public MockHttpServletRequest setRemoteHost (@Nullable final String sRemoteHost)
  {
    this.m_sRemoteHost = sRemoteHost;
    return this;
  }

  @Override
  @Nullable
  public String getRemoteHost ()
  {
    return this.m_sRemoteHost;
  }

  @Override
  public void setAttribute (@Nonnull final String sName, @Nullable final Object aValue)
  {
    checkActive ();
    ValueEnforcer.notNull (sName, "Name");

    if (aValue != null)
      this.m_aAttributes.put (sName, aValue);
    else
      this.m_aAttributes.remove (sName);
  }

  @Override
  public void removeAttribute (@Nonnull final String sName)
  {
    ValueEnforcer.notNull (sName, "Name");

    checkActive ();
    this.m_aAttributes.remove (sName);
  }

  /**
   * Clear all of this request's attributes.
   */
  @Nonnull
  public MockHttpServletRequest clearAttributes ()
  {
    this.m_aAttributes.clear ();
    return this;
  }

  /**
   * Add a new preferred locale, before any existing locales.
   *
   * @param aLocale
   *        preferred locale
   * @return this
   */
  @Nonnull
  public MockHttpServletRequest addPreferredLocale (@Nonnull final Locale aLocale)
  {
    ValueEnforcer.notNull (aLocale, "Locale");
    this.m_aLocales.add (0, aLocale);
    return this;
  }

  @Override
  @Nonnull
  public Locale getLocale ()
  {
    // One element is added in ctor!
    return this.m_aLocales.get (0);
  }

  @Override
  @Nonnull
  @Nonempty
  public Enumeration <Locale> getLocales ()
  {
    return ContainerHelper.getEnumeration (this.m_aLocales);
  }

  @Nonnull
  public MockHttpServletRequest setSecure (final boolean bSecure)
  {
    this.m_bSecure = bSecure;
    return this;
  }

  @Override
  public boolean isSecure ()
  {
    return this.m_bSecure;
  }

  @Override
  @Nonnull
  public MockRequestDispatcher getRequestDispatcher (@Nonnull final String sPath)
  {
    return new MockRequestDispatcher (sPath);
  }

  @Override
  @Deprecated
  public String getRealPath (@Nonnull final String sPath)
  {
    return getServletContext ().getRealPath (sPath);
  }

  @Nonnull
  public MockHttpServletRequest setRemotePort (final int nRemotePort)
  {
    this.m_nRemotePort = nRemotePort;
    return this;
  }

  @Override
  public int getRemotePort ()
  {
    return this.m_nRemotePort;
  }

  @Nonnull
  public MockHttpServletRequest setLocalName (@Nullable final String sLocalName)
  {
    this.m_sLocalName = sLocalName;
    return this;
  }

  @Override
  @Nullable
  public String getLocalName ()
  {
    return this.m_sLocalName;
  }

  @Nonnull
  public MockHttpServletRequest setLocalAddr (@Nullable final String sLocalAddr)
  {
    this.m_sLocalAddr = sLocalAddr;
    return this;
  }

  @Override
  @Nullable
  public String getLocalAddr ()
  {
    return this.m_sLocalAddr;
  }

  @Nonnull
  public MockHttpServletRequest setLocalPort (final int nLocalPort)
  {
    this.m_nLocalPort = nLocalPort;
    return this;
  }

  @Override
  public int getLocalPort ()
  {
    return this.m_nLocalPort;
  }

  @Nonnull
  public MockHttpServletRequest setAuthType (@Nullable final String sAuthType)
  {
    this.m_sAuthType = sAuthType;
    return this;
  }

  @Override
  @Nullable
  public String getAuthType ()
  {
    return this.m_sAuthType;
  }

  @Nonnull
  public MockHttpServletRequest setCookies (@Nullable final Cookie [] aCookies)
  {
    this.m_aCookies = ArrayHelper.getCopy (aCookies);
    return this;
  }

  @Override
  @Nullable
  public Cookie [] getCookies ()
  {
    return ArrayHelper.getCopy (this.m_aCookies);
  }

  @Nullable
  private static String _getUnifiedHeaderName (@Nullable final String s)
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
   * @return this
   * @see #getHeaderNames
   * @see #getHeader
   * @see #getHeaders
   * @see #getDateHeader
   * @see #getIntHeader
   */
  @Nonnull
  public final MockHttpServletRequest addHeader (@Nullable final String sName, @Nullable final Object aValue)
  {
    this.m_aHeaders.putSingle (_getUnifiedHeaderName (sName), aValue);
    return this;
  }

  @Nonnull
  public MockHttpServletRequest removeHeader (@Nullable final String sName)
  {
    this.m_aHeaders.remove (_getUnifiedHeaderName (sName));
    return this;
  }

  @Override
  @UnsupportedOperation
  @Deprecated
  public long getDateHeader (@Nullable final String sName)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  @UnsupportedOperation
  @Deprecated
  public int getIntHeader (@Nullable final String sName)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  @Nullable
  public String getHeader (@Nullable final String sName)
  {
    final Collection <Object> aValue = this.m_aHeaders.get (_getUnifiedHeaderName (sName));
    return aValue == null || aValue.isEmpty () ? null : String.valueOf (aValue.iterator ().next ());
  }

  @Override
  @Nonnull
  public Enumeration <Object> getHeaders (@Nullable final String sName)
  {
    final Collection <Object> vals = this.m_aHeaders.get (_getUnifiedHeaderName (sName));
    return ContainerHelper.getEnumeration (vals != null ? vals : ContainerHelper.newUnmodifiableList ());
  }

  @Override
  @Nonnull
  public Enumeration <String> getHeaderNames ()
  {
    return ContainerHelper.getEnumeration (this.m_aHeaders.keySet ());
  }

  @Nonnull
  public final MockHttpServletRequest setMethod (@Nullable final EHTTPMethod eMethod)
  {
    this.m_eMethod = eMethod;
    return this;
  }

  @Nullable
  public EHTTPMethod getMethodEnum ()
  {
    return this.m_eMethod;
  }

  @Override
  @Nullable
  public String getMethod ()
  {
    return this.m_eMethod == null ? null : this.m_eMethod.getName ();
  }

  @Nonnull
  public MockHttpServletRequest setPathInfo (@Nullable final String sPathInfo)
  {
    this.m_sPathInfo = sPathInfo;
    return this;
  }

  @Override
  @Nullable
  public String getPathInfo ()
  {
    return this.m_sPathInfo;
  }

  @Override
  @Nullable
  public String getPathTranslated ()
  {
    return this.m_sPathInfo != null ? getRealPath (this.m_sPathInfo) : null;
  }

  @Nonnull
  public MockHttpServletRequest setContextPath (@Nullable final String sContextPath)
  {
    if (sContextPath != null)
      if (!sContextPath.isEmpty () && !sContextPath.startsWith ("/"))
        s_aLogger.error ("Illegal context path specified: '" + sContextPath + "'");
    this.m_sContextPath = sContextPath;
    return this;
  }

  @Override
  @Nullable
  public String getContextPath ()
  {
    return this.m_sContextPath;
  }

  @Nonnull
  public MockHttpServletRequest setQueryString (@Nullable final String sQueryString)
  {
    this.m_sQueryString = sQueryString;
    return this;
  }

  @Override
  @Nullable
  public String getQueryString ()
  {
    return this.m_sQueryString;
  }

  @Nonnull
  public MockHttpServletRequest setRemoteUser (@Nullable final String sRemoteUser)
  {
    this.m_sRemoteUser = sRemoteUser;
    return this;
  }

  @Override
  @Nullable
  public String getRemoteUser ()
  {
    return this.m_sRemoteUser;
  }

  @Nonnull
  public MockHttpServletRequest addUserRole (@Nullable final String sRole)
  {
    this.m_aUserRoles.add (sRole);
    return this;
  }

  @Override
  public boolean isUserInRole (@Nullable final String sRole)
  {
    return this.m_aUserRoles.contains (sRole);
  }

  @Nonnull
  public MockHttpServletRequest setUserPrincipal (@Nullable final Principal aUserPrincipal)
  {
    this.m_aUserPrincipal = aUserPrincipal;
    return this;
  }

  @Override
  @Nullable
  public Principal getUserPrincipal ()
  {
    return this.m_aUserPrincipal;
  }

  @Override
  @Nullable
  public String getRequestedSessionId ()
  {
    return getSession (true).getId ();
  }

  @Nonnull
  public MockHttpServletRequest setRequestURI (@Nullable final String sRequestURI)
  {
    this.m_sRequestURI = sRequestURI;
    return this;
  }

  @Override
  @Nullable
  public String getRequestURI ()
  {
    return this.m_sRequestURI;
  }

  @Override
  @Nonnull
  public StringBuffer getRequestURL ()
  {
    return new StringBuffer ().append (RequestHelper.getFullServerName (this.m_sScheme,
                                                                        this.m_sServerName,
                                                                        this.m_nServerPort))
                              .append (getRequestURI ());
  }

  @Nonnull
  public MockHttpServletRequest setServletPath (@Nullable final String sServletPath)
  {
    if (sServletPath != null && !sServletPath.isEmpty () && !sServletPath.startsWith ("/"))
      s_aLogger.error ("ServletPath must be empty or start with a slash: '" + sServletPath + "'");
    this.m_sServletPath = sServletPath;
    return this;
  }

  @Override
  @Nullable
  public String getServletPath ()
  {
    return this.m_sServletPath;
  }

  /**
   * Define the session ID to be used when creating a new session
   *
   * @param sSessionID
   *        The session ID to be used. If it is <code>null</code> a unique
   *        session ID is generated.
   * @return this
   */
  @Nonnull
  public MockHttpServletRequest setSessionID (@Nullable final String sSessionID)
  {
    this.m_sSessionID = sSessionID;
    return this;
  }

  /**
   * @return The session ID to use or <code>null</code> if a new session ID
   *         should be generated!
   */
  @Nullable
  public String getSessionID ()
  {
    return this.m_sSessionID;
  }

  @Nonnull
  public MockHttpServletRequest setSession (@Nullable final HttpSession aHttpSession)
  {
    this.m_aSession = aHttpSession;
    if (aHttpSession instanceof MockHttpSession)
      ((MockHttpSession) aHttpSession).doAccess ();
    return this;
  }

  @Override
  @Nullable
  public HttpSession getSession (final boolean bCreate)
  {
    checkActive ();

    // Reset session if invalidated.
    if (this.m_aSession != null && !SessionHelper.isSessionValid (this.m_aSession))
    {
      this.m_aSession = null;
    }
    // Create new session if necessary.
    if (this.m_aSession == null && bCreate)
      this.m_aSession = new MockHttpSession (getServletContext (), this.m_sSessionID);

    // Update last access time
    if (this.m_aSession instanceof MockHttpSession)
      ((MockHttpSession) this.m_aSession).doAccess ();

    return this.m_aSession;
  }

  @Override
  @Nonnull
  public HttpSession getSession ()
  {
    return getSession (true);
  }

  @Nonnull
  public MockHttpServletRequest setRequestedSessionIdValid (final boolean bRequestedSessionIdValid)
  {
    this.m_bRequestedSessionIDValid = bRequestedSessionIdValid;
    return this;
  }

  @Override
  public boolean isRequestedSessionIdValid ()
  {
    return this.m_bRequestedSessionIDValid;
  }

  @Nonnull
  public MockHttpServletRequest setRequestedSessionIdFromCookie (final boolean bRequestedSessionIdFromCookie)
  {
    this.m_bRequestedSessionIDFromCookie = bRequestedSessionIdFromCookie;
    return this;
  }

  @Override
  public boolean isRequestedSessionIdFromCookie ()
  {
    return this.m_bRequestedSessionIDFromCookie;
  }

  @Nonnull
  public MockHttpServletRequest setRequestedSessionIdFromURL (final boolean bRequestedSessionIdFromURL)
  {
    this.m_bRequestedSessionIDFromURL = bRequestedSessionIdFromURL;
    return this;
  }

  @Override
  public boolean isRequestedSessionIdFromURL ()
  {
    return this.m_bRequestedSessionIDFromURL;
  }

  @Override
  @Deprecated
  public boolean isRequestedSessionIdFromUrl ()
  {
    return isRequestedSessionIdFromURL ();
  }

  /**
   * Set all path related members to the value to be deduced from the request
   * URI.
   *
   * @param sRequestURL
   *        The request URL to parse and set correctly. If it is
   *        <code>null</code> or empty, all methods are set to <code>null</code>
   *        .
   * @return this
   * @see #setScheme(String)
   * @see #setServerName(String)
   * @see #setServerPort(int)
   * @see #setContextPath(String)
   * @see #setServletPath(String)
   * @see #setPathInfo(String)
   * @see #setQueryString(String)
   * @see #setParameters(Map)
   */
  @Nonnull
  public MockHttpServletRequest setAllPaths (@Nullable final String sRequestURL)
  {
    if (StringHelper.hasText (sRequestURL))
    {
      final URI aURI = URLUtils.getAsURI (RequestHelper.getWithoutSessionID (sRequestURL));
      if (aURI != null)
      {
        // Server stuff - straight forward
        setScheme (aURI.getScheme ());
        setSecure (CWeb.SCHEME_HTTPS.equals (aURI.getScheme ()));
        setServerName (aURI.getHost ());
        setServerPort (RequestHelper.getServerPortToUse (aURI.getScheme (), aURI.getPort ()));

        // Path stuff
        String sPath = aURI.getPath ();

        // Context path
        final String sServletContextPath = this.m_aServletContext == null ? ""
                                                                          : this.m_aServletContext.getContextPath ();
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

        // Request parameters
        setQueryString (aURI.getQuery ());
        removeAllParameters ();
        setParameters (URLUtils.getQueryStringAsMap (aURI.getQuery ()));
        return this;
      }
    }

    setScheme (null);
    setSecure (false);
    setServerName (null);
    setServerPort (DEFAULT_SERVER_PORT);
    setContextPath (null);
    setServletPath (null);
    setPathInfo (null);
    setRequestURI (null);
    setQueryString (null);
    removeAllParameters ();
    return this;
  }

  @Deprecated
  @Nonnull
  public static MockHttpServletRequest createWithContent (@Nullable final byte [] aContent)
  {
    return createWithContent (aContent, null);
  }

  @Deprecated
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
