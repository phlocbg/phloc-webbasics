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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
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

  public static final class RequestData
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
      return m_aSortCols.length;
    }

    @Nonnull
    @ReturnsMutableCopy
    public int [] getSortCols ()
    {
      return ArrayHelper.newIntArray (m_aSortCols);
    }

    @Nonnull
    @ReturnsMutableCopy
    public List <RequestDataPerColumn> getColumnData ()
    {
      return ContainerHelper.newList (m_aColumnData);
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
                                         .append ("columns", m_aColumnData)
                                         .append ("echo", m_nEcho)
                                         .toString ();
    }
  }

  public static final class ResponseData
  {
    private final int m_nTotalRecords;
    private final int m_nTotalDisplayRecords;
    private final int m_nEcho;
    private final String m_sColumns;
    private final List <List <String>> m_aData;

    public ResponseData (final int nTotalRecords,
                         final int nTotalDisplayRecords,
                         final int nEcho,
                         @Nullable final String sColumns,
                         @Nonnull final List <List <String>> aData)
    {
      m_nTotalRecords = nTotalRecords;
      m_nTotalDisplayRecords = nTotalDisplayRecords;
      m_nEcho = nEcho;
      m_sColumns = sColumns;
      m_aData = aData;
    }

    /**
     * @return Total records, before filtering (i.e. the total number of records
     *         in the database)
     */
    public int getTotalRecords ()
    {
      return m_nTotalRecords;
    }

    /**
     * @return Total records, after filtering (i.e. the total number of records
     *         after filtering has been applied - not just the number of records
     *         being returned in this result set)
     */
    public int getTotalDisplayRecords ()
    {
      return m_nTotalDisplayRecords;
    }

    /**
     * @return An unaltered copy of sEcho sent from the client side. This
     *         parameter will change with each draw (it is basically a draw
     *         count) - so it is important that this is implemented.
     */
    public int getEcho ()
    {
      return m_nEcho;
    }

    /**
     * @return Optional - this is a string of column names, comma separated
     *         (used in combination with sName) which will allow DataTables to
     *         reorder data on the client-side if required for display. Note
     *         that the number of column names returned must exactly match the
     *         number of columns in the table. For a more flexible JSON format,
     *         please consider using mDataProp.
     */
    @Nullable
    public String getColumns ()
    {
      return m_sColumns;
    }

    /**
     * @return The data in a 2D array.
     */
    @Nonnull
    @ReturnsMutableCopy
    public List <List <String>> getData ()
    {
      return ContainerHelper.newList (m_aData);
    }

    @Nonnull
    public IJSONObject getAsJSON ()
    {
      final IJSONObject ret = new JSONObject ();
      ret.setIntegerProperty ("iTotalRecords", m_nTotalRecords);
      ret.setIntegerProperty ("iTotalDisplayRecords", m_nTotalDisplayRecords);
      ret.setStringProperty ("sEcho", Integer.toString (m_nEcho));
      if (StringHelper.hasText (m_sColumns))
        ret.setStringProperty ("sColumns", m_sColumns);
      ret.setListOfListProperty ("aaData", m_aData);
      return ret;
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("totalRecords", m_nTotalRecords)
                                         .append ("totalDisplayRecords", m_nTotalDisplayRecords)
                                         .append ("echo", m_nEcho)
                                         .append ("columns", m_sColumns)
                                         .append ("data", m_aData)
                                         .toString ();
    }
  }

  @Nonnull
  private ResponseData _handleRequest (@Nonnull final RequestData aRequestData)
  {
    return new ResponseData (0, 0, aRequestData.getEcho (), null, new ArrayList <List <String>> ());
  }

  @Override
  @Nonnull
  protected AjaxDefaultResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                   @Nonnull final MapBasedAttributeContainer aParams) throws Exception
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
      final String sCSortDir = aParams.getAttributeAsString (SORT_DIR_PREFIX + nColumn);
      final ESortOrder eCSortDir = CDataTables.SORT_ASC.equals (sCSortDir)
                                                                          ? ESortOrder.ASCENDING
                                                                          : CDataTables.SORT_DESC.equals (sCSortDir)
                                                                                                                    ? ESortOrder.DESCENDING
                                                                                                                    : null;
      final String sCDataProp = aParams.getAttributeAsString (DATA_PROP_PREFIX + nColumn);
      aColumnData.add (new RequestDataPerColumn (bCSearchable, sCSearch, bCRegEx, bCSortable, eCSortDir, sCDataProp));
    }
    final int [] aSortCols = new int [nSortingCols];
    for (int i = 0; i < nSortingCols; ++i)
    {
      final int nCSortCol = aParams.getAttributeAsInt (SORT_COL_PREFIX + i);
      aSortCols[i] = nCSortCol;
    }
    final RequestData aRequestData = new RequestData (nDisplayStart,
                                                      nDisplayLength,
                                                      sSearch,
                                                      bRegEx,
                                                      aSortCols,
                                                      aColumnData,
                                                      nEcho);
    // Main request handling
    final ResponseData aResponseData = _handleRequest (aRequestData);
    // Convert the response to JSON
    return AjaxDefaultResponse.createSuccess (aResponseData == null ? null : aResponseData.getAsJSON ());
  }
}
