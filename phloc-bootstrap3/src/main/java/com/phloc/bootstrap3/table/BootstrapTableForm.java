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
package com.phloc.bootstrap3.table;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.form.BootstrapHelpBlock;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCHasFocus;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.AbstractHCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.validation.error.IError;
import com.phloc.validation.error.IErrorList;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.IFormNote;
import com.phloc.webctrls.custom.table.HCTableFormItemRow;
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webscopes.mgr.WebScopeManager;

public class BootstrapTableForm extends AbstractBootstrapTable <BootstrapTableForm> implements IHCTableForm <BootstrapTableForm>
{
  private static final String REQUEST_ATTR_FIRST_FOCUSABLE = "Bootstrap3TableForm$FirstFocusable";

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
      final IHCControl <?> aRealControl = (IHCControl <?>) aCtrl;
      if (aRealControl.getID () == null)
        aRealControl.setID (GlobalIDFactory.getNewStringID ());
    }
  }

  @OverrideOnDemand
  @Deprecated
  protected void addLabelCell (@Nonnull final HCRow aRow, @Nullable final IFormLabel aLabel)
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
  {}

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

  @Nonnull
  @OverrideOnDemand
  @Deprecated
  protected IHCCell <?> addControlCell (@Nonnull final HCRow aRow,
                                        @Nullable final Iterable <? extends IHCNode> aCtrls,
                                        final boolean bHasError)
  {
    final List <IHCNode> aResolvedCtrls = new ArrayList <IHCNode> ();
    if (aCtrls != null)
    {
      // Inline all node lists for correct modification afterwards
      for (final IHCNode aCtrl : aCtrls)
        if (aCtrl instanceof AbstractHCNodeList <?>)
          aResolvedCtrls.addAll (((AbstractHCNodeList <?>) aCtrl).getChildren ());
        else
          aResolvedCtrls.add (aCtrl);

      // Customize controls
      modifyControls (aResolvedCtrls);
      _handleFocus (aResolvedCtrls, bHasError);
    }

    return aRow.addAndReturnCell (aResolvedCtrls);
  }

  @Deprecated
  public static void addControlCellErrorMessages (@Nonnull final IHCElementWithChildren <?> aParent,
                                                  @Nullable final IErrorList aFormErrors)
  {
    if (aFormErrors != null)
      for (final IError aError : aFormErrors.getAllItems ())
        aParent.addChild (new BootstrapHelpBlock ().addChild (aError.getErrorText ()));
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
        BootstrapTableForm.this.modifyControls (aCtrls);
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

  @Deprecated
  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final String sValue)
  {
    return addItemRow (aLabel, new HCTextNode (sValue), null);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNodeBuilder aCtrlBuilder)
  {
    return addItemRow (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), null);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNode aCtrl)
  {
    return addItemRow (aLabel, aCtrl, null);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final IHCNodeBuilder aCtrlBuilder,
                           @Nullable final IErrorList aFormErrors)
  {
    return addItemRow (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), aFormErrors);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final IHCNode aCtrl,
                           @Nullable final IErrorList aFormErrors)
  {
    return addItemRow (aLabel, aCtrl == null ? (List <IHCNode>) null : ContainerHelper.newList (aCtrl), aFormErrors);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    return addItemRow (aLabel, aCtrls, null);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final Iterable <? extends IHCNode> aCtrls,
                           @Nullable final IErrorList aFormErrors)
  {
    final EErrorLevel eHighest = aFormErrors == null ? null : aFormErrors.getMostSevereErrorLevel ();

    // Start row
    final HCRow aRow = super.addBodyRow ();
    if (eHighest != null && !eHighest.isSuccess ())
      aRow.addClass (CBootstrapCSS.getCSSClass (eHighest));

    // Label cell
    addLabelCell (aRow, aLabel);

    // Add main control
    final IHCCell <?> aCtrlCell = addControlCell (aRow, aCtrls, eHighest != null);

    // Add error messages
    addControlCellErrorMessages (aCtrlCell, aFormErrors);
    return aRow;
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final String sText,
                                   @Nullable final IFormNote aNote)
  {
    return addItemRowWithNote (aLabel, new HCTextNode (sText), aNote, null);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final String sText,
                                   @Nullable final IFormNote aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    return addItemRowWithNote (aLabel, new HCTextNode (sText), aNote, aFormErrors);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNodeBuilder aCtrlBuilder,
                                   @Nullable final IFormNote aNote)
  {
    return addItemRowWithNote (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), aNote, null);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNode aCtrl,
                                   @Nullable final IFormNote aNote)
  {
    return addItemRowWithNote (aLabel, aCtrl, aNote, null);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final Iterable <? extends IHCNode> aCtrls,
                                   @Nullable final IFormNote aNote)
  {
    return addItemRowWithNote (aLabel, aCtrls, aNote, null);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNodeBuilder aCtrlBuilder,
                                   @Nullable final IFormNote aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    return addItemRowWithNote (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), aNote, aFormErrors);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNode aCtrl,
                                   @Nullable final IFormNote aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    final HCRow aRow = addItemRow (aLabel, aCtrl, aFormErrors);
    aRow.addCell ().addChild (aNote);
    return aRow;
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final Iterable <? extends IHCNode> aCtrls,
                                   @Nullable final IFormNote aNote,
                                   @Nullable final IErrorList aFormErrors)
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
        focusNode (m_aFirstFocusable);
    }
    super.applyProperties (aDivElement, aConversionSettings);
  }
}
