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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;
import com.phloc.html.hc.api.IHCBaseTable;
import com.phloc.html.hc.conversion.HCConversionSettings;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.HCRow;
import com.phloc.webbasics.state.IHasUIState;
import com.phloc.webctrls.datatables.DataTablesColumn;
import com.phloc.webctrls.datatables.EDataTablesFilterType;

/**
 * This class holds tables to be used by the DataTables server side handling.
 * 
 * @author Philip Helger
 */
public final class DataTablesServerData implements IHasUIState
{
  /**
   * This class contains all data required for each column of a table.
   * 
   * @author Philip Helger
   */
  static final class ColumnData implements Serializable
  {
    private final Comparator <String> m_aComparator;

    private ColumnData (@Nonnull final Comparator <String> aComparator)
    {
      if (aComparator == null)
        throw new NullPointerException ("comparator");

      m_aComparator = aComparator;
    }

    @Nonnull
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
  private final List <DataTablesServerDataRow> m_aRows;
  private final Locale m_aDisplayLocale;
  private ServerSortState m_aServerSortState;
  private final EDataTablesFilterType m_eFilterType;

  public DataTablesServerData (@Nonnull final IHCBaseTable <?> aTable,
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
    final int nColumnCount = aTable.getColumnCount ();
    m_aColumns = new ColumnData [nColumnCount];
    for (final DataTablesColumn aColumn : aColumns)
    {
      final ColumnData aColumnData = ColumnData.create (aColumn.getComparator ());
      for (final int nTarget : aColumn.getAllTargets ())
      {
        if (nTarget < 0 || nTarget >= nColumnCount)
          throw new IllegalArgumentException ("DataTablesColumn is targeting illegal column index " +
                                              nTarget +
                                              "; must be >= 0 and < " +
                                              nColumnCount);
        m_aColumns[nTarget] = aColumnData;
      }
    }

    // Create HTML without namespaces
    final IHCConversionSettings aRealCS = createConversionSettings ();

    // Row data
    m_aRows = new ArrayList <DataTablesServerDataRow> (aTable.getBodyRowCount ());
    for (final HCRow aRow : aTable.getAllBodyRows ())
      m_aRows.add (new DataTablesServerDataRow (aRow, aRealCS));
    m_aDisplayLocale = aDisplayLocale;
    m_aServerSortState = new ServerSortState (this, aDisplayLocale);
    m_eFilterType = eFilterType;
  }

  /**
   * Create the HC conversion settings to be used for HTML serialization.
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static IHCConversionSettings createConversionSettings ()
  {
    final IHCConversionSettings aCS = HCSettings.getConversionSettings (GlobalDebug.isDebugMode ());
    // Create HTML without namespaces
    final HCConversionSettings aRealCS = new HCConversionSettings (aCS);
    aRealCS.getXMLWriterSettings ().setEmitNamespaces (false);
    return aRealCS;
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
  public List <DataTablesServerDataRow> directGetAllRows ()
  {
    return m_aRows;
  }

  public void sortAllRows (@Nonnull final Comparator <DataTablesServerDataRow> aComp)
  {
    if (aComp == null)
      throw new NullPointerException ("comp");

    Collections.sort (m_aRows, aComp);
  }

  @Nullable
  public DataTablesServerDataRow getRowOfID (@Nullable final String sID)
  {
    if (StringHelper.hasText (sID))
      for (final DataTablesServerDataRow aRow : m_aRows)
        if (sID.equals (aRow.getRowID ()))
          return aRow;
    return null;
  }

  @Nonnull
  public Locale getDisplayLocale ()
  {
    return m_aDisplayLocale;
  }

  @Nullable
  public Comparator <String> getColumnComparator (@Nonnegative final int nColumnIndex)
  {
    final ColumnData aColumnData = ArrayHelper.getSafeElement (m_aColumns, nColumnIndex);
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
