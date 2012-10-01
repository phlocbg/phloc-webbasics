package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnull;

import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.IHCBaseNode;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.customize.HCDefaultCustomizer;
import com.phloc.webctrls.bootstrap.BootstrapDropDownMenu;

public class BootstrapCustomizer extends HCDefaultCustomizer
{
  @Override
  public void customizeNode (@Nonnull final IHCNodeWithChildren <?> aParentElement,
                             @Nonnull final IHCBaseNode aNode,
                             @Nonnull final EHTMLVersion eHTMLVersion)
  {
    super.customizeNode (aParentElement, aNode, eHTMLVersion);

    if (aNode instanceof BootstrapDropDownMenu)
    {
      EBootstrapWorkarounds.IPAD_DROPDOWN_FIX.appendIfApplicable (aParentElement);
    }
  }
}
