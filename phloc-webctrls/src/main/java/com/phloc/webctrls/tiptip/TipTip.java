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
package com.phloc.webctrls.tiptip;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.AbstractHCSpan;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

/**
 * Tooltip class using tiptip jQuery plugin.<br>
 * Important implementation note: must derive from HCSpan since &lt;span>
 * elements may not be self closed in Internet Explorer!
 * 
 * @author philip
 */
public final class TipTip extends AbstractHCSpan <TipTip>
{
  /** Used for tooltip initialization as jquery selector! */
  public static final ICSSClassProvider CSS_CLASS_TOOLTIP = DefaultCSSClassProvider.create ("nocss_tooltip");

  private static final Logger s_aLogger = LoggerFactory.getLogger (TipTip.class);

  private final ETipType m_eType;

  public TipTip (final IPredefinedLocaleTextProvider aTextProvider)
  {
    this (aTextProvider.getText ());
  }

  public TipTip (final String sText)
  {
    this (sText, ETipType.INFO);
  }

  public TipTip (final String sText, @Nonnull final ETipType eType)
  {
    if (eType == null)
      throw new NullPointerException ("type");

    m_eType = eType;
    setTitle (sText);
    addClasses (m_eType, CSS_CLASS_TOOLTIP);
    registerExternalResources ();

    // Add initializer for tool tip only once per request!
    if (!WebScopeManager.getRequestScope ().getAndSetAttributeFlag (TipTip.class.getName ()))
    {
      // this is to enable jQuery tipTip
      // currently maxWidth set to 45% to avoid page overflow (assuming that the
      // element where it is applied is not very wide (smaller than 5% of the
      // total width)
      addChild (new HCScript (JQuery.classRef (CSS_CLASS_TOOLTIP)
                                    .jqinvoke ("tipTip")
                                    .arg (new JSAssocArray ().add ("maxWidth", "45%").add ("edgeOffset", 5))));
    }
  }

  @Override
  public String getPlainText ()
  {
    return StringHelper.getNotNull (getTitle ());
  }

  @Override
  protected void applyProperties (final IMicroElement aElement, final IHCConversionSettingsToNode aConversionSettings)
  {
    super.applyProperties (aElement, aConversionSettings);

    // Check if the contained text looks like HTML
    final String sTitle = getTitle ();
    if (StringHelper.hasText (sTitle) && (sTitle.indexOf ('<') >= 0 || sTitle.indexOf ('>') >= 0))
      s_aLogger.warn ("The text of a tooltip may not contain '<' or '>' as they are interpreted as HTML code: '" +
                      sTitle +
                      "'");
  }

  /**
   * Registers all external resources (CSS or JS files) this control needs
   */
  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (ETipTipJSPathProvider.TIPTIP_13);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (ETipTipCSSPathProvider.TIPTIP_13);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (ETipTipCSSPathProvider.TOOLTIP);
  }
}
