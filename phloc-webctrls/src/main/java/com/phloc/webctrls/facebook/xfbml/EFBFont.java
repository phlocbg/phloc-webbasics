package com.phloc.webctrls.facebook.xfbml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.id.IHasID;

public enum EFBFont implements IHasID <String>
{
  ARIAL ("arial"),
  LUCIDA ("lucida grande"),
  SEGOE ("segoe ui"),
  TAHOMA ("tahoma"),
  TREBUCHET ("trebuchet ms"),
  VERDANA ("verdana");

  private String m_sID;

  private EFBFont (@Nonnull final String sID)
  {
    this.m_sID = sID;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  @Nullable
  public static EFBFont getFromID (@Nullable final String sID)
  {
    for (final EFBFont eEntry : EFBFont.values ())
    {
      if (eEntry.getID ().equals (sID))
      {
        return eEntry;
      }
    }
    return null;
  }
}
