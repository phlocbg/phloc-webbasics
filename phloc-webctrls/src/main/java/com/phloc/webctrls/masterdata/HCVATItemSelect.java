/**
 * Copyright (C) 2006-2014 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.webctrls.masterdata;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.request.IHCRequestField;
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
    {
      final IVATItem aVATItem = aEntry.getValue ();
      if (!aVATItem.isDeprecated ())
      {
        final String sDisplayText = aVATItem.getDisplayText (aDisplayLocale);
        addOption (aEntry.getKey (), sDisplayText);
      }
    }
  }
}
