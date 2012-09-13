/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webctrls.country;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.AbstractCollationComparator;
import com.phloc.commons.locale.country.ComparatorLocaleDisplayCountryInLocale;
import com.phloc.commons.locale.country.CountryCache;
import com.phloc.commons.name.IDisplayTextProvider;
import com.phloc.html.hc.IHCRequestField;
import com.phloc.html.hc.html.HCOption;
import com.phloc.webctrls.custom.ExtHCSelect;
import com.phloc.webctrls.famfam.EFamFamFlagIcon;
import com.phloc.webctrls.famfam.FamFamFlags;

public class CountrySelect extends ExtHCSelect
{
  @Nonnull
  private static List <Locale> _getAllCountries ()
  {
    final List <Locale> aLocales = new ArrayList <Locale> ();
    for (final String sCountry : CountryCache.getAllCountries ())
      aLocales.add (CountryCache.getCountry (sCountry));
    return aLocales;
  }

  public CountrySelect (@Nonnull final IHCRequestField aRF, @Nonnull final Locale aDisplayLocale)
  {
    this (aRF, aDisplayLocale, _getAllCountries ());
  }

  public CountrySelect (@Nonnull final IHCRequestField aRF,
                        @Nonnull final Locale aDisplayLocale,
                        @Nonnull final Collection <Locale> aLocales)
  {
    this (aRF, aDisplayLocale, aLocales, null);
  }

  public CountrySelect (@Nonnull final IHCRequestField aRF,
                        @Nonnull final Locale aDisplayLocale,
                        @Nonnull final Collection <Locale> aLocales,
                        @Nullable final IDisplayTextProvider <Locale> aDisplayTextProvider)
  {
    super (aRF);

    if (aLocales.size () > 1)
      addOptionPleaseSelect (aDisplayLocale);

    Comparator <Locale> aComp;
    if (aDisplayTextProvider == null)
      aComp = new ComparatorLocaleDisplayCountryInLocale (aDisplayLocale, aDisplayLocale);
    else
      aComp = new AbstractCollationComparator <Locale> (aDisplayLocale)
      {
        @Override
        protected String asString (final Locale aObject)
        {
          return aDisplayTextProvider.getDisplayText (aObject, aDisplayLocale);
        }
      };
    for (final Locale aCountry : ContainerHelper.getSorted (aLocales, aComp))

    {
      final String sDisplayCountry = aDisplayTextProvider != null
                                                                 ? aDisplayTextProvider.getDisplayText (aCountry,
                                                                                                        aDisplayLocale)
                                                                 : aCountry.getDisplayCountry (aDisplayLocale);
      final HCOption aOption = addOption (aCountry.getCountry (), sDisplayCountry);
      final EFamFamFlagIcon eIcon = FamFamFlags.getFlagFromLocale (aCountry);
      if (eIcon != null)
        eIcon.applyToNode (aOption);
    }
  }
}
