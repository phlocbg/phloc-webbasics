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

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.charset.StringEncoder;
import com.phloc.commons.collections.trove.TroveUtils;
import com.phloc.commons.string.ToStringGenerator;

final class BMXWriterStringTable
{
  private final DataOutput m_aDOS;
  private final TObjectIntMap <String> m_aStrings;
  private int m_nLastUsedIndex = CBMXIO.INDEX_NULL_STRING;
  private final boolean m_bReUseStrings;
  private final StringEncoder s_aEncoder = new StringEncoder (CBMXIO.ENCODING);

  public BMXWriterStringTable (@Nonnull final DataOutput aDOS, final boolean bReUseStrings)
  {
    m_aDOS = aDOS;
    m_bReUseStrings = bReUseStrings;
    m_aStrings = bReUseStrings ? new TObjectIntHashMap <String> (1000) : null;
  }

  private void _onNewString (@Nonnull final String sString) throws IOException
  {
    m_aDOS.writeByte (CBMXIO.NODETYPE_STRING);
    final ByteBuffer aBytes = s_aEncoder.getAsNewByteBuffer (sString);
    m_aDOS.writeInt (aBytes.limit ());
    m_aDOS.write (aBytes.array (), aBytes.arrayOffset (), aBytes.limit ());
  }

  public int addString (@Nullable final CharSequence aString) throws IOException
  {
    if (aString == null)
      return CBMXIO.INDEX_NULL_STRING;

    return addString (aString.toString ());
  }

  @Nonnegative
  public int addString (@Nullable final String sString) throws IOException
  {
    if (sString == null)
      return CBMXIO.INDEX_NULL_STRING;

    if (m_bReUseStrings)
    {
      int nIndex = m_aStrings.get (sString);
      if (TroveUtils.isNotContained (nIndex))
      {
        nIndex = ++m_nLastUsedIndex;
        m_aStrings.put (sString, nIndex);
        _onNewString (sString);
      }
      return nIndex;
    }

    // Write String where it occurs!
    _onNewString (sString);
    return ++m_nLastUsedIndex;
  }

  @Nonnegative
  public int getStringCount ()
  {
    return m_aStrings.size ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("strings", m_aStrings).toString ();
  }
}
