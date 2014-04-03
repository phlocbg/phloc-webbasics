package com.phloc.appbasics.app.menu;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;

/**
 * Contains predefined window targets. They are also available in HCA_Target of
 * phloc-html, but we don't include this library here....
 * 
 * @author Philip Helger
 */
public enum EMenuItemExternalTarget
{
  /** New window */
  BLANK ("_blank"),
  /** This window */
  SELF ("_self"),
  /** Parent frame */
  PARENT ("_parent"),
  /** Out of frames */
  TOP ("_top");

  public static final EMenuItemExternalTarget DEFAULT = SELF;

  private final String m_sName;

  private EMenuItemExternalTarget (@Nonnull @Nonempty final String sName)
  {
    m_sName = ValueEnforcer.notEmpty (sName, "Name");
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }
}
