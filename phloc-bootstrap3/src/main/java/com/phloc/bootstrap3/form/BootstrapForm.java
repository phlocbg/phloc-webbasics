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

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.bootstrap3.BootstrapHelper;
import com.phloc.bootstrap3.CBootstrap;
import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.grid.BootstrapGridSpec;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.AbstractHCEdit;
import com.phloc.html.hc.html.AbstractHCForm;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.html.HCRadioButton;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.validation.error.IError;
import com.phloc.validation.error.IErrorList;

public class BootstrapForm extends AbstractHCForm <BootstrapForm>
{
  private final EBootstrapFormType m_eFormType;
  private BootstrapGridSpec m_aLeft = BootstrapGridSpec.create (2);
  private BootstrapGridSpec m_aRight = BootstrapGridSpec.create (10);

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
  public final BootstrapGridSpec getLeft ()
  {
    return m_aLeft;
  }

  /**
   * @return The right parts. Always <code>12 - getLeft ()</code>. Never
   *         <code>null</code>.
   */
  @Nonnull
  public final BootstrapGridSpec getRight ()
  {
    return m_aRight;
  }

  /**
   * Set the left part of a horizontal form. This implies setting the correct
   * right parts (= 12 - left).
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
    final BootstrapGridSpec aNewLeft = BootstrapGridSpec.create (nLeftParts);
    final BootstrapGridSpec aNewRight = BootstrapGridSpec.create (CBootstrap.GRID_SYSTEM_MAX - nLeftParts);
    return setSplitting (aNewLeft, aNewRight);
  }

  /**
   * Set the left part of a horizontal form.
   * 
   * @param aLeft
   *        The left parts. Must not be <code>null</code>.
   * @param aRight
   *        The right parts. Must not be <code>null</code>.
   * @return this
   */
  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public BootstrapForm setSplitting (@Nonnull final BootstrapGridSpec aLeft, @Nonnull final BootstrapGridSpec aRight)
  {
    if (aLeft == null)
      throw new NullPointerException ("left");
    if (aRight == null)
      throw new NullPointerException ("right");
    m_aLeft = aLeft;
    m_aRight = aRight;
    return this;
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final String sLabel, @Nonnull final IHCNode aCtrls)
  {
    return addFormGroup (sLabel, aCtrls, (IErrorList) null);
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final String sLabel,
                                     @Nonnull final IHCNode aCtrls,
                                     @Nullable final IErrorList aErrorList)
  {
    return addFormGroup (StringHelper.hasNoText (sLabel) ? null : new HCLabel ().addChild (sLabel), aCtrls, aErrorList);
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final String sLabel,
                                     @Nonnull final IHCNodeBuilder aCtrl,
                                     @Nullable final IErrorList aErrorList)
  {
    return addFormGroup (StringHelper.hasNoText (sLabel) ? null : new HCLabel ().addChild (sLabel),
                         aCtrl.build (),
                         aErrorList);
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final IHCElementWithChildren <?> aLabel, @Nonnull final IHCNode aCtrls)
  {
    return addFormGroup (aLabel, aCtrls, (IErrorList) null);
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final IHCElementWithChildren <?> aLabel,
                                     @Nonnull final IHCNode aCtrls,
                                     @Nullable final IErrorList aErrorList)
  {
    return addFormGroup (aLabel, aCtrls, (IHCNode) null, aErrorList);
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final IHCElementWithChildren <?> aLabel,
                                     @Nonnull final IHCNode aCtrls,
                                     @Nullable final String sHelpText,
                                     @Nullable final IErrorList aErrorList)
  {
    return addFormGroup (aLabel, aCtrls, HCTextNode.createOnDemand (sHelpText), aErrorList);
  }

  @Nonnull
  public BootstrapForm addFormGroup (@Nullable final IHCElementWithChildren <?> aLabel,
                                     @Nonnull final IHCNode aCtrls,
                                     @Nullable final IHCNode aHelpText,
                                     @Nullable final IErrorList aErrorList)
  {
    final IHCNode aFormGroup = createFormGroup (aLabel, aCtrls, aHelpText, aErrorList);
    addChild (aFormGroup);
    return this;
  }

  @Nonnull
  public IHCNode createFormGroup (@Nullable final IHCElementWithChildren <?> aLabel,
                                  @Nonnull final IHCNode aCtrls,
                                  @Nullable final IHCNode aHelpText,
                                  @Nullable final IErrorList aErrorList)
  {
    return createFormGroup (m_eFormType, m_aLeft, m_aRight, aLabel, aCtrls, aHelpText, aErrorList);
  }

