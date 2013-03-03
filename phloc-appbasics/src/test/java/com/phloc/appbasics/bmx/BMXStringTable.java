package com.phloc.appbasics.bmx;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.string.ToStringGenerator;

public class BMXStringTable
{
  /** The storage encoding of all strings in this table */
  public static final Charset ENCODING = CCharset.CHARSET_UTF_8_OBJ;

  private final SortedMap <String, byte []> m_aStrings = new TreeMap <String, byte []> ();
  private int m_nLongest = 0;

  public BMXStringTable ()
  {}

  public void addString (@Nullable final String sWord)
  {
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

  @Nonnegative
  public int getLongestWordByteCount ()
  {
    return m_nLongest;
  }

  /**
   * @return The number of bytes necessary to store the length of the longest
   *         entry. A value between 1 and 4, as the length of byte[] can have at
   *         last 32 bits.
   */
  @Nonnegative
  public int getLengthStorageByteCount ()
  {
    return 1 + (m_nLongest >> 8);
  }

  @Nonnull
  @ReturnsMutableObject (reason = "speed")
  public Collection <byte []> getAllByteArrays ()
  {
    return m_aStrings.values ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("strings", m_aStrings).append ("longest", m_nLongest).toString ();
  }
}
