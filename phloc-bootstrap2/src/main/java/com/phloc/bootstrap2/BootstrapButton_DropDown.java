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
package com.phloc.bootstrap2;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;

public class BootstrapButton_DropDown extends BootstrapButton_Button
{
  private final BootstrapDropDownMenu m_aDropDownMenu;

  /**
   * This constructor is only to be invoked from within a button group!
   * 
   * @param aDropDownMenu
   */
  BootstrapButton_DropDown (@Nonnull final BootstrapDropDownMenu aDropDownMenu)
  {
    super ();
    if (aDropDownMenu == null)
      throw new NullPointerException ("DropDownMenu");
    addClass (CBootstrapCSS.DROPDOWN_TOGGLE).setDataAttr ("toggle", "dropdown");
    m_aDropDownMenu = aDropDownMenu;
  }

  @Nonnull
  public BootstrapDropDownMenu getDropDownMenu ()
  {
    return m_aDropDownMenu;
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void internalBeforeConvertToNode (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    super.internalBeforeConvertToNode (aConversionSettings);
    // Add caret as last child
    addChild (new BootstrapCaret ());
  }
}
