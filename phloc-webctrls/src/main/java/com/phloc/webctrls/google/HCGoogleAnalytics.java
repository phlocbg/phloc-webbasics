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
package com.phloc.webctrls.google;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.impl.AbstractWrappedHCNode;
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
 * @author philip
 */
public class HCGoogleAnalytics extends AbstractWrappedHCNode
{
  private final String m_sAccount;
  private final HCScript m_aScript;

  public HCGoogleAnalytics (@Nonnull @Nonempty final String sAccount, final boolean bAnonymizeIP)
  {
    if (StringHelper.hasNoText (sAccount))
      throw new IllegalArgumentException ("account is empty");
    m_sAccount = sAccount;

    // Anonymize IP; see
    // http://code.google.com/intl/de-DE/apis/analytics/docs/gaJS/gaJSApi_gat.html
    final JSPackage aPkg = new JSPackage ();
    final JSVar gaq = aPkg.var ("_gaq", JSExpr.ref ("_gaq").cor (new JSArray ()));
    aPkg.addStatement (gaq.invoke ("push").arg (new JSArray ().add ("_setAccount").add (m_sAccount)));
    if (bAnonymizeIP)
      aPkg.addStatement (gaq.invoke ("push").arg (new JSArray ().add ("_gat._anonymizeIp")));
    aPkg.addStatement (gaq.invoke ("push").arg (new JSArray ().add ("_trackPageview")));
    aPkg.addStatement (gaq.invoke ("push").arg (new JSArray ().add ("_trackPageLoadTime")));
    final JSAnonymousFunction aAnonFunction = new JSAnonymousFunction ();
    final JSVar ga = aAnonFunction.body ().decl ("ga", JSHtml.documentCreateElement ().arg ("script"));
    aAnonFunction.body ().add (ga.ref ("type").assign (CMimeType.TEXT_JAVASCRIPT.getAsString ()));
    aAnonFunction.body ().add (ga.ref ("async").assign (true));
    aAnonFunction.body ().add (ga.ref ("src").assign (JSOp.cond (JSExpr.lit ("https:")
                                                                       .eq (JSHtml.windowLocationProtocol ()),
                                                                 JSExpr.lit ("https://ssl"),
                                                                 JSExpr.lit ("http://www"))
                                                          .plus (".google-analytics.com/ga.js")));
    final JSVar s = aAnonFunction.body ().decl ("s", JSHtml.documentGetElementsByTagName ("script").component (0));
    aAnonFunction.body ().add (s.ref ("parentNode").invoke ("insertBefore").arg (ga).arg (s));
    aPkg.invoke (aAnonFunction);
    m_aScript = new HCScript (aPkg);
  }

  @Nonnull
  @Nonempty
  public String getAccount ()
  {
    return m_sAccount;
  }

  @Override
  protected HCScript getContainedHCNode ()
  {
    return m_aScript;
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
