package com.phloc.webctrls.facebook;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.cache.AbstractNotifyingCache;
import com.phloc.commons.cache.ISimpleCache;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;

public final class FacebookLocaleMapping extends GlobalSingleton
{
  private static final Locale FALLBACK = Locale.US;
  private static final String [] FB_LOCALES = { "af_ZA",
                                               "ar_AR",
                                               "az_AZ",
                                               "be_BY",
                                               "bg_BG",
                                               "bn_IN",
                                               "bs_BA",
                                               "ca_ES",
                                               "cs_CZ",
                                               "cy_GB",
                                               "da_DK",
                                               "de_DE",
                                               "el_GR",
                                               "en_GB",
                                               "en_PI",
                                               "en_UD",
                                               "en_US",
                                               "eo_EO",
                                               "es_ES",
                                               "es_LA",
                                               "et_EE",
                                               "eu_ES",
                                               "fa_IR",
                                               "fb_LT",
                                               "fi_FI",
                                               "fo_FO",
                                               "fr_CA",
                                               "fr_FR",
                                               "fy_NL",
                                               "ga_IE",
                                               "gl_ES",
                                               "he_IL",
                                               "hi_IN",
                                               "hr_HR",
                                               "hu_HU",
                                               "hy_AM",
                                               "id_ID",
                                               "is_IS",
                                               "it_IT",
                                               "ja_JP",
                                               "ka_GE",
                                               "km_KH",
                                               "ko_KR",
                                               "ku_TR",
                                               "la_VA",
                                               "lt_LT",
                                               "lv_LV",
                                               "mk_MK",
                                               "ml_IN",
                                               "ms_MY",
                                               "nb_NO",
                                               "ne_NP",
                                               "nl_NL",
                                               "nn_NO",
                                               "pa_IN",
                                               "pl_PL",
                                               "ps_AF",
                                               "pt_BR",
                                               "pt_PT",
                                               "ro_RO",
                                               "ru_RU",
                                               "sk_SK",
                                               "sl_SI",
                                               "sq_AL",
                                               "sr_RS",
                                               "sv_SE",
                                               "sw_KE",
                                               "ta_IN",
                                               "te_IN",
                                               "th_TH",
                                               "tl_PH",
                                               "tr_TR",
                                               "uk_UA",
                                               "vi_VN",
                                               "zh_CN",
                                               "zh_HK",
                                               "zh_TW" };

  private static final Logger s_aLogger = LoggerFactory.getLogger (FacebookLocaleMapping.class);

  private final Set <Locale> m_aFBLocales = new HashSet <Locale> ();

  private final ISimpleCache <Locale, Locale> m_aCache = new AbstractNotifyingCache <Locale, Locale> (FacebookLocaleMapping.class.getName ())
  {
    @Override
    @Nonnull
    protected Locale getValueToCache (@Nullable final Locale aLocale)
    {
      return _getFBCompatibleLocale (aLocale);
    }
  };

  @Nonnull
  private Locale _getFBCompatibleLocale (@Nonnull final Locale aLocale)
  {
    if (m_aFBLocales.contains (aLocale))
      return aLocale;
    for (final Locale aCompatibleLocale : m_aFBLocales)
      if (aCompatibleLocale.getLanguage ().equals (aLocale.getLanguage ()))
      {
        s_aLogger.warn ("Using compatible locale '" +
                        aCompatibleLocale +
                        "' for facebook integration as requested locale '" +
                        aLocale +
                        "' is not supported!");
        return aCompatibleLocale;
      }

    s_aLogger.warn ("Using fallback locale '" +
                    FALLBACK +
                    "' for facebook integration as requested locale '" +
                    aLocale +
                    "' is not supported!");
    return FALLBACK;
  }

  @UsedViaReflection
  @Deprecated
  public FacebookLocaleMapping ()
  {
    for (final String sLocale : FB_LOCALES)
      m_aFBLocales.add (LocaleCache.getLocale (sLocale));
  }

  @Nonnull
  public static FacebookLocaleMapping getInstance ()
  {
    return getGlobalSingleton (FacebookLocaleMapping.class);
  }

  @Override
  protected void onDestroy () throws Exception
  {
    clearCache ();
  }

  @Nullable
  public Locale getFBLocale (@Nonnull final Locale aLocale)
  {
    return m_aCache.getFromCache (aLocale);
  }

  public void clearCache ()
  {
    if (m_aCache.clearCache ().isChanged ())
      s_aLogger.info ("Cache was cleared: " + getClass ().getName ());
  }
}
