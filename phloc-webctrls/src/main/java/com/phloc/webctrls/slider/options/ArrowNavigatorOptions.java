package com.phloc.webctrls.slider.options;

import com.phloc.commons.string.StringHelper;

public class ArrowNavigatorOptions extends AbstractSliderOptions
{
  private static final String DEFAULT_CLASS = "$JssorArrowNavigator$"; //$NON-NLS-1$

  public ArrowNavigatorOptions ()
  {
    this (DEFAULT_CLASS, 2);
  }

  /**
   * @param sClass
   *        Class to create the arrow navigator instance (default:
   *        $JssorArrowNavigator$)
   * @param nChangeToShow
   *        0 Never, 1 Mouse Over, 2 Always (default:2)
   */
  public ArrowNavigatorOptions (final String sClass, final int nChangeToShow)
  {
    setClass (sClass);
    setChanceToShow (nChangeToShow);
  }

  /**
   * Class to create navigator instance (default: $JssorBulletNavigator$)
   * 
   * @param sClass
   *        The CSS class
   * @return this for chaining
   */
  public ArrowNavigatorOptions setClass (final String sClass)
  {
    if (StringHelper.hasText (sClass))
    {
      this.m_aOptions.setKeywordProperty (EArrowNavigatorOptions.CLASS.getID (), sClass);
    }
    return this;
  }

  /**
   * 0 Never, 1 Mouse Over, 2 Always (default:2)
   * 
   * @param nShowType
   *        show type
   * @return this for chaining
   */
  public ArrowNavigatorOptions setChanceToShow (final int nShowType)
  {
    this.m_aOptions.setIntegerProperty (EArrowNavigatorOptions.CHANCE_TO_SHOW.getID (), nShowType);
    return this;
  }

  /**
   * Auto center navigator in parent container, 0 None, 1 Horizontal, 2
   * Vertical, 3 Both (default:0)
   * 
   * @param nAutoCenter
   *        auto center
   * @return this for chaining
   */
  public ArrowNavigatorOptions setAutoCenter (final int nAutoCenter)
  {
    this.m_aOptions.setIntegerProperty (EArrowNavigatorOptions.AUTO_CENTER.getID (), nAutoCenter);
    return this;
  }

  /**
   * Steps to go for each navigation request (default:1)
   * 
   * @param nSteps
   *        steps
   * @return this for chaining
   */
  public ArrowNavigatorOptions setSteps (final int nSteps)
  {
    this.m_aOptions.setIntegerProperty (EArrowNavigatorOptions.STEPS.getID (), nSteps);
    return this;
  }

  /**
   * Scales bullet navigator or not while slider scale (default:true)
   * 
   * @param bScale
   *        whether or not to scale
   * @return this for chaining
   */
  public ArrowNavigatorOptions setScale (final boolean bScale)
  {
    this.m_aOptions.setBooleanProperty (EArrowNavigatorOptions.SCALE.getID (), bScale);
    return this;
  }
}
