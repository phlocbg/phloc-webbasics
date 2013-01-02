package com.phloc.appbasics.app.menu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.page.IPage;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.tree.withid.unique.ITreeWithGlobalUniqueID;
import com.phloc.commons.url.ISimpleURL;

public interface IMenuTree extends ITreeWithGlobalUniqueID <String, IMenuObject, DefaultTreeItemWithID <String, IMenuObject>>
{
  /**
   * Append a new menu item separator at root level
   * 
   * @return The created menu item separator object. Never <code>null</code>.
   */
  @Nonnull
  IMenuSeparator createRootSeparator ();

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
  IMenuSeparator createSeparator (@Nonnull String sParentID);

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
  IMenuSeparator createSeparator (@Nonnull IMenuItem aParent);

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
  IMenuItemPage createRootItem (@Nonnull String sItemID, @Nonnull IPage aPage);

  /**
   * Append a new menu item at root level.
   * 
   * @param aPage
   *        The referenced page. May not be <code>null</code>.
   * @return The created menu item object. The ID of the menu item is the ID of
   *         the page. Never <code>null</code>.
   */
  @Nonnull
  IMenuItemPage createRootItem (@Nonnull IPage aPage);

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
  IMenuItemPage createItem (@Nonnull String sParentID, @Nonnull String sItemID, @Nonnull IPage aPage);

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
  IMenuItemPage createItem (@Nonnull String sParentID, @Nonnull IPage aPage);

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
  IMenuItemPage createItem (@Nonnull IMenuItem aParent, @Nonnull IPage aPage);

  /**
   * Append a new menu item at root level.
   * 
   * @param sItemID
   *        The new menu item ID. May not be <code>null</code>.
   * @param aURL
   *        The referenced URL. May not be <code>null</code>.
   * @param aName
   *        The name of the menu item. May not be <code>null</code>.
   * @return The created menu item object. Never <code>null</code>.
   */
  @Nonnull
  IMenuItemExternal createRootItem (@Nonnull String sItemID, @Nonnull ISimpleURL aURL, @Nonnull IHasDisplayText aName);

  /**
   * Append a new menu item below the specified parent.
   * 
   * @param aParent
   *        The parent menu item to append the item to. May not be
   *        <code>null</code>.
   * @param sItemID
   *        The new menu item ID. May not be <code>null</code>.
   * @param aURL
   *        The referenced URL. May not be <code>null</code>.
   * @param aName
   *        The name of the menu item. May not be <code>null</code>.
   * @return The created menu item object. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         If the passed parent menu item could not be resolved
   */
  @Nonnull
  IMenuItemExternal createItem (@Nonnull IMenuItem aParent,
                                @Nonnull String sItemID,
                                @Nonnull ISimpleURL aURL,
                                @Nonnull IHasDisplayText aName);

  /**
   * Append a new menu item below the specified parent.
   * 
   * @param sParentID
   *        The parent menu item ID to append the item to. May not be
   *        <code>null</code>.
   * @param sItemID
   *        The new menu item ID. May not be <code>null</code>.
   * @param aURL
   *        The referenced URL. May not be <code>null</code>.
   * @param aName
   *        The name of the menu item. May not be <code>null</code>.
   * @return The created menu item object. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         If the passed parent menu item could not be resolved
   */
  @Nonnull
  IMenuItemExternal createItem (@Nonnull String sParentID,
                                @Nonnull String sItemID,
                                @Nonnull ISimpleURL aURL,
                                @Nonnull IHasDisplayText aName);

  /**
   * Set the default menu item
   * 
   * @param sDefaultMenuItem
   *        The default menu item to be set. May be <code>null</code>.
   */
  void setDefaultMenuItemID (@Nullable String sDefaultMenuItem);

  /**
   * @return The default menu item ID. May be <code>null</code>.
   */
  @Nullable
  String getDefaultMenuItemID ();

  /**
   * Get the default menu item object.
   * 
   * @return <code>null</code> if either no default menu item is present, or the
   *         default menu item ID could not be resolved to a menu item
   */
  @Nullable
  IMenuItem getDefaultMenuItem ();

  /**
   * Get the menu object with the specified ID
   * 
   * @param sID
   *        The ID to be resolved. May be <code>null</code>.
   * @return <code>null</code> if the menu item could not be resolved
   */
  @Nullable
  IMenuObject getMenuObjectOfID (@Nullable String sID);
}
