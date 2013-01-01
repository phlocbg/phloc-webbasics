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
package com.phloc.webbasics.app.menu.ui;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.appbasics.app.menu.MenuTree;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerDynamicCallback;
import com.phloc.commons.hierarchy.EHierarchyCallbackReturn;
import com.phloc.commons.tree.utils.walk.TreeWalkerDynamic;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;

/**
 * Renders menu item nodes.
 * 
 * @author philip
 */
public class MenuRendererCallback extends
                                 DefaultHierarchyWalkerDynamicCallback <DefaultTreeItemWithID <String, IMenuObject>>
{
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
        final HCUL aNewLevel = new HCUL ();
        m_aRenderer.onLevelDown (aNewLevel);
        m_aMenuListStack.push (m_aMenuItemStack.peek ().addAndReturnChild (aNewLevel));
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
        HCLI aLI;
        if (aHCNode instanceof HCLI)
          aLI = aParent.addAndReturnItem ((HCLI) aHCNode);
        else
          aLI = aParent.addAndReturnItem (aHCNode);
        m_aRenderer.onMenuSeparatorItem (aLI);
        m_aMenuItemStack.push (aLI);
      }
      else
      {
        final boolean bSelected = aMenuObj.getID ().equals (m_sSelectedItem);
        if (aMenuObj instanceof IMenuItemPage)
        {
          // page item
          final IHCNode aHCNode = m_aRenderer.renderMenuItemPage ((IMenuItemPage) aMenuObj,
                                                                  aItem.hasChildren (),
                                                                  bSelected,
                                                                  aExpandedState.booleanValue ());
          HCLI aLI;
          if (aHCNode instanceof HCLI)
            aLI = aParent.addAndReturnItem ((HCLI) aHCNode);
          else
            aLI = aParent.addAndReturnItem (aHCNode);
          m_aRenderer.onMenuItemPageItem (aLI, bSelected);
          m_aMenuItemStack.push (aLI);
        }
        else
          if (aMenuObj instanceof IMenuItemExternal)
          {
            // external item
            final IHCNode aHCNode = m_aRenderer.renderMenuItemExternal ((IMenuItemExternal) aMenuObj,
                                                                        aItem.hasChildren (),
                                                                        bSelected,
                                                                        aExpandedState.booleanValue ());
            HCLI aLI;
            if (aHCNode instanceof HCLI)
              aLI = aParent.addAndReturnItem ((HCLI) aHCNode);
            else
              aLI = aParent.addAndReturnItem (aHCNode);
            m_aRenderer.onMenuItemExternalItem (aLI, bSelected);
            m_aMenuItemStack.push (aLI);
          }
          else
            throw new IllegalStateException ();
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
  public static HCUL createRenderedMenu (@Nonnull final IMenuItemRenderer aRenderer)
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
  public static HCUL createRenderedMenu (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aStartTreeItem,
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
  public static HCUL createRenderedMenu (@Nonnull final IMenuItemRenderer aRenderer,
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
  public static HCUL createRenderedMenu (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aStartTreeItem,
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
