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
package com.phloc.bootstrap3.navbar;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.EBootstrapText;
import com.phloc.bootstrap3.nav.BootstrapNav;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCButton;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCP;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html5.HCNav;
import com.phloc.html.hc.impl.HCTextNode;

/**
 * Bootstrap Navbar
 * 
 * @author Philip Helger
 */
public class BootstrapNavbar extends HCNav
{
  private final HCDiv m_aHeader;
  private final HCDiv m_aContent;

  public BootstrapNavbar (@Nonnull final EBootstrapNavbarType eType,
                          final boolean bCollapsible,
                          @Nonnull final Locale aDisplayLocale)
  {
    addClasses (CBootstrapCSS.NAVBAR, CBootstrapCSS.NAVBAR_DEFAULT, eType);
    setRole (EHTMLRole.NAVIGATION);

    final HCDiv aContainer = addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTAINER));
    m_aHeader = aContainer.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.NAVBAR_HEADER));

    // Create the main container
    m_aContent = aContainer.addAndReturnChild (new HCDiv ());

    if (bCollapsible)
    {
      // Create a per-instance class for collapsing
      final String sCollapseTarget = "navbar" + GlobalIDFactory.getNewIntID ();

      // Responsive toggle
      final HCButton aToggle = m_aHeader.addAndReturnChild (new HCButton ());
      aToggle.addClass (CBootstrapCSS.NAVBAR_TOGGLE);
      aToggle.setDataAttr ("toggle", "collapse");
      aToggle.setDataAttr ("target", "#" + sCollapseTarget);
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.SR_ONLY)
                                     .addChild (EBootstrapText.TOOGLE_NAVIGATION.getDisplayText (aDisplayLocale)));
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));

      m_aContent.addClasses (CBootstrapCSS.COLLAPSE, CBootstrapCSS.NAVBAR_COLLAPSE).setID (sCollapseTarget);
    }
  }

  @Nonnull
  private BootstrapNavbar _addNode (@Nonnull final EBootstrapNavbarPosition ePos, @Nullable final IHCElement <?> aNode)
  {
    if (aNode != null)
      aNode.addClass (ePos);
    if (ePos.isFixed ())
      m_aHeader.addChild (aNode);
    else
      m_aContent.addChild (aNode);
    return this;
  }

  @Nonnull
  public BootstrapNavbar addForm (@Nonnull final EBootstrapNavbarPosition ePos, @Nonnull final HCForm aForm)
  {
    aForm.addClass (CBootstrapCSS.NAVBAR_FORM);
    return _addNode (ePos, aForm);
  }

  @Nonnull
  public BootstrapNavbar addNav (@Nonnull final EBootstrapNavbarPosition ePos, @Nonnull final BootstrapNav aNav)
  {
    aNav.addClass (CBootstrapCSS.NAVBAR_NAV);
    return _addNode (ePos, aNav);
  }

  @Nonnull
  public BootstrapNavbar addText (@Nonnull final EBootstrapNavbarPosition ePos, @Nonnull final String sText)
  {
    return addText (ePos, new HCP ().addChild (sText));
  }

  @Nonnull
  public BootstrapNavbar addText (@Nonnull final EBootstrapNavbarPosition ePos,
                                  @Nonnull final IHCElementWithChildren <?> aText)
  {
    aText.addClass (CBootstrapCSS.NAVBAR_TEXT);
    return _addNode (ePos, aText);
  }

  @Nonnull
  public BootstrapNavbar addButton (@Nonnull final EBootstrapNavbarPosition ePos, @Nonnull final IHCElement <?> aButton)
  {
    aButton.addClass (CBootstrapCSS.NAVBAR_BTN);
    return _addNode (ePos, aButton);
  }

  @Nonnull
  public BootstrapNavbar addBrand (@Nonnull final String sBrand, @Nonnull final ISimpleURL aHomeLink)
  {
    return addBrand (EBootstrapNavbarPosition.FIXED, new HCTextNode (sBrand), aHomeLink);
  }

  @Nonnull
  public BootstrapNavbar addBrand (@Nonnull final EBootstrapNavbarPosition ePos,
                                   @Nonnull final String sBrand,
                                   @Nonnull final ISimpleURL aHomeLink)
  {
    return addBrand (ePos, new HCTextNode (sBrand), aHomeLink);
  }

  @Nonnull
  public BootstrapNavbar addBrand (@Nonnull final IHCNode aBrand, @Nonnull final ISimpleURL aHomeLink)
  {
    return addBrand (EBootstrapNavbarPosition.FIXED, aBrand, aHomeLink);
  }

  @Nonnull
  public BootstrapNavbar addBrand (@Nonnull final EBootstrapNavbarPosition ePos,
                                   @Nonnull final IHCNode aBrand,
                                   @Nonnull final ISimpleURL aHomeLink)
  {
    return _addNode (ePos, new HCA (aHomeLink).addChild (aBrand).addClass (CBootstrapCSS.NAVBAR_BRAND));
  }

  public boolean isInverse ()
  {
    return containsClass (CBootstrapCSS.NAVBAR_INVERSE);
  }

  @Nonnull
  public BootstrapNavbar setInverse (final boolean bInverse)
  {
    if (bInverse)
      addClass (CBootstrapCSS.NAVBAR_INVERSE);
    else
      removeClass (CBootstrapCSS.NAVBAR_INVERSE);
    return this;
  }
}