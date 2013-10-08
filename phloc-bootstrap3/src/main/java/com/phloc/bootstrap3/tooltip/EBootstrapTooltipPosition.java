package com.phloc.bootstrap3.tooltip;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

public enum EBootstrapTooltipPosition
{
  TOP ("top"),
  BOTTOM ("bottom"),
  LEFT ("left"),
  RIGHT ("right");

  private final String m_sValue;

  private EBootstrapTooltipPosition (@Nonnull @Nonempty final String sValue)
  {
    m_sValue = sValue;
  }

  @Nonnull
  @Nonempty
  public String getValue ()
  {
    return m_sValue;
  }
}
