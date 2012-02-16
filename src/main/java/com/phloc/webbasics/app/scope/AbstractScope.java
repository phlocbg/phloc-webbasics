package com.phloc.webbasics.app.scope;

import javax.annotation.Nullable;

import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.StringHelper;

abstract class AbstractScope implements IScope {
  @Nullable
  public final <T> T getCastedAttribute (@Nullable final String sName) {
    return GenericReflection.<Object, T> uncheckedCast (getAttributeObject (sName));
  }

  public final String getAttributeAsString (@Nullable final String sName) {
    return (String) getAttributeObject (sName);
  }

  public final int getAttributeAsInt (@Nullable final String sName, final int nDefault) {
    return StringHelper.parseInt (getAttributeObject (sName), nDefault);
  }
}
