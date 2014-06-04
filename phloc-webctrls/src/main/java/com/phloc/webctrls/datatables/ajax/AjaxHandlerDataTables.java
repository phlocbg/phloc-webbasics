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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.utils.HCSpecialNodes;
import com.phloc.webbasics.ajax.AbstractAjaxHandler;
import com.phloc.webbasics.ajax.AjaxDefaultResponse;
import com.phloc.webbasics.ajax.IAjaxResponse;
import com.phloc.webbasics.state.UIStateRegistry;
import com.phloc.webctrls.datatables.CDataTables;
import com.phloc.webctrls.datatables.EDataTablesFilterType;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * AJAX handler for filling DataTables
 * 
 * @author Philip Helger
 */
public class AjaxHandlerDataTables extends AbstractAjaxHandler
{
  private static final class RequestComparator extends AbstractComparator <DataTablesServerDataRow>
  {
    private final RequestDataSortColumn [] m_aSortCols;

    RequestComparator (@Nonnull final ServerSortState aServerSortState)
    {
      ValueEnforcer.notNull (aServerSortState, "ServerSortState");

      m_aSortCols = aServerSortState.getSortCols ();
    }

    @Override
    protected int mainCompare (@Nonnull final DataTablesServerDataRow aRow1,
                               @Nonnull final DataTablesServerDataRow aRow2)
    {
      int ret = 0;
      for (final RequestDataSortColumn aSortCol : m_aSortCols)
      {
        // Get the cells to compare
        final int nSortColumnIndex = aSortCol.getColumnIndex ();
        final DataTablesServerDataCell aCell1 = aRow1.getCellAtIndex (nSortColumnIndex);
        final DataTablesServerDataCell aCell2 = aRow2.getCellAtIndex (nSortColumnIndex);

        // Main compare
        ret = aSortCol.getComparator ().compare (aCell1.getTextContent (), aCell2.getTextContent ());
        if (ret != 0)
          break;
      }
      return ret;
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("sortCols", m_aSortCols).toString ();
    }
  }

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

  private static final Logger s_aLogger = LoggerFactory.getLogger (AjaxHandlerDataTables.class);

