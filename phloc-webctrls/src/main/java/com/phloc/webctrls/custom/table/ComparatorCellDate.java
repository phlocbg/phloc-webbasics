package com.phloc.webctrls.custom.table;

import java.util.Locale;

import javax.annotation.Nullable;

import com.phloc.datetime.format.SerializableDateTimeFormatter;
import com.phloc.datetime.format.SerializableDateTimeFormatter.EFormatStyle;

public class ComparatorCellDate extends ComparatorCellDateTime
{
  public ComparatorCellDate (@Nullable final Locale aParseLocale)
  {
    this (aParseLocale, null, null);
  }

  public ComparatorCellDate (@Nullable final Locale aParseLocale,
                             @Nullable final String sCommonPrefix,
                             @Nullable final String sCommonSuffix)
  {
    super (SerializableDateTimeFormatter.createForDate (EFormatStyle.DEFAULT, aParseLocale),
           sCommonPrefix,
           sCommonSuffix);
  }
}
