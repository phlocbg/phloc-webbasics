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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.ComparatorAsString;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.CHCParam;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.ajax.AbstractAjaxHandler;
import com.phloc.webbasics.ajax.AjaxDefaultResponse;
import com.phloc.webbasics.ajax.IAjaxResponse;
import com.phloc.webbasics.state.UIStateRegistry;
import com.phloc.webctrls.datatables.CDataTables;
import com.phloc.webctrls.datatables.ajax.DataTablesServerData.CellData;
import com.phloc.webctrls.datatables.ajax.DataTablesServerData.RowData;

public class AjaxHandlerDataTables extends AbstractAjaxHandler
{
  // This parameter must be passed to identify the table from the
  // UIStateRegistry!
  public static final String OBJECT_ID = CHCParam.PARAM_OBJECT;
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

  private static final String DT_ROW_ID = "DT_RowId";
  private static final String DT_ROW_CLASS = "DT_RowClass";

  @Nonnull
  private ResponseData _handleRequest (@Nonnull final RequestData aRequestData,
                                       @Nonnull final DataTablesServerData aDataTables)
  {
    final Locale aDisplayLocale = aDataTables.getDisplayLocale ();

    // Sorting
    {
      final ServerSortState aOldServerSortState = aDataTables.getServerSortState ();
      final ServerSortState aNewServerSortState = new ServerSortState (aRequestData.getSortColumnArray ());
      // Must we change the sorting?
      if (!aNewServerSortState.equals (aOldServerSortState))
      {
        final RequestDataSortColumn [] aNewSortCols = aNewServerSortState.getSortCols ();

        final Comparator <RowData> aComp = new AbstractComparator <RowData> ()
        {
          @Override
          protected int mainCompare (@Nonnull final RowData aRow1, @Nonnull final RowData aRow2)
          {
            int ret = 0;
            for (final RequestDataSortColumn aSortColumn : aNewSortCols)
            {
              final int nSortColumnIndex = aSortColumn.getColumnIndex ();
              final ComparatorAsString aStringComp = new ComparatorAsString (aDisplayLocale,
                                                                             aSortColumn.getSortDirectionOrDefault ());

              final CellData aCell1 = aRow1.getCellAtIndex (nSortColumnIndex);
              final CellData aCell2 = aRow2.getCellAtIndex (nSortColumnIndex);
              ret = aStringComp.compare (aCell1.getTextContent (), aCell2.getTextContent ());
              if (ret != 0)
                break;
            }
            return ret;
          }
        };

        // Main sorting
        aDataTables.sortAllRows (aComp);

        // Remember the new server state
        aDataTables.setServerSortState (aNewServerSortState);
      }
    }

    final ServerFilterState aNewServerFilterState = new ServerFilterState (aRequestData.getSearch (),
                                                                           aRequestData.isRegEx ());

    List <RowData> aResultRows = aDataTables.directGetAllRows ();
    if (aNewServerFilterState.hasSearchText ())
    {
      // filter rows
      final String sGlobalSearchText = aNewServerFilterState.getSearchText ();
      final boolean bGlobalSearchRegEx = aNewServerFilterState.isSearchRegEx ();
      final RequestDataColumn [] aColumns = aRequestData.getColumnDataArray ();

      final List <RowData> aFilteredRows = new ArrayList <RowData> ();
      for (final RowData aRow : aResultRows)
      {
        int nCellIndex = 0;
        for (final CellData aCell : aRow.directGetAllCells ())
        {
          final RequestDataColumn aColumn = aColumns[nCellIndex];
          if (aColumn.isSearchable ())
          {
            String sSearchText = aColumn.getSearchText ();
            if (StringHelper.hasNoText (sSearchText))
              sSearchText = sGlobalSearchText;
            final boolean bIsRegEx = aColumn.isSearchRegEx () || bGlobalSearchRegEx;
            final boolean bIsMatching = bIsRegEx ? aCell.matchesRegEx (sSearchText)
                                                : aCell.matchesPlain (sSearchText, aDisplayLocale);
            if (bIsMatching)
            {
              aFilteredRows.add (aRow);
              break;
            }
            nCellIndex++;
          }
        }
      }

      // Use the filtered rows
      aResultRows = aFilteredRows;
    }

    // Build the resulting array
    final List <Map <String, String>> aData = new ArrayList <Map <String, String>> ();
    for (int i = 0; i < aRequestData.getDisplayEnd (); ++i)
    {
      final int nRealIndex = aRequestData.getDisplayStart () + i;
      if (aResultRows.size () <= nRealIndex)
        break;

      final RowData aRow = aResultRows.get (nRealIndex);
      final Map <String, String> aRowData = new HashMap <String, String> ();
      if (aRow.hasRowID ())
        aRowData.put (DT_ROW_ID, aRow.getRowID ());
      if (aRow.hasRowClass ())
        aRowData.put (DT_ROW_CLASS, aRow.getRowClass ());
      int nCellIndex = 0;
      for (final CellData aCell : aRow.directGetAllCells ())
        aRowData.put (Integer.toString (nCellIndex++), aCell.getHTML ());
      aData.add (aRowData);
    }

    // Main response
    final int nTotalRecords = aDataTables.getRowCount ();
    final int nTotalDisplayRecords = aResultRows.size ();
    return new ResponseData (nTotalRecords, nTotalDisplayRecords, aRequestData.getEcho (), null, aData);
  }

