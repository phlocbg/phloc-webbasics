package com.phloc.webctrls.masterdata;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.hc.IHCRequestField;
import com.phloc.masterdata.vat.ComparatorVATItemPercentage;
import com.phloc.masterdata.vat.IVATItem;
import com.phloc.masterdata.vat.VATManager;
import com.phloc.webctrls.custom.HCExtSelect;

public class HCVATItemSelect extends HCExtSelect
{
  public HCVATItemSelect (@Nonnull final IHCRequestField aRF,
                          @Nonnull final Locale aCountry,
                          @Nonnull final Locale aDisplayLocale,
                          final boolean bAddEmptyOption)
  {
    this (aRF, VATManager.getDefaultInstance (), aCountry, aDisplayLocale, bAddEmptyOption);
  }

  public HCVATItemSelect (@Nonnull final IHCRequestField aRF,
                          @Nonnull final VATManager aVATManager,
                          @Nonnull final Locale aCountry,
                          @Nonnull final Locale aDisplayLocale,
                          final boolean bAddEmptyOption)
  {
    super (aRF);

    // add empty item
    if (bAddEmptyOption)
      addOptionPleaseSelect (aDisplayLocale);

    // for all VAT items of the given country
    final Map <String, IVATItem> aVATItems = aVATManager.getAllVATItemsForCountry (aCountry);

    // 0% should always be present
    if (!aVATItems.containsKey (VATManager.VATTYPE_NONE.getID ()))
      aVATItems.put (VATManager.VATTYPE_NONE.getID (), VATManager.VATTYPE_NONE);

    for (final Map.Entry <String, IVATItem> aEntry : ContainerHelper.getSortedByValue (aVATItems,
                                                                                       new ComparatorVATItemPercentage ())
                                                                    .entrySet ())
      if (!aEntry.getValue ().isDeprecated ())
      {
        final String sDisplayText = aEntry.getValue ().getDisplayText (aDisplayLocale);
        addOption (aEntry.getKey (), sDisplayText);
      }
  }
}
