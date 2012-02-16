package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;

public final class RequestScope extends AbstractScope implements IRequestScope {
  private final HttpServletRequest m_aHttpRequest;
  private final HttpServletResponse m_aHttpResponse;

  public RequestScope (@Nonnull final HttpServletRequest aHttpRequest, @Nonnull final HttpServletResponse aHttpResponse) {
    m_aHttpRequest = aHttpRequest;
    m_aHttpResponse = aHttpResponse;
  }

  @Nonnull
  public HttpServletRequest getRequest () {
    return m_aHttpRequest;
  }

  @Nonnull
  public HttpServletResponse getResponse () {
    return m_aHttpResponse;
  }

  public void destroyScope () {}

  @Nullable
  public Object getAttributeObject (@Nullable final String sName) {
    return m_aHttpRequest.getAttribute (sName);
  }

  public void setAttribute (@Nonnull final String sName, @Nullable final Object aValue) {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aValue == null)
      m_aHttpRequest.removeAttribute (sName);
    else
      m_aHttpRequest.setAttribute (sName, aValue);
  }

  @Nonnull
  public EChange removeAttribute (@Nonnull final String sName) {
    if (getAttributeObject (sName) == null)
      return EChange.UNCHANGED;
    m_aHttpRequest.removeAttribute (sName);
    return EChange.CHANGED;
  }

  @Nullable
  public String getUserAgent () {
    // Use non-standard headers first
    String sUserAgent = m_aHttpRequest.getHeader ("UA");
    if (sUserAgent == null) {
      sUserAgent = m_aHttpRequest.getHeader ("x-device-user-agent");
      if (sUserAgent == null)
        sUserAgent = m_aHttpRequest.getHeader ("User-Agent");
    }
    return sUserAgent;
  }

  @Override
  public String getFullContextPath () {
    final String sScheme = m_aHttpRequest.getScheme ();
    final StringBuilder aSB = new StringBuilder ();
    aSB.append (sScheme).append ("://").append (m_aHttpRequest.getServerName ());
    final int nPort = m_aHttpRequest.getServerPort ();
    if (("http".equals (sScheme) && nPort != 80) || ("https".equals (sScheme) && nPort != 443))
      aSB.append (':').append (nPort);
    return aSB.append (m_aHttpRequest.getContextPath ()).toString ();
  }
}
