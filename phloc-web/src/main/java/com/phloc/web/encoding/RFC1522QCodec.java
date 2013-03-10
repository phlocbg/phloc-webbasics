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
import java.util.BitSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.codec.DecoderException;
import com.phloc.commons.codec.EncoderException;

/**
 * Similar to the Quoted-Printable content-transfer-encoding defined in <a
 * href="http://www.ietf.org/rfc/rfc1521.txt">RFC 1521</a> and designed to allow
 * text containing mostly ASCII characters to be decipherable on an ASCII
 * terminal without decoding.
 * <p>
 * <a href="http://www.ietf.org/rfc/rfc1522.txt">RFC 1522</a> describes
 * techniques to allow the encoding of non-ASCII text in various portions of a
 * RFC 822 [2] message header, in a manner which is unlikely to confuse existing
 * message handling software.
 * <p>
 * This class is conditionally thread-safe. The instance field m_bEncodeBlanks
 * is mutable {@link #setEncodeBlanks(boolean)} but is not volatile, and
 * accesses are not synchronised. If an instance of the class is shared between
 * threads, the caller needs to ensure that suitable synchronisation is used to
 * ensure safe publication of the value between threads, and must not invoke
 * {@link #setEncodeBlanks(boolean)} after initial setup.
 * 
 * @see <a href="http://www.ietf.org/rfc/rfc1522.txt">MIME (Multipurpose
 *      Internet Mail Extensions) Part Two: Message Header Extensions for
 *      Non-ASCII Text</a>
 * @since 1.3
 * @version $Id: QCodec.java 1380305 2012-09-03 18:37:21Z tn $
 */
public class RFC1522QCodec extends AbstractRFC1522Codec
{
  /**
   * BitSet of printable characters as defined in RFC 1522.
   */
  private static final BitSet PRINTABLE_CHARS = new BitSet (256);
  // Static initializer for printable chars collection
  static
  {
    // alpha characters
    PRINTABLE_CHARS.set (' ');
    PRINTABLE_CHARS.set ('!');
    PRINTABLE_CHARS.set ('"');
    PRINTABLE_CHARS.set ('#');
    PRINTABLE_CHARS.set ('$');
    PRINTABLE_CHARS.set ('%');
    PRINTABLE_CHARS.set ('&');
    PRINTABLE_CHARS.set ('\'');
    PRINTABLE_CHARS.set ('(');
    PRINTABLE_CHARS.set (')');
    PRINTABLE_CHARS.set ('*');
    PRINTABLE_CHARS.set ('+');
    PRINTABLE_CHARS.set (',');
    PRINTABLE_CHARS.set ('-');
    PRINTABLE_CHARS.set ('.');
    PRINTABLE_CHARS.set ('/');
    for (int i = '0'; i <= '9'; i++)
      PRINTABLE_CHARS.set (i);
    PRINTABLE_CHARS.set (':');
    PRINTABLE_CHARS.set (';');
    PRINTABLE_CHARS.set ('<');
    PRINTABLE_CHARS.set ('>');
    PRINTABLE_CHARS.set ('@');
    for (int i = 'A'; i <= 'Z'; i++)
      PRINTABLE_CHARS.set (i);
    PRINTABLE_CHARS.set ('[');
    PRINTABLE_CHARS.set ('\\');
    PRINTABLE_CHARS.set (']');
    PRINTABLE_CHARS.set ('^');
    PRINTABLE_CHARS.set ('`');
    for (int i = 'a'; i <= 'z'; i++)
      PRINTABLE_CHARS.set (i);
    PRINTABLE_CHARS.set ('{');
    PRINTABLE_CHARS.set ('|');
    PRINTABLE_CHARS.set ('}');
    PRINTABLE_CHARS.set ('~');
  }

  private static final byte BLANK = 32;
  private static final byte UNDERSCORE = 95;

  /**
   * The default charset used for string decoding and encoding.
   */
  private final Charset m_aCharset;
  private boolean m_bEncodeBlanks = false;

  /**
   * Default constructor.
   */
  public RFC1522QCodec ()
  {
    this (CCharset.CHARSET_UTF_8_OBJ);
  }

