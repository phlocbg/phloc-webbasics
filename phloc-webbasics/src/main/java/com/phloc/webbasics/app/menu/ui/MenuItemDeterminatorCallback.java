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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.MenuTree;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerDynamicCallback;
import com.phloc.commons.hierarchy.EHierarchyCallbackReturn;
import com.phloc.commons.tree.utils.walk.TreeWalkerDynamic;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;

/**
 * Determine all menu items to show, depending on the currently selected menu
 * item.
 * 
 * @author philip
 */
public class MenuItemDeterminatorCallback extends
                                         DefaultHierarchyWalkerDynamicCallback <DefaultTreeItemWithID <String, IMenuObject>>
{
  private final Map <String, Boolean> m_aItems = new HashMap <String, Boolean> ();
  private final String m_sSelectedItem;
  private final DefaultTreeItemWithID <String, IMenuObject> m_aSelectedItem;

  protected MenuItemDeterminatorCallback ()
  {
    m_sSelectedItem = ApplicationRequestManager.getRequestMenuItemID ();
    m_aSelectedItem = MenuTree.getInstance ().getItemWithID (m_sSelectedItem);
    // The selected item may be null if an invalid menu item ID was passed
  }

  @OverrideOnDemand
  protected void rememberMenuItemForDisplay (@Nonnull @Nonempty final String sMenuItemID, final boolean bExpanded)
  {
    final Boolean aExpanded = m_aItems.get (sMenuItemID);
    if (aExpanded == null || !aExpanded.booleanValue ())
      m_aItems.put (sMenuItemID, Boolean.valueOf (bExpanded));
  }

  @Override
  public final EHierarchyCallbackReturn onItemBeforeChildren (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aItem)
  {
    boolean bShow;
    boolean bAddAllChildrenOnThisLevel = false;
    boolean bExpanded = false;
    final boolean bIsTopLevel = getLevel () == 0;
    if (m_aSelectedItem == null)
    {
      // Show only top level entries
      bShow = bIsTopLevel;
    }
    else
    {
      // 1. Top level entries are always shown
      // 2. Show this item, if it is a parent of the selected item (sub menu)
      // 3. Show this item, if it is a direct child of the selected item
      // 4. Show this item, if if is a direct child of the selected item's
      // parent
      if (bIsTopLevel)
      {
        // 1.
        bShow = true;
        bExpanded = m_aSelectedItem.isSameOrChildOf (aItem);
      }
      else
      {
        // 2.
        if (m_aSelectedItem.isSameOrChildOf (aItem))
        {
          bShow = true;
          bExpanded = true;
        }
        else
        {
          final DefaultTreeItemWithID <String, IMenuObject> aItemParent = aItem.getParent ();
          // 3.
          if (aItemParent.equals (m_aSelectedItem))
          {
            bShow = true;
          }
          else
            // 4.
            if (aItemParent.getChildItemOfDataID (m_sSelectedItem) != null)
              bShow = true;
            else
              bShow = false;
        }
        bAddAllChildrenOnThisLevel = bShow;
      }
    }

    // Check display filter
    if (bShow || bAddAllChildrenOnThisLevel)
    {
      if (!aItem.getData ().matchesDisplayFilter ())
      {
        bShow = false;
        bAddAllChildrenOnThisLevel = false;
        bExpanded = false;
      }
    }

    if (bShow)
      rememberMenuItemForDisplay (aItem.getID (), bExpanded);
    if (bAddAllChildrenOnThisLevel)
      for (final DefaultTreeItemWithID <String, IMenuObject> aSibling : aItem.getParent ().getChildren ())
        if (aSibling.getData ().matchesDisplayFilter ())
          rememberMenuItemForDisplay (aSibling.getID (), false);
    return EHierarchyCallbackReturn.CONTINUE;
  }

  @Nonnull
  @ReturnsImmutableObject
  public Map <String, Boolean> getAllItemIDs ()
  {
    return ContainerHelper.makeUnmodifiable (m_aItems);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Map <String, Boolean> getAllDisplayMenuItemIDs ()
  {
    return getAllDisplayMenuItemIDs (new MenuItemDeterminatorCallback ());
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Map <String, Boolean> getAllDisplayMenuItemIDs (@Nonnull final MenuItemDeterminatorCallback aDeterminator)
  {
    if (aDeterminator == null)
      throw new NullPointerException ("determinator");

    TreeWalkerDynamic.walkTree (MenuTree.getInstance (), aDeterminator);
    return aDeterminator.getAllItemIDs ();
  }
}
