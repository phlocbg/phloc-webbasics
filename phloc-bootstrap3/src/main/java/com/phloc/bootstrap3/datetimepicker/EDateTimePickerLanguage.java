package com.phloc.bootstrap3.datetimepicker;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.locale.LocaleCache;

public enum EDateTimePickerLanguage
{
  AR ("ar", "ar"),
  BG ("bg", "bg"),
  CA ("ca", "ca"),
  CS ("cs", "cs"),
  DA ("da", "da"),
  DE ("de", "de"),
  DE_AT ("de_AT", "de-AT"),
  EL ("el", "el"),
  ES ("es", "es"),
  FI ("fi", "fi"),
  FR ("fr", "fr"),
  HE ("he", "he"),
  HR ("hr", "hr"),
  HU ("hu", "hu"),
  ID ("id", "id"),
  IS ("is", "is"),
  IT ("it", "it"),
  JA ("ja", "ja"),
  KR ("kr", "kr"),
  LT ("lt", "lt"),
  LV ("lv", "lv"),
  MS ("ms", "ms"),
  NB ("nb", "nb"),
  NL ("nl", "nl"),
  NO ("no", "no"),
  PL ("pl", "pl"),
  PT ("pt", "pt"),
  PT_BR ("pt_BR", "pt-BR"),
  RO ("ro", "ro"),
  RS ("rs", "rs"),
  RS_LATIN ("rs_latin", "rs-latin"),
  RU ("ru", "ru"),
  SK ("sk", "sk"),
  SL ("sl", "sl"),
  SV ("sv", "sv"),
  SW ("sw", "sw"),
  TH ("th", "th"),
  TR ("tr", "tr"),
  UA ("ua", "ua"),
  UK ("uk", "uk"),
  ZH_CN ("zh_CN", "zh-CN"),
  ZH_TW ("zh_TW", "zh-TW");

  private final Locale m_aLocale;
  private final String m_sLanguageID;

  private EDateTimePickerLanguage (@Nonnull final String sLocale, @Nonnull @Nonempty final String sLanguageID)
  {
    m_aLocale = LocaleCache.getLocale (sLocale);
    if (m_aLocale == null)
      throw new IllegalStateException ("Failed to resolve '" + sLocale + "'");
    m_sLanguageID = sLanguageID;
  }

  @Nonnull
  public Locale getLocale ()
  {
    return m_aLocale;
  }

  @Nonnull
  @Nonempty
  public String getLanguageID ()
  {
    return m_sLanguageID;
  }

  @Nullable
  public static EDateTimePickerLanguage getFromLocaleOrDefault (@Nullable final Locale aLocale,
                                                                @Nullable final EDateTimePickerLanguage eDefault)
  {
    if (aLocale != null)
    {
      // Check locale with language and country only
      Locale aRealLocale = LocaleCache.getLocale (aLocale.getLanguage (), aLocale.getCountry ());
      for (final EDateTimePickerLanguage eLanguage : values ())
        if (eLanguage.getLocale ().equals (aRealLocale))
          return eLanguage;

      // Check locale with language only
      aRealLocale = LocaleCache.getLocale (aLocale.getLanguage ());
      for (final EDateTimePickerLanguage eLanguage : values ())
        if (eLanguage.getLocale ().equals (aRealLocale))
          return eLanguage;
    }
    return eDefault;
  }
}
