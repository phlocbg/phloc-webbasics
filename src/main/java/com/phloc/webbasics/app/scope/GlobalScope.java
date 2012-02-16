package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletContext;

import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;

public final class GlobalScope extends AbstractScope implements IGlobalScope {
  private final ServletContext m_aSC;

  public GlobalScope (@Nonnull final ServletContext aSC) {
    m_aSC = aSC;
  }

  @Nonnull
  public ServletContext getServletContext () {
    return m_aSC;
  }

  @Nullable
  public Object getAttributeObject (@Nullable final String sName) {
    return m_aSC.getAttribute (sName);
  }

  public void setAttribute (@Nonnull final String sName, @Nullable final Object aValue) {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aValue == null)
      m_aSC.removeAttribute (sName);
    else
      m_aSC.setAttribute (sName, aValue);
  }

  @Nonnull
  public EChange removeAttribute (@Nonnull final String sName) {
    if (getAttributeObject (sName) == null)
      return EChange.UNCHANGED;
    m_aSC.removeAttribute (sName);
    return EChange.CHANGED;
  }

  public void destroyScope () {}
}
