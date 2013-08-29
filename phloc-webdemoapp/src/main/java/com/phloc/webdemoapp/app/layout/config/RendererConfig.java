package com.phloc.webdemoapp.app.layout.config;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.appbasics.security.user.IUser;
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
import com.phloc.webctrls.bootstrap.BootstrapPageHeader;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.bootstrap3.base.Bootstrap3Container;
import com.phloc.webctrls.bootstrap3.breadcrumbs.Bootstrap3Breadcrumbs;
import com.phloc.webctrls.bootstrap3.breadcrumbs.Bootstrap3BreadcrumbsProvider;
import com.phloc.webctrls.bootstrap3.ext.Bootstrap3MenuItemRenderer;
import com.phloc.webctrls.bootstrap3.grid.Bootstrap3Row;
import com.phloc.webctrls.bootstrap3.grid.EBootstrap3GridMD;
import com.phloc.webctrls.bootstrap3.nav.Bootstrap3Nav;
import com.phloc.webctrls.bootstrap3.navbar.Bootstrap3Navbar;
import com.phloc.webctrls.bootstrap3.navbar.EBootstrap3NavbarType;
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
                                                                                        .getDefaultMenuItemID ());

    final Bootstrap3Navbar aNavbar = new Bootstrap3Navbar (EBootstrap3NavbarType.STATIC_TOP, true, aDisplayLocale);
    aNavbar.addBrand (false, HCNodeList.create (HCSpan.create ("DemoApp").addClass (CDemoAppCSS.CSS_CLASS_LOGO1),
                                                HCSpan.create (" Administration")
                                                      .addClass (CDemoAppCSS.CSS_CLASS_LOGO2)), aLinkToStartPage);

    final Bootstrap3Nav aNav = new Bootstrap3Nav ();

    aNav.addItem (EWebBasicsText.LOGIN_LOGOUT.getDisplayText (aDisplayLocale), LinkUtils.getURLWithContext ("/logout"));

    aNavbar.addNav (false, aNav);

    final IUser aUser = LoggedInUserManager.getInstance ().getCurrentUser ();
    aNavbar.addText (true,
                     HCP.create ("Logged in as ").addChild (HCStrong.create (aUser == null ? "guest"
                                                                                          : aUser.getDisplayName ())));
    return aNavbar;
  }

  @Nonnull
  public static IHCElement <?> getMenuContent (@Nonnull final Locale aDisplayLocale)
  {
    final IHCElement <?> ret = Bootstrap3MenuItemRenderer.createSideBarMenu (ApplicationMenuTree.getInstance (),
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
    final Bootstrap3Container aOuterContainer = new Bootstrap3Container ();

    // Header
    {
      aOuterContainer.addChild (_getHeaderContent (aLEC.getDisplayLocale ()));
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
      final Bootstrap3Row aRow = aOuterContainer.addAndReturnChild (new Bootstrap3Row ());
      final HCDiv aCol1 = aRow.createColumn (EBootstrap3GridMD.MD_3);
      final HCDiv aCol2 = aRow.createColumn (EBootstrap3GridMD.MD_9);

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
