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
package com.phloc.webctrls.datatables;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webbasics.http.EHTTPMethod;
import com.phloc.webctrls.datatable.EDataTableJSONKeyword;
import com.phloc.webctrls.datatable.EDataTableText;

public class DataTables implements IHCNodeBuilder
{
  public static final boolean DEFAULT_PAGINATE = true;
  public static final boolean DEFAULT_STATE_SAVE = false;
  public static final boolean DEFAULT_JQUERY_UI = false;

  private final String m_sParentElementID;
  private Locale m_aDisplayLocale;
  private boolean m_bPaginate = DEFAULT_PAGINATE;
  private boolean m_bStateSave = DEFAULT_STATE_SAVE;
  private boolean m_bJQueryUI = DEFAULT_JQUERY_UI;
  private final List <DataTablesColumn> m_aColumns = new ArrayList <DataTablesColumn> ();
  private DataTablesSorting m_aInitialSorting;
  private EDataTablesPaginationType m_ePaginationType;
  // server side processing
  private ISimpleURL m_aAjaxSource;
  private EHTTPMethod m_eServerMethod;

  @Nonnull
  private static String _ensureID (@Nonnull final AbstractHCBaseTable <?> aTable)
  {
    String sID = aTable.getID ();
    if (StringHelper.hasNoText (sID))
    {
      sID = GlobalIDFactory.getNewStringID ();
      aTable.setID (sID);
    }
    return sID;
  }

  /**
   * Apply to an existing table. If the table does not have an ID yet, a new one
   * is created.
   * 
   * @param aTable
   *        The table to apply the data table to
   */
  public DataTables (@Nonnull final AbstractHCBaseTable <?> aTable)
  {
    this (_ensureID (aTable));
  }

  public DataTables (@Nonnull final String sParentElementID)
  {
    if (StringHelper.hasNoText (sParentElementID))
      throw new IllegalArgumentException ("ParentElementID");
    m_sParentElementID = sParentElementID;

    PerRequestJSIncludes.registerJSIncludeForThisRequest (EDataTablesJSPathProvider.DATATABLES_194);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EDataTablesCSSPathProvider.DATATABLES_194);
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

  @Nonnull
  public DataTables addColumn (@Nonnull final DataTablesColumn aColumn)
  {
    if (aColumn == null)
      throw new NullPointerException ("column");
    m_aColumns.add (aColumn);
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

  @Nullable
  public IHCNode build ()
  {
    // init parameters
    final JSAssocArray aParams = new JSAssocArray ();
    if (m_bPaginate != DEFAULT_PAGINATE)
      aParams.add (EDataTableJSONKeyword.PAGINATE.getName (), m_bPaginate);
    if (m_bPaginate != DEFAULT_STATE_SAVE)
      aParams.add (EDataTableJSONKeyword.STATE_SAVE.getName (), m_bStateSave);
    if (m_bJQueryUI != DEFAULT_JQUERY_UI)
      aParams.add (EDataTableJSONKeyword.JQUERY_UI.getName (), m_bJQueryUI);
    if (!m_aColumns.isEmpty ())
    {
      final JSArray aArray = new JSArray ();
      for (final DataTablesColumn aColumn : m_aColumns)
        aArray.add (aColumn.getAsJS ());
      aParams.add (EDataTableJSONKeyword.COLUMN_DEFS.getName (), aArray);
    }
    if (m_aInitialSorting != null)
      aParams.add (EDataTableJSONKeyword.SORTING.getName (), m_aInitialSorting.getAsJS ());
    if (m_ePaginationType != null)
      aParams.add (EDataTableJSONKeyword.PAGINATION_TYPE.getName (), m_ePaginationType.getName ());
    aParams.add (EDataTableJSONKeyword.SERVER_SIDE.getName (), m_aAjaxSource != null);
    if (m_aAjaxSource != null)
      aParams.add (EDataTableJSONKeyword.AJAX_SOURCE.getName (), m_aAjaxSource.getAsString ());
    if (m_eServerMethod != null)
      aParams.add (EDataTableJSONKeyword.SERVER_METHOD.getName (), m_eServerMethod.getName ());

    if (m_aDisplayLocale != null)
    {
      final JSAssocArray aLanguage = new JSAssocArray ();
      final JSAssocArray aPagination = new JSAssocArray ();
      aPagination.add ("sFirst", EDataTableText.FIRST.getDisplayText (m_aDisplayLocale));
      aPagination.add ("sPrevious", EDataTableText.PREVIOUS.getDisplayText (m_aDisplayLocale));
      aPagination.add ("sNext", EDataTableText.NEXT.getDisplayText (m_aDisplayLocale));
      aPagination.add ("sLast", EDataTableText.LAST.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("oPaginate", aPagination);
      aLanguage.add ("sProcessing", EDataTableText.PROCESSING.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sLengthMenu", EDataTableText.LENGTH_MENU.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sZeroRecords", EDataTableText.ZERO_RECORDS.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sInfo", EDataTableText.INFO.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sInfoEmpty", EDataTableText.INFO_EMPTY.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sInfoFiltered", EDataTableText.INFO_FILTERED.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sInfoPostFix", EDataTableText.INFO_POSTFIX.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sSearch", EDataTableText.SEARCH.getDisplayText (m_aDisplayLocale));
      aLanguage.add ("sUrl", EDataTableText.URL.getDisplayText (m_aDisplayLocale));
      aParams.add (EDataTableJSONKeyword.LANGUAGE.getName (), aLanguage);
    }

    // main on document ready code
    final JSPackage aJSCode = new JSPackage ();
    aJSCode.var ("oTable", JQuery.idRef (m_sParentElementID).invoke ("dataTable").arg (aParams));
    return new HCScriptOnDocumentReady (aJSCode);
  }
}
