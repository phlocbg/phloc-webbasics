/**
 * Copyright (C) 2006-2012 phloc systems
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
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.string.StringHelper;
import com.phloc.css.media.CSSMediaList;
import com.phloc.css.media.ECSSMedium;
import com.phloc.html.CHTMLCharset;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.condcomment.ConditionalComment;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCBody;
import com.phloc.html.hc.html.HCHead;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.meta.EStandardMetaElement;
import com.phloc.html.meta.MetaElement;
import com.phloc.html.resource.css.CSSExternal;
import com.phloc.html.resource.js.JSExternal;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.webbasics.app.ApplicationRequestManager;
import com.phloc.webbasics.app.LinkUtils;

/**
 * Main class for creating HTML output
 * 
 * @author philip
 */
public class LayoutHTMLProvider implements IHTMLProvider
{
  public static final ICSSClassProvider CSS_CLASS_LAYOUT_AREA = DefaultCSSClassProvider.create ("layout_area");

  private final List <String> m_aLayoutAreaIDs;

  public LayoutHTMLProvider ()
  {
    this (LayoutManager.getInstance ().getAllAreaIDs ());
  }

  public LayoutHTMLProvider (@Nonnull @Nonempty final String sLayoutAreaID)
  {
    if (StringHelper.hasNoText (sLayoutAreaID))
      throw new IllegalArgumentException ("layoutAreaID");
    m_aLayoutAreaIDs = ContainerHelper.newList (sLayoutAreaID);
  }

  public LayoutHTMLProvider (@Nonnull @Nonempty final List <String> aLayoutAreaIDs)
  {
    if (ContainerHelper.isEmpty (aLayoutAreaIDs))
      throw new IllegalArgumentException ("layoutAreaIDs");
    m_aLayoutAreaIDs = ContainerHelper.newList (aLayoutAreaIDs);
  }

  /**
   * @param aHtml
   *        HTML element
   */
  @OverrideOnDemand
  protected void prepareBodyBeforeAreas (@Nonnull final HCHtml aHtml)
  {}

  /**
   * @param aHtml
   *        HTML element
   */
  @OverrideOnDemand
  protected void prepareBodyAfterAreas (@Nonnull final HCHtml aHtml)
  {}

  /**
   * Fill the HTML HEAD element.
   * 
   * @param aHtml
   *        The HTML object to be filled.
   */
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void fillHead (@Nonnull final HCHtml aHtml)
  {
    final HCHead aHead = aHtml.getHead ();

    // Special meta tag
    aHead.addMetaElement (EStandardMetaElement.CONTENT_TYPE.getAsMetaElement (CMimeType.TEXT_XML.getAsStringWithEncoding (CHTMLCharset.CHARSET_HTML)));

    // Add all configured meta element
    for (final Map.Entry <String, String> aEntry : HTMLConfigManager.getInstance ().getAllMetaTags ().entrySet ())
      aHead.addMetaElement (new MetaElement (aEntry.getKey (), aEntry.getValue ()));

    // Add configured CSS
    for (final String sCSSFile : HTMLConfigManager.getInstance ().getAllCSSFiles ())
      aHead.addCSS (new CSSExternal (LinkUtils.getURLWithContext (sCSSFile)));

    // Add configured print-only CSS
    for (final String sCSSPrintFile : HTMLConfigManager.getInstance ().getAllCSSPrintFiles ())
      aHead.addCSS (new CSSExternal (LinkUtils.getURLWithContext (sCSSPrintFile),
                                     new CSSMediaList (ECSSMedium.PRINT),
                                     null));

    // Add configured IE-only CSS
    for (final String sCSSIEFile : HTMLConfigManager.getInstance ().getAllCSSIEFiles ())
      aHead.addCSS (new CSSExternal (LinkUtils.getURLWithContext (sCSSIEFile), null, ConditionalComment.createForIE ()));

    // Add all configured JS
    for (final String sJSFile : HTMLConfigManager.getInstance ().getAllJSFiles ())
      aHead.addJS (new JSExternal (LinkUtils.getURLWithContext (sJSFile)));
  }

  @OverrideOnDemand
  @Nullable
  protected IHCNode getContentOfArea (@Nonnull final IRequestWebScope aRequestScope,
                                      @Nonnull final String sAreaID,
                                      @Nonnull final Locale aDisplayLocale)
  {
    // By default the layout manager is used
    return LayoutManager.getInstance ().getContentOfArea (aRequestScope, sAreaID, aDisplayLocale);
  }

  @OverrideOnDemand
  @Nonnull
  protected HCHtml createHCHtml (@Nonnull final EHTMLVersion eVersion)
  {
    return new HCHtml (eVersion);
  }

  @Nonnull
  public final HCHtml createHTML (@Nonnull final IRequestWebScope aRequestScope, @Nonnull final EHTMLVersion eVersion)
  {
    final HCHtml aHtml = createHCHtml (eVersion);
    final Locale aDisplayLocale = ApplicationRequestManager.getRequestDisplayLocale ();

    // create the default layout and fill the areas
    final HCBody aBody = aHtml.getBody ();

    prepareBodyBeforeAreas (aHtml);

    for (final String sAreaID : m_aLayoutAreaIDs)
    {
      final HCSpan aSpan = aBody.addAndReturnChild (new HCSpan ().addClass (CSS_CLASS_LAYOUT_AREA).setID (sAreaID));
      try
      {
        aSpan.addChild (getContentOfArea (aRequestScope, sAreaID, aDisplayLocale));
      }
      catch (final Throwable t)
      {
        // send internal error mail here
        InternalErrorHandler.handleInternalError (aSpan, t);
      }
    }

    prepareBodyAfterAreas (aHtml);

    // build HTML header (after body for per-request stuff)
    fillHead (aHtml);

    return aHtml;
  }
}
