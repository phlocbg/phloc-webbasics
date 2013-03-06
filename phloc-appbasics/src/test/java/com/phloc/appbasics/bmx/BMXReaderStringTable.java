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

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.nio.CharBuffer;

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

  public void add (@Nonnull final CharBuffer aCB)
  {
    add (aCB.toString ());
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
  public String getString (final int nIndex)
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
