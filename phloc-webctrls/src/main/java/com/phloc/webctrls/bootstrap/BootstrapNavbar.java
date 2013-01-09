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
package com.phloc.webctrls.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCButton;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCP;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCTextNode;

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
      final HCButton aToggle = m_aContainer.addAndReturnChild (new HCButton ());
      aToggle.addClasses (CBootstrapCSS.BTN, CBootstrapCSS.BTN_NAVBAR);
      aToggle.setCustomAttr ("data-toggle", "collapse");
      aToggle.setCustomAttr ("data-target", "." + CBootstrapCSS.NAV_COLLAPSE.getCSSClass ());
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
    }

    // Create the collapsable container
    m_aCollapse = m_aContainer.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.NAV_COLLAPSE));
  }

  private void _insert (final boolean bCollapsible, @Nullable final IHCNode aNode)
  {
    if (bCollapsible)
      m_aCollapse.addChild (aNode);
    else
      // before collapse
      m_aContainer.addChild (m_aContainer.getChildCount () - 1, aNode);
  }

  @Nonnull
  public BootstrapNavbar addBrand (final boolean bCollapsible,
                                   @Nonnull final String sBrand,
                                   @Nonnull final ISimpleURL aHomeLink)
  {
    return addBrand (bCollapsible, new HCTextNode (sBrand), aHomeLink);
  }

  @Nonnull
  public BootstrapNavbar addBrand (final boolean bCollapsible,
                                   @Nonnull final IHCNode aBrand,
                                   @Nonnull final ISimpleURL aHomeLink)
  {
    _insert (bCollapsible, new HCA (aHomeLink).addChild (aBrand).addClass (CBootstrapCSS.BRAND));
    return this;
  }

  @Nonnull
  public BootstrapNavbar addNav (final boolean bCollapsible, @Nullable final BootstrapNav aNav)
  {
    _insert (bCollapsible, aNav);
    return this;
  }

  @Nonnull
  public BootstrapNavbar addBreadcrumb (final boolean bCollapsible, @Nullable final BootstrapBreadcrumb aBreadCrumb)
  {
    _insert (bCollapsible, aBreadCrumb);
    return this;
  }

  @Nonnull
  public BootstrapNavbar addTextContent (final boolean bCollapsible, @Nonnull final String sText)
  {
    return addTextContent (bCollapsible, new HCP ().addChild (sText));
  }

  @Nonnull
  public BootstrapNavbar addTextContent (final boolean bCollapsible, @Nonnull final IHCElement <?> aText)
  {
    aText.addClasses (CBootstrapCSS.NAVBAR_TEXT, CBootstrapCSS.PULL_RIGHT);
    _insert (bCollapsible, aText);
    return this;
  }
}
