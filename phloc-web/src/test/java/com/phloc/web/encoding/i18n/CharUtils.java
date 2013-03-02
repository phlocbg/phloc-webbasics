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
package com.phloc.web.encoding.i18n;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.regex.RegExHelper;

/**
 * General utilities for dealing with Unicode characters
 * 
 * @author Apache Abdera
 */
@Immutable
public final class CharUtils
{
  private CharUtils ()
  {}

  /**
   * True if all the characters in chars are within the set [low,high]
   */
  public static boolean inRange (final char [] chars, final char low, final char high)
  {
    for (final char c : chars)
      if (c < low || c > high)
        return false;
    return true;
  }

  /**
   * True if all the characters in chars are within the set [low,high]
   */
  public static boolean inRange (final char [] chars, final int low, final int high)
  {
    for (int i = 0; i < chars.length; i++)
    {
      final char n = chars[i];
      final int c = Character.isHighSurrogate (n) && i + 1 < chars.length && Character.isLowSurrogate (chars[i + 1]) ? Character.toCodePoint (n,
                                                                                                                                              chars[i++])
                                                                                                                    : (int) n;
      if (c < low || c > high)
        return false;
    }
    return true;
  }

  /**
   * True if the codepoint is within the set [low,high]
   */
  public static boolean inRange (final int codepoint, final int low, final int high)
  {
    return codepoint >= low && codepoint <= high;
  }

  /**
   * Get the high surrogate for a particular unicode codepoint
   */
  public static char getHighSurrogate (final int c)
  {
    return Character.isSupplementaryCodePoint (c) ? (char) ((Character.MIN_HIGH_SURROGATE - (Character.MIN_SUPPLEMENTARY_CODE_POINT >> 10)) + (c >> 10))
                                                 : 0;
  }

  /**
   * Get the low surrogate for a particular unicode codepoint
   */
  public static char getLowSurrogate (final int c)
  {
    return Character.isSupplementaryCodePoint (c) ? (char) (0xDC00 + (c & 0x3FF)) : (char) c;
  }

  /**
   * Return the codepoint at the given location, automatically dealing with
   * surrogate pairs
   */
  @Nonnull
  public static Codepoint codepointAt (@Nonnull final CharSequence s, final int i)
  {
    final char c = s.charAt (i);
    if (c < Character.MIN_HIGH_SURROGATE || c > Character.MAX_LOW_SURROGATE)
      return new Codepoint (c);
    if (Character.isHighSurrogate (c))
    {
      if (s.length () != i)
      {
        final char low = s.charAt (i + 1);
        if (Character.isLowSurrogate (low))
          return new Codepoint (c, low);
      }
    }
    else
      if (Character.isLowSurrogate (c))
      {
        if (i >= 1)
        {
          final char high = s.charAt (i - 1);
          if (Character.isHighSurrogate (high))
            return new Codepoint (high, c);
        }
      }
    return new Codepoint (c);
  }

  /**
   * Insert a codepoint into the buffer, automatically dealing with surrogate
   * pairs
   */
  public static void insert (final CharSequence s, final int i, @Nonnull final Codepoint c)
  {
    insert (s, i, c.getValue ());
  }

  /**
   * Insert a codepoint into the buffer, automatically dealing with surrogate
   * pairs
   */
  public static void insert (@Nonnull final CharSequence s, final int i, final int c)
  {
    if (!(s instanceof StringBuilder) && !(s instanceof StringBuffer))
    {
      insert (new StringBuilder (s), i, c);
    }
    else
    {
      int nI = i;
      if (nI > 0 && nI < s.length ())
      {
        final char ch = s.charAt (nI);
        final boolean low = Character.isLowSurrogate (ch);
        if (low && Character.isHighSurrogate (s.charAt (nI - 1)))
        {
          nI--;
        }
      }
      if (s instanceof StringBuffer)
        ((StringBuffer) s).insert (nI, getAsCharArray (c));
      else
        ((StringBuilder) s).insert (nI, getAsCharArray (c));
    }
  }

  /**
   * Set the character at a given location, automatically dealing with surrogate
   * pairs
   */
  public static void setChar (@Nonnull final CharSequence s, final int i, @Nonnull final Codepoint c)
  {
    setChar (s, i, c.getValue ());
  }

