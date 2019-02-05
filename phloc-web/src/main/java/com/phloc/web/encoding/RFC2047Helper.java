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
package com.phloc.web.encoding;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.codec.DecoderException;
import com.phloc.commons.codec.RFC1522BCodec;
import com.phloc.commons.codec.RFC1522QCodec;

/**
 * RFC 2047 Helper
 *
 * @author Apache Abdera
 */
@Immutable
public final class RFC2047Helper
{
  public static enum ECodec
  {
   B,
   Q
  }

  public static final Charset DEFAULT_CHARSET = CCharset.CHARSET_UTF_8_OBJ;

  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final RFC2047Helper s_aInstance = new RFC2047Helper ();

  private RFC2047Helper ()
  {}

  @Nullable
  public static String encode (@Nullable final String sValue)
  {
    return encode (sValue, DEFAULT_CHARSET, ECodec.B);
  }

  @Nullable
  public static String encode (@Nullable final String sValue, @Nonnull final Charset aCharset)
  {
    return encode (sValue, aCharset, ECodec.B);
  }

  /**
   * Used to encode a string as specified by RFC 2047
   *
   * @param sValue
   *        The string to encode
   * @param aCharset
   *        The character set to use for the encoding
   * @param eCodec
   *        The codec to use
   * @return The encoded value
   */
  @Nullable
  public static String encode (@Nullable final String sValue, @Nonnull final Charset aCharset, final ECodec eCodec)
  {
    if (sValue == null)
      return null;

    try
    {
      switch (eCodec)
      {
        case Q:
          return new RFC1522QCodec (aCharset).encodeText (sValue);
        case B:
        default:
          return new RFC1522BCodec (aCharset).encodeText (sValue);
      }
    }
    catch (final Exception ex)
    {
      return sValue;
    }
  }

  /**
   * Used to decode a string as specified by RFC 2047
   *
   * @param sValue
   *        The encoded string
   * @return The decoded value
   */
  @Nullable
  public static String decode (@Nullable final String sValue)
  {
    if (sValue == null)
      return null;

    try
    {
      // try BCodec first
      return new RFC1522BCodec ().decodeText (sValue);
    }
    catch (final DecoderException de)
    {
      // try QCodec next
      try
      {
        return new RFC1522QCodec ().decodeText (sValue);
      }
      catch (final Exception ex)
      {
        return sValue;
      }
    }
    catch (final Exception e)
    {
      return sValue;
    }
  }
}
