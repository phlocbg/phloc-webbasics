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
package com.phloc.webbasics.app;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.IRequestManager;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.ReadonlySimpleURL;
import com.phloc.commons.url.SMap;
import com.phloc.commons.url.SimpleURL;
import com.phloc.commons.url.URLProtocolRegistry;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Misc utilities to create link URLs.
 * 
 * @author Philip Helger
 */
@Immutable
public final class LinkUtils
{
  /**
   * The default name of the stream servlet. If this is different in you
   * application you may not use the methods that refer to this path!
   */
  public static final String DEFAULT_STREAM_SERVLET_NAME = "stream";

  /**
   * The default path to the stream servlet. If this is different in you
   * application you may not use the methods that refer to this path!
   */
  public static final String DEFAULT_STREAM_SERVLET_PATH = "/" + DEFAULT_STREAM_SERVLET_NAME;

  private static final Logger s_aLogger = LoggerFactory.getLogger (LinkUtils.class);

  private LinkUtils ()
  {}

  @Nonnull
  private static String _getURIWithContext (@Nonnull final String sHRef)
  {
    final String sContextPath = WebScopeManager.getGlobalScope ().getContextPath ();
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
  public static String getURIWithContext (@Nonnull final String sHRef)
  {
    if (sHRef == null)
      throw new NullPointerException ("HRef");

    // If known protocol, keep it
    if (URLProtocolRegistry.hasKnownProtocol (sHRef))
      return sHRef;

    return _getURIWithContext (sHRef);
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
  public static SimpleURL getURLWithContext (@Nonnull final String sHRef)
  {
    return getURLWithContext (sHRef, null);
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
  public static String getURIWithServerAndContext (@Nonnull final String sHRef)
  {
    // If known protocol, keep it
    if (URLProtocolRegistry.hasKnownProtocol (sHRef))
      return sHRef;

    return WebScopeManager.getRequestScope ().getFullServerPath () + _getURIWithContext (sHRef);
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
  public static SimpleURL getURLWithServerAndContext (@Nonnull final String sHRef)
  {
    return getURLWithServerAndContext (sHRef, null);
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
  public static SimpleURL getURLWithServerAndContext (@Nonnull final String sHRef,
                                                      @Nullable final Map <String, String> aParams)
  {
    return new SimpleURL (getURIWithServerAndContext (sHRef), aParams);
  }

  /**
   * Get a link to the specified menu item.
   * 
   * @param sMenuItemID
   *        The ID of the menu item to link to. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static SimpleURL getLinkToMenuItem (@Nonnull final String sMenuItemID)
  {
    if (sMenuItemID == null)
      throw new NullPointerException ("menu item id");

    return new SimpleURL ().add (IRequestManager.REQUEST_PARAMETER_MENUITEM, sMenuItemID);
  }

  /**
   * @return A link to the start page. Never <code>null</code>. E.g.
   *         <code>/</code> or <code>/context</code>.
   */
  @Nonnull
  public static SimpleURL getHomeLink ()
  {
    final String sContextPath = WebScopeManager.getRequestScope ().getContextPath ();
    return new SimpleURL (sContextPath.length () == 0 ? "/" : sContextPath);
  }

  /**
   * @return A non-<code>null</code> URL to the current page, without any
   *         parameters. Never <code>null</code>.
   */
  @Nonnull
  public static SimpleURL getSelfHref ()
  {
    return getSelfHref (null);
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
  public static SimpleURL getSelfHref (@Nullable final Map <String, String> aParams)
  {
    final String sSelectedMenuItemID = ApplicationRequestManager.getInstance ().getRequestMenuItemID ();
    return getLinkToMenuItem (sSelectedMenuItemID).addAll (aParams);
  }

  /**
   * @return A map with the default parameters handled by the application
   *         framework. This currently consists of the selected menu item ID and
   *         the current display locale. Never <code>null</code> nor empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static SMap getDefaultParams ()
  {
    return new SMap ().add (IRequestManager.REQUEST_PARAMETER_MENUITEM,
                            ApplicationRequestManager.getInstance ().getRequestMenuItemID ())
                      .add (IRequestManager.REQUEST_PARAMETER_DISPLAY_LOCALE,
                            ApplicationRequestManager.getInstance ().getRequestDisplayLocale ().toString ());
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
  @ReturnsMutableCopy
  public static ISimpleURL getStreamURL (@Nonnull @Nonempty final String sURL)
  {
    if (StringHelper.hasNoText (sURL))
      throw new IllegalArgumentException ("URL");

    // If the URL is absolute, use it
    if (URLProtocolRegistry.hasKnownProtocol (sURL))
      return new ReadonlySimpleURL (sURL);

    String sPrefix = DEFAULT_STREAM_SERVLET_PATH;
    if (!StringHelper.startsWith (sURL, '/'))
      sPrefix += '/';
    return getURLWithContext (sPrefix + sURL);
  }
}
