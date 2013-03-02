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
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.codec.DecoderException;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;

/**
 * Codec for the Quoted-Printable section of <a
 * href="http://www.ietf.org/rfc/rfc1521.txt">RFC 1521</a>.
 * <p>
 * The Quoted-Printable encoding is intended to represent data that largely
 * consists of octets that correspond to printable characters in the ASCII
 * character set. It encodes the data in such a way that the resulting octets
 * are unlikely to be modified by mail transport. If the data being encoded are
 * mostly ASCII text, the encoded form of the data remains largely recognizable
 * by humans. A body which is entirely ASCII may also be encoded in
 * Quoted-Printable to ensure the integrity of the data should the message pass
 * through a character- translating, and/or line-wrapping gateway.
 * <p>
 * Note:
 * <p>
 * Rules #3, #4, and #5 of the quoted-printable spec are not implemented yet
 * because the complete quoted-printable spec does not lend itself well into the
 * byte[] oriented codec framework. Complete the codec once the streamable codec
 * framework is ready. The motivation behind providing the codec in a partial
 * form is that it can already come in handy for those applications that do not
 * require quoted-printable line formatting (rules #3, #4, #5), for instance Q
 * codec.
 * <p>
 * This class is immutable and thread-safe.
 * 
 * @see <a href="http://www.ietf.org/rfc/rfc1521.txt"> RFC 1521 MIME
 *      (Multipurpose Internet Mail Extensions) Part One: Mechanisms for
 *      Specifying and Describing the Format of Internet Message Bodies </a>
 * @since 1.3
 * @version $Id: java 1380305 2012-09-03 18:37:21Z tn $
 */
public class QuotedPrintableCodec
{
  /**
   * BitSet of printable characters as defined in RFC 1521.
   */
  private static final BitSet PRINTABLE_CHARS = new BitSet (256);
  private static final byte ESCAPE_CHAR = '=';
  private static final byte TAB = 9;
  private static final byte SPACE = 32;

  // Static initializer for printable chars collection
  static
  {
    // alpha characters
    for (int i = 33; i <= 60; i++)
      PRINTABLE_CHARS.set (i);
    for (int i = 62; i <= 126; i++)
      PRINTABLE_CHARS.set (i);
    PRINTABLE_CHARS.set (TAB);
    PRINTABLE_CHARS.set (SPACE);
  }

  /**
   * The default charset used for string decoding and encoding.
   */
  private final Charset m_aCharset;

  /**
   * Default constructor.
   */
  public QuotedPrintableCodec ()
  {
    this (CCharset.CHARSET_UTF_8_OBJ);
  }

  /**
   * Constructor which allows for the selection of a default charset.
   * 
   * @param aCharset
   *        the default string charset to use.
   * @throws UnsupportedCharsetException
   *         If the named charset is unavailable
   * @since 1.7
   */
  public QuotedPrintableCodec (@Nonnull final Charset aCharset)
  {
    m_aCharset = aCharset;
  }

  /**
   * Encodes byte into its quoted-printable representation.
   * 
   * @param b
   *        byte to encode
   * @param buffer
   *        the buffer to write to
   */
  private static final void _encodeQuotedPrintable (final int b, final NonBlockingByteArrayOutputStream buffer)
  {
    buffer.write (ESCAPE_CHAR);
    final char hex1 = Character.toUpperCase (Character.forDigit ((b >> 4) & 0xF, 16));
    final char hex2 = Character.toUpperCase (Character.forDigit (b & 0xF, 16));
    buffer.write (hex1);
    buffer.write (hex2);
  }

  /**
   * Encodes an array of bytes into an array of quoted-printable 7-bit
   * characters. Unsafe characters are escaped.
   * <p>
   * This function implements a subset of quoted-printable encoding
   * specification (rule #1 and rule #2) as defined in RFC 1521 and is suitable
   * for encoding binary data and unformatted text.
   * 
   * @param printable
   *        bitset of characters deemed quoted-printable
   * @param bytes
   *        array of bytes to be encoded
   * @return array of bytes containing quoted-printable data
   */
  @Nullable
  public static final byte [] encodeQuotedPrintable (@Nullable final BitSet printable, @Nullable final byte [] bytes)
  {
    if (bytes == null)
      return null;
    final BitSet aPrintable = printable == null ? PRINTABLE_CHARS : printable;
    final NonBlockingByteArrayOutputStream buffer = new NonBlockingByteArrayOutputStream ();
    for (final byte c : bytes)
    {
      int b = c;
      if (b < 0)
        b = 256 + b;

      if (aPrintable.get (b))
        buffer.write (b);
      else
        _encodeQuotedPrintable (b, buffer);
    }
    return buffer.toByteArray ();
  }

