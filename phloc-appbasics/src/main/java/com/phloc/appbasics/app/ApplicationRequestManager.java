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
package com.phloc.appbasics.app;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.MenuTree;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.locale.country.CountryCache;
import com.phloc.commons.string.StringHelper;
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
public final class ApplicationRequestManager
{
  public static final String REQUEST_PARAMETER_MENUITEM = "item";
  private static final String REQUEST_VALUE_MENUITEM = "$item";

  public static final String REQUEST_PARAMETER_DISPLAY_LOCALE = "locale";
  private static final String REQUEST_VALUE_DISPLAY_LOCALE = "$displaylocale";

  private ApplicationRequestManager ()
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
      final IMenuObject aMenuObject = MenuTree.getInstance ().getMenuObjectOfID (sMenuItemID);
      if (aMenuObject != null && aMenuObject instanceof IMenuItem && aMenuObject.matchesDisplayFilter ())
        aRequestScope.setAttribute (REQUEST_VALUE_MENUITEM, sMenuItemID);
    }

    // determine locale from request and store in session
    final String sDisplayLocale = aRequestScope.getAttributeAsString (REQUEST_PARAMETER_DISPLAY_LOCALE);
    if (sDisplayLocale != null)
    {
      final Locale aDisplayLocale = LocaleCache.getLocale (sDisplayLocale);
      if (aDisplayLocale != null)
      {
        final ISessionScope aSessionScope = ScopeManager.getSessionScope ();
        aSessionScope.setAttribute (REQUEST_VALUE_DISPLAY_LOCALE, aDisplayLocale);
      }
    }
  }

  /**
   * @return The ID of the requested menu item, or <code>null</code> if the
   *         corresponding request parameter is not present.
   */
  @Nullable
  public static String getRequestMenuItemID ()
  {
    return ScopeManager.getRequestScope ().getAttributeAsString (REQUEST_VALUE_MENUITEM);
  }

  /**
   * @return The current menu item. If none was selected, that the default menu
   *         item is returned.
   */
  @Nonnull
  public static String getCurrentMenuItemID ()
  {
    String sMenuItemID = getRequestMenuItemID ();
    if (StringHelper.hasNoText (sMenuItemID))
      sMenuItemID = MenuTree.getInstance ().getDefaultMenuItem ().getID ();
    return sMenuItemID;
  }

  /**
   * Resolve the request parameter for the menu item to an {@link IMenuItem}
   * object. If no parameter is present, return the default menu item.
   * 
   * @return The resolved menu item object from the request parameter or
   *         <code>null</code> if resolving the parameter failed.
   */
  @Nullable
  public static IMenuItem getRequestMenuItem ()
  {
    IMenuItem aDisplayMenuItem = null;

    final String sSelectedMenuItemID = getRequestMenuItemID ();
    if (sSelectedMenuItemID != null)
    {
      // Request parameter present
      final DefaultTreeItemWithID <String, IMenuObject> aSelectedMenuItem = MenuTree.getInstance ()
                                                                                    .getItemWithID (sSelectedMenuItemID);
      if (aSelectedMenuItem != null && aSelectedMenuItem.getData () instanceof IMenuItem)
        aDisplayMenuItem = (IMenuItem) aSelectedMenuItem.getData ();
    }
    else
    {
      // No request parameter

      // 1. use default menu item
      aDisplayMenuItem = MenuTree.getInstance ().getDefaultMenuItem ();

      if (aDisplayMenuItem == null)
      {
        // 2. no default menu item is present - get the very first menu item
        final DefaultTreeItemWithID <String, IMenuObject> aRootItem = MenuTree.getInstance ().getRootItem ();
        if (aRootItem != null && aRootItem.hasChildren ())
          for (final DefaultTreeItemWithID <String, IMenuObject> aItem : aRootItem.getChildren ())
            if (aItem.getData () instanceof IMenuItem)
            {
              aDisplayMenuItem = (IMenuItem) aItem.getData ();
              break;
            }
      }
    }

    return aDisplayMenuItem;
  }

  /**
   * @return The ID of resolved menu item or <code>null</code> if no menu item
   *         could be resolved!
   */
  @Nullable
  public static String getResolvedRequestMenuItemID ()
  {
    final IMenuItem aMenuItem = getRequestMenuItem ();
    return aMenuItem == null ? null : aMenuItem.getID ();
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
      final Locale aSessionDisplayLocale = aSessionScope.getCastedAttribute (REQUEST_VALUE_DISPLAY_LOCALE);
      if (aSessionDisplayLocale != null)
        return aSessionDisplayLocale;
    }

    // Nothing specified - use application default locale
    final Locale aDefaultLocale = ApplicationLocaleManager.getDefaultLocale ();
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
