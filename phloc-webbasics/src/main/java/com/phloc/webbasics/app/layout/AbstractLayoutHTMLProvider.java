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
package com.phloc.webbasics.app.layout;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCBody;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.web.smtp.IEmailAttachmentList;
import com.phloc.webbasics.app.SimpleWebExecutionContext;
import com.phloc.webbasics.app.error.InternalErrorHandler;
import com.phloc.webbasics.app.html.AbstractHTMLProvider;

/**
 * Abstract class for create layouts based on certain areas.
 * 
 * @author Philip Helger
 */
public abstract class AbstractLayoutHTMLProvider extends AbstractHTMLProvider
{
  public static final boolean DEFAULT_CREATE_LAYOUT_AREA_SPAN = true;

  /** CSS class for each layout area - mainly for debugging */
  public static final ICSSClassProvider CSS_CLASS_LAYOUT_AREA = DefaultCSSClassProvider.create ("layout_area");

  private final List <String> m_aLayoutAreaIDs;
  private boolean m_bCreateLayoutAreaSpan = DEFAULT_CREATE_LAYOUT_AREA_SPAN;

  public AbstractLayoutHTMLProvider (@Nonnull @Nonempty final List <String> aLayoutAreaIDs)
  {
    ValueEnforcer.notEmpty (aLayoutAreaIDs, "LayoutAreaIDs");

    m_aLayoutAreaIDs = ContainerHelper.newList (aLayoutAreaIDs);
  }

  /**
   * @return A non-<code>null</code> , non-empty list of all contained layout
   *         area IDs.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public final List <String> getAllLayoutAreaIDs ()
  {
    return ContainerHelper.newList (m_aLayoutAreaIDs);
  }

  /**
   * @return <code>true</code> if a span surrounding all layout elements should
   *         be created, <code>false</code> if created layout elements should
   *         directly be attached to the HTML body. Default value is
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
  public AbstractLayoutHTMLProvider setCreateLayoutAreaSpan (final boolean bCreateLayoutAreaSpan)
  {
    m_bCreateLayoutAreaSpan = bCreateLayoutAreaSpan;
    return this;
  }

  /**
   * Overridable method that is called before the content areas are rendered
   * 
   * @param aLEC
   *        The layout execution context to use. Never <code>null</code>.
   * @param aHtml
   *        HTML element
   */
  @OverrideOnDemand
  protected void prepareBodyBeforeAreas (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final HCHtml aHtml)
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
  @Nullable
  protected abstract IHCNode getContentOfArea (@Nonnull LayoutExecutionContext aLEC, @Nonnull String sAreaID);

  /**
   * Overridable method that is called after the content areas are rendered
   * 
   * @param aLEC
   *        Layout execution context
   * @param aHtml
   *        HTML element
   */
  @OverrideOnDemand
  protected void prepareBodyAfterAreas (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final HCHtml aHtml)
  {}

  @Override
  protected void fillBody (@Nonnull final SimpleWebExecutionContext aSWEC, @Nonnull final HCHtml aHtml)
  {
    final IMenuItemPage aSelectedMenuItem = ApplicationRequestManager.getInstance ().getRequestMenuItem ();
    final LayoutExecutionContext aLEC = new LayoutExecutionContext (aSWEC, aSelectedMenuItem);

    // create the default layout and fill the areas
    final HCBody aBody = aHtml.getBody ();

    // Before callback
    prepareBodyBeforeAreas (aLEC, aHtml);

    // For all layout areas
    for (final String sAreaID : m_aLayoutAreaIDs)
    {
      try
      {
        final IHCNode aContent = getContentOfArea (aLEC, sAreaID);
        if (m_bCreateLayoutAreaSpan)
        {
          // Create a span around the context
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
        InternalErrorHandler.handleInternalError (aBody,
                                                  t,
                                                  aSWEC.getRequestScope (),
                                                  (String) null,
                                                  (IEmailAttachmentList) null,
                                                  aSWEC.getDisplayLocale (),
                                                  true);
      }
    }

    // After callback
    prepareBodyAfterAreas (aLEC, aHtml);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("layoutAreaIDs", m_aLayoutAreaIDs)
                                       .append ("createLayoutAreaSpan", m_bCreateLayoutAreaSpan)
                                       .toString ();
  }
}
