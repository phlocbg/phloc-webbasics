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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.IHCNode;

/**
 * Main class for creating HTML output
 * 
 * @author Philip Helger
 */
public class LayoutHTMLProvider extends AbstractLayoutHTMLProvider
{
  private final ILayoutManager m_aLayoutMgr;

  public LayoutHTMLProvider (@Nonnull final ILayoutManager aLayoutMgr)
  {
    super (aLayoutMgr.getAllAreaIDs ());

    m_aLayoutMgr = aLayoutMgr;
  }

  /**
   * @return The layout manager passed in the constructor. Never
   *         <code>null</code>.
   */
  @Nonnull
  public final ILayoutManager getLayoutManager ()
  {
    return m_aLayoutMgr;
  }

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
  @Override
  @OverrideOnDemand
  @Nullable
  protected IHCNode getContentOfArea (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final String sAreaID)
  {
    // By default the layout manager is used
    return m_aLayoutMgr.getContentOfArea (aLEC, sAreaID);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("layoutMgr", m_aLayoutMgr).toString ();
  }
}
