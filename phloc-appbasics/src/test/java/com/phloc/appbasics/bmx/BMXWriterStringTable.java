package com.phloc.appbasics.bmx;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

  private final SortedMap <String, byte []> m_aStrings = new TreeMap <String, byte []> ();
  private Map <String, Integer> m_aIndexMap;
  private int m_nLongest = 0;
  private boolean m_bFinished = false;

  public BMXWriterStringTable ()
  {}

  public void addString (@Nullable final String sWord)
  {
    if (m_bFinished)
      throw new IllegalStateException ("Already finished");
    if (sWord != null)
      if (!m_aStrings.containsKey (sWord))
      {
        final byte [] aBytes = CharsetManager.getAsBytes (sWord, ENCODING);
        m_aStrings.put (sWord, aBytes);
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
    return 1 + (getStringCount () >> 8);
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
    return 1 + (getLongestWordByteCount () >> 8);
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
