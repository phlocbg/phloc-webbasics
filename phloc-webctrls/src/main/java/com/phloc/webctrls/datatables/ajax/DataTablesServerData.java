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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import com.phloc.commons.xml.serialize.IXMLWriterSettings;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCRow;
import com.phloc.webbasics.state.IHasUIState;

public final class DataTablesServerData implements IHasUIState
{
  public static final class CellData
  {
    private final String m_sHTML;
    private final String m_sTextContent;

    public CellData (@Nonnull final AbstractHCCell aCell, @Nonnull final IHCConversionSettings aCS)
    {
      final IMicroNode aNode = aCell.getAllChildrenAsNodeList ().convertToNode (aCS);
      final IXMLWriterSettings aXWS = aCS.getXMLWriterSettings ();
      final String sHTML = MicroWriter.getNodeAsString (aNode, aXWS);
      final char cSep = aXWS.isUseDoubleQuotesForAttributes () ? '"' : '\'';
      final String sNamespaceToRemove = " xmlns=" + cSep + aCS.getHTMLVersion ().getNamespaceURI () + cSep;
      m_sHTML = StringHelper.replaceAll (sHTML, sNamespaceToRemove, "");

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

    public boolean matchesRegEx (@Nonnull final String sSearchText)
    {
      return RegExHelper.stringMatchesPattern (sSearchText, m_sTextContent);
    }

    public boolean matchesPlain (@Nonnull final String sSearchText, @Nonnull final Locale aDisplayLocale)
    {
      return StringHelper.containsIgnoreCase (m_sTextContent, sSearchText, aDisplayLocale);
    }

    @Override
    @Nonnull
    public String toString ()
    {
      return new ToStringGenerator (this).append ("html", m_sHTML).append ("textContent", m_sTextContent).toString ();
    }
  }

  public static final class RowData
  {
    private final String m_sRowID;
    private final String m_sRowClass;
    private final List <CellData> m_aCells;

    public RowData (@Nonnull final HCRow aRow, @Nonnull final IHCConversionSettings aCS)
    {
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

  public static final ObjectType OBJECT_TYPE = new ObjectType ("datatables");

  private ServerSortState m_aServerSortState = new ServerSortState ();
  private final List <RowData> m_aRows;
  private final Locale m_aDisplayLocale;

  public DataTablesServerData (@Nonnull final AbstractHCBaseTable <?> aTable, @Nonnull final Locale aDisplayLocale)
  {
    if (aTable == null)
      throw new NullPointerException ("table");
    if (aDisplayLocale == null)
      throw new NullPointerException ("displayLocale");
    final IHCConversionSettings aCS = HCSettings.getConversionSettings (GlobalDebug.isDebugMode ());
    m_aRows = new ArrayList <RowData> (aTable.getBodyRowCount ());
    for (final HCRow aRow : aTable.getAllBodyRows ())
      m_aRows.add (new RowData (aRow, aCS));
    m_aDisplayLocale = aDisplayLocale;
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

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("serverSortState", m_aServerSortState).toString ();
  }
}
