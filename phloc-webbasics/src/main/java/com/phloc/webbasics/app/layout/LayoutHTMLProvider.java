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
package com.phloc.webbasics.app.layout;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCBody;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.app.html.AbstractHTMLProvider;
import com.phloc.webbasics.app.html.InternalErrorHandler;

/**
 * Main class for creating HTML output
 * 
 * @author philip
 */
public class LayoutHTMLProvider extends AbstractHTMLProvider
{
  public static final ICSSClassProvider CSS_CLASS_LAYOUT_AREA = DefaultCSSClassProvider.create ("layout_area");

  private final ILayoutManager m_aLayoutMgr;
  private final List <String> m_aLayoutAreaIDs;

  public LayoutHTMLProvider (@Nonnull final ILayoutManager aLayoutMgr)
  {
    if (aLayoutMgr == null)
      throw new NullPointerException ("LayoutManager");

    m_aLayoutMgr = aLayoutMgr;
    m_aLayoutAreaIDs = aLayoutMgr.getAllAreaIDs ();
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

  @OverrideOnDemand
  @Nullable
  protected IHCNode getContentOfArea (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final String sAreaID)
  {
    // By default the layout manager is used
    return m_aLayoutMgr.getContentOfArea (aLEC, sAreaID);
  }

  @Override
  @Nonnull
  protected void fillBody (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                           @Nonnull final HCHtml aHtml,
                           @Nonnull final Locale aDisplayLocale)
  {
    // create the default layout and fill the areas
    final HCBody aBody = aHtml.getBody ();

    prepareBodyBeforeAreas (aHtml);

    final LayoutExecutionContext aLEC = new LayoutExecutionContext (aRequestScope, aDisplayLocale);

    for (final String sAreaID : m_aLayoutAreaIDs)
    {
      try
      {
        final HCSpan aSpan = new HCSpan ().addClass (CSS_CLASS_LAYOUT_AREA).setID (sAreaID);
        aSpan.addChild (getContentOfArea (aLEC, sAreaID));
        // Append to body if no error occurred
        aBody.addChild (aSpan);
      }
      catch (final Throwable t)
      {
        // send internal error mail here
        InternalErrorHandler.handleInternalError (aBody, t);
      }
    }

    prepareBodyAfterAreas (aHtml);
  }
}
