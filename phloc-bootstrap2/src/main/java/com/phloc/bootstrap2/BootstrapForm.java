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

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webctrls.custom.ELabelType;
import com.phloc.webctrls.custom.impl.HCFormLabelUtils;

public class BootstrapForm extends HCForm
{
  public BootstrapForm (@Nonnull final ISimpleURL aAction)
  {
    super (aAction);
  }

  @Nullable
  public static IHCNode createStaticLabel (@Nullable final String sLabel, @Nullable final ELabelType eType)
  {
    return createStaticLabel (sLabel, eType, (IHCNode) null);
  }

  @Nullable
  public static IHCNode createStaticLabel (@Nullable final String sLabel,
                                           @Nullable final ELabelType eType,
                                           @Nullable final IHCNode aFor)
  {
    if (StringHelper.hasNoText (sLabel))
      return null;

    final String sRealLabel = eType == null ? sLabel : HCFormLabelUtils.getTextWithState (sLabel, eType);
    final HCLabel aLabel = HCLabel.create (sRealLabel).addClass (CBootstrapCSS.CONTROL_LABEL);
    if (aFor instanceof IHCControl <?>)
      aLabel.setFor (((IHCControl <?>) aFor).getName ());
    return aLabel;
  }

  @Nullable
  @OverrideOnDemand
  protected IHCNode createLabel (@Nullable final String sLabel, @Nullable final IHCNode aFor)
  {
    return createStaticLabel (sLabel, ELabelType.DEFAULT, aFor);
  }

  @Nonnull
  public static HCDiv createStaticControlGroup (@Nullable final IHCNode aLabel,
                                                @Nonnull final IHCNodeBuilder aCtrlBuilder)
  {
    return createStaticControlGroup (aLabel, aCtrlBuilder.build ());
  }

  @Nonnull
  public static HCDiv createStaticControlGroup (@Nullable final IHCNode aLabel, @Nullable final IHCNode aCtrls)
  {
    return createStaticControlGroup ((EErrorLevel) null, aLabel, aCtrls, (IBootstrapHelpItem <?>) null);
  }

  @Nonnull
  public static HCDiv createStaticControlGroup (@Nullable final EErrorLevel eErrorLevel,
                                                @Nullable final IHCNode aLabel,
                                                @Nullable final IHCNode aCtrls,
                                                @Nullable final IBootstrapHelpItem <?> aHelpItem)
  {
    final HCDiv aCtrlGroup = new HCDiv ().addClasses (CBootstrapCSS.CONTROL_GROUP,
                                                      CBootstrapCSS.getCSSClass (eErrorLevel));
    aCtrlGroup.addChild (aLabel);
    final HCDiv aControls = aCtrlGroup.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROLS));
    aControls.addChildren (aCtrls, aHelpItem);
    return aCtrlGroup;
  }

  @Nonnull
  public BootstrapForm addControlGroup (@Nullable final EErrorLevel eErrorLevel,
                                        @Nullable final IHCNode aLabel,
                                        @Nullable final IHCNode aCtrls,
                                        @Nullable final IBootstrapHelpItem <?> aHelpItem)
  {
    addChild (createStaticControlGroup (eErrorLevel, aLabel, aCtrls, aHelpItem));
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
    return addControlGroup (null, null, new HCNodeList ().addChildren (aCtrls), null);
  }
}
