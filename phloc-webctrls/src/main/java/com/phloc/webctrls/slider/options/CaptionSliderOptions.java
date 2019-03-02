package com.phloc.webctrls.slider.options;

import java.util.List;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.json.IJSONObject;

public class CaptionSliderOptions extends AbstractSliderOptions
{
  /**
   * Class to create instance to animate caption (default: $JssorCaptionSlider$)
   */
  public CaptionSliderOptions setClass (final String sClass)
  {
    if (StringHelper.hasText (sClass))
    {
      this.m_aOptions.setKeywordProperty (ECaptionSliderOptions.CLASS.getID (), sClass);
    }
    return this;
  }

  /**
   * An array of caption transitions to play caption
   * 
   * @param aTransitions
   *        transitions
   * @return this for chaining
   */
  public CaptionSliderOptions setTransitions (final List <IJSONObject> aTransitions)
  {
    if (ContainerHelper.isNotEmpty (aTransitions))
    {
      this.m_aOptions.setObjectListProperty (ECaptionSliderOptions.TRANSITIONS.getID (), aTransitions);
    }
    return this;
  }

  /**
   * How captions play in, 0 None (no play), 1 Chain (goes after main slide), 3
   * Chain Flatten (goes after main slide and flatten all caption animations)
   * (default:1)
   * 
   * @param nMode
   *        mode
   * @return this for chaining
   */
  public CaptionSliderOptions setPlayInMode (final int nMode)
  {
    this.m_aOptions.setIntegerProperty (ECaptionSliderOptions.PLAY_IN_MODE.getID (), nMode);
    return this;
  }

  /**
   * How captions play out, 0 None (no play), 1 Chain (goes before main slide),
   * 3 Chain Flatten (goes before main slide and flatten all caption animations)
   * (default:1)
   * 
   * @param nMode
   *        mode
   * @return this for chaining
   */
  public CaptionSliderOptions setPlayOutMode (final int nMode)
  {
    this.m_aOptions.setIntegerProperty (ECaptionSliderOptions.PLAY_OUT_MODE.getID (), nMode);
    return this;
  }
}
