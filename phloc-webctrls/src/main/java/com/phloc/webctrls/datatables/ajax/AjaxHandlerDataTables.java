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
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.ajax.AbstractAjaxHandler;
import com.phloc.webbasics.ajax.AjaxDefaultResponse;
import com.phloc.webbasics.ajax.IAjaxResponse;
import com.phloc.webbasics.state.UIStateRegistry;
import com.phloc.webctrls.datatables.CDataTables;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.DataTables.ServerState;

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

  @Nonnull
  private ResponseData _handleRequest (@Nonnull final RequestData aRequestData, @Nonnull final DataTables aDataTables)
  {
    final AbstractHCBaseTable <?> aTable = aDataTables.getTable ();
    final ServerState aOldServerState = aDataTables.getServerState ();
    final ServerState aNewServerState = new ServerState (aRequestData.getSearch (),
                                                         aRequestData.isRegEx (),
                                                         aRequestData.getSortCols ());
    if (!aNewServerState.equals (aOldServerState))
    {
      // Restructure table
      System.out.println ("Table needs to be changed to: " + aNewServerState);
      aDataTables.setServerState (aNewServerState);
    }

    final int nTotalRecords = aTable.getBodyRowCount ();
    final int nTotalDisplayRecords = 0;
    return new ResponseData (nTotalRecords,
                             nTotalDisplayRecords,
                             aRequestData.getEcho (),
                             null,
                             new ArrayList <Map <String, String>> ());
  }

  @Override
  @Nonnull
  protected IAjaxResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {

    System.out.println (aParams.getAllAttributeNames ());
    // Read input parameters
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

    final String sTableID = aParams.getAttributeAsString (OBJECT_ID);
    final DataTables aDataTables = UIStateRegistry.getCurrent ().getCastedState (DataTables.OBJECT_TYPE, sTableID);
    if (aDataTables == null)
      throw new IllegalArgumentException ("No such table: " + sTableID);

    // Main request handling
    final ResponseData aResponseData = _handleRequest (aRequestData, aDataTables);
    // Convert the response to JSON
    return AjaxDefaultResponse.createSuccess (aResponseData.getAsJSON ());
  }
}
