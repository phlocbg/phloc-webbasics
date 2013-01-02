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

import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.annotations.Nonempty;
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
public abstract class AbstractRequestManager implements IRequestManager
{
  public AbstractRequestManager ()
  {}

  @Nonnull
  protected abstract IMenuTree getMenuTree ();

  @Nonnull
  protected abstract ILocaleManager getLocaleManager ();

  @Nonnull
  @Nonempty
  protected abstract String getSessionAttrMenuItem ();

  @Nonnull
  @Nonempty
  protected abstract String getSessionAttrLocale ();

  public void onRequestBegin (@Nonnull final IRequestScope aRequestScope)
  {
    // determine page from request and store in request
    final String sMenuItemID = aRequestScope.getAttributeAsString (REQUEST_PARAMETER_MENUITEM);
    if (sMenuItemID != null)
    {
      // Validate the menu item ID and check the display filter!
      final IMenuObject aMenuObject = getMenuTree ().getMenuObjectOfID (sMenuItemID);
      if (aMenuObject instanceof IMenuItemPage && aMenuObject.matchesDisplayFilter ())
      {
        final ISessionScope aSessionScope = ScopeManager.getSessionScope (true);
        aSessionScope.setAttribute (getSessionAttrMenuItem (), aMenuObject);
      }
    }

    // determine locale from request and store in session
    final String sDisplayLocale = aRequestScope.getAttributeAsString (REQUEST_PARAMETER_DISPLAY_LOCALE);
    if (sDisplayLocale != null)
    {
      final Locale aDisplayLocale = LocaleCache.getLocale (sDisplayLocale);
      if (aDisplayLocale != null)
      {
        // Check if the locale is present in the locale manager
        if (getLocaleManager ().isSupportedLocale (aDisplayLocale))
        {
          // A valid locale was provided
          final ISessionScope aSessionScope = ScopeManager.getSessionScope (true);
          aSessionScope.setAttribute (getSessionAttrLocale (), aDisplayLocale);
        }
      }
    }
  }

  @Nullable
  public IMenuItemPage getSessionMenuItem ()
  {
    final ISessionScope aSessionScope = ScopeManager.getSessionScope (false);
    return aSessionScope == null ? null : aSessionScope.<IMenuItemPage> getCastedAttribute (getSessionAttrMenuItem ());
  }

  /**
   * @return The specified default menu item. May be <code>null</code> if no
   *         default menu item is specified.
   */
  @Nullable
  public IMenuItemPage getDefaultMenuItem ()
  {
    return (IMenuItemPage) getMenuTree ().getDefaultMenuItem ();
  }

  @Nonnull
  public IMenuItemPage getRequestMenuItem ()
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
    final DefaultTreeItemWithID <String, IMenuObject> aRootItem = getMenuTree ().getRootItem ();
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

  @Nonnull
  public String getRequestMenuItemID ()
  {
    return getRequestMenuItem ().getID ();
  }

  @Nonnull
  public Locale getRequestDisplayLocale ()
  {
    // Was a request locale set in session scope?
    final IScope aSessionScope = ScopeManager.getSessionScope (false);
    if (aSessionScope != null)
    {
      final Locale aSessionDisplayLocale = aSessionScope.getCastedAttribute (getSessionAttrLocale ());
      if (aSessionDisplayLocale != null)
        return aSessionDisplayLocale;
    }

    // Nothing specified - use application default locale
    final Locale aDefaultLocale = getLocaleManager ().getDefaultLocale ();
    if (aDefaultLocale == null)
      throw new IllegalStateException ("No application default locale is specified!");
    return aDefaultLocale;
  }

  @Nonnull
  public Locale getRequestDisplayCountry ()
  {
    return CountryCache.getCountry (getRequestDisplayLocale ());
  }
}
