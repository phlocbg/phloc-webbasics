package com.phloc.webctrls.bootstrap.ext;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCLI;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;

/**
 * A special menu item renderer for the footer area, where the items are
 * displayed horizontally
 * 
 * @author philip
 */
public class BootstrapMenuItemRendererHorz extends BootstrapMenuItemRenderer
{
  public BootstrapMenuItemRendererHorz (@Nonnull final Locale aContentLocale)
  {
    super (aContentLocale);
  }

  @Override
  @Nonnull
  public IHCNode renderSeparator (@Nonnull final IMenuSeparator aSeparator)
  {
    return new HCLI ().addChild ("·").addClass (CBootstrapCSS.MUTED);
  }
}
