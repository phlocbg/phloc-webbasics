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
package com.phloc.webctrls.datatables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.lang.DecimalFormatSymbolsFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.CHTMLAttributes;
import com.phloc.html.EHTMLElement;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCColGroup;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSBlock;
import com.phloc.html.js.builder.JSConditional;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.JSRef;
import com.phloc.html.js.builder.JSVar;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.html.js.builder.jquery.JQueryAjaxBuilder;
import com.phloc.html.js.builder.jquery.JQueryInvocation;
import com.phloc.html.js.builder.jquery.JQuerySelector;
import com.phloc.html.js.builder.jquery.JQuerySelectorList;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;
import com.phloc.web.http.EHTTPMethod;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webbasics.state.UIStateRegistry;
import com.phloc.webctrls.datatables.ajax.DataTablesServerData;
import com.phloc.webctrls.js.JSJQueryUtils;

public class DataTables implements IHCNodeBuilder
{
  public static final boolean DEFAULT_GENERATE_ON_DOCUMENT_READY = false;
  public static final boolean DEFAULT_AUTOWIDTH = true;
  public static final boolean DEFAULT_PAGINATE = true;
  public static final boolean DEFAULT_STATE_SAVE = false;
  public static final boolean DEFAULT_JQUERY_UI = false;
  public static final boolean DEFAULT_SCROLL_AUTO_CSS = true;
  public static final boolean DEFAULT_SCROLL_COLLAPSE = false;
  public static final boolean DEFAULT_USER_JQUERY_AJAX = false;
  public static final boolean DEFAULT_DEFER_RENDER = false;
  public static final EDataTablesPaginationType DEFAULT_PAGINATION_TYPE = EDataTablesPaginationType.FULL_NUMBERS;
  public static final int DEFAULT_DISPLAY_LENGTH = 10;
  public static final boolean DEFAULT_USE_FIXED_HEADER = false;

  private static final Logger s_aLogger = LoggerFactory.getLogger (DataTables.class);
  private static boolean s_bDefaultGenerateOnDocumentReady = DEFAULT_GENERATE_ON_DOCUMENT_READY;

  private final IHCTable <?> m_aTable;
  private boolean m_bGenerateOnDocumentReady = s_bDefaultGenerateOnDocumentReady;
  private Locale m_aDisplayLocale;
  private boolean m_bAutoWidth = DEFAULT_AUTOWIDTH;
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
  private Map <Integer, String> m_aLengthMenu;
  private int m_nDisplayLength = DEFAULT_DISPLAY_LENGTH;
  private String m_sDom;
  // server side processing
  private ISimpleURL m_aAjaxSource;
  private EHTTPMethod m_eServerMethod;
  private Map <String, String> m_aServerParams;
  private boolean m_bUseJQueryAjax = DEFAULT_USER_JQUERY_AJAX;
  private EDataTablesFilterType m_eServerFilterType = EDataTablesFilterType.DEFAULT;
  private boolean m_bDeferRender = DEFAULT_DEFER_RENDER;
  private final String m_sGeneratedJSVariableName = "oTable" + GlobalIDFactory.getNewIntID ();
  private ISimpleURL m_aTextLoadingURL;
  private String m_sTextLoadingURLLocaleParameterName;
  private Map <String, String> m_aTextLoadingParams;

  // FixedHeaderStuff
  private boolean m_bUseFixedHeader = DEFAULT_USE_FIXED_HEADER;
  private JSAssocArray m_aFixedHeaderOptions;

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
  public DataTables (@Nonnull final IHCTable <?> aTable)
  {
    ValueEnforcer.notNull (aTable, "Table");
    if (!aTable.hasHeaderRows ())
      s_aLogger.warn ("Table does not have a header row, so DataTables may not be displayed correctly!");
    ValueEnforcer.notEmpty (aTable.getID (), "Table has no ID!");

    this.m_aTable = aTable;
  }

  /**
   * @return The underlying table on which this object is operating.
   */
  @Nonnull
  public final IHCTable <?> getTable ()
  {
    return this.m_aTable;
  }

  /**
   * @return The ID of the underlying table on which this object is operating.
   */
  @Nonnull
  public final String getTableID ()
  {
    return this.m_aTable.getID ();
  }

