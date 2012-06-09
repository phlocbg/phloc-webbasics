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

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerDynamicCallback;
import com.phloc.commons.hierarchy.EHierarchyCallbackReturn;
import com.phloc.commons.tree.utils.walk.TreeWalkerDynamic;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.css.DefaultCSSClassProvider;
import com.phloc.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.webbasics.app.ApplicationRequestManager;
import com.phloc.webbasics.app.menu.IMenuItem;
import com.phloc.webbasics.app.menu.IMenuObject;
import com.phloc.webbasics.app.menu.IMenuSeparator;
import com.phloc.webbasics.app.menu.MenuTree;

/**
 * Renders menu item nodes.
 * 
 * @author philip
 */
public class MenuRendererCallback extends
                                 DefaultHierarchyWalkerDynamicCallback <DefaultTreeItemWithID <String, IMenuObject>>
{
  public static final ICSSClassProvider CSS_CLASS_MENU_SEPARATOR = DefaultCSSClassProvider.create ("menu_separator");
  public static final ICSSClassProvider CSS_CLASS_MENU_ITEM = DefaultCSSClassProvider.create ("menu_item");
  public static final ICSSClassProvider CSS_CLASS_SELECTED_MENU_ITEM = DefaultCSSClassProvider.create ("selected_menu_item");
  public static final String CSS_ID_PREFIX_MENU_ITEM = "menu_item_";

  private final NonBlockingStack <HCUL> m_aMenuListStack;
  private final IMenuItemRenderer m_aRenderer;
  private final Map <String, Boolean> m_aDisplayMenuItemIDs;
  private final NonBlockingStack <HCLI> m_aMenuItemStack = new NonBlockingStack <HCLI> ();
  private final NonBlockingStack <AtomicInteger> m_aChildCountStack = new NonBlockingStack <AtomicInteger> ();
  private final NonBlockingStack <DefaultTreeItemWithID <String, IMenuObject>> m_aTreeItemStack = new NonBlockingStack <DefaultTreeItemWithID <String, IMenuObject>> ();
  private final String m_sSelectedItem;

  protected MenuRendererCallback (@Nonnull final NonBlockingStack <HCUL> aMenuListStack,
                                  @Nonnull final IMenuItemRenderer aRenderer,
                                  @Nonnull final Map <String, Boolean> aDisplayMenuItemIDs)
  {
    if (aMenuListStack == null)
      throw new NullPointerException ("nodeStack");
    if (aRenderer == null)
      throw new NullPointerException ("renderer");
    if (aDisplayMenuItemIDs == null)
      throw new NullPointerException ("displayMenuItemIDs");

    m_aMenuListStack = aMenuListStack;
    m_aRenderer = aRenderer;
    m_aDisplayMenuItemIDs = aDisplayMenuItemIDs;

    m_aChildCountStack.push (new AtomicInteger (0));
    m_sSelectedItem = ApplicationRequestManager.getRequestMenuItemID ();
    // The selected item may be null if an invalid menu item ID was passed
  }

  @Override
  public final void onLevelDown ()
  {
    super.onLevelDown ();

    // Check if any child is visible
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = m_aTreeItemStack.peek ();
    for (final DefaultTreeItemWithID <String, IMenuObject> aChildItem : aParentItem.getChildren ())
      if (m_aDisplayMenuItemIDs.containsKey (aChildItem.getID ()))
      {
        // add sub menu structure at the right place
        m_aMenuListStack.push (m_aMenuItemStack.peek ().addAndReturnChild (new HCUL ()));
        break;
      }

    m_aChildCountStack.push (new AtomicInteger (0));
  }

  @Override
  public final void onLevelUp ()
  {
    final AtomicInteger aChildCount = m_aChildCountStack.pop ();
    if (aChildCount.intValue () > 0)
      m_aMenuListStack.pop ();
    super.onLevelUp ();
  }

  @Override
  public final EHierarchyCallbackReturn onItemBeforeChildren (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aItem)
  {
    m_aTreeItemStack.push (aItem);

    final Boolean aExpandedState = m_aDisplayMenuItemIDs.get (aItem.getID ());
    if (aExpandedState != null)
    {
      final HCUL aParent = m_aMenuListStack.peek ();
      final IMenuObject aMenuObj = aItem.getData ();
      if (aMenuObj instanceof IMenuSeparator)
      {
        // separator
        final IHCNode aHCNode = m_aRenderer.renderSeparator ((IMenuSeparator) aMenuObj);
        m_aMenuItemStack.push (aParent.addAndReturnItem (aHCNode).addClass (CSS_CLASS_MENU_SEPARATOR));
      }
      else
      {
        // item
        final IHCNode aHCNode = m_aRenderer.renderMenuItem ((IMenuItem) aMenuObj,
                                                            aItem.hasChildren (),
                                                            aMenuObj.getID ().equals (m_sSelectedItem),
                                                            aExpandedState.booleanValue ());
        m_aMenuItemStack.push (aParent.addAndReturnItem (aHCNode).addClass (CSS_CLASS_MENU_ITEM));
      }
      m_aChildCountStack.peek ().incrementAndGet ();
      return EHierarchyCallbackReturn.CONTINUE;
    }

    // Item should not be displayed
    // push fake item so the pop does not remove anything important!
    m_aMenuItemStack.push (new HCLI ());
    return EHierarchyCallbackReturn.USE_NEXT_SIBLING;
  }

  @Override
  @Nonnull
  public final EHierarchyCallbackReturn onItemAfterChildren (final DefaultTreeItemWithID <String, IMenuObject> aItem)
  {
    m_aTreeItemStack.pop ();
    m_aMenuItemStack.pop ();
    return EHierarchyCallbackReturn.CONTINUE;
  }

  /**
   * Render the whole menu
   * 
   * @param aRenderer
   *        The renderer to use
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static IHCNode createRenderedMenu (@Nonnull final IMenuItemRenderer aRenderer)
  {
    return createRenderedMenu (MenuTree.getInstance ().getRootItem (),
                               aRenderer,
                               MenuItemDeterminatorCallback.getAllDisplayMenuItemIDs ());
  }

  /**
   * Render a part of the menu
   * 
   * @param aStartTreeItem
   *        The start tree to iterate
   * @param aRenderer
   *        The renderer to use
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static IHCNode createRenderedMenu (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aStartTreeItem,
                                            @Nonnull final IMenuItemRenderer aRenderer)
  {
    return createRenderedMenu (aStartTreeItem, aRenderer, MenuItemDeterminatorCallback.getAllDisplayMenuItemIDs ());
  }

  /**
   * Render the whole menu
   * 
   * @param aRenderer
   *        The renderer to use
   * @param aDisplayMenuItemIDs
   *        The menu items to display as a map from menu item ID to expanded
   *        state
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static IHCNode createRenderedMenu (@Nonnull final IMenuItemRenderer aRenderer,
                                            @Nonnull final Map <String, Boolean> aDisplayMenuItemIDs)
  {
    return createRenderedMenu (MenuTree.getInstance ().getRootItem (), aRenderer, aDisplayMenuItemIDs);
  }

  /**
   * Render a part of the menu
   * 
   * @param aStartTreeItem
   *        The start tree to iterate
   * @param aRenderer
   *        The renderer to use
   * @param aDisplayMenuItemIDs
   *        The menu items to display as a map from menu item ID to expanded
   *        state
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static IHCNode createRenderedMenu (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aStartTreeItem,
                                            @Nonnull final IMenuItemRenderer aRenderer,
                                            @Nonnull final Map <String, Boolean> aDisplayMenuItemIDs)
  {
    final NonBlockingStack <HCUL> aNodeStack = new NonBlockingStack <HCUL> (new HCUL ());
    TreeWalkerDynamic.walkSubTree (aStartTreeItem,
                                   new MenuRendererCallback (aNodeStack, aRenderer, aDisplayMenuItemIDs));
    if (aNodeStack.size () != 1)
      throw new IllegalStateException ("Stack is inconsistent: " + aNodeStack);

    // Return the remaining UL
    return aNodeStack.pop ();
  }
}
