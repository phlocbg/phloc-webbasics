package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpSession;

import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;

final class SessionScope extends AbstractScope implements ISessionScope {
  private final HttpSession m_aHttpSession;

  public SessionScope (@Nonnull final HttpSession aHttpSession) {
    m_aHttpSession = aHttpSession;
  }

  @Nonnull
  public HttpSession getSession () {
    return m_aHttpSession;
  }

  @Nullable
  public Object getAttributeObject (@Nullable final String sName) {
    return m_aHttpSession.getAttribute (sName);
  }

  public void setAttribute (@Nonnull final String sName, @Nullable final Object aValue) {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aValue == null)
      m_aHttpSession.removeAttribute (sName);
    else
      m_aHttpSession.setAttribute (sName, aValue);
  }

  @Nonnull
  public EChange removeAttribute (@Nonnull final String sName) {
    if (getAttributeObject (sName) == null)
      return EChange.UNCHANGED;
    m_aHttpSession.removeAttribute (sName);
    return EChange.CHANGED;
  }

  public void destroyScope () {}
}
