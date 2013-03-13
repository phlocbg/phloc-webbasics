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
package com.phloc.web.http;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.string.StringHelper;

/**
 * HTTP string utils. Based on RFC 1945 http://tools.ietf.org/html/rfc1945
 * 
 * @author philip
 */
@Immutable
public final class HTTPStringHelper
{
  /** Minimum index (inclusive) */
  public static final int MIN_INDEX = 0;
  /** Maximum index (inclusive) */
  public static final int MAX_INDEX = 127;
  /** Comment start character */
  public static final char COMMENT_START = '(';
  /** Comment end character */
  public static final char COMMENT_END = ')';
  /** Quoted text start character */
  public static final char QUOTEDTEXT_START = '"';
  /** Quoted text end character */
  public static final char QUOTEDTEXT_END = '"';

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final HTTPStringHelper s_aInstance = new HTTPStringHelper ();

  private static final int UALPHA = 0x0001;
  private static final int LALPHA = 0x0002;
  private static final int ALPHA = 0x0004;
  private static final int DIGIT = 0x0008;
  private static final int CTL = 0x0010;
  private static final int HEX = 0x0020;
  private static final int NON_TOKEN = 0x0040;
  private static final int NON_TEXT = 0x0080;
  private static final int NON_COMMENT = 0x0100;
  private static final int NON_QUOTEDTEXT = 0x0200;
  private static final int RESERVED = 0x0400;
  private static final int EXTRA = 0x0800;
  private static final int SAFE = 0x1000;
  private static final int UNSAFE = 0x2000;

  private static final char [] MAPPINGS = new char [] {
                                                       // 0x00
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TOKEN | UNSAFE,
                                                       CTL | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       // 0x10
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       CTL | NON_TEXT | UNSAFE,
                                                       // 0x20
                                                       NON_TOKEN | UNSAFE,
                                                       EXTRA,
                                                       NON_TOKEN | NON_QUOTEDTEXT | UNSAFE,
                                                       UNSAFE,
                                                       SAFE,
                                                       UNSAFE,
                                                       RESERVED,
                                                       EXTRA,
                                                       NON_TOKEN | NON_COMMENT | EXTRA,
                                                       NON_TOKEN | NON_COMMENT | EXTRA,
                                                       EXTRA,
                                                       RESERVED,
                                                       NON_TOKEN | EXTRA,
                                                       SAFE,
                                                       SAFE,
                                                       NON_TOKEN | RESERVED,
                                                       // 0x30
                                                       DIGIT | HEX,
                                                       DIGIT | HEX,
                                                       DIGIT | HEX,
                                                       DIGIT | HEX,
                                                       DIGIT | HEX,
                                                       DIGIT | HEX,
                                                       DIGIT | HEX,
                                                       DIGIT | HEX,
                                                       DIGIT | HEX,
                                                       DIGIT | HEX,
                                                       NON_TOKEN | RESERVED,
                                                       NON_TOKEN | RESERVED,
                                                       NON_TOKEN | UNSAFE,
                                                       NON_TOKEN | RESERVED,
                                                       NON_TOKEN | UNSAFE,
                                                       NON_TOKEN | RESERVED,
                                                       // 0x40
                                                       NON_TOKEN | RESERVED,
                                                       UALPHA | ALPHA | HEX,
                                                       UALPHA | ALPHA | HEX,
                                                       UALPHA | ALPHA | HEX,
                                                       UALPHA | ALPHA | HEX,
                                                       UALPHA | ALPHA | HEX,
                                                       UALPHA | ALPHA | HEX,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       // 0x50
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       UALPHA | ALPHA,
                                                       NON_TOKEN,
                                                       NON_TOKEN,
                                                       NON_TOKEN,
                                                       0,
                                                       SAFE,
                                                       // 0x60
                                                       0,
                                                       LALPHA | ALPHA | HEX,
                                                       LALPHA | ALPHA | HEX,
                                                       LALPHA | ALPHA | HEX,
                                                       LALPHA | ALPHA | HEX,
                                                       LALPHA | ALPHA | HEX,
                                                       LALPHA | ALPHA | HEX,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       // 0x70
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       LALPHA | ALPHA,
                                                       NON_TOKEN,
                                                       0,
                                                       NON_TOKEN,
                                                       0,
                                                       CTL | NON_TEXT | UNSAFE };

  static
  {
    if (MAPPINGS.length != MAX_INDEX + 1)
      throw new InitializationException ("MAPPING array is invalid!");
  }

  private HTTPStringHelper ()
  {}

  public static boolean isChar (final int n)
  {
    return n >= MIN_INDEX && n <= MAX_INDEX;
  }

  public static boolean isOctet (final int n)
  {
    return n >= MIN_INDEX && n < 256;
  }

