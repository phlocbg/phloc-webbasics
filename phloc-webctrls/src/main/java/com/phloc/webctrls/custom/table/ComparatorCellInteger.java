package com.phloc.webctrls.custom.table;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.compare.AbstractIntegerComparator;
import com.phloc.commons.locale.LocaleFormatter;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.html.AbstractHCCell;

public class ComparatorCellInteger extends AbstractIntegerComparator <AbstractHCCell>
{
  private final Locale m_aLocale;
  private final String m_sCommonPrefix;
  private final String m_sCommonSuffix;

  public ComparatorCellInteger (@Nonnull final Locale aParseLocale)
  {
    this (aParseLocale, null, null);
  }

  public ComparatorCellInteger (@Nonnull final Locale aParseLocale,
                                @Nullable final String sCommonPrefix,
                                @Nullable final String sCommonSuffix)
  {
    if (aParseLocale == null)
      throw new NullPointerException ("locale");
    m_aLocale = aParseLocale;
    m_sCommonPrefix = sCommonPrefix;
    m_sCommonSuffix = sCommonSuffix;
  }

  @OverrideOnDemand
  protected String getCellText (@Nullable final AbstractHCCell aCell)
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
  protected final long asLong (@Nullable final AbstractHCCell aCell)
  {
    final String sText = getCellText (aCell);

    // Ensure that columns without text are sorted consistently compared to the
    // ones with non-numeric content
    if (StringHelper.hasNoText (sText))
      return -1;
    return LocaleFormatter.parseInt (sText, m_aLocale, 0);
  }
}
