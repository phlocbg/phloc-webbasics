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
package com.phloc.appbasics.app;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.GlobalMenuTree;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.locale.country.CountryCache;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.scopes.IScope;
import com.phloc.scopes.nonweb.domain.IRequestScope;
import com.phloc.scopes.nonweb.domain.ISessionScope;
import com.phloc.scopes.nonweb.mgr.ScopeManager;

/**
 * This class holds the per-request configuration settings.
 * <ul>
 * <li>Menu item to show</li>
 * <li>Display locale</li>
 * </ul>
 * 
 * @author philip
 */
public final class GlobalRequestManager
{
  public static final String REQUEST_PARAMETER_MENUITEM = "menuitem";
  private static final String SESSION_VALUE_MENUITEM = "$menuitem";

  public static final String REQUEST_PARAMETER_DISPLAY_LOCALE = "locale";
  private static final String SESSION_VALUE_DISPLAY_LOCALE = "$displaylocale";

  private GlobalRequestManager ()
  {}

  /**
   * To be called upon the beginning of each request. Checks for the content of
   * the request parameter {@value #REQUEST_PARAMETER_MENUITEM} to determine the
   * selected menu item. Checks for the content of the request parameter
   * {@value #REQUEST_PARAMETER_DISPLAY_LOCALE} to determine any changes in the
   * display locale.
   */
  public static void onRequestBegin (@Nonnull final IRequestScope aRequestScope)
  {
    // determine page from request and store in request
    final String sMenuItemID = aRequestScope.getAttributeAsString (REQUEST_PARAMETER_MENUITEM);
    if (sMenuItemID != null)
    {
      // Validate the menu item ID and check the display filter!
      final IMenuObject aMenuObject = GlobalMenuTree.getInstance ().getMenuObjectOfID (sMenuItemID);
      if (aMenuObject instanceof IMenuItemPage && aMenuObject.matchesDisplayFilter ())
      {
        final ISessionScope aSessionScope = ScopeManager.getSessionScope (true);
        aSessionScope.setAttribute (SESSION_VALUE_MENUITEM, aMenuObject);
      }
    }

    // determine locale from request and store in session
    final String sDisplayLocale = aRequestScope.getAttributeAsString (REQUEST_PARAMETER_DISPLAY_LOCALE);
    if (sDisplayLocale != null)
    {
      final Locale aDisplayLocale = LocaleCache.getLocale (sDisplayLocale);
      if (aDisplayLocale != null)
      {
        // A valid locale was provided
        final ISessionScope aSessionScope = ScopeManager.getSessionScope (true);
        aSessionScope.setAttribute (SESSION_VALUE_DISPLAY_LOCALE, aDisplayLocale);
      }
    }
  }

  /**
   * @return The ID of the last requested menu item, or <code>null</code> if the
   *         corresponding session parameter is not present.
   */
  @Nullable
  public static IMenuItemPage getSessionMenuItem ()
  {
    final ISessionScope aSessionScope = ScopeManager.getSessionScope (false);
    return aSessionScope == null ? null : aSessionScope.<IMenuItemPage> getCastedAttribute (SESSION_VALUE_MENUITEM);
  }

  /**
   * @return The specified default menu item. May be <code>null</code> if no
   *         default menu item is specified.
   */
  @Nullable
  public static IMenuItemPage getDefaultMenuItem ()
  {
    return (IMenuItemPage) GlobalMenuTree.getInstance ().getDefaultMenuItem ();
  }

  /**
   * Resolve the request parameter for the menu item to an {@link IMenuItem}
   * object. If no parameter is present, return the default menu item.
   * 
   * @return The resolved menu item object from the request parameter. Never
   *         <code>null</code>.
   */
  @Nonnull
  public static IMenuItemPage getRequestMenuItem ()
  {
    // Get selected item from request/session
    final IMenuItemPage aSelectedMenuItem = getSessionMenuItem ();
    if (aSelectedMenuItem != null && aSelectedMenuItem.matchesDisplayFilter ())
      return aSelectedMenuItem;

    // Use default menu item
    final IMenuItemPage aDefaultMenuItem = getDefaultMenuItem ();
    if (aDefaultMenuItem != null && aDefaultMenuItem.matchesDisplayFilter ())
      return aDefaultMenuItem;

    // Last fallback: use the first menu item
    final DefaultTreeItemWithID <String, IMenuObject> aRootItem = GlobalMenuTree.getInstance ().getRootItem ();
    if (aRootItem != null && aRootItem.hasChildren ())
      for (final DefaultTreeItemWithID <String, IMenuObject> aItem : aRootItem.getChildren ())
        if (aItem.getData () instanceof IMenuItemPage)
        {
          final IMenuItemPage aFirstMenuItem = (IMenuItemPage) aItem.getData ();
          if (aFirstMenuItem.matchesDisplayFilter ())
            return aFirstMenuItem;
        }

    throw new IllegalStateException ("No menu item is present!");
  }

  /**
   * @return The ID of the current request menu item. May not be
   *         <code>null</code>.
   */
  @Nonnull
  public static String getRequestMenuItemID ()
  {
    return getRequestMenuItem ().getID ();
  }

  /**
   * Get the locale to be used for this request. If no parameter is present, the
   * one from the session is used.
   * 
   * @return The locale to be used for the current request.
   */
  @Nonnull
  public static Locale getRequestDisplayLocale ()
  {
    // Was a request locale set in session scope?
    final IScope aSessionScope = ScopeManager.getSessionScope (false);
    if (aSessionScope != null)
    {
      final Locale aSessionDisplayLocale = aSessionScope.getCastedAttribute (SESSION_VALUE_DISPLAY_LOCALE);
      if (aSessionDisplayLocale != null)
        return aSessionDisplayLocale;
    }

    // Nothing specified - use application default locale
    final Locale aDefaultLocale = GlobalLocaleManager.getDefaultLocale ();
    if (aDefaultLocale == null)
      throw new IllegalStateException ("No application default locale is specified!");
    return aDefaultLocale;
  }

  /**
   * @return The country-Locale of the request display locale
   * @see #getRequestDisplayLocale()
   */
  @Nonnull
  public static Locale getRequestDisplayCountry ()
  {
    return CountryCache.getCountry (getRequestDisplayLocale ());
  }
}