  @Override
  @Nonnull
  protected IAjaxResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {
    System.out.println (ContainerHelper.getSortedByKey (aParams.getAllAttributes ()));
    // Read input parameters
    final int nDisplayStart = aParams.getAttributeAsInt (DISPLAY_START, 0);
    final int nDisplayLength = aParams.getAttributeAsInt (DISPLAY_LENGTH, 0);
    final int nColumns = aParams.getAttributeAsInt (COLUMNS, 0);
    final String sSearch = aParams.getAttributeAsString (SEARCH);
    final boolean bRegEx = aParams.getAttributeAsBoolean (REGEX, false);
    final int nSortingCols = aParams.getAttributeAsInt (SORTING_COLS);
    final int nEcho = aParams.getAttributeAsInt (ECHO);
    final List <RequestDataColumn> aColumnData = new ArrayList <RequestDataColumn> (nColumns);
    for (int nColumn = 0; nColumn < nColumns; ++nColumn)
    {
      final boolean bCSearchable = aParams.getAttributeAsBoolean (SEARCHABLE_PREFIX + nColumn);
      final String sCSearch = aParams.getAttributeAsString (SEARCH_PREFIX + nColumn);
      final boolean bCRegEx = aParams.getAttributeAsBoolean (REGEX_PREFIX + nColumn);
      final boolean bCSortable = aParams.getAttributeAsBoolean (SORTABLE_PREFIX + nColumn);
      final String sCDataProp = aParams.getAttributeAsString (DATA_PROP_PREFIX + nColumn);
      aColumnData.add (new RequestDataColumn (bCSearchable, sCSearch, bCRegEx, bCSortable, sCDataProp));
    }
    final RequestDataSortColumn [] aSortColumns = new RequestDataSortColumn [nSortingCols];
    for (int i = 0; i < nSortingCols; ++i)
    {
      final int nCSortCol = aParams.getAttributeAsInt (SORT_COL_PREFIX + i);
      final String sCSortDir = aParams.getAttributeAsString (SORT_DIR_PREFIX + i);
      final ESortOrder eCSortDir = CDataTables.SORT_ASC.equals (sCSortDir) ? ESortOrder.ASCENDING
                                                                          : CDataTables.SORT_DESC.equals (sCSortDir) ? ESortOrder.DESCENDING
                                                                                                                    : null;
      aSortColumns[i] = new RequestDataSortColumn (nCSortCol, eCSortDir);
    }
    final RequestData aRequestData = new RequestData (nDisplayStart,
                                                      nDisplayLength,
                                                      sSearch,
                                                      bRegEx,
                                                      aColumnData,
                                                      aSortColumns,
                                                      nEcho);

    // Resolve dataTables
    final String sDataTablesID = aParams.getAttributeAsString (OBJECT_ID);
    final DataTablesServerData aDataTables = UIStateRegistry.getCurrent ()
                                                            .getCastedState (DataTablesServerData.OBJECT_TYPE,
                                                                             sDataTablesID);
    if (aDataTables == null)
      return AjaxDefaultResponse.createError ("No such table: " + sDataTablesID);

    // Main request handling
    final ResponseData aResponseData = _handleRequest (aRequestData, aDataTables);
    // Convert the response to JSON
    return AjaxDefaultResponse.createSuccess (aResponseData.getAsJSON ());
  }
}
