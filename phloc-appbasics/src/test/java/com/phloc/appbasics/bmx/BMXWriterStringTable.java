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
package com.phloc.appbasics.bmx;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.string.ToStringGenerator;

public class BMXWriterStringTable
{
  /** The storage encoding of all strings in this table */
  public static final Charset ENCODING = CCharset.CHARSET_UTF_8_OBJ;

  /** The index for null strings */
  public static final int INDEX_NULL_STRING = 0;

  private final Map <String, byte []> m_aStrings = new HashMap <String, byte []> ();
  private Map <String, Integer> m_aIndexMap;
  private int m_nLongest = 0;
  private boolean m_bFinished = false;

  public BMXWriterStringTable ()
  {}

  public void addString (@Nullable final CharSequence aString)
  {
    if (aString != null)
      addString (aString.toString ());
  }

  public void addString (@Nullable final String sString)
  {
    if (m_bFinished)
      throw new IllegalStateException ("Already finished");
    if (sString != null)
      if (!m_aStrings.containsKey (sString))
      {
        final byte [] aBytes = CharsetManager.getAsBytes (sString, ENCODING);
        m_aStrings.put (sString, aBytes);
        m_nLongest = Math.max (m_nLongest, aBytes.length);
      }
  }

  public void addStrings (@Nullable final Iterable <String> aWords)
  {
    if (aWords != null)
      for (final String sWord : aWords)
        addString (sWord);
  }

  @Nonnegative
  public int getStringCount ()
  {
    return m_aStrings.size ();
  }

  /**
   * @return The number of bytes necessary to store the reference to an entry. A
   *         value between 1 and 4, as the length of m_aStrings can have at last
   *         32 bits (int).
   */
  @Nonnegative
  public int getReferenceStorageByteCount ()
  {
    final int n = getStringCount ();
    if (n > 0xffff)
      return 4;
    if (n > 0xff)
      return 2;
    return 1;
  }

  @Nonnegative
  public int getLongestWordByteCount ()
  {
    return m_nLongest;
  }

  /**
   * @return The number of bytes necessary to store the length of the longest
   *         entry. A value between 1 and 4, as the length of byte[] can have at
   *         last 32 bits (int).
   */
  @Nonnegative
  public int getLengthStorageByteCount ()
  {
    final int n = getLongestWordByteCount ();
    if (n > 0xffff)
      return 4;
    if (n > 0xff)
      return 2;
    return 1;
  }

  @Nonnull
  @ReturnsMutableObject (reason = "speed")
  public Collection <byte []> getAllByteArrays ()
  {
    return m_aStrings.values ();
  }

  @Nonnull
  public BMXWriterStringTable finish ()
  {
    if (m_bFinished)
      throw new IllegalStateException ("Already finished");

    m_bFinished = true;
    m_aIndexMap = new HashMap <String, Integer> (m_aStrings.size ());
    // Start with ID 1, as 0 is reserved for null string
    int nIndex = 1;
    for (final String sString : m_aStrings.keySet ())
      m_aIndexMap.put (sString, Integer.valueOf (nIndex++));
    if (m_aIndexMap.size () != m_aStrings.size ())
      throw new IllegalStateException ();
    return this;
  }

  @Nonnegative
  public int getIndex (@Nullable final String sString)
  {
    if (!m_bFinished)
      throw new IllegalStateException ("Not yet finished! Call finish first!");

    if (sString == null)
      return INDEX_NULL_STRING;

    final Integer aIndex = m_aIndexMap.get (sString);
    if (aIndex == null)
      throw new IllegalArgumentException ("Failed to resolve string in index map: '" + sString + "'");
    return aIndex.intValue ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("strings", m_aStrings).append ("longest", m_nLongest).toString ();
  }
}
