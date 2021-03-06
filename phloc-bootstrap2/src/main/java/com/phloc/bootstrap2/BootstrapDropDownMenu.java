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
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;

public class BootstrapDropDownMenu extends HCUL
{
  public BootstrapDropDownMenu ()
  {
    super ();
    addClass (CBootstrapCSS.DROPDOWN_MENU);
  }

  @Nonnull
  public BootstrapDropDownMenu addMenuItem (@Nonnull final ISimpleURL aURL,
                                            @Nullable final String sLabel,
                                            final boolean bActive)
  {
    final HCLI aLI = addAndReturnItem (new HCA (aURL).addChild (sLabel));
    if (bActive)
      aLI.addClass (CBootstrapCSS.ACTIVE);
    return this;
  }

  @Nonnull
  public BootstrapDropDownMenu addMenuItem (@Nonnull final ISimpleURL aURL,
                                            @Nullable final IHCNode aLabel,
                                            final boolean bActive)
  {
    final HCLI aLI = addAndReturnItem (new HCA (aURL).addChild (aLabel));
    if (bActive)
      aLI.addClass (CBootstrapCSS.ACTIVE);
    return this;
  }

  @Nonnull
  public BootstrapDropDownMenu addActiveMenuItem (@Nonnull final ISimpleURL aURL, @Nullable final String sLabel)
  {
    return addMenuItem (aURL, sLabel, true);
  }

  @Nonnull
  public BootstrapDropDownMenu addActiveMenuItem (@Nonnull final ISimpleURL aURL, @Nullable final IHCNode aLabel)
  {
    return addMenuItem (aURL, aLabel, true);
  }
}
