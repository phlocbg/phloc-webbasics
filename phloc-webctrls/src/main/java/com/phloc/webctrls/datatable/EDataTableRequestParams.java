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
package com.phloc.webctrls.datatable;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.name.IHasName;

public enum EDataTableRequestParams implements IHasName
{
  DISPLAY_LENGTH ("iDisplayLength"),
  DISPLAY_START ("iDisplayStart"),
  COLUMNS ("iColumns"),
  SEARCH ("sSearch"),
  REGEX ("bRegEx"),
  SEARCHABLE_PREFIX ("bSearchable_"),
  SEARCH_PREFIX ("sSearch_"),
  REGEX_PREFIX ("bRegEx_"),
  SORTABLE_PREFIX ("bSortable_"),
  SORTING_COLS ("iSortingCols"),
  SORT_COL_PREFIX ("iSortCol_"),
  SORT_COL_0 ("iSortCol_0"),
  SORT_COL_1 ("iSortCol_1"),
  SORT_COL_2 ("iSortCol_2"),
  SORT_COL_3 ("iSortCol_3"),
  SORT_COL_4 ("iSortCol_4"),
  SORT_DIR_PREFIX ("sSortDir_"),
  SORT_DIR_0 ("sSortDir_0"),
  SORT_DIR_1 ("sSortDir_1"),
  SORT_DIR_2 ("sSortDir_2"),
  SORT_DIR_3 ("sSortDir_3"),
  SORT_DIR_4 ("sSortDir_4"),
  DATA_PROP_PREFIX ("mDataProp_"),
  ECHO ("sEcho");

  private final String m_sName;

  private EDataTableRequestParams (@Nonnull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }
}
