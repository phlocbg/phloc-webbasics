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
import com.phloc.webbasics.app.error.InternalErrorHandler;
import com.phloc.webbasics.app.html.AbstractHTMLProvider;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Main class for creating HTML output
 * 
 * @author Philip Helger
 */
public class LayoutHTMLProvider extends AbstractHTMLProvider
{
  public static final boolean DEFAULT_CREATE_LAYOUT_AREA_SPAN = true;

  /** CSS class for each layout area - mainly for debugging */
  public static final ICSSClassProvider CSS_CLASS_LAYOUT_AREA = DefaultCSSClassProvider.create ("layout_area");

  private final ILayoutManager m_aLayoutMgr;
  private final List <String> m_aLayoutAreaIDs;
  private boolean m_bCreateLayoutAreaSpan = DEFAULT_CREATE_LAYOUT_AREA_SPAN;

  public LayoutHTMLProvider (@Nonnull final ILayoutManager aLayoutMgr)
  {
    if (aLayoutMgr == null)
      throw new NullPointerException ("LayoutManager");

    m_aLayoutMgr = aLayoutMgr;
    m_aLayoutAreaIDs = aLayoutMgr.getAllAreaIDs ();
  }

  /**
   * @return The layout manager passed in the constructor.
   */
  @Nonnull
  protected final ILayoutManager getLayoutManager ()
  {
    return m_aLayoutMgr;
  }

  /**
   * @return <code>true</code> if a span surrounding all layout elements should
   *         be created, <code>false</code> if not. Default value is
   *         {@link #DEFAULT_CREATE_LAYOUT_AREA_SPAN}.
   */
  public boolean isCreateLayoutAreaSpan ()
  {
    return m_bCreateLayoutAreaSpan;
  }

  /**
   * @param bCreateLayoutAreaSpan
   *        <code>true</code> if a span surrounding all layout elements should
   *        be created, <code>false</code> if not.
   * @return this
   */
  @Nonnull
  public LayoutHTMLProvider setCreateLayoutAreaSpan (final boolean bCreateLayoutAreaSpan)
  {
    m_bCreateLayoutAreaSpan = bCreateLayoutAreaSpan;
    return this;
  }

  /**
   * Overridable method that is called before the content areas are rendered
   * 
   * @param aHtml
   *        HTML element
   */
  @OverrideOnDemand
  protected void prepareBodyBeforeAreas (@Nonnull final HCHtml aHtml)
  {}

  /**
   * Overridable method that is called after the content areas are rendered
   * 
   * @param aHtml
   *        HTML element
   */
  @OverrideOnDemand
  protected void prepareBodyAfterAreas (@Nonnull final HCHtml aHtml)
  {}

  /**
   * Determine the content of a single area.
   * 
   * @param aLEC
   *        The layout execution context to use. Never <code>null</code>.
   * @param sAreaID
   *        The area ID to be rendered.
   * @return The node to be rendered for the passed layout area. May be
   *         <code>null</code>.
   */
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
        final IHCNode aContent = getContentOfArea (aLEC, sAreaID);
        if (m_bCreateLayoutAreaSpan)
        {
          final HCSpan aSpan = new HCSpan ().addClass (CSS_CLASS_LAYOUT_AREA).setID (sAreaID);
          aSpan.addChild (aContent);
          // Append to body if no error occurred
          aBody.addChild (aSpan);
        }
        else
          aBody.addChild (aContent);
      }
      catch (final Throwable t)
      {
        // send internal error mail here
        InternalErrorHandler.handleInternalError (aBody, t, aRequestScope, aDisplayLocale);
      }
    }

    prepareBodyAfterAreas (aHtml);
  }
}
