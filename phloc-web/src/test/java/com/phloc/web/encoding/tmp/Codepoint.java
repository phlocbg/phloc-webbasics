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
package com.phloc.web.encoding.tmp;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.hash.HashCodeGenerator;

/**
 * Represents a single Unicode Codepoint
 * 
 * @author Apache Abdera
 * @author philip
 */
@NotThreadSafe
public final class Codepoint implements Serializable, Comparable <Codepoint>
{
  private final int m_nValue;

  /**
   * Create a Codepoint from a byte array with the specified charset encoding.
   * Length must equal 1
   */
  public Codepoint (@Nonnull final byte [] bytes, @Nonnull final Charset encoding)
  {
    this (CharsetManager.getAsString (bytes, encoding));
  }

  private static int _getValueFromCharSequence (@Nonnull final CharSequence s)
  {
    final int nLength = s.length ();
    if (nLength == 0)
      throw new IllegalArgumentException ("Empty CharSequence");
    if (nLength == 1)
      return s.charAt (0);
    if (nLength > 2)
      throw new IllegalArgumentException ("Too many chars: " + s);
    return Character.toCodePoint (s.charAt (0), s.charAt (1));
  }

  /**
   * Create a Codepoint from a CharSequence. Length must equal 1 or 2
   */
  public Codepoint (@Nonnull final CharSequence value)
  {
    this (_getValueFromCharSequence (value));
  }

  /**
   * Create a Codepoint from a String. Length must equal 1 or 2
   */
  public Codepoint (@Nonnull final String value)
  {
    this (value.toCharArray ());
  }

  private static int _getValueFromCharArray (@Nonnull final char [] s)
  {
    final int nLength = s.length;
    if (nLength == 0)
      throw new IllegalArgumentException ("Empty char[]");
    if (nLength == 1)
      return s[0];
    if (nLength > 2)
      throw new IllegalArgumentException ("Too many chars: " + Arrays.toString (s));
    return Character.toCodePoint (s[0], s[1]);
  }

  /**
   * Create a Codepoint from a char array. Length must equal 1 or 2
   */
  public Codepoint (@Nonnull final char [] value)
  {
    this (_getValueFromCharArray (value));
  }

  /**
   * Create a codepoint from a single char
   */
  public Codepoint (final char value)
  {
    this ((int) value);
  }

  /**
   * Create a codepoint from a surrogate pair
   */
  public Codepoint (final char high, final char low)
  {
    this (Character.toCodePoint (high, low));
  }

  /**
   * Create a codepoint as a copy of another codepoint
   */
  public Codepoint (@Nonnull final Codepoint aCodepoint)
  {
    this (aCodepoint.m_nValue);
  }

  /**
   * Create a codepoint from a specific integer value
   */
  public Codepoint (@Nonnegative final int nValue)
  {
    if (!Character.isValidCodePoint (nValue))
      throw new IllegalArgumentException ("Invalid Codepoint: " + nValue);
    m_nValue = nValue;
  }

  /**
   * The codepoint value
   */
  @Nonnegative
  public int getValue ()
  {
    return m_nValue;
  }

  /**
   * True if this codepoint is supplementary
   */
  public boolean isSupplementary ()
  {
    return Character.isSupplementaryCodePoint (m_nValue);
  }

  /**
   * True if this codepoint is a low surrogate
   */
  public boolean isLowSurrogate ()
  {
    return Character.isLowSurrogate ((char) m_nValue);
  }

  /**
   * True if this codepoint is a high surrogate
   */
  public boolean isHighSurrogate ()
  {
    return Character.isHighSurrogate ((char) m_nValue);
  }

  /**
   * Get the high surrogate of this Codepoint
   */
  public char getHighSurrogate ()
  {
    return CodepointUtils.getHighSurrogate (m_nValue);
  }

  /**
   * Get the low surrogate of this Codepoint
   */
  public char getLowSurrogate ()
  {
    return CodepointUtils.getLowSurrogate (m_nValue);
  }

  /**
   * True if this Codepoint is a bidi control char
   */
  public boolean isBidi ()
  {
    return CodepointUtils.isBidi (m_nValue);
  }

  public boolean isDigit ()
  {
    return CodepointUtils.isDigit (m_nValue);
  }

  public boolean isAlpha ()
  {
    return CodepointUtils.isAlpha (m_nValue);
  }

  public boolean isAlphaDigit ()
  {
    return CodepointUtils.isAlpha (m_nValue);
  }

  @Nonnull
  @Nonempty
  public String getAsString ()
  {
    return CodepointUtils.getAsString (m_nValue);
  }

  @Nonnull
  @ReturnsMutableCopy
  public char [] getAsChars ()
  {
    return CodepointUtils.getAsCharArray (m_nValue);
  }

  /**
   * @return The number of chars necessary to represent this codepoint. Returns
   *         2 if this is a supplementary codepoint, 1 otherwise.
   */
  @Nonnull
  public int getCharCount ()
  {
    return Character.charCount (m_nValue);
  }

  @Nonnull
  public byte [] getAsBytes (@Nonnull final Charset aCharset)
  {
    return CharsetManager.getAsBytes (getAsString (), aCharset);
  }

  /**
   * Plane 0 (0000–FFFF): Basic Multilingual Plane (BMP). This is the plane
   * containing most of the character assignments so far. A primary objective
   * for the BMP is to support the unification of prior character sets as well
   * as characters for writing systems in current use. Plane 1 (10000–1FFFF):
   * Supplementary Multilingual Plane (SMP). Plane 2 (20000–2FFFF):
   * Supplementary Ideographic Plane (SIP) Planes 3 to 13 (30000–DFFFF) are
   * unassigned Plane 14 (E0000–EFFFF): Supplementary Special-purpose Plane
   * (SSP) Plane 15 (F0000–FFFFF) reserved for the Private Use Area (PUA) Plane
   * 16 (100000–10FFFF), reserved for the Private Use Area (PUA)
   **/
  public int getPlane ()
  {
    return m_nValue / (0xFFFF + 1);
  }

  /**
   * Get the next codepoint
   */
  @Nonnull
  public Codepoint next ()
  {
    if (m_nValue == 0x10ffff)
      throw new IndexOutOfBoundsException ();
    return new Codepoint (m_nValue + 1);
  }

  /**
   * Get the previous codepoint
   */
  @Nonnull
  public Codepoint previous ()
  {
    if (m_nValue == 0)
      throw new IndexOutOfBoundsException ();
    return new Codepoint (m_nValue - 1);
  }

  public int compareTo (@Nonnull final Codepoint o)
  {
    return CompareUtils.compare (m_nValue, o.m_nValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (!(o instanceof Codepoint))
      return false;
    final Codepoint rhs = (Codepoint) o;
    return m_nValue == rhs.m_nValue;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_nValue).getHashCode ();
  }
}
