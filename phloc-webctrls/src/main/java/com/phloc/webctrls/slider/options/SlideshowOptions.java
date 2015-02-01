package com.phloc.webctrls.slider.options;

import java.util.List;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.json.IJSONObject;

public class SlideshowOptions extends AbstractSliderOptions
{
  /**
   * Class to create thumbnail navigator instance (default:
   * $JssorThumbnailNavigator$)
   */
  public SlideshowOptions setClass (final String sClass)
  {
    if (StringHelper.hasText (sClass))
    {
      this.m_aOptions.setKeywordProperty (ESlideshowOptions.CLASS.getID (), sClass);
    }
    return this;
  }

  /**
   * An array of slide show transitions to play slide show
   */
  public SlideshowOptions setTransitions (final List <IJSONObject> aTransitions)
  {
    if (ContainerHelper.isNotEmpty (aTransitions))
    {
      this.m_aOptions.setObjectListProperty (ESlideshowOptions.TRANSITIONS.getID (), aTransitions);
    }
    return this;
  }

  /**
   * The way to choose transition to play slide, 1 Sequence, 0 Random
   * (default:1)
   */
  public SlideshowOptions setTransitionsOrder (final int nOrder)
  {
    this.m_aOptions.setIntegerProperty (ESlideshowOptions.TRANSITIONS_ORDER.getID (), nOrder);
    return this;
  }

  /**
   * Whether to bring slide link on top of the slider when slide show is
   * running, default value is false (default:false)
   */
  public SlideshowOptions setShowLink (final boolean bShowLink)
  {
    this.m_aOptions.setBooleanProperty (ESlideshowOptions.SHOW_LINK.getID (), bShowLink);
    return this;
  }
}