  /**
   * Set the character at a given location, automatically dealing with surrogate
   * pairs
   */
  public static void setChar (@Nonnull final CharSequence s, final int i, final int c)
  {
    if (!(s instanceof StringBuilder) && !(s instanceof StringBuffer))
    {
      setChar (new StringBuilder (s), i, c);
    }
    else
    {
      int l = 1;
      int nI = i;
      final char ch = s.charAt (nI);
      final boolean high = Character.isHighSurrogate (ch);
      final boolean low = Character.isLowSurrogate (ch);
      if (high || low)
      {
        if (high && (nI + 1) < s.length () && Character.isLowSurrogate (s.charAt (nI + 1)))
          l++;
        else
        {
          if (low && nI > 0 && Character.isHighSurrogate (s.charAt (nI - 1)))
          {
            nI--;
            l++;
          }
        }
      }
      if (s instanceof StringBuffer)
        ((StringBuffer) s).replace (nI, nI + l, getAsString (c));
      else
        ((StringBuilder) s).replace (nI, nI + l, getAsString (c));
    }
  }

  /**
   * Return the total number of codepoints in the buffer. Each surrogate pair
   * counts as a single codepoint
   */
  public static int length (final CharSequence c)
  {
    return _length (AbstractCodepointIterator.forCharSequence (c));
  }

  /**
   * Return the total number of codepoints in the buffer. Each surrogate pair
   * counts as a single codepoint
   */
  public static int length (final char [] c)
  {
    return _length (AbstractCodepointIterator.forCharArray (c));
  }

  private static int _length (@Nonnull final AbstractCodepointIterator ci)
  {
    int n = 0;
    while (ci.hasNext ())
    {
      ci.next ();
      n++;
    }
    return n;
  }

  /**
   * Return the char[] representation of the codepoint, automatically dealing
   * with surrogate pairs
   */
  @Nonnull
  @Nonempty
  public static char [] getAsCharArray (final int c)
  {
    return Character.toChars (c);
  }

  /**
   * Return the String representation of the codepoint, automatically dealing
   * with surrogate pairs
   */
  @Nonnull
  @Nonempty
  public static String getAsString (final int c)
  {
    return new String (getAsCharArray (c));
  }

  // Left-to-right embedding
  public static final char LRE = 0x202A;
  // Right-to-left embedding
  public static final char RLE = 0x202B;
  // Left-to-right override
  public static final char LRO = 0x202D;
  // Right-to-left override
  public static final char RLO = 0x202E;
  // Left-to-right mark
  public static final char LRM = 0x200E;
  // Right-to-left mark
  public static final char RLM = 0x200F;
  // Pop directional formatting
  public static final char PDF = 0x202C;

  /**
   * Removes leading and trailing bidi controls from the string
   */
  @Nullable
  public static String stripBidi (@Nullable final String s)
  {
    if (s == null || s.length () <= 1)
      return s;

    String ret = s;
    if (isBidi (ret.charAt (0)))
      ret = ret.substring (1);
    if (isBidi (ret.charAt (ret.length () - 1)))
      ret = ret.substring (0, ret.length () - 1);
    return ret;
  }

  /**
   * Removes bidi controls from within a string
   */
  public static String stripBidiInternal (@Nonnull final String s)
  {
    return RegExHelper.stringReplacePattern ("[\u202A\u202B\u202D\u202E\u200E\u200F\u202C]", s, "");
  }

  @Nonnull
  private static String _wrap (final String s, final char c1, final char c2)
  {
    final StringBuilder buf = new StringBuilder (s);
    if (buf.length () > 1)
    {
      if (buf.charAt (0) != c1)
        buf.insert (0, c1);
      if (buf.charAt (buf.length () - 1) != c2)
        buf.append (c2);
    }
    return buf.toString ();
  }

  /**
   * Wrap the string with the specified bidi control
   */
  public static String wrapBidi (@Nonnull final String s, final char c)
  {
    switch (c)
    {
      case RLE:
        return _wrap (s, RLE, PDF);
      case RLO:
        return _wrap (s, RLO, PDF);
      case LRE:
        return _wrap (s, LRE, PDF);
      case LRO:
        return _wrap (s, LRO, PDF);
      case RLM:
        return _wrap (s, RLM, RLM);
      case LRM:
        return _wrap (s, LRM, LRM);
      default:
        return s;
    }
  }

