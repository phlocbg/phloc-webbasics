package com.phloc.webctrls.facebook.xfbml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;

public enum EFBLikeAction implements IHasID <String>
{
  LIKE ("like"),
  RECOMMEND ("recommend");

  private final String m_sID;

  private EFBLikeAction (@Nonnull @Nonempty final String sID)
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
  public static EFBLikeAction getFromID (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EFBLikeAction.class, sID);
  }
}
