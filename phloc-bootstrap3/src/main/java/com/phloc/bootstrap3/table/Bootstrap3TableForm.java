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

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.bootstrap3.form.Bootstrap3HelpBlock;
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
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webscopes.mgr.WebScopeManager;

public class Bootstrap3TableForm extends AbstractBootstrap3Table <Bootstrap3TableForm> implements IHCTableForm <Bootstrap3TableForm>
{
  private static final String REQUEST_ATTR_FIRST_FOCUSABLE = "Bootstrap3TableForm$FirstFocusable";

  private boolean m_bFocusHandlingEnabled = true;
  private boolean m_bSetAutoFocus = false;
  private IHCHasFocus <?> m_aFirstFocusable;

  public Bootstrap3TableForm (@Nullable final HCCol... aWidths)
  {
    super (aWidths);
    setCondensed (true);
    setStriped (true);
  }

  @Nonnull
  public Bootstrap3TableForm setFocusHandlingEnabled (final boolean bFocusHandlingEnabled)
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

  @Nonnull
  @OverrideOnDemand
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

      if (isFocusHandlingEnabled ())
      {
        // Set focus on first element with error
        if (bHasError && !m_bSetAutoFocus)
          for (final IHCNode aCtrl : aResolvedCtrls)
            if (aCtrl instanceof IHCHasFocus <?>)
            {
              focusNode ((IHCHasFocus <?>) aCtrl);
              m_bSetAutoFocus = true;
              break;
            }

        // Find first focusable control
        if (m_aFirstFocusable == null)
          for (final IHCNode aCtrl : aResolvedCtrls)
            if (aCtrl instanceof IHCHasFocus <?>)
            {
              m_aFirstFocusable = (IHCHasFocus <?>) aCtrl;
              break;
            }
      }
    }

    return aRow.addAndReturnCell (aResolvedCtrls);
  }

  public static void addControlCellErrorMessages (@Nonnull final IHCElementWithChildren <?> aParent,
                                                  @Nullable final IErrorList aFormErrors)
  {
    if (aFormErrors != null)
      for (final IError aError : aFormErrors.getAllItems ())
        aParent.addChild (new Bootstrap3HelpBlock ().addChild (aError.getErrorText ()));
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
                           @Nullable final IErrorList aFormErrors)
  {
    return addItemRow (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), aFormErrors);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final IHCNode aCtrl,
                           @Nullable final IErrorList aFormErrors)
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
                           @Nullable final IErrorList aFormErrors)
  {
    final EErrorLevel eHighest = aFormErrors == null ? null : aFormErrors.getMostSevereErrorLevel ();

    // Start row
    final HCRow aRow = super.addBodyRow ();
    if (eHighest != null)
      aRow.addClass (CBootstrap3CSS.getCSSClass (eHighest));

    // Label cell
    addLabelCell (aRow, aLabel);

    // Add main control
    final IHCCell <?> aCtrlCell = addControlCell (aRow, aCtrls, eHighest != null);

    // Add error messages
    addControlCellErrorMessages (aCtrlCell, aFormErrors);
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
                                   @Nullable final IErrorList aFormErrors)
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
                                   @Nullable final IErrorList aFormErrors)
  {
    return addItemRowWithNote (aLabel, aCtrlBuilder == null ? null : aCtrlBuilder.build (), aNote, aFormErrors);
  }

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