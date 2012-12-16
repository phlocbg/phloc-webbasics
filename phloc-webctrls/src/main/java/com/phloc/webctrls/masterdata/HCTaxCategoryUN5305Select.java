package com.phloc.webctrls.masterdata;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.html.hc.IHCRequestField;
import com.phloc.masterdata.tax.ETaxCategoryUN5305;
import com.phloc.webctrls.custom.HCExtSelect;

public final class HCTaxCategoryUN5305Select extends HCExtSelect
{
  public HCTaxCategoryUN5305Select (@Nonnull final IHCRequestField aRF, @Nonnull final Locale aDisplayLocale)
  {
    super (aRF);

    // add empty item
    addOptionPleaseSelect (aDisplayLocale);

    // for all values
    for (final ETaxCategoryUN5305 eTaxCategory : ETaxCategoryUN5305.values ())
    {
      final String sDisplayText = eTaxCategory.getDisplayText (aDisplayLocale);
      addOption (eTaxCategory.getID (), sDisplayText);
    }
  }
}
