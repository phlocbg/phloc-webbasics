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
package com.phloc.webbasics.ui.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.impl.AbstractHCControl;
import com.phloc.html.hc.impl.AbstractHCElement;

public class BootstrapFormHorizontal extends HCForm
{
  public BootstrapFormHorizontal (@Nonnull final ISimpleURL aAction)
  {
    super (aAction);
    addClass (CBootstrapCSS.FORM_HORIZONTAL);
  }

  @Nonnull
  public BootstrapFormHorizontal addControlGroup (@Nullable final String sLabel,
                                                  @Nullable final AbstractHCControl <?> aCtrl)
  {
    final HCDiv aCtrlGroup = addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROL_GROUP));
    aCtrlGroup.addChild (new HCLabel (sLabel).addClass (CBootstrapCSS.CONTROL_LABEL).setFor (aCtrl.getName ()));
    final HCDiv aControls = aCtrlGroup.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROLS));
    aControls.addChild (aCtrl);
    return this;
  }

  @Nonnull
  public BootstrapFormHorizontal addControlGroup (@Nullable final AbstractHCElement <?>... aCtrls)
  {
    final HCDiv aCtrlGroup = addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROL_GROUP));
    final HCDiv aControls = aCtrlGroup.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROLS));
    aControls.addChildren (aCtrls);
    return this;
  }
}
