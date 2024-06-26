/**
 * Copyright (C) 2006-2015 phloc systems
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
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.http.CHTTPHeader;
import com.phloc.web.http.digestauth.HTTPDigestAuth;

/**
 * Handling for HTTP Basic Authentication
 * 
 * @author Boris Gregorcic
 */
@Immutable
public final class HTTPBasicAuth
{
  public static final String HEADER_VALUE_PREFIX_BASIC = "Basic"; //$NON-NLS-1$
  static final char USERNAME_PASSWORD_SEPARATOR = ':';
  private static final Charset DEFAULT_CHARSET = CCharset.CHARSET_ISO_8859_1_OBJ;
  private static Charset s_aCustomCharset = null;

  private static final Logger LOG = LoggerFactory.getLogger (HTTPDigestAuth.class);

  private HTTPBasicAuth ()
  {
    // private
  }

  @Nonnull
  protected static Charset getCharset ()
  {
    return s_aCustomCharset == null ? DEFAULT_CHARSET : s_aCustomCharset;
  }

  /**
   * Sets the passed character set as the one to be used for encoding basic
   * authentication credentials (default: ISO-8859-1)
   * 
   * @param aCustomCharset
   *        The character set to use, pass <code>null</code> to restore the
   *        default
   */
  public static void setCustomCharset (@Nullable final Charset aCustomCharset)
  {
    LOG.info ("Setting BASIC AUTH charset to {}", aCustomCharset == null ? DEFAULT_CHARSET : aCustomCharset); //$NON-NLS-1$
    s_aCustomCharset = aCustomCharset;
  }

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
  public static BasicAuthClientCredentials getBasicAuthClientCredentials (@Nonnull final HttpServletRequest aHttpRequest)
  {
    ValueEnforcer.notNull (aHttpRequest, "HttpRequest"); //$NON-NLS-1$

    final String sHeaderValue = aHttpRequest.getHeader (CHTTPHeader.AUTHORIZATION);
    return getBasicAuthClientCredentials (sHeaderValue);
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
  public static BasicAuthClientCredentials getBasicAuthClientCredentials (@Nullable final String sAuthHeader)
  {
    final String sRealHeader = StringHelper.trim (sAuthHeader);
    if (StringHelper.hasNoText (sRealHeader))
      return null;

    final String [] aElements = RegExHelper.getSplitToArray (sRealHeader, "\\s+", 2); //$NON-NLS-1$
    if (aElements.length != 2)
    {
      LOG.error ("String is not Basic Auth"); //$NON-NLS-1$
      return null;
    }

    if (!EqualsUtils.nullSafeEqualsIgnoreCase (aElements[0], HEADER_VALUE_PREFIX_BASIC))
    {
      LOG.error ("String does not start with 'Basic'"); //$NON-NLS-1$
      return null;
    }

    // Apply Base64 decoding
    final String sEncodedCredentials = aElements[1];
    final String sUsernamePassword = Base64Helper.safeDecodeAsString (sEncodedCredentials, getCharset ());
    if (sUsernamePassword == null)
    {
      LOG.error ("Illegal Base64 encoded value '{}'", sEncodedCredentials); //$NON-NLS-1$
      return null;
    }

    // Do we have a username/password separator?
    final int nIndex = sUsernamePassword.indexOf (USERNAME_PASSWORD_SEPARATOR);
    try
    {
      if (nIndex >= 0)
      {
        return new BasicAuthClientCredentials (sUsernamePassword.substring (0, nIndex),
                                               sUsernamePassword.substring (nIndex + 1));
      }
      return new BasicAuthClientCredentials (sUsernamePassword);
    }
    catch (NullPointerException | IllegalArgumentException aEx)
    {
      LOG.error ("Error reading basic authentication header", aEx); //$NON-NLS-1$
      return null;
    }
  }
}
