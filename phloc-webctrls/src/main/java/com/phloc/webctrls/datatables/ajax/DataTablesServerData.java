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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroNodeWithChildren;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;
import com.phloc.html.hc.conversion.HCConversionSettings;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCRow;
import com.phloc.webbasics.state.IHasUIState;
import com.phloc.webctrls.datatables.DataTablesColumn;
import com.phloc.webctrls.datatables.EDataTablesFilterType;

public final class DataTablesServerData implements IHasUIState
{
  static final class CellData implements Serializable
  {
    private static final Logger s_aLogger = LoggerFactory.getLogger (CellData.class);

    private final String m_sHTML;
    private final String m_sTextContent;

    public CellData (@Nonnull final AbstractHCCell aCell, @Nonnull final IHCConversionSettings aCS)
    {
      if (aCell.hasAnyStyle ())
        s_aLogger.warn ("Cell has styles assigned which will be lost: " + aCell.getAllStyles ());
      if (aCell.hasAnyClass ())
        s_aLogger.warn ("Cell has classes assigned which will be lost: " + aCell.getAllClasses ());

      final IMicroNode aNode = aCell.getAllChildrenAsNodeList ().convertToNode (aCS);
      m_sHTML = MicroWriter.getNodeAsString (aNode, aCS.getXMLWriterSettings ());

      if (aNode instanceof IMicroNodeWithChildren)
        m_sTextContent = ((IMicroNodeWithChildren) aNode).getTextContent ();
      else
        if (aNode.isText ())
        {
          // ignore whitespace-only content
          if (!((IMicroText) aNode).isElementContentWhitespace ())
            m_sTextContent = aNode.getNodeValue ();
          else
            m_sTextContent = null;
        }
        else
          if (aNode.isCDATA ())
          {
            m_sTextContent = aNode.getNodeValue ();
          }
          else
            m_sTextContent = null;
    }

    @Nullable
    public String getHTML ()
    {
      return m_sHTML;
    }

    @Nonnull
    public String getTextContent ()
    {
      return m_sTextContent;
    }

    public void matchRegEx (@Nonnull final String [] aSearchTexts, @Nonnull final BitSet aMatchingWords)
    {
      for (int i = 0; i < aSearchTexts.length; ++i)
      {
        final String sSearchText = aSearchTexts[i];
        if (RegExHelper.stringMatchesPattern (sSearchText, m_sTextContent))
          aMatchingWords.set (i);
      }
    }

    public void matchPlainTextCaseSensitive (@Nonnull final String [] aSearchTexts, @Nonnull final BitSet aMatchingWords)
    {
      for (int i = 0; i < aSearchTexts.length; ++i)
      {
        final String sSearchText = aSearchTexts[i];
        if (StringHelper.contains (m_sTextContent, sSearchText))
          aMatchingWords.set (i);
      }
    }

    public void matchPlainTextIgnoreCase (@Nonnull final String [] aSearchTexts,
                                          @Nonnull final Locale aDisplayLocale,
                                          @Nonnull final BitSet aMatchingWords)
    {
      for (int i = 0; i < aSearchTexts.length; ++i)
      {
        final String sSearchText = aSearchTexts[i];
        if (StringHelper.containsIgnoreCase (m_sTextContent, sSearchText, aDisplayLocale))
          aMatchingWords.set (i);
      }
    }

    @Override
    @Nonnull
    public String toString ()
    {
      return new ToStringGenerator (this).append ("html", m_sHTML).append ("textContent", m_sTextContent).toString ();
    }
  }

  static final class RowData implements Serializable
  {
    private static final Logger s_aLogger = LoggerFactory.getLogger (RowData.class);

    private final String m_sRowID;
    private final String m_sRowClass;
    private final List <CellData> m_aCells;

    public RowData (@Nonnull final HCRow aRow, @Nonnull final IHCConversionSettings aCS)
    {
      if (aRow.hasAnyStyle ())
        s_aLogger.warn ("Cell has styles assigned which will be lost: " + aRow.getAllStyles ());

      m_sRowID = aRow.getID ();
      m_sRowClass = aRow.getAllClassesAsString ();
      m_aCells = new ArrayList <CellData> (aRow.getCellCount ());
      for (final AbstractHCCell aCell : aRow.getAllCells ())
        m_aCells.add (new CellData (aCell, aCS));
    }

    @Nullable
    public String getRowID ()
    {
      return m_sRowID;
    }

    public boolean hasRowID ()
    {
      return StringHelper.hasText (m_sRowID);
    }

    @Nullable
    public String getRowClass ()
    {
      return m_sRowClass;
    }

