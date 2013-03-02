/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package com.phloc.web.encoding;

import java.io.Serializable;
import java.nio.charset.Charset;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.hash.HashCodeGenerator;

/**
 * Represents a single Unicode Codepoint
 */
public final class Codepoint implements Serializable, Comparable <Codepoint>
{
  private static final Charset DEFAULT_ENCODING = CCharset.CHARSET_UTF_8_OBJ;

  private final int m_nValue;

  /**
   * Create a Codepoint from a byte array using the default encoding (UTF-8)
   */
  public Codepoint (@Nonnull final byte [] bytes)
  {
    this (bytes, DEFAULT_ENCODING);
  }

  /**
   * Create a Codepoint from a byte array with the specified charset encoding.
   * Length must equal 1
   */
  public Codepoint (@Nonnull final byte [] bytes, @Nonnull final Charset encoding)
  {
    this (CharsetManager.getAsString (bytes, encoding));
  }

  /**
   * Create a Codepoint from a CharSequence. Length must equal 1
   */
  public Codepoint (@Nonnull final CharSequence value)
  {
    this (_valueFromCharSequence (value));
  }

  private static int _valueFromCharSequence (@Nonnull final CharSequence s)
  {
    if (s.length () == 1)
      return s.charAt (0);
    if (s.length () > 2)
      throw new IllegalArgumentException ("Too many chars: " + s);
    return CharUtils.getSupplementaryValue (s.charAt (0), s.charAt (1));
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
    this (CharUtils.getSupplementaryValue (high, low));
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
    if (nValue < 0)
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
    return CharUtils.getHighSurrogate (m_nValue);
  }

  /**
   * Get the low surrogate of this Codepoint
   */
  public char getLowSurrogate ()
  {
    return CharUtils.getLowSurrogate (m_nValue);
  }

  /**
   * True if this Codepoint is a bidi control char
   */
  public boolean isBidi ()
  {
    return CharUtils.isBidi (m_nValue);
  }

  public boolean isDigit ()
  {
    return CharUtils.isDigit (m_nValue);
  }

  public boolean isAlpha ()
  {
    return CharUtils.isAlpha (m_nValue);
  }

  public boolean isAlphaDigit ()
  {
    return CharUtils.isAlpha (m_nValue);
  }

  @Nonnull
  @Nonempty
  public String getAsString ()
  {
    return CharUtils.getAsString (m_nValue);
  }

  @Nonnull
  @ReturnsMutableCopy
  public char [] getAsChars ()
  {
    return getAsString ().toCharArray ();
  }

  /**
   * @return The number of chars necessary to represent this codepoint. Returns
   *         2 if this is a supplementary codepoint, 1 otherwise.
   */
  @Nonnull
  public int getCharCount ()
  {
    return CharUtils.getStringLength (m_nValue);
  }

  @Nonnull
  public byte [] getAsBytes ()
  {
    return getAsBytes (DEFAULT_ENCODING);
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
