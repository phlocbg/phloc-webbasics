package com.phloc.webctrls.masterdata;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.html.hc.IHCRequestField;
import com.phloc.masterdata.tax.ETaxTypeUN5153;
import com.phloc.webctrls.custom.HCExtSelect;

public final class HCTaxTypeUN5153Select extends HCExtSelect
{
  public HCTaxTypeUN5153Select (@Nonnull final IHCRequestField aRF, @Nonnull final Locale aDisplayLocale)
  {
    super (aRF);

    // add empty item
    addOptionPleaseSelect (aDisplayLocale);

    // for all values
    for (final ETaxTypeUN5153 eTaxType : ETaxTypeUN5153.values ())
    {
      final String sDisplayText = eTaxType.getDisplayText (aDisplayLocale);
      addOption (eTaxType.getID (), sDisplayText);
    }
  }
}
