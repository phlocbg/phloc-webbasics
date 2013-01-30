/**
 * Copyright (C) 2012-2013 Philip Helger <ph@phloc.com>
 * All Rights Reserved
 *
 * This file is part of the Ecoware Online Shop.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.phloc.webctrls.bootstrap.servlet;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.version.Version;
import com.phloc.html.hc.conversion.HCConversionSettingsProvider;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.customize.HCDefaultCustomizer;
import com.phloc.html.hc.customize.HCMultiCustomizer;
import com.phloc.webbasics.app.ApplicationWebSettings;
import com.phloc.webbasics.servlet.WebAppListenerMultiApp;
import com.phloc.webctrls.bootstrap.CBootstrap;
import com.phloc.webctrls.bootstrap.EBootstrapIcon;
import com.phloc.webctrls.bootstrap.ext.BootstrapCustomizer;

/**
 * Bootstrap specific initialization listener
 * 
 * @author philip
 */
public abstract class WebAppListenerMultiAppBootstrap extends WebAppListenerMultiApp
{
  @Nonnull
  @OverrideOnDemand
  protected Version getBoostrapVersion ()
  {
    return CBootstrap.BOOTSTRAP_VERSION_222;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  protected void initGlobals ()
  {
    super.initGlobals ();

    // UI stuff:

    // Special Bootstrap customizer
    final HCConversionSettingsProvider aCSP = new HCConversionSettingsProvider (ApplicationWebSettings.getHTMLVersion ());
    aCSP.setCustomizer (new HCMultiCustomizer (new HCDefaultCustomizer (),
                                               new BootstrapCustomizer (getBoostrapVersion ())));
    HCSettings.setConversionSettingsProvider (aCSP);

    // Using Bootstrap icon set by default
    EBootstrapIcon.setAsDefault ();
  }
}
