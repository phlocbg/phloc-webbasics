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

import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.impl.HCNodeList;

public class BootstrapForm extends HCForm
{
  public BootstrapForm (@Nonnull final ISimpleURL aAction)
  {
    super (aAction);
  }

  @Nullable
  protected IHCNode createLabel (final String sLabel, @Nullable final IHCNode aFor)
  {
    if (StringHelper.hasNoText (sLabel))
      return null;
    final HCLabel aLabel = new HCLabel (sLabel).addClass (CBootstrapCSS.CONTROL_LABEL);
    if (aFor instanceof IHCControl <?>)
      aLabel.setFor (((IHCControl <?>) aFor).getName ());
    return aLabel;
  }

  @Nonnull
  public BootstrapForm addControlGroup (@Nullable final EErrorLevel eErrorLevel,
                                        @Nullable final IHCNode aLabel,
                                        @Nullable final IHCNode aCtrls,
                                        @Nullable final String sErrorMsg)
  {
    final HCDiv aCtrlGroup = addAndReturnChild (new HCDiv ().addClasses (CBootstrapCSS.CONTROL_GROUP,
                                                                         CBootstrapCSS.getCSSClass (eErrorLevel)));
    aCtrlGroup.addChild (aLabel);
    final HCDiv aControls = aCtrlGroup.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROLS));
    aControls.addChild (aCtrls);
    if (StringHelper.hasText (sErrorMsg))
      aControls.addChild (new BootstrapHelpInline (sErrorMsg));
    return this;
  }

  @Nonnull
  public BootstrapForm addControlGroup (@Nullable final String sLabel, @Nullable final IHCNode aCtrl)
  {
    return addControlGroup (null, createLabel (sLabel, aCtrl), aCtrl, null);
  }

  @Nonnull
  public BootstrapForm addControlGroup (@Nullable final IHCNode aCtrl)
  {
    return addControlGroup (null, null, aCtrl, null);
  }

  @Nonnull
  public BootstrapForm addControlGroup (@Nullable final IHCNode... aCtrls)
  {
    return addControlGroup (null, null, new HCNodeList (aCtrls), null);
  }
}
