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
package com.phloc.webdemoapp.app.layout.config;

import javax.annotation.Nonnull;

import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.layout.CLayout;
import com.phloc.webbasics.app.layout.ILayoutAreaContentProvider;
import com.phloc.webbasics.app.layout.ILayoutManager;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;
import com.phloc.webctrls.bootstrap.BootstrapContainer;
import com.phloc.webctrls.bootstrap.BootstrapRow;
import com.phloc.webctrls.bootstrap.EBootstrapSpan;

/**
 * This class registers the renderer for the layout areas.
 * 
 * @author philip
 */
public final class LayoutConfig
{
  /**
   * The header renderer.
   * 
   * @author philip
   */
  private static final class AreaHeader implements ILayoutAreaContentProvider
  {
    public IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC)
    {
      return ConfigHeaderProvider.getContent (aLEC.getDisplayLocale ());
    }
  }

  /**
   * The navigation bar renderer.
   * 
   * @author philip
   */
  private static final class AreaNavBar implements ILayoutAreaContentProvider
  {
    public IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC)
    {
      return ConfigNavbarProvider.getContent (aLEC.getDisplayLocale ()).build ();
    }
  }

  /**
   * The viewport renderer (menu + content area)
   * 
   * @author philip
   */
  private static final class AreaViewPort implements ILayoutAreaContentProvider
  {
    @Nonnull
    public IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC)
    {
      final BootstrapRow aRow = new BootstrapRow (true);
      // left
      final HCNodeList aLeft = new HCNodeList ();
      // We need a wrapper span for easy AJAX content replacement
      aLeft.addChild (HCSpan.create (ConfigMenuProvider.getContent (aLEC.getDisplayLocale ()))
                            .setID (CLayout.LAYOUT_AREAID_MENU));
      aLeft.addChild (new HCDiv ().setID (CLayout.LAYOUT_AREAID_SPECIAL));
      aRow.addColumn (EBootstrapSpan.SPAN3, aLeft);

      // content
      aRow.addColumn (EBootstrapSpan.SPAN9, ConfigContentProvider.getContent (aLEC));

      final BootstrapContainer aContentLayout = new BootstrapContainer (true);
      aContentLayout.setContent (aRow);
      return aContentLayout.build ();
    }
  }

  private LayoutConfig ()
  {}

  public static void init (@Nonnull final ILayoutManager aLayoutMgr)
  {
    // Register all layout area handler (order is important for SEO!)
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_HEADER, new AreaHeader ());
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_NAVBAR, new AreaNavBar ());
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_VIEWPORT, new AreaViewPort ());
  }
}
