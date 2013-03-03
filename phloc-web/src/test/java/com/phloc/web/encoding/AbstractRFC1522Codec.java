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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.codec.DecoderException;
import com.phloc.commons.codec.EncoderException;
import com.phloc.commons.string.StringHelper;

/**
 * Implements methods common to all codecs defined in RFC 1522.
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
 * @version $Id: RFC1522Codec.java 1379145 2012-08-30 21:02:52Z tn $
 */
public abstract class AbstractRFC1522Codec
{
  /** Separator. */
  protected static final char SEP = '?';

  /** Prefix. */
  protected static final String POSTFIX = "?=";

  /** Postfix. */
  protected static final String PREFIX = "=?";

  /**
   * Applies an RFC 1522 compliant encoding scheme to the given string of text
   * with the given charset.
   * <p>
   * This method constructs the "encoded-word" header common to all the RFC 1522
   * codecs and then invokes {@link #doEncoding(byte [])} method of a concrete
   * class to perform the specific encoding.
   * 
   * @param sText
   *        a string to encode
   * @param aCharset
   *        a charset to be used
   * @return RFC 1522 compliant "encoded-word"
   * @throws EncoderException
   *         thrown if there is an error condition during the Encoding process.
   * @see <a
   *      href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
   *      charsets</a>
   */
  @Nullable
  protected String encodeText (@Nullable final String sText, @Nonnull final Charset aCharset) throws EncoderException
  {
    if (sText == null)
      return null;

    final StringBuilder aSB = new StringBuilder ();
    aSB.append (PREFIX).append (aCharset.name ()).append (SEP).append (getRFC1522Encoding ()).append (SEP);
    final byte [] rawData = doEncoding (sText.getBytes (aCharset));
    aSB.append (CharsetManager.getAsString (rawData, CCharset.CHARSET_US_ASCII_OBJ));
    aSB.append (POSTFIX);
    return aSB.toString ();
  }

  /**
   * Applies an RFC 1522 compliant decoding scheme to the given string of text.
   * <p>
   * This method processes the "encoded-word" header common to all the RFC 1522
   * codecs and then invokes {@link #doEncoding(byte [])} method of a concrete
   * class to perform the specific decoding.
   * 
   * @param sText
   *        a string to decode
   * @return A new decoded String or {@code null} if the input is {@code null}.
   * @throws DecoderException
   *         thrown if there is an error condition during the decoding process.
   */
  @Nullable
  protected String decodeText (@Nullable final String sText) throws DecoderException
  {
    if (sText == null)
      return null;

    if (!sText.startsWith (PREFIX) || !sText.endsWith (POSTFIX))
      throw new DecoderException ("RFC 1522 violation: malformed encoded content");

    final int terminator = sText.length () - 2;
    int from = 2;
    int to = sText.indexOf (SEP, from);
    if (to == terminator)
      throw new DecoderException ("RFC 1522 violation: charset token not found");
    final String sCharset = sText.substring (from, to);
    if (StringHelper.hasNoText (sCharset))
      throw new DecoderException ("RFC 1522 violation: charset not specified");
    from = to + 1;
    to = sText.indexOf (SEP, from);
    if (to == terminator)
      throw new DecoderException ("RFC 1522 violation: encoding token not found");
    final String sEncoding = sText.substring (from, to);
    if (!getRFC1522Encoding ().equalsIgnoreCase (sEncoding))
      throw new DecoderException ("This codec cannot decode " + sEncoding + " encoded content");
    from = to + 1;
    to = sText.indexOf (SEP, from);
    byte [] data = CharsetManager.getAsBytes (sText.substring (from, to), CCharset.CHARSET_US_ASCII_OBJ);
    data = doDecoding (data);
    return CharsetManager.getAsString (data, sCharset);
  }

  /**
   * Returns the codec name (referred to as encoding in the RFC 1522).
   * 
   * @return name of the codec
   */
  @Nonnull
  @Nonempty
  protected abstract String getRFC1522Encoding ();

  /**
   * Encodes an array of bytes using the defined encoding scheme.
   * 
   * @param bytes
   *        Data to be encoded
   * @return A byte array containing the encoded data
   * @throws EncoderException
   *         thrown if the Encoder encounters a failure condition during the
   *         encoding process.
   */
  @Nonnull
  protected abstract byte [] doEncoding (@Nonnull byte [] bytes) throws EncoderException;

  /**
   * Decodes an array of bytes using the defined encoding scheme.
   * 
   * @param bytes
   *        Data to be decoded
   * @return a byte array that contains decoded data
   * @throws DecoderException
   *         A decoder exception is thrown if a Decoder encounters a failure
   *         condition during the decode process.
   */
  @Nonnull
  protected abstract byte [] doDecoding (@Nonnull byte [] bytes) throws DecoderException;
}
