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
package com.phloc.webbasics.app.page;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;

/**
 * This page is instantiated per page view, so that the thread safety of the
 * execution parameters is more clear.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class WebPageExecutionContext extends LayoutExecutionContext
{
  private final IWebPage m_aWebPage;
  private final HCNodeList m_aNodeList = new HCNodeList ();

  public WebPageExecutionContext (@Nonnull final WebPageExecutionContext aWPEC)
  {
    this (aWPEC, aWPEC.getWebPage ());
  }

  public WebPageExecutionContext (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final IWebPage aWebPage)
  {
    super (aLEC, aLEC.getSelectedMenuItem ());
    m_aWebPage = ValueEnforcer.notNull (aWebPage, "WebPage");
  }

  /**
   * @return The invoked web page. Never <code>null</code>.
   */
  @Nonnull
  public IWebPage getWebPage ()
  {
    return m_aWebPage;
  }

  /**
   * @return The node list to be filled with content. Never <code>null</code>.
   */
  @Nonnull
  public HCNodeList getNodeList ()
  {
    return m_aNodeList;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("webPage", m_aWebPage)
                            .append ("nodeList", m_aNodeList)
                            .toString ();
  }
}
