/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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

import com.phloc.commons.base64.Base64;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.codec.DecoderException;
import com.phloc.commons.codec.EncoderException;

/**
 * Identical to the Base64 encoding defined by <a
 * href="http://www.ietf.org/rfc/rfc1521.txt">RFC 1521</a> and allows a
 * character set to be specified.
 * <p>
 * <a href="http://www.ietf.org/rfc/rfc1522.txt">RFC 1522</a> describes
 * techniques to allow the encoding of non-ASCII text in various portions of a
 * RFC 822 [2] message header, in a manner which is unlikely to confuse existing
 * message handling software.
 * <p>
 * This class is immutable and thread-safe.
 * 
 * @see <a href="http://www.ietf.org/rfc/rfc1522.txt">MIME (Multipurpose
 *      Internet Mail Extensions) Part Two: Message Header Extensions for
 *      Non-ASCII Text</a>
 * @since 1.3
 * @version $Id: BCodec.java 1380305 2012-09-03 18:37:21Z tn $
 */
public class BCodec extends AbstractRFC1522Codec
{
  /**
   * The default charset used for string decoding and encoding.
   */
  private final Charset m_aCharset;

  /**
   * Default constructor.
   */
  public BCodec ()
  {
    this (CCharset.CHARSET_UTF_8_OBJ);
  }

  /**
   * Constructor which allows for the selection of a default charset
   * 
   * @param charset
   *        the default string charset to use.
   * @see <a
   *      href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
   *      charsets</a>
   * @since 1.7
   */
  public BCodec (@Nonnull final Charset charset)
  {
    m_aCharset = charset;
  }

  @Override
  protected String getEncoding ()
  {
    return "B";
  }

  @Override
  protected byte [] doEncoding (final byte [] bytes)
  {
    if (bytes == null)
      return null;
    return Base64.encodeBytesToBytes (bytes);
  }

  @Override
  protected byte [] doDecoding (final byte [] bytes)
  {
    if (bytes == null)
      return null;
    return Base64Helper.safeDecode (bytes);
  }

  /**
   * Encodes a string into its Base64 form using the specified charset. Unsafe
   * characters are escaped.
   * 
   * @param value
   *        string to convert to Base64 form
   * @param charset
   *        the charset for <code>value</code>
   * @return Base64 string
   * @throws EncoderException
   *         thrown if a failure condition is encountered during the encoding
   *         process.
   * @since 1.7
   */
  public String encode (final String value, final Charset charset) throws EncoderException
  {
    if (value == null)
      return null;
    return encodeText (value, charset);
  }

  /**
   * Encodes a string into its Base64 form using the default charset. Unsafe
   * characters are escaped.
   * 
   * @param value
   *        string to convert to Base64 form
   * @return Base64 string
   * @throws EncoderException
   *         thrown if a failure condition is encountered during the encoding
   *         process.
   */
  public String encode (final String value) throws EncoderException
  {
    if (value == null)
      return null;
    return encode (value, getCharset ());
  }

  /**
   * Decodes a Base64 string into its original form. Escaped characters are
   * converted back to their original representation.
   * 
   * @param value
   *        Base64 string to convert into its original form
   * @return original string
   * @throws DecoderException
   *         A decoder exception is thrown if a failure condition is encountered
   *         during the decode process.
   */
  public String decode (final String value) throws DecoderException
  {
    if (value == null)
      return null;
    return decodeText (value);
  }

  /**
   * Gets the default charset name used for string decoding and encoding.
   * 
   * @return the default charset name
   * @since 1.7
   */
  public Charset getCharset ()
  {
    return m_aCharset;
  }

  /**
   * Gets the default charset name used for string decoding and encoding.
   * 
   * @return the default charset name
   */
  public String getDefaultCharset ()
  {
    return m_aCharset.name ();
  }
}
