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
package com.phloc.webctrls.bootstrap.derived;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.webctrls.bootstrap.BootstrapButton;
import com.phloc.webctrls.bootstrap.EBootstrapIcon;

/**
 * Special button
 * 
 * @author philip
 */
public class BootstrapButtonOK extends BootstrapButton
{
  private void _init ()
  {
    setIcon (EBootstrapIcon.ICON_OK);
  }

  public BootstrapButtonOK ()
  {
    super ();
    _init ();
  }

  public BootstrapButtonOK (@Nonnull final IPredefinedLocaleTextProvider aChild)
  {
    this (aChild.getText ());
  }

  public BootstrapButtonOK (@Nullable final String sChild)
  {
    super (sChild);
    _init ();
  }
}