  /**
   * Constructor which allows for the selection of a default charset.
   * 
   * @param aCharset
   *        the default string charset to use.
   * @see <a
   *      href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
   *      charsets</a>
   * @since 1.7
   */
  public RFC1522QCodec (@Nonnull final Charset aCharset)
  {
    m_aCharset = aCharset;
  }

  @Override
  protected String getRFC1522Encoding ()
  {
    return "Q";
  }

  @Override
  @Nonnull
  protected byte [] doEncoding (@Nonnull final byte [] bytes)
  {
    final byte [] data = QuotedPrintableCodec.encodeQuotedPrintable (PRINTABLE_CHARS, bytes);
    if (m_bEncodeBlanks)
      for (int i = 0; i < data.length; i++)
        if (data[i] == BLANK)
          data[i] = UNDERSCORE;
    return data;
  }

  @Override
  @Nonnull
  protected byte [] doDecoding (@Nonnull final byte [] bytes) throws DecoderException
  {
    boolean bHasUnderscores = false;
    for (final byte b : bytes)
      if (b == UNDERSCORE)
      {
        bHasUnderscores = true;
        break;
      }

    if (bHasUnderscores)
    {
      final byte [] tmp = new byte [bytes.length];
      for (int i = 0; i < bytes.length; i++)
      {
        final byte b = bytes[i];
        if (b != UNDERSCORE)
          tmp[i] = b;
        else
          tmp[i] = BLANK;
      }
      return QuotedPrintableCodec.decodeQuotedPrintable (tmp);
    }
    return QuotedPrintableCodec.decodeQuotedPrintable (bytes);
  }

  /**
   * Encodes a string into its quoted-printable form using the specified
   * charset. Unsafe characters are escaped.
   * 
   * @param sText
   *        string to convert to quoted-printable form
   * @param aCharset
   *        the charset for str
   * @return quoted-printable string
   * @throws EncoderException
   *         thrown if a failure condition is encountered during the encoding
   *         process.
   * @since 1.7
   */
  @Nullable
  public String encode (@Nullable final String sText, @Nonnull final Charset aCharset) throws EncoderException
  {
    if (sText == null)
      return null;
    return encodeText (sText, aCharset);
  }

  /**
   * Encodes a string into its quoted-printable form using the default charset.
   * Unsafe characters are escaped.
   * 
   * @param sText
   *        string to convert to quoted-printable form
   * @return quoted-printable string
   * @throws EncoderException
   *         thrown if a failure condition is encountered during the encoding
   *         process.
   */
  @Nullable
  public String encode (@Nullable final String sText) throws EncoderException
  {
    if (sText == null)
      return null;
    return encodeText (sText, getCharset ());
  }

  /**
   * Decodes a quoted-printable string into its original form. Escaped
   * characters are converted back to their original representation.
   * 
   * @param sText
   *        quoted-printable string to convert into its original form
   * @return original string
   * @throws DecoderException
   *         A decoder exception is thrown if a failure condition is encountered
   *         during the decode process.
   */
  @Nullable
  public String decode (@Nullable final String sText) throws DecoderException
  {
    if (sText == null)
      return null;
    return decodeText (sText);
  }

  /**
   * Gets the default charset name used for string decoding and encoding.
   * 
   * @return the default charset name
   * @since 1.7
   */
  @Nonnull
  public Charset getCharset ()
  {
    return m_aCharset;
  }

  /**
   * Tests if optional transformation of SPACE characters is to be used
   * 
   * @return {@code true} if SPACE characters are to be transformed,
   *         {@code false} otherwise
   */
  public boolean isEncodeBlanks ()
  {
    return m_bEncodeBlanks;
  }

  /**
   * Defines whether optional transformation of SPACE characters is to be used
   * 
   * @param bEncodeBlanks
   *        {@code true} if SPACE characters are to be transformed,
   *        {@code false} otherwise
   */
  public void setEncodeBlanks (final boolean bEncodeBlanks)
  {
    m_bEncodeBlanks = bEncodeBlanks;
  }
}
