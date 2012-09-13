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
package com.phloc.webctrls.bootstrap.ext;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.menu.ui.IMenuItemRenderer;
import com.phloc.webbasics.app.menu.ui.MenuRendererCallback;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;
import com.phloc.webctrls.bootstrap.EBootstrapIcon;

/**
 * Default implementation of {@link IMenuItemRenderer}
 * 
 * @author philip
 */
public class BootstrapMenuItemRenderer implements IMenuItemRenderer
{
  private final Locale m_aContentLocale;

  public BootstrapMenuItemRenderer (@Nonnull final Locale aContentLocale)
  {
    if (aContentLocale == null)
      throw new NullPointerException ("contentLocale");
    m_aContentLocale = aContentLocale;
  }

  @Nonnull
  public Locale getContentLocale ()
  {
    return m_aContentLocale;
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
    aLink.addChild (aMenuItem.getDisplayText (m_aContentLocale));
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
    aLink.addChild (aMenuItem.getDisplayText (m_aContentLocale));
    if (bHasChildren && !bIsExpanded)
      aLink.addChildren (new HCTextNode (" "), EBootstrapIcon.CHEVRON_RIGHT.getAsNode ());
    return aLink;
  }

  public void onLevelDown (@Nonnull final HCUL aNewLevel)
  {
    aNewLevel.addClasses (CBootstrapCSS.NAV, CBootstrapCSS.NAV_LIST);
  }

  public void onMenuSeparatorItem (@Nonnull final HCLI aLI)
  {
    // empty
  }

  public void onMenuItemPageItem (@Nonnull final HCLI aLI, final boolean bSelected)
  {
    if (bSelected)
      aLI.addClass (CBootstrapCSS.ACTIVE);
  }

  public void onMenuItemExternalItem (@Nonnull final HCLI aLI, final boolean bSelected)
  {
    if (bSelected)
      aLI.addClass (CBootstrapCSS.ACTIVE);
  }

  @Nonnull
  public static HCDiv createSideBarMenu (@Nonnull final Locale aDisplayLocale)
  {
    final HCDiv ret = new HCDiv ().addClasses (CBootstrapCSS.WELL, CBootstrapCSS.SIDEBAR_NAV);
    ret.addChild (MenuRendererCallback.createRenderedMenu (new BootstrapMenuItemRenderer (aDisplayLocale))
                                      .addClasses (CBootstrapCSS.NAV, CBootstrapCSS.NAV_LIST));
    return ret;
  }
}
