package com.phloc.webctrls.facebook.xfbml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.id.IHasID;

public enum EFBLikeAction implements IHasID <String>
{
  LIKE ("like"),
  RECOMMEND ("recommend");

  private String m_sID;

  private EFBLikeAction (@Nonnull final String sID)
  {
    this.m_sID = sID;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  @Nullable
  public static EFBLikeAction getFromID (@Nullable final String sID)
  {
    for (final EFBLikeAction eEntry : EFBLikeAction.values ())
    {
      if (eEntry.getID ().equals (sID))
      {
        return eEntry;
      }
    }
    return null;
  }
}
