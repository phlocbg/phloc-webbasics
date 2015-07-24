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
package com.phloc.webctrls.facebook;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.mgr.WebScopeManager;

public class HCFacebookSDK extends HCNodeList
{
  private static final long serialVersionUID = 8777348152828970837L;
  private static String REQUEST_ATTR_FB_SDK_INCLUDED = "fb-sdk-included"; //$NON-NLS-1$
  private static final String FB_ROOT_ID = "fb-root"; //$NON-NLS-1$
  private static final String FB_API_VERSION = "v2.2"; //$NON-NLS-1$
  private static final Logger LOG = LoggerFactory.getLogger (HCFacebookSDK.class);

  public HCFacebookSDK (@Nonnull @Nonempty final String sAppID,
                        @Nonnull final Locale aDisplayLocale,
                        final boolean bCheckLoginStatus,
                        final boolean bEnableCookies,
                        final boolean bUseXFBML)
  {
    this (sAppID, aDisplayLocale, bCheckLoginStatus, bEnableCookies, bUseXFBML, FB_API_VERSION);
  }

  public HCFacebookSDK (@Nonnull @Nonempty final String sAppID,
                        @Nonnull final Locale aDisplayLocale,
                        final boolean bCheckLoginStatus,
                        final boolean bEnableCookies,
                        final boolean bUseXFBML,
                        final String sAPIVersion)
  {
    if (isIncludedInRequest ())
    {
      LOG.info ("FB SDK already included!"); //$NON-NLS-1$
      return;
    }
    addChild (new HCDiv ().setID (FB_ROOT_ID));
    if (true)
    {
      addChild (new HCScriptOnDocumentReady (JSExpr.invoke ("facebookLoadSDKjQuery") //$NON-NLS-1$
                                                   .arg (sAppID)
                                                   .arg (FacebookLocaleMapping.getInstance ()
                                                                              .getFBLocale (aDisplayLocale)
                                                                              .toString ())
                                                   .arg (FB_ROOT_ID)
                                                   .arg (bCheckLoginStatus)
                                                   .arg (bEnableCookies)
                                                   .arg (bUseXFBML)
                                                   .arg (sAPIVersion)));
    }
    else
    {
      addChild (new HCScript (new JSInvocation ("facebookLoadSDKAsync").arg (sAppID) //$NON-NLS-1$
                                                                       .arg (FacebookLocaleMapping.getInstance ()
                                                                                                  .getFBLocale (aDisplayLocale)
                                                                                                  .toString ())
                                                                       .arg (FB_ROOT_ID)
                                                                       .arg (bCheckLoginStatus)
                                                                       .arg (bEnableCookies)
                                                                       .arg (bUseXFBML)
                                                                       .arg (sAPIVersion)));
    }
    markIncludedInRequest ();
    registerExternalResources ();
  }

  @Nullable
  public static boolean isIncludedInRequest ()
  {
    final IRequestWebScopeWithoutResponse aRequestScope = WebScopeManager.getRequestScopeOrNull ();
    if (aRequestScope != null)
    {
      return aRequestScope.getAttributeAsBoolean (REQUEST_ATTR_FB_SDK_INCLUDED);
    }
    return false;
  }

  @Nullable
  private static void markIncludedInRequest ()
  {
    final IRequestWebScopeWithoutResponse aRequestScope = WebScopeManager.getRequestScopeOrNull ();
    if (aRequestScope != null)
    {
      aRequestScope.setAttribute (REQUEST_ATTR_FB_SDK_INCLUDED, true);
    }
  }

  /**
   * Registers all external resources (CSS or JS files) this control needs
   */
  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EFacebookJSPathProvider.FACEBOOK);
  }
}
