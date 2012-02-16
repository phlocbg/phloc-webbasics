package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.EChange;

public interface IScope {
  @Nullable
  Object getAttributeObject (@Nullable String sName);

  @Nullable
  <T> T getCastedAttribute (@Nullable String sName);

  @Nullable
  String getAttributeAsString (@Nullable String sName);

  int getAttributeAsInt (@Nullable String sName, int nDefault);

  void setAttribute (@Nonnull String sName, @Nullable Object aValue);

  @Nonnull
  EChange removeAttribute (@Nullable String sName);

  void destroyScope ();
}
