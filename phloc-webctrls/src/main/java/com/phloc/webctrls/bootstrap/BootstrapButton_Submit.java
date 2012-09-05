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
package com.phloc.webctrls.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.api.EHCButtonType;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

public class BootstrapButton_Submit extends BootstrapButton
{
  private void _init ()
  {
    setType (EHCButtonType.SUBMIT);
    setType (EBootstrapButtonType.PRIMARY);
  }

  public BootstrapButton_Submit ()
  {
    super ();
    _init ();
  }

  public BootstrapButton_Submit (@Nullable final String sLabel)
  {
    super (sLabel);
    _init ();
  }

  public BootstrapButton_Submit (@Nullable final IIcon aIcon)
  {
    super (aIcon);
    _init ();
  }

  public BootstrapButton_Submit (@Nullable final String sLabel, @Nullable final IIcon aIcon)
  {
    super (sLabel, aIcon);
    _init ();
  }

  public BootstrapButton_Submit (@Nonnull final EDefaultIcon eIcon)
  {
    super (eIcon);
    _init ();
  }

  public BootstrapButton_Submit (@Nullable final String sLabel, @Nonnull final EDefaultIcon eIcon)
  {
    super (sLabel, eIcon);
    _init ();
  }
}
