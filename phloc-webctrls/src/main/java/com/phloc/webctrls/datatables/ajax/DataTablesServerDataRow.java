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
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCRow;

/**
 * This class holds table rows to be used by the DataTables server side
 * handling.
 * 
 * @author Philip Helger
 */
public final class DataTablesServerDataRow implements Serializable
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DataTablesServerDataRow.class);

  private final String m_sRowID;
  private final String m_sRowClass;
  private final List <DataTablesServerDataCell> m_aCells;

  public DataTablesServerDataRow (@Nonnull final HCRow aRow, @Nonnull final IHCConversionSettings aCS)
  {
    if (aRow.hasAnyStyle ())
      s_aLogger.warn ("Cell has styles assigned which will be lost: " + aRow.getAllStyles ());

    m_sRowID = aRow.getID ();
    m_sRowClass = aRow.getAllClassesAsString ();
    m_aCells = new ArrayList <DataTablesServerDataCell> (aRow.getCellCount ());
    for (final AbstractHCCell aCell : aRow.getAllCells ())
      m_aCells.add (new DataTablesServerDataCell (aCell, aCS));
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

  /**
   * @return All CSS classes of the row as one big string
   */
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
  public List <DataTablesServerDataCell> directGetAllCells ()
  {
    return m_aCells;
  }

  @Nonnull
  public DataTablesServerDataCell getCellAtIndex (@Nonnegative final int nIndex)
  {
    return m_aCells.get (nIndex);
  }
}
