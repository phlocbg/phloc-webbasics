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

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.impl.HCEntityNode;
import com.phloc.webbasics.app.LinkUtils;

/**
 * Default implementation of {@link IMenuItemRenderer}
 * 
 * @author philip
 */
public class DefaultMenuItemRenderer implements IMenuItemRenderer
{
  public static final ICSSClassProvider CSS_CLASS_SELECTED_MENU_ITEM = DefaultCSSClassProvider.create ("selected_menu_item");
  public static final String CSS_ID_PREFIX_MENU_ITEM = "menu_item_";

  protected Locale m_aContentLocale;

  public DefaultMenuItemRenderer (@Nonnull final Locale aContentLocale)
  {
    if (aContentLocale == null)
      throw new NullPointerException ("contentLocale");
    m_aContentLocale = aContentLocale;
  }

  @Nonnull
  public IHCNode renderSeparator (@Nonnull final IMenuSeparator aSeparator)
  {
    return HCEntityNode.newNBSP ();
  }

  @Nonnull
  public IHCNode renderMenuItem (@Nonnull final IMenuItem aMenuItem,
                                 final boolean bHasChildren,
                                 final boolean bIsSelected,
                                 final boolean bIsExpanded)
  {
    if (aMenuItem instanceof IMenuItemPage)
    {
      final String sMenuItemID = aMenuItem.getID ();
      final HCA aLink = new HCA (LinkUtils.getLinkToMenuItem (sMenuItemID));
      aLink.addChild (aMenuItem.getDisplayText (m_aContentLocale) + (bHasChildren && !bIsExpanded ? " [+]" : ""));
      aLink.setID (CSS_ID_PREFIX_MENU_ITEM + sMenuItemID);
      if (bIsSelected)
        aLink.addClass (CSS_CLASS_SELECTED_MENU_ITEM);
      return aLink;
    }

    if (aMenuItem instanceof IMenuItemExternal)
    {
      final String sMenuItemID = aMenuItem.getID ();
      final HCA aLink = new HCA (((IMenuItemExternal) aMenuItem).getURL ());
      aLink.addChild (aMenuItem.getDisplayText (m_aContentLocale) + (bHasChildren && !bIsExpanded ? " [+]" : ""));
      aLink.setID (CSS_ID_PREFIX_MENU_ITEM + sMenuItemID);
      if (bIsSelected)
        aLink.addClass (CSS_CLASS_SELECTED_MENU_ITEM);
      return aLink;
    }

    throw new IllegalStateException ();
  }
}
