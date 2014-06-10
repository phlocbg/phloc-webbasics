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
package com.phloc.webbasics.app.page.system;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * The default renderer used for {@link PageShowChildren}.
 * 
 * @author Philip Helger
 */
public class PageShowChildrenRenderer implements Serializable
{
  public PageShowChildrenRenderer ()
  {}

  /**
   * Render a menu separator
   * 
   * @param aMenuSeparator
   *        The menu separator. Never <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @return The rendered representation or <code>null</code>
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode renderMenuSeparator (@Nonnull final IMenuSeparator aMenuSeparator,
                                         @Nonnull final Locale aDisplayLocale)
  {
    return null;
  }

  /**
   * Render a menu item to an internal page
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param aMenuItemPage
   *        The menu item. Never <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @return The rendered representation or <code>null</code>
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode renderMenuItemPage (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                        @Nonnull final IMenuItemPage aMenuItemPage,
                                        @Nonnull final Locale aDisplayLocale)
  {
    if (!aMenuItemPage.matchesDisplayFilter ())
      return null;
    return new HCA (LinkUtils.getLinkToMenuItem (aRequestScope, aMenuItemPage.getID ())).addChild (aMenuItemPage.getDisplayText (aDisplayLocale));
  }

  /**
   * Render a menu item to an external page
   * 
   * @param aMenuItemExternal
   *        The menu item. Never <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @return The rendered representation or <code>null</code>
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode renderMenuItemExternal (@Nonnull final IMenuItemExternal aMenuItemExternal,
                                            @Nonnull final Locale aDisplayLocale)
  {
    if (!aMenuItemExternal.matchesDisplayFilter ())
      return null;
    return new HCA (aMenuItemExternal.getURL ()).addChild (aMenuItemExternal.getDisplayText (aDisplayLocale));
  }
}
