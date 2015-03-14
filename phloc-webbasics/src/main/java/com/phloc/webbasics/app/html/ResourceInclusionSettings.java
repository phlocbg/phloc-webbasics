package com.phloc.webbasics.app.html;

import com.phloc.commons.GlobalDebug;

/**
 * These settings let you influence whether regular or minified versions of CSS
 * and JS resources should be included (by default regular version re only used
 * in debug mode)
 * 
 * @author Boris Gregorcic
 */
public class ResourceInclusionSettings
{
  private static final class SingletonHolder
  {
    /**
     * The singleton instance
     */
    public static final ResourceInclusionSettings INSTANCE = new ResourceInclusionSettings ();
  }

  private boolean m_bUseRegularCSS = GlobalDebug.isDebugMode ();
  private boolean m_bUseRegularJS = GlobalDebug.isDebugMode ();

  /**
   * Ctor for singleton creation
   */
  protected ResourceInclusionSettings ()
  {
    // protected
  }

  /**
   * Ctor
   * 
   * @return the singleton instance
   */
  public static ResourceInclusionSettings getInstance ()
  {
    return SingletonHolder.INSTANCE;
  }

  public boolean isUseRegularCSS ()
  {
    return this.m_bUseRegularCSS;
  }

  public void setUseRegularCSS (final boolean bUseRegular)
  {
    this.m_bUseRegularCSS = bUseRegular;
  }

  public boolean isUseRegularJS ()
  {
    return this.m_bUseRegularJS;
  }

  public void setUseRegularJS (final boolean bUseRegular)
  {
    this.m_bUseRegularJS = bUseRegular;
  }
}
