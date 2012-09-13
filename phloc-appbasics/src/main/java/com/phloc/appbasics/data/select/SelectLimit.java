/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.appbasics.data.select;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public final class SelectLimit
{
  private final int m_nStartIndex;
  private final int m_nResultCount;

  public SelectLimit (@Nonnegative final int nStartIndex, @Nonnegative final int nResultCount)
  {
    if (nStartIndex < 0)
      throw new IllegalArgumentException ("Start index invalid: " + nStartIndex);
    if (nResultCount <= 0)
      throw new IllegalArgumentException ("Result count: " + nResultCount);
    m_nStartIndex = nStartIndex;
    m_nResultCount = nResultCount;
  }

  @Nonnegative
  public int getStartIndex ()
  {
    return m_nStartIndex;
  }

  @Nonnegative
  public int getResultCount ()
  {
    return m_nResultCount;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SelectLimit))
      return false;
    final SelectLimit rhs = (SelectLimit) o;
    return m_nStartIndex == rhs.m_nStartIndex && m_nResultCount == rhs.m_nResultCount;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_nStartIndex).append (m_nResultCount).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("startIndex", m_nStartIndex)
                                       .append ("resultCount", m_nResultCount)
                                       .toString ();
  }
}
