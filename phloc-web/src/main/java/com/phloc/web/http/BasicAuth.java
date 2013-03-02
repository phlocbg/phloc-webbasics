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
package com.phloc.web.http;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.string.StringHelper;

/**
 * Basic handling for HTTP Basic Auth
 * 
 * @author philip
 */
@Immutable
public final class BasicAuth
{
  public static final String HEADER_VALUE_PREFIX_BASIC = "Basic ";

  @PresentForCodeCoverage
  private static final BasicAuth s_aInstance = new BasicAuth ();

  private BasicAuth ()
  {}

  /**
   * Create the request HTTP header value for use with the
   * {@link CHTTPHeader#AUTHORIZATION} header name.
   * 
   * @param sUsername
   *        The username to use. May neither be <code>null</code> nor empty.
   * @param sPassword
   *        The password to use. May be <code>null</code> or empty.
   * @return The HTTP header value to use. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public static String getRequestHeaderValue (@Nonnull final String sUsername, @Nullable final String sPassword)
  {
    if (StringHelper.hasNoText (sUsername))
      throw new IllegalArgumentException ("username is missing");

    final String sCombined = StringHelper.getConcatenatedOnDemand (sUsername, ":", sPassword);
    return HEADER_VALUE_PREFIX_BASIC + Base64Helper.safeEncode (sCombined, CCharset.CHARSET_ISO_8859_1_OBJ);
  }

  /**
   * Get user name and password from the passed HTTP header value.
   * 
   * @param sAuthHeader
   *        The HTTP header value to be interpreted. May be <code>null</code>.
   * @return <code>null</code> if the passed value is not a correct HTTP Basic
   *         Auth header value. A non-<code>null</code> array with 1 item, if
   *         only a password is present, an array with 2 items if username and
   *         password are present.
   */
  @Nullable
  public static String [] getBasicAuthValues (@Nullable final String sAuthHeader)
  {
    if (!StringHelper.startsWith (sAuthHeader, HEADER_VALUE_PREFIX_BASIC))
      return null;

    // Remove the auth prefix
    final String sEncodedCredentials = sAuthHeader.substring (HEADER_VALUE_PREFIX_BASIC.length ());

    // Apply Base64 decoding
    final String sUsernamePassword = Base64Helper.safeDecodeAsString (sEncodedCredentials,
                                                                      CCharset.CHARSET_ISO_8859_1_OBJ);
    if (sUsernamePassword == null)
    {
      // Illegal base64 encoded value
      return null;
    }

    // Do we have a username/password separator?
    final int nIndex = sUsernamePassword.indexOf (':');
    if (nIndex >= 0)
      return new String [] { sUsernamePassword.substring (0, nIndex), sUsernamePassword.substring (nIndex + 1) };
    return new String [] { sUsernamePassword };
  }
}
