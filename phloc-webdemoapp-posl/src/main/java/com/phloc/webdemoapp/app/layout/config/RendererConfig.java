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

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.base.BootstrapContainer;
import com.phloc.bootstrap3.breadcrumbs.BootstrapBreadcrumbs;
import com.phloc.bootstrap3.breadcrumbs.BootstrapBreadcrumbsProvider;
import com.phloc.bootstrap3.ext.BootstrapMenuItemRenderer;
import com.phloc.bootstrap3.grid.BootstrapRow;
import com.phloc.bootstrap3.nav.BootstrapNav;
import com.phloc.bootstrap3.navbar.BootstrapNavbar;
import com.phloc.bootstrap3.navbar.EBootstrapNavbarPosition;
import com.phloc.bootstrap3.navbar.EBootstrapNavbarType;
import com.phloc.bootstrap3.pageheader.BootstrapPageHeader;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCH1;
import com.phloc.html.hc.html.HCP;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html.HCStrong;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.layout.CLayout;
import com.phloc.webbasics.app.layout.ILayoutAreaContentProvider;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;
import com.phloc.webbasics.app.page.IWebPage;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.app.page.system.SystemPageNotFound;
import com.phloc.webdemoapp.ui.CDemoAppCSS;

/**
 * The viewport renderer (menu + content area)
 * 
 * @author Philip Helger
 */
public final class RendererConfig implements ILayoutAreaContentProvider
{
  @Nonnull
  private static IHCNode _getHeaderContent (final Locale aDisplayLocale)
  {
    final ISimpleURL aLinkToStartPage = LinkUtils.getLinkToMenuItem (ApplicationMenuTree.getInstance ()
                                                                                        .getTree ()
                                                                                        .getDefaultMenuItemID ());

    final BootstrapNavbar aNavbar = new BootstrapNavbar (EBootstrapNavbarType.STATIC_TOP, true, aDisplayLocale);
    aNavbar.addBrand (HCNodeList.create (HCSpan.create ("DemoApp").addClass (CDemoAppCSS.CSS_CLASS_LOGO1),
                                         HCSpan.create (" Administration").addClass (CDemoAppCSS.CSS_CLASS_LOGO2)),
                      aLinkToStartPage);

    final IUser aUser = LoggedInUserManager.getInstance ().getCurrentUser ();
    aNavbar.addText (EBootstrapNavbarPosition.COLLAPSIBLE_RIGHT,
                     HCP.create ("Logged in as ").addChild (HCStrong.create (aUser == null ? "guest"
                                                                                          : aUser.getDisplayName ())));

    final BootstrapNav aNav = new BootstrapNav ();
    aNav.addItem (EWebBasicsText.LOGIN_LOGOUT.getDisplayText (aDisplayLocale), LinkUtils.getURLWithContext ("/logout"));
    aNavbar.addNav (EBootstrapNavbarPosition.COLLAPSIBLE_RIGHT, aNav);
    return aNavbar;
  }

  @Nonnull
  public static IHCElement <?> getMenuContent (@Nonnull final Locale aDisplayLocale)
  {
    final IHCElement <?> ret = BootstrapMenuItemRenderer.createSideBarMenu (ApplicationMenuTree.getInstance ()
                                                                                               .getTree (),
                                                                            aDisplayLocale);
    return ret;
  }

  @Nonnull
  private static IHCNode _getMainContent (@Nonnull final LayoutExecutionContext aLEC)
  {
    // Get the requested menu item
    final IMenuItemPage aSelectedMenuItem = ApplicationRequestManager.getInstance ().getRequestMenuItem ();

    // Resolve the page of the selected menu item (if found)
    IWebPage aDisplayPage = SystemPageNotFound.getInstance ();
    if (aSelectedMenuItem != null)
    {
      // Only if we have display rights!
      if (aSelectedMenuItem.matchesDisplayFilter ())
        aDisplayPage = (IWebPage) aSelectedMenuItem.getPage ();
      else
      {
        // No rights -> goto start page
        aDisplayPage = (IWebPage) ApplicationRequestManager.getInstance ()
                                                           .getMenuTree ()
                                                           .getDefaultMenuItem ()
                                                           .getPage ();
      }
    }

    final WebPageExecutionContext aWPEC = new WebPageExecutionContext (aLEC, aDisplayPage);

    // Build page content: header + content
    final HCNodeList aPageContainer = new HCNodeList ();
    final String sHeaderText = aDisplayPage.getHeaderText (aWPEC);
    if (StringHelper.hasText (sHeaderText))
      aPageContainer.addChild (new BootstrapPageHeader ().addChild (HCH1.create (sHeaderText)));
    // Main fill content
    aDisplayPage.getContent (aWPEC);
    // Add result
    aPageContainer.addChild (aWPEC.getNodeList ());
    return aPageContainer;
  }

  @Nonnull
  public IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC)
  {
    final BootstrapContainer aOuterContainer = new BootstrapContainer ();

    // Header
    {
      aOuterContainer.addChild (_getHeaderContent (aLEC.getDisplayLocale ()));
    }

    // Breadcrumbs
    {
      final BootstrapBreadcrumbs aBreadcrumbs = BootstrapBreadcrumbsProvider.createBreadcrumbs (ApplicationMenuTree.getInstance ()
                                                                                                                   .getTree (),
                                                                                                aLEC.getDisplayLocale ());
      aBreadcrumbs.addClass (CBootstrapCSS.HIDDEN_XS);
      aOuterContainer.addChild (aBreadcrumbs);
    }

    // Content
    {
      final BootstrapRow aRow = aOuterContainer.addAndReturnChild (new BootstrapRow ());
      final HCDiv aCol1 = aRow.createColumn (3);
      final HCDiv aCol2 = aRow.createColumn (9);

      // left
      // We need a wrapper span for easy AJAX content replacement
      aCol1.addChild (HCSpan.create (getMenuContent (aLEC.getDisplayLocale ())).setID (CLayout.LAYOUT_AREAID_MENU));
      aCol1.addChild (new HCDiv ().setID (CLayout.LAYOUT_AREAID_SPECIAL));

      // content
      aCol2.addChild (_getMainContent (aLEC));
    }

    return aOuterContainer;
  }
}
