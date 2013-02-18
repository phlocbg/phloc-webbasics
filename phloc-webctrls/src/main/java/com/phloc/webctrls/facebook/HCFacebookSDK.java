package com.phloc.webctrls.facebook;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSInvocation;
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
    addChild (new HCScript (new JSInvocation ("facebookLoadSDKAsync").arg (sAppID)
                                                                     .arg (FacebookLocaleMapping.getInstance ()
                                                                                                .getFBLocale (aDisplayLocale)
                                                                                                .toString ())
                                                                     .arg (FB_ROOT_ID)
                                                                     .arg (bCheckLoginStatus)
                                                                     .arg (bEnableCookies)
                                                                     .arg (bUseXFBML)));
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
