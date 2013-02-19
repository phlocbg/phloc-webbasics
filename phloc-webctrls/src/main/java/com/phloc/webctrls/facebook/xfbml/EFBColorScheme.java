package com.phloc.webctrls.facebook.xfbml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;

public enum EFBColorScheme implements IHasID <String>
{
  LIGHT ("light"), // default
  DARK ("dark");

  private final String m_sID;

  private EFBColorScheme (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nullable
  public static EFBColorScheme getFromID (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EFBColorScheme.class, sID);
  }
}
