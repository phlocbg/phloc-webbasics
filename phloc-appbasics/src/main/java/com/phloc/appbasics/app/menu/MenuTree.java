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
package com.phloc.appbasics.app.menu;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.page.IPage;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.tree.withid.unique.DefaultTreeWithGlobalUniqueID;
import com.phloc.commons.url.ISimpleURL;

/**
 * Represents a single menu tree
 * 
 * @author Philip Helger
 */
public class MenuTree extends DefaultTreeWithGlobalUniqueID <String, IMenuObject> implements IMenuTree
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MenuTree.class);

  private List <String> m_aDefaultMenuItemIDs;

  public MenuTree ()
  {}

  @Nonnull
  private static <T extends IMenuObject> T _createChildItem (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aParentItem,
                                                             @Nonnull final T aMenuObject)
  {
    if (aParentItem.createChildItem (aMenuObject.getID (), aMenuObject, false) == null)
      throw new IllegalArgumentException ("Failed to add the menu object " +
                                          aMenuObject +
                                          " probably the ID is already contained!");
    return aMenuObject;
  }

  @Nonnull
  public IMenuSeparator createRootSeparator ()
  {
    return _createChildItem (getRootItem (), new MenuSeparator ());
  }

  @Nonnull
  public IMenuSeparator createSeparator (@Nonnull final String sParentID)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = getItemWithID (sParentID);
    if (aParentItem == null)
      throw new IllegalArgumentException ("No such parent menu item '" + sParentID + "'");
    return _createChildItem (aParentItem, new MenuSeparator ());
  }

  @Nonnull
  public IMenuSeparator createSeparator (@Nonnull final IMenuItem aParent)
  {
    ValueEnforcer.notNull (aParent, "Parent");

    return createSeparator (aParent.getID ());
  }

  @Nonnull
  public IMenuItemPage createRootItem (@Nonnull final String sItemID, @Nonnull final IPage aPage)
  {
    return _createChildItem (getRootItem (), new MenuItemPage (sItemID, aPage));
  }

  @Nonnull
  public IMenuItemPage createRootItem (@Nonnull final IPage aPage)
  {
    ValueEnforcer.notNull (aPage, "Page");

    return createRootItem (aPage.getID (), aPage);
  }

  @Nonnull
  public IMenuItemPage createItem (@Nonnull final String sParentID,
                                   @Nonnull final String sItemID,
                                   @Nonnull final IPage aPage)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = getItemWithID (sParentID);
    if (aParentItem == null)
      throw new IllegalArgumentException ("No such parent menu item '" + sParentID + "'");
    return _createChildItem (aParentItem, new MenuItemPage (sItemID, aPage));
  }

  @Nonnull
  public IMenuItemPage createItem (@Nonnull final String sParentID, @Nonnull final IPage aPage)
  {
    ValueEnforcer.notNull (aPage, "Page");

    return createItem (sParentID, aPage.getID (), aPage);
  }

  @Nonnull
  public IMenuItemPage createItem (@Nonnull final IMenuItem aParent, @Nonnull final IPage aPage)
  {
    ValueEnforcer.notNull (aParent, "Parent");

    return createItem (aParent.getID (), aPage);
  }

  @Nonnull
  public IMenuItemExternal createRootItem (@Nonnull final String sItemID,
                                           @Nonnull final ISimpleURL aURL,
                                           @Nonnull final IHasDisplayText aName)
  {
    return _createChildItem (getRootItem (), new MenuItemExternal (sItemID, aURL, aName));
  }

  @Nonnull
  public IMenuItemExternal createItem (@Nonnull final IMenuItem aParent,
                                       @Nonnull final String sItemID,
                                       @Nonnull final ISimpleURL aURL,
                                       @Nonnull final IHasDisplayText aName)
  {
    ValueEnforcer.notNull (aParent, "Parent");

    return createItem (aParent.getID (), sItemID, aURL, aName);
  }

  @Nonnull
  public IMenuItemExternal createItem (@Nonnull final String sParentID,
                                       @Nonnull final String sItemID,
                                       @Nonnull final ISimpleURL aURL,
                                       @Nonnull final IHasDisplayText aName)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = getItemWithID (sParentID);
    if (aParentItem == null)
      throw new IllegalArgumentException ("No such parent menu item '" + sParentID + "'");
    return _createChildItem (aParentItem, new MenuItemExternal (sItemID, aURL, aName));
  }

  public void setDefaultMenuItemID (@Nullable final String sDefaultMenuItemID)
  {
    m_aDefaultMenuItemIDs = sDefaultMenuItemID == null ? null : ContainerHelper.newList (sDefaultMenuItemID);
  }

  public void setDefaultMenuItemIDs (@Nullable final String... aDefaultMenuItemIDs)
  {
    m_aDefaultMenuItemIDs = ArrayHelper.isEmpty (aDefaultMenuItemIDs) ? null
                                                                     : ContainerHelper.newList (aDefaultMenuItemIDs);
  }

  public void setDefaultMenuItemIDs (@Nullable final List <String> aDefaultMenuItemIDs)
  {
    m_aDefaultMenuItemIDs = ContainerHelper.isEmpty (aDefaultMenuItemIDs) ? null
                                                                         : ContainerHelper.newList (aDefaultMenuItemIDs);
  }

  @Nullable
  public String getDefaultMenuItemID ()
  {
    return ContainerHelper.getFirstElement (m_aDefaultMenuItemIDs);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllDefaultMenuItemIDs ()
  {
    return ContainerHelper.newList (m_aDefaultMenuItemIDs);
  }

  @Nullable
  private IMenuItemPage _getDefaultMenuItem (@Nullable final String sMenuItemID)
  {
    if (sMenuItemID != null)
    {
      // Resolve default menu item ID
      final DefaultTreeItemWithID <String, IMenuObject> aTreeItem = getItemWithID (sMenuItemID);
      if (aTreeItem != null)
      {
        final IMenuObject aMenuItem = aTreeItem.getData ();
        if (aMenuItem instanceof IMenuItemPage)
          return (IMenuItemPage) aMenuItem;
        s_aLogger.warn ("The default menu object ID '" +
                        sMenuItemID +
                        "' does not resolve to an IMenuItemPage but to " +
                        CGStringHelper.getSafeClassName (aMenuItem));
      }
      else
        s_aLogger.warn ("Failed to resolve the default menu item ID '" + sMenuItemID + "'");
    }
    return null;
  }

  @Nullable
  public IMenuItemPage getDefaultMenuItem ()
  {
    return _getDefaultMenuItem (getDefaultMenuItemID ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IMenuItemPage> getAllDefaultMenuItems ()
  {
    final List <IMenuItemPage> ret = new ArrayList <IMenuItemPage> ();
    if (m_aDefaultMenuItemIDs != null)
      for (final String sDefaultMenuItemID : m_aDefaultMenuItemIDs)
      {
        final IMenuItemPage aDefaultMenuItem = _getDefaultMenuItem (sDefaultMenuItemID);
        if (aDefaultMenuItem != null)
          ret.add (aDefaultMenuItem);
      }
    return ret;
  }

  @Nullable
  public IMenuObject getMenuObjectOfID (@Nullable final String sID)
  {
    return getItemDataWithID (sID);
  }

  public void iterateAllMenuObjects (@Nonnull final INonThrowingRunnableWithParameter <IMenuObject> aCallback)
  {
    ValueEnforcer.notNull (aCallback, "Callback");

    TreeWalker.walkTree (this, new DefaultHierarchyWalkerCallback <DefaultTreeItemWithID <String, IMenuObject>> ()
    {
      @Override
      public final void onItemBeforeChildren (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aItem)
      {
        aCallback.run (aItem.getData ());
      }
    });
  }

  @Nullable
  public IMenuItemPage replaceMenuItem (@Nonnull final IPage aNewPage)
  {
    ValueEnforcer.notNull (aNewPage, "NewPage");

    final String sID = aNewPage.getID ();
    final DefaultTreeItemWithID <String, IMenuObject> aItem = getItemWithID (sID);
    if (aItem == null)
      return null;

    final IMenuItemPage ret = new MenuItemPage (sID, aNewPage);
    aItem.setData (ret);
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final MenuTree rhs = (MenuTree) o;
    return EqualsUtils.equals (m_aDefaultMenuItemIDs, rhs.m_aDefaultMenuItemIDs);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aDefaultMenuItemIDs).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .appendIfNotNull ("defaultMenuItemIDs", m_aDefaultMenuItemIDs)
                            .toString ();
  }
}
