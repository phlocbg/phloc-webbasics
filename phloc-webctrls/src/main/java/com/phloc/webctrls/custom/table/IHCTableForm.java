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

public interface IHCTableForm <IMPLTYPE extends IHCTableForm <IMPLTYPE>> extends IHCBaseTable <IMPLTYPE>
{
  @Nonnull
  IMPLTYPE setFocusHandlingEnabled (boolean bFocusHandlingEnabled);

  boolean isFocusHandlingEnabled ();

  @Nonnull
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable String sValue);

  @Nonnull
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNodeBuilder aCtrlBuilder);

  @Nonnull
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNode aCtrl);

  @Nonnull
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNodeBuilder aCtrlBuilder, @Nullable IErrorList aFormErrors);

  @Nonnull
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNode aCtrl, @Nullable IErrorList aFormErrors);

  @Nonnull
  HCRow addItemRow (@Nullable IFormLabel aLabel, @Nullable Iterable <? extends IHCNode> aCtrls);

  @Nonnull
  HCRow addItemRow (@Nullable IFormLabel aLabel,
                    @Nullable Iterable <? extends IHCNode> aCtrls,
                    @Nullable IErrorList aFormErrors);

  @Nonnull
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel, @Nullable String sText, @Nullable IFormNote aNote);

  @Nonnull
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable String sText,
                            @Nullable IFormNote aNote,
                            @Nullable IErrorList aFormErrors);

  @Nonnull
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable IHCNodeBuilder aCtrlBuilder,
                            @Nullable IFormNote aNote);

  @Nonnull
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel, @Nullable IHCNode aCtrl, @Nullable IFormNote aNote);

  @Nonnull
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable Iterable <? extends IHCNode> aCtrls,
                            @Nullable IFormNote aNote);

  @Nonnull
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable IHCNodeBuilder aCtrlBuilder,
                            @Nullable IFormNote aNote,
                            @Nullable IErrorList aFormErrors);

  @Nonnull
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable IHCNode aCtrl,
                            @Nullable IFormNote aNote,
                            @Nullable IErrorList aFormErrors);

  @Nonnull
  HCRow addItemRowWithNote (@Nullable IFormLabel aLabel,
                            @Nullable Iterable <? extends IHCNode> aCtrls,
                            @Nullable IFormNote aNote,
                            @Nullable IErrorList aFormErrors);
}
