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
package com.phloc.webctrls.datatables.ajax;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;

final class RequestData
{
  private final int m_nDisplayStart;
  private final int m_nDisplayLength;
  private final String m_sSearch;
  private final boolean m_bRegEx;
  private final int [] m_aSortCols;
  private final List <RequestDataPerColumn> m_aColumnData;
  private final int m_nEcho;

  RequestData (final int nDisplayStart,
               final int nDisplayLength,
               @Nullable final String sSearch,
               final boolean bRegEx,
               @Nonnull final int [] aSortCols,
               @Nonnull final List <RequestDataPerColumn> aColumnData,
               final int nEcho)
  {
    if (aSortCols == null)
      throw new NullPointerException ("sortCols");
    if (aColumnData == null)
      throw new NullPointerException ("columnData");
    if (ContainerHelper.containsAnyNullElement (aColumnData))
      throw new IllegalArgumentException ("ColumnData may not contain null elements");
    m_nDisplayStart = nDisplayStart;
    m_nDisplayLength = nDisplayLength;
    m_sSearch = sSearch;
    m_bRegEx = bRegEx;
    m_aSortCols = aSortCols;
    m_aColumnData = aColumnData;
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
   *         this number, unless the server has fewer records to return.
   */
  public int getDisplayEnd ()
  {
    return m_nDisplayLength;
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
   * @return Global search field
   */
  @Nullable
  public String getSearch ()
  {
    return m_sSearch;
  }

  /**
   * @return True if the global filter should be treated as a regular expression
   *         for advanced filtering, false if not.
   */
  public boolean isRegEx ()
  {
    return m_bRegEx;
  }

  /**
   * @return Number of columns to sort on. Always &ge; 0.
   */
  @Nonnegative
  public int getSortingCols ()
  {
    return m_aSortCols.length;
  }

  @Nonnull
  @ReturnsMutableCopy
  public int [] getSortCols ()
  {
    return ArrayHelper.newIntArray (m_aSortCols);
  }

  @Nonnull
  public RequestDataPerColumn getColumn (@Nonnegative final int nIndex)
  {
    return m_aColumnData.get (nIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <RequestDataPerColumn> getColumnData ()
  {
    return ContainerHelper.newList (m_aColumnData);
  }

  @Nonnull
  @ReturnsMutableCopy
  public RequestDataPerColumn [] getColumnDataArray ()
  {
    return ArrayHelper.newArray (m_aColumnData, RequestDataPerColumn.class);
  }

  /**
   * @return Information for DataTables to use for rendering.
   */
  @Nonnull
  public int getEcho ()
  {
    return m_nEcho;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("displayStart", m_nDisplayStart)
                                       .append ("displayLength", m_nDisplayLength)
                                       .append ("search", m_sSearch)
                                       .append ("regEx", m_bRegEx)
                                       .append ("sortCols", m_aSortCols)
                                       .append ("columnData", m_aColumnData)
                                       .append ("echo", m_nEcho)
                                       .toString ();
  }
}
