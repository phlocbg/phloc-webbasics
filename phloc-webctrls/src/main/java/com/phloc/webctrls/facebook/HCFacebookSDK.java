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
package com.phloc.webctrls.facebook;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

public class HCFacebookSDK extends HCNodeList
{
  private static final String FB_ROOT_ID = "fb-root";

  public HCFacebookSDK (@Nonnull @Nonempty final String sAppID,
                        @Nonnull final Locale aDisplayLocale,
                        final boolean bCheckLoginStatus,
                        final boolean bEnableCookies,
                        final boolean bUseXFBML)
  {
    addChild (new HCDiv ().setID (FB_ROOT_ID));
    if (true)
    {
      addChild (new HCScript (JQuery.onDocumentReady (JSExpr.invoke ("facebookLoadSDKjQuery")
                                                            .arg (sAppID)
                                                            .arg (FacebookLocaleMapping.getInstance ()
                                                                                       .getFBLocale (aDisplayLocale)
                                                                                       .toString ())
                                                            .arg (FB_ROOT_ID)
                                                            .arg (bCheckLoginStatus)
                                                            .arg (bEnableCookies)
                                                            .arg (bUseXFBML))));
    }
    else
    {
      addChild (new HCScript (new JSInvocation ("facebookLoadSDKAsync").arg (sAppID)
                                                                       .arg (FacebookLocaleMapping.getInstance ()
                                                                                                  .getFBLocale (aDisplayLocale)
                                                                                                  .toString ())
                                                                       .arg (FB_ROOT_ID)
                                                                       .arg (bCheckLoginStatus)
                                                                       .arg (bEnableCookies)
                                                                       .arg (bUseXFBML)));
    }
    registerExternalResources ();
  }

  /**
   * Registers all external resources (CSS or JS files) this control needs
   */
  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EFacebookJSPathProvider.FACEBOOK);
  }
}
