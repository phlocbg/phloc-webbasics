package com.phloc.webctrls.slider.options;

public enum ECaptionSliderOptions
{
  /**
   * Class to create instance to animate caption (default: $JssorCaptionSlider$)
   */
  CLASS ("$Class", false), //$NON-NLS-1$
  /**
   * An array of caption transitions to play caption
   */
  TRANSITIONS ("$Transitions", false), //$NON-NLS-1$
  /**
   * How captions play in, 0 None (no play), 1 Chain (goes after main slide), 3
   * Chain Flatten (goes after main slide and flatten all caption animations)
   * (default:1)
   */
  PLAY_IN_MODE ("$PlayInMode", true), //$NON-NLS-1$
  /**
   * How captions play out, 0 None (no play), 1 Chain (goes before main slide),
   * 3 Chain Flatten (goes before main slide and flatten all caption animations)
   * (default:1)
   */
  PLAY_OUT_MODE ("$PlayOutMode", true); //$NON-NLS-1$

  private String m_sID;
  private boolean m_bOptional;

  private ECaptionSliderOptions (final String sID, final boolean bOptional)
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
