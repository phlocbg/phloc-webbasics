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

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.http.CHTTPHeader;
import com.phloc.web.http.HTTPStringHelper;

/**
 * Handling for HTTP Digest Authentication
 * 
 * @author philip
 */
@Immutable
public final class HTTPDigestAuth
{
  public static final String HEADER_VALUE_PREFIX_DIGEST = "Digest";

  public static final String ALGORITHM_MD5 = "MD5";
  public static final String ALGORITHM_MD5_SESS = "MD5-sess";
  public static final String DEFAULT_ALGORITHM = ALGORITHM_MD5;

  public static final String QOP_AUTH = "auth";
  public static final String QOP_AUTH_INT = "auth-int";
  public static final String DEFAULT_QOP = QOP_AUTH;

  private static final Logger s_aLogger = LoggerFactory.getLogger (HTTPDigestAuth.class);
  private static final char SEPARATOR = ':';
  private static final Charset CHARSET = CCharset.CHARSET_ISO_8859_1_OBJ;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final HTTPDigestAuth s_aInstance = new HTTPDigestAuth ();

  private HTTPDigestAuth ()
  {}

  /**
   * Get the Digest authentication credentials from the passed HTTP servlet
   * request from the HTTP header {@link CHTTPHeader#AUTHORIZATION}.
   * 
   * @param aHttpRequest
   *        The HTTP request to be interpreted. May be <code>null</code>.
   * @return <code>null</code> if the passed request does not contain a valid
   *         HTTP Digest Authentication header value.
   */
  @Nullable
  public static DigestAuthCredentials getDigestAuthCredentials (@Nonnull final HttpServletRequest aHttpRequest)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("httpRequest");

