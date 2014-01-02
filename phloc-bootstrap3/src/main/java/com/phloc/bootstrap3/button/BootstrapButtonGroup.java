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
package com.phloc.bootstrap3.button;

import javax.annotation.Nonnull;

import com.phloc.bootstrap3.dropdown.BootstrapDropdownMenu;
import com.phloc.html.hc.html.AbstractHCDiv;

public class BootstrapButtonGroup extends AbstractHCDiv <BootstrapButtonGroup>
{
  private final EBootstrapButtonGroupType m_eType;
  private final EBootstrapButtonGroupSize m_eSize;

  public BootstrapButtonGroup ()
  {
    this (EBootstrapButtonGroupType.DEFAULT, EBootstrapButtonGroupSize.DEFAULT);
  }

  public BootstrapButtonGroup (@Nonnull final EBootstrapButtonGroupType eType)
  {
    this (eType, EBootstrapButtonGroupSize.DEFAULT);
  }

  public BootstrapButtonGroup (@Nonnull final EBootstrapButtonGroupSize eSize)
  {
    this (EBootstrapButtonGroupType.DEFAULT, eSize);
  }

  public BootstrapButtonGroup (@Nonnull final EBootstrapButtonGroupType eType,
                               @Nonnull final EBootstrapButtonGroupSize eSize)
  {
    addClasses (eType.getAllCSSClasses ());
    addClass (eSize);
    m_eType = eType;
    m_eSize = eSize;
  }

  @Nonnull
  public EBootstrapButtonGroupType getType ()
  {
    return m_eType;
  }

  @Nonnull
  public EBootstrapButtonGroupSize getSize ()
  {
    return m_eSize;
  }

  @Nonnull
  public BootstrapDropdownMenu addDropDownMenu ()
  {
    final BootstrapDropdownMenu aDDM = addAndReturnChild (new BootstrapDropdownMenu ());
    // Overwrite default "menu" role
    aDDM.setRole (null);
    return aDDM;
  }

  /**
   * Add a button and convert it to a dropdown menu
   * 
   * @param aButton
   *        The button to be added. May not be <code>null</code>.
   * @return The created drop down button
   */
  @Nonnull
  public BootstrapDropdownMenu addButtonAsDropDownMenu (@Nonnull final BootstrapButton aButton)
  {
    // Add caret to button
    BootstrapDropdownMenu.makeDropdownToggle (aButton);

    final BootstrapButtonGroup aNestedGroup = addAndReturnChild (new BootstrapButtonGroup ());
    aNestedGroup.addChild (aButton);
    return aNestedGroup.addAndReturnChild (addDropDownMenu ());
  }
}
