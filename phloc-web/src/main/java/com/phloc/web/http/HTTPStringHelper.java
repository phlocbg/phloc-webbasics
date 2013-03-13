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
  private static final int CR = 0x0020;
  private static final int LF = 0x0040;
  private static final int SP = 0x0080;
  private static final int HTAB = 0x0100;
  private static final int QUOTE = 0x0200;

  private static final char [] MAPPINGS = new char [] {
                                                       // 0x00
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL | HTAB,
                                                       CTL | LF,
                                                       CTL,
                                                       CTL,
                                                       CTL | CR,
                                                       CTL,
                                                       CTL,
                                                       // 0x10
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       CTL,
                                                       // 0x20
                                                       SP,
                                                       0,
                                                       QUOTE,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       // 0x30
                                                       DIGIT,
                                                       DIGIT,
                                                       DIGIT,
                                                       DIGIT,
                                                       DIGIT,
                                                       DIGIT,
                                                       DIGIT,
                                                       DIGIT,
                                                       DIGIT,
                                                       DIGIT,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       // 0x40
                                                       0,
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
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       // 0x60
                                                       0,
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
                                                       0,
                                                       0,
                                                       0,
                                                       0,
                                                       CTL };

  static
  {
    if (MAPPINGS.length != MAX_INDEX + 1)
      throw new InitializationException ("MAPPING array is invalid!");
  }

  private HTTPStringHelper ()
  {}

  public static boolean isChar (final byte b)
  {
    return isChar (b & 0xff);
  }

  public static boolean isChar (final int n)
  {
    return n >= MIN_INDEX && n <= MAX_INDEX;
  }

  public static boolean isUpperAlpha (final byte b)
  {
    return isUpperAlpha (b & 0xff);
  }

  public static boolean isUpperAlpha (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & UALPHA) == UALPHA;
  }

  public static boolean isLowerAlpha (final byte b)
  {
    return isLowerAlpha (b & 0xff);
  }

  public static boolean isLowerAlpha (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & LALPHA) == LALPHA;
  }

  public static boolean isAlpha (final byte b)
  {
    return isAlpha (b & 0xff);
  }

  public static boolean isAlpha (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & ALPHA) == ALPHA;
  }

  public static boolean isDigit (final byte b)
  {
    return isDigit (b & 0xff);
  }

  public static boolean isDigit (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & DIGIT) == DIGIT;
  }

  public static boolean isControl (final byte b)
  {
    return isControl (b & 0xff);
  }

  public static boolean isControl (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & CTL) == CTL;
  }

  public static boolean isCR (final byte b)
  {
    return isCR (b & 0xff);
  }

  public static boolean isCR (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & CR) == CR;
  }

  public static boolean isLF (final byte b)
  {
    return isLF (b & 0xff);
  }

  public static boolean isLF (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & LF) == LF;
  }

  public static boolean isSpace (final byte b)
  {
    return isSpace (b & 0xff);
  }

  public static boolean isSpace (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & SP) == SP;
  }

  public static boolean isTab (final byte b)
  {
    return isTab (b & 0xff);
  }

  public static boolean isTab (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & HTAB) == HTAB;
  }

  public static boolean isQuote (final byte b)
  {
    return isQuote (b & 0xff);
  }

  public static boolean isQuote (final int n)
  {
    return isChar (n) && (MAPPINGS[n] & QUOTE) == QUOTE;
  }
}
