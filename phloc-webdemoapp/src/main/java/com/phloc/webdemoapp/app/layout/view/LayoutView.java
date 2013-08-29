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
package com.phloc.webdemoapp.app.layout.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCP;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.layout.CLayout;
import com.phloc.webbasics.app.layout.ILayoutAreaContentProvider;
import com.phloc.webbasics.app.layout.ILayoutManager;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;
import com.phloc.webctrls.bootstrap.ext.BootstrapMenuItemRendererWellHorz;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.bootstrap3.base.Bootstrap3Container;
import com.phloc.webctrls.bootstrap3.breadcrumbs.Bootstrap3Breadcrumbs;
import com.phloc.webctrls.bootstrap3.breadcrumbs.Bootstrap3BreadcrumbsProvider;
import com.phloc.webctrls.bootstrap3.grid.Bootstrap3Row;
import com.phloc.webctrls.bootstrap3.grid.EBootstrap3GridMD;
import com.phloc.webdemoapp.app.menu.view.CDemoAppMenuView;

/**
 * This class registers the renderer for the layout areas.
 * 
 * @author Philip Helger
 */
public final class LayoutView
{
  /**
   * The viewport renderer (menu + content area)
   * 
   * @author Philip Helger
   */
  private static final class AreaViewPort implements ILayoutAreaContentProvider
  {
    private static final ICSSClassProvider CSS_CLASS_FOOTER_LINKS = DefaultCSSClassProvider.create ("footer-links");

    private List <IMenuObject> m_aFooterObjects;

    /**
     * Retrieve and cache all menu objects to be displayed in the footer. It can
     * be cached, as the menu cannot be modified at runtime.
     * 
     * @return Never <code>null</code>
     */
    @Nonnull
    private List <IMenuObject> _getFooterObjects ()
    {
      if (m_aFooterObjects == null)
      {
        m_aFooterObjects = new ArrayList <IMenuObject> ();
        ApplicationMenuTree.getInstance ()
                           .iterateAllMenuObjects (new INonThrowingRunnableWithParameter <IMenuObject> ()
                           {
                             public void run (@Nonnull final IMenuObject aCurrentObject)
                             {
                               if (aCurrentObject.containsFlag (CDemoAppMenuView.FLAG_FOOTER))
                                 m_aFooterObjects.add (aCurrentObject);
                             }
                           });
      }
      return m_aFooterObjects;
    }

    @Nonnull
    public IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC)
    {
      final Bootstrap3Container aOuterContainer = new Bootstrap3Container ();

      // Header
      {
        aOuterContainer.addChild (ViewHeaderProvider.getContent (aLEC.getDisplayLocale ()));
      }

      // Breadcrumbs
      {
        final Bootstrap3Breadcrumbs aBreadcrumbs = Bootstrap3BreadcrumbsProvider.createBreadcrumbs (ApplicationMenuTree.getInstance (),
                                                                                                    aLEC.getDisplayLocale ());
        aBreadcrumbs.addClass (CBootstrap3CSS.HIDDEN_XS);
        aOuterContainer.addChild (aBreadcrumbs);
      }

      // Content
      {
        final Bootstrap3Row aRow = new Bootstrap3Row ();
        // left
        final HCNodeList aLeft = new HCNodeList ();
        // We need a wrapper span for easy AJAX content replacement
        aLeft.addChild (HCSpan.create (ViewMenuProvider.getContent (aLEC.getDisplayLocale ()))
                              .setID (CLayout.LAYOUT_AREAID_MENU));
        aLeft.addChild (new HCDiv ().setID (CLayout.LAYOUT_AREAID_SPECIAL));
        aRow.createColumn (EBootstrap3GridMD.MD_3).addChild (aLeft);

        // content
        aRow.createColumn (EBootstrap3GridMD.MD_9).addChild (ViewContentProvider.getContent (aLEC));
        aOuterContainer.addChild (aRow);
      }

      {
        final HCDiv aDiv = new HCDiv ().addClass (CBootstrap3CSS.CONTAINER).setID (CLayout.LAYOUT_AREAID_FOOTER);

        aDiv.addChild (HCP.create ("Demo web application for the phloc OSS-stack"));
        aDiv.addChild (HCP.create ("Created by phloc-systems"));

        final BootstrapMenuItemRendererWellHorz aRenderer = new BootstrapMenuItemRendererWellHorz (aLEC.getDisplayLocale ());
        final HCUL aUL = aDiv.addAndReturnChild (new HCUL ().addClass (CSS_CLASS_FOOTER_LINKS));
        for (final IMenuObject aMenuObj : _getFooterObjects ())
        {
          if (aMenuObj instanceof IMenuSeparator)
            aUL.addItem (aRenderer.renderSeparator ((IMenuSeparator) aMenuObj));
          else
            if (aMenuObj instanceof IMenuItemPage)
              aUL.addItem (aRenderer.renderMenuItemPage ((IMenuItemPage) aMenuObj, false, false, false));
            else
              if (aMenuObj instanceof IMenuItemExternal)
                aUL.addItem (aRenderer.renderMenuItemExternal ((IMenuItemExternal) aMenuObj, false, false, false));
              else
                throw new IllegalStateException ("Unsupported menu object type!");
        }
        aOuterContainer.addChild (aDiv);
      }

      return aOuterContainer;
    }
  }

  private LayoutView ()
  {}

  public static void init (@Nonnull final ILayoutManager aLayoutMgr)
  {
    // Register all layout area handler (order is important for SEO!)
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_VIEWPORT, new AreaViewPort ());
  }
}
