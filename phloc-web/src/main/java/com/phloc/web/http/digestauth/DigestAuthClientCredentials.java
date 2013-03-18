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
package com.phloc.web.http.digestauth;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.http.HTTPStringHelper;

/**
 * Credentials for HTTP digest authentication
 * 
 * @author philip
 */
@Immutable
public final class DigestAuthClientCredentials
{
  private final String m_sUserName;
  private final String m_sRealm;
  private final String m_sServerNonce;
  private final String m_sDigestURI;
  private final String m_sResponse;
  private final String m_sAlgorithm;
  private final String m_sClientNonce;
  private final String m_sOpaque;
  private final String m_sMessageQOP;
  private final int m_nNonceCount;

  public DigestAuthClientCredentials (@Nonnull @Nonempty final String sUserName,
                                      @Nonnull @Nonempty final String sRealm,
                                      @Nonnull @Nonempty final String sServerNonce,
                                      @Nonnull @Nonempty final String sDigestURI,
                                      @Nonnull @Nonempty final String sResponse,
                                      @Nullable final String sAlgorithm,
                                      @Nullable final String sClientNonce,
                                      @Nullable final String sOpaque,
                                      @Nullable final String sMessageQOP,
                                      @Nullable final String sNonceCount)
  {
    if (StringHelper.hasNoText (sUserName))
      throw new IllegalArgumentException ("UserName");
    if (StringHelper.hasNoText (sRealm))
      throw new IllegalArgumentException ("Realm");
    if (StringHelper.hasNoText (sServerNonce))
      throw new IllegalArgumentException ("Nonce");
    if (StringHelper.hasNoText (sUserName))
      throw new IllegalArgumentException ("DigestURI");
    if (StringHelper.hasNoText (sResponse))
      throw new IllegalArgumentException ("Response");
    if (sResponse.length () != 32)
      throw new IllegalArgumentException ("The 'response' value must be a 32-byte hex string!");
    if (!HTTPStringHelper.isLowerHexNotEmpty (sResponse))
      throw new IllegalArgumentException ("The 'response' value must consist of all lowercase hex chars!");
    if (StringHelper.hasText (sMessageQOP) && StringHelper.hasNoText (sClientNonce))
      throw new IllegalArgumentException ("If 'qop' is present 'cnonce' must also be present!");
    if (StringHelper.hasNoText (sMessageQOP) && StringHelper.hasText (sClientNonce))
      throw new IllegalArgumentException ("If 'qop' is not present 'cnonce' must also not be present!");
    if (StringHelper.hasText (sMessageQOP) && StringHelper.hasNoText (sNonceCount))
      throw new IllegalArgumentException ("If 'qop' is present 'nc' must also be present!");
    if (StringHelper.hasNoText (sMessageQOP) && StringHelper.hasText (sNonceCount))
      throw new IllegalArgumentException ("If 'qop' is not present 'nc' must also not be present!");
    if (sNonceCount != null && sNonceCount.length () != 8)
      throw new IllegalArgumentException ("The 'nonceCount' value must be a 8-byte hex string!");
    if (sNonceCount != null && !HTTPStringHelper.isHexNotEmpty (sNonceCount))
      throw new IllegalArgumentException ("The 'nonceCount' value must consist only of hex chars!");
    m_sUserName = sUserName;
    m_sRealm = sRealm;
    m_sServerNonce = sServerNonce;
    m_sDigestURI = sDigestURI;
    m_sResponse = sResponse;
    m_sAlgorithm = sAlgorithm;
    m_sClientNonce = sClientNonce;
    m_sOpaque = sOpaque;
    m_sMessageQOP = sMessageQOP;
    m_nNonceCount = sNonceCount == null ? -1 : StringParser.parseInt (sNonceCount, 16, -1);
    if (sNonceCount != null && m_nNonceCount == -1)
      throw new IllegalArgumentException ("The 'nonce-count' parameter is invalid: '" + sNonceCount + "'");
  }

