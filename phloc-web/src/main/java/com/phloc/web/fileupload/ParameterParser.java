/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.web.fileupload;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.string.StringHelper;

/**
 * A simple parser intended to parse sequences of name/value pairs. Parameter
 * values are exptected to be enclosed in quotes if they contain unsafe
 * characters, such as '=' characters or separators. Parameter values are
 * optional and can be omitted.
 * <p>
 * <code>param1 = value; param2 = "anything goes; really"; param3</code>
 * </p>
 * 
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 */
public final class ParameterParser
{
  /**
   * String to be parsed.
   */
  private char [] m_aChars;

  /**
   * Current position in the string.
   */
  private int m_nPos = 0;

  /**
   * Maximum position in the string.
   */
  private int m_nLen = 0;

  /**
   * Start of a token.
   */
  private int m_nIndex1 = 0;

  /**
   * End of a token.
   */
  private int m_nIndex2 = 0;

  /**
   * Whether names stored in the map should be converted to lower case.
   */
  private boolean m_bLowerCaseNames = false;

  /**
   * Default ParameterParser constructor.
   */
  public ParameterParser ()
  {}

  /**
   * Are there any characters left to parse?
   * 
   * @return <code>true</code> if there are unparsed characters,
   *         <code>false</code> otherwise.
   */
  private boolean _hasChar ()
  {
    return this.m_nPos < this.m_nLen;
  }

  /**
   * A helper method to process the parsed token. This method removes leading
   * and trailing blanks as well as enclosing quotation marks, when necessary.
   * 
   * @param quoted
   *        <code>true</code> if quotation marks are expected,
   *        <code>false</code> otherwise.
   * @return the token
   */
  @Nullable
  private String _getToken (final boolean quoted)
  {
    // Trim leading white spaces
    while (this.m_nIndex1 < this.m_nIndex2 && Character.isWhitespace (this.m_aChars[this.m_nIndex1]))
    {
      this.m_nIndex1++;
    }
    // Trim trailing white spaces
    while (this.m_nIndex2 > this.m_nIndex1 && Character.isWhitespace (this.m_aChars[this.m_nIndex2 - 1]))
    {
      this.m_nIndex2--;
    }
    // Strip away quotation marks if necessary
    if (quoted)
    {
      if ((this.m_nIndex2 - this.m_nIndex1) >= 2 &&
          this.m_aChars[this.m_nIndex1] == '"' &&
          this.m_aChars[this.m_nIndex2 - 1] == '"')
      {
        this.m_nIndex1++;
        this.m_nIndex2--;
      }
    }
    String result = null;
    if (this.m_nIndex2 > this.m_nIndex1)
    {
      result = new String (this.m_aChars, this.m_nIndex1, this.m_nIndex2 - this.m_nIndex1);
    }
    return result;
  }

  /**
   * Parses out a token until any of the given terminators is encountered.
   * 
   * @param cTerminator1
   *        the first terminating character. Any when encountered signify the
   *        end of the token
   * @param cTerminator2
   *        the second terminating character. Any when encountered signify the
   *        end of the token
   * @return the token
   */
  @Nullable
  private String _parseToken (final char cTerminator1, final char cTerminator2)
  {
    char ch;
    this.m_nIndex1 = this.m_nPos;
    this.m_nIndex2 = this.m_nPos;
    while (_hasChar ())
    {
      ch = this.m_aChars[this.m_nPos];
      if (ch == cTerminator1 || ch == cTerminator2)
        break;
      this.m_nIndex2++;
      this.m_nPos++;
    }
    return _getToken (false);
  }

  /**
   * Parses out a token until any of the given terminators is encountered
   * outside the quotation marks.
   * 
   * @param cTerminator
   *        the terminating character. Any of these characters when encountered
   *        outside the quotation marks signify the end of the token
   * @return the token
   */
  @Nullable
  private String _parseQuotedToken (final char cTerminator)
  {
    char ch;
    this.m_nIndex1 = this.m_nPos;
    this.m_nIndex2 = this.m_nPos;
    boolean bQuoted = false;
    boolean bCharEscaped = false;
    while (_hasChar ())
    {
      ch = this.m_aChars[this.m_nPos];
      if (!bQuoted && cTerminator == ch)
        break;

      if (!bCharEscaped && ch == '"')
        bQuoted = !bQuoted;
      bCharEscaped = (!bCharEscaped && ch == '\\');
      this.m_nIndex2++;
      this.m_nPos++;
    }
    return _getToken (true);
  }

  /**
   * Returns <code>true</code> if parameter names are to be converted to lower
   * case when name/value pairs are parsed.
   * 
   * @return <code>true</code> if parameter names are to be converted to lower
   *         case when name/value pairs are parsed. Otherwise returns
   *         <code>false</code>
   */
  public boolean isLowerCaseNames ()
  {
    return this.m_bLowerCaseNames;
  }

  /**
   * Sets the flag if parameter names are to be converted to lower case when
   * name/value pairs are parsed.
   * 
   * @param b
   *        <code>true</code> if parameter names are to be converted to lower
   *        case when name/value pairs are parsed. <code>false</code> otherwise.
   */
  public void setLowerCaseNames (final boolean b)
  {
    this.m_bLowerCaseNames = b;
  }

  /**
   * Extracts a map of name/value pairs from the given string. Names are
   * expected to be unique. Multiple separators may be specified and the
   * earliest found in the input string is used.
   * 
   * @param sStr
   *        the string that contains a sequence of name/value pairs
   * @param aSeparators
   *        the name/value pairs separators
   * @return a map of name/value pairs
   */
  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> parse (@Nullable final String sStr, @Nullable final char [] aSeparators)
  {
    if (ArrayHelper.isEmpty (aSeparators))
      return new HashMap <String, String> ();

    char cSep = aSeparators[0];
    if (sStr != null)
    {
      // Find the first separator to use
      int idx = sStr.length ();
      for (final char cSep2 : aSeparators)
      {
        final int tmp = sStr.indexOf (cSep2);
        if (tmp != -1 && tmp < idx)
        {
          idx = tmp;
          cSep = cSep2;
        }
      }
    }
    return parse (sStr, cSep);
  }

  /**
   * Extracts a map of name/value pairs from the given string. Names are
   * expected to be unique.
   * 
   * @param str
   *        the string that contains a sequence of name/value pairs
   * @param separator
   *        the name/value pairs separator
   * @return a map of name/value pairs
   */
  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> parse (@Nullable final String str, final char separator)
  {
    final HashMap <String, String> params = new HashMap <String, String> ();
    if (str != null)
    {
      final char [] chars = str.toCharArray ();
      this.m_aChars = chars;
      this.m_nPos = 0;
      this.m_nLen = str.length ();

      String sParamName = null;
      String sParamValue = null;
      while (_hasChar ())
      {
        sParamName = _parseToken ('=', separator);
        sParamValue = null;
        if (_hasChar () && chars[this.m_nPos] == '=')
        {
          this.m_nPos++; // skip '='
          sParamValue = _parseQuotedToken (separator);
        }
        if (_hasChar () && chars[this.m_nPos] == separator)
        {
          this.m_nPos++; // skip separator
        }
        if (StringHelper.hasText (sParamName))
        {
          if (this.m_bLowerCaseNames)
            sParamName = sParamName.toLowerCase (Locale.US);
          params.put (sParamName, sParamValue);
        }
      }
    }
    return params;
  }
}
