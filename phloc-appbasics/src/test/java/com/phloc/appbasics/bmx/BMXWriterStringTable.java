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
import com.phloc.commons.string.ToStringGenerator;

public class BMXWriterStringTable
{
  /** The storage encoding of all strings in this table */
  public static final Charset ENCODING = CCharset.CHARSET_UTF_8_OBJ;

  /** The index for null strings */
  public static final int INDEX_NULL_STRING = 0;

  /**
   * Represents a single entry in the string table
   * 
   * @author philip
   */
  public static final class Entry
  {
    private final byte [] m_aBytes;
    private final int m_nIndex;

    public Entry (@Nonnull final byte [] aBytes, @Nonnegative final int nIndex)
    {
      m_aBytes = aBytes;
      m_nIndex = nIndex;
    }

    @Nonnull
    @ReturnsMutableObject (reason = "speed")
    public byte [] getBytes ()
    {
      return m_aBytes;
    }

    @Nonnegative
    public int getIndex ()
    {
      return m_nIndex;
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("byteCount", m_aBytes.length).append ("index", m_nIndex).toString ();
    }
  }

  private final Map <String, Entry> m_aStrings = new HashMap <String, Entry> ();
  private int m_nLongest = 0;
  private int m_nLastUsedIndex = INDEX_NULL_STRING;

  public BMXWriterStringTable ()
  {}

  public int addString (@Nullable final CharSequence aString)
  {
    if (aString == null)
      return INDEX_NULL_STRING;

    return addString (aString.toString ());
  }

  public int addString (@Nullable final String sString)
  {
    if (sString == null)
      return INDEX_NULL_STRING;

    Entry aEntry = m_aStrings.get (sString);
    if (aEntry == null)
    {
      final byte [] aBytes = sString.getBytes (ENCODING);
      aEntry = new Entry (aBytes, ++m_nLastUsedIndex);
      m_aStrings.put (sString, aEntry);
      m_nLongest = Math.max (m_nLongest, aBytes.length);
    }
    return aEntry.getIndex ();
  }

  @Nonnegative
  public int getStringCount ()
  {
    return m_aStrings.size ();
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
  public Collection <Entry> getAllEntries ()
  {
    return m_aStrings.values ();
  }

  @Nonnegative
  public int getIndex (@Nullable final String sString)
  {
    if (sString == null)
      return INDEX_NULL_STRING;

    final Entry aEntry = m_aStrings.get (sString);
    if (aEntry == null)
      throw new IllegalArgumentException ("Failed to resolve string in index map: '" + sString + "'");
    return aEntry.m_nIndex;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("strings", m_aStrings).append ("longest", m_nLongest).toString ();
  }
}
