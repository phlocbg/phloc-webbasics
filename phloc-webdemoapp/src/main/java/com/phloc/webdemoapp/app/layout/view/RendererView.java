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
import com.phloc.webctrls.bootstrap.BootstrapPageHeader;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;
import com.phloc.webctrls.bootstrap.ext.BootstrapMenuItemRendererWellHorz;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.bootstrap3.base.Bootstrap3Container;
import com.phloc.webctrls.bootstrap3.breadcrumbs.Bootstrap3Breadcrumbs;
import com.phloc.webctrls.bootstrap3.breadcrumbs.Bootstrap3BreadcrumbsProvider;
import com.phloc.webctrls.bootstrap3.dropdown.Bootstrap3DropdownMenu;
import com.phloc.webctrls.bootstrap3.ext.Bootstrap3MenuItemRenderer;
import com.phloc.webctrls.bootstrap3.grid.Bootstrap3Row;
import com.phloc.webctrls.bootstrap3.grid.EBootstrap3GridMD;
import com.phloc.webctrls.bootstrap3.nav.Bootstrap3Nav;
import com.phloc.webctrls.bootstrap3.navbar.Bootstrap3Navbar;
import com.phloc.webctrls.bootstrap3.navbar.EBootstrap3NavbarPosition;
import com.phloc.webctrls.bootstrap3.navbar.EBootstrap3NavbarType;
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
    ApplicationMenuTree.getInstance ().iterateAllMenuObjects (new INonThrowingRunnableWithParameter <IMenuObject> ()
    {
      public void run (@Nonnull final IMenuObject aCurrentObject)
      {
        if (aCurrentObject.containsFlag (CDemoAppMenuView.FLAG_FOOTER))
          m_aFooterObjects.add (aCurrentObject);
      }
    });
  }

  private static void _addLoginLogout (@Nonnull final Bootstrap3Navbar aNavbar, @Nonnull final Locale aDisplayLocale)
  {
    final IUser aUser = LoggedInUserManager.getInstance ().getCurrentUser ();
    if (aUser != null)
    {
      aNavbar.addText (EBootstrap3NavbarPosition.COLLAPSIBLE_RIGHT,
                       HCP.create ("Logged in as ").addChild (HCStrong.create (aUser.getDisplayName ())));

      final Bootstrap3Nav aNav = new Bootstrap3Nav ();
      aNav.addItem (EWebBasicsText.LOGIN_LOGOUT.getDisplayText (aDisplayLocale),
                    LinkUtils.getURLWithContext ("/logout"));
      aNavbar.addNav (EBootstrap3NavbarPosition.COLLAPSIBLE_DEFAULT, aNav);
    }
    else
    {
      final Bootstrap3Nav aNav = new Bootstrap3Nav ();
      final Bootstrap3DropdownMenu aDropDown = aNav.addDropdownMenu ("Login");
      {
        final HCDiv aDiv = new HCDiv ().addStyle (CCSSProperties.PADDING.newValue ("10px"))
                                       .addStyle (CCSSProperties.WIDTH.newValue ("250px"));
        aDiv.addChild (DemoAppAccessUI.createViewLoginForm (aDisplayLocale, null, false)
                                      .addClass (CBootstrapCSS.NAVBAR_FORM));
        aDropDown.addItem (aDiv);
      }
      aNavbar.addNav (EBootstrap3NavbarPosition.FIXED, aNav);
    }
  }

  @Nonnull
  private static IHCNode _getNavbar (final Locale aDisplayLocale)
  {
    final ISimpleURL aLinkToStartPage = LinkUtils.getLinkToMenuItem (ApplicationMenuTree.getInstance ()
                                                                                        .getDefaultMenuItemID ());

    final Bootstrap3Navbar aNavbar = new Bootstrap3Navbar (EBootstrap3NavbarType.STATIC_TOP, true, aDisplayLocale);
    aNavbar.addBrand (HCSpan.create ("DemoApp").addClass (CDemoAppCSS.CSS_CLASS_LOGO1), aLinkToStartPage);

    _addLoginLogout (aNavbar, aDisplayLocale);
    return aNavbar;
  }

  @Nonnull
  public static IHCNode getMenuContent (@Nonnull final Locale aDisplayLocale)
  {
    // Main menu
    final IMenuTree aMenuTree = ApplicationMenuTree.getInstance ();
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
    final IHCElement <?> aMenu = Bootstrap3MenuItemRenderer.createSideBarMenu (aMenuTree, aCallback, aDisplayLocale);

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
    final Bootstrap3Container aOuterContainer = new Bootstrap3Container ();

    // Header
    {
      aOuterContainer.addChild (_getNavbar (aLEC.getDisplayLocale ()));
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

    // Footer
    {
      final HCDiv aDiv = new HCDiv ().addClass (CBootstrap3CSS.CONTAINER).setID (CLayout.LAYOUT_AREAID_FOOTER);

      aDiv.addChild (HCP.create ("Demo web application for the phloc OSS-stack"));
      aDiv.addChild (HCP.create ("Created by phloc-systems"));

      final BootstrapMenuItemRendererWellHorz aRenderer = new BootstrapMenuItemRendererWellHorz (aLEC.getDisplayLocale ());
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
