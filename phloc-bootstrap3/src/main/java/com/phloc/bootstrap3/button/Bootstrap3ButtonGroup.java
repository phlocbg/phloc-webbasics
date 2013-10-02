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
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.impl.HCTextNode;

public class Bootstrap3ButtonGroup extends AbstractHCDiv <Bootstrap3ButtonGroup>
{
  private final EBootstrap3ButtonGroupType m_eType;

  public Bootstrap3ButtonGroup ()
  {
    this (EBootstrap3ButtonGroupType.DEFAULT);
  }

  public Bootstrap3ButtonGroup (@Nonnull final EBootstrap3ButtonGroupType eType)
  {
    addClasses (eType.getAllCSSClasses ());
    m_eType = eType;
  }

  @Nonnull
  public EBootstrap3ButtonGroupType getType ()
  {
    return m_eType;
  }

  /**
   * Add a new empty drop down menu to the group.
   * 
   * @param sDropDownLabel
   *        The label to be displayed. May be <code>null</code>.
   * @return The created drop down button
   */
  @Nonnull
  public Bootstrap3DropdownMenu addDropDownMenu (@Nullable final String sDropDownLabel)
  {
    return addDropDownMenu (new HCTextNode (sDropDownLabel));
  }

  /**
   * Add a new empty drop down menu to the group.
   * 
   * @param aDropDownLabel
   *        The label to be displayed. May be <code>null</code>.
   * @return The created drop down button
   */
  @Nonnull
  public Bootstrap3DropdownMenu addDropDownMenu (@Nullable final IHCNode aDropDownLabel)
  {
    final Bootstrap3ButtonGroup aNestedGroup = addAndReturnChild (new Bootstrap3ButtonGroup ());
    {
      // Drop down button + caret
      final Bootstrap3Button aButton = aNestedGroup.addAndReturnChild (new Bootstrap3Button ());
      aButton.addChild (aDropDownLabel);
      Bootstrap3DropdownMenu.makeDropdownToggle (aButton);
    }
    final Bootstrap3DropdownMenu aDDM = aNestedGroup.addAndReturnChild (new Bootstrap3DropdownMenu ());
    // Overwrite default "menu" role
    aDDM.setRole (null);
    return aDDM;
  }
}
