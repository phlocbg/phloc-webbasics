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

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerDynamicCallback;
import com.phloc.commons.hierarchy.EHierarchyCallbackReturn;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.css.DefaultCSSClassProvider;
import com.phloc.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCEntityNode;
import com.phloc.webbasics.app.ApplicationRequestManager;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.menu.IMenuItem;
import com.phloc.webbasics.app.menu.IMenuObject;
import com.phloc.webbasics.app.menu.IMenuSeparator;

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
  private final NonBlockingStack <HCLI> m_aMenuItemStack = new NonBlockingStack <HCLI> ();
  private final NonBlockingStack <AtomicInteger> m_aChildCountStack = new NonBlockingStack <AtomicInteger> ();
  private final NonBlockingStack <DefaultTreeItemWithID <String, IMenuObject>> m_aTreeItemStack;
  private final Locale m_aContentLocale;
  private final Set <String> m_aDisplayMenuItemIDs;
  private final String m_sSelectedItem;

  public MenuRendererCallback (@Nonnull final NonBlockingStack <HCUL> aNodeStack, @Nonnull final Locale aContentLocale)
  {
    this (aNodeStack, aContentLocale, MenuItemDeterminatorCallback.getAllDisplayMenuItemIDs ());
  }

  public MenuRendererCallback (@Nonnull final NonBlockingStack <HCUL> aNodeStack,
                               @Nonnull final Locale aContentLocale,
                               @Nonnull final Set <String> aDisplayMenuItemIDs)
  {
    if (aNodeStack == null)
      throw new NullPointerException ("nodeStack");
    if (aContentLocale == null)
      throw new NullPointerException ("contentLocale");
    if (aDisplayMenuItemIDs == null)
      throw new NullPointerException ("displayMenuItemIDs");

    m_aMenuListStack = aNodeStack;
    m_aTreeItemStack = new NonBlockingStack <DefaultTreeItemWithID <String, IMenuObject>> ();
    m_aChildCountStack.push (new AtomicInteger ());
    m_aContentLocale = aContentLocale;
    m_aDisplayMenuItemIDs = aDisplayMenuItemIDs;
    m_sSelectedItem = ApplicationRequestManager.getRequestMenuItemID ();
    // The selected item may be null if an invalid menu item ID was passed
  }

  @Override
  public final void onLevelDown ()
  {
    super.onLevelDown ();
    final DefaultTreeItemWithID <String, IMenuObject> aParentItem = m_aTreeItemStack.peek ();
    boolean bHasVisibleChildren = false;
    for (final DefaultTreeItemWithID <String, IMenuObject> aChildItem : aParentItem.getChildren ())
      if (m_aDisplayMenuItemIDs.contains (aChildItem.getID ()))
      {
        bHasVisibleChildren = true;
        break;
      }

    if (bHasVisibleChildren)
    {
      // add sub menu structure at the right place
      m_aMenuListStack.push (m_aMenuItemStack.peek ().addAndReturnChild (new HCUL ()));
    }
    m_aChildCountStack.push (new AtomicInteger ());
  }

  @Override
  public final void onLevelUp ()
  {
    final AtomicInteger aChildCount = m_aChildCountStack.pop ();
    if (aChildCount.intValue () > 0)
      m_aMenuListStack.pop ();
    super.onLevelUp ();
  }

  @Nonnull
  @OverrideOnDemand
  protected IHCNode createSeparator ()
  {
    return HCEntityNode.newNBSP ();
  }

  @Nonnull
  protected IHCNode createMenuItem (@Nonnull final IMenuItem aMenuItem,
                                    @Nonnull final Locale aContentLocale,
                                    final boolean bHasChildren,
                                    final boolean bSelected)
  {
    final String sMenuItemID = aMenuItem.getID ();
    final HCA aLink = new HCA (LinkUtils.getLinkToMenuItem (sMenuItemID));
    aLink.addChild (aMenuItem.getDisplayText (aContentLocale) + (bHasChildren ? " [+]" : ""));
    aLink.setID (CSS_ID_PREFIX_MENU_ITEM + sMenuItemID);
    if (bSelected)
      aLink.addClass (CSS_CLASS_SELECTED_MENU_ITEM);
    return aLink;
  }

  @Override
  public final EHierarchyCallbackReturn onItemBeforeChildren (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aItem)
  {
    m_aTreeItemStack.push (aItem);

    if (m_aDisplayMenuItemIDs.contains (aItem.getID ()))
    {
      final HCUL aParent = m_aMenuListStack.peek ();
      // Only root level and the selected subtree is rendered
      final IMenuObject aMenuObj = aItem.getData ();
      if (aMenuObj instanceof IMenuSeparator)
      {
        // separator
        final IHCNode aHC = createSeparator ();
        m_aMenuItemStack.push (aParent.addAndReturnItem (aHC).addClass (CSS_CLASS_MENU_SEPARATOR));
      }
      else
      {
        // item
        final IHCNode aHC = createMenuItem ((IMenuItem) aMenuObj,
                                            m_aContentLocale,
                                            aItem.hasChildren (),
                                            aMenuObj.getID ().equals (m_sSelectedItem));
        m_aMenuItemStack.push (aParent.addAndReturnItem (aHC).addClass (CSS_CLASS_MENU_ITEM));
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
  @OverrideOnDemand
  @Nonnull
  public final EHierarchyCallbackReturn onItemAfterChildren (final DefaultTreeItemWithID <String, IMenuObject> aItem)
  {
    m_aTreeItemStack.pop ();
    m_aMenuItemStack.pop ();
    return EHierarchyCallbackReturn.CONTINUE;
  }
}
