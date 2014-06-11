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
package com.phloc.webbasics.app.html;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.webbasics.IWebURIToURLConverter;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * This class keeps track of all the JS files that must be included for a single
 * request, so that the controls are working properly.
 * 
 * @author Philip Helger
 */
public final class PerRequestJSIncludes
{
  private static final String REQUEST_ATTR_JSINCLUDE = PerRequestJSIncludes.class.getName ();

  private PerRequestJSIncludes ()
  {}

  @Nullable
  private static Set <IJSPathProvider> _getPerRequestSet (final boolean bCreateIfNotExisting)
  {
    final IRequestWebScopeWithoutResponse aRequestScope = WebScopeManager.getRequestScope ();
    Set <IJSPathProvider> ret = aRequestScope.getCastedAttribute (REQUEST_ATTR_JSINCLUDE);
    if (ret == null && bCreateIfNotExisting)
    {
      ret = new LinkedHashSet <IJSPathProvider> ();
      aRequestScope.setAttribute (REQUEST_ATTR_JSINCLUDE, ret);
    }
    return ret;
  }

  /**
   * Register a new JS item only for this request
   * 
   * @param aJSPathProvider
   *        The JS path provider to use. May not be <code>null</code>.
   */
  public static void registerJSIncludeForThisRequest (@Nonnull final IJSPathProvider aJSPathProvider)
  {
    ValueEnforcer.notNull (aJSPathProvider, "JSPathProvider");

    _getPerRequestSet (true).add (aJSPathProvider);
  }

  /**
   * Unregister a existing JS item only from this request
   * 
   * @param aJSPathProvider
   *        The JS path provider to use. May not be <code>null</code>.
   */
  public static void unregisterJSIncludeFromThisRequest (@Nonnull final IJSPathProvider aJSPathProvider)
  {
    ValueEnforcer.notNull (aJSPathProvider, "JSPathProvider");

    _getPerRequestSet (true).remove (aJSPathProvider);
  }

  /**
   * Unregister all existing JS items from this request
   */
  public static void unregisterAllJSIncludesFromThisRequest ()
  {
    final Set <IJSPathProvider> aSet = _getPerRequestSet (false);
    if (aSet != null)
      aSet.clear ();
  }

  /**
   * @return A non-<code>null</code> set with all JS paths to be included in
   *         this request. Order is ensured using LinkedHashSet.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static Set <IJSPathProvider> getAllRegisteredJSIncludesForThisRequest ()
  {
    final Set <IJSPathProvider> ret = _getPerRequestSet (false);
    return ContainerHelper.newOrderedSet (ret);
  }

  /**
   * Get all JS includes registered for this path with the specified converter.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param aConverter
   *        The converter from the realtive URI path (e.g.
   *        "autonumeric/autonumeric.css") to the final URL to be used (e.g.
   *        "/stream/autonumeric/autonumeric.css"). May not be <code>null</code>
   *        .
   * @param bRegularVersion
   *        <code>true</code> to use the non-minified version,
   *        <code>false</code> to use the minified version.
   * @return A non-<code>null</code> list with all JS URLs to be included in
   *         this request.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static List <ISimpleURL> getAllRegisteredJSIncludeURLsForThisRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                                               @Nonnull final IWebURIToURLConverter aConverter,
                                                                               final boolean bRegularVersion)
  {
    ValueEnforcer.notNull (aRequestScope, "RequestScope");
    ValueEnforcer.notNull (aConverter, "Converter");

    final Set <IJSPathProvider> aSet = _getPerRequestSet (false);
    final List <ISimpleURL> ret = new ArrayList <ISimpleURL> ();
    if (aSet != null)
      for (final IJSPathProvider aPathProvider : aSet)
        ret.add (aConverter.getAsURL (aRequestScope, aPathProvider.getJSItemPath (bRegularVersion)));
    return ret;
  }

  /**
   * @return <code>true</code> if at least a single JS path has been registered
   *         for this request only
   */
  public static boolean hasRegisteredJSIncludesForThisRequest ()
  {
    return ContainerHelper.isNotEmpty (_getPerRequestSet (false));
  }
}