  /**
   * True if the codepoint is a digit
   */
  public static boolean isDigit (@Nonnull final Codepoint codepoint)
  {
    return isDigit (codepoint.getValue ());
  }

  /**
   * True if the codepoint is a digit
   */
  public static boolean isDigit (final int codepoint)
  {
    return Character.isDigit (codepoint);
  }

  /**
   * True if the codepoint is part of the ASCII alphabet (a-z, A-Z)
   */
  public static boolean isAlpha (@Nonnull final Codepoint codepoint)
  {
    return isAlpha (codepoint.getValue ());
  }

  /**
   * True if the codepoint is part of the ASCII alphabet (a-z, A-Z)
   */
  public static boolean isAlpha (final int codepoint)
  {
    return Character.isLetter (codepoint);
  }

  /**
   * True if isAlpha and isDigit both return true
   */
  public static boolean isAlphaDigit (@Nonnull final Codepoint codepoint)
  {
    return isAlphaDigit (codepoint.getValue ());
  }

  /**
   * True if isAlpha and isDigit both return true
   */
  public static boolean isAlphaDigit (final int codepoint)
  {
    return Character.isLetterOrDigit (codepoint);
  }

  public static boolean isHex (final int codepoint)
  {
    return isDigit (codepoint) || inRange (codepoint, 'a', 'f') || inRange (codepoint, 'A', 'F');
  }

  /**
   * True if the codepoint is a bidi control character
   */
  public static boolean isBidi (@Nonnull final Codepoint codepoint)
  {
    return isBidi (codepoint.getValue ());
  }

  /**
   * True if the codepoint is a bidi control character
   */
  public static boolean isBidi (final int codepoint)
  {
    return codepoint == LRM ||
           codepoint == RLM ||
           codepoint == LRE ||
           codepoint == RLE ||
           codepoint == LRO ||
           codepoint == RLO ||
           codepoint == PDF;
  }

  @CheckForSigned
  public static int get_index (@Nonnull final int [] set, final int value)
  {
    int s = 0, e = set.length;
    while (e - s > 8)
    {
      final int i = (e + s) >> 1;
      s = set[i] <= value ? i : s;
      e = set[i] > value ? i : e;
    }
    while (s < e)
    {
      if (value < set[s])
        break;
      s++;
    }
    return s == e ? -1 : s - 1;
  }

  /**
   * Treats the specified int array as an Inversion Set and returns true if the
   * value is located within the set. This will only work correctly if the
   * values in the int array are monotonically increasing
   */
  public static boolean invset_contains (@Nonnull final int [] set, final int value)
  {
    int s = 0, e = set.length;
    while (e - s > 8)
    {
      final int i = (e + s) >> 1;
      s = set[i] <= value ? i : s;
      e = set[i] > value ? i : e;
    }
    while (s < e)
    {
      if (value < set[s])
        break;
      s++;
    }
    return ((s - 1) & 1) == 0;
  }

  public static boolean isPctEnc (final int codepoint)
  {
    return codepoint == '%' || isDigit (codepoint) || inRange (codepoint, 'A', 'F') || inRange (codepoint, 'a', 'f');
  }

  public static boolean isMark (final int codepoint)
  {
    return codepoint == '-' ||
           codepoint == '_' ||
           codepoint == '.' ||
           codepoint == '!' ||
           codepoint == '~' ||
           codepoint == '*' ||
           codepoint == '\\' ||
           codepoint == '\'' ||
           codepoint == '(' ||
           codepoint == ')';
  }

  public static boolean isUnreserved (final int codepoint)
  {
    return isAlphaDigit (codepoint) || codepoint == '-' || codepoint == '.' || codepoint == '_' || codepoint == '~';
  }

  public static boolean isReserved (final int codepoint)
  {
    return codepoint == '$' ||
           codepoint == '&' ||
           codepoint == '+' ||
           codepoint == ',' ||
           codepoint == '/' ||
           codepoint == ':' ||
           codepoint == ';' ||
           codepoint == '=' ||
           codepoint == '?' ||
           codepoint == '@' ||
           codepoint == '[' ||
           codepoint == ']';
  }

  public static boolean isGenDelim (final int codepoint)
  {
    return codepoint == '#' ||
           codepoint == '/' ||
           codepoint == ':' ||
           codepoint == '?' ||
           codepoint == '@' ||
           codepoint == '[' ||
           codepoint == ']';
  }

