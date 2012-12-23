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
package com.phloc.webctrls.datatables.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.webbasics.ajax.AbstractAjaxHandler;
import com.phloc.webbasics.ajax.AjaxDefaultResponse;
import com.phloc.webctrls.datatables.CDataTables;

public class AjaxHandlerDataTables extends AbstractAjaxHandler
{
  private static final String DISPLAY_START = "iDisplayStart";
  private static final String DISPLAY_LENGTH = "iDisplayLength";
  private static final String COLUMNS = "iColumns";
  private static final String SEARCH = "sSearch";
  private static final String REGEX = "bRegEx";
  private static final String SEARCHABLE_PREFIX = "bSearchable_";
  private static final String SEARCH_PREFIX = "sSearch_";
  private static final String REGEX_PREFIX = "bRegEx_";
  private static final String SORTABLE_PREFIX = "bSortable_";
  private static final String SORTING_COLS = "iSortingCols";
  private static final String SORT_COL_PREFIX = "iSortCol_";
  private static final String SORT_DIR_PREFIX = "sSortDir_";
  private static final String DATA_PROP_PREFIX = "mDataProp_";
  private static final String ECHO = "sEcho";

  @Immutable
  public static final class RequestDataPerColumn
  {
    private final boolean m_bSearchable;
    private final String m_sSearch;
    private final boolean m_bRegEx;
    private final boolean m_bSortable;
    private final int m_nSortCol;
    private final ESortOrder m_eSortDir;
    private final String m_sDataProp;

    RequestDataPerColumn (final boolean bSearchable,
                          @Nullable final String sSearch,
                          final boolean bRegEx,
                          final boolean bSortable,
                          final int nSortCol,
                          @Nullable final ESortOrder eSortDir,
                          @Nullable final String sDataProp)
    {
      m_bSearchable = bSearchable;
      m_sSearch = sSearch;
      m_bRegEx = bRegEx;
      m_bSortable = bSortable;
      m_nSortCol = nSortCol;
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
     * @return Column being sorted on (you will need to decode this number for
     *         your database)
     */
    public int getSortCol ()
    {
      return m_nSortCol;
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
  }

  public static final class RequestData
  {
    private final int m_nDisplayStart;
    private final int m_nDisplayLength;
    private final String m_sSearch;
    private final boolean m_bRegEx;
    private final int m_nSortingCols;
    private final List <RequestDataPerColumn> m_aColumnData;
    private final int m_nEcho;

    RequestData (final int nDisplayStart,
                 final int nDisplayLength,
                 @Nullable final String sSearch,
                 final boolean bRegEx,
                 final int nSortingCols,
                 @Nonnull final List <RequestDataPerColumn> aColumnData,
                 final int nEcho)
    {
      m_nDisplayStart = nDisplayStart;
      m_nDisplayLength = nDisplayLength;
      m_sSearch = sSearch;
      m_bRegEx = bRegEx;
      m_nSortingCols = nSortingCols;
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
     *         It is expected that the number of records returned will be equal
     *         to this number, unless the server has fewer records to return.
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
     * @return True if the global filter should be treated as a regular
     *         expression for advanced filtering, false if not.
     */
    public boolean isRegEx ()
    {
      return m_bRegEx;
    }

    /**
     * @return Number of columns to sort on
     */
    public int getSortingCols ()
    {
      return m_nSortingCols;
    }

    @Nonnull
    public List <RequestDataPerColumn> getColumnData ()
    {
      return ContainerHelper.newList (m_aColumnData);
    }

    /**
     * @return Information for DataTables to use for rendering.
     */
    @Nonnull
    public String getEcho ()
    {
      return Integer.toString (m_nEcho);
    }
  }

  @Override
  @Nonnull
  protected AjaxDefaultResponse mainHandleRequest (@Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {
    final int nDisplayStart = aParams.getAttributeAsInt (DISPLAY_START, 0);
    final int nDisplayLength = aParams.getAttributeAsInt (DISPLAY_LENGTH, 0);
    final int nColumns = aParams.getAttributeAsInt (COLUMNS, 0);
    final String sSearch = aParams.getAttributeAsString (SEARCH);
    final boolean bRegEx = aParams.getAttributeAsBoolean (REGEX, false);
    final int nSortingCols = aParams.getAttributeAsInt (SORTING_COLS);
    final int nEcho = aParams.getAttributeAsInt (ECHO);
    final List <RequestDataPerColumn> aColumnData = new ArrayList <RequestDataPerColumn> (nColumns);
    for (int nColumn = 0; nColumn < nColumns; ++nColumn)
    {
      final boolean bCSearchable = aParams.getAttributeAsBoolean (SEARCHABLE_PREFIX + nColumn);
      final String sCSearch = aParams.getAttributeAsString (SEARCH_PREFIX + nColumn);
      final boolean bCRegEx = aParams.getAttributeAsBoolean (REGEX_PREFIX + nColumn);
      final boolean bCSortable = aParams.getAttributeAsBoolean (SORTABLE_PREFIX + nColumn);
      final int nCSortCol = aParams.getAttributeAsInt (SORT_COL_PREFIX + nColumn);
      final String sCSortDir = aParams.getAttributeAsString (SORT_DIR_PREFIX + nColumn);
      final ESortOrder eCSortDir = CDataTables.SORT_ASC.equals (sCSortDir)
                                                                          ? ESortOrder.ASCENDING
                                                                          : CDataTables.SORT_DESC.equals (sCSortDir)
                                                                                                                    ? ESortOrder.DESCENDING
                                                                                                                    : null;
      final String sCDataProp = aParams.getAttributeAsString (DATA_PROP_PREFIX + nColumn);
      aColumnData.add (new RequestDataPerColumn (bCSearchable,
                                                 sCSearch,
                                                 bCRegEx,
                                                 bCSortable,
                                                 nCSortCol,
                                                 eCSortDir,
                                                 sCDataProp));
    }
    new RequestData (nDisplayStart, nDisplayLength, sSearch, bRegEx, nSortingCols, aColumnData, nEcho);
    return AjaxDefaultResponse.createSuccess (null);
  }
}
