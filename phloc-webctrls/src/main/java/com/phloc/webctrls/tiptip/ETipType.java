package com.phloc.webctrls.tiptip;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.css.ICSSClassProvider;

public enum ETipType implements ICSSClassProvider
{
  INFO ("tip_info"),
  ERROR ("tip_error");

  private final String m_sCSSClass;

  private ETipType (@Nonnull @Nonempty final String sCSSClass)
  {
    m_sCSSClass = sCSSClass;
  }

  @Nonnull
  @Nonempty
  public String getCSSClass ()
  {
    return m_sCSSClass;
  }
}
