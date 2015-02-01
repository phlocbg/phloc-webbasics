package com.phloc.webctrls.slider.options;

public enum ESlideshowOptions
{
  /**
   * Class to create thumbnail navigator instance (default:
   * $JssorThumbnailNavigator$)
   */
  CLASS ("$Class", false), //$NON-NLS-1$
  /**
   * An array of slide show transitions to play slide show
   */
  TRANSITIONS ("$Transitions", false), //$NON-NLS-1$
  /**
   * The way to choose transition to play slide, 1 Sequence, 0 Random
   * (default:1)
   */
  TRANSITIONS_ORDER ("$TransitionsOrder", true), //$NON-NLS-1$
  /**
   * Whether to bring slide link on top of the slider when slide show is
   * running, default value is false (default:false)
   */
  SHOW_LINK ("$ShowLink", true); //$NON-NLS-1$

  private String m_sID;
  private boolean m_bOptional;

  private ESlideshowOptions (final String sID, final boolean bOptional)
  {
    this.m_sID = sID;
    this.m_bOptional = bOptional;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  public boolean isOptional ()
  {
    return this.m_bOptional;
  }
}
