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

import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCP;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html.HCStrong;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webctrls.bootstrap.BootstrapNav;
import com.phloc.webctrls.bootstrap.BootstrapNavbar;
import com.phloc.webctrls.bootstrap.EBootstrapNavBarType;
import com.phloc.webdemoapp.ui.CDemoAppCSS;

/**
 * The content provider for the header area.
 * 
 * @author philip
 */
public final class ConfigHeaderProvider
{
  private ConfigHeaderProvider ()
  {}

  @Nonnull
  public static IHCNode getContent (final Locale aDisplayLocale)
  {
    final ISimpleURL aLinkToStartPage = LinkUtils.getLinkToMenuItem (ApplicationMenuTree.getInstance ()
                                                                                        .getDefaultMenuItemID ());

    final BootstrapNavbar aNavbar = new BootstrapNavbar (EBootstrapNavBarType.FIXED_TOP, true);
    aNavbar.addBrand (false, HCNodeList.create (HCSpan.create ("DemoApp").addClass (CDemoAppCSS.CSS_CLASS_LOGO1),
                                                HCSpan.create (" Administration")
                                                      .addClass (CDemoAppCSS.CSS_CLASS_LOGO2)), aLinkToStartPage);

    final BootstrapNav aNav = new BootstrapNav ();

    aNav.addItem (EWebBasicsText.LOGIN_LOGOUT.getDisplayText (aDisplayLocale), LinkUtils.getURLWithContext ("/logout"));

    aNavbar.addNav (false, aNav);

    final IUser aUser = LoggedInUserManager.getInstance ().getCurrentUser ();
    aNavbar.addTextContent (true,
                            HCP.create ("Logged in as ")
                               .addChild (HCStrong.create (aUser == null ? "guest" : aUser.getDisplayName ())));
    return aNavbar;
  }
}
