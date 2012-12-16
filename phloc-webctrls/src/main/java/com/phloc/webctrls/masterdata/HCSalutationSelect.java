package com.phloc.webctrls.masterdata;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.hc.IHCRequestField;
import com.phloc.masterdata.person.ESalutation;
import com.phloc.webctrls.custom.HCExtSelect;

public final class HCSalutationSelect extends HCExtSelect
{
  public HCSalutationSelect (final IHCRequestField aRF, final Locale aDisplayLocale)
  {
    this (aRF, aDisplayLocale, true);
  }

  public HCSalutationSelect (final IHCRequestField aRF,
                             final Locale aDisplayLocale,
                             final boolean bAddOptionPleaseSelect)
  {
    this (aRF, aDisplayLocale, bAddOptionPleaseSelect, ESalutation.values ());
  }

  public HCSalutationSelect (final IHCRequestField aRF,
                             final Locale aDisplayLocale,
                             final boolean bAddOptionPleaseSelect,
                             @Nonnull @Nonempty final ESalutation [] aSalutations)
  {
    super (aRF);

    // add empty item
    if (bAddOptionPleaseSelect)
      addOptionPleaseSelect (aDisplayLocale);

    // for all salutations
    for (final ESalutation eSalutation : aSalutations)
    {
      final String sDisplayText = eSalutation.getDisplayText (aDisplayLocale);
      addOption (eSalutation.getID (), sDisplayText);
    }
  }
}
