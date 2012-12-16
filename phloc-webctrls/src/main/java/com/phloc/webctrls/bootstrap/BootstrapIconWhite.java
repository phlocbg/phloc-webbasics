package com.phloc.webctrls.bootstrap;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCI;
import com.phloc.webctrls.custom.IIcon;

public class BootstrapIconWhite implements IIcon
{
  private final String m_sCSSClass;

  public BootstrapIconWhite (@Nonnull @Nonempty final String sCSSClass)
  {
    if (StringHelper.hasNoText (sCSSClass))
      throw new IllegalArgumentException ("CSSClass");
    m_sCSSClass = sCSSClass;
  }

  @Nonnull
  @Nonempty
  public String getCSSClass ()
  {
    return m_sCSSClass;
  }

  @Nonnull
  public IHCNode getAsNode ()
  {
    return new HCI ().addClasses (this, EBootstrapIcon.CSS_CLASS_ICON_WHITE);
  }

  @Nonnull
  public <T extends IHCElement <?>> T applyToNode (@Nonnull final T aElement)
  {
    aElement.addClasses (this, EBootstrapIcon.CSS_CLASS_ICON_WHITE);
    return aElement;
  }
}
