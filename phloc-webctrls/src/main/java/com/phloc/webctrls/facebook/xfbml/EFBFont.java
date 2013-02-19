package com.phloc.webctrls.facebook.xfbml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;

public enum EFBFont implements IHasID <String>
{
  ARIAL ("arial"),
  LUCIDA ("lucida grande"),
  SEGOE ("segoe ui"),
  TAHOMA ("tahoma"),
  TREBUCHET ("trebuchet ms"),
  VERDANA ("verdana");

  private final String m_sID;

  private EFBFont (@Nonnull @Nonempty final String sID)
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
  public static EFBFont getFromID (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EFBFont.class, sID);
  }
}
