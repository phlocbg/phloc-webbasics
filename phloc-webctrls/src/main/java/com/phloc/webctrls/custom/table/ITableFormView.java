package com.phloc.webctrls.custom.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCBaseTable;
import com.phloc.html.hc.IHCNode;
import com.phloc.webctrls.custom.IFormLabel;

public interface ITableFormView <THISTYPE extends ITableFormView <THISTYPE>> extends IHCBaseTable <THISTYPE>
{
  void addItemRow (@Nullable IFormLabel aLabel, @Nullable String sValue);

  void addItemRow (@Nullable IFormLabel aLabel, @Nullable String... aValues);

  void addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNode aValue);

  void addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNode... aValues);

  void addItemRow (@Nullable IFormLabel aLabel, @Nullable Iterable <? extends IHCNode> aValues);

  void addItemRow (@Nonnull String sLabel, @Nullable String sValue);

  void addItemRow (@Nonnull String sLabel, @Nullable String... aValue);

  void addItemRow (@Nonnull String sLabel, @Nullable IHCNode aValue);

  void addItemRow (@Nonnull String sLabel, @Nullable IHCNode... aValues);

  void addItemRow (@Nonnull String sLabel, @Nullable Iterable <? extends IHCNode> aValues);
}