  /**
   * @return The user name. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getUserName ()
  {
    return m_sUserName;
  }

  /**
   * @return The realm. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getRealm ()
  {
    return m_sRealm;
  }

  /**
   * @return The nonce. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getServerNonce ()
  {
    return m_sServerNonce;
  }

  /**
   * @return The digest URI. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getDigestURI ()
  {
    return m_sDigestURI;
  }

  @Nullable
  public String getResponse ()
  {
    return m_sResponse;
  }

  @Nullable
  public String getAlgorithm ()
  {
    return m_sAlgorithm;
  }

  @Nullable
  public String getClientNonce ()
  {
    return m_sClientNonce;
  }

  @Nullable
  public String getOpaque ()
  {
    return m_sOpaque;
  }

  @Nullable
  public String getMessageQOP ()
  {
    return m_sMessageQOP;
  }

  @CheckForSigned
  public int getNonceCount ()
  {
    return m_nNonceCount;
  }

  @Nonnull
  @Nonempty
  public String getRequestValue ()
  {
    final StringBuilder aSB = new StringBuilder (HTTPDigestAuth.HEADER_VALUE_PREFIX_DIGEST);
    aSB.append (" username=")
       .append (HTTPStringHelper.getQuotedTextString (m_sUserName))
       .append (", realm=")
       .append (HTTPStringHelper.getQuotedTextString (m_sRealm))
       .append (", nonce=")
       .append (HTTPStringHelper.getQuotedTextString (m_sServerNonce))
       .append (", uri=")
       .append (HTTPStringHelper.getQuotedTextString (m_sDigestURI))
       .append (", response=")
       .append (HTTPStringHelper.getQuotedTextString (m_sResponse));
    if (m_sAlgorithm != null)
      aSB.append (", algorithm=").append (m_sAlgorithm);
    if (m_sClientNonce != null)
      aSB.append (", cnonce=").append (HTTPStringHelper.getQuotedTextString (m_sClientNonce));
    if (m_sOpaque != null)
      aSB.append (", opaque=").append (HTTPStringHelper.getQuotedTextString (m_sOpaque));
    if (m_sMessageQOP != null)
      aSB.append (", qop=").append (m_sMessageQOP);
    if (m_nNonceCount > 0)
      aSB.append (", nc=").append (HTTPDigestAuth.getNonceCountString (m_nNonceCount));
    return aSB.toString ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof DigestAuthClientCredentials))
      return false;
    final DigestAuthClientCredentials rhs = (DigestAuthClientCredentials) o;
    return m_sUserName.equals (rhs.m_sUserName) &&
           m_sRealm.equals (rhs.m_sRealm) &&
           m_sServerNonce.equals (rhs.m_sServerNonce) &&
           m_sDigestURI.equals (rhs.m_sDigestURI) &&
           m_sResponse.equals (rhs.m_sResponse) &&
           EqualsUtils.equals (m_sAlgorithm, rhs.m_sAlgorithm) &&
           EqualsUtils.equals (m_sClientNonce, rhs.m_sClientNonce) &&
           EqualsUtils.equals (m_sOpaque, rhs.m_sOpaque) &&
           EqualsUtils.equals (m_sMessageQOP, rhs.m_sMessageQOP) &&
           m_nNonceCount == rhs.m_nNonceCount;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sUserName)
                                       .append (m_sRealm)
                                       .append (m_sServerNonce)
                                       .append (m_sDigestURI)
                                       .append (m_sResponse)
                                       .append (m_sAlgorithm)
                                       .append (m_sClientNonce)
                                       .append (m_sOpaque)
                                       .append (m_sMessageQOP)
                                       .append (m_nNonceCount)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("userName", m_sUserName)
                                       .append ("realm", m_sRealm)
                                       .append ("serverNonce", m_sServerNonce)
                                       .append ("digestUri", m_sDigestURI)
                                       .append ("response", m_sResponse)
                                       .appendIfNotNull ("algorithm", m_sAlgorithm)
                                       .appendIfNotNull ("clientNonce", m_sClientNonce)
                                       .appendIfNotNull ("opaque", m_sOpaque)
                                       .appendIfNotNull ("messageQop", m_sMessageQOP)
                                       .append ("noncecount", m_nNonceCount)
                                       .toString ();
  }
}
