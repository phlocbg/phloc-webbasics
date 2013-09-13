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
package com.phloc.bootstrap2.ext;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.appbasics.app.menu.MenuItemDeterminatorCallback;
import com.phloc.bootstrap2.BootstrapWell;
import com.phloc.bootstrap2.CBootstrapCSS;
import com.phloc.bootstrap2.EBootstrapIcon;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.factory.FactoryNewInstance;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.menu.ui.AbstractMenuItemRenderer;
import com.phloc.webbasics.app.menu.ui.IMenuItemRenderer;
import com.phloc.webbasics.app.menu.ui.MenuRendererCallback;

/**
 * Default implementation of {@link IMenuItemRenderer}
 * 
 * @author Philip Helger
 */
public class BootstrapMenuItemRendererWell extends AbstractMenuItemRenderer <HCUL>
{
  public BootstrapMenuItemRendererWell (@Nonnull final Locale aContentLocale)
  {
    super (aContentLocale);
  }

  @Nonnull
  public IHCNode renderSeparator (@Nonnull final IMenuSeparator aSeparator)
  {
    return new HCLI ().addClass (CBootstrapCSS.DIVIDER);
  }

  /**
   * Get the label to display.
   * 
   * @param aMenuItem
   *        Menu item. Never <code>null</code>.
   * @param bHasChildren
   *        <code>true</code> if the item has children
   * @param bIsSelected
   *        <code>true</code> if it is selected
   * @param bIsExpanded
   *        <code>true</code> if it is expanded.
   * @return The label text. Should not be <code>null</code>.
   * @see #getContentLocale()
   */
  @Nonnull
  @OverrideOnDemand
  protected String getMenuItemPageLabel (@Nonnull final IMenuItemPage aMenuItem,
                                         final boolean bHasChildren,
                                         final boolean bIsSelected,
                                         final boolean bIsExpanded)
  {
    return aMenuItem.getDisplayText (getContentLocale ());
  }

  @Nonnull
  public IHCNode renderMenuItemPage (@Nonnull final IMenuItemPage aMenuItem,
                                     final boolean bHasChildren,
                                     final boolean bIsSelected,
                                     final boolean bIsExpanded)
  {
    final HCA aLink = new HCA (LinkUtils.getLinkToMenuItem (aMenuItem.getID ()));
    aLink.addChild (getMenuItemPageLabel (aMenuItem, bHasChildren, bIsSelected, bIsExpanded));
    if (bHasChildren && !bIsExpanded)
      aLink.addChildren (new HCTextNode (" "), EBootstrapIcon.CHEVRON_RIGHT.getAsNode ());
    return aLink;
  }

  /**
   * Get the label to display.
   * 
   * @param aMenuItem
   *        Menu item. Never <code>null</code>.
   * @param bHasChildren
   *        <code>true</code> if the item has children
   * @param bIsSelected
   *        <code>true</code> if it is selected
   * @param bIsExpanded
   *        <code>true</code> if it is expanded.
   * @return The label text. Should not be <code>null</code>.
   * @see #getContentLocale()
   */
  @Nonnull
  @OverrideOnDemand
  protected String getMenuItemExternalLabel (@Nonnull final IMenuItemExternal aMenuItem,
                                             final boolean bHasChildren,
                                             final boolean bIsSelected,
                                             final boolean bIsExpanded)
  {
    return aMenuItem.getDisplayText (getContentLocale ());
  }

  @Nonnull
  public IHCNode renderMenuItemExternal (@Nonnull final IMenuItemExternal aMenuItem,
                                         final boolean bHasChildren,
                                         final boolean bIsSelected,
                                         final boolean bIsExpanded)
  {
    final HCA aLink = new HCA (aMenuItem.getURL ());
    aLink.setTarget (HCA_Target.BLANK);
    aLink.addChild (getMenuItemExternalLabel (aMenuItem, bHasChildren, bIsSelected, bIsExpanded));
    if (bHasChildren && !bIsExpanded)
      aLink.addChildren (new HCTextNode (" "), EBootstrapIcon.CHEVRON_RIGHT.getAsNode ());
    return aLink;
  }

  @Override
  public void onLevelDown (@Nonnull final HCUL aNewLevel)
  {
    aNewLevel.addClasses (CBootstrapCSS.NAV, CBootstrapCSS.NAV_LIST);
  }

  @Override
  public void onMenuItemPageItem (@Nonnull final HCLI aLI,
                                  final boolean bHasChildren,
                                  final boolean bSelected,
                                  final boolean bExpanded)
  {
    if (bSelected)
      aLI.addClass (CBootstrapCSS.ACTIVE);
  }

  @Override
  public void onMenuItemExternalItem (@Nonnull final HCLI aLI,
                                      final boolean bHasChildren,
                                      final boolean bSelected,
                                      final boolean bExpanded)
  {
    if (bSelected)
      aLI.addClass (CBootstrapCSS.ACTIVE);
  }

  @Nonnull
  public static BootstrapWell createSideBarMenu (@Nonnull final IMenuTree aMenuTree,
                                                 @Nonnull final Locale aDisplayLocale)
  {
    return createSideBarMenu (aMenuTree, new MenuItemDeterminatorCallback (aMenuTree), aDisplayLocale);
  }

  @Nonnull
  public static BootstrapWell createSideBarMenu (@Nonnull final IMenuTree aMenuTree,
                                                 @Nonnull final MenuItemDeterminatorCallback aDeterminator,
                                                 @Nonnull final Locale aDisplayLocale)
  {
    return createSideBarMenu (aMenuTree, aDeterminator, new BootstrapMenuItemRendererWell (aDisplayLocale));
  }

  @Nonnull
  public static BootstrapWell createSideBarMenu (@Nonnull final IMenuTree aMenuTree,
                                                 @Nonnull final MenuItemDeterminatorCallback aDeterminator,
                                                 @Nonnull final BootstrapMenuItemRendererWell aRenderer)
  {
    final Map <String, Boolean> aAllDisplayMenuItemIDs = MenuItemDeterminatorCallback.getAllDisplayMenuItemIDs (aDeterminator);
    final HCUL aUL = MenuRendererCallback.createRenderedMenu (FactoryNewInstance.create (HCUL.class),
                                                              aMenuTree.getRootItem (),
                                                              aRenderer,
                                                              aAllDisplayMenuItemIDs)
                                         .addClasses (CBootstrapCSS.NAV, CBootstrapCSS.NAV_LIST);
    final BootstrapWell ret = new BootstrapWell ();
    ret.addChild (aUL);
    return ret;
  }
}
