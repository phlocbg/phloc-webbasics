package com.phloc.webctrls.slider.options;

import com.phloc.commons.id.IHasSimpleIntID;

/**
 * The way to fill image in slide
 * 
 * @author Boris Gregorcic
 */
public enum EFillMode implements IHasSimpleIntID
{
  // stretch (default)
  STRETCH (0),
  // keep aspect ratio and put all inside slide
  CONTAIN (1),
  // keep aspect ratio and cover whole slide
  COVER (2),
  // actual size
  ACTUAL (4),
  // contain for large image, actual size for small image
  CONTAIN_LARGE (5);

  private int m_nID;

  private EFillMode (final int nID)
  {
    this.m_nID = nID;
  }

  @Override
  public int getID ()
  {
    return this.m_nID;
  }
}
