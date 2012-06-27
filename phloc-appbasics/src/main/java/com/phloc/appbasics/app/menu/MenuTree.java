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
package com.phloc.appbasics.app.menu;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.page.IBasePage;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.tree.withid.unique.DefaultTreeWithGlobalUniqueID;
import com.phloc.commons.tree.withid.unique.ITreeWithGlobalUniqueID;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;

/**
 * Represents the menu tree
 * 
 * @author philip
 */
public final class MenuTree extends GlobalSingleton implements
                                                   ITreeWithGlobalUniqueID <String, IMenuObject, DefaultTreeItemWithID <String, IMenuObject>>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MenuTree.class);
  private final DefaultTreeWithGlobalUniqueID <String, IMenuObject> m_aTree = new DefaultTreeWithGlobalUniqueID <String, IMenuObject> ();

  private String m_sDefaultMenuItem;

  @UsedViaReflection
  @Deprecated
  public MenuTree ()
  {}

  @Nonnull
  public static MenuTree getInstance ()
  {
    return getGlobalSingleton (MenuTree.class);
  }

  @Nonnull
  private static <T extends IMenuObject> T _createChildItem (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aParentItem,
                                                             @Nonnull final T aMenuObject)
  {
    aParentItem.createChildItem (aMenuObject.getID (), aMenuObject);
    return aMenuObject;
  }

  /**
   * Append a new menu item separator at root level
   * 
   * @return The created menu item separator object. Never <code>null</code>.
   */
  @Nonnull
  public IMenuSeparator createRootSeparator ()
  {
    return _createChildItem (getRootItem (), new MenuSeparator ());
  }

  /**
   * Append a new menu item separator as a child of the passed menu item
   * 
   * @param sParentID
   *        The parent menu item ID to append the separator to. May not be
   *        <code>null</code>.
   * @return The created menu item separator object. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         If the passed parent menu item could not be resolved
   */
  @Nonnull
  public IMenuSeparator createSeparator (@Nonnull final String sParentID)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = getItemWithID (sParentID);
    if (aParentItem == null)
      throw new IllegalArgumentException ("No such parent menu item '" + sParentID + "'");
    return _createChildItem (aParentItem, new MenuSeparator ());
  }

  /**
   * Append a new menu item separator as a child of the passed menu item
   * 
   * @param aParent
   *        The parent menu item to append the separator to. May not be
   *        <code>null</code>.
   * @return The created menu item separator object. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         If the passed parent menu item could not be resolved
   */
  @Nonnull
  public IMenuSeparator createSeparator (@Nonnull final IMenuItem aParent)
  {
    if (aParent == null)
      throw new NullPointerException ("parent");

    return createSeparator (aParent.getID ());
  }

  /**
   * Append a new menu item at root level.
   * 
   * @param sItemID
   *        The new menu item ID. May not be <code>null</code>.
   * @param aPage
   *        The referenced page. May not be <code>null</code>.
   * @return The created menu item object. Never <code>null</code>.
   */
  @Nonnull
  public IMenuItem createRootItem (@Nonnull final String sItemID, @Nonnull final IBasePage aPage)
  {
    return _createChildItem (getRootItem (), new MenuItem (sItemID, aPage));
  }

  /**
   * Append a new menu item at root level.
   * 
   * @param aPage
   *        The referenced page. May not be <code>null</code>.
   * @return The created menu item object. The ID of the menu item is the ID of
   *         the page. Never <code>null</code>.
   */
  @Nonnull
  public IMenuItem createRootItem (@Nonnull final IBasePage aPage)
  {
    if (aPage == null)
      throw new NullPointerException ("page");

    return createRootItem (aPage.getID (), aPage);
  }

  /**
   * Append a new menu item below the specified parent.
   * 
   * @param sParentID
   *        The parent menu item ID to append the item to. May not be
   *        <code>null</code>.
   * @param sItemID
   *        The new menu item ID. May not be <code>null</code>.
   * @param aPage
   *        The referenced page. May not be <code>null</code>.
   * @return The created menu item object. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         If the passed parent menu item could not be resolved
   */
  @Nonnull
  public IMenuItem createItem (@Nonnull final String sParentID,
                               @Nonnull final String sItemID,
                               @Nonnull final IBasePage aPage)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = getItemWithID (sParentID);
    if (aParentItem == null)
      throw new IllegalArgumentException ("No such parent menu item '" + sParentID + "'");
    return _createChildItem (aParentItem, new MenuItem (sItemID, aPage));
  }

  /**
   * Append a new menu item below the specified parent.
   * 
   * @param sParentID
   *        The parent menu item ID to append the item to. May not be
   *        <code>null</code>.
   * @param aPage
   *        The referenced page. May not be <code>null</code>.
   * @return The created menu item object. The ID of the menu item is the ID of
   *         the page. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         If the passed parent menu item could not be resolved
   */
  @Nonnull
  public IMenuItem createItem (@Nonnull final String sParentID, @Nonnull final IBasePage aPage)
  {
    if (aPage == null)
      throw new NullPointerException ("page");

    return createItem (sParentID, aPage.getID (), aPage);
  }

  /**
   * Append a new menu item below the specified parent.
   * 
   * @param aParent
   *        The parent menu item to append the item to. May not be
   *        <code>null</code>.
   * @param aPage
   *        The referenced page. May not be <code>null</code>.
   * @return The created menu item object. The ID of the menu item is the ID of
   *         the page. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         If the passed parent menu item could not be resolved
   */
  @Nonnull
  public IMenuItem createItem (@Nonnull final IMenuItem aParent, @Nonnull final IBasePage aPage)
  {
    if (aParent == null)
      throw new NullPointerException ("parent");

    return createItem (aParent.getID (), aPage);
  }

  @Deprecated
  public void setDefaultMenuItem (@Nullable final String sDefaultMenuItem)
  {
    setDefaultMenuItemID (sDefaultMenuItem);
  }

  /**
   * Set the default menu item
   * 
   * @param sDefaultMenuItem
   *        The default menu item to be set. May be <code>null</code>.
   */
  public void setDefaultMenuItemID (@Nullable final String sDefaultMenuItem)
  {
    m_sDefaultMenuItem = sDefaultMenuItem;
  }

  /**
   * @return The default menu item ID. May be <code>null</code>.
   */
  @Nullable
  public String getDefaultMenuItemID ()
  {
    return m_sDefaultMenuItem;
  }

  /**
   * Get the default menu item object.
   * 
   * @return <code>null</code> if either no default menu item is present, or the
   *         default menu item ID could not be resolved to a menu item
   */
  @Nullable
  public IMenuItem getDefaultMenuItem ()
  {
    if (m_sDefaultMenuItem != null)
    {
      final DefaultTreeItemWithID <String, IMenuObject> aDefaultMenuItem = getItemWithID (m_sDefaultMenuItem);
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

  /**
   * Get the menu object with the specified ID
   * 
   * @param sID
   *        The ID to be resolved. May be <code>null</code>.
   * @return <code>null</code> if the menu item could not be resolved
   */
  @Nullable
  public IMenuObject getMenuObjectOfID (@Nullable final String sID)
  {
    final DefaultTreeItemWithID <String, IMenuObject> aTreeItem = getItemWithID (sID);
    return aTreeItem == null ? null : aTreeItem.getData ();
  }

  public DefaultTreeItemWithID <String, IMenuObject> getRootItem ()
  {
    return m_aTree.getRootItem ();
  }

  public DefaultTreeItemWithID <String, IMenuObject> getChildWithID (final DefaultTreeItemWithID <String, IMenuObject> aCurrent,
                                                                     final String aID)
  {
    return m_aTree.getChildWithID (aCurrent, aID);
  }

  public boolean hasChildren (final DefaultTreeItemWithID <String, IMenuObject> aCurrent)
  {
    return m_aTree.hasChildren (aCurrent);
  }

  public int getChildCount (final DefaultTreeItemWithID <String, IMenuObject> aCurrent)
  {
    return m_aTree.getChildCount (aCurrent);
  }

  public Collection <? extends DefaultTreeItemWithID <String, IMenuObject>> getChildren (final DefaultTreeItemWithID <String, IMenuObject> aCurrent)
  {
    return m_aTree.getChildren (aCurrent);
  }

  public DefaultTreeItemWithID <String, IMenuObject> getItemWithID (final String aDataID)
  {
    return m_aTree.getItemWithID (aDataID);
  }

  public Collection <DefaultTreeItemWithID <String, IMenuObject>> getAllItems ()
  {
    return m_aTree.getAllItems ();
  }

  public boolean isItemSameOrDescendant (final String aParentItemID, final String aChildItemID)
  {
    return m_aTree.isItemSameOrDescendant (aParentItemID, aChildItemID);
  }
}
