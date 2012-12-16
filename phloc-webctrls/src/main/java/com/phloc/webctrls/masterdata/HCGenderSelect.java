package com.phloc.webctrls.masterdata;

import java.util.Locale;

import com.phloc.html.hc.IHCRequestField;
import com.phloc.masterdata.person.EGender;
import com.phloc.webctrls.custom.HCExtSelect;

public final class HCGenderSelect extends HCExtSelect
{
  public HCGenderSelect (final IHCRequestField aRF, final Locale aDisplayLocale)
  {
    super (aRF);

    // add empty item
    addOptionPleaseSelect (aDisplayLocale);

    // for all genders
    for (final EGender eGender : EGender.values ())
    {
      final String sDisplayText = eGender.getDisplayText (aDisplayLocale);
      addOption (eGender.getID (), sDisplayText);
    }
  }
}