  public static boolean isUpperAlphaChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & UALPHA) == UALPHA;
  }

  public static boolean isLowerAlphaChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & LALPHA) == LALPHA;
  }

  public static boolean isAlphaChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & ALPHA) == ALPHA;
  }

  public static boolean isDigitChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & DIGIT) == DIGIT;
  }

  public static boolean isControlChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & CTL) == CTL;
  }

  public static boolean isCRChar (final int n)
  {
    return n == 13;
  }

  public static boolean isLFChar (final int n)
  {
    return n == 10;
  }

  public static boolean isSpaceChar (final int n)
  {
    return n == 32;
  }

  public static boolean isTabChar (final int n)
  {
    return n == 9;
  }

  public static boolean isQuoteChar (final int n)
  {
    return n == 34;
  }

  public static boolean isHexChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & HEX) == HEX;
  }

  public static boolean isNonTokenChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & NON_TOKEN) == NON_TOKEN;
  }

  public static boolean isTokenChar (final int n)
  {
    if (!isChar (n))
      return false;
    final int nMapping = MAPPINGS[n];
    return (nMapping & CTL) == 0 && (nMapping & NON_TOKEN) == 0;
  }

  public static boolean isToken (@Nullable final char [] aChars)
  {
    if (ArrayHelper.isEmpty (aChars))
      return false;
    for (final char c : aChars)
      if (!isTokenChar (c))
        return false;
    return true;
  }

  public static boolean isToken (@Nullable final String sStr)
  {
    if (StringHelper.hasNoText (sStr))
      return false;
    return isToken (sStr.toCharArray ());
  }

  public static boolean isTextChar (final int n)
  {
    if (n < MIN_INDEX)
      return false;
    // Any octet allowed!
    if (n > MAX_INDEX)
      return n < 256;
    return (MAPPINGS[n] & NON_TEXT) == 0;
  }

  public static boolean isCommentChar (final int n)
  {
    if (n < MIN_INDEX)
      return false;
    // Any octet allowed!
    if (n > MAX_INDEX)
      return n < 256;
    final int nMapping = MAPPINGS[n];
    return (nMapping & NON_TEXT) == 0 && (nMapping & NON_COMMENT) == 0;
  }

  public static boolean isComment (@Nullable final char [] aChars)
  {
    if (ArrayHelper.getSize (aChars) < 2 || aChars[0] != COMMENT_START || aChars[aChars.length - 1] != COMMENT_END)
      return false;
    for (int i = 1; i < aChars.length - 1; ++i)
      if (!isCommentChar (aChars[i]))
        return false;
    return true;
  }

  public static boolean isComment (@Nullable final String sStr)
  {
    if (StringHelper.getLength (sStr) < 2)
      return false;
    return isComment (sStr.toCharArray ());
  }

  public static boolean isQuotedTextChar (final int n)
  {
    if (!isChar (n))
      return false;
    final int nMapping = MAPPINGS[n];
    return (nMapping & NON_TEXT) == 0 && (nMapping & NON_QUOTEDTEXT) == 0;
  }

  public static boolean isQuotedText (@Nullable final char [] aChars)
  {
    if (ArrayHelper.getSize (aChars) < 2 ||
        aChars[0] != QUOTEDTEXT_START ||
        aChars[aChars.length - 1] != QUOTEDTEXT_END)
      return false;
    for (int i = 1; i < aChars.length - 1; ++i)
      if (!isQuotedTextChar (aChars[i]))
        return false;
    return true;
  }

  public static boolean isQuotedText (@Nullable final String sStr)
  {
    if (StringHelper.getLength (sStr) < 2)
      return false;
    return isQuotedText (sStr.toCharArray ());
  }

  public static boolean isQuotedTextContent (@Nullable final char [] aChars)
  {
    if (aChars == null)
      return false;
    for (final char c : aChars)
      if (!isQuotedTextChar (c))
        return false;
    return true;
  }

  public static boolean isQuotedTextContent (@Nullable final String sStr)
  {
    if (sStr == null)
      return false;
    return isQuotedTextContent (sStr.toCharArray ());
  }

  public static boolean isWord (@Nullable final char [] aChars)
  {
    return isToken (aChars) || isQuotedText (aChars);
  }

  public static boolean isWord (@Nullable final String sStr)
  {
    if (StringHelper.hasNoText (sStr))
      return false;
    return isWord (sStr.toCharArray ());
  }

  public static boolean isReservedChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & RESERVED) == RESERVED;
  }

  public static boolean isExtraChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & EXTRA) == EXTRA;
  }

  public static boolean isSafeChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & SAFE) == SAFE;
  }

  public static boolean isUnsafeChar (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & UNSAFE) == UNSAFE;
  }

  public static boolean isNationalChar (final int n)
  {
    if (n < MIN_INDEX)
      return false;
    // Any octet allowed!
    if (n > MAX_INDEX)
      return n < 256;
    final int nMapping = MAPPINGS[n];
    return (nMapping & ALPHA) == 0 &&
           (nMapping & DIGIT) == 0 &&
           (nMapping & RESERVED) == 0 &&
           (nMapping & EXTRA) == 0 &&
           (nMapping & SAFE) == 0 &&
           (nMapping & UNSAFE) == 0;
  }

  public static boolean isUnreservedChar (final int n)
  {
    if (!isChar (n))
      return false;
    final int nMapping = MAPPINGS[n];
    if ((nMapping & RESERVED) == RESERVED || (nMapping & UNSAFE) == UNSAFE)
      return false;
    return (nMapping & ALPHA) == ALPHA ||
           (nMapping & DIGIT) == DIGIT ||
           (nMapping & EXTRA) == EXTRA ||
           (nMapping & SAFE) == SAFE;
  }
}
