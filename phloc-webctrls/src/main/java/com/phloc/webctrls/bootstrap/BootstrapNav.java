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
package com.phloc.webctrls.bootstrap;

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
import com.phloc.webctrls.custom.IIcon;

/**
 * Navigation items
 * 
 * @author philip
 */
public class BootstrapNav extends HCUL
{
  public static final boolean DEFAULT_ACTIVE = false;

  public BootstrapNav ()
  {
    super ();
    addClass (CBootstrapCSS.NAV);
  }

  @Nonnull
  public BootstrapNav setNavList (final boolean bNavList)
  {
    if (bNavList)
      addClass (CBootstrapCSS.NAV_LIST);
    else
      removeClass (CBootstrapCSS.NAV_LIST);
    return this;
  }

  @Nonnull
  public BootstrapNav addHeader (@Nullable final String sText)
  {
    return addHeader (sText, DEFAULT_ACTIVE);
  }

  @Nonnull
  public BootstrapNav addHeader (@Nullable final String sText, final boolean bActive)
  {
    return addHeader (new HCTextNode (sText), bActive);
  }

  @Nonnull
  public BootstrapNav addHeader (@Nullable final String sText, @Nonnull final ISimpleURL aTarget, final boolean bActive)
  {
    return addHeader (new HCA (aTarget).addChild (sText), bActive);
  }

  @Nonnull
  public BootstrapNav addHeader (@Nullable final IHCNode aContent, final boolean bActive)
  {
    final HCLI aItem = addAndReturnItem (aContent).addClass (CBootstrapCSS.NAV_HEADER);
    if (bActive)
      aItem.addClass (CBootstrapCSS.ACTIVE);
    return this;
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final String sText, @Nonnull final ISimpleURL aTarget)
  {
    return addItem (sText, aTarget, DEFAULT_ACTIVE, (IIcon) null);
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final String sText,
                               @Nonnull final ISimpleURL aTarget,
                               @Nullable final IIcon aIcon)
  {
    return addItem (sText, aTarget, DEFAULT_ACTIVE, aIcon);
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final String sText, @Nonnull final ISimpleURL aTarget, final boolean bActive)
  {
    // no icon
    return addItem (sText, aTarget, bActive, null);
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final String sText,
                               @Nonnull final ISimpleURL aTarget,
                               final boolean bActive,
                               @Nullable final IIcon aIcon)
  {
    return addItem (new HCA (aTarget).addChild (sText), bActive, aIcon);
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final IHCElementWithChildren <?> aContent)
  {
    return addItem (aContent, DEFAULT_ACTIVE);
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final IHCElementWithChildren <?> aContent, final boolean bActive)
  {
    return addItem (aContent, bActive, null);
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final IHCElementWithChildren <?> aContent,
                               final boolean bActive,
                               @Nullable final IIcon aIcon)
  {
    final HCLI aItem = addAndReturnItem (aContent);
    if (bActive)
      aItem.addClass (CBootstrapCSS.ACTIVE);
    if (aIcon != null)
    {
      // Icon is the first child of the content
      aContent.addChild (0, aIcon.getAsNode ());
    }
    return this;
  }

  @Nonnull
  public BootstrapNav addDivider ()
  {
    addItem ().addClass (CBootstrapCSS.DIVIDER);
    return this;
  }

  @Nonnull
  public BootstrapNav addDividerVertical ()
  {
    addItem ().addClass (CBootstrapCSS.DIVIDER_VERTICAL);
    return this;
  }

  @Nonnull
  public HCLI addDropDownItem (@Nullable final String sText)
  {
    return addDropDownItem (new HCTextNode (sText));
  }

  @Nonnull
  public HCLI addDropDownItem (@Nullable final IHCNode aText)
  {
    final HCLI aLI = addItem ().addClass (CBootstrapCSS.DROPDOWN);
    aLI.addChild (new HCA (new SimpleURL ()).addClass (CBootstrapCSS.DROPDOWN_TOGGLE)
                                            .setCustomAttr ("data-toggle", "dropdown")
                                            .addChild (aText)
                                            .addChild (new BootstrapCaret ()));
    return aLI;
  }
}
