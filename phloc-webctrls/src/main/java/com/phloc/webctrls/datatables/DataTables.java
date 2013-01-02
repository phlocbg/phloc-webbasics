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
package com.phloc.webctrls.datatables;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.JSVar;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webbasics.http.EHTTPMethod;

public class DataTables implements IHCNodeBuilder
{
  public static final boolean DEFAULT_PAGINATE = true;
  public static final boolean DEFAULT_STATE_SAVE = false;
  public static final boolean DEFAULT_JQUERY_UI = false;
  public static final EDataTablesPaginationType DEFAULT_PAGINATION_TYPE = EDataTablesPaginationType.FULL_NUMBERS;
  private static final Logger s_aLogger = LoggerFactory.getLogger (DataTables.class);

  private final String m_sParentElementID;
  private Locale m_aDisplayLocale;
  private boolean m_bPaginate = DEFAULT_PAGINATE;
  private boolean m_bStateSave = DEFAULT_STATE_SAVE;
  private boolean m_bJQueryUI = DEFAULT_JQUERY_UI;
  private final List <DataTablesColumn> m_aColumns = new ArrayList <DataTablesColumn> ();
  private DataTablesSorting m_aInitialSorting;
  private EDataTablesPaginationType m_ePaginationType = DEFAULT_PAGINATION_TYPE;
  // server side processing
  private ISimpleURL m_aAjaxSource;
  private EHTTPMethod m_eServerMethod;
  private Map <String, String> m_aServerParams;
  private boolean m_bUseJQueryAjax;

  /**
   * Apply to an existing table. If the table does not have an ID yet, a new one
   * is created.
   * 
   * @param aTable
   *        The table to apply the data table to. May not be <code>null</code>
   *        and must have a valid ID!
   */
  public DataTables (@Nonnull final AbstractHCBaseTable <?> aTable)
  {
    if (aTable == null)
      throw new NullPointerException ("Table");
    if (!aTable.hasHeaderRow ())
      s_aLogger.warn ("Table does not have a header row, so DataTables may not be displayed correctly!");

    m_sParentElementID = aTable.getID ();
    if (StringHelper.hasNoText (m_sParentElementID))
      throw new IllegalArgumentException ("Table has no ID!");

    registerExternalResources ();
  }

  @Nonnull
  public DataTables setDisplayLocale (@Nullable final Locale aDisplayLocale)
  {
    m_aDisplayLocale = aDisplayLocale;
    return this;
  }

  @Nonnull
  public DataTables setPaginate (final boolean bPaginate)
  {
    m_bPaginate = bPaginate;
    return this;
  }

  @Nonnull
  public DataTables setStateSave (final boolean bStateSave)
  {
    m_bStateSave = bStateSave;
    return this;
  }

  @Nonnull
  public DataTables setJQueryUI (final boolean bJQueryUI)
  {
    m_bJQueryUI = bJQueryUI;
    return this;
  }

  @Nullable
  public DataTablesColumn getColumn (@Nonnegative final int nIndex)
  {
    if (nIndex < 0 || nIndex >= m_aColumns.size ())
      return null;
    return m_aColumns.get (nIndex);
  }

  @Nonnull
  public DataTables addColumn (@Nonnull final DataTablesColumn aColumn)
  {
    if (aColumn == null)
      throw new NullPointerException ("column");
    m_aColumns.add (aColumn);
    return this;
  }

  @Nonnull
  public DataTables addAllColumns (@Nonnull final AbstractHCBaseTable <?> aTable)
  {
    // Add all columns
    final int nCols = aTable.getColGroup ().getColumnCount ();
    for (int i = 0; i < nCols; ++i)
      addColumn (new DataTablesColumn (i));
    return this;
  }

  @Nonnull
  public DataTables setInitialSorting (@Nonnegative final int nIndex, @Nonnull final ESortOrder eSortOrder)
  {
    return setInitialSorting (new DataTablesSorting ().addColumn (nIndex, eSortOrder));
  }

  @Nonnull
  public DataTables setInitialSorting (@Nullable final DataTablesSorting aInitialSorting)
  {
    m_aInitialSorting = aInitialSorting;
    return this;
  }

  @Nonnull
  public DataTables setPaginationType (@Nullable final EDataTablesPaginationType ePaginationType)
  {
    m_ePaginationType = ePaginationType;
    return this;
  }

  @Nonnull
  public DataTables setAjaxSource (@Nullable final ISimpleURL aAjaxSource)
  {
    m_aAjaxSource = aAjaxSource;
    return this;
  }

  @Nonnull
  public DataTables setServerMethod (@Nullable final EHTTPMethod eServerMethod)
  {
    m_eServerMethod = eServerMethod;
    return this;
  }

  @Nonnull
  public DataTables setServerParams (@Nullable final Map <String, String> aServerParams)
  {
    m_aServerParams = aServerParams;
    return this;
  }

