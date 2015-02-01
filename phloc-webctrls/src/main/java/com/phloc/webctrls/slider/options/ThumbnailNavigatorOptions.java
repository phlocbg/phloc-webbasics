package com.phloc.webctrls.slider.options;

import com.phloc.commons.string.StringHelper;

public class ThumbnailNavigatorOptions extends AbstractSliderOptions
{
  private static final String DEFAULT_CLASS = "$JssorThumbnailNavigator$"; //$NON-NLS-1$

  public ThumbnailNavigatorOptions ()
  {
    setClass (DEFAULT_CLASS);
    setChanceToShow (2);
  }

  /**
   * Class to create navigator instance (default: $JssorThumbnailNavigator$)
   */
  public ThumbnailNavigatorOptions setClass (final String sClass)
  {
    if (StringHelper.hasText (sClass))
    {
      this.m_aOptions.setKeywordProperty (EThumbnailNavigatorOptions.CLASS.getID (), sClass);
    }
    return this;
  }

  /**
   * 0 Never, 1 Mouse Over, 2 Always (default:2)
   */
  public ThumbnailNavigatorOptions setChanceToShow (final int nShowType)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.CHANCE_TO_SHOW.getID (), nShowType);
    return this;
  }

  /**
   * Enable loop(circular) of carousel or not, 0: stop, 1: loop, 2 rewind,
   * default value is 1
   */
  public ThumbnailNavigatorOptions setLoop (final int nLoopType)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.LOOP.getID (), nLoopType);
    return this;
  }

  /**
   * 0 None, 1 act by click, 2 act by mouse hover, 3 both, default value is 1
   * (default:1)
   */
  public ThumbnailNavigatorOptions setActionMode (final int nActionMode)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.ACTION_MODE.getID (), nActionMode);
    return this;
  }

  /**
   * Auto center navigator in parent container, 0 None, 1 Horizontal, 2
   * Vertical, 3 Both (default:0)
   */
  public ThumbnailNavigatorOptions setAutoCenter (final int nAutoCenter)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.AUTO_CENTER.getID (), nAutoCenter);
    return this;
  }

  /**
   * Specify lanes to arrange items (default:1)
   */
  public ThumbnailNavigatorOptions setLanes (final int nLanes)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.LANES.getID (), nLanes);
    return this;
  }

  /**
   * Horizontal space between each item in pixel (default:0)
   */
  public ThumbnailNavigatorOptions setSpacingX (final int nSpacingX)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.SPACING_X.getID (), nSpacingX);
    return this;
  }

  /**
   * Vertical space between each item in pixel (default:0)
   */
  public ThumbnailNavigatorOptions setSpacingY (final int nSpacingY)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.SPACING_Y.getID (), nSpacingY);
    return this;
  }

  /**
   * Number of pieces to display (the slide show would be disabled if the value
   * is set to greater than 1) (default:1)
   */
  public ThumbnailNavigatorOptions setDisplayPieces (final int nPieces)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.DISPLAY_PIECES.getID (), nPieces);
    return this;
  }

  /**
   * The offset position to park slide (this options applies only when slide
   * show disabled) (default:0)
   */
  public ThumbnailNavigatorOptions setParkingPosition (final int nParkingPosition)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.PARKING_POSITION.getID (), nParkingPosition);
    return this;
  }

  /**
   * Disable drag or not (default:false)
   */
  public ThumbnailNavigatorOptions setDisableDrag (final boolean bDisable)
  {
    this.m_aOptions.setBooleanProperty (EThumbnailNavigatorOptions.DISABLE_DRAG.getID (), bDisable);
    return this;
  }

  /**
   * The orientation of the navigator, 1 horizontal, 2 vertical (default:1)
   */
  public ThumbnailNavigatorOptions setOrientation (final int nOrientation)
  {
    this.m_aOptions.setIntegerProperty (EThumbnailNavigatorOptions.ORIENTATION.getID (), nOrientation);
    return this;
  }

  /**
   * Scales bullet navigator or not while slider scale (default:true)
   */
  public ThumbnailNavigatorOptions setScale (final boolean bScale)
  {
    this.m_aOptions.setBooleanProperty (EThumbnailNavigatorOptions.SCALE.getID (), bScale);
    return this;
  }
}
