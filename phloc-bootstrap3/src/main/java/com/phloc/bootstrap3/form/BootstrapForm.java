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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.grid.EBootstrapGridMD;
import com.phloc.bootstrap3.grid.IBootstrapGridElementExtended;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.html.AbstractHCEdit;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.html.HCRadioButton;
import com.phloc.html.hc.impl.HCTextNode;

public class BootstrapForm extends HCForm
{
  private final EBootstrapFormType m_eFormType;

  public BootstrapForm ()
  {
    this ((ISimpleURL) null);
  }

  public BootstrapForm (@Nullable final ISimpleURL aAction)
  {
    this (aAction, EBootstrapFormType.DEFAULT);
  }

  public BootstrapForm (@Nonnull final EBootstrapFormType eFormType)
  {
    this ((ISimpleURL) null, eFormType);
  }

  public BootstrapForm (@Nullable final String sAction)
  {
    this (sAction, EBootstrapFormType.DEFAULT);
  }

  public BootstrapForm (@Nullable final ISimpleURL aAction, @Nonnull final EBootstrapFormType eFormType)
  {
    this (aAction == null ? null : aAction.getAsString (), eFormType);
  }

  public BootstrapForm (@Nullable final String sAction, @Nonnull final EBootstrapFormType eFormType)
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

  @Nonnull
  protected IBootstrapGridElementExtended getLeft ()
  {
    return EBootstrapGridMD.MD_2;
  }

  @Nonnull
  protected IBootstrapGridElementExtended getRight ()
  {
    return EBootstrapGridMD.MD_10;
  }

  private static void _getAllHCControls (@Nullable final IHCNode aNode, @Nonnull final List <IHCControl <?>> ret)
  {
    if (aNode instanceof IHCControl <?>)
      ret.add ((IHCControl <?>) aNode);
    if (aNode instanceof IHCNodeWithChildren <?>)
    {
      // E.g. HCNodeList
      final IHCNodeWithChildren <?> aParent = (IHCNodeWithChildren <?>) aNode;
      if (aParent.hasChildren ())
        for (final IHCNode aChild : aParent.getChildren ())
          _getAllHCControls (aChild, ret);
    }
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final String sLabel, @Nonnull final IHCNode aCtrl)
  {
    if (aCtrl == null)
      throw new NullPointerException ("ctrl");

    final List <IHCControl <?>> aAllCtrls = new ArrayList <IHCControl <?>> ();
    // FIXME replace with HCUtils.getAllHCControls in phloc-html > 3.12.1
    _getAllHCControls (aCtrl, aAllCtrls);

    // Set CSS class to all contained controls
    for (final IHCControl <?> aCurCtrl : aAllCtrls)
      if (!(aCurCtrl instanceof HCCheckBox) && !(aCurCtrl instanceof HCRadioButton))
        aCurCtrl.addClass (CBootstrapCSS.FORM_CONTROL);

    final IHCControl <?> aFirstControl = ContainerHelper.getFirstElement (aAllCtrls);
    if (aFirstControl instanceof HCCheckBox)
    {
      // Check box
      final HCDiv aCheckbox = new HCDiv ().addClass (CBootstrapCSS.CHECKBOX);
      final HCLabel aLabel = aCheckbox.addAndReturnChild (new HCLabel ());
      aLabel.addChild (aCtrl);
      if (StringHelper.hasText (sLabel))
        aLabel.addChild (new HCTextNode (" " + sLabel));

      if (m_eFormType == EBootstrapFormType.HORIZONTAL)
        addChild (new HCDiv ().addClasses (getLeft ().getCSSClassOffset (), getRight ()).addChild (aCheckbox));
      else
        addChild (aCheckbox);
    }
    else
      if (aFirstControl instanceof HCRadioButton)
      {
        // Radio button
        final HCDiv aRadio = new HCDiv ().addClass (CBootstrapCSS.RADIO);
        final HCLabel aLabel = aRadio.addAndReturnChild (new HCLabel ());
        aLabel.addChild (aCtrl);
        if (StringHelper.hasText (sLabel))
          aLabel.addChild (new HCTextNode (" " + sLabel));

        if (m_eFormType == EBootstrapFormType.HORIZONTAL)
          addChild (new HCDiv ().addClasses (getLeft ().getCSSClassOffset (), getRight ()).addChild (aRadio));
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
          if (m_eFormType == EBootstrapFormType.INLINE)
            aLabel.addClass (CBootstrapCSS.SR_ONLY);
          else
            if (m_eFormType == EBootstrapFormType.HORIZONTAL)
              aLabel.addClasses (CBootstrapCSS.CONTROL_LABEL, getLeft ());

          if (aFirstControl != null)
          {
            // We have a label for a control
            aLabel.setFor (aFirstControl);

            // Set the default placeholder (if none is present)
            if (aFirstControl instanceof AbstractHCEdit <?>)
            {
              final AbstractHCEdit <?> aEdit = (AbstractHCEdit <?>) aFirstControl;
              if (StringHelper.hasNoText (aEdit.getPlaceholder ()))
                aEdit.setPlaceholder (sLabel);
            }
          }

          // Add in form group
          final HCDiv aFormGroup = new HCDiv ().addClass (CBootstrapCSS.FORM_GROUP);
          if (m_eFormType == EBootstrapFormType.HORIZONTAL)
            addChild (aFormGroup.addChildren (aLabel, new HCDiv ().addClass (getRight ()).addChild (aCtrl)));
          else
            addChild (aFormGroup.addChildren (aLabel, aCtrl));
        }
        else
        {
          // No label - just add controls
          if (m_eFormType == EBootstrapFormType.HORIZONTAL)
          {
            final HCDiv aFormGroup = new HCDiv ().addClass (CBootstrapCSS.FORM_GROUP);
            aFormGroup.addChild (new HCDiv ().addClasses (getLeft ().getCSSClassOffset (), getRight ())
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