  public static boolean isSubDelim (final int codepoint)
  {
    return codepoint == '!' ||
           codepoint == '$' ||
           codepoint == '&' ||
           codepoint == '\'' ||
           codepoint == '(' ||
           codepoint == ')' ||
           codepoint == '*' ||
           codepoint == '+' ||
           codepoint == ',' ||
           codepoint == ';' ||
           codepoint == '=' ||
           codepoint == '\\';
  }

  public static boolean isPchar (final int codepoint)
  {
    return isUnreserved (codepoint) ||
           codepoint == ':' ||
           codepoint == '@' ||
           codepoint == '&' ||
           codepoint == '=' ||
           codepoint == '+' ||
           codepoint == '$' ||
           codepoint == ',';
  }

  public static boolean isPath (final int codepoint)
  {
    return isPchar (codepoint) || codepoint == ';' || codepoint == '/' || codepoint == '%' || codepoint == ',';
  }

  public static boolean isPathNoDelims (final int codepoint)
  {
    return isPath (codepoint) && !isGenDelim (codepoint);
  }

  public static boolean isScheme (final int codepoint)
  {
    return isAlphaDigit (codepoint) || codepoint == '+' || codepoint == '-' || codepoint == '.';
  }

  public static boolean isUserInfo (final int codepoint)
  {
    return isUnreserved (codepoint) || isSubDelim (codepoint) || isPctEnc (codepoint);
  }

  public static boolean isQuery (final int codepoint)
  {
    return isPchar (codepoint) || codepoint == ';' || codepoint == '/' || codepoint == '?' || codepoint == '%';
  }

  public static boolean isFragment (final int codepoint)
  {
    return isPchar (codepoint) || codepoint == '/' || codepoint == '?' || codepoint == '%';
  }

  public static boolean is_ucschar (final int codepoint)
  {
    return inRange (codepoint, '\u00A0', '\uD7FF') ||
           inRange (codepoint, '\uF900', '\uFDCF') ||
           inRange (codepoint, '\uFDF0', '\uFFEF') ||
           inRange (codepoint, 0x10000, 0x1FFFD) ||
           inRange (codepoint, 0x20000, 0x2FFFD) ||
           inRange (codepoint, 0x30000, 0x3FFFD) ||
           inRange (codepoint, 0x40000, 0x4FFFD) ||
           inRange (codepoint, 0x50000, 0x5FFFD) ||
           inRange (codepoint, 0x60000, 0x6FFFD) ||
           inRange (codepoint, 0x70000, 0x7FFFD) ||
           inRange (codepoint, 0x80000, 0x8FFFD) ||
           inRange (codepoint, 0x90000, 0x9FFFD) ||
           inRange (codepoint, 0xA0000, 0xAFFFD) ||
           inRange (codepoint, 0xB0000, 0xBFFFD) ||
           inRange (codepoint, 0xC0000, 0xCFFFD) ||
           inRange (codepoint, 0xD0000, 0xDFFFD) ||
           inRange (codepoint, 0xE1000, 0xEFFFD);
  }

  public static boolean is_iprivate (final int codepoint)
  {
    return inRange (codepoint, '\uE000', '\uF8FF') ||
           inRange (codepoint, 0xF0000, 0xFFFFD) ||
           inRange (codepoint, 0x100000, 0x10FFFD);
  }

  public static boolean is_iunreserved (final int codepoint)
  {
    return isAlphaDigit (codepoint) || isMark (codepoint) || is_ucschar (codepoint);
  }

  public static boolean is_ipchar (final int codepoint)
  {
    return is_iunreserved (codepoint) ||
           isSubDelim (codepoint) ||
           codepoint == ':' ||
           codepoint == '@' ||
           codepoint == '&' ||
           codepoint == '=' ||
           codepoint == '+' ||
           codepoint == '$';
  }

  public static boolean is_ipath (final int codepoint)
  {
    return is_ipchar (codepoint) || codepoint == ';' || codepoint == '/' || codepoint == '%' || codepoint == ',';
  }

  public static boolean is_ipathnodelims (final int codepoint)
  {
    return is_ipath (codepoint) && !isGenDelim (codepoint);
  }

  public static boolean is_iquery (final int codepoint)
  {
    return is_ipchar (codepoint) ||
           is_iprivate (codepoint) ||
           codepoint == ';' ||
           codepoint == '/' ||
           codepoint == '?' ||
           codepoint == '%';
  }

