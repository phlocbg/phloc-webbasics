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

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.css.property.CCSSProperties;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCP;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html.HCStrong;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;
import com.phloc.webctrls.bootstrap3.dropdown.Bootstrap3DropdownMenu;
import com.phloc.webctrls.bootstrap3.nav.Bootstrap3Nav;
import com.phloc.webctrls.bootstrap3.navbar.Bootstrap3Navbar;
import com.phloc.webctrls.bootstrap3.navbar.EBootstrap3NavbarType;
import com.phloc.webdemoapp.ui.CDemoAppCSS;
import com.phloc.webdemoapp.ui.DemoAppAccessUI;

/**
 * The content provider for the header area.
 * 
 * @author Philip Helger
 */
final class ViewHeaderProvider
{
  private ViewHeaderProvider ()
  {}

  private static void _addLoginLogout (@Nonnull final Bootstrap3Navbar aNavbar, @Nonnull final Locale aDisplayLocale)
  {
    final IUser aUser = LoggedInUserManager.getInstance ().getCurrentUser ();
    if (aUser != null)
    {
      final Bootstrap3Nav aNav = new Bootstrap3Nav ();
      aNav.addItem (EWebBasicsText.LOGIN_LOGOUT.getDisplayText (aDisplayLocale),
                    LinkUtils.getURLWithContext ("/logout"));
      aNavbar.addNav (true, aNav);
      aNavbar.addText (true, HCP.create ("Logged in as ").addChild (HCStrong.create (aUser.getDisplayName ())));
    }
    else
    {
      final Bootstrap3Nav aNav = new Bootstrap3Nav ();
      final Bootstrap3DropdownMenu aDropDown = aNav.addDropdownMenu ("Login");
      {
        final HCDiv aDiv = new HCDiv ().addStyle (CCSSProperties.PADDING.newValue ("15px"));
        aDiv.addChild (DemoAppAccessUI.createViewLoginForm (aDisplayLocale, null, false)
                                      .addClass (CBootstrapCSS.NAVBAR_FORM));
        aDropDown.addItem (aDiv);
      }
      aNavbar.addNav (false, aNav);
    }
  }

  @Nonnull
  public static IHCNode getContent (final Locale aDisplayLocale)
  {
    final ISimpleURL aLinkToStartPage = LinkUtils.getLinkToMenuItem (ApplicationMenuTree.getInstance ()
                                                                                        .getDefaultMenuItemID ());

    final Bootstrap3Navbar aNavbar = new Bootstrap3Navbar (EBootstrap3NavbarType.STATIC_TOP, true, aDisplayLocale);
    aNavbar.addBrand (false, HCSpan.create ("DemoApp").addClass (CDemoAppCSS.CSS_CLASS_LOGO1), aLinkToStartPage);

    _addLoginLogout (aNavbar, aDisplayLocale);
    return aNavbar;
  }
}
