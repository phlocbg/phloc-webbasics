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
package com.phloc.webctrls.datatables;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.js.builder.JSArray;

/**
 * Defines a DataTables sortable column
 * 
 * @author Philip Helger
 */
public final class DataTablesSortColumn
{
  private final int m_nIndex;
  private final ESortOrder m_eSortOrder;

  public DataTablesSortColumn (@Nonnegative final int nIndex, @Nonnull final ESortOrder eSortOrder)
  {
    if (nIndex < 0)
      throw new IllegalArgumentException ("index");
    if (eSortOrder == null)
      throw new NullPointerException ("sortOrder");
    m_nIndex = nIndex;
    m_eSortOrder = eSortOrder;
  }

  @Nonnegative
  public int getIndex ()
  {
    return m_nIndex;
  }

  @Nonnull
  public ESortOrder getSortOrder ()
  {
    return m_eSortOrder;
  }

  @Nonnull
  public JSArray getAsJS ()
  {
    final JSArray ret = new JSArray ();
    ret.add (m_nIndex);
    ret.add (m_eSortOrder.isAscending () ? CDataTables.SORT_ASC : CDataTables.SORT_DESC);
    return ret;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("index", m_nIndex).append ("sortOrder", m_eSortOrder).toString ();
  }
}
