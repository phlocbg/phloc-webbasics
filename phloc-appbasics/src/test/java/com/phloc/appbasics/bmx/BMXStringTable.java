package com.phloc.appbasics.bmx;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.string.ToStringGenerator;

public class BMXStringTable
{
  private final Map <String, byte []> m_aWords = new HashMap <String, byte []> ();
  private int m_nLongest = 0;

  public BMXStringTable ()
  {}

  public void addString (@Nullable final String sWord)
  {
    if (sWord != null)
      if (!m_aWords.containsKey (sWord))
      {
        final byte [] aBytes = CharsetManager.getAsBytes (sWord, CCharset.CHARSET_UTF_8_OBJ);
        m_aWords.put (sWord, aBytes);
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
  public int getLongestWordByteCount ()
  {
    return m_nLongest;
  }

  /**
   * @return The number of bytes necessary to store the length of the longest
   *         entry.
   */
  @Nonnegative
  public int getLengthStorageByteCount ()
  {
    return 1 + (m_nLongest >> 8);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("words", m_aWords).append ("longest", m_nLongest).toString ();
  }
}
