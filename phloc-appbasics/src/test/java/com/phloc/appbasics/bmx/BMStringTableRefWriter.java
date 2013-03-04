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

import java.io.DataOutput;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class BMStringTableRefWriter
{
  private final BMXWriterStringTable m_aST;
  private final DataOutput m_aDO;
  private final int m_nReferenceStorageByteCount;

  public BMStringTableRefWriter (@Nonnull final BMXWriterStringTable aST, @Nonnull final DataOutput aDO)
  {
    m_aST = aST;
    m_aDO = aDO;
    m_nReferenceStorageByteCount = aST.getReferenceStorageByteCount ();
  }

  public void writeStringRef (@Nullable final CharSequence aString) throws IOException
  {
    writeStringRef (aString == null ? null : aString.toString ());
  }

  public void writeStringRef (@Nullable final String sString) throws IOException
  {
    final int nIndex = m_aST.getIndex (sString);
    switch (m_nReferenceStorageByteCount)
    {
      case 1:
        m_aDO.writeByte (nIndex);
        break;
      case 2:
        m_aDO.writeShort (nIndex);
        break;
      case 4:
        m_aDO.writeInt (nIndex);
        break;
    }
  }
}
