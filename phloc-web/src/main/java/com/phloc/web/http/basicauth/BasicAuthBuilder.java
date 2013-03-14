package com.phloc.web.http.basicauth;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.web.http.HTTPStringHelper;

public final class BasicAuthBuilder
{
  private String m_sRealm;

  public BasicAuthBuilder ()
  {}

  /**
   * Set the realm to be used.
   * 
   * @param sRealm
   *        The realm to be used. May not be <code>null</code> and should not be
   *        empty.
   * @return this
   */
  @Nonnull
  public BasicAuthBuilder setRealm (@Nonnull final String sRealm)
  {
    if (!HTTPStringHelper.isQuotedTextContent (sRealm))
      throw new IllegalArgumentException ("realm is invalid: " + sRealm);

    m_sRealm = HTTPStringHelper.QUOTEDTEXT_START + sRealm + HTTPStringHelper.QUOTEDTEXT_END;
    return this;
  }

  public boolean isValid ()
  {
    return m_sRealm != null;
  }

  @Nonnull
  @Nonempty
  public String build ()
  {
    if (!isValid ())
      throw new IllegalStateException ("Built basic auth is not valid!");
    final StringBuilder ret = new StringBuilder (HTTPBasicAuth.HEADER_VALUE_PREFIX_BASIC);
    if (m_sRealm != null)
      ret.append (" realm=").append (m_sRealm);
    return ret.toString ();
  }
}
