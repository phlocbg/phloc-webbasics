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
package com.phloc.webdemoapp.app.menu.view;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.webdemoapp.page.view.PageDemo;
import com.phloc.webpages.PageViewExternal;

@Immutable
public final class MenuView
{
  private MenuView ()
  {}

  public static void init (@Nonnull final IMenuTree aMenuTree)
  {
    // Common stuff
    aMenuTree.createRootItem (new PageDemo (CDemoAppMenuView.MENU_DEMO));
    aMenuTree.createRootItem (new PageViewExternal (CDemoAppMenuView.MENU_SITENOTICE,
                                                    "Site notice",
                                                    new ClassPathResource ("viewpages/en/site-notice.xml")));

    aMenuTree.createRootItem (new PageViewExternal (CDemoAppMenuView.MENU_GTC,
                                                    "GTC",
                                                    new ClassPathResource ("viewpages/en/gtc.xml")))
             .addFlag (CDemoAppMenuView.FLAG_FOOTER);

    // Set default
    aMenuTree.setDefaultMenuItemID (CDemoAppMenuView.MENU_SITENOTICE);
  }
}
