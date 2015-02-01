package com.phloc.webctrls.slider.options;

import com.phloc.commons.string.StringHelper;

public class BulletNavigatorOptions extends AbstractSliderOptions
{
  private static final String DEFAULT_CLASS = "$JssorBulletNavigator$"; //$NON-NLS-1$

  public BulletNavigatorOptions ()
  {
    setClass (DEFAULT_CLASS);
    setChanceToShow (2);
  }

  /**
   * Class to create navigator instance (default: $JssorBulletNavigator$)
   */
  public BulletNavigatorOptions setClass (final String sClass)
  {
    if (StringHelper.hasText (sClass))
    {
      this.m_aOptions.setKeywordProperty (EBulletNavigatorOptions.CLASS.getID (), sClass);
    }
    return this;
  }

  /**
   * 0 Never, 1 Mouse Over, 2 Always (default:2)
   */
  public BulletNavigatorOptions setChanceToShow (final int nShowType)
  {
    this.m_aOptions.setIntegerProperty (EBulletNavigatorOptions.CHANCE_TO_SHOW.getID (), nShowType);
    return this;
  }

  /**
   * 0 None, 1 act by click, 2 act by mouse hover, 3 both, default value is 1
   * (default:1)
   */
  public BulletNavigatorOptions setActionMode (final int nActionMode)
  {
    this.m_aOptions.setIntegerProperty (EBulletNavigatorOptions.ACTION_MODE.getID (), nActionMode);
    return this;
  }

  /**
   * Auto center navigator in parent container, 0 None, 1 Horizontal, 2
   * Vertical, 3 Both (default:0)
   */
  public BulletNavigatorOptions setAutoCenter (final int nAutoCenter)
  {
    this.m_aOptions.setIntegerProperty (EBulletNavigatorOptions.AUTO_CENTER.getID (), nAutoCenter);
    return this;
  }

  /**
   * Steps to go for each navigation request (default:1)
   */
  public BulletNavigatorOptions setSteps (final int nSteps)
  {
    this.m_aOptions.setIntegerProperty (EBulletNavigatorOptions.STEPS.getID (), nSteps);
    return this;
  }

  /**
   * Specify lanes to arrange items (default:1)
   */
  public BulletNavigatorOptions setLanes (final int nLanes)
  {
    this.m_aOptions.setIntegerProperty (EBulletNavigatorOptions.LANES.getID (), nLanes);
    return this;
  }

  /**
   * Horizontal space between each item in pixel (default:0)
   */
  public BulletNavigatorOptions setSpacingX (final int nSpacingX)
  {
    this.m_aOptions.setIntegerProperty (EBulletNavigatorOptions.SPACING_X.getID (), nSpacingX);
    return this;
  }

  /**
   * Vertical space between each item in pixel (default:0)
   */
  public BulletNavigatorOptions setSpacingY (final int nSpacingY)
  {
    this.m_aOptions.setIntegerProperty (EBulletNavigatorOptions.SPACING_Y.getID (), nSpacingY);
    return this;
  }

  /**
   * The orientation of the navigator, 1 horizontal, 2 vertical (default:1)
   */
  public BulletNavigatorOptions setOrientation (final int nOrientation)
  {
    this.m_aOptions.setIntegerProperty (EBulletNavigatorOptions.ORIENTATION.getID (), nOrientation);
    return this;
  }

  /**
   * Scales bullet navigator or not while slider scale (default:true)
   */
  public BulletNavigatorOptions setScale (final boolean bScale)
  {
    this.m_aOptions.setBooleanProperty (EBulletNavigatorOptions.SCALE.getID (), bScale);
    return this;
  }
}
