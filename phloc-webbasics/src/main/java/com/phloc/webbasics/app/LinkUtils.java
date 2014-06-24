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
package com.phloc.webbasics.app;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.ReadonlySimpleURL;
import com.phloc.commons.url.SMap;
import com.phloc.commons.url.SimpleURL;
import com.phloc.commons.url.URLProtocolRegistry;
import com.phloc.webbasics.app.layout.ILayoutExecutionContext;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Misc utilities to create link URLs.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class LinkUtils
{
  public static final String STREAM_SERVLET_NAME_REGEX = "[a-zA-Z0-9-_]+";

  /**
   * The default name of the stream servlet. If this is different in you
   * application you may not use the methods that refer to this path!
   */
  public static final String DEFAULT_STREAM_SERVLET_NAME = "stream";

  /**
   * The default path to the stream servlet. If this is different in you
   * application you may not use the methods that refer to this path!
   * 
   * @deprecated Use {@link #getStreamServletPath()} instead
   */
  @Deprecated
  public static final String DEFAULT_STREAM_SERVLET_PATH = "/" + DEFAULT_STREAM_SERVLET_NAME;

  private static final Logger s_aLogger = LoggerFactory.getLogger (LinkUtils.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  @GuardedBy ("s_aRWLock")
  private static String s_sStreamServletName = DEFAULT_STREAM_SERVLET_NAME;

  private LinkUtils ()
  {}

  public static void setStreamServletName (@Nonnull @Nonempty final String sStreamServletName)
  {
    ValueEnforcer.notEmpty (sStreamServletName, "StreamServletName");
    if (!RegExHelper.stringMatchesPattern (STREAM_SERVLET_NAME_REGEX, sStreamServletName))
      throw new IllegalArgumentException ("Invalid StreamServletName '" +
                                          sStreamServletName +
                                          "' passed. It must match the following rexg: " +
                                          STREAM_SERVLET_NAME_REGEX);

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_sStreamServletName = sStreamServletName;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @Nonempty
  public static String getStreamServletName ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_sStreamServletName;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @Nonempty
  public static String getStreamServletPath ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return "/" + s_sStreamServletName;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  private static String _getURIWithContext (@Nonnull final String sContextPath, @Nonnull final String sHRef)
  {
    if (StringHelper.hasText (sContextPath) && sHRef.startsWith (sContextPath))
    {
      s_aLogger.warn ("The passed href '" + sHRef + "' already contains the context path '" + sContextPath + "'!");
      return sHRef;
    }

    // Always prefix with context path!
    final StringBuilder aSB = new StringBuilder (sContextPath);
    if (!StringHelper.startsWith (sHRef, '/'))
      aSB.append ('/');
    return aSB.append (sHRef).toString ();
  }

  /**
   * Prefix the passed href with the relative context path in case the passed
   * href has no protocol yet.
   * 
   * @param sHRef
   *        The href to be extended. May not be <code>null</code>.
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/<i>href</i></code> otherwise. Never
   *         <code>null</code>.
   */
  @Nonnull
  @Deprecated
  public static String getURIWithContext (@Nonnull final String sHRef)
  {
    ValueEnforcer.notNull (sHRef, "HRef");

    // If known protocol, keep it
    if (URLProtocolRegistry.hasKnownProtocol (sHRef))
      return sHRef;

    final String sContextPath = WebScopeManager.getGlobalScope ().getContextPath ();
    return _getURIWithContext (sContextPath, sHRef);
  }

  /**
   * Prefix the passed href with the relative context path in case the passed
   * href has no protocol yet.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param sHRef
   *        The href to be extended. May not be <code>null</code>.
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/<i>href</i></code> otherwise. Never
   *         <code>null</code>.
   */
  @Nonnull
  public static String getURIWithContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                          @Nonnull final String sHRef)
  {
    ValueEnforcer.notNull (aRequestScope, "RequestScope");
    ValueEnforcer.notNull (sHRef, "HRef");

    // If known protocol, keep it
    if (URLProtocolRegistry.hasKnownProtocol (sHRef))
      return sHRef;

    final String sContextPath = aRequestScope.getContextPath ();
    return aRequestScope.encodeURL (_getURIWithContext (sContextPath, sHRef));
  }

  /**
   * Prefix the passed href with the relative context path in case the passed
   * href has no protocol yet.
   * 
   * @param sHRef
   *        The href to be extended. May not be <code>null</code>.
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/<i>href</i></code> otherwise. Never
   *         <code>null</code>.
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getURLWithContext (@Nonnull final String sHRef)
  {
    return getURLWithContext (sHRef, (Map <String, String>) null);
  }

  /**
   * Prefix the passed href with the relative context path in case the passed
   * href has no protocol yet.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param sHRef
   *        The href to be extended. May not be <code>null</code>.
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/<i>href</i></code> otherwise. Never
   *         <code>null</code>.
   */
  @Nonnull
  public static SimpleURL getURLWithContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final String sHRef)
  {
    return getURLWithContext (aRequestScope, sHRef, (Map <String, String>) null);
  }

  /**
   * Prefix the passed href with the relative context path in case the passed
   * href has no protocol yet.
   * 
   * @param sHRef
   *        The href to be extended. May not be <code>null</code>.
   * @param aParams
   *        Optional parameter map. May be <code>null</code>.
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/<i>href</i></code> otherwise. Never
   *         <code>null</code>.
   */
  @Nonnull
  public static SimpleURL getURLWithContext (@Nonnull final String sHRef, @Nullable final Map <String, String> aParams)
  {
    return new SimpleURL (getURIWithContext (sHRef), aParams);
  }

  /**
   * Prefix the passed href with the relative context path in case the passed
   * href has no protocol yet.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param sHRef
   *        The href to be extended. May not be <code>null</code>.
   * @param aParams
   *        Optional parameter map. May be <code>null</code>.
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/<i>href</i></code> otherwise. Never
   *         <code>null</code>.
   */
  @Nonnull
  public static SimpleURL getURLWithContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final String sHRef,
                                             @Nullable final Map <String, String> aParams)
  {
    final String sURIWithContext = getURIWithContext (aRequestScope, sHRef);
    return new SimpleURL (sURIWithContext, aParams);
  }

  /**
   * Prefix the passed href with the absolute server + context path in case the
   * passed href has no protocol yet.
   * 
   * @param sHRef
   *        The href to be extended. May not be <code>null</code>.
   * @return Either the original href if already absolute or
   *         <code>http://servername:8123/webapp-context/<i>href</i></code>
   *         otherwise. Never <code>null</code>.
   */
  @Nonnull
  @Deprecated
  public static String getURIWithServerAndContext (@Nonnull final String sHRef)
  {
    // If known protocol, keep it
    if (URLProtocolRegistry.hasKnownProtocol (sHRef))
      return sHRef;

    final IRequestWebScopeWithoutResponse aRequestScope = WebScopeManager.getRequestScope ();
    final String sContextPath = aRequestScope.getContextPath ();
    return aRequestScope.getFullServerPath () + _getURIWithContext (sContextPath, sHRef);
  }

  /**
   * Prefix the passed href with the absolute server + context path in case the
   * passed href has no protocol yet.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param sHRef
   *        The href to be extended. May not be <code>null</code>.
   * @return Either the original href if already absolute or
   *         <code>http://servername:8123/webapp-context/<i>href</i></code>
   *         otherwise. Never <code>null</code>.
   */
  @Nonnull
  public static String getURIWithServerAndContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                   @Nonnull final String sHRef)
  {
    ValueEnforcer.notNull (aRequestScope, "RequestScope");
    ValueEnforcer.notNull (sHRef, "HRef");

    // If known protocol, keep it
    if (URLProtocolRegistry.hasKnownProtocol (sHRef))
      return sHRef;

    final String sContextPath = aRequestScope.getContextPath ();
    final String sURI = aRequestScope.getFullServerPath () + _getURIWithContext (sContextPath, sHRef);
    return aRequestScope.encodeURL (sURI);
  }

  /**
   * Prefix the passed href with the absolute server + context path in case the
   * passed href has no protocol yet.
   * 
   * @param sHRef
   *        The href to be extended.
   * @return Either the original href if already absolute or
   *         <code>http://servername:8123/webapp-context/<i>href</i></code>
   *         otherwise.
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getURLWithServerAndContext (@Nonnull final String sHRef)
  {
    return getURLWithServerAndContext (sHRef, (Map <String, String>) null);
  }

  /**
   * Prefix the passed href with the absolute server + context path in case the
   * passed href has no protocol yet.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param sHRef
   *        The href to be extended.
   * @return Either the original href if already absolute or
   *         <code>http://servername:8123/webapp-context/<i>href</i></code>
   *         otherwise.
   */
  @Nonnull
  public static SimpleURL getURLWithServerAndContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                      @Nonnull final String sHRef)
  {
    return getURLWithServerAndContext (aRequestScope, sHRef, (Map <String, String>) null);
  }

  /**
   * Prefix the passed href with the absolute server + context path in case the
   * passed href has no protocol yet.
   * 
   * @param sHRef
   *        The href to be extended.
   * @return Either the original href if already absolute or
   *         <code>http://servername:8123/webapp-context/<i>href</i></code>
   *         otherwise.
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getURLWithServerAndContext (@Nonnull final String sHRef,
                                                      @Nullable final Map <String, String> aParams)
  {
    return new SimpleURL (getURIWithServerAndContext (sHRef), aParams);
  }

  /**
   * Prefix the passed href with the absolute server + context path in case the
   * passed href has no protocol yet.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param sHRef
   *        The href to be extended.
   * @return Either the original href if already absolute or
   *         <code>http://servername:8123/webapp-context/<i>href</i></code>
   *         otherwise.
   */
  @Nonnull
  public static SimpleURL getURLWithServerAndContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                      @Nonnull final String sHRef,
                                                      @Nullable final Map <String, String> aParams)
  {
    final String sURI = getURIWithServerAndContext (aRequestScope, sHRef);
    return new SimpleURL (sURI, aParams);
  }

  /**
   * Get a link to the specified menu item.
   * 
   * @param aMenuObject
   *        The menu object to link to. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static SimpleURL getLinkToMenuItem (@Nonnull final IMenuObject aMenuObject)
  {
    ValueEnforcer.notNull (aMenuObject, "MenuObject");

    if (aMenuObject instanceof IMenuItemExternal)
      return new SimpleURL (((IMenuItemExternal) aMenuObject).getURL ());

    return getLinkToMenuItem (aMenuObject.getID ());
  }

  /**
   * Get a link to the specified menu item.
   * 
   * @param sMenuItemID
   *        The ID of the menu item to link to. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @Deprecated
  private static SimpleURL _getLinkToMenuItem (@Nonnull final ApplicationRequestManager aARM,
                                               @Nonnull final String sMenuItemID)
  {
    return new SimpleURL ().add (aARM.getRequestParamNameMenuItem (), sMenuItemID);
  }

  /**
   * Get a link to the specified menu item.
   * 
   * @param sMenuItemID
   *        The ID of the menu item to link to. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getLinkToMenuItem (@Nonnull final String sMenuItemID)
  {
    ValueEnforcer.notNull (sMenuItemID, "MenuItemID");

    final ApplicationRequestManager aARM = ApplicationRequestManager.getInstance ();
    return _getLinkToMenuItem (aARM, sMenuItemID);
  }

  /**
   * Get a link to the specified menu item.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param sMenuItemID
   *        The ID of the menu item to link to. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getLinkToMenuItem (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final String sMenuItemID)
  {
    ValueEnforcer.notNull (aRequestScope, "RequestScope");
    ValueEnforcer.notNull (sMenuItemID, "MenuItemID");

    final ApplicationRequestManager aARM = ApplicationRequestManager.getInstance ();
    return new SimpleURL (aRequestScope.encodeURL (aRequestScope.getFullContextAndServletPath ())).add (aARM.getRequestParamNameMenuItem (),
                                                                                                        sMenuItemID);
  }

  /**
   * @return A link to the start page. Never <code>null</code>. E.g.
   *         <code>/</code> or <code>/context</code>.
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getHomeLink ()
  {
    final String sContextPath = WebScopeManager.getGlobalScope ().getContextPath ();
    return new SimpleURL (sContextPath.length () == 0 ? "/" : sContextPath);
  }

  /**
   * @return A link to the start page without any session ID. Never
   *         <code>null</code>. E.g. <code>/</code> or <code>/context</code>.
   *         This is useful for logout links.
   */
  @Nonnull
  public static SimpleURL getHomeLinkWithoutSession ()
  {
    final String sContextPath = WebScopeManager.getGlobalScope ().getContextPath ();
    return new SimpleURL (sContextPath.length () == 0 ? "/" : sContextPath);
  }

  /**
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @return A link to the start page. Never <code>null</code>. E.g.
   *         <code>/</code> or <code>/context</code>.
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getHomeLink (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    ValueEnforcer.notNull (aRequestScope, "RequestScope");

    final String sContextPath = aRequestScope.getContextPath ();
    final String sURI = sContextPath.length () == 0 ? "/" : sContextPath;
    return new SimpleURL (aRequestScope.encodeURL (sURI));
  }

  /**
   * @return A non-<code>null</code> URL to the current page, without any
   *         parameters. Never <code>null</code>.
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getSelfHref ()
  {
    return getSelfHref ((Map <String, String>) null);
  }

  /**
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @return A non-<code>null</code> URL to the current page, without any
   *         parameters. Never <code>null</code>.
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getSelfHref (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return getSelfHref (aRequestScope, (Map <String, String>) null);
  }

  /**
   * Get the URL to the current page with the provided set of parameter.
   * 
   * @param aParams
   *        The optional request parameters to be used. May be <code>null</code>
   *        or empty.
   * @return The non-<code>null</code> URL to the current page (selected menu
   *         item) with the passed parameters.
   * @see #getLinkToMenuItem(String)
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getSelfHref (@Nullable final Map <String, String> aParams)
  {
    final ApplicationRequestManager aARM = ApplicationRequestManager.getInstance ();
    final String sSelectedMenuItemID = aARM.getRequestMenuItemID ();
    return _getLinkToMenuItem (aARM, sSelectedMenuItemID).addAll (aParams);
  }

  /**
   * Get the URL to the current page with the provided set of parameter.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param aParams
   *        The optional request parameters to be used. May be <code>null</code>
   *        or empty.
   * @return The non-<code>null</code> URL to the current page (selected menu
   *         item) with the passed parameters.
   * @see #getLinkToMenuItem(String)
   */
  @Nonnull
  @Deprecated
  public static SimpleURL getSelfHref (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                       @Nullable final Map <String, String> aParams)
  {
    final ApplicationRequestManager aARM = ApplicationRequestManager.getInstance ();
    final String sSelectedMenuItemID = aARM.getRequestMenuItemID ();
    return new SimpleURL (aRequestScope.encodeURL (aRequestScope.getFullContextAndServletPath ())).add (aARM.getRequestParamNameMenuItem (),
                                                                                                        sSelectedMenuItemID);
  }

  /**
   * @return A map with the default parameters handled by the application
   *         framework. This currently consists of the selected menu item ID and
   *         the current display locale. Never <code>null</code> nor empty.
   */
  @Nonnull
  @Deprecated
  @ReturnsMutableCopy
  public static SMap getDefaultParams ()
  {
    final ApplicationRequestManager aARM = ApplicationRequestManager.getInstance ();
    return new SMap ().add (aARM.getRequestParamNameMenuItem (), aARM.getRequestMenuItemID ())
                      .add (aARM.getRequestParamNameLocale (), aARM.getRequestDisplayLocale ().toString ());
  }

  /**
   * @return A map with the default parameters handled by the application
   *         framework. This currently consists of the selected menu item ID and
   *         the current display locale. Never <code>null</code> nor empty.
   */
  @Nonnull
  @Deprecated
  @ReturnsMutableCopy
  public static SMap getDefaultParams (@Nonnull final ILayoutExecutionContext aLEC)
  {
    final ApplicationRequestManager aARM = ApplicationRequestManager.getInstance ();
    return new SMap ().add (aARM.getRequestParamNameMenuItem (), aLEC.getSelectedMenuItemID ())
                      .add (aARM.getRequestParamNameLocale (), aLEC.getDisplayLocale ().toString ());
  }

  /**
   * Get the default URL to stream the passed URL. It is assumed that the
   * servlet is located under the path "/stream". Because of the logic of the
   * stream servlet, no parameter are assumed.
   * 
   * @param sURL
   *        The URL to be streamed. If it does not start with a slash ("/") one
   *        is prepended automatically. If the URL already has a protocol, it is
   *        returned unchanged. May neither be <code>null</code> nor empty.
   * @return The URL incl. the context to be stream. E.g.
   *         <code>/<i>webapp-context</i>/stream/<i>URL</i></code>.
   * @see #DEFAULT_STREAM_SERVLET_PATH
   */
  @Nonnull
  @Deprecated
  public static ISimpleURL getStreamURL (@Nonnull @Nonempty final String sURL)
  {
    ValueEnforcer.notEmpty (sURL, "URL");

    // If the URL is absolute, use it
    if (URLProtocolRegistry.hasKnownProtocol (sURL))
      return new ReadonlySimpleURL (sURL);

    String sPrefix = getStreamServletPath ();
    if (!StringHelper.startsWith (sURL, '/'))
      sPrefix += '/';
    return getURLWithContext (sPrefix + sURL);
  }

  /**
   * Get the default URL to stream the passed URL. It is assumed that the
   * servlet is located under the path "/stream". Because of the logic of the
   * stream servlet, no parameter are assumed.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param sURL
   *        The URL to be streamed. If it does not start with a slash ("/") one
   *        is prepended automatically. If the URL already has a protocol, it is
   *        returned unchanged. May neither be <code>null</code> nor empty.
   * @return The URL incl. the context to be stream. E.g.
   *         <code>/<i>webapp-context</i>/stream/<i>URL</i></code>.
   * @see #DEFAULT_STREAM_SERVLET_PATH
   */
  @Nonnull
  public static ISimpleURL getStreamURL (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                         @Nonnull @Nonempty final String sURL)
  {
    ValueEnforcer.notNull (aRequestScope, "RequestScope");
    ValueEnforcer.notEmpty (sURL, "URL");

    // If the URL is absolute, use it
    if (URLProtocolRegistry.hasKnownProtocol (sURL))
      return new ReadonlySimpleURL (sURL);

    final StringBuilder aPrefix = new StringBuilder (getStreamServletPath ());
    if (!StringHelper.startsWith (sURL, '/'))
      aPrefix.append ('/');
    return getURLWithContext (aRequestScope, aPrefix.append (sURL).toString ());
  }
}