  @Nonnull
  public DataTables setUseJQueryAjax (final boolean bUseJQueryAjax)
  {
    m_bUseJQueryAjax = bUseJQueryAjax;
    return this;
  }

  @Nullable
  public IHCNode build ()
  {
    // init parameters
    final JSAssocArray aParams = new JSAssocArray ();
    if (m_bPaginate != DEFAULT_PAGINATE)
      aParams.add ("bPaginate", m_bPaginate);
    if (m_bPaginate != DEFAULT_STATE_SAVE)
      aParams.add ("bStateSave", m_bStateSave);
    if (m_bJQueryUI != DEFAULT_JQUERY_UI)
      aParams.add ("bJQueryUI", m_bJQueryUI);
    if (!m_aColumns.isEmpty ())
    {
      final JSArray aArray = new JSArray ();
      for (final DataTablesColumn aColumn : m_aColumns)
        aArray.add (aColumn.getAsJS ());
      aParams.add ("aoColumnDefs", aArray);
    }
    if (m_aInitialSorting != null)
      aParams.add ("aaSorting", m_aInitialSorting.getAsJS ());
    if (m_ePaginationType != null)
      aParams.add ("sPaginationType", m_ePaginationType.getName ());
    aParams.add ("bServerSide", m_aAjaxSource != null);
    if (m_aAjaxSource != null)
      aParams.add ("sAjaxSource", m_aAjaxSource.getAsString ());
    if (m_eServerMethod != null)
      aParams.add ("sServerMethod", m_eServerMethod.getName ());
    if (ContainerHelper.isNotEmpty (m_aServerParams))
    {
      final JSAnonymousFunction aAF = new JSAnonymousFunction ();
      final JSVar aData = aAF.param ("aoData");
      for (final Map.Entry <String, String> aEntry : m_aServerParams.entrySet ())
      {
        aAF.body ()
           .invoke (aData, "push")
           .arg (new JSAssocArray ().add ("name", aEntry.getKey ()).add ("value", aEntry.getValue ()));
      }
      aParams.add ("fnServerParams", aAF);
    }
    if (m_bUseJQueryAjax)
    {
      final JSAnonymousFunction aAF = new JSAnonymousFunction ();
      final JSVar sSource = aAF.param ("s");
      final JSVar aoData = aAF.param ("t");
      final JSVar fnCallback = aAF.param ("u");
      final JSVar oSettings = aAF.param ("v");
      final JSAssocArray aAjax = new JSAssocArray ().add ("dataType", "json");
      if (m_eServerMethod != null)
        aAjax.add ("type", m_eServerMethod.getName ());
      // Success callback, to take out the "value" parameter from the AJAX
      // default response object
      final JSAnonymousFunction aCB = new JSAnonymousFunction ();
      final JSVar aData = aCB.param ("a");
      final JSVar aTextStatus = aCB.param ("b");
      final JSVar aJQXHR = aCB.param ("c");
      aCB.body ().invoke (fnCallback.name ()).arg (aData.ref ("value")).arg (aTextStatus).arg (aJQXHR);
      aAjax.add ("url", sSource).add ("data", aoData).add ("success", aCB);
      aAF.body ().assign (oSettings.ref ("jqXHR"), JQuery.ajax ().arg (aAjax));
      aParams.add ("fnServerData", aAF);
    }

    if (m_aDisplayLocale != null)
    {
      final JSAssocArray aLanguage = new JSAssocArray ();
      final JSAssocArray aPagination = new JSAssocArray ();
      aPagination.add ("sFirst", EDataTablesText.FIRST.getDisplayText (m_aDisplayLocale));
      aPagination.add ("sPrevious", EDataTablesText.PREVIOUS.getDisplayText (m_aDisplayLocale));
      aPagination.add ("sNext", EDataTablesText.NEXT.getDisplayText (m_aDisplayLocale));
      aPagination.add ("sLast", EDataTablesText.LAST.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("oPaginate", aPagination);
      aLanguage.add ("sProcessing", EDataTablesText.PROCESSING.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sLengthMenu", EDataTablesText.LENGTH_MENU.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sZeroRecords", EDataTablesText.ZERO_RECORDS.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sInfo", EDataTablesText.INFO.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sInfoEmpty", EDataTablesText.INFO_EMPTY.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sInfoFiltered", EDataTablesText.INFO_FILTERED.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sInfoPostFix", EDataTablesText.INFO_POSTFIX.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sSearch", EDataTablesText.SEARCH.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sUrl", EDataTablesText.URL.getDisplayText (m_aDisplayLocale));
      aParams.add ("oLanguage", aLanguage);
    }

    // main on document ready code
    final JSPackage aJSCode = new JSPackage ();
    aJSCode.var ("oTable", JQuery.idRef (m_sParentElementID).invoke ("dataTable").arg (aParams));
    return new HCScriptOnDocumentReady (aJSCode);
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EDataTablesJSPathProvider.DATATABLES_194);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EDataTablesCSSPathProvider.DATATABLES_194);
  }
}