  public static boolean is_ifragment (final int codepoint)
  {
    return is_ipchar (codepoint) || is_iprivate (codepoint) || codepoint == '/' || codepoint == '?' || codepoint == '%';
  }

  public static boolean is_iregname (final int codepoint)
  {
    return is_iunreserved (codepoint) ||
           codepoint == '!' ||
           codepoint == '$' ||
           codepoint == '&' ||
           codepoint == '\'' ||
           codepoint == '(' ||
           codepoint == ')' ||
           codepoint == '*' ||
           codepoint == '+' ||
           codepoint == ',' ||
           codepoint == ';' ||
           codepoint == '=' ||
           codepoint == '"';
  }

  public static boolean is_ipliteral (final int codepoint)
  {
    return isHex (codepoint) || codepoint == ':' || codepoint == '[' || codepoint == ']';
  }

  public static boolean is_ihost (final int codepoint)
  {
    return is_iregname (codepoint) || is_ipliteral (codepoint);
  }

  public static boolean is_regname (final int codepoint)
  {
    return isUnreserved (codepoint) ||
           codepoint == '!' ||
           codepoint == '$' ||
           codepoint == '&' ||
           codepoint == '\'' ||
           codepoint == '(' ||
           codepoint == ')' ||
           codepoint == '*' ||
           codepoint == '+' ||
           codepoint == ',' ||
           codepoint == ';' ||
           codepoint == '=' ||
           codepoint == '"';
  }

  public static boolean is_iuserinfo (final int codepoint)
  {
    return is_iunreserved (codepoint) ||
           codepoint == ';' ||
           codepoint == ':' ||
           codepoint == '&' ||
           codepoint == '=' ||
           codepoint == '+' ||
           codepoint == '$' ||
           codepoint == ',';
  }

  public static boolean is_iserver (final int codepoint)
  {
    return is_iuserinfo (codepoint) ||
           is_iregname (codepoint) ||
           isAlphaDigit (codepoint) ||
           codepoint == '.' ||
           codepoint == ':' ||
           codepoint == '@' ||
           codepoint == '[' ||
           codepoint == ']' ||
           codepoint == '%' ||
           codepoint == '-';
  }

  /**
   * Verifies a sequence of codepoints using the specified filter
   */
  public static void verify (final AbstractCodepointIterator ci, final ICodepointFilter filter)
  {
    final AbstractCodepointIterator rci = AbstractCodepointIterator.restrict (ci, filter);
    while (rci.hasNext ())
      rci.next ();
  }

  /**
   * Verifies a sequence of codepoints using the specified filter
   */
  public static void verify (final AbstractCodepointIterator ci, @Nonnull final ECharProfile profile)
  {
    verify (ci, profile.getFilter ());
  }

  /**
   * Verifies a sequence of codepoints using the specified profile
   */
  public static void verify (@Nullable final char [] s, @Nonnull final ECharProfile profile)
  {
    if (s == null)
      return;
    verify (AbstractCodepointIterator.forCharArray (s), profile);
  }

  /**
   * Verifies a sequence of codepoints using the specified profile
   */
  public static void verify (@Nullable final String s, @Nonnull final ECharProfile profile)
  {
    if (s == null)
      return;
    verify (AbstractCodepointIterator.forCharSequence (s), profile);
  }

  /**
   * Verifies a sequence of codepoints using the specified filter
   */
  public static void verifyNot (final AbstractCodepointIterator ci, final ICodepointFilter filter)
  {
    final AbstractCodepointIterator rci = ci.restrict (filter, false, true);
    while (rci.hasNext ())
      rci.next ();
  }

  /**
   * Verifies a sequence of codepoints using the specified profile
   */
  public static void verifyNot (final AbstractCodepointIterator ci, final ECharProfile profile)
  {
    final AbstractCodepointIterator rci = ci.restrict (profile.getFilter (), false, true);
    while (rci.hasNext ())
      rci.next ();
  }

  /**
   * Verifies a sequence of codepoints using the specified profile
   */
  public static void verifyNot (final char [] array, final ECharProfile profile)
  {
    final AbstractCodepointIterator rci = AbstractCodepointIterator.forCharArray (array)
                                                                   .restrict (profile.getFilter (), false, true);
    while (rci.hasNext ())
      rci.next ();
  }
}
