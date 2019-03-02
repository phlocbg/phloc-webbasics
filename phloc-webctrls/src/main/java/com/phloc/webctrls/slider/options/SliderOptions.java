package com.phloc.webctrls.slider.options;

public class SliderOptions extends AbstractSliderOptions
{
  /**
   * The way to fill image in slide, 0 stretch, 1 contain (keep aspect ratio and
   * put all inside slide), 2 cover (keep aspect ratio and cover whole slide), 4
   * actual size, 5 contain for large image, actual size for small image,
   * default value is 0
   * 
   * @param eFillMode
   *        fill mode
   * @return this for chaining
   */
  public SliderOptions setFillMode (final EFillMode eFillMode)
  {
    if (eFillMode != null)
    {
      this.m_aOptions.setIntegerProperty (ESliderOptions.FILL_MODE.getID (), eFillMode.getID ());
    }
    return this;
  }

  /**
   * For image with lazy loading format (&lt;IMG src2="url" .../&gt;), by
   * default it will be loaded only when the slide comes.But an integer value
   * (maybe 1, 2 or 3) indicates that how far of nearby slides should be loaded
   * immediately as well, default value is 1.
   * 
   * @param bLazy
   *        lazy
   * @return this for chaining
   */
  public SliderOptions setLazyLoading (final boolean bLazy)
  {
    this.m_aOptions.setBooleanProperty (ESliderOptions.LAZY_LOADING.getID (), bLazy);
    return this;
  }

