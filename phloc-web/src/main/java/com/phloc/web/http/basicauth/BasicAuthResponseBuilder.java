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
package com.phloc.web.http.basicauth;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.web.http.HTTPStringHelper;

@NotThreadSafe
public final class BasicAuthResponseBuilder
{
  private String m_sRealm;

  public BasicAuthResponseBuilder ()
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
  public BasicAuthResponseBuilder setRealm (@Nonnull final String sRealm)
  {
    if (!HTTPStringHelper.isQuotedTextContent (sRealm))
      throw new IllegalArgumentException ("realm is invalid: " + sRealm);

    m_sRealm = HTTPStringHelper.QUOTEDTEXT_BEGIN + sRealm + HTTPStringHelper.QUOTEDTEXT_END;
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
      throw new IllegalStateException ("Built Basic auth is not valid!");
    final StringBuilder ret = new StringBuilder (HTTPBasicAuth.HEADER_VALUE_PREFIX_BASIC);
    if (m_sRealm != null)
      ret.append (" realm=").append (m_sRealm);
    return ret.toString ();
  }
}