    final String sHeaderValue = aHttpRequest.getHeader (CHTTPHeader.AUTHORIZATION);
    return getDigestAuthCredentials (sHeaderValue);
  }

  /**
   * Get the parameters of a Digest authentication string. It may be used for
   * both client and server handling.
   * 
   * @param sAuthHeader
   *        The HTTP header value to be interpreted. May be <code>null</code>.
   * @return <code>null</code> if the passed value cannot be parsed as a HTTP
   *         Digest Authentication value, a {@link LinkedHashMap} with all
   *         parameter name-value pairs in the order they are contained.
   */
  @Nullable
  public static Map <String, String> getDigestAuthParams (@Nullable final String sAuthHeader)
  {
    final String sRealHeader = StringHelper.trim (sAuthHeader);
    if (StringHelper.hasNoText (sRealHeader))
      return null;

    if (!sRealHeader.startsWith (HEADER_VALUE_PREFIX_DIGEST))
    {
      s_aLogger.error ("String does not start with 'Digest'");
      return null;
    }

    final char [] aChars = sRealHeader.toCharArray ();
    int nIndex = HEADER_VALUE_PREFIX_DIGEST.length ();

    if (nIndex >= aChars.length || !HTTPStringHelper.isLinearWhitespaceChar (aChars[nIndex]))
    {
      s_aLogger.error ("No whitespace after 'Digest'");
      return null;
    }
    nIndex++;

    final Map <String, String> aParams = new LinkedHashMap <String, String> ();
    while (true)
    {
      // Skip all spaces
      while (nIndex < aChars.length && HTTPStringHelper.isLinearWhitespaceChar (aChars[nIndex]))
        nIndex++;

      // Find token name
      int nStartIndex = nIndex;
      while (nIndex < aChars.length && HTTPStringHelper.isTokenChar (aChars[nIndex]))
        nIndex++;
      if (nStartIndex == nIndex)
      {
        s_aLogger.error ("No token and no whitespace found for auth-param name: '" + aChars[nIndex] + "'");
        return null;
      }
      final String sToken = sRealHeader.substring (nStartIndex, nIndex);

      // Skip all spaces
      while (nIndex < aChars.length && HTTPStringHelper.isLinearWhitespaceChar (aChars[nIndex]))
        nIndex++;

      if (nIndex >= aChars.length || aChars[nIndex] != '=')
      {
        s_aLogger.error ("No separator char '=' found after '" + sToken + "'");
        return null;
      }
      nIndex++;

      // Skip all spaces
      while (nIndex < aChars.length && HTTPStringHelper.isLinearWhitespaceChar (aChars[nIndex]))
        nIndex++;

      if (nIndex >= aChars.length)
      {
        s_aLogger.error ("Found nothing after '=' of '" + sToken + "'");
        return null;
      }

      String sValue;
      if (aChars[nIndex] == HTTPStringHelper.QUOTEDTEXT_BEGIN)
      {
        // Quoted string
        ++nIndex;
        nStartIndex = nIndex;
        while (nIndex < aChars.length && HTTPStringHelper.isQuotedTextChar (aChars[nIndex]))
          nIndex++;
        if (nIndex >= aChars.length)
        {
          s_aLogger.error ("Unexpected EOF in quoted text for '" + sToken + "'");
          return null;
        }
        if (aChars[nIndex] != HTTPStringHelper.QUOTEDTEXT_END)
        {
          s_aLogger.error ("Quoted string of token '" +
                           sToken +
                           "' is not terminated correctly: '" +
                           aChars[nIndex] +
                           "'");
          return null;
        }
        sValue = sRealHeader.substring (nStartIndex, nIndex);

        // Skip termination char
        nIndex++;
      }
      else
      {
        // Token
        nStartIndex = nIndex;
        while (nIndex < aChars.length && HTTPStringHelper.isTokenChar (aChars[nIndex]))
          nIndex++;
        if (nStartIndex == nIndex)
        {
          s_aLogger.error ("No token and no whitespace found for auth-param value of '" +
                           sToken +
                           "': '" +
                           aChars[nIndex] +
                           "'");
          return null;
        }
        sValue = sRealHeader.substring (nStartIndex, nIndex);
      }

      // Remember key/value pair
      aParams.put (sToken, sValue);

      // Skip all spaces
      while (nIndex < aChars.length && HTTPStringHelper.isLinearWhitespaceChar (aChars[nIndex]))
        nIndex++;

      // Check if there are any additional parameters
      if (nIndex >= aChars.length)
      {
        // No more tokens - we're done
        break;
      }

      // If there is a comma, another parameter is expected
      if (aChars[nIndex] != ',')
      {
        s_aLogger.error ("Illegal character after auth-param '" + sToken + "': '" + aChars[nIndex] + "'");
        return null;
      }
      ++nIndex;

      if (nIndex >= aChars.length)
      {
        s_aLogger.error ("Found nothing after continuation of auth-param '" + sToken + "'");
        return null;
      }
    }

    return aParams;
  }

  /**
   * Get the Digest authentication credentials from the passed HTTP header
   * value.
   * 
   * @param sAuthHeader
   *        The HTTP header value to be interpreted. May be <code>null</code>.
   * @return <code>null</code> if the passed value is not a correct HTTP Digest
   *         Authentication header value.
   */
  @Nullable
  public static DigestAuthCredentials getDigestAuthCredentials (@Nullable final String sAuthHeader)
  {
    final Map <String, String> aParams = getDigestAuthParams (sAuthHeader);
    if (aParams == null)
      return null;

    final String sUserName = aParams.remove ("username");
    if (sUserName == null)
    {
      s_aLogger.error ("Digest Auth does not container 'username'");
      return null;
    }
    final String sRealm = aParams.remove ("realm");
    if (sRealm == null)
    {
      s_aLogger.error ("Digest Auth does not container 'realm'");
      return null;
    }
    final String sNonce = aParams.remove ("nonce");
    if (sNonce == null)
    {
      s_aLogger.error ("Digest Auth does not container 'nonce'");
      return null;
    }
    final String sDigestURI = aParams.remove ("uri");
    if (sDigestURI == null)
    {
      s_aLogger.error ("Digest Auth does not container 'uri'");
      return null;
    }
    final String sResponse = aParams.remove ("response");
    if (sResponse == null)
    {
      s_aLogger.error ("Digest Auth does not container 'response'");
      return null;
    }
    final String sAlgorithm = aParams.remove ("algorithm");
    final String sCNonce = aParams.remove ("cnonce");
    final String sOpaque = aParams.remove ("opaque");
    final String sMessageQOP = aParams.remove ("qop");
    final String sNonceCount = aParams.remove ("nc");
    if (!aParams.isEmpty ())
      s_aLogger.warn ("Digest Auth contains unhandled parameters: " + aParams.toString ());

    return new DigestAuthCredentials (sUserName,
                                      sRealm,
                                      sNonce,
                                      sDigestURI,
                                      sResponse,
                                      sAlgorithm,
                                      sCNonce,
                                      sOpaque,
                                      sMessageQOP,
                                      sNonceCount);
  }
}
