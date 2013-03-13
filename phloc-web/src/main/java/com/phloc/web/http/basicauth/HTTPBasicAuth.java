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

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.http.CHTTPHeader;
import com.phloc.web.http.HTTPStringHelper;

/**
 * Handling for HTTP Basic Authentication
 * 
 * @author philip
 */
@Immutable
public final class HTTPBasicAuth
{
  public static final String HEADER_VALUE_PREFIX_BASIC = "Basic";
  private static final char USERNAME_PASSWORD_SEPARATOR = ':';
  private static final Charset CHARSET = CCharset.CHARSET_ISO_8859_1_OBJ;

  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final HTTPBasicAuth s_aInstance = new HTTPBasicAuth ();

  private HTTPBasicAuth ()
  {}

  /**
   * Create the request HTTP header value for use with the
   * {@link CHTTPHeader#AUTHORIZATION} header name.
   * 
   * @param aCredentials
   *        The credentials to use. May not be <code>null</code>.
   * @return The HTTP header value to use. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public static String getRequestHeaderValue (@Nonnull final BasicAuthCredentials aCredentials)
  {
    if (aCredentials == null)
      throw new NullPointerException ("credentials");

    final String sCombined = StringHelper.getConcatenatedOnDemand (aCredentials.getUserName (),
                                                                   USERNAME_PASSWORD_SEPARATOR,
                                                                   aCredentials.getPassword ());
    return HEADER_VALUE_PREFIX_BASIC + " " + Base64Helper.safeEncode (sCombined, CHARSET);
  }

  /**
   * Get the Basic authentication credentials from the passed HTTP header value.
   * 
   * @param sAuthHeader
   *        The HTTP header value to be interpreted. May be <code>null</code>.
   * @return <code>null</code> if the passed value is not a correct HTTP Basic
   *         Authentication header value.
   */
  @Nullable
  public static BasicAuthCredentials getBasicAuthCredentials (@Nullable final String sAuthHeader)
  {
    final String sRealHeader = StringHelper.trim (sAuthHeader);
    if (StringHelper.hasNoText (sRealHeader))
      return null;

    final String [] aElements = RegExHelper.getSplitToArray (sRealHeader, "\\s+", 2);
    if (aElements.length != 2)
      return null;

    if (!aElements[0].equals (HEADER_VALUE_PREFIX_BASIC))
      return null;

    // Remove the auth prefix
    final String sEncodedCredentials = aElements[1];

    // Apply Base64 decoding
    final String sUsernamePassword = Base64Helper.safeDecodeAsString (sEncodedCredentials, CHARSET);
    if (sUsernamePassword == null)
    {
      // Illegal base64 encoded value
      return null;
    }

    // Do we have a username/password separator?
    final int nIndex = sUsernamePassword.indexOf (USERNAME_PASSWORD_SEPARATOR);
    if (nIndex >= 0)
      return new BasicAuthCredentials (sUsernamePassword.substring (0, nIndex),
                                       sUsernamePassword.substring (nIndex + 1));
    return new BasicAuthCredentials (sUsernamePassword);
  }

  /**
   * Create the HTTP response header value to be used with the
   * {@link CHTTPHeader#WWW_AUTHENTICATE} header.
   * 
   * @param sRealm
   *        The realm to be used. May neither be <code>null</code> nor empty.
   * @return The HTTP header value to be used. Neither <code>null</code> nor
   *         empty.
   */
  @Nonnull
  @Nonempty
  public static String createWWWAuthenticate (@Nonnull @Nonempty final String sRealm)
  {
    if (!HTTPStringHelper.isQuotedTextContent (sRealm))
      throw new IllegalArgumentException ("realm is invalid: " + sRealm);

    return HEADER_VALUE_PREFIX_BASIC +
           " realm=" +
           HTTPStringHelper.QUOTEDTEXT_START +
           sRealm +
           HTTPStringHelper.QUOTEDTEXT_END;
  }
}
