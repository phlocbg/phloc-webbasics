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
package com.phloc.web.encoding;

import java.nio.charset.Charset;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.codec.DecoderException;

/**
 * @author Apache Abdera
 */
public class RFC2047Helper
{
  public static final Charset DEFAULT_CHARSET = CCharset.CHARSET_UTF_8_OBJ;

  public static enum Codec
  {
    B,
    Q
  }

  public static String encode (final String value)
  {
    return encode (value, DEFAULT_CHARSET, Codec.B);
  }

  public static String encode (final String value, final Charset charset)
  {
    return encode (value, charset, Codec.B);
  }

  /**
   * Used to encode a string as specified by RFC 2047
   * 
   * @param value
   *        The string to encode
   * @param charset
   *        The character set to use for the encoding
   */
  public static String encode (final String value, final Charset charset, final Codec codec)
  {
    if (value == null)
      return null;
    try
    {
      switch (codec)
      {
        case Q:
          return (new RFC1522QCodec (charset)).encode (value);
        case B:
        default:
          return (new RFC1522BCodec (charset)).encode (value);
      }
    }
    catch (final Exception e)
    {
      return value;
    }
  }

  /**
   * Used to decode a string as specified by RFC 2047
   * 
   * @param value
   *        The encoded string
   */
  public static String decode (final String value)
  {
    if (value == null)
      return null;
    try
    {
      // try BCodec first
      return new RFC1522BCodec ().decode (value);
    }
    catch (final DecoderException de)
    {
      // try QCodec next
      try
      {
        return new RFC1522QCodec ().decode (value);
      }
      catch (final Exception ex)
      {
        return value;
      }
    }
    catch (final Exception e)
    {
      return value;
    }
  }
}
