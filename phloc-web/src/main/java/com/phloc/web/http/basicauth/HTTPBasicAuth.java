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
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.http.CHTTPHeader;
import com.phloc.web.http.digestauth.HTTPDigestAuth;

/**
 * Handling for HTTP Basic Authentication
 * 
 * @author philip
 */
@Immutable
public final class HTTPBasicAuth
{
  public static final String HEADER_VALUE_PREFIX_BASIC = "Basic";
  static final char USERNAME_PASSWORD_SEPARATOR = ':';
  static final Charset CHARSET = CCharset.CHARSET_ISO_8859_1_OBJ;
  private static final Logger s_aLogger = LoggerFactory.getLogger (HTTPDigestAuth.class);

  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final HTTPBasicAuth s_aInstance = new HTTPBasicAuth ();

  private HTTPBasicAuth ()
  {}

  /**
   * Get the Basic authentication credentials from the passed HTTP servlet
   * request from the HTTP header {@link CHTTPHeader#AUTHORIZATION}.
   * 
   * @param aHttpRequest
   *        The HTTP request to be interpreted. May be <code>null</code>.
   * @return <code>null</code> if the passed request does not contain a valid
   *         HTTP Basic Authentication header value.
   */
  @Nullable
  public static BasicAuthClientCredentials getBasicAuthCredentials (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");

    final String sHeaderValue = aHttpRequest.getHeader (CHTTPHeader.AUTHORIZATION);
    return getBasicAuthCredentials (sHeaderValue);
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
  public static BasicAuthClientCredentials getBasicAuthCredentials (@Nullable final String sAuthHeader)
  {
    final String sRealHeader = StringHelper.trim (sAuthHeader);
    if (StringHelper.hasNoText (sRealHeader))
      return null;

    final String [] aElements = RegExHelper.getSplitToArray (sRealHeader, "\\s+", 2);
    if (aElements.length != 2)
    {
      s_aLogger.error ("String is not Basic Auth");
      return null;
    }

    if (!aElements[0].equals (HEADER_VALUE_PREFIX_BASIC))
    {
      s_aLogger.error ("String does not start with 'Basic'");
      return null;
    }

    // Apply Base64 decoding
    final String sEncodedCredentials = aElements[1];
    final String sUsernamePassword = Base64Helper.safeDecodeAsString (sEncodedCredentials, CHARSET);
    if (sUsernamePassword == null)
    {
      s_aLogger.error ("Illegal Base64 encoded value '" + sEncodedCredentials + "'");
      return null;
    }

    // Do we have a username/password separator?
    final int nIndex = sUsernamePassword.indexOf (USERNAME_PASSWORD_SEPARATOR);
    if (nIndex >= 0)
      return new BasicAuthClientCredentials (sUsernamePassword.substring (0, nIndex),
                                       sUsernamePassword.substring (nIndex + 1));
    return new BasicAuthClientCredentials (sUsernamePassword);
  }

  /**
   * Create the HTTP response header value to be used with the
   * {@link CHTTPHeader#WWW_AUTHENTICATE} header.
   * 
   * @param sRealm
   *        The realm to be used. May not be <code>null</code> and should not be
   *        empty.
   * @return The HTTP header value to be used. Neither <code>null</code> nor
   *         empty.
   */
  @Nonnull
  @Nonempty
  public static String createBasicAuthResponse (@Nonnull final String sRealm)
  {
    return new BasicAuthServerBuilder ().setRealm (sRealm).build ();
  }
}