  /**
   * Decodes an array quoted-printable characters into an array of original
   * bytes. Escaped characters are converted back to their original
   * representation.
   * <p>
   * This function implements a subset of quoted-printable encoding
   * specification (rule #1 and rule #2) as defined in RFC 1521.
   * 
   * @param bytes
   *        array of quoted-printable characters
   * @return array of original bytes @ Thrown if quoted-printable decoding is
   *         unsuccessful
   */
  public static final byte [] decodeQuotedPrintable (@Nullable final byte [] bytes)
  {
    if (bytes == null)
      return null;

    final NonBlockingByteArrayOutputStream buffer = new NonBlockingByteArrayOutputStream ();
    for (int i = 0; i < bytes.length; i++)
    {
      final int b = bytes[i];
      if (b == ESCAPE_CHAR)
      {
        try
        {
          final int u = _digit16 (bytes[++i]);
          final int l = _digit16 (bytes[++i]);
          buffer.write ((char) ((u << 4) + l));
        }
        catch (final ArrayIndexOutOfBoundsException e)
        {
          throw new DecoderException ("Invalid quoted-printable encoding", e);
        }
      }
      else
      {
        buffer.write (b);
      }
    }
    return buffer.toByteArray ();
  }

  /**
   * Encodes an array of bytes into an array of quoted-printable 7-bit
   * characters. Unsafe characters are escaped.
   * <p>
   * This function implements a subset of quoted-printable encoding
   * specification (rule #1 and rule #2) as defined in RFC 1521 and is suitable
   * for encoding binary data and unformatted text.
   * 
   * @param bytes
   *        array of bytes to be encoded
   * @return array of bytes containing quoted-printable data
   */
  @Nullable
  public byte [] encode (@Nullable final byte [] bytes)
  {
    return encodeQuotedPrintable (PRINTABLE_CHARS, bytes);
  }

  /**
   * Decodes an array of quoted-printable characters into an array of original
   * bytes. Escaped characters are converted back to their original
   * representation.
   * <p>
   * This function implements a subset of quoted-printable encoding
   * specification (rule #1 and rule #2) as defined in RFC 1521.
   * 
   * @param bytes
   *        array of quoted-printable characters
   * @return array of original bytes @ Thrown if quoted-printable decoding is
   *         unsuccessful
   */
  public byte [] decode (final byte [] bytes)
  {
    return decodeQuotedPrintable (bytes);
  }

  /**
   * Encodes a string into its quoted-printable form using the default string
   * charset. Unsafe characters are escaped.
   * <p>
   * This function implements a subset of quoted-printable encoding
   * specification (rule #1 and rule #2) as defined in RFC 1521 and is suitable
   * for encoding binary data.
   * 
   * @param str
   *        string to convert to quoted-printable form
   * @return quoted-printable string
   * @see #getCharset()
   */
  public String encode (final String str)
  {
    return encode (str, getCharset ());
  }

  /**
   * Decodes a quoted-printable string into its original form using the
   * specified string charset. Escaped characters are converted back to their
   * original representation.
   * 
   * @param str
   *        quoted-printable string to convert into its original form
   * @param charset
   *        the original string charset
   * @return original string @ Thrown if quoted-printable decoding is
   *         unsuccessful
   * @since 1.7
   */
  public String decode (final String str, final Charset charset)
  {
    if (str == null)
      return null;
    return new String (decode (CharsetManager.getAsBytes (str, CCharset.CHARSET_US_ASCII_OBJ)), charset);
  }

  /**
   * Decodes a quoted-printable string into its original form using the default
   * string charset. Escaped characters are converted back to their original
   * representation.
   * 
   * @param str
   *        quoted-printable string to convert into its original form
   * @return original string @ Thrown if quoted-printable decoding is
   *         unsuccessful. Thrown if charset is not supported.
   * @see #getCharset()
   */
  public String decode (final String str)
  {
    return decode (str, getCharset ());
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

  /**
   * Encodes a string into its quoted-printable form using the specified
   * charset. Unsafe characters are escaped.
   * <p>
   * This function implements a subset of quoted-printable encoding
   * specification (rule #1 and rule #2) as defined in RFC 1521 and is suitable
   * for encoding binary data and unformatted text.
   * 
   * @param str
   *        string to convert to quoted-printable form
   * @param charset
   *        the charset for str
   * @return quoted-printable string
   * @since 1.7
   */
  public String encode (final String str, final Charset charset)
  {
    if (str == null)
      return null;
    return CharsetManager.getAsString (encode (str.getBytes (charset)), CCharset.CHARSET_US_ASCII_OBJ);
  }

  /**
   * Returns the numeric value of the character <code>b</code> in radix 16.
   * 
   * @param b
   *        The byte to be converted.
   * @return The numeric value represented by the character in radix 16. @
   *         Thrown when the byte is not valid per
   *         {@link Character#digit(char,int)}
   */
  private static int _digit16 (final byte b)
  {
    final int i = Character.digit ((char) b, 16);
    if (i == -1)
      throw new DecoderException ("Invalid URL encoding: not a valid digit (radix 16): " + b);
    return i;
  }
}
