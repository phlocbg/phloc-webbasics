/**
 * Copyright (C) 2006-2015 phloc systems
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
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.appbasics.app.menu.MenuItemDeterminatorCallback;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.factory.IFactory;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerDynamicCallback;
import com.phloc.commons.hierarchy.EHierarchyCallbackReturn;
import com.phloc.commons.tree.utils.walk.TreeWalkerDynamic;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCList;
import com.phloc.html.hc.html.HCLI;
import com.phloc.webbasics.app.ISimpleWebExecutionContext;
import com.phloc.webbasics.app.layout.ILayoutExecutionContext;

/**
 * Renders menu item nodes.
 * 
 * @author Philip Helger
 */
public class MenuRendererCallback <T extends AbstractHCList <?>> extends DefaultHierarchyWalkerDynamicCallback <DefaultTreeItemWithID <String, IMenuObject>>
{
  private final ISimpleWebExecutionContext m_aSWEC;
  private final IFactory <T> m_aFactory;
  private final NonBlockingStack <T> m_aMenuListStack;
  private final IMenuItemRenderer <T> m_aRenderer;
  private final Map <String, Boolean> m_aDisplayMenuItemIDs;
  private final NonBlockingStack <HCLI> m_aMenuItemStack = new NonBlockingStack <HCLI> ();
  private final NonBlockingStack <AtomicInteger> m_aChildCountStack = new NonBlockingStack <AtomicInteger> ();
  private final NonBlockingStack <DefaultTreeItemWithID <String, IMenuObject>> m_aTreeItemStack = new NonBlockingStack <DefaultTreeItemWithID <String, IMenuObject>> ();
  private final String m_sSelectedItem;

  protected MenuRendererCallback (@Nonnull final ISimpleWebExecutionContext aSWEC,
                                  @Nonnull final IFactory <T> aFactory,
                                  @Nonnull final NonBlockingStack <T> aMenuListStack,
                                  @Nonnull final IMenuItemRenderer <T> aRenderer,
                                  @Nonnull final Map <String, Boolean> aDisplayMenuItemIDs)
  {
    ValueEnforcer.notNull (aSWEC, "LEC");
    ValueEnforcer.notNull (aMenuListStack, "MenuListStack");
    ValueEnforcer.notNull (aRenderer, "Renderer");
    ValueEnforcer.notNull (aDisplayMenuItemIDs, "DisplayMenuItemIDs");

    this.m_aSWEC = aSWEC;
    this.m_aFactory = aFactory;
    this.m_aMenuListStack = aMenuListStack;
    this.m_aRenderer = aRenderer;
    this.m_aDisplayMenuItemIDs = aDisplayMenuItemIDs;

    this.m_aChildCountStack.push (new AtomicInteger (0));
    this.m_sSelectedItem = ApplicationRequestManager.getInstance ().getRequestMenuItemID ();
    // The selected item may be null if an invalid menu item ID was passed
  }

  @Override
  public final void onLevelDown ()
  {
    super.onLevelDown ();

    // Check if any child is visible
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = this.m_aTreeItemStack.peek ();
    for (final DefaultTreeItemWithID <String, IMenuObject> aChildItem : aParentItem.getChildren ())
      if (this.m_aDisplayMenuItemIDs.containsKey (aChildItem.getID ()))
      {
        // add sub menu structure at the right place
        final T aNewLevel = this.m_aFactory.create ();
        this.m_aRenderer.onLevelDown (aNewLevel);
        this.m_aMenuListStack.push (this.m_aMenuItemStack.peek ().addAndReturnChild (aNewLevel));
        break;
      }

    this.m_aChildCountStack.push (new AtomicInteger (0));
  }

  @Override
  public final void onLevelUp ()
  {
    final AtomicInteger aChildCount = this.m_aChildCountStack.pop ();
    if (aChildCount.intValue () > 0)
    {
      final T aLastLevel = this.m_aMenuListStack.pop ();
      this.m_aRenderer.onLevelUp (aLastLevel);
    }
    super.onLevelUp ();
  }

