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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.HCScript;
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
import com.phloc.webbasics.state.UIStateRegistry;

public class DataTables implements IHCNodeBuilder
{
  public static final boolean DEFAULT_GENERATE_ON_DOCUMENT_READY = false;
  public static final boolean DEFAULT_PAGINATE = true;
  public static final boolean DEFAULT_STATE_SAVE = false;
  public static final boolean DEFAULT_JQUERY_UI = false;
  public static final boolean DEFAULT_SCROLL_AUTO_CSS = true;
  public static final boolean DEFAULT_SCROLL_COLLAPSE = false;
  public static final boolean DEFAULT_SCROLL_INFINITE = false;
  public static final boolean DEFAULT_USER_JQUERY_AJAX = false;
  public static final boolean DEFAULT_DEFER_RENDER = false;
  public static final EDataTablesPaginationType DEFAULT_PAGINATION_TYPE = EDataTablesPaginationType.FULL_NUMBERS;
  public static final int DEFAULT_DISPLAY_LENGTH = 10;

  private static final Logger s_aLogger = LoggerFactory.getLogger (DataTables.class);
  private static boolean s_bDefaultGenerateOnDocumentReady = DEFAULT_GENERATE_ON_DOCUMENT_READY;

  private final AbstractHCBaseTable <?> m_aTable;
  private boolean m_bGenerateOnDocumentReady = s_bDefaultGenerateOnDocumentReady;
  private Locale m_aDisplayLocale;
  private boolean m_bPaginate = DEFAULT_PAGINATE;
  private boolean m_bStateSave = DEFAULT_STATE_SAVE;
  private boolean m_bJQueryUI = DEFAULT_JQUERY_UI;
  private final List <DataTablesColumn> m_aColumns = new ArrayList <DataTablesColumn> ();
  private DataTablesSorting m_aInitialSorting;
  private EDataTablesPaginationType m_ePaginationType = DEFAULT_PAGINATION_TYPE;
  private String m_sScrollX;
  private String m_sScrollXInner;
  private String m_sScrollY;
  private boolean m_bScrollAutoCSS = DEFAULT_SCROLL_AUTO_CSS;
  private boolean m_bScrollCollapse = DEFAULT_SCROLL_COLLAPSE;
  private boolean m_bScrollInfinite = DEFAULT_SCROLL_INFINITE;
  private Map <Integer, String> m_aLengthMenu;
  private int m_nDisplayLength = DEFAULT_DISPLAY_LENGTH;
  private String m_sDom;
  // server side processing
  private ISimpleURL m_aAjaxSource;
  private EHTTPMethod m_eServerMethod;
  private Map <String, String> m_aServerParams;
  private boolean m_bUseJQueryAjax = DEFAULT_USER_JQUERY_AJAX;
  private boolean m_bDeferRender = DEFAULT_DEFER_RENDER;

  public static boolean isDefaultGenerateOnDocumentReady ()
  {
    return s_bDefaultGenerateOnDocumentReady;
  }

  public static void setDefaultGenerateOnDocumentReady (final boolean bGenerateOnDocumentReady)
  {
    s_bDefaultGenerateOnDocumentReady = bGenerateOnDocumentReady;
  }

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
    if (!aTable.hasHeaderRows ())
      s_aLogger.warn ("Table does not have a header row, so DataTables may not be displayed correctly!");
    if (StringHelper.hasNoText (aTable.getID ()))
      throw new IllegalArgumentException ("Table has no ID!");

