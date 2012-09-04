/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webctrls.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCP;
import com.phloc.html.hc.html.HCSpan;

/**
 * Bootstrap Navbar
 * 
 * @author philip
 */
public class BootstrapNavbar extends AbstractHCDiv <BootstrapNavbar>
{
  private final HCDiv m_aContainer;
  private final HCDiv m_aCollapse;

  public BootstrapNavbar (@Nonnull final EBootstrapNavBarType eType, final boolean bAddResponsiveToggle)
  {
    addClasses (CBootstrapCSS.NAVBAR, eType);
    final HCDiv aInner = addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.NAVBAR_INNER));
    m_aContainer = aInner.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTAINER_FLUID));

    if (bAddResponsiveToggle)
    {
      // Responsive toggle
      final BootstrapButton aToggle = m_aContainer.addAndReturnChild (new BootstrapButton ());
      aToggle.addClass (CBootstrapCSS.BTN_NAVBAR);
      aToggle.setCustomAttr ("data-toggle", "collapse");
      aToggle.setCustomAttr ("data-target", "." + CBootstrapCSS.NAV_COLLAPSE.getCSSClass ());
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
    }

    // Create the collapsable container
    m_aCollapse = m_aContainer.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.NAV_COLLAPSE));
  }

  @Nonnull
  public BootstrapNavbar addBrand (@Nonnull final String sBrand, @Nonnull final ISimpleURL aHomeLink)
  {
    // Remove old brand (if any)
    m_aContainer.addChild (new HCA (aHomeLink).addChild (sBrand).addClass (CBootstrapCSS.BRAND));
    return this;
  }

  @Nonnull
  public BootstrapNavbar addNav (@Nullable final BootstrapNav aNav)
  {
    m_aContainer.addChild (aNav);
    return this;
  }

  @Nonnull
  public BootstrapNavbar addBreadcrumb (@Nullable final BootstrapBreadcrumb aBreadCrumb)
  {
    m_aContainer.addChild (aBreadCrumb);
    return this;
  }

  @Nonnull
  public BootstrapNavbar addTextContent (@Nonnull final String sText)
  {
    return addTextContent (new HCP (sText));
  }

  @Nonnull
  public BootstrapNavbar addTextContent (@Nonnull final IHCElement <?> aText)
  {
    aText.addClasses (CBootstrapCSS.NAVBAR_TEXT, CBootstrapCSS.PULL_RIGHT);
    m_aCollapse.addChild (aText);
    return this;
  }
}
