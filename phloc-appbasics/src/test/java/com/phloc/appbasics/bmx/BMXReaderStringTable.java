package com.phloc.appbasics.bmx;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class BMXReaderStringTable
{
  private final TIntObjectMap <String> aStringTable = new TIntObjectHashMap <String> ();
  private int nLastUsedStringTableIndex = 0;

  public BMXReaderStringTable ()
  {}

  public void add (@Nonnull final String s)
  {
    aStringTable.put (++nLastUsedStringTableIndex, s);
  }

  @Nullable
  public String get (final int nIndex)
  {
    if (nIndex == CBMXIO.INDEX_NULL_STRING)
      return null;

    final String ret = aStringTable.get (nIndex);
    if (ret == null)
      throw new IllegalArgumentException ("Failed to resolve index " + nIndex);
    return ret;
  }
}
