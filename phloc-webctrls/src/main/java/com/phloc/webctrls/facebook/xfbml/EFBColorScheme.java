package com.phloc.webctrls.facebook.xfbml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.id.IHasID;

public enum EFBColorScheme implements IHasID <String>
{
  LIGHT ("light"), // default
  DARK ("dark");

  private String m_sID;

  private EFBColorScheme (@Nonnull final String sID)
  {
    this.m_sID = sID;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  @Nullable
  public static EFBColorScheme getFromID (@Nullable final String sID)
  {
    for (final EFBColorScheme eEntry : EFBColorScheme.values ())
    {
      if (eEntry.getID ().equals (sID))
      {
        return eEntry;
      }
    }
    return null;
  }
}
