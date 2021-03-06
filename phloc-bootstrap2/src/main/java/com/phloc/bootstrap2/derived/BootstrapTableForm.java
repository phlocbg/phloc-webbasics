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
package com.phloc.bootstrap2.derived;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap2.AbstractBootstrapTable;
import com.phloc.bootstrap2.BootstrapHelpBlock;
import com.phloc.bootstrap2.CBootstrapCSS;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCHasFocus;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRadioButton;
import com.phloc.html.hc.html.HCRow;
import com.phloc.validation.error.IError;
import com.phloc.validation.error.IErrorList;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.table.HCTableFormItemRow;
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webscopes.mgr.WebScopeManager;

public class BootstrapTableForm extends AbstractBootstrapTable <BootstrapTableForm> implements IHCTableForm <BootstrapTableForm>
{
  private static final String REQUEST_ATTR_FIRST_FOCUSABLE = "BootstrapTableForm$FirstFocusable";

  private boolean m_bFocusHandlingEnabled = true;
  private boolean m_bSetAutoFocus = false;
  private IHCHasFocus <?> m_aFirstFocusable;

  public BootstrapTableForm (@Nullable final HCCol... aWidths)
  {
    super (aWidths);
    setCondensed (true);
    setStriped (true);
  }

  @Nonnull
  public BootstrapTableForm setFocusHandlingEnabled (final boolean bFocusHandlingEnabled)
  {
    m_bFocusHandlingEnabled = bFocusHandlingEnabled;
    return this;
  }

  public boolean isFocusHandlingEnabled ()
  {
    return m_bFocusHandlingEnabled;
  }

  @OverrideOnDemand
  protected void focusNode (@Nonnull final IHCHasFocus <?> aCtrl)
  {
    aCtrl.setFocused (true);
    if (aCtrl instanceof IHCControl <?>)
    {
      // Ensure that an ID is present
      ((IHCControl <?>) aCtrl).ensureID ();
    }
  }

  private void _handleFocus (@Nonnull final Iterable <? extends IHCNode> aCtrls, final boolean bHasErrors)
  {
    if (isFocusHandlingEnabled ())
    {
      // Set focus on first element with error
      if (bHasErrors && !m_bSetAutoFocus)
        for (final IHCNode aCtrl : aCtrls)
          if (aCtrl instanceof IHCHasFocus <?>)
          {
            focusNode ((IHCHasFocus <?>) aCtrl);
            m_bSetAutoFocus = true;
            break;
          }

      // Find first focusable control
      if (m_aFirstFocusable == null)
        for (final IHCNode aCtrl : aCtrls)
          if (aCtrl instanceof IHCHasFocus <?>)
          {
            m_aFirstFocusable = (IHCHasFocus <?>) aCtrl;
            break;
          }
    }
  }

  public static final boolean hasDefinedInputSize (@Nonnull final IHCElement <?> aElement)
  {
    final Set <ICSSClassProvider> aClasses = aElement.getAllClasses ();
    return aClasses.contains (CBootstrapCSS.INPUT_MINI) ||
           aClasses.contains (CBootstrapCSS.INPUT_SMALL) ||
           aClasses.contains (CBootstrapCSS.INPUT_MEDIUM) ||
           aClasses.contains (CBootstrapCSS.INPUT_LARGE) ||
           aClasses.contains (CBootstrapCSS.INPUT_XLARGE) ||
           aClasses.contains (CBootstrapCSS.INPUT_XXLARGE) ||
           aClasses.contains (CBootstrapCSS.INPUT_BLOCK_LEVEL);
  }

  @Nonnull
  public HCTableFormItemRow createItemRow ()
  {
    final HCTableFormItemRow ret = new HCTableFormItemRow (false, getColumnCount () > 2)
    {
      @Override
      protected IHCNode createErrorNode (@Nonnull final IError aError)
      {
        return new BootstrapHelpBlock ().addChild (aError.getErrorText ());
      }

      @Override
      protected void modifyControls (@Nonnull final Iterable <? extends IHCNode> aCtrls, final boolean bHasErrors)
      {
        // Set full width class on first control
        for (final IHCNode aCtrl : aCtrls)
          if (aCtrl instanceof IHCElement <?> &&
              !(aCtrl instanceof HCCheckBox) &&
              !(aCtrl instanceof HCRadioButton) &&
              !(aCtrl instanceof IHCTable <?>))
          {
            final IHCElement <?> aElement = (IHCElement <?>) aCtrl;
            // Don't resize elements with a predefined size
            if (!hasDefinedInputSize (aElement))
              aElement.addClass (CBootstrapCSS.INPUT_BLOCK_LEVEL);
            break;
          }

        _handleFocus (aCtrls, bHasErrors);
        if (bHasErrors)
          addClass (CBootstrapCSS.getCSSClass (EErrorLevel.ERROR));
        else
          removeClass (CBootstrapCSS.getCSSClass (EErrorLevel.ERROR));
      }
    };
    addBodyRow (ret);
    return ret;
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final String sValue)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (sValue);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNodeBuilder aCtrlBuilder)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrlBuilder);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNode aCtrl)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrl);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final IHCNodeBuilder aCtrlBuilder,
                           @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrlBuilder).setErrorList (aFormErrors);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final IHCNode aCtrl,
                           @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrl).setErrorList (aFormErrors);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrls);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final Iterable <? extends IHCNode> aCtrls,
                           @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrls).setErrorList (aFormErrors);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final String sText,
                                   @Nullable final IHCNode aNote)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (sText).setNote (aNote);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final String sText,
                                   @Nullable final IHCNode aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (sText).setErrorList (aFormErrors).setNote (aNote);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNodeBuilder aCtrlBuilder,
                                   @Nullable final IHCNode aNote)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrlBuilder).setNote (aNote);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNode aCtrl,
                                   @Nullable final IHCNode aNote)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrl).setNote (aNote);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final Iterable <? extends IHCNode> aCtrls,
                                   @Nullable final IHCNode aNote)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrls).setNote (aNote);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNodeBuilder aCtrlBuilder,
                                   @Nullable final IHCNode aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrlBuilder).setErrorList (aFormErrors).setNote (aNote);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNode aCtrl,
                                   @Nullable final IHCNode aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrl).setErrorList (aFormErrors).setNote (aNote);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final Iterable <? extends IHCNode> aCtrls,
                                   @Nullable final IHCNode aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrls).setErrorList (aFormErrors).setNote (aNote);
  }

  @Override
  protected void applyProperties (final IMicroElement aDivElement, final IHCConversionSettingsToNode aConversionSettings)
  {
    if (isFocusHandlingEnabled () && !m_bSetAutoFocus && m_aFirstFocusable != null)
    {
      // No focus has yet be set
      // Try to focus the first control (if available), but do it only once per
      // request because the cursor can only be on one control at a time :)
      if (!WebScopeManager.getRequestScope ().getAndSetAttributeFlag (REQUEST_ATTR_FIRST_FOCUSABLE))
        focusNode (m_aFirstFocusable);
    }
    super.applyProperties (aDivElement, aConversionSettings);
  }
}
