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
package com.phloc.appbasics.exchange.bulkimport;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.poi.ss.usermodel.Cell;

import com.phloc.appbasics.exchange.EExchangeFileType;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.poi.excel.ExcelReadUtils;

/**
 * Abstract base implementation of a bulk import based on MS Excel files.
 * 
 * @author Philip Helger
 */
public abstract class AbstractBulkImportExcel extends AbstractBulkImport
{
  @Translatable
  private static enum EText implements IHasDisplayTextWithArgs
  {
    MSG_ERR_NO_STRING ("Zeile {0}, Spalte {1}: Zeichenkette erwartet", "Row {0}, Column {1}: expected string"),
    MSG_ERR_NO_INT ("Zeile {0}, Spalte {1}: Ganzzahl erwartet", "Row {0}, Column {1}: expected integer"),
    MSG_ERR_NO_DATE ("Zeile {0}, Spalte {1}: Datum erwartet", "Row {0}, Column {1}: expected date"),
    MSG_ERR_NO_BOOLEAN ("Zeile {0}, Spalte {1}: Wahrheitswert erwartet", "Row {0}, Column {1}: expected boolean");

    @Nonnull
    private final ITextProvider m_aTP;

    private EText (@Nonnull final String sDE, @Nonnull final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    @Override
    public String getDisplayTextWithArgs (@Nonnull final Locale aContentLocale, @Nullable final Object... aArgs)
    {
      return DefaultTextResolver.getTextWithArgs (this, m_aTP, aContentLocale, aArgs);
    }
  }

  protected AbstractBulkImportExcel (@Nonnegative final int nHeaderRowsToSkip,
                                     @Nonnull @Nonempty final List <IHasDisplayText> aColumns,
                                     @Nonnull @Nonempty final EExchangeFileType... aFileTypes)
  {
    super (nHeaderRowsToSkip, aColumns, aFileTypes);
  }

  @Nullable
  protected static final String getNormalizedString (@Nullable final String s)
  {
    if (s == null)
      return null;
    final StringBuilder aSB = new StringBuilder (s.length ());
    for (final char c : s.toCharArray ())
      if (Character.getType (c) != Character.CONTROL)
        aSB.append (c);
    return StringHelper.replaceAllRepeatedly (aSB.toString ().trim (), "  ", " ");
  }

  @Nullable
  protected static final String getCellAsString (@Nonnull final Cell aCell)
  {
    final String sValue = ExcelReadUtils.getCellValueString (aCell);
    if (sValue == null)
      return null;

    final StringBuilder aSB = new StringBuilder (sValue.length ());
    for (final char c : sValue.toCharArray ())
      if (Character.getType (c) != Character.CONTROL)
        aSB.append (c);
    return StringHelper.replaceAllRepeatedly (aSB.toString ().trim (), "  ", " ");
  }

  @Nullable
  protected static final Integer getCellAsInteger (@Nonnegative final int nRowIndex,
                                                   @Nonnull final Cell aCell,
                                                   @Nonnull final BulkImportResult aRes,
                                                   @Nonnull final Locale aDisplayLocale)
  {
    final Object aValue = ExcelReadUtils.getCellValueObject (aCell);
    if (aValue == null)
      return null;
    if (aValue instanceof Integer)
      return (Integer) aValue;

    Integer ret = null;
    if (aValue instanceof String)
      ret = StringParser.parseIntObj ((String) aValue);

    if (ret == null)
      aRes.addWarning (EText.MSG_ERR_NO_INT.getDisplayTextWithArgs (aDisplayLocale,
                                                                    Integer.toString (nRowIndex),
                                                                    Integer.toString (aCell.getColumnIndex ())));
    return ret;
  }

  @Nullable
  protected static final Date getCellAsDate (@Nonnegative final int nRowIndex,
                                             @Nonnull final Cell aCell,
                                             @Nonnull final BulkImportResult aRes,
                                             @Nonnull final Locale aDisplayLocale)
  {
    final Date aValue = ExcelReadUtils.getCellValueJavaDate (aCell);
    if (aValue == null && ExcelReadUtils.getCellValueObject (aCell) != null)
    {
      aRes.addWarning (EText.MSG_ERR_NO_DATE.getDisplayTextWithArgs (aDisplayLocale,
                                                                     Integer.toString (nRowIndex),
                                                                     Integer.toString (aCell.getColumnIndex ())));
      return null;
    }
    return aValue;
  }

  @Nonnull
  protected static final ETriState getCellAsBoolean (@Nonnegative final int nRowIndex,
                                                     @Nonnull final Cell aCell,
                                                     @Nonnull final BulkImportResult aRes,
                                                     @Nonnull final Locale aDisplayLocale)
  {
    final Object aValue = ExcelReadUtils.getCellValueObject (aCell);
    if (aValue == null)
      return ETriState.UNDEFINED;
    if (!(aValue instanceof Boolean))
    {
      aRes.addWarning (EText.MSG_ERR_NO_BOOLEAN.getDisplayTextWithArgs (aDisplayLocale,
                                                                        Integer.toString (nRowIndex),
                                                                        Integer.toString (aCell.getColumnIndex ())));
      return ETriState.UNDEFINED;
    }
    return ETriState.valueOf ((Boolean) aValue);
  }
}
