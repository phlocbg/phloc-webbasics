package com.phloc.appbasics.security;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;

/**
 * Generic access enum
 * 
 * @author philip
 */
public enum EAccess implements IHasID <String>
{
  GRANTED ("grant", true),
  DENIED ("deny", false),
  INHERITED ("inherit", CSecurity.NO_RIGHT_SPECIFIED_MEANS_HAS_ACCESS);

  private final String m_sID;
  private final boolean m_bHasAccess;

  private EAccess (@Nonnull @Nonempty final String sID, final boolean bHasAccess)
  {
    m_sID = sID;
    m_bHasAccess = bHasAccess;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  public boolean hasAccess ()
  {
    return m_bHasAccess;
  }

  public boolean isInherited ()
  {
    return this == INHERITED;
  }
}
