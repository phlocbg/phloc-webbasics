package com.phloc.webctrls.facebook.xfbml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;

public enum EFBLikeLayout implements IHasID <String>
{
  /**
   * displays social text to the right of the button and friends' profile photos
   * below. Minimum width: 225 pixels. Minimum increases by 40px if action is
   * 'recommend' by and increases by 60px if send is 'true'. Default width: 450
   * pixels. Height: 35 pixels (without photos) or 80 pixels (with photos).
   */
  STANDARD ("standard"),

  /**
   * displays the total number of likes to the right of the button. Minimum
   * width: 90 pixels. Default width: 90 pixels. Height: 20 pixels.
   */
  BUTTON_COUNT ("button_count"),

  /**
   * displays the total number of likes above the button. Minimum width: 55
   * pixels. Default width: 55 pixels. Height: 65 pixels.
   */
  BOX_COUNT ("box_count");

  /** Default like layout */
  public static final EFBLikeLayout DEFAULT = STANDARD;

  private final String m_sID;

  private EFBLikeLayout (@Nonnull @Nonempty final String sID)
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
  public static EFBLikeLayout getFromID (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EFBLikeLayout.class, sID);
  }
}
