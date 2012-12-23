package com.phloc.webctrls.custom.table;

import java.util.Locale;

import javax.annotation.Nullable;

import com.phloc.datetime.format.SerializableDateTimeFormatter;
import com.phloc.datetime.format.SerializableDateTimeFormatter.EFormatStyle;

public class ComparatorCellTime extends ComparatorCellDateTime
{
  public ComparatorCellTime (@Nullable final Locale aParseLocale)
  {
    this (aParseLocale, null, null);
  }

  public ComparatorCellTime (@Nullable final Locale aParseLocale,
                             @Nullable final String sCommonPrefix,
                             @Nullable final String sCommonSuffix)
  {
    super (SerializableDateTimeFormatter.createForTime (EFormatStyle.DEFAULT, aParseLocale),
           sCommonPrefix,
           sCommonSuffix);
  }
}
