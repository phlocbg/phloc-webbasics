package com.phloc.webctrls.custom.table;

import java.util.Locale;

import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.compare.AbstractCollationComparator;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.html.AbstractHCCell;

public class ComparatorCellString extends AbstractCollationComparator <AbstractHCCell>
{
  private final String m_sCommonPrefix;
  private final String m_sCommonSuffix;

  public ComparatorCellString (@Nullable final Locale aParseLocale)
  {
    this (aParseLocale, null, null);
  }

  public ComparatorCellString (@Nullable final Locale aParseLocale,
                               @Nullable final String sCommonPrefix,
                               @Nullable final String sCommonSuffix)
  {
    super (aParseLocale);
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
  protected final String asString (@Nullable final AbstractHCCell aCell)
  {
    return getCellText (aCell);
  }
}
