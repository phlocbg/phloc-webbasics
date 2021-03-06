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
package com.phloc.webctrls.custom.table;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.string.StringHelper;
import com.phloc.datetime.PDTUtils;
import com.phloc.datetime.format.PDTFromString;
import com.phloc.datetime.format.SerializableDateTimeFormatter;
import com.phloc.datetime.format.SerializableDateTimeFormatter.EFormatStyle;
import com.phloc.html.hc.IHCCell;

/**
 * This comparator is responsible for sorting cells by date and/or time.
 * 
 * @author Philip Helger
 */
public class ComparatorCellDateTime extends AbstractComparator <IHCCell <?>>
{
  private final SerializableDateTimeFormatter m_aFormatter;
  private final String m_sCommonPrefix;
  private final String m_sCommonSuffix;

  public ComparatorCellDateTime (@Nullable final Locale aParseLocale)
  {
    this (aParseLocale, null, null);
  }

  public ComparatorCellDateTime (@Nullable final Locale aParseLocale,
                                 @Nullable final String sCommonPrefix,
                                 @Nullable final String sCommonSuffix)
  {
    this (SerializableDateTimeFormatter.create (EFormatStyle.DEFAULT, EFormatStyle.DEFAULT, aParseLocale),
          sCommonPrefix,
          sCommonSuffix);
  }

  public ComparatorCellDateTime (@Nonnull final SerializableDateTimeFormatter aFormatter)
  {
    this (aFormatter, null, null);
  }

  public ComparatorCellDateTime (@Nonnull final SerializableDateTimeFormatter aFormatter,
                                 @Nullable final String sCommonPrefix,
                                 @Nullable final String sCommonSuffix)
  {
    ValueEnforcer.notNull (aFormatter, "Formatter");
    m_aFormatter = aFormatter;
    m_sCommonPrefix = sCommonPrefix;
    m_sCommonSuffix = sCommonSuffix;
  }

  @OverrideOnDemand
  protected String getCellText (@Nullable final IHCCell <?> aCell)
  {
    if (aCell == null)
      return "";

    String sText = aCell.getPlainText ();

    // strip common prefix and suffix
    if (StringHelper.hasText (m_sCommonPrefix))
      sText = StringHelper.trimStart (sText, m_sCommonPrefix);
    if (StringHelper.hasText (m_sCommonSuffix))
      sText = StringHelper.trimEnd (sText, m_sCommonSuffix);

    return sText;
  }

  @Override
  protected final int mainCompare (final IHCCell <?> aCell1, final IHCCell <?> aCell2)
  {
    final String sText1 = getCellText (aCell1);
    final String sText2 = getCellText (aCell2);
    final DateTime aDT1 = PDTFromString.getDateTimeFromString (sText1, m_aFormatter.getFormatter ());
    final DateTime aDT2 = PDTFromString.getDateTimeFromString (sText2, m_aFormatter.getFormatter ());
    return PDTUtils.nullSafeCompare (aDT1, aDT2);
  }
}
