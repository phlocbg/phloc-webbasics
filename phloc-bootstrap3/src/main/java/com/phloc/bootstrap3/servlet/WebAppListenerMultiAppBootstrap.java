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
package com.phloc.bootstrap3.servlet;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.bootstrap3.CBootstrap;
import com.phloc.bootstrap3.EBootstrapIcon;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.version.Version;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.conversion.HCConversionSettings;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.customize.HCMultiCustomizer;
import com.phloc.webbasics.app.html.WebHTMLCreator;
import com.phloc.webbasics.app.layout.ILayoutExecutionContext;
import com.phloc.webbasics.servlet.WebAppListenerMultiApp;

/**
 * Bootstrap specific initialization listener
 *
 * @author Philip Helger
 */
public abstract class WebAppListenerMultiAppBootstrap <LECTYPE extends ILayoutExecutionContext> extends WebAppListenerMultiApp <LECTYPE>
{
  @Nonnull
  @OverrideOnDemand
  protected Version getBoostrapVersion ()
  {
    return CBootstrap.BOOTSTRAP_VERSION_320;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  protected void initGlobals ()
  {
    super.initGlobals ();

    // UI stuff:

    // Always use HTML5 for Bootstrap3
    WebHTMLCreator.setHTMLVersion (EHTMLVersion.HTML5);

    // Special Bootstrap customizer
    HCSettings.getConversionSettingsProvider ()
              .setCustomizer (new HCMultiCustomizer (HCConversionSettings.createDefaultCustomizer (),
                                                     new BootstrapCustomizer (getBoostrapVersion ())));

    // Using Bootstrap icon set by default
    EBootstrapIcon.setAsDefault ();
  }
}
