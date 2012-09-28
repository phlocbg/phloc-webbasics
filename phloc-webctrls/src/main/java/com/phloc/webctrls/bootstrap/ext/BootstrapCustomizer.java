package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnull;

import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.customize.HCDefaultCustomizer;
import com.phloc.webctrls.bootstrap.BootstrapDropDownMenu;

public class BootstrapCustomizer extends HCDefaultCustomizer
{
  @Override
  public void customizeHCElement (@Nonnull final IHCNodeWithChildren <?> aParentElement,
                                  @Nonnull final IHCElement <?> aElement,
                                  @Nonnull final EHTMLVersion eHTMLVersion)
  {
    super.customizeHCElement (aParentElement, aElement, eHTMLVersion);

    if (aElement instanceof BootstrapDropDownMenu)
    {
      EBootstrapWorkarounds.IPAD_DROPDOWN_FIX.appendIfApplicable (aParentElement);
    }
  }
}
