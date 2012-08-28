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
package com.phloc.webbasics.app;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.SMap;
import com.phloc.commons.url.SimpleURL;
import com.phloc.commons.url.URLProtocolRegistry;
import com.phloc.html.js.marshal.JSMarshaller;
import com.phloc.scopes.web.mgr.WebScopeManager;

/**
 * Misc utilities to create link URLs.
 * 
 * @author philip
 */
@Immutable
public final class LinkUtils
{
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
   *        The href to be extended.
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/<i>href</i></code> otherwise.
   */
  @Nonnull
  public static String getURIWithContext (@Nonnull final String sHRef)
  {
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
   *        The href to be extended.
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/<i>href</i></code> otherwise.
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
   *        The href to be extended.
   * @param aParams
   *        optional parameter map
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/<i>href</i></code> otherwise.
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
   *        The href to be extended.
   * @return Either the original href if already absolute or
   *         <code>http://servername:8123/webapp-context/<i>href</i></code>
   *         otherwise.
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
   * Create a valid AJAX URI for the passed Ajax function name, assuming that
   * the Ajax servlet is located at "/ajax/"
   * 
   * @param sFunctionName
   *        The name of the Ajax function
   * @return The absolute path to the Ajax function like
   *         <code>/webapp-context/ajax/<i>functionName</i></code>.
   */
  @Nonnull
  public static String getAjaxURI (@Nonnull final String sFunctionName)
  {
    if (!JSMarshaller.isJSIdentifier (sFunctionName))
      throw new IllegalArgumentException ("Invalid function name '" + sFunctionName + "' specified");

    return getURIWithContext ("/ajax/" + sFunctionName);
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
    return new SimpleURL ().add (ApplicationRequestManager.REQUEST_PARAMETER_MENUITEM, sMenuItemID);
  }

  @Nonnull
  public static SimpleURL getHomeLink ()
  {
    return new SimpleURL (WebScopeManager.getRequestScope ().getFullContextPath ());
  }

  @Nonnull
  public static SimpleURL getSelfHref ()
  {
    return getSelfHref (null);
  }

  @Nonnull
  public static SimpleURL getSelfHref (@Nullable final Map <String, String> aParams)
  {
    return getLinkToMenuItem (ApplicationRequestManager.getCurrentMenuItemID ()).addAll (aParams);
  }

  @Nonnull
  public static SMap getDefaultParams ()
  {
    return new SMap ().add (ApplicationRequestManager.REQUEST_PARAMETER_MENUITEM,
                            ApplicationRequestManager.getCurrentMenuItemID ())
                      .add (ApplicationRequestManager.REQUEST_PARAMETER_DISPLAY_LOCALE,
                            ApplicationRequestManager.getRequestDisplayLocale ().toString ());
  }
}
