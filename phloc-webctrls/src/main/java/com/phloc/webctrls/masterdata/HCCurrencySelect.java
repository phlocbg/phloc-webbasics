package com.phloc.webctrls.masterdata;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.hc.IHCRequestField;
import com.phloc.masterdata.currency.ECurrency;
import com.phloc.webctrls.custom.HCExtSelect;

public final class HCCurrencySelect extends HCExtSelect
{
  public HCCurrencySelect (@Nonnull final IHCRequestField aRF, @Nonnull final Locale aDisplayLocale)
  {
    this (ContainerHelper.newList (ECurrency.values ()), aRF, aDisplayLocale);
  }

  public HCCurrencySelect (@Nonnull final Iterable <ECurrency> aCurrencies,
                           @Nonnull final IHCRequestField aRF,
                           @Nonnull final Locale aDisplayLocale)
  {
    super (aRF);

    // Add empty element
    addOptionPleaseSelect (aDisplayLocale);

    // For all supported currencies
    for (final ECurrency eCurrency : aCurrencies)
    {
      final String sDisplayText = eCurrency.getCurrencySymbol () +
                                  " (" +
                                  eCurrency.getDisplayText (aDisplayLocale) +
                                  ')';
      addOption (eCurrency.getID (), sDisplayText);
    }
  }
}
