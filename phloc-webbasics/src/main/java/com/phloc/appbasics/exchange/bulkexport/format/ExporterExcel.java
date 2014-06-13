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
package com.phloc.appbasics.exchange.bulkexport.format;

import java.io.OutputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.WillClose;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.phloc.appbasics.exchange.EExchangeFileType;
import com.phloc.appbasics.exchange.bulkexport.EExportRecordType;
import com.phloc.appbasics.exchange.bulkexport.IExportRecord;
import com.phloc.appbasics.exchange.bulkexport.IExportRecordField;
import com.phloc.appbasics.exchange.bulkexport.IExportRecordProvider;
import com.phloc.appbasics.exchange.bulkexport.IExporterFile;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.type.EBaseType;
import com.phloc.datetime.CPDT;
import com.phloc.poi.excel.EExcelVersion;
import com.phloc.poi.excel.WorkbookCreationHelper;
import com.phloc.poi.excel.style.ExcelStyle;

/**
 * Export records to Excel workbook.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class ExporterExcel implements IExporterFile
{
  private static final ExcelStyle STYLE_DATE = new ExcelStyle ().setDataFormat ("dd.mm.yyyy");
  private static final ExcelStyle STYLE_TIME = new ExcelStyle ().setDataFormat ("hh:mm:ss");
  private static final ExcelStyle STYLE_DATETIME = new ExcelStyle ().setDataFormat ("dd.mm.yyyy hh:mm:ss");

  @Nonnull
  private final EExcelVersion m_eVersion;

  public ExporterExcel (@Nonnull final EExcelVersion eVersion)
  {
    m_eVersion = ValueEnforcer.notNull (eVersion, "Version");
  }

  @Nonnull
  public EExcelVersion getExcelVersion ()
  {
    return m_eVersion;
  }

  /**
   * Callback when a new row is created.
   * 
   * @param aWBCH
   *        The creation helper
   * @param eRecordType
   *        The record type
   * @param aRow
   *        The created row.
   * @param nRowIndex
   *        The 0-based total index of the row (including header)
   */
  @OverrideOnDemand
  protected void onAddRow (@Nonnull final WorkbookCreationHelper aWBCH,
                           @Nonnull final EExportRecordType eRecordType,
                           @Nonnull final Row aRow,
                           @Nonnegative final int nRowIndex)
  {}

  /**
   * Callback when a new row is created.
   * 
   * @param aWBCH
   *        The creation helper
   * @param eRecordType
   *        The record type
   * @param aCell
   *        The created cell.
   * @param nCellIndex
   *        The 0-based index of the cell in the current row
   * @param eBaseType
   *        The data type of the last cells data.
   */
  @OverrideOnDemand
  protected void onAddCell (@Nonnull final WorkbookCreationHelper aWBCH,
                            @Nonnull final EExportRecordType eRecordType,
                            @Nonnull final Cell aCell,
                            @Nonnegative final int nCellIndex,
                            @Nonnull final EBaseType eBaseType)
  {}

  private void _emitRecord (@Nonnull final WorkbookCreationHelper aWBCH,
                            @Nonnull final EExportRecordType eRecordType,
                            @Nonnull final IExportRecord aRecord)
  {
    final int nRowIndex = aWBCH.getRowCount ();
    final Row aRow = aWBCH.addRow ();

    // Callback
    onAddRow (aWBCH, eRecordType, aRow, nRowIndex);

    for (final IExportRecordField aField : aRecord.getAllFields ())
    {
      final Object aFieldValue = aField.getFieldValue ();
      if (aFieldValue == null)
      {
        // Add an empty cell
        aWBCH.addCell ();
      }
      else
      {
        final Cell aCell;
        final int nCellIndex = aWBCH.getCellCountInRow ();
        switch (aField.getFieldType ())
        {
          case BOOLEAN:
            aCell = aWBCH.addCell (((Boolean) aFieldValue).booleanValue ());
            break;
          case INT:
            aCell = aWBCH.addCell (((Number) aFieldValue).intValue ());
            break;
          case DOUBLE:
            aCell = aWBCH.addCell (((Number) aFieldValue).doubleValue ());
            break;
          case TEXT:
            aCell = aWBCH.addCell ((String) aFieldValue);
            break;
          case DATE:
            aCell = aWBCH.addCell (((LocalDate) aFieldValue).toDateTime (CPDT.NULL_LOCAL_TIME).toDate ());
            aWBCH.addCellStyle (STYLE_DATE);
            break;
          case TIME:
            aCell = aWBCH.addCell (((LocalTime) aFieldValue).toDateTime (CPDT.NULL_DATETIME).toDate ());
            aWBCH.addCellStyle (STYLE_TIME);
            break;
          case DATETIME:
            if (aFieldValue instanceof LocalDateTime)
              aCell = aWBCH.addCell (((LocalDateTime) aFieldValue).toDateTime ().toDate ());
            else
            {
              // Here we have a timezone -> use calendar
              aCell = aWBCH.addCell (((DateTime) aFieldValue).toGregorianCalendar ());
            }
            aWBCH.addCellStyle (STYLE_DATETIME);
            break;
          default:
            throw new IllegalArgumentException ("The type " + aField.getFieldType () + " cannot be written to Excel!");
        }

        // Callback
        onAddCell (aWBCH, eRecordType, aCell, nCellIndex, aField.getFieldType ());
      }
    }
  }

  @Nonnull
  public final ESuccess exportRecords (@Nonnull final IExportRecordProvider aProvider,
                                       @Nonnull @WillClose final OutputStream aOS)
  {
    try
    {
      ValueEnforcer.notNull (aProvider, "Provider");
      ValueEnforcer.notNull (aOS, "OutputStream");

      final WorkbookCreationHelper aWBCH = new WorkbookCreationHelper (m_eVersion);
      aWBCH.createNewSheet ();

      // Header
      final IExportRecord aHeaderRecord = aProvider.getHeader ();
      if (aHeaderRecord != null)
        _emitRecord (aWBCH, EExportRecordType.HEADER, aHeaderRecord);

      // Body
      for (final IExportRecord aBodyRecord : IterableIterator.create (aProvider.getBodyRecords ()))
        _emitRecord (aWBCH, EExportRecordType.BODY, aBodyRecord);

      // Footer
      final IExportRecord aFooterRecord = aProvider.getFooter ();
      if (aFooterRecord != null)
        _emitRecord (aWBCH, EExportRecordType.FOOTER, aFooterRecord);

      if (aWBCH.getRowCount () == 0)
        return ESuccess.FAILURE;

      aWBCH.autoSizeAllColumns ();
      return aWBCH.write (aOS);
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }

  @Nonnull
  public EExchangeFileType getFileType ()
  {
    return m_eVersion.equals (EExcelVersion.XLS) ? EExchangeFileType.XLS : EExchangeFileType.XLSX;
  }
}
