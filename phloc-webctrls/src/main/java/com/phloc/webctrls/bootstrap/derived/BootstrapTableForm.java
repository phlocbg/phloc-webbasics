package com.phloc.webctrls.bootstrap.derived;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.api.IHCHasFocus;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.scopes.web.domain.IRequestWebScope;
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
  private static final String REQUEST_ATTR = "BootstrapTableForm$FirstFocusable";

  private boolean m_bSetAutoFocus = false;
  private IHCNode m_aFirstFocusable;

  public BootstrapTableForm (final HCCol... aWidths)
  {
    super (aWidths);
    setCondensed (true);
    setStriped (true);
  }

  private static void _focusNode (@Nonnull final IHCNode aCtrl)
  {
    ((IHCHasFocus <?>) aCtrl).setFocused (true);
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

  @Nonnull
  private AbstractHCCell _addControlCell (@Nonnull final HCRow aRow,
                                          @Nullable final Iterable <? extends IHCNode> aCtrls,
                                          final boolean bHasError)
  {
    if (aCtrls != null)
    {
      // Set full width class on first control
      for (final IHCNode aCtrl : aCtrls)
        if (aCtrl instanceof IHCElement <?> && !(aCtrl instanceof HCCheckBox))
        {
          ((IHCElement <?>) aCtrl).addClass (CBootstrapCSS.INPUT_XXLARGE);
          break;
        }

      // Set focus on first element with error
      if (bHasError && !m_bSetAutoFocus)
        for (final IHCNode aCtrl : aCtrls)
          if (aCtrl instanceof IHCHasFocus <?>)
          {
            _focusNode (aCtrl);
            m_bSetAutoFocus = true;
            break;
          }

      // Find first focusable control
      if (m_aFirstFocusable == null)
        for (final IHCNode aCtrl : aCtrls)
          if (aCtrl instanceof IHCHasFocus <?>)
          {
            m_aFirstFocusable = aCtrl;
            break;
          }
    }

    return aRow.addAndReturnCell (aCtrls);
  }

  private static void _addControlCellErrorMessages (@Nonnull final AbstractHCCell aCell,
                                                    @Nullable final IFormFieldErrorList aFormErrors)
  {
    if (aFormErrors != null)
      for (final IFormFieldError aError : aFormErrors.getAllItems ())
        aCell.addChild (new BootstrapHelpBlock (aError.getErrorText ()));
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final String sValue)
  {
    return addItemRow (aLabel, new HCTextNode (sValue), null);
  }

  @Nonnull
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNode aCtrl)
  {
    return addItemRow (aLabel, aCtrl, null);
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
  protected void applyProperties (final IMicroElement aDivElement, final IHCConversionSettings aConversionSettings)
  {
    if (!m_bSetAutoFocus && m_aFirstFocusable != null)
    {
      // No focus has yet be set
      // Try to focus the first control (if available), but do it only once per
      // request because the cursor can only be on one control at a time :)
      final IRequestWebScope aScope = WebScopeManager.getRequestScope ();
      if (!aScope.containsAttribute (REQUEST_ATTR))
      {
        _focusNode (m_aFirstFocusable);
        aScope.setAttribute (REQUEST_ATTR, Boolean.TRUE);
      }
    }
    super.applyProperties (aDivElement, aConversionSettings);
  }
}
