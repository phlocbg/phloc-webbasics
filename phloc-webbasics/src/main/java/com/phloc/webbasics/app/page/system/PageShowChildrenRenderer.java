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
package com.phloc.webbasics.app.page.system;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.webbasics.app.page.IWebPageExecutionContext;

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
   * @param aWPEC
   *        Web page execution context. May not be <code>null</code>.
   * @param aMenuSeparator
   *        The menu separator. Never <code>null</code>.
   * @return The rendered representation or <code>null</code>
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode renderMenuSeparator (@Nonnull final IWebPageExecutionContext aWPEC,
                                         @Nonnull final IMenuSeparator aMenuSeparator)
  {
    return null;
  }

  /**
   * Render a menu item to an internal page
   *
   * @param aWPEC
   *        Web page execution context. May not be <code>null</code>.
   * @param aMenuItemPage
   *        The menu item. Never <code>null</code>.
   * @return The rendered representation or <code>null</code>
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode renderMenuItemPage (@Nonnull final IWebPageExecutionContext aWPEC,
                                        @Nonnull final IMenuItemPage aMenuItemPage)
  {
    if (!aMenuItemPage.matchesDisplayFilter ())
      return null;
    return new HCA (aWPEC.getLinkToMenuItem (aMenuItemPage.getID ())).addChild (aMenuItemPage.getDisplayText (aWPEC.getDisplayLocale ()));
  }

  /**
   * Render a menu item to an external page
   *
   * @param aWPEC
   *        Web page execution context. May not be <code>null</code>.
   * @param aMenuItemExternal
   *        The menu item. Never <code>null</code>.
   * @return The rendered representation or <code>null</code>
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode renderMenuItemExternal (@Nonnull final IWebPageExecutionContext aWPEC,
                                            @Nonnull final IMenuItemExternal aMenuItemExternal)
  {
    if (!aMenuItemExternal.matchesDisplayFilter ())
      return null;

    final HCA ret = new HCA (aMenuItemExternal.getURL ());
    // Set window target (if defined)
    if (StringHelper.hasText (aMenuItemExternal.getTarget ()))
      ret.setTarget (new HCA_Target (aMenuItemExternal.getTarget ()));
    ret.addChild (aMenuItemExternal.getDisplayText (aWPEC.getDisplayLocale ()));
    return ret;
  }
}
