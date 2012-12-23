package com.phloc.webctrls.custom.table;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.locale.LocaleFormatter;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.html.AbstractHCCell;

/**
 * This comparator is responsible for sorting cells by BigInteger
 * 
 * @author philip
 */
public class ComparatorCellBigInteger extends AbstractComparator <AbstractHCCell>
{
  private static final BigDecimal DEFAULT_VALUE = BigDecimal.ZERO;
  private final Locale m_aLocale;
  private final String m_sCommonPrefix;
  private final String m_sCommonSuffix;

  public ComparatorCellBigInteger (@Nonnull final Locale aParseLocale)
  {
    this (aParseLocale, null, null);
  }

  public ComparatorCellBigInteger (@Nonnull final Locale aParseLocale,
                                   @Nullable final String sCommonPrefix,
                                   @Nullable final String sCommonSuffix)
  {
    if (aParseLocale == null)
      throw new NullPointerException ("parseLocale");
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
  protected final int mainCompare (final AbstractHCCell aCell1, final AbstractHCCell aCell2)
  {
    final String sText1 = getCellText (aCell1);
    final String sText2 = getCellText (aCell2);

    final BigInteger aBD1 = LocaleFormatter.parseBigDecimal (sText1, m_aLocale, DEFAULT_VALUE).toBigIntegerExact ();
    final BigInteger aBD2 = LocaleFormatter.parseBigDecimal (sText2, m_aLocale, DEFAULT_VALUE).toBigIntegerExact ();
    return aBD1.compareTo (aBD2);
  }
}
