package com.phloc.webbasics.ui.bootstrap;

import javax.annotation.Nonnull;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.AbstractWrappedHCNode;

/**
 * Navbar
 * 
 * @author philip
 */
public class BootstrapNavbar extends AbstractWrappedHCNode
{
  private final HCDiv m_aDiv = new HCDiv ();
  private final HCDiv m_aContainer;
  private final HCDiv m_aCollapse;

  public BootstrapNavbar ()
  {
    this (false);
  }

  public BootstrapNavbar (final boolean bFixedAtTop)
  {
    m_aDiv.addClass (CBootstrapCSS.NAVBAR);
    if (bFixedAtTop)
      m_aDiv.addClass (CBootstrapCSS.NAVBAR_FIXED_TOP);
    final HCDiv aInner = m_aDiv.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.NAVBAR_INNER));
    m_aContainer = aInner.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTAINER_FLUID));

    // Responsive toggle
    {
      final HCA aToggle = m_aContainer.addAndReturnChild (new HCA ());
      aToggle.addClasses (CBootstrapCSS.BTN, CBootstrapCSS.BTN_NAVBAR);
      aToggle.setCustomAttr ("data-toggle", "collapse");
      aToggle.setCustomAttr ("data-target", "." + CBootstrapCSS.NAV_COLLAPSE.getCSSClass ());
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
      aToggle.addChild (new HCSpan ().addClass (CBootstrapCSS.ICON_BAR));
    }

    m_aCollapse = m_aContainer.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.NAV_COLLAPSE));
  }

  @Nonnull
  public BootstrapNavbar setBrand (@Nonnull final String sBrand, @Nonnull final ISimpleURL aHomeLink)
  {
    // Remove old brand (if any)
    final int nIndex = 1;
    if (m_aContainer.getChildAtIndex (nIndex) instanceof HCA)
      m_aContainer.removeChild (nIndex);
    m_aContainer.addChild (nIndex, new HCA (aHomeLink).addChild (sBrand).addClass (CBootstrapCSS.BRAND));
    return this;
  }

  @Nonnull
  public BootstrapNavbar setNav (@Nonnull final BootstrapNav aNav)
  {
    m_aCollapse.removeAllChildren ();
    m_aCollapse.addChild (aNav);
    return this;
  }

  @Override
  protected IHCNode getContainedHCNode ()
  {
    return m_aDiv;
  }
}
