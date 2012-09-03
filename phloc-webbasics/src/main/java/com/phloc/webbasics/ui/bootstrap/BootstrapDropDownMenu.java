package com.phloc.webbasics.ui.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;

public class BootstrapDropDownMenu extends HCUL
{
  private void _init ()
  {
    addClass (CBootstrapCSS.DROPDOWN_MENU);
  }

  public BootstrapDropDownMenu ()
  {
    super ();
    _init ();
  }

  @Nonnull
  public BootstrapDropDownMenu addMenuItem (@Nonnull final ISimpleURL aURL,
                                            @Nullable final String sLabel,
                                            final boolean bActive)
  {
    final HCLI aLI = addAndReturnItem (new HCA (aURL).addChild (sLabel));
    if (bActive)
      aLI.addClass (CBootstrapCSS.ACTIVE);
    return this;
  }
}
