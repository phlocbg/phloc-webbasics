package com.phloc.bootstrap3.tooltip;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

public enum EBootstrapTooltipTrigger
{
  CLICK ("click"),
  HOVER ("hover"),
  FOCUS ("focus"),
  MANUAL ("manual");

  private final String m_sValue;

  private EBootstrapTooltipTrigger (@Nonnull @Nonempty final String sValue)
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
