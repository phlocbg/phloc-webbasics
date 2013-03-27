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
package com.phloc.webdemoapp.app.layout.view;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.appbasics.app.menu.MenuItemDeterminatorCallback;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.webctrls.bootstrap.ext.BootstrapMenuItemRendererWell;
import com.phloc.webdemoapp.app.menu.view.CDemoAppMenuView;

/**
 * The content provider for the menu area.
 * 
 * @author philip
 */
public final class ViewMenuProvider
{
  private ViewMenuProvider ()
  {}

  @Nonnull
  public static IHCNode getContent (@Nonnull final Locale aDisplayLocale)
  {
    // Main menu
    final IMenuTree aMenuTree = ApplicationMenuTree.getInstance ();
    final MenuItemDeterminatorCallback aCallback = new MenuItemDeterminatorCallback (aMenuTree)
    {
      @Override
      protected boolean isMenuItemValidToBeDisplayed (@Nonnull final IMenuObject aMenuObj)
      {
        // Don't show items that belong to the footer
        if (aMenuObj.containsFlag (CDemoAppMenuView.FLAG_FOOTER))
          return false;

        // Use default code
        return super.isMenuItemValidToBeDisplayed (aMenuObj);
      }
    };
    final IHCElement <?> aMenu = BootstrapMenuItemRendererWell.createSideBarMenu (aMenuTree, aCallback, aDisplayLocale);

    return aMenu;
  }
}
