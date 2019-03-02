package com.phloc.webctrls.slider.options;

import javax.annotation.Nullable;

public enum ESliderOptions
{
 /**
  * The way to fill image in slide, 0 stretch, 1 contain (keep aspect ratio and
  * put all inside slide), 2 cover (keep aspect ratio and cover whole slide), 4
  * actual size, 5 contain for large image, actual size for small image, default
  * value is 0
  */
 FILL_MODE ("$FillMode", true), //$NON-NLS-1$
 /**
  * For image with lazy loading format (&lt;IMG src2="url" .../&gt;), by default
  * it will be loaded only when the slide comes.But an integer value (maybe 1, 2
  * or 3) indicates that how far of nearby slides should be loaded immediately
  * as well, default value is 1.
  */
 LAZY_LOADING ("$LazyLoading", true), //$NON-NLS-1$
 /**
  * Index of slide to display when initialize, default value is 0
  */
 START_INDEX ("$StartIndex", true), //$NON-NLS-1$
 /**
  * Whether to auto play, to enable slide show, this option must be set to true.
  */
 AUTO_PLAY ("$AutoPlay", true), //$NON-NLS-1$
 /**
  * Enable loop(circular) of carousel or not, 0: stop, 1: loop, 2 rewind,
  * default value is 1
  */
 LOOP ("$Loop", true), //$NON-NLS-1$
 /**
  * Enable hardware acceleration or not, default value is true
  */
 HWA ("$HWA", true), //$NON-NLS-1$
 /**
  * Steps to go for each navigation request (this options applies only when
  * slide show disabled).
  */
 AUTO_PLAY_STEPS ("$AutoPlaySteps", true), //$NON-NLS-1$
 /**
  * Interval (in milliseconds) to go for next slide since the previous stopped
  * if the slider is auto playing
  */
 AUTO_PLAY_INTERVAL ("$AutoPlayInterval", true), //$NON-NLS-1$
 /**
  * Whether to pause when mouse over if a slider is auto playing, 0 no pause, 1
  * pause for desktop, 2 pause for touch device, 3 pause for desktop and touch
  * device, 4 freeze for desktop, 8 freeze for touch device, 12 freeze for
  * desktop and touch device, default value is 1
  */
 PAUSE_ON_HOVER ("$PauseOnHover", true), //$NON-NLS-1$
 /**
  * Allows keyboard (arrow key) navigation or not (default: false)
  */
 ARROW_KEY_NAVIGATION ("$ArrowKeyNavigation", true), //$NON-NLS-1$
 /**
  * Specifies default duration for right to left animation in milliseconds
  */
 SLIDE_DURATION ("$SlideDuration", true), //$NON-NLS-1$
 /**
  * Specifies easing for right to left animation
  * (default:$JssorEasing$.$EaseOutQuad)
  */
 SLIDE_EASING ("$SlideEasing", true), //$NON-NLS-1$
 /**
  * Minimum drag offset to trigger slide (default:20)
  */
 MIN_DRAG_OFFSET ("$MinDragOffsetToSlide", true), //$NON-NLS-1$
 /**
  * Width of every slide in pixels, default value is width of 'slides' container
  */
 SLIDE_WIDTH ("$SlideWidth", true), //$NON-NLS-1$
 /**
  * Height of every slide in pixels, default value is height of 'slides'
  * container
  */
 SLIDE_HEIGHT ("$SlideHeight", true), //$NON-NLS-1$
 /**
  * Space between each slide in pixels (default:0)
  */
 SLIDE_SPACING ("$SlideSpacing", true), //$NON-NLS-1$
 /**
  * Number of pieces to display (the slide show would be disabled if the value
  * is set to greater than 1) (default:1)
  */
 DISPLAY_PIECES ("$DisplayPieces", true), //$NON-NLS-1$
 /**
  * The offset position to park slide (this options applies only when slide show
  * disabled) (default:0)
  */
 PARKING_POSITION ("$ParkingPosition", true), //$NON-NLS-1$
 /**
  * The way (0 parallel, 1 recursive, default value is 1) to search UI
  * components (slides container, loading screen, navigator container, arrow
  * navigator container, thumbnail navigator container etc). (default:1)
  */
 UI_SEARCH_MODE ("$UISearchMode", true), //$NON-NLS-1$
 /**
  * Orientation to play slide (for auto play, navigation), 1 horizontal, 2
  * vertical, 5 horizontal reverse, 6 vertical reverse
  */
 PLAY_ORIENTATION ("$PlayOrientation", true), //$NON-NLS-1$
 /**
  * Orientation to drag slide, 0 no drag, 1 horizontal, 2 vertical, 3 either
  * (Note that the $DragOrientation should be the same as $PlayOrientation when
  * $DisplayPieces is greater than 1, or parking position is not 0) (default:1)
  */
 DRAG_ORIENTATION ("$DragOrientation", true), //$NON-NLS-1$
 /**
  * Options to specify and enable navigator or not
  */
 BULLET_NAVIGATOR_OPTIONS ("$BulletNavigatorOptions", true), //$NON-NLS-1$
 /**
  * Options to specify and enable arrow navigator or not
  */
 ARROW_NAVIGATOR_OPTIONS ("$ArrowNavigatorOptions", true), //$NON-NLS-1$
 /**
  * Options to specify and enable thumbnail navigator or not
  */
 THUMBNAIL_NAVIGATOR_OPTIONS ("$ThumbnailNavigatorOptions", true), //$NON-NLS-1$
 /**
  * Options to specify and enable slide show or not
  */
 SLIDESHOW_OPTIONS ("$SlideshowOptions", true), //$NON-NLS-1$
 /**
  * Options which specifies how to animate caption
  */
 CAPTION_SLIDER_OPTIONS ("$CaptionSliderOptions", true); //$NON-NLS-1$

  private String m_sID;
  private boolean m_bOptional;

  private ESliderOptions (final String sID, final boolean bOptional)
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

  /**
   * Tries to resolve the enum entry corresponding to the passed ID
   * 
   * @param sID
   *        ID of the enum entry
   * @return The resolved enum entry, or <code>null</code>
   */
  @Nullable
  public static ESliderOptions getFromID (final String sID)
  {
    for (final ESliderOptions eValue : values ())
    {
      if (eValue.getID ().equals (sID))
      {
        return eValue;
      }
    }
    return null;
  }
}
