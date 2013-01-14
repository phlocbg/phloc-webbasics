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
package com.phloc.webctrls.bootstrap.derived;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.api.IHCHasFocus;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.form.validation.EFormErrorLevel;
import com.phloc.webbasics.form.validation.IFormFieldError;
import com.phloc.webbasics.form.validation.IFormFieldErrorList;
import com.phloc.webctrls.bootstrap.BootstrapHelpBlock;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.IFormNote;

public final class BootstrapTableForm extends BootstrapTable
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

  private static void _focusNode (@Nonnull final IHCHasFocus <?> aCtrl)
  {
    aCtrl.setFocused (true);
    if (aCtrl instanceof IHCControl <?>)
    {
      // Ensure that an ID is present
      final IHCControl <?> aRealControl = (IHCControl <?>) aCtrl;
      if (aRealControl.getID () == null)
        aRealControl.setID (GlobalIDFactory.getNewStringID ());
    }
  }

  private void _addLabelCell (@Nonnull final HCRow aRow, @Nullable final IFormLabel aLabel)
  {
    aRow.addCell (aLabel);
  }

  /**
   * Modify the passed controls for a certain row
   * 
   * @param aCtrls
   *        The list of controls for a cell. Never <code>null</code> but maybe
   *        empty
   */
  @OverrideOnDemand
  protected void modifyControls (@Nonnull final Iterable <? extends IHCNode> aCtrls)
  {
    // Set full width class on first control
    for (final IHCNode aCtrl : aCtrls)
      if (aCtrl instanceof IHCElement <?> && !(aCtrl instanceof HCCheckBox))
      {
        ((IHCElement <?>) aCtrl).addClass (CBootstrapCSS.INPUT_BLOCK_LEVEL);
        break;
      }
  }

  @Nonnull
  private AbstractHCCell _addControlCell (@Nonnull final HCRow aRow,
                                          @Nullable final Iterable <? extends IHCNode> aCtrls,
                                          final boolean bHasError)
  {
    if (aCtrls != null)
    {
      modifyControls (aCtrls);

      if (isFocusHandlingEnabled ())
      {
        // Set focus on first element with error
        if (bHasError && !m_bSetAutoFocus)
          for (final IHCNode aCtrl : aCtrls)
            if (aCtrl instanceof IHCHasFocus <?>)
            {
              _focusNode ((IHCHasFocus <?>) aCtrl);
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

    return aRow.addAndReturnCell (aCtrls);
  }

  private static void _addControlCellErrorMessages (@Nonnull final AbstractHCCell aCell,
                                                    @Nullable final IFormFieldErrorList aFormErrors)
  {
    if (aFormErrors != null)
      for (final IFormFieldError aError : aFormErrors.getAllItems ())
        aCell.addChild (new BootstrapHelpBlock ().addChild (aError.getErrorText ()));
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final String sValue)
  {
    return addItemRow (aLabel, new HCTextNode (sValue), null);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNodeBuilder aCtrlBuilder)
  {
    return addItemRow (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), null);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNode aCtrl)
  {
    return addItemRow (aLabel, aCtrl, null);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final IHCNodeBuilder aCtrlBuilder,
                           @Nullable final IFormFieldErrorList aFormErrors)
  {
    return addItemRow (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), aFormErrors);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final IHCNode aCtrl,
                           @Nullable final IFormFieldErrorList aFormErrors)
  {
    return addItemRow (aLabel, aCtrl == null ? (List <IHCNode>) null : ContainerHelper.newList (aCtrl), aFormErrors);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    return addItemRow (aLabel, aCtrls, null);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final Iterable <? extends IHCNode> aCtrls,
                           @Nullable final IFormFieldErrorList aFormErrors)
  {
    final EFormErrorLevel eHighest = aFormErrors == null ? null : aFormErrors.getMostSevereErrorLevel ();

    // Start row
    final HCRow aRow = super.addBodyRow ();
    if (eHighest != null)
      aRow.addClass (CBootstrapCSS.getCSSClass (eHighest));

    // Label cell
    _addLabelCell (aRow, aLabel);

    // Add main control
    final AbstractHCCell aCtrlCell = _addControlCell (aRow, aCtrls, eHighest != null);

    // Add error messages
    _addControlCellErrorMessages (aCtrlCell, aFormErrors);
    return aRow;
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final String sText,
                                   @Nullable final IFormNote aNote)
  {
    return addItemRowWithNote (aLabel, new HCTextNode (sText), aNote, null);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final String sText,
                                   @Nullable final IFormNote aNote,
                                   @Nullable final IFormFieldErrorList aFormErrors)
  {
    return addItemRowWithNote (aLabel, new HCTextNode (sText), aNote, aFormErrors);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNodeBuilder aCtrlBuilder,
                                   @Nullable final IFormNote aNote)
  {
    return addItemRowWithNote (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), aNote, null);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNode aCtrl,
                                   @Nullable final IFormNote aNote)
  {
    return addItemRowWithNote (aLabel, aCtrl, aNote, null);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final Iterable <? extends IHCNode> aCtrls,
                                   @Nullable final IFormNote aNote)
  {
    return addItemRowWithNote (aLabel, aCtrls, aNote, null);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNodeBuilder aCtrlBuilder,
                                   @Nullable final IFormNote aNote,
                                   @Nullable final IFormFieldErrorList aFormErrors)
  {
    return addItemRowWithNote (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), aNote, aFormErrors);
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNode aCtrl,
                                   @Nullable final IFormNote aNote,
                                   @Nullable final IFormFieldErrorList aFormErrors)
  {
    final HCRow aRow = addItemRow (aLabel, aCtrl, aFormErrors);
    aRow.addCell ().addChild (aNote);
    return aRow;
  }

  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final Iterable <? extends IHCNode> aCtrls,
                                   @Nullable final IFormNote aNote,
                                   @Nullable final IFormFieldErrorList aFormErrors)
  {
    final HCRow aRow = addItemRow (aLabel, aCtrls, aFormErrors);
    aRow.addCell ().addChild (aNote);
    return aRow;
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
        _focusNode (m_aFirstFocusable);
    }
    super.applyProperties (aDivElement, aConversionSettings);
  }
}
