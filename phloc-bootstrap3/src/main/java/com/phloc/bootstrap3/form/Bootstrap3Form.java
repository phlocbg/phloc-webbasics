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
package com.phloc.bootstrap3.form;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.bootstrap3.grid.EBootstrap3GridMD;
import com.phloc.bootstrap3.grid.IBootstrap3GridElementExtended;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCEdit;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.html.HCRadioButton;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.impl.HCTextNode;

public class Bootstrap3Form extends HCForm
{
  private final EBootstrap3FormType m_eFormType;

  public Bootstrap3Form ()
  {
    this ((ISimpleURL) null);
  }

  public Bootstrap3Form (@Nullable final ISimpleURL aAction)
  {
    this (aAction, EBootstrap3FormType.DEFAULT);
  }

  public Bootstrap3Form (@Nonnull final EBootstrap3FormType eFormType)
  {
    this ((ISimpleURL) null, eFormType);
  }

  public Bootstrap3Form (@Nullable final String sAction)
  {
    this (sAction, EBootstrap3FormType.DEFAULT);
  }

  public Bootstrap3Form (@Nullable final ISimpleURL aAction, @Nonnull final EBootstrap3FormType eFormType)
  {
    this (aAction == null ? null : aAction.getAsString (), eFormType);
  }

  public Bootstrap3Form (@Nullable final String sAction, @Nonnull final EBootstrap3FormType eFormType)
  {
    super ();
    if (eFormType == null)
      throw new NullPointerException ("FormType");
    if (sAction != null)
      setAction (sAction);
    m_eFormType = eFormType;
    setRole (EHTMLRole.FORM);
    addClass (eFormType);
  }

  private IBootstrap3GridElementExtended _getLeft ()
  {
    return EBootstrap3GridMD.MD_2;
  }

  private IBootstrap3GridElementExtended _getRight ()
  {
    return EBootstrap3GridMD.MD_10;
  }

  @Nonnull
  public Bootstrap3Form addFormGroup (@Nullable final String sLabel, @Nonnull final IHCNode aCtrl)
  {
    if (aCtrl == null)
      throw new NullPointerException ("ctrl");

    final IHCControl <?> aFirstControl = HCUtils.getFirstHCControl (aCtrl);
    if (aFirstControl instanceof HCCheckBox)
    {
      // Check box
      final HCDiv aCheckbox = new HCDiv ().addClass (CBootstrap3CSS.CHECKBOX);
      final HCLabel aLabel = aCheckbox.addAndReturnChild (new HCLabel ());
      aLabel.addChild (aCtrl);
      if (StringHelper.hasText (sLabel))
        aLabel.addChild (new HCTextNode (" " + sLabel));

      if (m_eFormType == EBootstrap3FormType.HORIZONTAL)
        addChild (new HCDiv ().addClasses (_getLeft ().getCSSClassOffset (), _getRight ()).addChild (aCheckbox));
      else
        addChild (aCheckbox);
    }
    else
      if (aFirstControl instanceof HCRadioButton)
      {
        // Radio button
        final HCDiv aRadio = new HCDiv ().addClass (CBootstrap3CSS.RADIO);
        final HCLabel aLabel = aRadio.addAndReturnChild (new HCLabel ());
        aLabel.addChild (aCtrl);
        if (StringHelper.hasText (sLabel))
          aLabel.addChild (new HCTextNode (" " + sLabel));

        if (m_eFormType == EBootstrap3FormType.HORIZONTAL)
          addChild (new HCDiv ().addClasses (_getLeft ().getCSSClassOffset (), _getRight ()).addChild (aRadio));
        else
          addChild (aRadio);
      }
      else
      {
        // Other control
        if (StringHelper.hasText (sLabel))
        {
          // We have a label
          final HCLabel aLabel = HCLabel.create (sLabel);

          // Screen reader only....
          if (m_eFormType == EBootstrap3FormType.INLINE)
            aLabel.addClass (CBootstrap3CSS.SR_ONLY);
          else
            if (m_eFormType == EBootstrap3FormType.HORIZONTAL)
              aLabel.addClasses (CBootstrap3CSS.CONTROL_LABEL, _getLeft ());

          if (aFirstControl != null)
          {
            // We have a label for a control
            String sControlID = aFirstControl.getID ();
            if (StringHelper.hasNoText (sControlID))
            {
              sControlID = GlobalIDFactory.getNewStringID ();
              aFirstControl.setID (sControlID);
            }
            aLabel.setFor (sControlID);

            // Ensure the form-control class is present
            aFirstControl.addClass (CBootstrap3CSS.FORM_CONTROL);

            // Set the default placeholder (if none is present)
            if (aFirstControl instanceof AbstractHCEdit <?>)
            {
              final AbstractHCEdit <?> aEdit = (AbstractHCEdit <?>) aFirstControl;
              if (StringHelper.hasNoText (aEdit.getPlaceholder ()))
                aEdit.setPlaceholder (sLabel);
            }
          }

          // Add in form group
          final HCDiv aFormGroup = new HCDiv ().addClass (CBootstrap3CSS.FORM_GROUP);
          if (m_eFormType == EBootstrap3FormType.HORIZONTAL)
            addChild (aFormGroup.addChildren (aLabel, new HCDiv ().addClass (_getRight ()).addChild (aCtrl)));
          else
            addChild (aFormGroup.addChildren (aLabel, aCtrl));
        }
        else
        {
          // No label - just add controls
          if (m_eFormType == EBootstrap3FormType.HORIZONTAL)
          {
            final HCDiv aFormGroup = new HCDiv ().addClass (CBootstrap3CSS.FORM_GROUP);
            aFormGroup.addChild (new HCDiv ().addClasses (_getLeft ().getCSSClassOffset (), _getRight ())
                                             .addChild (aCtrl));
            addChild (aFormGroup);
          }
          else
          {
            addChild (aCtrl);
          }
        }
      }
    return this;
  }
}
