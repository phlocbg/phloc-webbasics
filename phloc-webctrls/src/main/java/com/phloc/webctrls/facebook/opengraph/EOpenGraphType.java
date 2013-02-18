package com.phloc.webctrls.facebook.opengraph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.id.IHasID;

public enum EOpenGraphType implements IHasID <String>
{
  TITLE ("og:title"),
  TYPE ("og:type"),
  URL ("og:url"),
  IMAGE ("og:image"),
  SITE_NAME ("og:site_name"),
  DESCRIPTION ("og:description");

  private String m_sID;

  private EOpenGraphType (@Nonnull final String sID)
  {
    this.m_sID = sID;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  @Nullable
  public static EOpenGraphType getFromID (@Nullable final String sID)
  {
    for (final EOpenGraphType eEntry : EOpenGraphType.values ())
    {
      if (eEntry.getID ().equals (sID))
      {
        return eEntry;
      }
    }
    return null;
  }
}
