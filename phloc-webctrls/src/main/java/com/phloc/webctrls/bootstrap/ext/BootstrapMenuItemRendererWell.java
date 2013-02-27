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
package com.phloc.webctrls.bootstrap.ext;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.appbasics.app.menu.MenuItemDeterminatorCallback;
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
import com.phloc.webctrls.bootstrap.BootstrapWell;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;
import com.phloc.webctrls.bootstrap.EBootstrapIcon;

/**
 * Default implementation of {@link IMenuItemRenderer}
 * 
 * @author philip
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

  @Nonnull
  public IHCNode renderMenuItemPage (@Nonnull final IMenuItemPage aMenuItem,
                                     final boolean bHasChildren,
                                     final boolean bIsSelected,
                                     final boolean bIsExpanded)
  {
    final String sMenuItemID = aMenuItem.getID ();
    final HCA aLink = new HCA (LinkUtils.getLinkToMenuItem (sMenuItemID));
    aLink.addChild (aMenuItem.getDisplayText (getContentLocale ()));
    if (bHasChildren && !bIsExpanded)
      aLink.addChildren (new HCTextNode (" "), EBootstrapIcon.CHEVRON_RIGHT.getAsNode ());
    return aLink;
  }

  @Nonnull
  public IHCNode renderMenuItemExternal (@Nonnull final IMenuItemExternal aMenuItem,
                                         final boolean bHasChildren,
                                         final boolean bIsSelected,
                                         final boolean bIsExpanded)
  {
    final HCA aLink = new HCA (aMenuItem.getURL ());
    aLink.setTarget (HCA_Target.BLANK);
    aLink.addChild (aMenuItem.getDisplayText (getContentLocale ()));
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
                                  boolean bHasChildren,
                                  final boolean bSelected,
                                  boolean bExpanded)
  {
    if (bSelected)
      aLI.addClass (CBootstrapCSS.ACTIVE);
  }

  @Override
  public void onMenuItemExternalItem (@Nonnull final HCLI aLI,
                                      boolean bHasChildren,
                                      final boolean bSelected,
                                      boolean bExpanded)
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
    final BootstrapWell ret = new BootstrapWell ();
    final Map <String, Boolean> aAllDisplayMenuItemIDs = MenuItemDeterminatorCallback.getAllDisplayMenuItemIDs (aDeterminator);
    ret.addChild (MenuRendererCallback.createRenderedMenu (FactoryNewInstance.create (HCUL.class),
                                                           aMenuTree.getRootItem (),
                                                           new BootstrapMenuItemRendererWell (aDisplayLocale),
                                                           aAllDisplayMenuItemIDs).addClasses (CBootstrapCSS.NAV,
                                                                                               CBootstrapCSS.NAV_LIST));
    return ret;
  }
}
