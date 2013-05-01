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
package com.phloc.webbasics.app.html;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.mime.IMimeType;
import com.phloc.html.CHTMLCharset;
import com.phloc.html.hc.html.HCHead;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.meta.EStandardMetaElement;
import com.phloc.html.meta.MetaElement;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Main class for creating HTML output
 * 
 * @author Philip Helger
 */
public abstract class AbstractHTMLProvider implements IHTMLProvider
{
  private HTMLConfigManager m_aHTMLConfigMgr;

  public AbstractHTMLProvider ()
  {}

  @Nonnull
  @OverrideOnDemand
  protected HTMLConfigManager getHTMLConfigMgr ()
  {
    if (m_aHTMLConfigMgr == null)
      m_aHTMLConfigMgr = new HTMLConfigManager (HTMLConfigManager.DEFAULT_BASE_PATH);
    return m_aHTMLConfigMgr;
  }

  @Nonnull
  @OverrideOnDemand
  protected Locale getDisplayLocale ()
  {
    return ApplicationRequestManager.getInstance ().getRequestDisplayLocale ();
  }

  @OverrideOnDemand
  @Nonnull
  protected HCHtml createHCHtml (@Nonnull final Locale aDisplayLocale)
  {
    return new HCHtml ().setLanguage (aDisplayLocale.getLanguage ());
  }

  @Nonnull
  @ReturnsMutableCopy
  @OverrideOnDemand
  protected Map <String, String> getAllMetaTags ()
  {
    return getHTMLConfigMgr ().getAllMetaTags ();
  }

  @Nonnull
  @ReturnsMutableCopy
  @OverrideOnDemand
  protected List <CSSFiles.Item> getAllCSSItems ()
  {
    return getHTMLConfigMgr ().getAllCSSItems ();
  }

  @Nonnull
  @ReturnsMutableCopy
  @OverrideOnDemand
  protected List <JSFiles.Item> getAllJSItems ()
  {
    return getHTMLConfigMgr ().getAllJSItems ();
  }

  /**
   * Fill the HTML HEAD element.
   * 
   * @param aRequestScope
   *        The request scope to use
   * @param aHtml
   *        The HTML object to be filled.
   * @param aDisplayLocale
   *        The display locale to use
   */
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void fillHead (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                           @Nonnull final HCHtml aHtml,
                           @Nonnull final Locale aDisplayLocale)
  {
    final HCHead aHead = aHtml.getHead ();

    // Special meta tag
    final IMimeType aMimeType = WebHTMLCreator.getMimeType (aRequestScope);
    aHead.addMetaElement (EStandardMetaElement.CONTENT_TYPE.getAsMetaElement (aMimeType.getAsStringWithEncoding (CHTMLCharset.CHARSET_HTML)));

    // Add all configured meta element
    for (final Map.Entry <String, String> aEntry : getAllMetaTags ().entrySet ())
      aHead.addMetaElement (new MetaElement (aEntry.getKey (), aEntry.getValue ()));

    // Add configured CSS
    for (final CSSFiles.Item aCSSItem : getAllCSSItems ())
      aHead.addCSS (aCSSItem.getAsNode ());

    // Add all configured JS
    for (final JSFiles.Item aJSFile : getAllJSItems ())
      aHead.addJS (aJSFile.getAsNode ());
  }

  /**
   * Fill the HTML body
   * 
   * @param aRequestScope
   *        request scope to use
   * @param aHtml
   *        HTML object to be filled
   * @param aDisplayLocale
   *        Display locale to use
   */
  protected abstract void fillBody (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                    @Nonnull HCHtml aHtml,
                                    @Nonnull Locale aDisplayLocale);

  @Nonnull
  public final HCHtml createHTML (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    final Locale aDisplayLocale = getDisplayLocale ();

    final HCHtml aHtml = createHCHtml (aDisplayLocale);

    // fill body
    fillBody (aRequestScope, aHtml, aDisplayLocale);

    // build HTML header (after body for per-request stuff)
    fillHead (aRequestScope, aHtml, aDisplayLocale);

    return aHtml;
  }
}
