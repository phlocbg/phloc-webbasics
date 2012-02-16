package com.phloc.webbasics.ui.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.AbstractWrappedHCNode;
import com.phloc.html.hc.impl.HCTextNode;

/**
 * Navigation items
 * 
 * @author philip
 */
public class BootstrapNav extends AbstractWrappedHCNode
{
  protected final HCUL m_aUL = new HCUL ();

  public BootstrapNav ()
  {
    m_aUL.addClass (CBootstrapCSS.NAV);
  }

  @Nonnull
  public BootstrapNav addHeader (@Nullable final String sText, final boolean bActive)
  {
    return addHeader (new HCTextNode (sText), bActive);
  }

  @Nonnull
  public BootstrapNav addHeader (@Nullable final IHCNode aContent, final boolean bActive)
  {
    final HCLI aItem = m_aUL.addAndReturnItem (aContent).addClass (CBootstrapCSS.NAV_HEADER);
    if (bActive)
      aItem.addClass (CBootstrapCSS.ACTIVE);
    return this;
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final String sText, @Nonnull final ISimpleURL aTarget, final boolean bActive)
  {
    // no icon
    return addItem (sText, aTarget, bActive, null);
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final String sText,
                               @Nonnull final ISimpleURL aTarget,
                               final boolean bActive,
                               @Nullable final EBootstrapIcon eIcon)
  {
    return addItem (new HCA (aTarget).addChild (sText), bActive, eIcon);
  }

  @Nonnull
  public BootstrapNav addItem (@Nullable final IHCElementWithChildren <?> aContent,
                               final boolean bActive,
                               @Nullable final EBootstrapIcon eIcon)
  {
    final HCLI aItem = m_aUL.addAndReturnItem (aContent);
    if (bActive)
      aItem.addClass (CBootstrapCSS.ACTIVE);
    if (eIcon != null)
    {
      // Icon is the first child of the content
      aContent.addChild (0, eIcon.getAsNode ());
    }
    return this;
  }

  @Override
  protected final IHCNode getContainedHCNode ()
  {
    return m_aUL;
  }
}
