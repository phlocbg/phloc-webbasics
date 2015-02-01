package com.phloc.webctrls.slider.skin;

public enum EArrowSkin
{
  S01 ("01", 45, 45), //$NON-NLS-1$
  S02 ("02", 55, 55), //$NON-NLS-1$
  S03 ("03", 55, 55), //$NON-NLS-1$
  S04 ("04", 28, 40), //$NON-NLS-1$
  S05 ("05", 40, 40), //$NON-NLS-1$
  S06 ("06", 45, 45), //$NON-NLS-1$
  S07 ("07", 50, 50), //$NON-NLS-1$
  S08 ("08", 50, 50), //$NON-NLS-1$
  S09 ("09", 50, 50), //$NON-NLS-1$
  S10 ("10", 28, 40), //$NON-NLS-1$
  S11 ("11", 37, 37), //$NON-NLS-1$
  S12 ("12", 30, 46), //$NON-NLS-1$
  S13 ("13", 40, 50), //$NON-NLS-1$
  S14 ("14", 30, 50), //$NON-NLS-1$
  S15 ("15", 20, 38), //$NON-NLS-1$
  S16 ("16", 22, 36), //$NON-NLS-1$
  S18 ("18", 29, 29), //$NON-NLS-1$
  S19 ("19", 50, 50), //$NON-NLS-1$
  S20 ("20", 55, 55), //$NON-NLS-1$
  S21 ("21", 55, 55); //$NON-NLS-1$

  private String m_sID;
  private int m_nWidth;
  private int m_nHeight;

  private EArrowSkin (final String sID, final int nWidth, final int nHeight)
  {
    this.m_sID = sID;
    this.m_nWidth = nWidth;
    this.m_nHeight = nHeight;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  public int getWidth ()
  {
    return this.m_nWidth;
  }

  public int getHeight ()
  {
    return this.m_nHeight;
  }
}
