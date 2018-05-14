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
package com.phloc.webctrls.google;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.ISimpleTextProvider;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSExprDirect;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSPackage;

/**
 * Control for emitting Google Analytics code.
 * 
 * @author Boris Gregorcic
 */
public class HCGoogleAnalyticsUniversal extends HCScript
{
  private static final long serialVersionUID = 2876227304162232905L;

  @Translatable
  private static enum EText implements ISimpleTextProvider
  {
   MSG_TRACKING_OPT_OUT ("Google Analytics Tracking ist jetzt deaktiviert!", "Google Analytics tracking is now deactivated!"); //$NON-NLS-1$ //$NON-NLS-2$

    private final ITextProvider m_aTP;

    private EText (@Nonnull final String sDE, @Nonnull final String sEN)
    {
      this.m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    @Override
    public String getText (Locale aContentLocale)
    {
      return this.m_aTP.getText (aContentLocale);
    }

    @Override
    public String getTextWithLocaleFallback (Locale aContentLocale)
    {
      return this.m_aTP.getTextWithLocaleFallback (aContentLocale);
    }
  }

  private final String m_sAccount;

  private static final String ACCOUNT_PLACEHOLDER = "%ACCOUNT%";
  private static final String OPTIONS_PLACEHOLDER = "%OPTIONS%";
  private static final String MSG_OPT_OUT_PLACEHOLDER = "%MSG_OPT_OUT%";

  // https://www.datenschutzbeauftragter-info.de/fachbeitraege/google-analytics-datenschutzkonform-einsetzen/
  private static final String OPTOUT_CODE_TEMPLATE = " var gaProperty = '" +
                                                     ACCOUNT_PLACEHOLDER +
                                                     "'; \r\n" +
                                                     "    var disableStr = 'ga-disable-' + gaProperty; \r\n" +
                                                     "    if (document.cookie.indexOf(disableStr + '=true') > -1) { \r\n" +
                                                     "        window[disableStr] = true;\r\n" +
                                                     "    } \r\n" +
                                                     "    function gaOptout() { \r\n" +
                                                     "        document.cookie = disableStr + '=true; expires=Thu, 31 Dec 2099 23:59:59 UTC; path=/'; \r\n" +
                                                     "        window[disableStr] = true; \r\n" +
                                                     "        alert('" +
                                                     MSG_OPT_OUT_PLACEHOLDER +
                                                     "'); \r\n" +
                                                     "    }\r\n";

  private static final String ORIG_ANALYTICS_CODE_TEMPLATE = "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\r\n" +
                                                             "(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\r\n" +
                                                             "m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\r\n" +
                                                             "})(window,document,'script','//www.google-analytics.com/analytics.js','ga');\r\n" +
                                                             "\r\n" +
                                                             "ga('create', '" +
                                                             ACCOUNT_PLACEHOLDER +
                                                             "', 'auto');\r\n" +
                                                             OPTIONS_PLACEHOLDER +
                                                             "\r\n" +
                                                             "ga('send', 'pageview');";

  private static String getTrackingOptOutMessage (@Nullable final Locale aDisplayLocale,
                                                  @Nullable final String sTrackingOptOutMessage)
  {
    if (StringHelper.hasText (sTrackingOptOutMessage))
    {
      return sTrackingOptOutMessage;
    }
    String sMsg = EText.MSG_TRACKING_OPT_OUT.getTextWithLocaleFallback (aDisplayLocale == null ? TextProvider.EN
                                                                                               : aDisplayLocale);
    return StringHelper.getNotNull (sMsg, EText.MSG_TRACKING_OPT_OUT.getTextWithLocaleFallback (TextProvider.EN));
  }

  @Nonnull
  private static JSPackage _createJSCode (final String sAccount,
                                          final boolean bAnonymizeIP,
                                          final boolean bEnhancedLinkAttribution,
                                          @Nullable final Locale aDisplayLocale,
                                          @Nullable final String sTrackingOptOutMessage)
  {
    if (StringHelper.hasNoText (sAccount))
    {
      throw new IllegalArgumentException ("account is empty");
    }
    String sCode = OPTOUT_CODE_TEMPLATE + ORIG_ANALYTICS_CODE_TEMPLATE;
    sCode = StringHelper.replaceAll (sCode, ACCOUNT_PLACEHOLDER, sAccount);
    sCode = StringHelper.replaceAll (sCode,
                                     MSG_OPT_OUT_PLACEHOLDER,
                                     getTrackingOptOutMessage (aDisplayLocale, sTrackingOptOutMessage));
    final JSPackage aPkg = new JSPackage ();
    StringBuilder aOptions = new StringBuilder ();
    if (bAnonymizeIP)
    {
      aOptions.append ("ga('set', 'anonymizeIp', true);");
    }
    sCode = StringHelper.replaceAll (sCode, OPTIONS_PLACEHOLDER, aOptions.toString ());
    aPkg.add (new JSExprDirect (sCode));
    return aPkg;
  }

  public HCGoogleAnalyticsUniversal (@Nonnull @Nonempty final String sAccount,
                                     final boolean bAnonymizeIP,
                                     @Nullable final Locale aDisplayLocale,
                                     @Nullable final String sTrackingOptOutMessage)
  {
    this (sAccount, bAnonymizeIP, false, aDisplayLocale, sTrackingOptOutMessage);
  }

  public HCGoogleAnalyticsUniversal (@Nonnull @Nonempty final String sAccount,
                                     final boolean bAnonymizeIP,
                                     final boolean bEnhancedLinkAttribution,
                                     @Nullable final Locale aDisplayLocale,
                                     @Nullable final String sTrackingOptOutMessage)
  {
    super (_createJSCode (sAccount, bAnonymizeIP, bEnhancedLinkAttribution, aDisplayLocale, sTrackingOptOutMessage));
    this.m_sAccount = sAccount;
  }

  @Nonnull
  @Nonempty
  public String getAccount ()
  {
    return this.m_sAccount;
  }

  /**
   * Set this in the "onclick" events of links to track them
   * 
   * @param sCategory
   *        The category. May not be <code>null</code>.
   * @param sAction
   *        The action. May not be <code>null</code>.
   * @param sLabel
   *        The label. May be <code>null</code>.
   * @param aValue
   *        Optional numeric value. May be <code>null</code>.
   * @return The JS code to be set to "onclick" event
   */
  @Nonnull
  public static JSInvocation createEventTrackingCode (@Nonnull final String sCategory,
                                                      @Nonnull final String sAction,
                                                      @Nullable final String sLabel,
                                                      @Nullable final Integer aValue)
  {
    JSInvocation aJS = JSExpr.invoke ("ga");
    aJS.arg ("send");
    aJS.arg ("event");
    aJS.arg (sCategory);
    aJS.arg (sAction);
    if (StringHelper.hasText (sLabel))
    {
      aJS.arg (sLabel);
    }
    if (aValue != null)
    {
      aJS.arg (aValue);
    }
    return aJS;
  }
}
