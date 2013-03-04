package com.phloc.appbasics.bmx;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

final class BMXReaderStringTable
{
  private final Map <Integer, String> aStringTable = new HashMap <Integer, String> ();
  private int nLastUsedStringTableIndex = 0;

  public BMXReaderStringTable ()
  {}

  public void add (@Nonnull final String s)
  {
    aStringTable.put (Integer.valueOf (++nLastUsedStringTableIndex), s);
  }

  @Nonnull
  public String get (final int nIndex)
  {
    final String ret = aStringTable.get (Integer.valueOf (nIndex));
    if (ret == null)
      throw new IllegalArgumentException ("Failed to resolve index " + nIndex);
    return ret;
  }
}
