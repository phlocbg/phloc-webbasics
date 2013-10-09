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
package com.phloc.webctrls.custom.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCBaseTable;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCRow;
import com.phloc.validation.error.IErrorList;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.IFormNote;

/**
 * Base interface for a form table
 * 
 * @author Philip Helger
 * @param <IMPLTYPE>
 *        Implementation type
 */
public interface IHCTableForm <IMPLTYPE extends IHCTableForm <IMPLTYPE>> extends IHCBaseTable <IMPLTYPE>
{
  @Nonnull
  IMPLTYPE setFocusHandlingEnabled (boolean bFocusHandlingEnabled);

  boolean isFocusHandlingEnabled ();

  @Nonnull
  HCTableFormItemRow createItemRow ();

  @Nonnull
  @Deprecated
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable String sValue);

  @Nonnull
  @Deprecated
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNodeBuilder aCtrlBuilder);

  @Nonnull
  @Deprecated
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNode aCtrl);

  @Nonnull
  @Deprecated
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNode... aCtrls);

  @Nonnull
  @Deprecated
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNodeBuilder aCtrlBuilder, @Nullable IErrorList aFormErrors);

  @Nonnull
  @Deprecated
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNode aCtrl, @Nullable IErrorList aFormErrors);

  @Nonnull
  @Deprecated
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable Iterable <? extends IHCNode> aCtrls);

  @Nonnull
  @Deprecated
  HCRow addItemRow (@Nullable IFormLabel aLabel,
                    @Nullable Iterable <? extends IHCNode> aCtrls,
                    @Nullable IErrorList aFormErrors);

  @Nonnull
  @Deprecated
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel, @Nullable String sText, @Nullable IFormNote aNote);

  @Nonnull
  @Deprecated
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable String sText,
                            @Nullable IFormNote aNote,
                            @Nullable IErrorList aFormErrors);

  @Nonnull
  @Deprecated
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable IHCNodeBuilder aCtrlBuilder,
                            @Nullable IFormNote aNote);

  @Nonnull
  @Deprecated
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel, @Nullable IHCNode aCtrl, @Nullable IFormNote aNote);

  @Nonnull
  @Deprecated
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable Iterable <? extends IHCNode> aCtrls,
                            @Nullable IFormNote aNote);

  @Nonnull
  @Deprecated
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable IHCNodeBuilder aCtrlBuilder,
                            @Nullable IFormNote aNote,
                            @Nullable IErrorList aFormErrors);

  @Nonnull
  @Deprecated
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable IHCNode aCtrl,
                            @Nullable IFormNote aNote,
                            @Nullable IErrorList aFormErrors);

  @Nonnull
  @Deprecated
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable Iterable <? extends IHCNode> aCtrls,
                            @Nullable IFormNote aNote,
                            @Nullable IErrorList aFormErrors);
}
