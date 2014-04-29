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
package com.phloc.bootstrap3.form;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.validation.error.IErrorList;
import com.phloc.webctrls.custom.ELabelType;
import com.phloc.webctrls.custom.impl.HCFormLabel;

@NotThreadSafe
public class BootstrapFormGroup
{
  private HCLabel m_aLabel;
  private IHCNode m_aCtrl;
  private IHCNode m_aHelpText;
  private IErrorList m_aErrorList;

  public BootstrapFormGroup ()
  {}

  @Nonnull
  public final BootstrapFormGroup setLabel (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return setLabel (aTextProvider.getText ());
  }

  @Nonnull
  public final BootstrapFormGroup setLabel (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.create (sLabel));
  }

  @Nonnull
  public final BootstrapFormGroup setLabel (@Nullable final String sLabel, @Nonnull final ELabelType eLabelType)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.create (sLabel, eLabelType));
  }

  @Nonnull
  public final BootstrapFormGroup setLabelMandatory (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.createMandatory (sLabel));
  }

  @Nonnull
  public final BootstrapFormGroup setLabelAlternative (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.createAlternative (sLabel));
  }

  /**
   * Called after the label cell was altered.
   * 
   * @param aLabel
   *        The newly set label. May be <code>null</code>
   */
  @OverrideOnDemand
  protected void onLabelModified (@Nullable final HCLabel aLabel)
  {}

  @Nonnull
  public final BootstrapFormGroup setLabel (@Nullable final HCLabel aLabel)
  {
    m_aLabel = aLabel;
    onLabelModified (aLabel);
    return this;
  }

  @Nullable
  public final HCLabel getLabel ()
  {
    return m_aLabel;
  }

  /**
   * Called after the control was changed
   * 
   * @param aCtrl
   *        The new control. May be <code>null</code>.
   */
  @OverrideOnDemand
  protected void onCtrlModified (@Nullable final IHCNode aCtrl)
  {}

  @Nonnull
  public final BootstrapFormGroup setCtrl (@Nullable final String sValue)
  {
    return setCtrl (new HCTextNode (sValue));
  }

  @Nonnull
  public final BootstrapFormGroup setCtrl (@Nullable final String... aValues)
  {
    return setCtrl (HCNodeList.create (aValues));
  }

  @Nonnull
  public final BootstrapFormGroup setCtrl (@Nullable final IHCNodeBuilder aCtrlBuilder)
  {
    return setCtrl (aCtrlBuilder == null ? null : aCtrlBuilder.build ());
  }

  @Nonnull
  public final BootstrapFormGroup setCtrl (@Nullable final IHCNode aCtrl)
  {
    m_aCtrl = aCtrl;
    onCtrlModified (aCtrl);
    return this;
  }

  @Nonnull
  public final BootstrapFormGroup setCtrl (@Nullable final IHCNode... aCtrls)
  {
    return setCtrl (HCNodeList.create (aCtrls));
  }

  @Nonnull
  public final BootstrapFormGroup setCtrl (@Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    return setCtrl (HCNodeList.create (aCtrls));
  }

  @Nullable
  public final IHCNode getCtrl ()
  {
    return m_aCtrl;
  }

  @Nonnull
  public final BootstrapFormGroup setErrorList (@Nullable final IErrorList aErrorList)
  {
    m_aErrorList = aErrorList;
    return this;
  }

  @Nullable
  public final IErrorList getErrorList ()
  {
    return m_aErrorList;
  }

  /**
   * Called after the help text was altered.
   * 
   * @param aNote
   *        The new note
   */
  @OverrideOnDemand
  protected void onHelpTextModified (@Nullable final IHCNode aNote)
  {}

  @Nonnull
  public final BootstrapFormGroup setHelpText (@Nullable final String sHelpText)
  {
    return setHelpText (HCTextNode.createOnDemand (sHelpText));
  }

  @Nonnull
  public final BootstrapFormGroup setHelpText (@Nullable final IHCNode aHelpText)
  {
    m_aHelpText = aHelpText;
    onHelpTextModified (aHelpText);
    return this;
  }

  @Nullable
  public final IHCNode getHelpText ()
  {
    return m_aHelpText;
  }
}
