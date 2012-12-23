package com.phloc.webctrls.custom.table;

import java.math.BigDecimal;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.masterdata.currency.ECurrency;

/**
 * A cell comparator that handles currency formatted cells that all use the same
 * currency.
 * 
 * @author philip
 */
public final class ComparatorCellFixedCurrencyFormat extends ComparatorCellDouble
{
  private final ECurrency m_eCurrency;

  public ComparatorCellFixedCurrencyFormat (@Nonnull final Locale aLocale, @Nonnull final ECurrency eCurrency)
  {
    super (aLocale);
    if (eCurrency == null)
      throw new NullPointerException ("currency");
    m_eCurrency = eCurrency;
  }

  @Override
  protected double asDouble (final String sCellText)
  {
    return m_eCurrency.parseCurrencyFormat (sCellText, BigDecimal.ZERO).doubleValue ();
  }
}
