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
package com.phloc.webbasics.app.page;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

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

  public WebPageExecutionContext (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final IWebPage aWebPage)
  {
    super (aLEC.getRequestScope (), aLEC.getDisplayLocale ());
    if (aWebPage == null)
      throw new NullPointerException ("webPage");
    m_aWebPage = aWebPage;
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
   * @return The node list to be filled with content.
   */
  @Nonnull
  public HCNodeList getNodeList ()
  {
    return m_aNodeList;
  }
}
