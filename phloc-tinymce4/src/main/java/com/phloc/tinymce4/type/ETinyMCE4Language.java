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
package com.phloc.tinymce4.type;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.string.StringHelper;

/**
 * All TinyMCE4 supported languages. Note: not all locales here are valid Java
 * locales!<br>
 * Partly generated by MainCreateTinyMCELanguageEnum
 * 
 * @author Philip Helger
 */
public enum ETinyMCE4Language
{
  AR ("ar"),
  AR_SA ("ar_SA"),
  AZ ("az"),
  BE ("be"),
  BG_BG ("bg_BG"),
  /* Note: this is not a valid Java locale! */
  BN_BD ("bn_BD"),
  BS ("bs"),
  CA ("ca"),
  CS ("cs"),
  CY ("cy"),
  DA ("da"),
  DE ("de"),
  DE_AT ("de_AT"),
  DV ("dv"),
  EL ("el"),
  EN_CA ("en_CA"),
  EN_GB ("en_GB"),
  ES ("es"),
  ET ("et"),
  EU ("eu"),
  FA ("fa"),
  FI ("fi"),
  FO ("fo"),
  FR_FR ("fr_FR"),
  GD ("gd"),
  GL ("gl"),
  /* Note: this is not a valid Java locale! */
  HE_IL ("he_IL"),
  HR ("hr"),
  HU_HU ("hu_HU"),
  HY ("hy"),
  /* Note: this is not a valid Java locale! */
  ID ("id"),
  IS_IS ("is_IS"),
  IT ("it"),
  JA ("ja"),
  /* Note: this is not a valid Java locale! */
  KA_GE ("ka_GE"),
  KK ("kk"),
  /* Note: this is not a valid Java locale! */
  KM_KH ("km_KH"),
  KO_KR ("ko_KR"),
  LB ("lb"),
  LT ("lt"),
  LV ("lv"),
  ML ("ml"),
  /* Note: this is not a valid Java locale! */
  ML_IN ("ml_IN"),
  /* Note: this is not a valid Java locale! */
  MN_MN ("mn_MN"),
  /* Note: this is not a valid Java locale! */
  NB_NO ("nb_NO"),
  NL ("nl"),
  PL ("pl"),
  PT_BR ("pt_BR"),
  PT_PT ("pt_PT"),
  RO ("ro"),
  RU ("ru"),
  /* Note: this is not a valid Java locale! */
  SI_LK ("si_LK"),
  SK ("sk"),
  SL_SI ("sl_SI"),
  SR ("sr"),
  SV_SE ("sv_SE"),
  TA ("ta"),
  /* Note: this is not a valid Java locale! */
  TA_IN ("ta_IN"),
  TG ("tg"),
  TH_TH ("th_TH"),
  TR_TR ("tr_TR"),
  TT ("tt"),
  UG ("ug"),
  UK ("uk"),
  UK_UA ("uk_UA"),
  VI ("vi"),
  VI_VN ("vi_VN"),
  ZH_CN ("zh_CN"),
  ZH_TW ("zh_TW");

  private final String m_sValue;

  private ETinyMCE4Language (@Nonnull @Nonempty final String sValue)
  {
    m_sValue = sValue;
  }

  @Nonnull
  @Nonempty
  public String getValue ()
  {
    return m_sValue;
  }

  @Nullable
  public static ETinyMCE4Language getFromValueOrNull (@Nullable final String sValue)
  {
    return getFromValueOrDefault (sValue, null);
  }

  @Nullable
  public static ETinyMCE4Language getFromValueOrDefault (@Nullable final String sValue,
                                                         @Nullable final ETinyMCE4Language eDefault)
  {
    if (StringHelper.hasText (sValue))
      for (final ETinyMCE4Language e : values ())
        if (sValue.equals (e.m_sValue))
          return e;
    return eDefault;
  }

  /**
   * Get the language from the passed locale, considering only available
   * translations. If the passed locale has language 'English' no translation is
   * required!
   * 
   * @param aLocale
   *        The locale to check. May be <code>null</code>.
   * @return <code>null</code> if no special translation locale was found.
   */
  @Nullable
  public static ETinyMCE4Language getFromLocaleOrNull (@Nullable final Locale aLocale)
  {
    return getFromLocaleOrDefault (aLocale, null);
  }

  /**
   * Get the language from the passed locale, considering only available
   * translations. If the passed locale has language 'English' no translation is
   * required!
   * 
   * @param aLocale
   *        The locale to check. May be <code>null</code>.
   * @param eDefault
   *        The default language to be returned if no match was found. May be
   *        <code>null</code>.
   * @return <code>eDefault</code> if no special translation locale was found.
   */
  @Nullable
  public static ETinyMCE4Language getFromLocaleOrDefault (@Nullable final Locale aLocale,
                                                          @Nullable final ETinyMCE4Language eDefault)
  {
    // Check for special case English - no translation required in this case
    if (aLocale != null && !aLocale.getLanguage ().equals ("en"))
    {
      // Check for direct match
      String sLocaleStr = aLocale.toString ();
      for (final ETinyMCE4Language e : values ())
        if (sLocaleStr.equals (e.m_sValue))
          return e;

      if (StringHelper.hasText (aLocale.getCountry ()))
      {
        // Not found so far - extract language and country
        final Locale aLocale2 = LocaleCache.getLocale (aLocale.getLanguage (), aLocale.getCountry ());
        if (aLocale2 != null)
        {
          sLocaleStr = aLocale2.toString ();
          for (final ETinyMCE4Language e : values ())
            if (sLocaleStr.equals (e.m_sValue))
              return e;
        }
      }

      if (StringHelper.hasText (aLocale.getLanguage ()))
      {
        // Not found so far - extract language only
        sLocaleStr = aLocale.getLanguage ();
        for (final ETinyMCE4Language e : values ())
          if (sLocaleStr.equals (e.m_sValue))
            return e;
      }
    }
    return eDefault;
  }
}
