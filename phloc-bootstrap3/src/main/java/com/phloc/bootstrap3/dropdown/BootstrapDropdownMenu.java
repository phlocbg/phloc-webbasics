/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.bootstrap3.dropdown;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.base.BootstrapCaret;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;

public class BootstrapDropdownMenu extends HCUL
{
  public BootstrapDropdownMenu ()
  {
    addClass (CBootstrapCSS.DROPDOWN_MENU);
    setRole (EHTMLRole.MENU);
  }

  @Override
  protected void onAddItem (@Nonnull final HCLI aItem)
  {
    if (aItem.getRole () == null)
      aItem.setRole (EHTMLRole.PRESENTATION);
  }

  @Nonnull
  public BootstrapDropdownMenu addMenuItem (@Nonnull final ISimpleURL aURL,
                                            @Nullable final String sLabel,
                                            final boolean bActive)
  {
    final HCLI aLI = addAndReturnItem (new HCA (aURL).addChild (sLabel));
    if (bActive)
      aLI.addClass (CBootstrapCSS.ACTIVE);
    return this;
  }

  @Nonnull
  public BootstrapDropdownMenu addMenuItem (@Nonnull final ISimpleURL aURL,
                                            @Nullable final IHCNode aLabel,
                                            final boolean bActive)
  {
    final HCLI aLI = addAndReturnItem (new HCA (aURL).addChild (aLabel));
    if (bActive)
      aLI.addClass (CBootstrapCSS.ACTIVE);
    return this;
  }

  @Nonnull
  public BootstrapDropdownMenu addActiveMenuItem (@Nonnull final ISimpleURL aURL, @Nullable final String sLabel)
  {
    return addMenuItem (aURL, sLabel, true);
  }

  @Nonnull
  public BootstrapDropdownMenu addActiveMenuItem (@Nonnull final ISimpleURL aURL, @Nullable final IHCNode aLabel)
  {
    return addMenuItem (aURL, aLabel, true);
  }

  @Nonnull
  public BootstrapDropdownMenu addDivider ()
  {
    addItem ().addClass (CBootstrapCSS.DIVIDER);
    return this;
  }

  @Nonnull
  public BootstrapDropdownMenu addHeader (@Nullable final String sHeaderText)
  {
    if (StringHelper.hasText (sHeaderText))
      addItem (sHeaderText).addClass (CBootstrapCSS.DROPDOWN_HEADER);
    return this;
  }

  public static void disableItem (@Nonnull final HCLI aItem)
  {
    aItem.addClass (CBootstrapCSS.DISABLED);
  }

  /**
   * Call this method to convert an element to a dropdown toggle. Important:
   * call this after all children are added, because a caret is added at the
   * end!
   * 
   * @param aElement
   *        The element to use. May not be <code>null</code>.
   */
  @Nonnull
  public static <IMPLTYPE extends IHCElementWithChildren <?>> IMPLTYPE makeDropdownToggle (@Nonnull final IMPLTYPE aElement)
  {
    aElement.addClass (CBootstrapCSS.DROPDOWN_TOGGLE)
            .setDataAttr ("toggle", "dropdown")
            .addChild (new BootstrapCaret ());
    return aElement;
  }
}
