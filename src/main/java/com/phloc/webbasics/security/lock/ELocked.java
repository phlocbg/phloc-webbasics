package com.phloc.webbasics.security.lock;

import javax.annotation.Nonnull;

public enum ELocked
{
  LOCKED,
  NOT_LOCKED;

  public boolean isLocked ()
  {
    return this == LOCKED;
  }

  public boolean isNotLocked ()
  {
    return this == NOT_LOCKED;
  }

  @Nonnull
  public static ELocked valueOf (final boolean bLocked)
  {
    return bLocked ? LOCKED : NOT_LOCKED;
  }
}
