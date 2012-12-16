package com.phloc.webctrls.masterdata;

import java.util.Locale;

import com.phloc.html.hc.IHCRequestField;
import com.phloc.masterdata.trade.EIncoterm;
import com.phloc.webctrls.custom.HCExtSelect;

public final class HCIncotermSelect extends HCExtSelect
{
  public HCIncotermSelect (final IHCRequestField aRF, final Locale aDisplayLocale)
  {
    super (aRF);

    // add empty item
    addOptionPleaseSelect (aDisplayLocale);

    // for all delivery terms
    for (final EIncoterm eDeliveryTerm : EIncoterm.values ())
    {
      final String sDisplayText = eDeliveryTerm.getDisplayText (aDisplayLocale);
      addOption (eDeliveryTerm.getID (), sDisplayText);
    }
  }
}
