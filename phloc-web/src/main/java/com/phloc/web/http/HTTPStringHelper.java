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

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.exceptions.InitializationException;

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

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final HTTPStringHelper s_aInstance = new HTTPStringHelper ();

  private static final int UALPHA = 0x0001;
  private static final int LALPHA = 0x0002;
  private static final int ALPHA = 0x0004;
  private static final int DIGIT = 0x0008;
  private static final int CTL = 0x0010;
  private static final int HEX = 0x0020;
  private static final int TSPECIAL = 0x0040;
  private static final int NONTEXT = 0x0080;
  private static final int NONCOMMENT = 0x0100;
  private static final int NONQUOTEDTEXT = 0x0200;

  private static final char [] MAPPINGS = new char [] {
                                                       // 0x00
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | TSPECIAL,
                                                       CTL,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       // 0x10
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       CTL | NONTEXT,
                                                       // 0x20
                                                       TSPECIAL,
                                                       0,
                                                       TSPECIAL | NONQUOTEDTEXT,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       TSPECIAL | NONCOMMENT,
                                                       TSPECIAL | NONCOMMENT,
                                                       0,
                                                       0,
                                                       TSPECIAL,
                                                       0,
                                                       0,
                                                       TSPECIAL,
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
                                                       TSPECIAL,
                                                       TSPECIAL,
                                                       TSPECIAL,
                                                       TSPECIAL,
                                                       TSPECIAL,
                                                       TSPECIAL,
                                                       // 0x40
                                                       TSPECIAL,
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
                                                       TSPECIAL,
                                                       TSPECIAL,
                                                       TSPECIAL,
                                                       0,
                                                       0,
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
                                                       TSPECIAL,
                                                       0,
                                                       TSPECIAL,
                                                       0,
                                                       CTL | NONTEXT };

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

  public static boolean isUpperAlpha (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & UALPHA) == UALPHA;
  }

  public static boolean isLowerAlpha (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & LALPHA) == LALPHA;
  }

  public static boolean isAlpha (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & ALPHA) == ALPHA;
  }

  public static boolean isDigit (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & DIGIT) == DIGIT;
  }

  public static boolean isControl (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & CTL) == CTL;
  }

  public static boolean isCR (final int n)
  {
    return n == 13;
  }

  public static boolean isLF (final int n)
  {
    return n == 10;
  }

  public static boolean isSpace (final int n)
  {
    return n == 32;
  }

  public static boolean isTab (final int n)
  {
    return n == 9;
  }

  public static boolean isQuote (final int n)
  {
    return n == 34;
  }

  public static boolean isHex (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & HEX) == HEX;
  }

  public static boolean isTokenSpecial (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & TSPECIAL) == TSPECIAL;
  }

  public static boolean isText (final int n)
  {
    if (n < MIN_INDEX)
      return false;
    if (n > MAX_INDEX)
      return n < 256;
    return (MAPPINGS[n] & NONTEXT) == 0;
  }

  public static boolean isComment (final int n)
  {
    if (n < MIN_INDEX)
      return false;
    if (n > MAX_INDEX)
      return n < 256;
    final int nMapping = MAPPINGS[n];
    return (nMapping & NONTEXT) == 0 && (nMapping & NONCOMMENT) == 0;
  }

  public static boolean isQuotedText (final int n)
  {
    if (!isChar (n))
      return false;
    final int nMapping = MAPPINGS[n];
    return (nMapping & NONTEXT) == 0 && (nMapping & NONQUOTEDTEXT) == 0;
  }
}
