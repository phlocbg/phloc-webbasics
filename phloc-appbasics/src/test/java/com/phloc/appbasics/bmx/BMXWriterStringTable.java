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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.string.ToStringGenerator;

final class BMXWriterStringTable
{
  private final DataOutputStream m_aDOS;
  private final Map <String, Integer> m_aStrings = new HashMap <String, Integer> (1000);
  private int m_nLastUsedIndex = CBMXIO.INDEX_NULL_STRING;

  public BMXWriterStringTable (@Nonnull final DataOutputStream aDOS)
  {
    m_aDOS = aDOS;
  }

  public int addString (@Nullable final CharSequence aString) throws IOException
  {
    if (aString == null)
      return CBMXIO.INDEX_NULL_STRING;

    return addString (aString.toString ());
  }

  private void _onNewString (@Nonnull final String sString) throws IOException
  {
    m_aDOS.writeByte (CBMXIO.NODETYPE_STRING);
    final byte [] aBytes = sString.getBytes (CBMXIO.ENCODING);
    m_aDOS.writeInt (aBytes.length);
    m_aDOS.write (aBytes, 0, aBytes.length);
  }

  public int addString (@Nullable final String sString) throws IOException
  {
    if (sString == null)
      return CBMXIO.INDEX_NULL_STRING;

    Integer aIndex = m_aStrings.get (sString);
    if (aIndex == null)
    {
      aIndex = Integer.valueOf (++m_nLastUsedIndex);
      m_aStrings.put (sString, aIndex);
      _onNewString (sString);
    }
    return aIndex.intValue ();
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