  public boolean isGenerateOnDocumentReady ()
  {
    return this.m_bGenerateOnDocumentReady;
  }

  @Nonnull
  public DataTables setGenerateOnDocumentReady (final boolean bGenerateOnDocumentReady)
  {
    this.m_bGenerateOnDocumentReady = bGenerateOnDocumentReady;
    return this;
  }

  @Nullable
  public Locale getDisplayLocale ()
  {
    return this.m_aDisplayLocale;
  }

  @Nonnull
  public DataTables setDisplayLocale (@Nullable final Locale aDisplayLocale)
  {
    this.m_aDisplayLocale = aDisplayLocale;
    return this;
  }

  public boolean isAutoWidth ()
  {
    return this.m_bAutoWidth;
  }

  @Nonnull
  public DataTables setAutoWidth (final boolean bAutoWidth)
  {
    this.m_bAutoWidth = bAutoWidth;
    return this;
  }

  public boolean isPaginate ()
  {
    return this.m_bPaginate;
  }

  @Nonnull
  public DataTables setPaginate (final boolean bPaginate)
  {
    this.m_bPaginate = bPaginate;
    return this;
  }

  public boolean isStateSave ()
  {
    return this.m_bStateSave;
  }

  @Nonnull
  public DataTables setStateSave (final boolean bStateSave)
  {
    this.m_bStateSave = bStateSave;
    return this;
  }

  public boolean isJQueryUI ()
  {
    return this.m_bJQueryUI;
  }

  @Nonnull
  public DataTables setJQueryUI (final boolean bJQueryUI)
  {
    this.m_bJQueryUI = bJQueryUI;
    return this;
  }

