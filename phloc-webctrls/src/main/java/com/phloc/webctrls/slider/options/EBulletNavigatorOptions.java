package com.phloc.webctrls.slider.options;


public enum EBulletNavigatorOptions
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
   * 0 None, 1 act by click, 2 act by mouse hover, 3 both, default value is 1
   * (default:1)
   */
  ACTION_MODE ("$ActionMode", true), //$NON-NLS-1$
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
   * Specify lanes to arrange items (default:1)
   */
  LANES ("$Lanes", true), //$NON-NLS-1$
  /**
   * Horizontal space between each item in pixel (default:0)
   */
  SPACING_X ("$SpacingX", true), //$NON-NLS-1$
  /**
   * Vertical space between each item in pixel (default:0)
   */
  SPACING_Y ("$SpacingY", true), //$NON-NLS-1$
  /**
   * The orientation of the navigator, 1 horizontal, 2 vertical (default:1)
   */
  ORIENTATION ("$Orientation", true), //$NON-NLS-1$
  /**
   * Scales bullet navigator or not while slider scale (default:true)
   */
  SCALE ("$Scale", true); //$NON-NLS-1$

  private String m_sID;
  private boolean m_bOptional;

  private EBulletNavigatorOptions (final String sID, final boolean bOptional)
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
