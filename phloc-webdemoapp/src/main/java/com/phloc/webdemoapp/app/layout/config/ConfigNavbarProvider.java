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
package com.phloc.webdemoapp.app.layout.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webctrls.bootstrap.BootstrapBreadcrumb;
import com.phloc.webctrls.bootstrap.BootstrapContainer;
import com.phloc.webctrls.bootstrap.BootstrapRow;
import com.phloc.webctrls.bootstrap.EBootstrapSpan;

/**
 * The content provider for the navigation bar area.
 * 
 * @author philip
 */
public final class ConfigNavbarProvider
{
  private ConfigNavbarProvider ()
  {}

  private static void _getBreadCrumbs (final BootstrapBreadcrumb aBreadcrumb, final Locale aDisplayLocale)
  {
    final List <IMenuItem> aItems = new ArrayList <IMenuItem> ();
    IMenuItem aCurrent = ApplicationRequestManager.getInstance ().getRequestMenuItem ();
    while (aCurrent != null)
    {
      aItems.add (0, aCurrent);
      final DefaultTreeItemWithID <String, IMenuObject> aTreeItem = ApplicationMenuTree.getInstance ()
                                                                                       .getItemWithID (aCurrent.getID ());
      aCurrent = aTreeItem.isRootItem () ? null : (IMenuItem) aTreeItem.getParent ().getData ();
    }

    final int nItems = aItems.size ();
    if (nItems >= 0)
    {
      for (int i = 0; i < nItems; ++i)
      {
        final IMenuItem aItem = aItems.get (i);

        // add separator?
        if (i > 0)
          aBreadcrumb.addSeparator (" | ");

        // Create link on all but the last item
        if (i < nItems - 1)
          aBreadcrumb.addLink (LinkUtils.getLinkToMenuItem (aItem.getID ()), aItem.getDisplayText (aDisplayLocale));
        else
          aBreadcrumb.addActive (aItem.getDisplayText (aDisplayLocale));
      }
    }
  }

  @Nonnull
  public static BootstrapContainer getContent (final Locale aDisplayLocale)
  {
    final BootstrapBreadcrumb aBreadcrumb = new BootstrapBreadcrumb ();
    _getBreadCrumbs (aBreadcrumb, aDisplayLocale);

    final BootstrapContainer aCont = new BootstrapContainer ();
    final BootstrapRow aRow = new BootstrapRow ();
    aRow.addColumn (EBootstrapSpan.SPAN12, aBreadcrumb);
    aCont.setContent (aRow);
    return aCont;
  }
}
