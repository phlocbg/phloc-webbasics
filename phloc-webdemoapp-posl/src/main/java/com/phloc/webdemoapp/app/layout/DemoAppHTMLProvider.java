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
package com.phloc.webdemoapp.app.layout;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.html.HCHead;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCLink;
import com.phloc.html.hc.html.HCScriptFile;
import com.phloc.html.resource.css.ICSSPathProvider;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webbasics.app.layout.ApplicationLayoutManager;
import com.phloc.webbasics.app.layout.LayoutHTMLProvider;
import com.phloc.webdemoapp.ui.config.DemoAppLinkHelper;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Main class for creating HTML output
 * 
 * @author Philip Helger
 */
public class DemoAppHTMLProvider extends LayoutHTMLProvider
{
  public DemoAppHTMLProvider ()
  {
    super (ApplicationLayoutManager.getInstance ());
    setCreateLayoutAreaSpan (false);
  }

  public static final void addPerRequestCSSAndJS (@Nonnull final HCHead aHead)
  {
    // Regular - non-minimized - version?
    final boolean bRegular = GlobalDebug.isDebugMode ();

    // Add per request CSS and JS
    for (final ICSSPathProvider aCSS : PerRequestCSSIncludes.getAllRegisteredCSSIncludesForThisRequest ())
      aHead.addCSS (HCLink.createCSSLink (DemoAppLinkHelper.getStreamURL (aCSS.getCSSItemPath (bRegular))));
    for (final IJSPathProvider aJS : PerRequestJSIncludes.getAllRegisteredJSIncludesForThisRequest ())
      aHead.addJS (HCScriptFile.create (DemoAppLinkHelper.getStreamURL (aJS.getJSItemPath (bRegular))));
  }

  /**
   * Fill the HTML HEAD element.
   * 
   * @param aHtml
   *        The HTML object to be filled.
   */
  @Override
  @OverrideOnDemand
  protected void fillHead (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                           @Nonnull final HCHtml aHtml,
                           @Nonnull final Locale aDisplayLocale)
  {
    super.fillHead (aRequestScope, aHtml, aDisplayLocale);
    addPerRequestCSSAndJS (aHtml.getHead ());
    aHtml.getHead ().setPageTitle ("phloc-webdemoapp");
  }
}
