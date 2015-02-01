package com.phloc.webctrls.slider.options;

public enum EArrowNavigatorOptions
{
  /**
   * Class to create navigator instance (default: $JssorBulletNavigator$)
   */
  CLASS ("$Class", false), //$NON-NLS-1$
  /**
   * 0 Never, 1 Mouse Over, 2 Always (default:2)
   */
  CHANCE_TO_SHOW ("$ChanceToShow", false), //$NON-NLS-1$
  /**
   * Auto center navigator in parent container, 0 None, 1 Horizontal, 2
   * Vertical, 3 Both (default:0)
   */
  AUTO_CENTER ("$AutoCenter", true), //$NON-NLS-1$
  /**
   * Steps to go for each navigation request (default:1)
   */
  STEPS ("$Steps", true), //$NON-NLS-1$
  /**
   * Scales bullet navigator or not while slider scale (default:true)
   */
  SCALE ("$Scale", true); //$NON-NLS-1$

  private String m_sID;
  private boolean m_bOptional;

  private EArrowNavigatorOptions (final String sID, final boolean bOptional)
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
