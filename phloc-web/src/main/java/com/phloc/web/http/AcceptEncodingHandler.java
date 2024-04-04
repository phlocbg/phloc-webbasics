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
package com.phloc.web.http;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;

/**
 * Handler for the request HTTP header field "Accept-Encoding"
 * 
 * @author Philip Helger
 */
@Immutable
public final class AcceptEncodingHandler
{
  /** Any encoding */
  public static final String ANY_ENCODING = "*";
  // Standard encodings - must all be lowercase!
  /** Standard encoding "identity" */
  public static final String IDENTITY_ENCODING = "identity";
  /** Standard encoding "gzip" */
  public static final String GZIP_ENCODING = "gzip";
  /** Standard encoding "x-gzip" */
  public static final String X_GZIP_ENCODING = "x-gzip";
  /** Standard encoding "deflate" */
  public static final String DEFLATE_ENCODING = "deflate";
  /** Standard encoding "compress" */
  public static final String COMPRESS_ENCODING = "compress";
  /** Standard encoding "x-compress" */
  public static final String X_COMPRESS_ENCODING = "x-compress";

  private static final Logger s_aLogger = LoggerFactory.getLogger (AcceptEncodingHandler.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final AcceptEncodingHandler s_aInstance = new AcceptEncodingHandler ();

  private AcceptEncodingHandler ()
  {}

  @Nonnull
  public static AcceptEncodingList getAcceptEncodings (@Nullable final String sAcceptEncoding)
  {
    final AcceptEncodingList ret = new AcceptEncodingList ();
    if (StringHelper.hasNoText (sAcceptEncoding))
    {
      // No definition - Identity encoding only
      ret.addEncoding (IDENTITY_ENCODING, QValue.MAX_QUALITY);
    }
    else
    {
      // Charsets are separated by "," or ", "
      for (final String sItem : StringHelper.getExploded (',', sAcceptEncoding))
      {
        // Qualities are separated by ";"
        final String [] aParts = StringHelper.getExplodedArray (';', sItem.trim (), 2);
        final String sEncoding = aParts[0];
        if (StringHelper.hasNoText (sEncoding))
        {
          s_aLogger.warn ("Accept-Encoding item '" + sItem + "' has no encoding!");
          continue;
        }

        // Default quality is 1
        double dQuality = QValue.MAX_QUALITY;
        if (aParts.length == 2)
        {
          final String sQuality = aParts[1].trim ();
          if (sQuality.startsWith ("q="))
            dQuality = StringParser.parseDouble (sQuality.substring (2), QValue.MAX_QUALITY);
        }
        ret.addEncoding (sEncoding, dQuality);
      }
    }
    return ret;
  }

  @Nonnull
  public static AcceptEncodingList getAcceptEncodings (@Nonnull final HttpServletRequest aHttpRequest)
  {
    // Check if a value is cached in the HTTP request
    AcceptEncodingList aValue = (AcceptEncodingList) aHttpRequest.getAttribute (AcceptEncodingList.class.getName ());
    if (aValue == null)
    {
      final String sAcceptEncoding = aHttpRequest.getHeader (CHTTPHeader.ACCEPT_ENCODING);
      aValue = getAcceptEncodings (sAcceptEncoding);
      aHttpRequest.setAttribute (AcceptEncodingList.class.getName (), aValue);
    }
    return aValue;
  }
}
