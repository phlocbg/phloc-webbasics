package com.phloc.webctrls.slider.options;

import com.phloc.json.IJSONConvertible;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;

public abstract class AbstractSliderOptions implements IJSONConvertible
{
  protected final IJSONObject m_aOptions = new JSONObject ();

  @Override
  public IJSONObject getAsJSON ()
  {
    return this.m_aOptions;
  }
}
