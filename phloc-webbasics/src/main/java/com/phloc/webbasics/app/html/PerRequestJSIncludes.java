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
package com.phloc.webbasics.app.html;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * This class keeps track of all the JS files that must be included for a single
 * request, so that the controls are working properly.
 * 
 * @author philip
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

  public static void registerJSIncludeForThisRequest (@Nonnull final IJSPathProvider aJSPathProvider)
  {
    if (aJSPathProvider == null)
      throw new NullPointerException ("JSPathProvider");
    _getPerRequestSet (true).add (aJSPathProvider);
  }

  public static void unregisterJSIncludeFromThisRequest (@Nonnull final IJSPathProvider aJSPathProvider)
  {
    if (aJSPathProvider == null)
      throw new NullPointerException ("JSPathProvider");
    _getPerRequestSet (true).remove (aJSPathProvider);
  }

  /**
   * @return A non-<code>null</code> set with all JS paths to be included in
   *         this request. Order is ensured using LinkedHashSet.
   */
  @Nonnull
  @ReturnsImmutableObject
  public static Set <IJSPathProvider> getAllRegisteredJSIncludesForThisRequest ()
  {
    final Set <IJSPathProvider> ret = _getPerRequestSet (false);
    return ContainerHelper.makeUnmodifiableNotNull (ret);
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
