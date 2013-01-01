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
package com.phloc.webbasics.app.menu.ui;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;

/**
 * Interface for rendering menu objects
 * 
 * @author philip
 */
public interface IMenuItemRenderer
{
  /**
   * @param aSeparator
   *        The separator to be rendered.
   * @return The rendered menu separator. May not be <code>null</code>.
   */
  @Nonnull
  IHCNode renderSeparator (@Nonnull IMenuSeparator aSeparator);

  /**
   * Render a menu item on a page
   * 
   * @param aMenuItem
   *        The menu item to be rendered.
   * @param bHasChildren
   *        <code>true</code> if the menu item has children
   * @param bIsSelected
   *        <code>true</code> if the menu item is a selected menu item
   * @param bIsExpanded
   *        <code>true</code> if the menu item is expanded
   * @return The rendered menu item. May not be <code>null</code>.
   */
  @Nonnull
  IHCNode renderMenuItemPage (@Nonnull IMenuItemPage aMenuItem,
                              boolean bHasChildren,
                              boolean bIsSelected,
                              boolean bIsExpanded);

  /**
   * Render a menu item with an external link
   * 
   * @param aMenuItem
   *        The menu item to be rendered.
   * @param bHasChildren
   *        <code>true</code> if the menu item has children
   * @param bIsSelected
   *        <code>true</code> if the menu item is a selected menu item
   * @param bIsExpanded
   *        <code>true</code> if the menu item is expanded
   * @return The rendered menu item. May not be <code>null</code>.
   */
  @Nonnull
  IHCNode renderMenuItemExternal (@Nonnull IMenuItemExternal aMenuItem,
                                  boolean bHasChildren,
                                  boolean bIsSelected,
                                  boolean bIsExpanded);

  /**
   * Called when a new sub-level is entered
   * 
   * @param aNewLevel
   *        The HCUL to be modified
   */
  void onLevelDown (@Nonnull HCUL aNewLevel);

  void onMenuSeparatorItem (@Nonnull HCLI aLI);

  void onMenuItemPageItem (@Nonnull HCLI aLI, boolean bSelected);

  void onMenuItemExternalItem (@Nonnull HCLI aLI, boolean bSelected);
}
