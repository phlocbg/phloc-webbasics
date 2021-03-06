/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webctrls.datatables.ajax;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Encapsulates the request data to a single DataTables AJAX request
 * 
 * @author Philip Helger
 */
final class RequestData
{
  private final int m_nDisplayStart;
  private final int m_nDisplayLength;
  private final RequestDataSearch m_aSearch;
  private final List <RequestDataColumn> m_aColumnData;
  private final RequestDataSortColumn [] m_aSortColumns;
  private final int m_nEcho;

  RequestData (final int nDisplayStart,
               final int nDisplayLength,
               @Nullable final String sSearchText,
               final boolean bSearchRegEx,
               @Nonnull final List <RequestDataColumn> aColumnData,
               @Nonnull final RequestDataSortColumn [] aSortColumns,
               final int nEcho)
  {
    ValueEnforcer.notNull (aSortColumns, "SortColumns");
    ValueEnforcer.notNull (aColumnData, "ColumnData");
    if (ContainerHelper.containsAnyNullElement (aColumnData))
      throw new IllegalArgumentException ("ColumnData may not contain null elements");
    m_nDisplayStart = nDisplayStart;
    m_nDisplayLength = nDisplayLength;
    m_aSearch = new RequestDataSearch (sSearchText, bSearchRegEx);
    m_aColumnData = aColumnData;
    m_aSortColumns = aSortColumns;
    m_nEcho = nEcho;
  }

  /**
   * @return Display start point in the current data set.
   */
  public int getDisplayStart ()
  {
    return m_nDisplayStart;
  }

  /**
   * @return Number of records that the table can display in the current draw.
   *         It is expected that the number of records returned will be equal to
   *         this number, unless the server has fewer records to return. If this
   *         value is -1 it means show all entries.
   */
  public int getDisplayLength ()
  {
    return m_nDisplayLength;
  }

  /**
   * @return <code>true</code> if all entries should be shown,
   *         <code>false</code> if the number is limited
   */
  public boolean showAllEntries ()
  {
    return m_nDisplayLength == -1;
  }

  /**
   * @return Number of columns being displayed (useful for getting individual
   *         column search info)
   */
  public int getColumnCount ()
  {
    return m_aColumnData.size ();
  }

  /**
   * @return <code>true</code> if either the global search or any column search
   *         is active.
   */
  public boolean isSearchActive ()
  {
    // Global search text present?
    if (m_aSearch.hasSearchText ())
      return true;

    // Per-column search text present?
    for (final RequestDataColumn aColumn : m_aColumnData)
      if (aColumn.isSearchable () && aColumn.getSearch ().hasSearchText ())
        return true;

    return false;
  }

  /**
   * @return Global search field
   */
  @Nonnull
  public RequestDataSearch getSearch ()
  {
    return m_aSearch;
  }

  @Nonnull
  public RequestDataColumn getColumn (@Nonnegative final int nIndex)
  {
    return m_aColumnData.get (nIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <RequestDataColumn> getColumnData ()
  {
    return ContainerHelper.newList (m_aColumnData);
  }

  @Nonnull
  @ReturnsMutableCopy
  public RequestDataColumn [] getColumnDataArray ()
  {
    return ArrayHelper.newArray (m_aColumnData, RequestDataColumn.class);
  }

  /**
   * @return Number of columns to sort on. Always &ge; 0.
   */
  @Nonnegative
  public int getSortColumnCount ()
  {
    return m_aSortColumns.length;
  }

  @Nonnull
  @ReturnsMutableCopy
  public RequestDataSortColumn [] getSortColumnArray ()
  {
    return ArrayHelper.getCopy (m_aSortColumns);
  }

  /**
   * @return Information for DataTables to use for rendering.
   */
  public int getEcho ()
  {
    return m_nEcho;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("displayStart", m_nDisplayStart)
                                       .append ("displayLength", m_nDisplayLength)
                                       .append ("search", m_aSearch)
                                       .append ("sortCols", m_aSortColumns)
                                       .append ("columnData", m_aColumnData)
                                       .append ("echo", m_nEcho)
                                       .toString ();
  }
}