  @Nonnull
  public static IHCNode createFormGroup (@Nonnull final EBootstrapFormType eFormType,
                                         @Nullable final BootstrapGridSpec aLeft,
                                         @Nullable final BootstrapGridSpec aRight,
                                         @Nullable final IHCElementWithChildren <?> aLabel,
                                         @Nonnull final IHCNode aCtrls,
                                         @Nullable final IHCNode aHelpText,
                                         @Nullable final IErrorList aErrorList)
  {
    if (aCtrls == null)
      throw new NullPointerException ("ctrl");

    final List <IHCControl <?>> aAllCtrls = HCUtils.getAllHCControls (aCtrls);

    // Set CSS class to all contained controls
    BootstrapHelper.markAsFormControls (aAllCtrls);

    final IHCControl <?> aFirstControl = ContainerHelper.getFirstElement (aAllCtrls);
    HCDiv aFinalNode;
    if (aFirstControl instanceof HCCheckBox)
    {
      // Check box
      final HCDiv aCheckboxDiv = new HCDiv ().addClass (CBootstrapCSS.CHECKBOX);
      if (aLabel == null || !aLabel.hasChildren ())
      {
        aCheckboxDiv.addChild (new HCLabel ().addChild (aCtrls));
      }
      else
      {
        aLabel.addChild (0, aCtrls);
        aLabel.addChild (1, " ");
        aCheckboxDiv.addChild (aLabel);
      }

      if (eFormType == EBootstrapFormType.HORIZONTAL)
      {
        final HCDiv aChild = new HCDiv ();
        aLeft.applyOffsetTo (aChild);
        aRight.applyTo (aChild);
        aFinalNode = new HCDiv ().addClass (CBootstrapCSS.FORM_GROUP).addChild (aChild.addChild (aCheckboxDiv));
      }
      else
        aFinalNode = aCheckboxDiv;
    }
    else
      if (aFirstControl instanceof HCRadioButton)
      {
        // Radio button
        final HCDiv aRadioDiv = new HCDiv ().addClass (CBootstrapCSS.RADIO);
        if (aLabel == null || !aLabel.hasChildren ())
        {
          aRadioDiv.addChild (new HCLabel ().addChild (aCtrls));
        }
        else
        {
          aLabel.addChild (0, aCtrls);
          aLabel.addChild (1, " ");
          aRadioDiv.addChild (aLabel);
        }

        if (eFormType == EBootstrapFormType.HORIZONTAL)
        {
          final HCDiv aChild = new HCDiv ();
          aLeft.applyOffsetTo (aChild);
          aRight.applyTo (aChild);
          aFinalNode = new HCDiv ().addClass (CBootstrapCSS.FORM_GROUP).addChild (aChild.addChild (aRadioDiv));
        }
        else
          aFinalNode = aRadioDiv;
      }
      else
      {
        // Other control - add in form group
        aFinalNode = new HCDiv ().addClass (CBootstrapCSS.FORM_GROUP);

        if (aLabel != null && aLabel.hasChildren ())
        {
          // We have a label

          // Screen reader only....
          if (eFormType == EBootstrapFormType.INLINE)
            aLabel.addClass (CBootstrapCSS.SR_ONLY);
          else
            if (eFormType == EBootstrapFormType.HORIZONTAL)
            {
              aLabel.addClass (CBootstrapCSS.CONTROL_LABEL);
              aLeft.applyTo (aLabel);
            }

          if (aFirstControl != null)
          {
            // We have a label for a control
            if (aLabel instanceof HCLabel)
              ((HCLabel) aLabel).setFor (aFirstControl);

            // Set the default placeholder (if none is present)
            if (aFirstControl instanceof AbstractHCEdit <?>)
            {
              final AbstractHCEdit <?> aEdit = (AbstractHCEdit <?>) aFirstControl;
              // Only check for null, so that empty string overrides this
              // default behaviour
              if (aEdit.getPlaceholder () == null)
                aEdit.setPlaceholder (aLabel.getPlainText ());
            }
          }

          if (eFormType == EBootstrapFormType.HORIZONTAL)
          {
            final HCDiv aChild = new HCDiv ();
            aRight.applyTo (aChild);
            aFinalNode.addChildren (aLabel, aChild.addChild (aCtrls));
          }
          else
            aFinalNode.addChildren (aLabel, aCtrls);
        }
        else
        {
          // No label - just add controls
          if (eFormType == EBootstrapFormType.HORIZONTAL)
          {
            final HCDiv aChild = new HCDiv ();
            aLeft.applyOffsetTo (aChild);
            aRight.applyTo (aChild);
            aFinalNode.addChild (aChild.addChild (aCtrls));
          }
          else
            aFinalNode.addChild (aCtrls);
        }
      }

    // Help text
    if (aHelpText != null)
    {
      final BootstrapHelpBlock aHelpBlock = new BootstrapHelpBlock ().addChild (aHelpText);
      if (eFormType == EBootstrapFormType.INLINE)
        aHelpBlock.addClass (CBootstrapCSS.SR_ONLY);
      if (eFormType == EBootstrapFormType.HORIZONTAL)
        ((HCDiv) aFinalNode.getLastChild ()).addChild (aHelpBlock);
      else
        aFinalNode.addChild (aHelpBlock);
    }

    // Check form errors - highlighting
    if (aErrorList != null)
    {
      if (aErrorList.containsAtLeastOneError ())
        aFinalNode.addClass (CBootstrapCSS.HAS_ERROR);
      else
        if (aErrorList.containsAtLeastOneFailure ())
          aFinalNode.addClass (CBootstrapCSS.HAS_WARNING);

      for (final IError aError : aErrorList)
      {
        final BootstrapHelpBlock aHelpBlock = new BootstrapHelpBlock ().addChild (aError.getErrorText ());
        if (eFormType == EBootstrapFormType.HORIZONTAL)
        {
          aLeft.applyOffsetTo (aHelpBlock);
          aRight.applyTo (aHelpBlock);
        }
        aFinalNode.addChild (aHelpBlock);
      }
    }
    return aFinalNode;
  }
}
