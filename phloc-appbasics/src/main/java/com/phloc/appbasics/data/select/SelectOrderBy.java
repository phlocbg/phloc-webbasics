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
package com.phloc.appbasics.data.select;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents the sorting of a specific column, and the sort order
 * 
 * @author philip
 */
@Immutable
public final class SelectOrderBy
{
  private final String m_sColumn;
  private final ESortOrder m_eSortOrder;

  /**
   * Constructor
   * 
   * @param sColumn
   *        Column name. May neither be <code>null</code> nor empty.
   * @param eSortOrder
   *        Sort order. May not be <code>null</code>.
   */
  public SelectOrderBy (@Nonnull @Nonempty final String sColumn, @Nonnull final ESortOrder eSortOrder)
  {
    if (StringHelper.hasNoText (sColumn))
      throw new IllegalArgumentException ("column name may not be empty");
    if (eSortOrder == null)
      throw new NullPointerException ("sortOrder");
    m_sColumn = sColumn;
    m_eSortOrder = eSortOrder;
  }

  /**
   * @return The column name. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getColumn ()
  {
    return m_sColumn;
  }

  /**
   * @return The sort order. Neither <code>null</code>.
   */
  @Nonnull
  public ESortOrder getOrder ()
  {
    return m_eSortOrder;
  }

  /**
   * @return The default SQL sort order. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getOrderSQL ()
  {
    return m_eSortOrder.isAscending () ? "ASC" : "DESC";
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SelectOrderBy))
      return false;
    final SelectOrderBy rhs = (SelectOrderBy) o;
    return m_sColumn.equals (rhs.m_sColumn) && m_eSortOrder == rhs.m_eSortOrder;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sColumn).append (m_eSortOrder).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("column", m_sColumn).append ("sortOrder", m_eSortOrder).toString ();
  }
}
