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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.EHTMLElement;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSOp;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.JSVar;
import com.phloc.html.js.builder.html.JSHtml;

/**
 * Control for emitting Google Analytics code.
 * 
 * @author Philip Helger
 */
public class HCGoogleAnalytics extends HCScript
{
  private static final long serialVersionUID = -7348359349778311222L;
  private final String m_sAccount;

  @Nonnull
  private static JSPackage _createJSCode (final String sAccount,
                                          final boolean bAnonymizeIP,
                                          final boolean bEnhancedLinkAttribution)
  {
    if (StringHelper.hasNoText (sAccount))
      throw new IllegalArgumentException ("account is empty");

    final JSPackage aPkg = new JSPackage ();
    final JSVar gaq = aPkg.var ("_gaq", JSExpr.ref ("_gaq").cor (new JSArray ()));
    aPkg.add (gaq.invoke ("push").arg (new JSArray ().add ("_setAccount").add (sAccount)));
    if (bEnhancedLinkAttribution)
    {
      // Source:
      // http://support.google.com/analytics/bin/answer.py?hl=en&answer=2558867
      // Must be before the _trackPageview!
      aPkg.add (gaq.invoke ("push")
                   .arg (new JSArray ().add ("_require")
                                       .add ("inpage_linkid")
                                       .add ("//www.google-analytics.com/plugins/ga/inpage_linkid.js")));
    }
    if (bAnonymizeIP)
    {
      // Anonymize IP; see
      // http://code.google.com/intl/de-DE/apis/analytics/docs/gaJS/gaJSApi_gat.html
      aPkg.add (gaq.invoke ("push").arg (new JSArray ().add ("_gat._anonymizeIp")));
    }
    aPkg.add (gaq.invoke ("push").arg (new JSArray ().add ("_trackPageview")));
    aPkg.add (gaq.invoke ("push").arg (new JSArray ().add ("_trackPageLoadTime")));

    final JSAnonymousFunction aAnonFunction = new JSAnonymousFunction ();
    final JSVar ga = aAnonFunction.body ().var ("ga", JSHtml.documentCreateElement (EHTMLElement.SCRIPT));
    aAnonFunction.body ().add (ga.ref ("type").assign (CMimeType.TEXT_JAVASCRIPT.getAsString ()));
    aAnonFunction.body ().add (ga.ref ("async").assign (true));
    aAnonFunction.body ()
                 .add (ga.ref ("src")
                         .assign (JSOp.cond (JSExpr.lit ("https:").eq (JSHtml.windowLocationProtocol ()),
                                             JSExpr.lit ("https://ssl"),
                                             JSExpr.lit ("http://www"))
                                      .plus (".google-analytics.com/ga.js")));
    final JSVar s = aAnonFunction.body ().var ("s",
                                               JSHtml.documentGetElementsByTagName (EHTMLElement.SCRIPT).component0 ());
    aAnonFunction.body ().add (s.ref ("parentNode").invoke ("insertBefore").arg (ga).arg (s));
    aPkg.invoke (aAnonFunction);
    return aPkg;
  }

  public HCGoogleAnalytics (@Nonnull @Nonempty final String sAccount, final boolean bAnonymizeIP)
  {
    this (sAccount, bAnonymizeIP, false);
  }

  public HCGoogleAnalytics (@Nonnull @Nonempty final String sAccount,
                            final boolean bAnonymizeIP,
                            final boolean bEnhancedLinkAttribution)
  {
    super (_createJSCode (sAccount, bAnonymizeIP, bEnhancedLinkAttribution));
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
    final JSArray aArray = new JSArray ().add ("_trackEvent").add (sCategory).add (sAction);
    if (StringHelper.hasText (sLabel))
      aArray.add (sLabel);
    if (aValue != null)
      aArray.add (aValue.intValue ());

    return JSExpr.ref ("_gaq").invoke ("push").arg (aArray);
  }
}
