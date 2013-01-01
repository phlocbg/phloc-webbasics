/**
 * Copyright (C) 2006-2013 phloc systems
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
