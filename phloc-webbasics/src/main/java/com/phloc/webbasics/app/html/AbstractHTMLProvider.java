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
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.html.HCHead;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.meta.EStandardMetaElement;
import com.phloc.html.meta.MetaElement;
import com.phloc.webbasics.app.ISimpleWebExecutionContext;
import com.phloc.webbasics.app.SimpleWebExecutionContext;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Main class for creating HTML output
 * 
 * @author Philip Helger
 */
public abstract class AbstractHTMLProvider implements IHTMLProvider
{
  public AbstractHTMLProvider ()
  {}

  @Nonnull
  @OverrideOnDemand
  protected HTMLConfigManager getHTMLConfigMgr ()
  {
    return HTMLConfigManager.getInstance ();
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
  protected List <CSSItem> getAllCSSItems ()
  {
    return getHTMLConfigMgr ().getAllCSSItems ();
  }

  @Nonnull
  @ReturnsMutableCopy
  @OverrideOnDemand
  protected List <JSItem> getAllJSItems ()
  {
    return getHTMLConfigMgr ().getAllJSItems ();
  }

  /**
   * Fill the HTML HEAD element.
   * 
   * @param aSWEC
   *        Web execution context
   * @param aHtml
   *        The HTML object to be filled.
   */
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void fillHead (@Nonnull final ISimpleWebExecutionContext aSWEC, @Nonnull final HCHtml aHtml)
  {
    final IRequestWebScopeWithoutResponse aRequestScope = aSWEC.getRequestScope ();
    final HCHead aHead = aHtml.getHead ();

    // Special meta tag
    final IMimeType aMimeType = WebHTMLCreator.getMimeType (aRequestScope);
    aHead.addMetaElement (EStandardMetaElement.CONTENT_TYPE.getAsMetaElement (aMimeType.getAsString ()));

    // Add all configured meta element
    for (final Map.Entry <String, String> aEntry : getAllMetaTags ().entrySet ())
      aHead.addMetaElement (new MetaElement (aEntry.getKey (), aEntry.getValue ()));

    // Add configured CSS
    for (final CSSItem aCSSItem : getAllCSSItems ())
      aHead.addCSS (aCSSItem.getAsNode (aRequestScope));

    // Add all configured JS
    for (final JSItem aJSFile : getAllJSItems ())
      aHead.addJS (aJSFile.getAsNode (aRequestScope));
  }

  /**
   * Fill the HTML body
   * 
   * @param aSWEC
   *        Web execution context
   * @param aHtml
   *        HTML object to be filled
   */
  protected abstract void fillBody (@Nonnull final ISimpleWebExecutionContext aSWEC, @Nonnull HCHtml aHtml);

  @Nonnull
  public final HCHtml createHTML (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    final Locale aDisplayLocale = getDisplayLocale ();

    // Build the execution scope
    final ISimpleWebExecutionContext aSWEC = new SimpleWebExecutionContext (aRequestScope, aDisplayLocale);

    // Create the surrounding HTML element
    final HCHtml aHtml = createHCHtml (aDisplayLocale);

    // fill body
    fillBody (aSWEC, aHtml);

    // build HTML header (after body for per-request stuff)
    fillHead (aSWEC, aHtml);

    return aHtml;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
