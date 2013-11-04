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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.bootstrap3.CBootstrap;
import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.grid.EBootstrapGridSM;
import com.phloc.bootstrap3.grid.IBootstrapGridElementExtended;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.css.ICSSClassProvider;
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
import com.phloc.validation.error.IError;
import com.phloc.validation.error.IErrorList;

public class BootstrapForm extends HCForm
{
  private final EBootstrapFormType m_eFormType;
  private EBootstrapGridSM m_aLeft = EBootstrapGridSM.SM_2;
  private EBootstrapGridSM m_aRight = EBootstrapGridSM.SM_10;

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

  /**
   * @return The left parts. Always &ge; 1 and &lt; 12. Never <code>null</code>.
   */
  @Nonnull
  public final IBootstrapGridElementExtended getLeft ()
  {
    return m_aLeft;
  }

  /**
   * @return The right parts. Always <code>12 - getLeft ()</code>. Never
   *         <code>null</code>.
   */
  @Nonnull
  public final IBootstrapGridElementExtended getRight ()
  {
    return m_aRight;
  }

  /**
   * Set the left part of a horizontal form.
   * 
   * @param nLeftParts
   *        The left parts. Must be &ge; 1 and &lt; 12!
   * @return this
   */
  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public BootstrapForm setLeft (@Nonnegative final int nLeftParts)
  {
    if (nLeftParts < 1 || nLeftParts >= CBootstrap.GRID_SYSTEM_MAX)
      throw new IllegalArgumentException ("Left parts must be >= 1 and < 12!");
    final EBootstrapGridSM aNewLeft = EBootstrapGridSM.getFromParts (nLeftParts);
    final EBootstrapGridSM aNewRight = EBootstrapGridSM.getFromParts (CBootstrap.GRID_SYSTEM_MAX - nLeftParts);
    if (aNewLeft == null || aNewRight == null)
      throw new IllegalStateException ("Unhandled error for " + nLeftParts);
    m_aLeft = aNewLeft;
    m_aRight = aNewRight;
    return this;
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final String sLabel, @Nonnull final IHCNode aCtrl)
  {
    return addFormGroup (sLabel, aCtrl, (IErrorList) null);
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final String sLabel,
                                     @Nonnull final IHCNode aCtrls,
                                     @Nullable final IErrorList aErrorList)
  {
    if (aCtrls == null)
      throw new NullPointerException ("ctrl");

    final List <IHCControl <?>> aAllCtrls = new ArrayList <IHCControl <?>> ();
    HCUtils.getAllHCControls (aCtrls, aAllCtrls);

    // Set CSS class to all contained controls
    for (final IHCControl <?> aCurCtrl : aAllCtrls)
      if (!(aCurCtrl instanceof HCCheckBox) && !(aCurCtrl instanceof HCRadioButton))
        aCurCtrl.addClass (CBootstrapCSS.FORM_CONTROL);

    // Check form errors
    ICSSClassProvider aErrorCSS = null;
    if (aErrorList != null)
    {
      if (aErrorList.containsAtLeastOneError ())
        aErrorCSS = CBootstrapCSS.HAS_ERROR;
      else
        if (aErrorList.containsAtLeastOneFailure ())
          aErrorCSS = CBootstrapCSS.HAS_WARNING;
    }

    final IHCControl <?> aFirstControl = ContainerHelper.getFirstElement (aAllCtrls);
    HCDiv aFinalNode;
    if (aFirstControl instanceof HCCheckBox)
    {
      // Check box
      final HCDiv aCheckboxDiv = new HCDiv ().addClass (CBootstrapCSS.CHECKBOX);
      final HCLabel aLabel = aCheckboxDiv.addAndReturnChild (new HCLabel ());
      aLabel.addChild (aCtrls);
      if (StringHelper.hasText (sLabel))
        aLabel.addChild (new HCTextNode (" " + sLabel));

      if (m_eFormType == EBootstrapFormType.HORIZONTAL)
        aFinalNode = new HCDiv ().addClass (CBootstrapCSS.FORM_GROUP)
                                 .addChild (new HCDiv ().addClasses (m_aLeft.getCSSClassOffset (), m_aRight)
                                                        .addChild (aCheckboxDiv));
      else
        aFinalNode = aCheckboxDiv;
    }
    else
      if (aFirstControl instanceof HCRadioButton)
      {
        // Radio button
        final HCDiv aRadioDiv = new HCDiv ().addClass (CBootstrapCSS.RADIO);
        final HCLabel aLabel = aRadioDiv.addAndReturnChild (new HCLabel ());
        aLabel.addChild (aCtrls);
        if (StringHelper.hasText (sLabel))
          aLabel.addChild (new HCTextNode (" " + sLabel));

        if (m_eFormType == EBootstrapFormType.HORIZONTAL)
          aFinalNode = new HCDiv ().addClass (CBootstrapCSS.FORM_GROUP)
                                   .addChild (new HCDiv ().addClasses (m_aLeft.getCSSClassOffset (), m_aRight)
                                                          .addChild (aRadioDiv));
        else
          aFinalNode = aRadioDiv;
      }
      else
      {
        // Other control - add in form group
        aFinalNode = new HCDiv ().addClass (CBootstrapCSS.FORM_GROUP);

        if (StringHelper.hasText (sLabel))
        {
          // We have a label
          final HCLabel aLabel = HCLabel.create (sLabel);

          // Screen reader only....
          if (m_eFormType == EBootstrapFormType.INLINE)
            aLabel.addClass (CBootstrapCSS.SR_ONLY);
          else
            if (m_eFormType == EBootstrapFormType.HORIZONTAL)
              aLabel.addClasses (CBootstrapCSS.CONTROL_LABEL, m_aLeft);

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

          if (m_eFormType == EBootstrapFormType.HORIZONTAL)
            aFinalNode.addChildren (aLabel, new HCDiv ().addClass (m_aRight).addChild (aCtrls));
          else
            aFinalNode.addChildren (aLabel, aCtrls);
        }
        else
        {
          // No label - just add controls
          if (m_eFormType == EBootstrapFormType.HORIZONTAL)
            aFinalNode.addChild (new HCDiv ().addClasses (m_aLeft.getCSSClassOffset (), m_aRight).addChild (aCtrls));
          else
            aFinalNode.addChild (aCtrls);
        }
      }

    // Add error highlighting
    aFinalNode.addClass (aErrorCSS);
    if (aErrorList != null)
      for (final IError aError : aErrorList)
      {
        final BootstrapHelpBlock aHelpBlock = new BootstrapHelpBlock ().addChild (aError.getErrorText ());
        if (m_eFormType == EBootstrapFormType.HORIZONTAL)
          aHelpBlock.addClasses (m_aLeft.getCSSClassOffset (), m_aRight);
        aFinalNode.addChild (aHelpBlock);
      }
    addChild (aFinalNode);
    return this;
  }
}