  @Nullable
  public DataTablesColumn getColumn (@Nonnegative final int nIndex)
  {
    return ContainerHelper.getSafe (this.m_aColumns, nIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <DataTablesColumn> getAllColumns ()
  {
    return ContainerHelper.newList (this.m_aColumns);
  }

  public boolean hasColumns ()
  {
    return !this.m_aColumns.isEmpty ();
  }

  @Nonnegative
  public int getColumnCount ()
  {
    return this.m_aColumns.size ();
  }

  @Nonnull
  public DataTablesColumn getOrCreateColumnOfTarget (@Nonnegative final int nTarget)
  {
    for (final DataTablesColumn aCurColumn : this.m_aColumns)
      if (aCurColumn.hasTarget (nTarget))
        return aCurColumn;

    final DataTablesColumn aColumn = new DataTablesColumn (nTarget);
    this.m_aColumns.add (aColumn);
    return aColumn;
  }

  @Nonnull
  public DataTables addColumn (@Nonnull final DataTablesColumn aColumn)
  {
    ValueEnforcer.notNull (aColumn, "Column");

    // Check if targets are unique!
    for (final int nTarget : aColumn.getAllTargets ())
      for (final DataTablesColumn aCurColumn : this.m_aColumns)
        if (aCurColumn.hasTarget (nTarget))
        {
          s_aLogger.warn ("Another DataTablesColumn with target " + nTarget + " is already contained!");
          break;
        }
    this.m_aColumns.add (aColumn);
    return this;
  }

  @Nonnull
  public DataTables addAllColumns (@Nonnull final IHCTable <?> aTable)
  {
    ValueEnforcer.notNull (aTable, "Table");

    // Add all columns
    final HCColGroup aColGroup = aTable.getColGroup ();
    if (aColGroup != null)
    {
      int nColIndex = 0;
      for (final HCCol aCol : aColGroup.getAllColumns ())
      {
        final DataTablesColumn aColumn = new DataTablesColumn (nColIndex);
        if (!aCol.isStar ())
          aColumn.setWidth (aCol.getWidth ());
        addColumn (aColumn);
        ++nColIndex;
      }
    }
    return this;
  }

  @Nonnull
  public DataTables setAllColumnsSortable (final boolean bSortable)
  {
    for (final DataTablesColumn aColumn : this.m_aColumns)
      aColumn.setSortable (bSortable);
    return this;
  }

  @Nonnull
  public DataTables setAllColumnsSearchable (final boolean bSearchable)
  {
    for (final DataTablesColumn aColumn : this.m_aColumns)
      aColumn.setSearchable (bSearchable);
    return this;
  }

  public boolean hasAnyInvisibleColumn ()
  {
    for (final DataTablesColumn aColumn : this.m_aColumns)
      if (!aColumn.isVisible ())
        return true;
    return false;
  }

  @Nonnegative
  public int getVisibleColumnCount ()
  {
    int ret = 0;
    for (final DataTablesColumn aColumn : this.m_aColumns)
      if (aColumn.isVisible ())
        ++ret;
    return ret;
  }

  @Nullable
  public DataTablesSorting getInitialSorting ()
  {
    return this.m_aInitialSorting;
  }

  @Nonnull
  public DataTables setInitialSorting (@Nonnegative final int nIndex, @Nonnull final ESortOrder eSortOrder)
  {
    return setInitialSorting (new DataTablesSorting ().addColumn (nIndex, eSortOrder));
  }

  @Nonnull
  public DataTables setInitialSorting (@Nullable final DataTablesSorting aInitialSorting)
  {
    this.m_aInitialSorting = aInitialSorting;
    return this;
  }

  @Nullable
  public EDataTablesPaginationType getPaginationType ()
  {
    return this.m_ePaginationType;
  }

  @Nonnull
  public DataTables setPaginationType (@Nullable final EDataTablesPaginationType ePaginationType)
  {
    this.m_ePaginationType = ePaginationType;
    return this;
  }

  @Nullable
  public String getScrollX ()
  {
    return this.m_sScrollX;
  }

  @Nonnull
  public DataTables setScrollX (@Nullable final String sScrollX)
  {
    this.m_sScrollX = sScrollX;
    return this;
  }

  @Nullable
  public String getScrollXInner ()
  {
    return this.m_sScrollXInner;
  }

  @Nonnull
  public DataTables setScrollXInner (@Nullable final String sScrollXInner)
  {
    this.m_sScrollXInner = sScrollXInner;
    return this;
  }

  @Nullable
  public String getScrollY ()
  {
    return this.m_sScrollY;
  }

  @Nonnull
  public DataTables setScrollY (@Nullable final String sScrollY)
  {
    this.m_sScrollY = sScrollY;
    return this;
  }

  public boolean isScrollAutoCSS ()
  {
    return this.m_bScrollAutoCSS;
  }

  @Nonnull
  public DataTables setScrollAutoCSS (final boolean bScrollAutoCSS)
  {
    this.m_bScrollAutoCSS = bScrollAutoCSS;
    return this;
  }

  public boolean isScrollCollapse ()
  {
    return this.m_bScrollCollapse;
  }

  @Nonnull
  public DataTables setScrollCollapse (final boolean bScrollCollapse)
  {
    this.m_bScrollCollapse = bScrollCollapse;
    return this;
  }

  @Nullable
  public String getDom ()
  {
    return this.m_sDom;
  }

  @Nonnull
  public DataTables setDom (@Nullable final String sDom)
  {
    this.m_sDom = sDom;
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <Integer, String> getLengthMenu ()
  {
    return ContainerHelper.newMap (this.m_aLengthMenu);
  }

  @Nonnull
  public DataTables setLengthMenu (@Nullable final int... aLength)
  {
    if (aLength == null)
      this.m_aLengthMenu = null;
    else
    {
      final Map <Integer, String> aLengthMenu = new LinkedHashMap <Integer, String> ();
      for (final int nValue : aLength)
        aLengthMenu.put (Integer.valueOf (nValue), null);
      this.m_aLengthMenu = aLengthMenu;
    }
    return this;
  }

  @Nonnull
  public DataTables setLengthMenu (@Nullable final Map <Integer, String> aLength)
  {
    this.m_aLengthMenu = aLength == null ? null : ContainerHelper.newOrderedMap (aLength);
    return this;
  }

  @Nonnegative
  public int getDisplayLength ()
  {
    return this.m_nDisplayLength;
  }

  /**
   * Set to -1 to show all
   * 
   * @param nDisplayLength
   *        Number of items to display per page
   * @return this
   */
  @Nonnull
  public DataTables setDisplayLength (final int nDisplayLength)
  {
    this.m_nDisplayLength = nDisplayLength;
    return this;
  }

  // Server side handling params

  @Nullable
  public ISimpleURL getAjaxSource ()
  {
    return this.m_aAjaxSource;
  }

  @Nonnull
  public DataTables setAjaxSource (@Nullable final ISimpleURL aAjaxSource)
  {
    this.m_aAjaxSource = aAjaxSource;
    return this;
  }

  @Nullable
  public EHTTPMethod getServerMethod ()
  {
    return this.m_eServerMethod;
  }

  @Nonnull
  public DataTables setServerMethod (@Nullable final EHTTPMethod eServerMethod)
  {
    this.m_eServerMethod = eServerMethod;
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAllServerParams ()
  {
    return ContainerHelper.newMap (this.m_aServerParams);
  }

  @Nonnull
  public DataTables setServerParams (@Nullable final Map <String, String> aServerParams)
  {
    this.m_aServerParams = aServerParams;
    return this;
  }

  public boolean isUseJQueryAjax ()
  {
    return this.m_bUseJQueryAjax;
  }

  @Nonnull
  public DataTables setUseJQueryAjax (final boolean bUseJQueryAjax)
  {
    this.m_bUseJQueryAjax = bUseJQueryAjax;
    return this;
  }

  @Nonnull
  public EDataTablesFilterType getServerFilterType ()
  {
    return this.m_eServerFilterType;
  }

  @Nonnull
  public DataTables setServerFilterType (@Nonnull final EDataTablesFilterType eServerFilterType)
  {
    this.m_eServerFilterType = ValueEnforcer.notNull (eServerFilterType, "ServerFilterType");
    return this;
  }

  public boolean isDeferRender ()
  {
    return this.m_bDeferRender;
  }

  @Nonnull
  public DataTables setDeferRender (final boolean bDeferRender)
  {
    this.m_bDeferRender = bDeferRender;
    return this;
  }

  @Nullable
  public ISimpleURL getTextLoadingURL ()
  {
    return this.m_aTextLoadingURL;
  }

  @Nullable
  public String getextLoadingURLLocaleParameterName ()
  {
    return this.m_sTextLoadingURLLocaleParameterName;
  }

  @Nonnull
  public DataTables setTextLoadingURL (@Nullable final ISimpleURL aTextLoadingURL,
                                       @Nullable final String sTextLoadingURLLocaleParameterName)
  {
    if (aTextLoadingURL != null && StringHelper.hasNoText (sTextLoadingURLLocaleParameterName))
      throw new IllegalArgumentException ("If a text loading URL is present, a text loading URL locale parameter name must also be present");
    this.m_aTextLoadingURL = aTextLoadingURL;
    this.m_sTextLoadingURLLocaleParameterName = sTextLoadingURLLocaleParameterName;
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAllTextLoadingParams ()
  {
    return ContainerHelper.newMap (this.m_aTextLoadingParams);
  }

  @Nonnull
  public DataTables setTextLoadingParams (@Nullable final Map <String, String> aTextLoadingParams)
  {
    this.m_aTextLoadingParams = aTextLoadingParams;
    return this;
  }

  public boolean isUseFixedHeader ()
  {
    return this.m_bUseFixedHeader;
  }

  @Nonnull
  public DataTables setUseFixedHeader (final boolean bUseFixedHeader)
  {
    this.m_bUseFixedHeader = bUseFixedHeader;
    return this;
  }

  @Nullable
  @ReturnsMutableObject (reason = "design")
  public JSAssocArray getFixedHeaderOptions ()
  {
    return this.m_aFixedHeaderOptions;
  }

  @Nonnull
  public DataTables setFixedHeaderOptions (@Nullable final JSAssocArray aFixedHeaderOptions)
  {
    this.m_aFixedHeaderOptions = aFixedHeaderOptions;
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

  @Nonnull
  public static IJSONObject createLanguageJson (@Nonnull final Locale aDisplayLocale)
  {
    final IJSONObject aLanguage = new JSONObject ();
    aLanguage.set ("oAria",
                   new JSONObject ().set ("sSortAscending",
                                          EDataTablesText.SORT_ASCENDING.getDisplayText (aDisplayLocale))
                                    .set ("sSortDescending",
                                          EDataTablesText.SORT_DESCENDING.getDisplayText (aDisplayLocale)));
    aLanguage.set ("sDecimal", DecimalFormatSymbolsFactory.getInstance (aDisplayLocale).getDecimalSeparator ());
    aLanguage.set ("sEmptyTable", EDataTablesText.EMPTY_TABLE.getDisplayText (aDisplayLocale));
    aLanguage.set ("sInfo", EDataTablesText.INFO.getDisplayText (aDisplayLocale));
    aLanguage.set ("sInfoEmpty", EDataTablesText.INFO_EMPTY.getDisplayText (aDisplayLocale));
    aLanguage.set ("sInfoFiltered", EDataTablesText.INFO_FILTERED.getDisplayText (aDisplayLocale));
    aLanguage.set ("sInfoPostFix", EDataTablesText.INFO_POSTFIX.getDisplayText (aDisplayLocale));
    aLanguage.set ("sLengthMenu", EDataTablesText.LENGTH_MENU.getDisplayText (aDisplayLocale));
    aLanguage.set ("sLoadingRecords", EDataTablesText.LOADING_RECORDS.getDisplayText (aDisplayLocale));
    aLanguage.set ("oPaginate",
                   new JSONObject ().set ("sFirst", EDataTablesText.FIRST.getDisplayText (aDisplayLocale))
                                    .set ("sLast", EDataTablesText.LAST.getDisplayText (aDisplayLocale))
                                    .set ("sNext", EDataTablesText.NEXT.getDisplayText (aDisplayLocale))
                                    .set ("sPrevious", EDataTablesText.PREVIOUS.getDisplayText (aDisplayLocale)));
    aLanguage.set ("sProcessing", EDataTablesText.PROCESSING.getDisplayText (aDisplayLocale));
    aLanguage.set ("sSearch", EDataTablesText.SEARCH.getDisplayText (aDisplayLocale));
    aLanguage.set ("sInfoThousands", EDataTablesText.INFO_THOUSANDS.getDisplayText (aDisplayLocale));
    aLanguage.set ("sUrl", "");
    aLanguage.set ("sZeroRecords", EDataTablesText.ZERO_RECORDS.getDisplayText (aDisplayLocale));
    return aLanguage;
  }

  @OverrideOnDemand
  protected void onRegisterExternalResources ()
  {}

  @Override
  @Nullable
  public final IHCNode build ()
  {
    registerExternalResources ();
    if (this.m_bUseFixedHeader)
    {
      PerRequestJSIncludes.registerJSIncludeForThisRequest (EDataTablesJSPathProvider.EXTRAS_FIXED_HEADER);
      PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EDataTablesCSSPathProvider.EXTRAS_FIXED_HEADER);
    }
    onRegisterExternalResources ();

    // init parameters
    final JSAssocArray aParams = new JSAssocArray ();
    if (this.m_bAutoWidth != DEFAULT_AUTOWIDTH)
      aParams.add ("bAutoWidth", this.m_bAutoWidth);
    if (this.m_bPaginate != DEFAULT_PAGINATE)
      aParams.add ("bPaginate", this.m_bPaginate);
    if (this.m_bStateSave != DEFAULT_STATE_SAVE)
      aParams.add ("bStateSave", this.m_bStateSave);
    if (this.m_bJQueryUI != DEFAULT_JQUERY_UI)
      aParams.add ("bJQueryUI", this.m_bJQueryUI);
    if (!this.m_aColumns.isEmpty ())
    {
      final JSArray aArray = new JSArray ();
      for (final DataTablesColumn aColumn : this.m_aColumns)
        aArray.add (aColumn.getAsJS ());
      aParams.add ("aoColumnDefs", aArray);
    }
    // Provide any empty array if no sorting is defined, because otherwise an
    // implicit sorting of the first column, ascending is done
    aParams.add ("aaSorting", this.m_aInitialSorting != null ? this.m_aInitialSorting.getAsJS () : new JSArray ());
    if (this.m_ePaginationType != null)
      aParams.add ("sPaginationType", this.m_ePaginationType.getName ());
    if (StringHelper.hasText (this.m_sScrollX))
      aParams.add ("sScrollX", this.m_sScrollX);
    if (StringHelper.hasText (this.m_sScrollXInner))
      aParams.add ("sScrollXInner", this.m_sScrollXInner);
    if (StringHelper.hasText (this.m_sScrollY))
      aParams.add ("sScrollY", this.m_sScrollY);
    if (this.m_bScrollAutoCSS != DEFAULT_SCROLL_AUTO_CSS)
      aParams.add ("bScrollAutoCss", this.m_bScrollAutoCSS);
    if (this.m_bScrollCollapse != DEFAULT_SCROLL_COLLAPSE)
      aParams.add ("bScrollCollapse", this.m_bScrollCollapse);
    if (StringHelper.hasText (this.m_sDom))
      aParams.add ("sDom", this.m_sDom);
    if (this.m_aLengthMenu != null && !this.m_aLengthMenu.isEmpty ())
    {
      final JSArray aArray1 = new JSArray ();
      final JSArray aArray2 = new JSArray ();
      for (final Map.Entry <Integer, String> aEntry : this.m_aLengthMenu.entrySet ())
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
    if (this.m_nDisplayLength != DEFAULT_DISPLAY_LENGTH)
      aParams.add ("iDisplayLength", this.m_nDisplayLength);

    // Server handling parameters
    final boolean bServerSide = this.m_aAjaxSource != null;
    if (bServerSide)
    {
      aParams.add ("bServerSide", true);
      // This copies the content of the table
      final DataTablesServerData aServerData = new DataTablesServerData (this.m_aTable,
                                                                         this.m_aColumns,
                                                                         this.m_aDisplayLocale,
                                                                         this.m_eServerFilterType);
      UIStateRegistry.getCurrent ().registerState (this.m_aTable.getID (), aServerData);
      // Remove all body rows to avoid initial double painting, as the most
      // reasonable state is retrieved from the server!
      this.m_aTable.removeAllBodyRows ();
    }

    if (hasAnyInvisibleColumn ())
    {
      // Remove all columns as this breaks the rendering
      this.m_aTable.removeAllColumns ();
    }

    if (this.m_aAjaxSource != null)
      aParams.add ("sAjaxSource", this.m_aAjaxSource.getAsString ());
    if (this.m_eServerMethod != null)
      aParams.add ("sServerMethod", this.m_eServerMethod.getName ());
    if (ContainerHelper.isNotEmpty (this.m_aServerParams))
    {
      final JSAnonymousFunction aAF = new JSAnonymousFunction ();
      final JSVar aData = aAF.param ("aoData");
      for (final Map.Entry <String, String> aEntry : this.m_aServerParams.entrySet ())
      {
        aAF.body ()
           .invoke (aData, "push")
           .arg (new JSAssocArray ().add ("name", aEntry.getKey ()).add ("value", aEntry.getValue ()));
      }
      aParams.add ("fnServerParams", aAF);
    }
    if (this.m_bUseJQueryAjax)
    {
      final JSAnonymousFunction aAF = new JSAnonymousFunction ();
      final JSVar sSource = aAF.param ("s");
      final JSVar aoData = aAF.param ("t");
      final JSVar fnCallback = aAF.param ("u");
      final JSVar oSettings = aAF.param ("v");
      final JQueryAjaxBuilder aAjaxBuilder = new JQueryAjaxBuilder ().cache (false)
                                                                     .dataType ("json")
                                                                     .type (this.m_eServerMethod == null ? null
                                                                                                         : this.m_eServerMethod.getName ())
                                                                     .url (sSource)
                                                                     .data (aoData)
                                                                     .success (JSJQueryUtils.jqueryAjaxSuccessHandler (fnCallback,
                                                                                                                       true));
      aAF.body ().assign (oSettings.ref ("jqXHR"), aAjaxBuilder.build ());
      aParams.add ("fnServerData", aAF);
      JSJQueryUtils.registerResources ();
    }
    if (this.m_bDeferRender != DEFAULT_DEFER_RENDER)
      aParams.add ("bDeferRender", this.m_bDeferRender);

    // Display texts
    if (this.m_aDisplayLocale != null)
    {
      IJSONObject aLanguage;
      if (this.m_aTextLoadingURL != null)
      {
        // Load texts from there
        final SimpleURL aFinalURL = new SimpleURL (this.m_aTextLoadingURL).add (this.m_sTextLoadingURLLocaleParameterName,
                                                                                this.m_aDisplayLocale.getLanguage ());
        if (this.m_aTextLoadingParams != null)
          aFinalURL.addAll (this.m_aTextLoadingParams);
        aLanguage = new JSONObject ().set ("sUrl", aFinalURL.getAsString ());
      }
      else
      {
        // Inline texts
        aLanguage = createLanguageJson (this.m_aDisplayLocale);
      }
      aParams.add ("oLanguage", aLanguage);
    }

    modifyParams (aParams);

    // main on document ready code
    final JSPackage aJSCode = new JSPackage ();

    addCodeBeforeDataTables (aJSCode);
    final JSVar aJSTable = aJSCode.var (this.m_sGeneratedJSVariableName,
                                        JQuery.idRef (this.m_aTable).invoke ("dataTable").arg (aParams));
    if (this.m_bUseFixedHeader)
    {
      final JSInvocation aJSFixedHeader = new JSInvocation ("new FixedHeader").arg (aJSTable);
      if (this.m_aFixedHeaderOptions != null)
        aJSFixedHeader.arg (this.m_aFixedHeaderOptions);
      aJSCode.add (aJSFixedHeader);
    }
    addCodeAfterDataTables (aJSCode, aJSTable);

    return getWrapped (aJSCode);
  }

  /**
   * Depending on {@link #isGenerateOnDocumentReady()} wrap the passed JSCode in
   * the matching HTML element (either {@link HCScriptOnDocumentReady} or in
   * {@link HCScript}.
   * 
   * @param aJSCode
   *        The JS code to be wrapped. May not be <code>null</code>.
   * @return The non-<code>null</code> HCNode
   */
  @Nonnull
  public IHCNode getWrapped (@Nonnull final IJSCodeProvider aJSCode)
  {
    return this.m_bGenerateOnDocumentReady ? new HCScriptOnDocumentReady (aJSCode) : new HCScript (aJSCode);
  }

  /**
   * @return The name of the JS variable that contains the dataTable object. The
   *         scope of the variable depends on the state of the
   *         {@link #isGenerateOnDocumentReady()} method.
   */
  @Nonnull
  @Nonempty
  public final String getJSVariableName ()
  {
    return this.m_sGeneratedJSVariableName;
  }

  /**
   * Create an {@link HCScript} or {@link HCScriptOnDocumentReady} block that
   * handles expand and collapse. The following pre-conditions must be met: The
   * first column must be the expand/collapse column and it must contain an
   * image where the event handler is registered.
   * 
   * @param aExpandImgURL
   *        The URL of the expand icon (closed state)
   * @param aCollapseImgURL
   *        The URL of the collapse icon (open state)
   * @param nColumnIndexWithDetails
   *        The index of the column that contains the details. Must be &ge; 0
   *        and is usually hidden.
   * @param aCellClass
   *        The CSS class to be applied to the created cell. May be
   *        <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public IHCNode createExpandCollapseHandling (@Nonnull final ISimpleURL aExpandImgURL,
                                               @Nonnull final ISimpleURL aCollapseImgURL,
                                               @Nonnegative final int nColumnIndexWithDetails,
                                               @Nullable final ICSSClassProvider aCellClass)
  {
    final DataTablesColumn aColumn = getColumn (nColumnIndexWithDetails);
    if (aColumn != null && aColumn.isVisible ())
      s_aLogger.warn ("The column with the expand text, should not be visible!");

    final JSRef jsTable = JSExpr.ref (this.m_sGeneratedJSVariableName);

    final JSPackage aPackage = new JSPackage ();
    final JSAnonymousFunction aOpenCloseCallback = new JSAnonymousFunction ();
    {
      final JSVar jsTR = aOpenCloseCallback.body ()
                                           .var ("r", JQuery.jQueryThis ().parents (EHTMLElement.TR).component0 ());
      final JSConditional aIf = aOpenCloseCallback.body ()._if (jsTable.invoke ("fnIsOpen").arg (jsTR));
      aIf._then ().assign (JSExpr.THIS.ref (CHTMLAttributes.SRC), aExpandImgURL.getAsString ());
      aIf._then ().invoke (jsTable, "fnClose").arg (jsTR);
      aIf._else ().assign (JSExpr.THIS.ref (CHTMLAttributes.SRC), aCollapseImgURL.getAsString ());
      aIf._else ()
         .invoke (jsTable, "fnOpen")
         .arg (jsTR)
         .arg (jsTable.invoke ("fnGetData").arg (jsTR).component (nColumnIndexWithDetails))
         .arg (aCellClass == null ? null : aCellClass.getCSSClass ());
    }
    aPackage.add (JQuery.idRef (this.m_aTable)
                        .on ()
                        .arg ("click")
                        .arg (new JQuerySelectorList (JQuerySelector.element (EHTMLElement.TBODY),
                                                      JQuerySelector.element (EHTMLElement.TD),
                                                      JQuerySelector.element (EHTMLElement.IMG)))
                        .arg (aOpenCloseCallback));
    return getWrapped (aPackage);
  }

  /**
   * @param aRowSelect
   *        E.g. <code>JQuery.jQueryThis ().parents (EHTMLElement.TR)</code>
   * @param bSwapUsingJQuery
   *        Use it only, if if no actions can be performed on the table! This is
   *        much quicker.
   * @return The created JS code
   */
  @Nonnull
  public IJSCodeProvider getMoveRowUpCode (@Nonnull final JQueryInvocation aRowSelect, final boolean bSwapUsingJQuery)
  {
    final JSRef jsTable = JSExpr.ref (this.m_sGeneratedJSVariableName);

    final JSPackage aPackage = new JSPackage ();
    final JSVar aRow = aPackage.var ("row", aRowSelect);
    final JSVar aPrevRow = aPackage.var ("prow", aRow.invoke ("prev"));
    final JSBlock aIfPrev = aPackage._if (aPrevRow.ref ("length").gt (0))._then ();

    if (bSwapUsingJQuery)
    {
      // This is much quicker, if sorting and searching is disabled
      aIfPrev.add (aRow.invoke ("detach"));
      aIfPrev.add (aPrevRow.invoke ("before").arg (aRow));
    }
    else
    {
      final JSVar aRow0 = aIfPrev.var ("row0", aRow.invoke ("get").arg (0));
      final JSVar aPrevRow0 = aIfPrev.var ("prow0", aPrevRow.invoke ("get").arg (0));

      final JSVar aData = aIfPrev.var ("data", jsTable.invoke ("fnGetData").arg (aRow0));
      final JSVar aPrevData = aIfPrev.var ("prevdata", jsTable.invoke ("fnGetData").arg (aPrevRow0));

      aIfPrev.invoke (jsTable, "fnUpdate").arg (aPrevData).arg (aRow0);
      aIfPrev.invoke (jsTable, "fnUpdate").arg (aData).arg (aPrevRow0);
    }
    return aPackage;
  }

  /**
   * @param aRowSelect
   *        E.g. <code>JQuery.jQueryThis ().parents (EHTMLElement.TR)</code>
   * @param bSwapUsingJQuery
   *        Use it only, if if no actions can be performed on the table! This is
   *        much quicker.
   * @return The created JS code
   */
  @Nonnull
  public IJSCodeProvider getMoveRowDownCode (@Nonnull final JQueryInvocation aRowSelect, final boolean bSwapUsingJQuery)
  {
    final JSRef jsTable = JSExpr.ref (this.m_sGeneratedJSVariableName);

    final JSPackage aPackage = new JSPackage ();
    final JSVar aRow = aPackage.var ("row", aRowSelect);
    final JSVar aNextRow = aPackage.var ("nrow", aRow.invoke ("next"));
    final JSBlock aIfNext = aPackage._if (aNextRow.ref ("length").gt (0))._then ();

    if (bSwapUsingJQuery)
    {
      // This is much quicker, if sorting and searching is disabled
      aIfNext.add (aRow.invoke ("detach"));
      aIfNext.add (aNextRow.invoke ("after").arg (aRow));
    }
    else
    {
      final JSVar aRow0 = aIfNext.var ("row0", aRow.invoke ("get").arg (0));
      final JSVar aNextRow0 = aIfNext.var ("nrow0", aNextRow.invoke ("get").arg (0));

      final JSVar aData = aIfNext.var ("data", jsTable.invoke ("fnGetData").arg (aRow0));
      final JSVar aNextData = aIfNext.var ("nextdata", jsTable.invoke ("fnGetData").arg (aNextRow0));

      aIfNext.invoke (jsTable, "fnUpdate").arg (aNextData).arg (aRow0);
      aIfNext.invoke (jsTable, "fnUpdate").arg (aData).arg (aNextRow0);
    }
    return aPackage;
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EDataTablesJSPathProvider.DATATABLES_1_10);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EDataTablesCSSPathProvider.DATATABLES_1_10);
  }
}
