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
package com.phloc.appbasics.app.menu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.page.IPage;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.url.ISimpleURL;

/**
 * Represents a proxy for the menu tree to avoid code duplication for the
 * different IMenuTree implementations
 * 
 * @author Philip Helger
 */
public class MenuTreeProxy
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MenuTreeProxy.class);

  private final IMenuTree m_aMenuTree;
  private String m_sDefaultMenuItem;

  public MenuTreeProxy (@Nonnull final IMenuTree aMenuTree)
  {
    if (aMenuTree == null)
      throw new NullPointerException ("menuTree");
    m_aMenuTree = aMenuTree;
  }

  @Nonnull
  private static <T extends IMenuObject> T _createChildItem (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aParentItem,
                                                             @Nonnull final T aMenuObject)
  {
    aParentItem.createChildItem (aMenuObject.getID (), aMenuObject);
    return aMenuObject;
  }

  @Nonnull
  public IMenuSeparator createRootSeparator ()
  {
    return _createChildItem (m_aMenuTree.getRootItem (), new MenuSeparator ());
  }

  @Nonnull
  public IMenuSeparator createSeparator (@Nonnull final String sParentID)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = m_aMenuTree.getItemWithID (sParentID);
    if (aParentItem == null)
      throw new IllegalArgumentException ("No such parent menu item '" + sParentID + "'");
    return _createChildItem (aParentItem, new MenuSeparator ());
  }

  @Nonnull
  public IMenuSeparator createSeparator (@Nonnull final IMenuItem aParent)
  {
    if (aParent == null)
      throw new NullPointerException ("parent");

    return createSeparator (aParent.getID ());
  }

  @Nonnull
  public IMenuItemPage createRootItem (@Nonnull final String sItemID, @Nonnull final IPage aPage)
  {
    return _createChildItem (m_aMenuTree.getRootItem (), new MenuItemPage (sItemID, aPage));
  }

  @Nonnull
  public IMenuItemPage createRootItem (@Nonnull final IPage aPage)
  {
    if (aPage == null)
      throw new NullPointerException ("page");

    return createRootItem (aPage.getID (), aPage);
  }

  @Nonnull
  public IMenuItemPage createItem (@Nonnull final String sParentID,
                                   @Nonnull final String sItemID,
                                   @Nonnull final IPage aPage)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = m_aMenuTree.getItemWithID (sParentID);
    if (aParentItem == null)
      throw new IllegalArgumentException ("No such parent menu item '" + sParentID + "'");
    return _createChildItem (aParentItem, new MenuItemPage (sItemID, aPage));
  }

  @Nonnull
  public IMenuItemPage createItem (@Nonnull final String sParentID, @Nonnull final IPage aPage)
  {
    if (aPage == null)
      throw new NullPointerException ("page");

    return createItem (sParentID, aPage.getID (), aPage);
  }

  @Nonnull
  public IMenuItemPage createItem (@Nonnull final IMenuItem aParent, @Nonnull final IPage aPage)
  {
    if (aParent == null)
      throw new NullPointerException ("parent");

    return createItem (aParent.getID (), aPage);
  }

  @Nonnull
  public IMenuItemExternal createRootItem (@Nonnull final String sItemID,
                                           @Nonnull final ISimpleURL aURL,
                                           @Nonnull final IHasDisplayText aName)
  {
    return _createChildItem (m_aMenuTree.getRootItem (), new MenuItemExternal (sItemID, aURL, aName));
  }

  @Nonnull
  public IMenuItemExternal createItem (@Nonnull final IMenuItem aParent,
                                       @Nonnull final String sItemID,
                                       @Nonnull final ISimpleURL aURL,
                                       @Nonnull final IHasDisplayText aName)
  {
    if (aParent == null)
      throw new NullPointerException ("parent");

    return createItem (aParent.getID (), sItemID, aURL, aName);
  }

  @Nonnull
  public IMenuItemExternal createItem (@Nonnull final String sParentID,
                                       @Nonnull final String sItemID,
                                       @Nonnull final ISimpleURL aURL,
                                       @Nonnull final IHasDisplayText aName)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = m_aMenuTree.getItemWithID (sParentID);
    if (aParentItem == null)
      throw new IllegalArgumentException ("No such parent menu item '" + sParentID + "'");
    return _createChildItem (aParentItem, new MenuItemExternal (sItemID, aURL, aName));
  }

  public void setDefaultMenuItemID (@Nullable final String sDefaultMenuItem)
  {
    m_sDefaultMenuItem = sDefaultMenuItem;
  }

  @Nullable
  public String getDefaultMenuItemID ()
  {
    return m_sDefaultMenuItem;
  }

  @Nullable
  public IMenuItem getDefaultMenuItem ()
  {
    if (m_sDefaultMenuItem != null)
    {
      final DefaultTreeItemWithID <String, IMenuObject> aDefaultMenuItem = m_aMenuTree.getItemWithID (m_sDefaultMenuItem);
      if (aDefaultMenuItem != null)
      {
        final IMenuObject aData = aDefaultMenuItem.getData ();
        if (aData instanceof IMenuItem)
          return (IMenuItem) aData;
        s_aLogger.warn ("The default menu object ID does not resolve to a menu item but to " +
                        CGStringHelper.getSafeClassName (aData));
      }
      else
        s_aLogger.warn ("Failed to resolve the default menu item ID '" + m_sDefaultMenuItem + "'");
    }
    return null;
  }

  @Nullable
  public IMenuObject getMenuObjectOfID (@Nullable final String sID)
  {
    return m_aMenuTree.getItemDataWithID (sID);
  }

  public void iterateAllMenuObjects (@Nonnull final INonThrowingRunnableWithParameter <IMenuObject> aCallback)
  {
    if (aCallback == null)
      throw new NullPointerException ("Callback");
    TreeWalker.walkTree (m_aMenuTree,
                         new DefaultHierarchyWalkerCallback <DefaultTreeItemWithID <String, IMenuObject>> ()
                         {
                           @Override
                           public final void onItemBeforeChildren (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aItem)
                           {
                             aCallback.run (aItem.getData ());
                           }
                         });
  }

}
