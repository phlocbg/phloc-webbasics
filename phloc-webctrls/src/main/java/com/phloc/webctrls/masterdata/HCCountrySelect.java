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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasBooleanRepresentation;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.AbstractCollationComparator;
import com.phloc.commons.locale.country.ComparatorLocaleDisplayCountryInLocale;
import com.phloc.commons.locale.country.CountryCache;
import com.phloc.commons.name.IDisplayTextProvider;
import com.phloc.html.hc.IHCRequestField;
import com.phloc.html.hc.html.HCOption;
import com.phloc.masterdata.locale.DeprecatedLocaleHandler;
import com.phloc.webctrls.custom.HCExtSelect;
import com.phloc.webctrls.famfam.EFamFamFlagIcon;
import com.phloc.webctrls.famfam.FamFamFlags;

public class HCCountrySelect extends HCExtSelect
{
  public static enum EWithDeprecated implements IHasBooleanRepresentation
  {
    TRUE,
    FALSE;

    public static final EWithDeprecated DEFAULT = FALSE;

    public boolean getAsBoolean ()
    {
      return this == TRUE;
    }
  }

  @Nonnull
  private static List <Locale> _getAllCountries (@Nonnull final EWithDeprecated eWithDeprecated)
  {
    final boolean bWithDeprecated = eWithDeprecated.getAsBoolean ();
    final List <Locale> aLocales = new ArrayList <Locale> ();
    for (final String sCountry : CountryCache.getAllCountries ())
    {
      final Locale aCountry = CountryCache.getCountry (sCountry);
      if (bWithDeprecated || !DeprecatedLocaleHandler.getDefaultInstance ().isDeprecatedLocaleWithFallback (aCountry))
        aLocales.add (aCountry);
    }
    return aLocales;
  }

  public HCCountrySelect (@Nonnull final IHCRequestField aRF, @Nonnull final Locale aDisplayLocale)
  {
    this (aRF, aDisplayLocale, EWithDeprecated.DEFAULT);
  }

  public HCCountrySelect (@Nonnull final IHCRequestField aRF,
                          @Nonnull final Locale aDisplayLocale,
                          @Nonnull final EWithDeprecated eWithDeprecated)
  {
    this (aRF, aDisplayLocale, _getAllCountries (eWithDeprecated));
  }

  public HCCountrySelect (@Nonnull final IHCRequestField aRF,
                          @Nonnull final Locale aDisplayLocale,
                          final boolean bAlwaysShowPleaseSelect)
  {
    this (aRF, aDisplayLocale, EWithDeprecated.DEFAULT, bAlwaysShowPleaseSelect);
  }

  public HCCountrySelect (@Nonnull final IHCRequestField aRF,
                          @Nonnull final Locale aDisplayLocale,
                          @Nonnull final EWithDeprecated eWithDeprecated,
                          final boolean bAlwaysShowPleaseSelect)
  {
    this (aRF, aDisplayLocale, _getAllCountries (eWithDeprecated), null, bAlwaysShowPleaseSelect);
  }

  public HCCountrySelect (@Nonnull final IHCRequestField aRF,
                          @Nonnull final Locale aDisplayLocale,
                          @Nonnull final Collection <Locale> aLocales)
  {
    this (aRF, aDisplayLocale, aLocales, null);
  }

  public HCCountrySelect (@Nonnull final IHCRequestField aRF,
                          @Nonnull final Locale aDisplayLocale,
                          @Nonnull final Collection <Locale> aLocales,
                          @Nullable final IDisplayTextProvider <Locale> aDisplayTextProvider)
  {
    // Backwards compatibility
    this (aRF, aDisplayLocale, aLocales, aDisplayTextProvider, true);
  }

  public HCCountrySelect (@Nonnull final IHCRequestField aRF,
                          @Nonnull final Locale aDisplayLocale,
                          @Nonnull final Collection <Locale> aLocales,
                          @Nullable final IDisplayTextProvider <Locale> aDisplayTextProvider,
                          final boolean bAlwaysShowPleaseSelect)
  {
    super (aRF);

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

    if (!hasSelectedOption () || bAlwaysShowPleaseSelect)
      addOptionPleaseSelect (aDisplayLocale);
  }
}