  @Nonnull
  private ResponseData _handleRequest (@Nonnull final RequestData aRequestData,
                                       @Nonnull final DataTablesServerData aServerData)
  {
    final Locale aDisplayLocale = aServerData.getDisplayLocale ();

    // Sorting possible?
    if (aServerData.getRowCount () > 0)
    {
      final ServerSortState aOldServerSortState = aServerData.getServerSortState ();
      final ServerSortState aNewServerSortState = new ServerSortState (aServerData,
                                                                       aRequestData.getSortColumnArray (),
                                                                       aDisplayLocale);
      // Must we change the sorting?
      if (!aNewServerSortState.equals (aOldServerSortState))
      {
        // Yes, change the sorting
        final Comparator <DataTablesServerDataRow> aComp = new RequestComparator (aNewServerSortState);

        // Main sorting
        if (s_aLogger.isDebugEnabled ())
          s_aLogger.debug ("DataTables sorting " + aServerData.getRowCount () + " rows with " + aComp);
        aServerData.sortAllRows (aComp);

        // Remember the new server state
        aServerData.setServerSortState (aNewServerSortState);
      }
    }

    List <DataTablesServerDataRow> aResultRows = aServerData.directGetAllRows ();
    if (aRequestData.isSearchActive ())
    {
      // filter rows
      final RequestDataSearch aGlobalSearch = aRequestData.getSearch ();
      final String [] aGlobalSearchTexts = aGlobalSearch.getSearchTexts ();
      final boolean bGlobalSearchRegEx = aGlobalSearch.isRegEx ();
      final RequestDataColumn [] aColumns = aRequestData.getColumnDataArray ();
      final EDataTablesFilterType eFilterType = aServerData.getFilterType ();

      boolean bContainsAnyColumnSpecificSearch = false;
      for (final RequestDataColumn aColumn : aColumns)
        if (aColumn.isSearchable () && aColumn.getSearch ().hasSearchText ())
        {
          bContainsAnyColumnSpecificSearch = true;
          if (eFilterType == EDataTablesFilterType.ALL_TERMS_PER_ROW)
            s_aLogger.error ("DataTables has column specific search term - this is not implemented for filter type ALL_TERMS_PER_ROW!");
        }

      final List <DataTablesServerDataRow> aFilteredRows = new ArrayList <DataTablesServerDataRow> ();
      if (bContainsAnyColumnSpecificSearch)
      {
        // For all rows
        for (final DataTablesServerDataRow aRow : aResultRows)
        {
          // For all cells in row
          int nSearchableCellIndex = 0;
          for (final DataTablesServerDataCell aCell : aRow.directGetAllCells ())
          {
            final RequestDataColumn aColumn = aColumns[nSearchableCellIndex];
            if (aColumn.isSearchable ())
            {
              // Determine search texts
              final RequestDataSearch aColumnSearch = aColumn.getSearch ();
              String [] aColumnSearchTexts;
              boolean bColumnSearchRegEx;
              if (aColumnSearch.hasSearchText ())
              {
                aColumnSearchTexts = aColumnSearch.getSearchTexts ();
                bColumnSearchRegEx = aColumnSearch.isRegEx ();
              }
              else
              {
                aColumnSearchTexts = aGlobalSearchTexts;
                bColumnSearchRegEx = bGlobalSearchRegEx;
              }
              final BitSet aMatchingWords = new BitSet (aColumnSearchTexts.length);

              // Main matching
              if (bColumnSearchRegEx)
                aCell.matchRegEx (aColumnSearchTexts, aMatchingWords);
              else
                aCell.matchPlainTextIgnoreCase (aColumnSearchTexts, aDisplayLocale, aMatchingWords);
              if (!aMatchingWords.isEmpty ())
              {
                aFilteredRows.add (aRow);
                break;
              }
              nSearchableCellIndex++;
            }
          }
        }
      }
      else
      {
        // Only global search is relevant

        // For all rows
        for (final DataTablesServerDataRow aRow : aResultRows)
        {
          // Each matching search term is represented as a bit in here
          final BitSet aMatchingWords = new BitSet (aGlobalSearchTexts.length);

          // For all cells in row
          int nSearchableCellIndex = 0;
          perrow: for (final DataTablesServerDataCell aCell : aRow.directGetAllCells ())
          {
            final RequestDataColumn aColumn = aColumns[nSearchableCellIndex];
            if (aColumn.isSearchable ())
            {
              // Main matching
              if (bGlobalSearchRegEx)
                aCell.matchRegEx (aGlobalSearchTexts, aMatchingWords);
              else
                aCell.matchPlainTextIgnoreCase (aGlobalSearchTexts, aDisplayLocale, aMatchingWords);

              switch (eFilterType)
              {
                case ALL_TERMS_PER_ROW:
                  if (aMatchingWords.cardinality () == aGlobalSearchTexts.length)
                  {
                    // Row matched all search terms
                    aFilteredRows.add (aRow);

                    // Goto next row
                    break perrow;
                  }
                  break;
                case ANY_TERM_PER_ROW:
                  if (!aMatchingWords.isEmpty ())
                  {
                    // Row matched any search term
                    aFilteredRows.add (aRow);

                    // Goto next row
                    break perrow;
                  }
                  // Clear again for next cell
                  aMatchingWords.clear ();
                  break;
                default:
                  throw new IllegalStateException ("Unhandled filter type: " + eFilterType);
              }
            }
            nSearchableCellIndex++;
          }
        }
      }

      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("DataTables filtered " + aFilteredRows.size () + " rows out of " + aResultRows.size ());

      // Use the filtered rows
      aResultRows = aFilteredRows;
    }

    // Build the resulting array
    final HCSpecialNodes aSpecialNodes = new HCSpecialNodes ();
    final List <Map <String, String>> aData = new ArrayList <Map <String, String>> ();
    int nResultRowCount = 0;
    final boolean bAllEntries = aRequestData.showAllEntries ();
    // Just in case ;-)
    final int nMaxResultRows = Math.min (aRequestData.getDisplayLength (), aServerData.getRowCount ());
    while (bAllEntries || nResultRowCount < nMaxResultRows)
    {
      final int nRealIndex = aRequestData.getDisplayStart () + nResultRowCount;
      if (nRealIndex >= aResultRows.size ())
      {
        // No more matching rows available
        break;
      }

      final DataTablesServerDataRow aRow = aResultRows.get (nRealIndex);
      final Map <String, String> aRowData = new HashMap <String, String> ();
      if (aRow.hasRowID ())
        aRowData.put (DT_ROW_ID, aRow.getRowID ());
      if (aRow.hasRowClass ())
        aRowData.put (DT_ROW_CLASS, aRow.getRowClass ());
      int nCellIndex = 0;
      for (final DataTablesServerDataCell aCell : aRow.directGetAllCells ())
      {
        aRowData.put (Integer.toString (nCellIndex++), aCell.getHTML ());

        // Merge all special nodes into the global ones
        aSpecialNodes.addAll (aCell.getSpecialNodes ());
      }
      aData.add (aRowData);
      ++nResultRowCount;
    }

    // Main response
    final int nTotalRecords = aServerData.getRowCount ();
    final int nTotalDisplayRecords = aResultRows.size ();
    return new ResponseData (nTotalRecords, nTotalDisplayRecords, aRequestData.getEcho (), null, aData, aSpecialNodes);
  }

  @Override
  @Nonnull
  protected IAjaxResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("DataTables AJAX request: " + ContainerHelper.getSortedByKey (aParams.getAllAttributes ()));

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
    final DataTablesServerData aServerData = UIStateRegistry.getCurrent ()
                                                            .getCastedState (DataTablesServerData.OBJECT_TYPE,
                                                                             sDataTablesID);
    if (aServerData == null)
      return AjaxDefaultResponse.createError ("No such table data: " + sDataTablesID);

    // Main request handling
    final ResponseData aResponseData = _handleRequest (aRequestData, aServerData);

    // Convert the response to JSON and add the special nodes
    return AjaxDefaultResponse.createSuccess (aResponseData.getAsJson ()).addAll (aResponseData.getSpecialNodes ());
  }
}
