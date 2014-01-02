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
package com.phloc.webctrls.custom.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCHasFocus;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.AbstractHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.validation.error.IErrorList;
import com.phloc.webctrls.custom.IFormLabel;

public abstract class AbstractHCTableForm <IMPLTYPE extends AbstractHCTableForm <IMPLTYPE>> extends AbstractHCTable <IMPLTYPE> implements IHCTableForm <IMPLTYPE>
{
  private boolean m_bFocusHandlingEnabled = true;
  private boolean m_bSetAutoFocus = false;
  private IHCHasFocus <?> m_aFirstFocusable;

  public AbstractHCTableForm ()
  {
    super ();
  }

  public AbstractHCTableForm (@Nullable final HCCol aCol)
  {
    super (aCol);
  }

  public AbstractHCTableForm (@Nullable final HCCol... aCols)
  {
    super (aCols);
  }

  public AbstractHCTableForm (@Nullable final Iterable <? extends HCCol> aCols)
  {
    super (aCols);
  }

  @Nonnull
  public IMPLTYPE setFocusHandlingEnabled (final boolean bFocusHandlingEnabled)
  {
    m_bFocusHandlingEnabled = bFocusHandlingEnabled;
    return thisAsT ();
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
  public HCTableFormItemRow createItemRow ()
  {
    final HCTableFormItemRow ret = new HCTableFormItemRow (false, getColumnCount () > 2)
    {
      @Override
      protected void modifyControls (@Nonnull final Iterable <? extends IHCNode> aCtrls, final boolean bHasErrors)
      {
        _handleFocus (aCtrls, bHasErrors);
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
}