  @Override
  public final EHierarchyCallbackReturn onItemBeforeChildren (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aItem)
  {
    this.m_aTreeItemStack.push (aItem);

    final Boolean aExpandedState = this.m_aDisplayMenuItemIDs.get (aItem.getID ());
    if (aExpandedState != null)
    {
      final T aParent = this.m_aMenuListStack.peek ();
      final IMenuObject aMenuObj = aItem.getData ();
      boolean bHasChildren = aItem.hasChildren ();
      if (bHasChildren)
      {
        // Check if the item has children to be displayed!
        boolean bHasDisplayChildren = false;
        for (final IMenuObject aChildMenuObj : aItem.getAllChildDatas ())
          if (this.m_aDisplayMenuItemIDs.containsKey (aChildMenuObj.getID ()))
          {
            bHasDisplayChildren = true;
            break;
          }
        bHasChildren = bHasDisplayChildren;
      }
      if (aMenuObj instanceof IMenuSeparator)
      {
        // separator
        final IHCNode aHCNode = this.m_aRenderer.renderSeparator (this.m_aSWEC, (IMenuSeparator) aMenuObj);
        HCLI aLI;
        if (aHCNode instanceof HCLI)
          aLI = aParent.addAndReturnItem ((HCLI) aHCNode);
        else
          aLI = aParent.addAndReturnItem (aHCNode);
        this.m_aRenderer.onMenuSeparatorItem (this.m_aSWEC, aLI);
        this.m_aMenuItemStack.push (aLI);
      }
      else
      {
        final boolean bExpanded = aExpandedState.booleanValue ();
        final boolean bSelected = aMenuObj.getID ().equals (this.m_sSelectedItem);
        if (aMenuObj instanceof IMenuItemPage)
        {
          // page item
          final IHCNode aHCNode = this.m_aRenderer.renderMenuItemPage (this.m_aSWEC,
                                                                       (IMenuItemPage) aMenuObj,
                                                                       bHasChildren,
                                                                       bSelected,
                                                                       bExpanded);
          HCLI aLI;
          if (aHCNode instanceof HCLI)
            aLI = aParent.addAndReturnItem ((HCLI) aHCNode);
          else
            aLI = aParent.addAndReturnItem (aHCNode);
          this.m_aRenderer.onMenuItemPageItem (this.m_aSWEC, aLI, bHasChildren, bSelected, bExpanded);
          this.m_aMenuItemStack.push (aLI);
        }
        else
          if (aMenuObj instanceof IMenuItemExternal)
          {
            // external item
            final IHCNode aHCNode = this.m_aRenderer.renderMenuItemExternal (this.m_aSWEC,
                                                                             (IMenuItemExternal) aMenuObj,
                                                                             bHasChildren,
                                                                             bSelected,
                                                                             bExpanded);
            HCLI aLI;
            if (aHCNode instanceof HCLI)
              aLI = aParent.addAndReturnItem ((HCLI) aHCNode);
            else
              aLI = aParent.addAndReturnItem (aHCNode);
            this.m_aRenderer.onMenuItemExternalItem (this.m_aSWEC, aLI, bHasChildren, bSelected, bExpanded);
            this.m_aMenuItemStack.push (aLI);
          }
          else
            throw new IllegalStateException ("Unsupported menu object type: " + aMenuObj);
      }
      this.m_aChildCountStack.peek ().incrementAndGet ();
      return EHierarchyCallbackReturn.CONTINUE;
    }

    // Item should not be displayed
    // push fake item so the pop does not remove anything important!
    this.m_aMenuItemStack.push (new HCLI ());
    return EHierarchyCallbackReturn.USE_NEXT_SIBLING;
  }

  @Override
  @Nonnull
  public final EHierarchyCallbackReturn onItemAfterChildren (final DefaultTreeItemWithID <String, IMenuObject> aItem)
  {
    this.m_aTreeItemStack.pop ();
    this.m_aMenuItemStack.pop ();
    return EHierarchyCallbackReturn.CONTINUE;
  }

