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
package com.phloc.webctrls.bootstrap3.servlet;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.version.Version;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.conversion.HCConversionSettingsProvider;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.customize.HCDefaultCustomizer;
import com.phloc.html.hc.customize.HCMultiCustomizer;
import com.phloc.webbasics.app.ApplicationWebSettings;
import com.phloc.webbasics.servlet.WebAppListenerMultiApp;
import com.phloc.webctrls.bootstrap.EBootstrapIcon;
import com.phloc.webctrls.bootstrap.ext.BootstrapCustomizer;
import com.phloc.webctrls.bootstrap3.CBootstrap3;

/**
 * Bootstrap specific initialization listener
 * 
 * @author Philip Helger
 */
public abstract class WebAppListenerMultiAppBootstrap3 extends WebAppListenerMultiApp
{
  @Nonnull
  @OverrideOnDemand
  protected Version getBoostrapVersion ()
  {
    return CBootstrap3.BOOTSTRAP_VERSION_300;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  protected void initGlobals ()
  {
    super.initGlobals ();

    // UI stuff:

    // Always use HTML5 for Bootstrap3
    ApplicationWebSettings.setHTMLVersion (EHTMLVersion.HTML5);

    // Special Bootstrap customizer
    final HCConversionSettingsProvider aCSP = new HCConversionSettingsProvider (ApplicationWebSettings.getHTMLVersion ());
    aCSP.setCustomizer (new HCMultiCustomizer (new HCDefaultCustomizer (),
                                               new BootstrapCustomizer (getBoostrapVersion ())));
    HCSettings.setConversionSettingsProvider (aCSP);

    // Using Bootstrap icon set by default
    EBootstrapIcon.setAsDefault ();
  }
}
