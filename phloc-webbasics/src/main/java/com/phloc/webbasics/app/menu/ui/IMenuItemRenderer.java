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
package com.phloc.webbasics.app.menu.ui;

import javax.annotation.Nonnull;

import com.phloc.html.hc.IHCNode;
import com.phloc.webbasics.app.menu.IMenuItem;
import com.phloc.webbasics.app.menu.IMenuSeparator;

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
   * Render a menu item
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
  IHCNode renderMenuItem (@Nonnull IMenuItem aMenuItem, boolean bHasChildren, boolean bIsSelected, boolean bIsExpanded);
}
