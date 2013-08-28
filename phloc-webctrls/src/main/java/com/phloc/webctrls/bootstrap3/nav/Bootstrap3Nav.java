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
package com.phloc.webctrls.bootstrap3.nav;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.bootstrap3.dropdown.Bootstrap3DropdownMenu;
import com.phloc.webctrls.custom.IIcon;

/**
 * Navigation items
 * 
 * @author Philip Helger
 */
public class Bootstrap3Nav extends HCUL
{
  /** By default an item is not disabled */
  public static final boolean DEFAULT_DISABLED = false;

  private final EBootstrap3NavType m_eNavType;

  public Bootstrap3Nav (@Nonnull final EBootstrap3NavType eNavType)
  {
    if (eNavType == null)
      throw new NullPointerException ("NavType");

    addClass (CBootstrap3CSS.NAV);
    addClasses (eNavType.getAllCSSClasses ());
    m_eNavType = eNavType;
  }

  @Nonnull
  public EBootstrap3NavType getNavType ()
  {
    return m_eNavType;
  }

  @Nonnull
  public Bootstrap3Nav addItem (@Nullable final String sText, @Nonnull final ISimpleURL aTarget)
  {
    return addItem (sText, aTarget, DEFAULT_DISABLED, (IIcon) null);
  }

  @Nonnull
  public Bootstrap3Nav addItem (@Nullable final String sText,
                                @Nonnull final ISimpleURL aTarget,
                                @Nullable final IIcon aIcon)
  {
    return addItem (sText, aTarget, DEFAULT_DISABLED, aIcon);
  }

  @Nonnull
  public Bootstrap3Nav addItem (@Nullable final String sText, @Nonnull final ISimpleURL aTarget, final boolean bDisabled)
  {
    // no icon
    return addItem (sText, aTarget, bDisabled, null);
  }

  @Nonnull
  public Bootstrap3Nav addItem (@Nullable final String sText,
                                @Nonnull final ISimpleURL aTarget,
                                final boolean bDisabled,
                                @Nullable final IIcon aIcon)
  {
    return addItem (new HCA (aTarget).addChild (sText), bDisabled, aIcon);
  }

  @Nonnull
  public Bootstrap3Nav addItem (@Nullable final IHCElementWithChildren <?> aContent)
  {
    return addItem (aContent, DEFAULT_DISABLED);
  }

  @Nonnull
  public Bootstrap3Nav addItem (@Nullable final IHCElementWithChildren <?> aContent, final boolean bDisabled)
  {
    return addItem (aContent, bDisabled, null);
  }

  @Nonnull
  public Bootstrap3Nav addItem (@Nullable final IHCElementWithChildren <?> aContent,
                                final boolean bDisabled,
                                @Nullable final IIcon aIcon)
  {
    final HCLI aItem = addAndReturnItem (aContent);
    if (bDisabled)
      aItem.addClass (CBootstrap3CSS.DISABLED);
    if (aIcon != null)
    {
      // Icon is the first child of the content
      aContent.addChild (0, aIcon.getAsNode ());
    }
    return this;
  }

  @Nonnull
  public Bootstrap3DropdownMenu addDropdownMenu (@Nullable final String sText)
  {
    return addDropdownMenu (new HCTextNode (sText));
  }

  @Nonnull
  public Bootstrap3DropdownMenu addDropdownMenu (@Nullable final IHCNode aText)
  {
    final HCLI aLI = addItem ().addClass (CBootstrap3CSS.DROPDOWN);
    aLI.addChild (Bootstrap3DropdownMenu.makeDropdownToggle (new HCA (new SimpleURL ()).addChild (aText)));
    final Bootstrap3DropdownMenu aMenu = aLI.addAndReturnChild (new Bootstrap3DropdownMenu ());
    return aMenu;
  }
}
