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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
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

  private final List <String> m_aLayoutAreaIDs;

  public LayoutHTMLProvider ()
  {
    this (GlobalLayoutManager.getInstance ().getAllAreaIDs ());
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

  @OverrideOnDemand
  @Nullable
  protected IHCNode getContentOfArea (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                      @Nonnull final String sAreaID,
                                      @Nonnull final Locale aDisplayLocale)
  {
    // By default the layout manager is used
    return GlobalLayoutManager.getInstance ().getContentOfArea (aRequestScope, sAreaID, aDisplayLocale);
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
  }
}