    public boolean hasRowClass ()
    {
      return StringHelper.hasText (m_sRowClass);
    }

    @Nonnull
    @ReturnsMutableObject (reason = "speed")
    public List <CellData> directGetAllCells ()
    {
      return m_aCells;
    }

    @Nonnull
    public CellData getCellAtIndex (@Nonnegative final int nIndex)
    {
      return m_aCells.get (nIndex);
    }
  }

  static final class ColumnData implements Serializable
  {
    private final Comparator <String> m_aComparator;

    private ColumnData (@Nonnull final Comparator <String> aComparator)
    {
      m_aComparator = aComparator;
    }

    public Comparator <String> getComparator ()
    {
      return m_aComparator;
    }

    @Nullable
    public static ColumnData create (@Nullable final Comparator <String> aComparator)
    {
      return aComparator == null ? null : new ColumnData (aComparator);
    }
  }

  public static final ObjectType OBJECT_TYPE = new ObjectType ("datatables");

  private final ColumnData [] m_aColumns;
  private final List <RowData> m_aRows;
  private final Locale m_aDisplayLocale;
  private ServerSortState m_aServerSortState;
  private final EDataTablesFilterType m_eFilterType;

  public DataTablesServerData (@Nonnull final AbstractHCBaseTable <?> aTable,
                               @Nonnull final List <DataTablesColumn> aColumns,
                               @Nonnull final Locale aDisplayLocale,
                               @Nonnull final EDataTablesFilterType eFilterType)
  {
    if (aTable == null)
      throw new NullPointerException ("table");
    if (aColumns == null)
      throw new NullPointerException ("columns");
    if (aDisplayLocale == null)
      throw new NullPointerException ("displayLocale");
    if (eFilterType == null)
      throw new NullPointerException ("searchType");

    // Column data
    final int nColumns = aTable.getColumnCount ();
    m_aColumns = new ColumnData [nColumns];
    for (final DataTablesColumn aColumn : aColumns)
    {
      final ColumnData aColumnData = ColumnData.create (aColumn.getComparator ());
      for (final int nTarget : aColumn.getAllTargets ())
      {
        if (nTarget < 0 || nTarget >= nColumns)
          throw new IllegalArgumentException ("DataTablesColumn is targeting illegal column index " +
                                              nTarget +
                                              "; must be >= 0 and < " +
                                              nColumns);
        m_aColumns[nTarget] = aColumnData;
      }
    }

    // Row data
    final IHCConversionSettings aCS = HCSettings.getConversionSettings (GlobalDebug.isDebugMode ());
    // Create HTML without namespaces
    final HCConversionSettings aRealCS = new HCConversionSettings (aCS);
    aRealCS.getXMLWriterSettings ().setEmitNamespaces (false);

    m_aRows = new ArrayList <RowData> (aTable.getBodyRowCount ());
    for (final HCRow aRow : aTable.getAllBodyRows ())
      m_aRows.add (new RowData (aRow, aRealCS));
    m_aDisplayLocale = aDisplayLocale;
    m_aServerSortState = new ServerSortState (this, aDisplayLocale);
    m_eFilterType = eFilterType;
  }

  @Nonnull
  public ObjectType getTypeID ()
  {
    return OBJECT_TYPE;
  }

  @Nonnull
  public ServerSortState getServerSortState ()
  {
    return m_aServerSortState;
  }

  @Nonnull
  public void setServerSortState (@Nonnull final ServerSortState aServerSortState)
  {
    if (aServerSortState == null)
      throw new NullPointerException ("serverSortState");
    m_aServerSortState = aServerSortState;
  }

  @Nonnegative
  public int getRowCount ()
  {
    return m_aRows.size ();
  }

  @Nonnull
  @ReturnsMutableObject (reason = "speed")
  public List <RowData> directGetAllRows ()
  {
    return m_aRows;
  }

  public void sortAllRows (@Nonnull final Comparator <RowData> aComp)
  {
    Collections.sort (m_aRows, aComp);
  }

  @Nonnull
  public Locale getDisplayLocale ()
  {
    return m_aDisplayLocale;
  }

  @Nullable
  public Comparator <String> getColumnComparator (@Nonnegative final int nColumnIndex)
  {
    final ColumnData aColumnData = m_aColumns[nColumnIndex];
    return aColumnData == null ? null : aColumnData.getComparator ();
  }

  @Nonnull
  public EDataTablesFilterType getFilterType ()
  {
    return m_eFilterType;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("displayLocale", m_aDisplayLocale)
                                       .append ("serverSortState", m_aServerSortState)
                                       .append ("filterType", m_eFilterType)
                                       .toString ();
  }
}
