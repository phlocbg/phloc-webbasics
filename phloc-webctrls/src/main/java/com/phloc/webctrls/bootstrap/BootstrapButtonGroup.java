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

import com.phloc.html.hc.html.AbstractHCDiv;

/**
 * Bootstrap block help.
 * 
 * @author Philip Helger
 */
public class BootstrapButtonGroup extends AbstractHCDiv <BootstrapButtonGroup>
{
  public BootstrapButtonGroup ()
  {
    super ();
    addClass (CBootstrapCSS.BTN_GROUP);
  }

  /**
   * Add a new empty drop down button to the group.
   * 
   * @return The created drop down button
   */
  @Nonnull
  public BootstrapButton_DropDown addDropDownButton ()
  {
    final BootstrapDropDownMenu aDDM = new BootstrapDropDownMenu ();
    final BootstrapButton_DropDown aButton = new BootstrapButton_DropDown (aDDM);
    addChild (aButton);
    addChild (aDDM);
    return aButton;
  }
}
