package com.phloc.webbasics.ui.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.impl.AbstractHCControl;
import com.phloc.html.hc.impl.AbstractHCElement;

public class BootstrapFormHorizontal extends HCForm
{
  public BootstrapFormHorizontal (@Nonnull final ISimpleURL aAction)
  {
    super (aAction);
    addClass (CBootstrapCSS.FORM_HORIZONTAL);
  }

  @Nonnull
  public BootstrapFormHorizontal addControlGroup (@Nullable final String sLabel,
                                                  @Nullable final AbstractHCControl <?> aCtrl)
  {
    final HCDiv aCtrlGroup = addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROL_GROUP));
    aCtrlGroup.addChild (new HCLabel (sLabel).addClass (CBootstrapCSS.CONTROL_LABEL).setFor (aCtrl.getName ()));
    final HCDiv aControls = aCtrlGroup.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROLS));
    aControls.addChild (aCtrl);
    return this;
  }

  @Nonnull
  public BootstrapFormHorizontal addControlGroup (@Nullable final AbstractHCElement <?>... aCtrls)
  {
    final HCDiv aCtrlGroup = addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROL_GROUP));
    final HCDiv aControls = aCtrlGroup.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.CONTROLS));
    aControls.addChildren (aCtrls);
    return this;
  }
}
