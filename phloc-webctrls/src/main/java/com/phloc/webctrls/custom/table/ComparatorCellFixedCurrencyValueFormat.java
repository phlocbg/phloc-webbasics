package com.phloc.webctrls.custom.table;

import java.math.BigDecimal;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.masterdata.currency.ECurrency;

/**
 * A cell comparator that handles value formatted currency cells that all use
 * the same currency.
 * 
 * @author philip
 */
public final class ComparatorCellFixedCurrencyValueFormat extends ComparatorCellDouble
{
  private final ECurrency m_eCurrency;

  public ComparatorCellFixedCurrencyValueFormat (@Nonnull final Locale aLocale, @Nonnull final ECurrency eCurrency)
  {
    super (aLocale);
    if (eCurrency == null)
      throw new NullPointerException ("currency");
    m_eCurrency = eCurrency;
  }

  @Override
  protected double asDouble (final String sCellText)
  {
    return m_eCurrency.parseValueFormat (sCellText, BigDecimal.ZERO).doubleValue ();
  }
}