  /**
   * Render the whole menu
   * 
   * @param <T>
   *        object type handled by the factory and renderer
   * @param aLEC
   *        The current layout execution context. Required for cookie-less
   *        handling. May not be <code>null</code>.
   * @param aFactory
   *        The factory to be used to create nodes of type T. May not be
   *        <code>null</code>.
   * @param aRenderer
   *        The renderer to use
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <T extends AbstractHCList <T>> T createRenderedMenu (@Nonnull final ILayoutExecutionContext aLEC,
                                                                     @Nonnull final IFactory <T> aFactory,
                                                                     @Nonnull final IMenuItemRenderer <T> aRenderer)
  {
    final IMenuTree aMenuTree = aLEC.getMenuTree ();
    return createRenderedMenu (aLEC,
                               aFactory,
                               aMenuTree.getRootItem (),
                               aRenderer,
                               MenuItemDeterminatorCallback.getAllDisplayMenuItemIDs (aMenuTree,
                                                                                      aLEC.getSelectedMenuItemID ()));
  }

  /**
   * Render a part of the menu
   * 
   * @param <T>
   *        object type handled by the factory and renderer
   * @param aLEC
   *        The current layout execution context. Required for cookie-less
   *        handling. May not be <code>null</code>.
   * @param aFactory
   *        The factory to be used to create nodes of type T. May not be
   *        <code>null</code>.
   * @param aStartTreeItem
   *        The start tree to iterate
   * @param aRenderer
   *        The renderer to use
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <T extends AbstractHCList <?>> T createRenderedMenu (@Nonnull final ILayoutExecutionContext aLEC,
                                                                     @Nonnull final IFactory <T> aFactory,
                                                                     @Nonnull final DefaultTreeItemWithID <String, IMenuObject> aStartTreeItem,
                                                                     @Nonnull final IMenuItemRenderer <T> aRenderer)
  {
    final IMenuTree aMenuTree = aLEC.getMenuTree ();
    return createRenderedMenu (aLEC,
                               aFactory,
                               aStartTreeItem,
                               aRenderer,
                               MenuItemDeterminatorCallback.getAllDisplayMenuItemIDs (aMenuTree,
                                                                                      aLEC.getSelectedMenuItemID ()));
  }

  /**
   * Render the whole menu
   * 
   * @param <T>
   *        object type handled by the factory and renderer
   * @param aLEC
   *        The current layout execution context. Required for cookie-less
   *        handling. May not be <code>null</code>.
   * @param aFactory
   *        The factory to be used to create nodes of type T. May not be
   *        <code>null</code>.
   * @param aRenderer
   *        The renderer to use
   * @param aDisplayMenuItemIDs
   *        The menu items to display as a map from menu item ID to expanded
   *        state
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <T extends AbstractHCList <?>> T createRenderedMenu (@Nonnull final ILayoutExecutionContext aLEC,
                                                                     @Nonnull final IFactory <T> aFactory,
                                                                     @Nonnull final IMenuItemRenderer <T> aRenderer,
                                                                     @Nonnull final Map <String, Boolean> aDisplayMenuItemIDs)
  {
    return createRenderedMenu (aLEC, aFactory, aLEC.getMenuTree ().getRootItem (), aRenderer, aDisplayMenuItemIDs);
  }

  /**
   * Render a part of the menu
   * 
   * @param <T>
   *        object type handled by the factory and renderer
   * @param aSWEC
   *        The current layout execution context. Required for cookie-less
   *        handling. May not be <code>null</code>.
   * @param aFactory
   *        The factory to be used to create nodes of type T. May not be
   *        <code>null</code>.
   * @param aStartTreeItem
   *        The start tree to iterate. May not be <code>null</code>.
   * @param aRenderer
   *        The renderer to use. May not be <code>null</code>.
   * @param aDisplayMenuItemIDs
   *        The menu items to display as a map from menu item ID to expanded
   *        state. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <T extends AbstractHCList <?>> T createRenderedMenu (@Nonnull final ISimpleWebExecutionContext aSWEC,
                                                                     @Nonnull final IFactory <T> aFactory,
                                                                     @Nonnull final DefaultTreeItemWithID <String, IMenuObject> aStartTreeItem,
                                                                     @Nonnull final IMenuItemRenderer <T> aRenderer,
                                                                     @Nonnull final Map <String, Boolean> aDisplayMenuItemIDs)
  {
    ValueEnforcer.notNull (aFactory, "Factory");

    final NonBlockingStack <T> aNodeStack = new NonBlockingStack <T> ();
    aNodeStack.push (aFactory.create ());
    TreeWalkerDynamic.walkSubTree (aStartTreeItem,
                                   new MenuRendererCallback <T> (aSWEC,
                                                                 aFactory,
                                                                 aNodeStack,
                                                                 aRenderer,
                                                                 aDisplayMenuItemIDs));
    if (aNodeStack.size () != 1)
      throw new IllegalStateException ("Stack is inconsistent: " + aNodeStack);

    // Return the remaining UL
    return aNodeStack.pop ();
  }
}
