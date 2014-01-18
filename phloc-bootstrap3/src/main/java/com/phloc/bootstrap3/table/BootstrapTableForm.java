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
package com.phloc.bootstrap3.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCHasFocus;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.validation.error.IErrorList;
import com.phloc.webctrls.custom.IFormLabel;
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

  @Nonnull
  public BootstrapTableFormItemRow createItemRow ()
  {
    final BootstrapTableFormItemRow ret = new BootstrapTableFormItemRow (false, getColumnCount () > 2)
    {
      @Override
      protected void modifyControls (@Nonnull final Iterable <? extends IHCNode> aCtrls, final boolean bHasErrors)
      {
        _handleFocus (aCtrls, bHasErrors);
        super.modifyControls (aCtrls, bHasErrors);
      }
    };
    addBodyRow (ret);
    return ret;
  }

  @Nonnull
  @Deprecated
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final String sValue)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (sValue);
  }

  @Nonnull
  @Deprecated
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNodeBuilder aCtrlBuilder)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrlBuilder);
  }

  @Nonnull
  @Deprecated
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNode aCtrl)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrl);
  }

  @Nonnull
  @Deprecated
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final IHCNodeBuilder aCtrlBuilder,
                           @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrlBuilder).setErrorList (aFormErrors);
  }

  @Nonnull
  @Deprecated
  public HCRow addItemRow (@Nullable final IFormLabel aLabel,
                           @Nullable final IHCNode aCtrl,
                           @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrl).setErrorList (aFormErrors);
  }

  @Nonnull
  @Deprecated
  public HCRow addItemRow (@Nullable final IFormLabel aLabel, @Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrls);
  }

  @Nonnull
  @Deprecated
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

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final String sText,
                                   @Nullable final IHCNode aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (sText).setErrorList (aFormErrors).setNote (aNote);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNodeBuilder aCtrlBuilder,
                                   @Nullable final IHCNode aNote)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrlBuilder).setNote (aNote);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNode aCtrl,
                                   @Nullable final IHCNode aNote)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrl).setNote (aNote);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final Iterable <? extends IHCNode> aCtrls,
                                   @Nullable final IHCNode aNote)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrls).setNote (aNote);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNodeBuilder aCtrlBuilder,
                                   @Nullable final IHCNode aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrlBuilder).setErrorList (aFormErrors).setNote (aNote);
  }

  @Deprecated
  @Nonnull
  public HCRow addItemRowWithNote (@Nullable final IFormLabel aLabel,
                                   @Nullable final IHCNode aCtrl,
                                   @Nullable final IHCNode aNote,
                                   @Nullable final IErrorList aFormErrors)
  {
    return createItemRow ().setLabel (aLabel).setCtrl (aCtrl).setErrorList (aFormErrors).setNote (aNote);
  }

  @Deprecated
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
