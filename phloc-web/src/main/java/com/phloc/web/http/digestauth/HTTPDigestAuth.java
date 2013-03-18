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
  private static final char SEPARATOR = ':';
  private static final Charset CHARSET = CCharset.CHARSET_ISO_8859_1_OBJ;
  private static final Logger s_aLogger = LoggerFactory.getLogger (HTTPDigestAuth.class);

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
    final String sRealHeader = StringHelper.trim (sAuthHeader);
    if (StringHelper.hasNoText (sRealHeader))
      return null;

    if (!sRealHeader.startsWith (HEADER_VALUE_PREFIX_DIGEST))
    {
      s_aLogger.warn ("String does not start with 'Digest'");
      return null;
    }

    final char [] aChars = sRealHeader.toCharArray ();
    int nIndex = HEADER_VALUE_PREFIX_DIGEST.length ();

    if (nIndex >= aChars.length || !HTTPStringHelper.isLinearWhitespaceChar (aChars[nIndex]))
    {
      s_aLogger.warn ("No space after 'Digest'");
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
        s_aLogger.warn ("No token and no whitespace found for auth-param name: '" + aChars[nIndex] + "'");
        return null;
      }
      final String sToken = sRealHeader.substring (nStartIndex, nIndex);

      // Skip all spaces
      while (nIndex < aChars.length && HTTPStringHelper.isLinearWhitespaceChar (aChars[nIndex]))
        nIndex++;

      if (nIndex >= aChars.length || aChars[nIndex] != '=')
      {
        s_aLogger.warn ("No separator char '=' found after '" + sToken + "'");
        return null;
      }
      nIndex++;

      // Skip all spaces
      while (nIndex < aChars.length && HTTPStringHelper.isLinearWhitespaceChar (aChars[nIndex]))
        nIndex++;

      if (nIndex >= aChars.length)
      {
        s_aLogger.warn ("Found nothing after '=' of '" + sToken + "'");
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
          s_aLogger.warn ("Unexpected EOF in quoted text for '" + sToken + "'");
          return null;
        }
        if (aChars[nIndex] != HTTPStringHelper.QUOTEDTEXT_END)
        {
          s_aLogger.warn ("Quoted string of token '" +
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
          s_aLogger.warn ("No token and no whitespace found for auth-param value of '" +
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
        s_aLogger.warn ("Illegal character after auth-param '" + sToken + "': '" + aChars[nIndex] + "'");
        return null;
      }
      ++nIndex;

      if (nIndex >= aChars.length)
      {
        s_aLogger.warn ("Found nothing after continuation of auth-param '" + sToken + "'");
        return null;
      }
    }

    for (final Map.Entry <String, String> aEntry : aParams.entrySet ())
      System.out.println (aEntry.getKey () + " == " + aEntry.getValue ());
    return null;
  }
}