  /**
   * Index of slide to display when initialize, default value is 0
   * 
   * @param nIndex
   *        index
   * @return this for chaining
   */
  public SliderOptions setStartIndex (final int nIndex)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.START_INDEX.getID (), nIndex);
    return this;
  }

  /**
   * Whether to auto play, to enable slide show, this option must be set to
   * true.
   * 
   * @param bAutoPlay
   *        auto play
   * @return this for chaining
   */
  public SliderOptions setAutoPlay (final boolean bAutoPlay)
  {
    this.m_aOptions.setBooleanProperty (ESliderOptions.AUTO_PLAY.getID (), bAutoPlay);
    return this;
  }

  /**
   * Enable loop(circular) of carousel or not, 0: stop, 1: loop, 2 rewind,
   * default value is 1
   * 
   * @param nLoopType
   *        loop type
   * @return this for chaining
   */
  public SliderOptions setLoop (final int nLoopType)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.LOOP.getID (), nLoopType);
    return this;
  }

  /**
   * Enable hardware acceleration or not, default value is true
   * 
   * @param bEnabled
   *        enabled
   * @return this for chaining
   */
  public SliderOptions setHardwareAcceleration (final boolean bEnabled)
  {
    this.m_aOptions.setBooleanProperty (ESliderOptions.HWA.getID (), bEnabled);
    return this;
  }

  /**
   * Steps to go for each navigation request (this options applies only when
   * slide show disabled).
   * 
   * @param nSteps
   *        steps
   * @return this for chaining
   */
  public SliderOptions setAutoPlaySteps (final int nSteps)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.AUTO_PLAY_STEPS.getID (), nSteps);
    return this;
  }

  /**
   * Interval (in milliseconds) to go for next slide since the previous stopped
   * if the slider is auto playing
   * 
   * @param nInterval
   *        interval
   * @return this for chaining
   */
  public SliderOptions setAutoPlayInterval (final int nInterval)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.AUTO_PLAY_INTERVAL.getID (), nInterval);
    return this;
  }

  /**
   * Whether to pause when mouse over if a slider is auto playing, 0 no pause, 1
   * pause for desktop, 2 pause for touch device, 3 pause for desktop and touch
   * device, 4 freeze for desktop, 8 freeze for touch device, 12 freeze for
   * desktop and touch device, default value is 1
   * 
   * @param nPauseType
   *        pause type
   * @return this for chaining
   */
  public SliderOptions setPauseOnHover (final int nPauseType)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.PAUSE_ON_HOVER.getID (), nPauseType);
    return this;
  }

  /**
   * Allows keyboard (arrow key) navigation or not (default: false)
   * 
   * @param bEnabled
   *        enabled
   * @return this for chaining
   */
  public SliderOptions setArrowKeyNavigation (final boolean bEnabled)
  {
    this.m_aOptions.setBooleanProperty (ESliderOptions.ARROW_KEY_NAVIGATION.getID (), bEnabled);
    return this;
  }

  /**
   * Specifies default duration for right to left animation in milliseconds
   * 
   * @param nDuration
   *        duration
   * @return this for chaining
   */
  public SliderOptions setSlideDuration (final int nDuration)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.SLIDE_DURATION.getID (), nDuration);
    return this;
  }

  /**
   * Specifies easing for right to left animation
   * (default:$JssorEasing$.$EaseOutQuad)
   * 
   * @param eSlideEasing
   *        slide easing
   * @return this for chaining
   */
  public SliderOptions setSlideEasing (final EEasing eSlideEasing)
  {
    if (eSlideEasing != null)
    {
      this.m_aOptions.setKeywordProperty (ESliderOptions.SLIDE_EASING.getID (), eSlideEasing.getID ());
    }
    return this;
  }

  /**
   * Minimum drag offset to trigger slide (default:20)
   * 
   * @param nOffset
   *        offset
   * @return this for chaining
   */
  public SliderOptions setMinDragOffsetToSlide (final int nOffset)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.MIN_DRAG_OFFSET.getID (), nOffset);
    return this;
  }

  /**
   * Width of every slide in pixels, default value is width of 'slides'
   * container
   * 
   * @param nWidth
   *        width
   * @return this for chaining
   */
  public SliderOptions setSlideWidth (final int nWidth)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.SLIDE_WIDTH.getID (), nWidth);
    return this;
  }

  /**
   * Height of every slide in pixels, default value is height of 'slides'
   * container
   * 
   * @param nHeight
   *        height
   * @return this for chaining
   */
  public SliderOptions setSlideHeight (final int nHeight)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.SLIDE_HEIGHT.getID (), nHeight);
    return this;
  }

  /**
   * Space between each slide in pixels (default:0)
   * 
   * @param nSpacing
   *        spacing
   * @return this for chaining
   */
  public SliderOptions setSlideSpacing (final int nSpacing)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.SLIDE_SPACING.getID (), nSpacing);
    return this;
  }

  /**
   * Number of pieces to display (the slide show would be disabled if the value
   * is set to greater than 1) (default:1)
   * 
   * @param nPieces
   *        pieces
   * @return this for chaining
   */
  public SliderOptions setDisplayPieces (final int nPieces)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.DISPLAY_PIECES.getID (), nPieces);
    return this;
  }

  /**
   * The offset position to park slide (this options applies only when slide
   * show disabled) (default:0)
   * 
   * @param nParkingPosition
   *        parking position
   * @return this for chaining
   */
  public SliderOptions setParkingPosition (final int nParkingPosition)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.PARKING_POSITION.getID (), nParkingPosition);
    return this;
  }

  /**
   * The way (0 parallel, 1 recursive, default value is 1) to search UI
   * components (slides container, loading screen, navigator container, arrow
   * navigator container, thumbnail navigator container etc). (default:1)
   * 
   * @param nUISearchMode
   *        UI search mode
   * @return this for chaining
   */
  public SliderOptions setUISearchMode (final int nUISearchMode)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.UI_SEARCH_MODE.getID (), nUISearchMode);
    return this;
  }

  /**
   * Orientation to play slide (for auto play, navigation), 1 horizontal, 2
   * vertical, 5 horizontal reverse, 6 vertical reverse
   * 
   * @param nPlayOrientation
   *        play orientation
   * @return this for chaining
   */
  public SliderOptions setPlayOrientation (final int nPlayOrientation)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.PLAY_ORIENTATION.getID (), nPlayOrientation);
    return this;
  }

  /**
   * Orientation to drag slide, 0 no drag, 1 horizontal, 2 vertical, 3 either
   * (Note that the $DragOrientation should be the same as $PlayOrientation when
   * $DisplayPieces is greater than 1, or parking position is not 0) (default:1)
   * 
   * @param nDragOrientation
   *        drag orientation
   * @return this for chaining
   */
  public SliderOptions setDragOrientation (final int nDragOrientation)
  {
    this.m_aOptions.setIntegerProperty (ESliderOptions.DRAG_ORIENTATION.getID (), nDragOrientation);
    return this;
  }

  /**
   * Options to specify and enable navigator or not
   * 
   * @param aOptions
   *        options
   * @return this for chaining
   */
  public SliderOptions setBulletNavigatorOptions (final BulletNavigatorOptions aOptions)
  {
    if (aOptions != null)
    {
      this.m_aOptions.setObjectProperty (ESliderOptions.BULLET_NAVIGATOR_OPTIONS.getID (), aOptions.getAsJSON ());
    }
    return this;
  }

  /**
   * Options to specify and enable arrow navigator or not
   * 
   * @param aOptions
   *        options
   * @return this for chaining
   */
  public SliderOptions setArrowNavigatorOptions (final ArrowNavigatorOptions aOptions)
  {
    if (aOptions != null)
    {
      this.m_aOptions.setObjectProperty (ESliderOptions.ARROW_NAVIGATOR_OPTIONS.getID (), aOptions.getAsJSON ());
    }
    return this;
  }

  /**
   * Options to specify and enable thumbnail navigator or not
   * 
   * @param aOptions
   *        options
   * @return this for chaining
   */
  public SliderOptions setThumbnailNavigatorOptions (final ThumbnailNavigatorOptions aOptions)
  {
    if (aOptions != null)
    {
      this.m_aOptions.setObjectProperty (ESliderOptions.THUMBNAIL_NAVIGATOR_OPTIONS.getID (), aOptions.getAsJSON ());
    }
    return this;
  }

  /**
   * Options to specify and enable slide show or not
   * 
   * @param aOptions
   *        options
   * @return this for chaining
   */
  public SliderOptions setSlideshowOptions (final SlideshowOptions aOptions)
  {
    if (aOptions != null)
    {
      this.m_aOptions.setObjectProperty (ESliderOptions.SLIDESHOW_OPTIONS.getID (), aOptions.getAsJSON ());
    }
    return this;
  }

  /**
   * Options which specifies how to animate caption
   * 
   * @param aOptions
   *        options
   * @return this for chaining
   */
  public SliderOptions setCaptionSliderOptions (final CaptionSliderOptions aOptions)
  {
    if (aOptions != null)
    {
      this.m_aOptions.setObjectProperty (ESliderOptions.CAPTION_SLIDER_OPTIONS.getID (), aOptions.getAsJSON ());
    }
    return this;
  }
}
