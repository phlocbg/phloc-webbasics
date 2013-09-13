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
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.menu.ui.AbstractMenuItemRenderer;
import com.phloc.webbasics.app.menu.ui.IMenuItemRenderer;
import com.phloc.webbasics.app.menu.ui.MenuRendererCallback;
import com.phloc.webctrls.bootstrap.BootstrapCaret;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;

/**
 * Implementation of {@link IMenuItemRenderer} creating a Navbar compliant menu
 * 
 * @author Philip Helger
 */
public class BootstrapMenuItemRendererNavbar extends AbstractMenuItemRenderer <HCUL>
{
  private int m_nLevel = 0;

  public BootstrapMenuItemRendererNavbar (@Nonnull final Locale aContentLocale)
  {
    super (aContentLocale);
  }

  @Nonnull
  public HCLI renderSeparator (@Nonnull final IMenuSeparator aSeparator)
  {
    return new HCLI ().addClass (CBootstrapCSS.DIVIDER);
  }

  @Nonnull
  public HCA renderMenuItemPage (@Nonnull final IMenuItemPage aMenuItem,
                                 final boolean bHasChildren,
                                 final boolean bIsSelected,
                                 final boolean bIsExpanded)
  {
    final String sMenuItemID = aMenuItem.getID ();
    final HCA aLink = new HCA (LinkUtils.getLinkToMenuItem (sMenuItemID));
    aLink.addChild (aMenuItem.getDisplayText (getContentLocale ()));
    return aLink;
  }

  @Nonnull
  public HCA renderMenuItemExternal (@Nonnull final IMenuItemExternal aMenuItem,
                                     final boolean bHasChildren,
                                     final boolean bIsSelected,
                                     final boolean bIsExpanded)
  {
    final HCA aLink = new HCA (aMenuItem.getURL ());
    aLink.setTarget (HCA_Target.BLANK);
    aLink.addChild (aMenuItem.getDisplayText (getContentLocale ()));
    return aLink;
  }

  @Override
  public void onLevelDown (@Nonnull final HCUL aNewLevel)
  {
    aNewLevel.addClass (CBootstrapCSS.DROPDOWN_MENU).setRole (EHTMLRole.MENU);
    ++m_nLevel;
  }

  @Override
  public void onLevelUp (@Nonnull final HCUL aLastLevel)
  {
    --m_nLevel;
  }

  @Override
  public void onMenuItemPageItem (@Nonnull final HCLI aLI,
                                  final boolean bHasChildren,
                                  final boolean bSelected,
                                  final boolean bExpanded)
  {
    if (m_nLevel > 0 && bHasChildren)
      aLI.addClass (CBootstrapCSS.DROPDOWN_SUBMENU);
  }

  @Override
  public void onMenuItemExternalItem (@Nonnull final HCLI aLI,
                                      final boolean bHasChildren,
                                      final boolean bSelected,
                                      final boolean bExpanded)
  {
    if (m_nLevel > 0 && bHasChildren)
      aLI.addClass (CBootstrapCSS.DROPDOWN_SUBMENU);
  }

  @Nonnull
  public static HCUL createNavbarMenu (@Nonnull final IMenuTree aMenuTree, @Nonnull final Locale aDisplayLocale)
  {
    final Map <String, Boolean> aAllDisplayMenuItemIDs = MenuItemDeterminatorCallback.getAllMenuItemIDs (aMenuTree);
    return createNavbarMenu (aMenuTree, aDisplayLocale, aAllDisplayMenuItemIDs);
  }

  @Nonnull
  public static HCUL createNavbarMenu (@Nonnull final IMenuTree aMenuTree,
                                       @Nonnull final Locale aDisplayLocale,
                                       @Nonnull final Map <String, Boolean> aAllDisplayMenuItemIDs)
  {
    final HCUL aUL = MenuRendererCallback.createRenderedMenu (FactoryNewInstance.create (HCUL.class),
                                                              aMenuTree.getRootItem (),
                                                              new BootstrapMenuItemRendererNavbar (aDisplayLocale),
                                                              aAllDisplayMenuItemIDs);
    aUL.addClass (CBootstrapCSS.NAV);
    aUL.setRole (EHTMLRole.NAVIGATION);

    // For all root items
    for (final HCLI aLI : aUL.getChildren ())
    {
      // Childcount >= 2 means "has sub items"
      if (aLI.getChildCount () >= 2)
      {
        aLI.addClass (CBootstrapCSS.DROPDOWN);
        ((HCA) aLI.getChildAtIndex (0)).addClass (CBootstrapCSS.DROPDOWN_TOGGLE)
                                       .setCustomAttr ("data-toggle", "dropdown")
                                       .setRole (EHTMLRole.BUTTON)
                                       .addChild (new BootstrapCaret ());
      }
    }
    return aUL;
  }
}
