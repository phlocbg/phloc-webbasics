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

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
final class RequestDataPerColumn
{
  private final boolean m_bSearchable;
  private final String m_sSearch;
  private final boolean m_bRegEx;
  private final boolean m_bSortable;
  private final ESortOrder m_eSortDir;
  private final String m_sDataProp;

  RequestDataPerColumn (final boolean bSearchable,
                        @Nullable final String sSearch,
                        final boolean bRegEx,
                        final boolean bSortable,
                        @Nullable final ESortOrder eSortDir,
                        @Nullable final String sDataProp)
  {
    m_bSearchable = bSearchable;
    m_sSearch = sSearch;
    m_bRegEx = bRegEx;
    m_bSortable = bSortable;
    m_eSortDir = eSortDir;
    m_sDataProp = sDataProp;
  }

  /**
   * @return Indicator for if a column is flagged as searchable or not on the
   *         client-side
   */
  public boolean isSearchable ()
  {
    return m_bSearchable;
  }

  /**
   * @return Individual column filter
   */
  @Nullable
  public String getSearch ()
  {
    return m_sSearch;
  }

  /**
   * @return True if the individual column filter should be treated as a
   *         regular expression for advanced filtering, false if not
   */
  public boolean isRegEx ()
  {
    return m_bRegEx;
  }

  /**
   * @return Indicator for if a column is flagged as sortable or not on the
   *         client-side
   */
  public boolean isSortable ()
  {
    return m_bSortable;
  }

  /**
   * @return Direction to be sorted
   */
  @Nullable
  public ESortOrder getSortDir ()
  {
    return m_eSortDir;
  }

  /**
   * @return The value specified by mDataProp for each column. This can be
   *         useful for ensuring that the processing of data is independent
   *         from the order of the columns.
   */
  @Nullable
  public String getDataProp ()
  {
    return m_sDataProp;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("searchable", m_bSearchable)
                                       .append ("search", m_sSearch)
                                       .append ("regEx", m_bRegEx)
                                       .append ("sortable", m_bSortable)
                                       .append ("sortDir", m_eSortDir)
                                       .append ("dataProp", m_sDataProp)
                                       .toString ();
  }
}