    m_aTable = aTable;
    registerExternalResources ();
  }

  /**
   * @return The underlying table on which this object is operating.
   */
  @Nonnull
  public final AbstractHCBaseTable <?> getTable ()
  {
    return m_aTable;
  }

  public boolean getGenerateOnDocumentReady ()
  {
    return m_bGenerateOnDocumentReady;
  }

  @Nonnull
  public DataTables setGenerateOnDocumentReady (final boolean bGenerateOnDocumentReady)
  {
    m_bGenerateOnDocumentReady = bGenerateOnDocumentReady;
    return this;
  }

  @Nullable
  public Locale getDisplayLocale ()
  {
    return m_aDisplayLocale;
  }

  @Nonnull
  public DataTables setDisplayLocale (@Nullable final Locale aDisplayLocale)
  {
    m_aDisplayLocale = aDisplayLocale;
    return this;
  }

  public boolean isPaginate ()
  {
    return m_bPaginate;
  }

  @Nonnull
  public DataTables setPaginate (final boolean bPaginate)
  {
    m_bPaginate = bPaginate;
    return this;
  }

  public boolean isStateSave ()
  {
    return m_bStateSave;
  }

  @Nonnull
  public DataTables setStateSave (final boolean bStateSave)
  {
    m_bStateSave = bStateSave;
    return this;
  }

  public boolean isJQueryUI ()
  {
    return m_bJQueryUI;
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
    if (aTable == null)
      throw new NullPointerException ("table");
    // Add all columns
    final int nCols = aTable.getColGroup ().getColumnCount ();
    for (int i = 0; i < nCols; ++i)
      addColumn (new DataTablesColumn (i));
    return this;
  }

  public boolean hasColumns ()
  {
    return !m_aColumns.isEmpty ();
  }

  @Nonnegative
  public int getColumnCount ()
  {
    return m_aColumns.size ();
  }

  @Nullable
  public DataTablesSorting getInitialSorting ()
  {
    return m_aInitialSorting;
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

  @Nullable
  public EDataTablesPaginationType getPaginationType ()
  {
    return m_ePaginationType;
  }

  @Nonnull
  public DataTables setPaginationType (@Nullable final EDataTablesPaginationType ePaginationType)
  {
    m_ePaginationType = ePaginationType;
    return this;
  }

  @Nullable
  public String getScrollX ()
  {
    return m_sScrollX;
  }

  @Nonnull
  public DataTables setScrollX (@Nullable final String sScrollX)
  {
    m_sScrollX = sScrollX;
    return this;
  }

  @Nullable
  public String getScrollXInner ()
  {
    return m_sScrollXInner;
  }

  @Nonnull
  public DataTables setScrollXInner (@Nullable final String sScrollXInner)
  {
    m_sScrollXInner = sScrollXInner;
    return this;
  }

  @Nullable
  public String getScrollY ()
  {
    return m_sScrollY;
  }

  @Nonnull
  public DataTables setScrollY (@Nullable final String sScrollY)
  {
    m_sScrollY = sScrollY;
    return this;
  }

  public boolean isScrollAutoCSS ()
  {
    return m_bScrollAutoCSS;
  }

  @Nonnull
  public DataTables setScrollAutoCSS (final boolean bScrollAutoCSS)
  {
    m_bScrollAutoCSS = bScrollAutoCSS;
    return this;
  }

  public boolean isScrollCollapse ()
  {
    return m_bScrollCollapse;
  }

  @Nonnull
  public DataTables setScrollCollapse (final boolean bScrollCollapse)
  {
    m_bScrollCollapse = bScrollCollapse;
    return this;
  }

  public boolean isScrollInfinite ()
  {
    return m_bScrollInfinite;
  }

  @Nonnull
  public DataTables setScrollInfinite (final boolean bScrollInfinite)
  {
    m_bScrollInfinite = bScrollInfinite;
    return this;
  }

  @Nullable
  public String getDom ()
  {
    return m_sDom;
  }

  @Nonnull
  public DataTables setDom (@Nullable final String sDom)
  {
    m_sDom = sDom;
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <Integer, String> getLengthMenu ()
  {
    return ContainerHelper.newMap (m_aLengthMenu);
  }

  @Nonnull
  public DataTables setLengthMenu (@Nullable final int... aLength)
  {
    if (aLength == null)
      m_aLengthMenu = null;
    else
    {
      final Map <Integer, String> aLengthMenu = new LinkedHashMap <Integer, String> ();
      for (final int nValue : aLength)
        aLengthMenu.put (Integer.valueOf (nValue), null);
      m_aLengthMenu = aLengthMenu;
    }
    return this;
  }

  @Nonnull
  public DataTables setLengthMenu (@Nullable final Map <Integer, String> aLength)
  {
    m_aLengthMenu = aLength == null ? null : ContainerHelper.newOrderedMap (aLength);
    return this;
  }

  @Nonnegative
  public int getDisplayLength ()
  {
    return m_nDisplayLength;
  }

  @Nonnull
  public DataTables setDisplayLength (@Nonnegative final int nDisplayLength)
  {
    if (nDisplayLength < 1)
      throw new IllegalArgumentException ("displayLength is too small!");
    m_nDisplayLength = nDisplayLength;
    return this;
  }

  // Server side handling params

  @Nullable
  public ISimpleURL getAjaxSource ()
  {
    return m_aAjaxSource;
  }

  @Nonnull
  public DataTables setAjaxSource (@Nullable final ISimpleURL aAjaxSource)
  {
    m_aAjaxSource = aAjaxSource;
    return this;
  }

  @Nullable
  public EHTTPMethod getServerMethod ()
  {
    return m_eServerMethod;
  }

  @Nonnull
  public DataTables setServerMethod (@Nullable final EHTTPMethod eServerMethod)
  {
    m_eServerMethod = eServerMethod;
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAllServerParams ()
  {
    return ContainerHelper.newMap (m_aServerParams);
  }

  @Nonnull
  public DataTables setServerParams (@Nullable final Map <String, String> aServerParams)
  {
    m_aServerParams = aServerParams;
    return this;
  }

  public boolean isUseJQueryAjax ()
  {
    return m_bUseJQueryAjax;
  }

  @Nonnull
  public DataTables setUseJQueryAjax (final boolean bUseJQueryAjax)
  {
    m_bUseJQueryAjax = bUseJQueryAjax;
    return this;
  }

  public boolean isDeferRender ()
  {
    return m_bDeferRender;
  }

  @Nonnull
  public DataTables setDeferRender (final boolean bDeferRender)
  {
    m_bDeferRender = bDeferRender;
    return this;
  }

  /**
   * modify parameter map
   * 
   * @param aParams
   *        parameter map
   */
  @OverrideOnDemand
  protected void modifyParams (@Nonnull final JSAssocArray aParams)
  {}

  /**
   * Add JS code before the data tables init is called
   * 
   * @param aPackage
   *        The JS Package to add the code to
   */
  @OverrideOnDemand
  protected void addCodeBeforeDataTables (@Nonnull final JSPackage aPackage)
  {}

  /**
   * Add JS code before the data tables init is called
   * 
   * @param aPackage
   *        The JS Package to add the code to
   * @param aJSTable
   *        The JS reference to the created DataTables object
   */
  @OverrideOnDemand
  protected void addCodeAfterDataTables (@Nonnull final JSPackage aPackage, @Nonnull final JSVar aJSTable)
  {}

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
    if (StringHelper.hasText (m_sScrollX))
      aParams.add ("sScrollX", m_sScrollX);
    if (StringHelper.hasText (m_sScrollXInner))
      aParams.add ("sScrollXInner", m_sScrollXInner);
    if (StringHelper.hasText (m_sScrollY))
      aParams.add ("sScrollY", m_sScrollY);
    if (m_bScrollAutoCSS != DEFAULT_SCROLL_AUTO_CSS)
      aParams.add ("bScrollAutoCss", m_bScrollAutoCSS);
    if (m_bScrollCollapse != DEFAULT_SCROLL_COLLAPSE)
      aParams.add ("bScrollCollapse", m_bScrollCollapse);
    if (m_bScrollInfinite != DEFAULT_SCROLL_INFINITE)
      aParams.add ("bScrollInfinite", m_bScrollInfinite);
    if (StringHelper.hasText (m_sDom))
      aParams.add ("sDom", m_sDom);
    if (m_aLengthMenu != null && !m_aLengthMenu.isEmpty ())
    {
      final JSArray aArray1 = new JSArray ();
      final JSArray aArray2 = new JSArray ();
      for (final Map.Entry <Integer, String> aEntry : m_aLengthMenu.entrySet ())
      {
        final int nKey = aEntry.getKey ().intValue ();
        final String sValue = aEntry.getValue ();
        aArray1.add (nKey);
        if (sValue != null)
          aArray2.add (sValue);
        else
          aArray2.add (nKey);
      }
      aParams.add ("aLengthMenu", new JSArray ().add (aArray1).add (aArray2));
    }
    if (m_nDisplayLength != DEFAULT_DISPLAY_LENGTH)
      aParams.add ("iDisplayLength", m_nDisplayLength);

    // Server handling parameters
    final boolean bServerSide = m_aAjaxSource != null;
    if (bServerSide)
    {
      aParams.add ("bServerSide", true);
      UIStateRegistry.getCurrent ().registerState (m_aTable.getID (),
                                                   new DataTablesServerData (m_aTable, m_aDisplayLocale));
    }
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
    if (m_bDeferRender != DEFAULT_DEFER_RENDER)
      aParams.add ("bDeferRender", m_bDeferRender);

    // Display texts
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

    modifyParams (aParams);

    // main on document ready code
    final JSPackage aJSCode = new JSPackage ();

    addCodeBeforeDataTables (aJSCode);
    final JSVar aJSTable = aJSCode.var ("oTable" + GlobalIDFactory.getNewIntID (), JQuery.idRef (m_aTable.getID ())
                                                                                         .invoke ("dataTable")
                                                                                         .arg (aParams));
    addCodeAfterDataTables (aJSCode, aJSTable);

    return m_bGenerateOnDocumentReady ? new HCScriptOnDocumentReady (aJSCode) : new HCScript (aJSCode);
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EDataTablesJSPathProvider.DATATABLES_194);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EDataTablesCSSPathProvider.DATATABLES_194);
  }
}
