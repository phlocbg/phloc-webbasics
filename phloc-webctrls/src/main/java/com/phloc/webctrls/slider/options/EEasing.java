package com.phloc.webctrls.slider.options;

import com.phloc.commons.id.IHasID;

public enum EEasing implements IHasID <String>
{
  EASE_LINEAR ("$EaseLinear"), //$NON-NLS-1$
  EASE_GO_BACK ("$EaseGoBack"), //$NON-NLS-1$
  EASE_SWING ("$EaseSwing"), //$NON-NLS-1$
  EASE_IN_QUAD ("$EaseInQuad"), //$NON-NLS-1$
  EASE_OUT_QUAD ("$EaseOutQuad"), //$NON-NLS-1$
  EASE_IN_OUT_QUAD ("$EaseInOutQuad"), //$NON-NLS-1$
  EASE_IN_CUBIC ("$EaseInCubic"), //$NON-NLS-1$
  EASE_OUT_CUBIC ("$EaseOutCubic"), //$NON-NLS-1$
  EASE_IN_OUT_CUBIC ("$EaseInOutCubic"), //$NON-NLS-1$
  EASE_IN_QUART ("$EaseInQuart"), //$NON-NLS-1$
  EASE_OUT_QUAR ("$EaseOutQuar"), //$NON-NLS-1$
  EASE_IN_OUT_QUART ("$EaseInOutQuart"), //$NON-NLS-1$
  EASE_IN_QUINT ("$EaseInQuint"), //$NON-NLS-1$
  EASE_OUT_QUINT ("$EaseOutQuint"), //$NON-NLS-1$
  EASE_IN_OUT_QUINT ("$EaseInOutQuint"), //$NON-NLS-1$
  EASE_IN_SINE ("$EaseInSine"), //$NON-NLS-1$
  EASE_OUT_SINE ("$EaseOutSine"), //$NON-NLS-1$
  EASE_IN_OUT_SINE ("$EaseInOutSine"), //$NON-NLS-1$
  EASE_IN_EXPO ("$EaseInExpo"), //$NON-NLS-1$
  EASE_OUT_EXPO ("$EaseOutExpo"), //$NON-NLS-1$
  EASE_IN_OUT_EXPO ("$EaseInOutExpo"), //$NON-NLS-1$
  EASE_IN_CIRC ("$EaseInCirc"), //$NON-NLS-1$
  EASE_OUT_CIRC ("$EaseOutCirc"), //$NON-NLS-1$
  EASE_IN_OUT_CIRC ("$EaseInOutCirc"), //$NON-NLS-1$
  EASE_IN_ELASTIC ("$EaseInElastic"), //$NON-NLS-1$
  EASE_OUT_ELASTIC ("$EaseOutElastic"), //$NON-NLS-1$
  EASE_IN_OUT_ELASTIC ("$EaseInOutElastic"), //$NON-NLS-1$
  EASE_IN_BACK ("$EaseInBack"), //$NON-NLS-1$
  EASE_OUT_BACK ("$EaseOutBack"), //$NON-NLS-1$
  EASE_IN_OUT_BACK ("$EaseInOutBack"), //$NON-NLS-1$
  EASE_IN_BOUNCE ("$EaseInBounce"), //$NON-NLS-1$
  EASE_OUT_BOUNCE ("$EaseOutBounce"), //$NON-NLS-1$
  EASE_IN_OUT_BOUNCE ("$EaseInOutBounce"), //$NON-NLS-1$
  EASE_IN_WAVE ("$EaseInWave"), //$NON-NLS-1$
  EASE_OUT_WAVE ("$EaseOutWave"), //$NON-NLS-1$
  EASE_OUT_JUMP ("$EaseOutJump"), //$NON-NLS-1$
  EASE_IN_JUMP ("$EaseInJump"); //$NON-NLS-1$

  private static final String PREFIX_EASING = "$JssorEasing$."; //$NON-NLS-1$
  private String m_sID;

  private EEasing (final String sID)
  {
    this.m_sID = sID;
  }

  @Override
  public String getID ()
  {
    return PREFIX_EASING + this.m_sID;
  }
}
