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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.compare.ReverseComparator;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.comparator.ComparatorTableString;

/**
 * The current sort state of a {@link DataTables} object.
 * 
 * @author philip
 */
final class ServerSortState
{
  private final RequestDataSortColumn [] m_aSortState;

  ServerSortState (@Nonnull final DataTablesServerData aServerData, @Nonnull final Locale aDisplayLocale)
  {
    this (aServerData, new RequestDataSortColumn [0], aDisplayLocale);
  }

  ServerSortState (@Nonnull final DataTablesServerData aServerData,
                   @Nonnull final RequestDataSortColumn [] aSortCols,
                   @Nonnull final Locale aDisplayLocale)
  {
    if (aServerData == null)
      throw new NullPointerException ("serverData");
    if (aSortCols == null)
      throw new NullPointerException ("sortCols");
    m_aSortState = aSortCols;

    // Extract the comparators for all sort columns
    for (final RequestDataSortColumn aSortColumn : aSortCols)
    {
      // Get the configured comparator
      Comparator <String> aStringComp = aServerData.getColumnComparator (aSortColumn.getColumnIndex ());
      if (aStringComp == null)
        aStringComp = new ComparatorTableString (aDisplayLocale);
      if (aSortColumn.getSortDirectionOrDefault ().isDescending ())
      {
        // Reverse the comparator
        aStringComp = ReverseComparator.create (aStringComp);
      }
      aSortColumn.setComparator (aStringComp);
    }
  }

  /**
   * @return Number of columns to sort on
   */
  @Nonnegative
  public int getSortingCols ()
  {
    return m_aSortState.length;
  }

  @Nonnull
  @ReturnsMutableCopy
  public RequestDataSortColumn [] getSortCols ()
  {
    return ArrayHelper.getCopy (m_aSortState);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ServerSortState))
      return false;
    final ServerSortState rhs = (ServerSortState) o;
    return Arrays.equals (m_aSortState, rhs.m_aSortState);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aSortState).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("sortCols", m_aSortState).toString ();
  }
}
