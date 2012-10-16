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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public class SelectFilterLike implements ISelectFilterLike
{
  private final String m_sColumn;
  private final String m_sFilterValue;

  public SelectFilterLike (@Nonnull @Nonempty final String sColumn, @Nonnull final String sFilterValue)
  {
    if (StringHelper.hasNoText (sColumn))
      throw new IllegalArgumentException ("column name may not be empty");
    if (sFilterValue == null)
      throw new NullPointerException ("filterValue");
    m_sColumn = sColumn;
    m_sFilterValue = sFilterValue.length () == 0 ? null : sFilterValue;
  }

  @Nonnull
  @Nonempty
  public String getColumn ()
  {
    return m_sColumn;
  }

  @Nonnull
  public ESelectFilterOperation getOperation ()
  {
    return ESelectFilterOperation.LIKE;
  }

  @Nullable
  public String getFilterValue ()
  {
    return m_sFilterValue;
  }

  @Nonnull
  public String getFilterValueSQL ()
  {
    return m_sFilterValue == null ? "%" : "%" + m_sFilterValue + "%";
  }

  @UnsupportedOperation
  public Object getFilterValueSQL2 ()
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SelectFilterLike))
      return false;
    final SelectFilterLike rhs = (SelectFilterLike) o;
    return m_sColumn.equals (rhs.m_sColumn) && m_sFilterValue.equals (rhs.m_sFilterValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sColumn).append (m_sFilterValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("column", m_sColumn).append ("filterValue", m_sFilterValue).toString ();
  }
}
