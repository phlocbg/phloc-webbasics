package com.phloc.appbasics.bmx;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class BMXReaderStringTable
{
  private final boolean m_bReUseStrings;
  private final TIntObjectMap <String> m_aStringTable;
  private String m_sLastString;
  private int m_nLastUsedStringTableIndex = 0;

  public BMXReaderStringTable (final boolean bReUseStrings)
  {
    m_bReUseStrings = bReUseStrings;
    m_aStringTable = bReUseStrings ? new TIntObjectHashMap <String> (1000) : null;
  }

  public void add (@Nonnull final String s)
  {
    if (m_bReUseStrings)
    {
      m_aStringTable.put (++m_nLastUsedStringTableIndex, s);
    }
    else
    {
      if (m_sLastString != null)
        throw new NullPointerException ("Internal inconsistency");
      m_sLastString = s;
    }
  }

  @Nullable
  public String get (final int nIndex)
  {
    if (nIndex == CBMXIO.INDEX_NULL_STRING)
      return null;

    if (m_bReUseStrings)
    {
      final String ret = m_aStringTable.get (nIndex);
      if (ret == null)
        throw new IllegalArgumentException ("Failed to resolve index " + nIndex);
      return ret;
    }

    final String ret = m_sLastString;
    m_sLastString = null;
    return ret;
  }
}
