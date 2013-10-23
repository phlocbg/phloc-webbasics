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
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.appbasics.app.menu.MenuItemDeterminatorCallback;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.base.BootstrapContainer;
import com.phloc.bootstrap3.breadcrumbs.BootstrapBreadcrumbs;
import com.phloc.bootstrap3.breadcrumbs.BootstrapBreadcrumbsProvider;
import com.phloc.bootstrap3.dropdown.BootstrapDropdownMenu;
import com.phloc.bootstrap3.ext.BootstrapMenuItemRenderer;
import com.phloc.bootstrap3.ext.BootstrapMenuItemRendererHorz;
import com.phloc.bootstrap3.grid.BootstrapRow;
import com.phloc.bootstrap3.grid.EBootstrapGridMD;
import com.phloc.bootstrap3.nav.BootstrapNav;
import com.phloc.bootstrap3.navbar.BootstrapNavbar;
import com.phloc.bootstrap3.navbar.EBootstrapNavbarPosition;
import com.phloc.bootstrap3.navbar.EBootstrapNavbarType;
import com.phloc.bootstrap3.pageheader.BootstrapPageHeader;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.css.property.CCSSProperties;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCH1;
import com.phloc.html.hc.html.HCP;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html.HCStrong;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.layout.CLayout;
import com.phloc.webbasics.app.layout.ILayoutAreaContentProvider;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;
import com.phloc.webbasics.app.page.IWebPage;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webdemoapp.app.menu.view.CDemoAppMenuView;
import com.phloc.webdemoapp.ui.CDemoAppCSS;
import com.phloc.webdemoapp.ui.DemoAppAccessUI;

/**
 * The viewport renderer (menu + content area)
 * 
 * @author Philip Helger
 */
public final class RendererView implements ILayoutAreaContentProvider
{
  private static final ICSSClassProvider CSS_CLASS_FOOTER_LINKS = DefaultCSSClassProvider.create ("footer-links");

  private final List <IMenuObject> m_aFooterObjects;

  public RendererView ()
  {
    m_aFooterObjects = new ArrayList <IMenuObject> ();
    ApplicationMenuTree.getInstance ()
                       .getTree ()
                       .iterateAllMenuObjects (new INonThrowingRunnableWithParameter <IMenuObject> ()
                       {
                         public void run (@Nonnull final IMenuObject aCurrentObject)
                         {
                           if (aCurrentObject.containsFlag (CDemoAppMenuView.FLAG_FOOTER))
                             m_aFooterObjects.add (aCurrentObject);
                         }
                       });
  }

  private static void _addLoginLogout (@Nonnull final BootstrapNavbar aNavbar, @Nonnull final Locale aDisplayLocale)
  {
    final IUser aUser = LoggedInUserManager.getInstance ().getCurrentUser ();
    if (aUser != null)
    {
      final BootstrapNav aNav = new BootstrapNav ();
      aNav.addItem (EWebBasicsText.LOGIN_LOGOUT.getDisplayText (aDisplayLocale),
                    LinkUtils.getURLWithContext ("/logout"));
      aNavbar.addNav (EBootstrapNavbarPosition.COLLAPSIBLE_RIGHT, aNav);

      aNavbar.addText (EBootstrapNavbarPosition.COLLAPSIBLE_RIGHT,
                       HCP.create ("Logged in as ").addChild (HCStrong.create (aUser.getDisplayName ())));
    }
    else
    {
      final BootstrapNav aNav = new BootstrapNav ();
      final BootstrapDropdownMenu aDropDown = aNav.addDropdownMenu ("Login");
      {
        final HCDiv aDiv = new HCDiv ().addStyle (CCSSProperties.PADDING.newValue ("10px"))
                                       .addStyle (CCSSProperties.WIDTH.newValue ("250px"));
        aDiv.addChild (DemoAppAccessUI.createViewLoginForm (aDisplayLocale, null, false)
                                      .addClass (CBootstrapCSS.NAVBAR_FORM));
        aDropDown.addItem (aDiv);
      }
      aNavbar.addNav (EBootstrapNavbarPosition.FIXED, aNav);
    }
  }

