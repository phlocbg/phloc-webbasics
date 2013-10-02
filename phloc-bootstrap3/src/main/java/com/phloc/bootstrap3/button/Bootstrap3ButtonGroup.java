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
package com.phloc.bootstrap3.button;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.dropdown.Bootstrap3DropdownMenu;
import com.phloc.html.hc.html.AbstractHCDiv;

public class Bootstrap3ButtonGroup extends AbstractHCDiv <Bootstrap3ButtonGroup>
{
  private final EBootstrap3ButtonGroupType m_eType;
  private final EBootstrap3ButtonGroupSize m_eSize;

  public Bootstrap3ButtonGroup ()
  {
    this (EBootstrap3ButtonGroupType.DEFAULT, EBootstrap3ButtonGroupSize.DEFAULT);
  }

  public Bootstrap3ButtonGroup (@Nonnull final EBootstrap3ButtonGroupType eType)
  {
    this (eType, EBootstrap3ButtonGroupSize.DEFAULT);
  }

  public Bootstrap3ButtonGroup (@Nonnull final EBootstrap3ButtonGroupSize eSize)
  {
    this (EBootstrap3ButtonGroupType.DEFAULT, eSize);
  }

  public Bootstrap3ButtonGroup (@Nonnull final EBootstrap3ButtonGroupType eType,
                                @Nonnull final EBootstrap3ButtonGroupSize eSize)
  {
    addClasses (eType.getAllCSSClasses ());
    addClass (eSize);
    m_eType = eType;
    m_eSize = eSize;
  }

  @Nonnull
  public EBootstrap3ButtonGroupType getType ()
  {
    return m_eType;
  }

  @Nonnull
  public EBootstrap3ButtonGroupSize getSize ()
  {
    return m_eSize;
  }

  @Nonnull
  public Bootstrap3DropdownMenu addDropDownMenu ()
  {
    final Bootstrap3DropdownMenu aDDM = addAndReturnChild (new Bootstrap3DropdownMenu ());
    // Overwrite default "menu" role
    aDDM.setRole (null);
    return aDDM;
  }

  /**
   * Add a button and convert it to a dropdown menu
   * 
   * @param aButton
   *        The button to be added. May be <code>null</code>.
   * @return The created drop down button
   */
  @Nonnull
  public Bootstrap3DropdownMenu addButtonAsDropDownMenu (@Nullable final Bootstrap3Button aButton)
  {
    // Add caret to button
    Bootstrap3DropdownMenu.makeDropdownToggle (aButton);

    final Bootstrap3ButtonGroup aNestedGroup = addAndReturnChild (new Bootstrap3ButtonGroup ());
    aNestedGroup.addChild (aButton);
    return aNestedGroup.addAndReturnChild (addDropDownMenu ());
  }
}
