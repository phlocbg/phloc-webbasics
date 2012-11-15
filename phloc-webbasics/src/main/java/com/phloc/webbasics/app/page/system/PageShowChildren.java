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
package com.phloc.webbasics.app.page.system;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.MenuTree;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.page.AbstractWebPage;

public class PageShowChildren extends AbstractWebPage
{
  private static final class ShowChildren extends
                                         DefaultHierarchyWalkerCallback <DefaultTreeItemWithID <String, IMenuObject>>
  {
    private final Locale m_aDisplayLocale;
    private final NonBlockingStack <HCUL> m_aStack;

    private ShowChildren (@Nonnull final HCUL aUL, @Nonnull final Locale aDisplayLocale)
    {
      m_aDisplayLocale = aDisplayLocale;
      m_aStack = new NonBlockingStack <HCUL> (aUL);
    }

    @Override
    public void onLevelDown ()
    {
      super.onLevelDown ();
      final HCUL aNewLevel = new HCUL ();
      m_aStack.peek ().getLastItem ().addChild (aNewLevel);
      m_aStack.push (aNewLevel);
    }

    @Override
    public void onLevelUp ()
    {
      m_aStack.pop ();
      super.onLevelUp ();
    }

    @Override
    public void onItemBeforeChildren (@Nullable final DefaultTreeItemWithID <String, IMenuObject> aTreeItem)
    {
      final IMenuObject aMenuObj = aTreeItem == null ? null : aTreeItem.getData ();
      if (aMenuObj instanceof IMenuItem)
      {
        final IMenuItem aMenuItem = (IMenuItem) aMenuObj;
        if (aMenuItem.matchesDisplayFilter ())
        {
          // Item as link to menu item
          m_aStack.peek ()
                  .addItem (new HCA (LinkUtils.getLinkToMenuItem (aMenuItem.getID ())).addChild (aMenuItem.getDisplayText (m_aDisplayLocale)));
        }
      }
    }
  }

  public PageShowChildren (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  @Override
  protected void fillContent (@Nonnull final Locale aDisplayLocale, @Nonnull final HCNodeList aNodeList)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aMenuTreeItem = MenuTree.getInstance ().getItemWithID (getID ());
    if (aMenuTreeItem != null && (aMenuTreeItem.getData () instanceof IMenuItem))
    {
      final HCUL aUL = new HCUL ();
      TreeWalker.walkSubTree (aMenuTreeItem, new ShowChildren (aUL, aDisplayLocale));
      aNodeList.addChild (aUL);
    }
  }
}