  @Nonnull
  private static IHCNode _getNavbar (final Locale aDisplayLocale)
  {
    final ISimpleURL aLinkToStartPage = LinkUtils.getLinkToMenuItem (ApplicationMenuTree.getInstance ()
                                                                                        .getTree ()
                                                                                        .getDefaultMenuItemID ());

    final BootstrapNavbar aNavbar = new BootstrapNavbar (EBootstrapNavbarType.STATIC_TOP, true, aDisplayLocale);
    aNavbar.addBrand (HCSpan.create ("DemoApp").addClass (CDemoAppCSS.CSS_CLASS_LOGO1), aLinkToStartPage);

    _addLoginLogout (aNavbar, aDisplayLocale);
    return aNavbar;
  }

  @Nonnull
  public static IHCNode getMenuContent (@Nonnull final Locale aDisplayLocale)
  {
    // Main menu
    final IMenuTree aMenuTree = ApplicationMenuTree.getInstance ().getTree ();
    final MenuItemDeterminatorCallback aCallback = new MenuItemDeterminatorCallback (aMenuTree)
    {
      @Override
      protected boolean isMenuItemValidToBeDisplayed (@Nonnull final IMenuObject aMenuObj)
      {
        // Don't show items that belong to the footer
        if (aMenuObj.containsFlag (CDemoAppMenuView.FLAG_FOOTER))
          return false;

        // Use default code
        return super.isMenuItemValidToBeDisplayed (aMenuObj);
      }
    };
    final IHCElement <?> aMenu = BootstrapMenuItemRenderer.createSideBarMenu (aMenuTree, aCallback, aDisplayLocale);

    return aMenu;
  }

  @Nonnull
  private static IHCNode _getMainContent (@Nonnull final LayoutExecutionContext aLEC)
  {
    // Get the requested menu item
    final IMenuItemPage aSelectedMenuItem = ApplicationRequestManager.getInstance ().getRequestMenuItem ();

    // Resolve the page of the selected menu item (if found)
    IWebPage aDisplayPage;
    if (aSelectedMenuItem.matchesDisplayFilter ())
    {
      // Only if we have display rights!
      aDisplayPage = (IWebPage) aSelectedMenuItem.getPage ();
    }
    else
    {
      // No rights -> goto start page
      aDisplayPage = (IWebPage) ApplicationRequestManager.getInstance ()
                                                         .getMenuTree ()
                                                         .getDefaultMenuItem ()
                                                         .getPage ();
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
      aOuterContainer.addChild (_getNavbar (aLEC.getDisplayLocale ()));
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
      final HCDiv aCol1 = aRow.createColumn (EBootstrapGridMD.MD_3);
      final HCDiv aCol2 = aRow.createColumn (EBootstrapGridMD.MD_9);

      // left
      // We need a wrapper span for easy AJAX content replacement
      aCol1.addChild (HCSpan.create (getMenuContent (aLEC.getDisplayLocale ())).setID (CLayout.LAYOUT_AREAID_MENU));
      aCol1.addChild (new HCDiv ().setID (CLayout.LAYOUT_AREAID_SPECIAL));

      // content
      aCol2.addChild (_getMainContent (aLEC));
    }

    // Footer
    {
      final HCDiv aDiv = new HCDiv ().addClass (CBootstrapCSS.CONTAINER).setID (CLayout.LAYOUT_AREAID_FOOTER);

      aDiv.addChild (HCP.create ("Demo web application for the phloc OSS-stack"));
      aDiv.addChild (HCP.create ("Created by phloc-systems"));

      final BootstrapMenuItemRendererHorz aRenderer = new BootstrapMenuItemRendererHorz (aLEC.getDisplayLocale ());
      final HCUL aUL = aDiv.addAndReturnChild (new HCUL ().addClass (CSS_CLASS_FOOTER_LINKS));
      for (final IMenuObject aMenuObj : m_aFooterObjects)
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
