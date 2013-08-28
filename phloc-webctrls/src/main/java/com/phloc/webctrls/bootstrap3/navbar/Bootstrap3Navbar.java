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
package com.phloc.webctrls.bootstrap3.navbar;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCButton;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCP;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html5.HCNav;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.bootstrap3.EBootstrap3Text;

/**
 * Bootstrap Navbar
 * 
 * @author Philip Helger
 */
public class Bootstrap3Navbar extends HCNav
{
  private final HCDiv m_aHeader;
  private final HCDiv m_aContent;

  public Bootstrap3Navbar (@Nonnull final EBootstrap3NavbarType eType,
                           final boolean bCollapsible,
                           @Nonnull final Locale aDisplayLocale)
  {
    addClasses (CBootstrap3CSS.NAVBAR, CBootstrap3CSS.NAVBAR_DEFAULT, eType);
    setRole (EHTMLRole.NAVIGATION);

    final HCDiv aContainer = addAndReturnChild (new HCDiv ().addClass (CBootstrap3CSS.CONTAINER));
    m_aHeader = aContainer.addAndReturnChild (new HCDiv ().addClass (CBootstrap3CSS.NAVBAR_HEADER));

    // Create the main container
    m_aContent = aContainer.addAndReturnChild (new HCDiv ());

    if (bCollapsible)
    {
      // Create a per-instance class for collapsing
      final String sCollapseTarget = "navbar" + GlobalIDFactory.getNewIntID ();

      // Responsive toggle
      final HCButton aToggle = m_aHeader.addAndReturnChild (new HCButton ());
      aToggle.addClass (CBootstrap3CSS.NAVBAR_TOGGLE);
      aToggle.setCustomAttr ("data-toggle", "collapse");
      aToggle.setCustomAttr ("data-target", "#" + sCollapseTarget);
      aToggle.addChild (new HCSpan ().addClass (CBootstrap3CSS.SR_ONLY)
                                     .addChild (EBootstrap3Text.TOOGLE_NAVIGATION.getDisplayText (aDisplayLocale)));
      aToggle.addChild (new HCSpan ().addClass (CBootstrap3CSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrap3CSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrap3CSS.ICON_BAR));

      m_aContent.addClasses (CBootstrap3CSS.COLLAPSE, CBootstrap3CSS.NAVBAR_COLLAPSE).setID (sCollapseTarget);
    }
  }

  @Nonnull
  private Bootstrap3Navbar _insert (final boolean bCollapsible, @Nullable final IHCNode aNode)
  {
    if (bCollapsible)
      m_aContent.addChild (aNode);
    else
      m_aHeader.addChild (aNode);
    return this;
  }

  @Nonnull
  public Bootstrap3Navbar addText (final boolean bCollapsible, @Nonnull final String sText)
  {
    return addText (bCollapsible, new HCP ().addChild (sText));
  }

  @Nonnull
  public Bootstrap3Navbar addText (final boolean bCollapsible, @Nonnull final IHCElementWithChildren <?> aText)
  {
    aText.addClass (CBootstrap3CSS.NAVBAR_TEXT);
    return _insert (bCollapsible, aText);
  }

  @Nonnull
  public Bootstrap3Navbar addButton (final boolean bCollapsible, @Nonnull final IHCElement <?> aButton)
  {
    aButton.addClass (CBootstrap3CSS.NAVBAR_BTN);
    return _insert (bCollapsible, aButton);
  }

  @Nonnull
  public Bootstrap3Navbar addBrand (final boolean bCollapsible,
                                    @Nonnull final String sBrand,
                                    @Nonnull final ISimpleURL aHomeLink)
  {
    return addBrand (bCollapsible, new HCTextNode (sBrand), aHomeLink);
  }

  @Nonnull
  public Bootstrap3Navbar addBrand (final boolean bCollapsible,
                                    @Nonnull final IHCNode aBrand,
                                    @Nonnull final ISimpleURL aHomeLink)
  {
    return _insert (bCollapsible, new HCA (aHomeLink).addChild (aBrand).addClass (CBootstrap3CSS.NAVBAR_BRAND));
  }

  public boolean isInverse ()
  {
    return containsClass (CBootstrap3CSS.NAVBAR_INVERSE);
  }

  @Nonnull
  public Bootstrap3Navbar setInverse (final boolean bInverse)
  {
    if (bInverse)
      addClass (CBootstrap3CSS.NAVBAR_INVERSE);
    else
      removeClass (CBootstrap3CSS.NAVBAR_INVERSE);
    return this;
  }
}
