package com.phloc.webctrls.facebook.opengraph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;

public enum EOpenGraphMetaTag implements IHasID <String>
{
  TITLE ("og:title"),
  TYPE ("og:type"),
  URL ("og:url"),
  IMAGE ("og:image"),
  SITE_NAME ("og:site_name"),
  DESCRIPTION ("og:description");

  private final String m_sID;

  private EOpenGraphMetaTag (@Nonnull @Nonempty final String sID)
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
  public static EOpenGraphMetaTag getFromID (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EOpenGraphMetaTag.class, sID);
  }
}
