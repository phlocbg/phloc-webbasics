package com.phloc.web.http.digestauth;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.web.http.HTTPStringHelper;

public final class DigestAuthResponseBuilder
{
  private String m_sRealm;

  public DigestAuthResponseBuilder ()
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
  public DigestAuthResponseBuilder setRealm (@Nonnull final String sRealm)
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
    final StringBuilder ret = new StringBuilder (HTTPDigestAuth.HEADER_VALUE_PREFIX_DIGEST);
    if (m_sRealm != null)
      ret.append (" realm=").append (m_sRealm);
    return ret.toString ();
  }
}
