package com.phloc.bootstrap3.tooltip;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.IJQuerySelector;

public class BootstrapTooltip
{
  private final IJQuerySelector m_aSelector;
  private boolean m_bAnimation = true;
  private boolean m_bHTML = false;
  private EBootstrapTooltipPosition m_ePlacement = EBootstrapTooltipPosition.TOP;
  private boolean m_bPlacementAuto = false;
  private JSAnonymousFunction m_aPlacementFunc;

  public BootstrapTooltip (@Nonnull final IJQuerySelector aSelector)
  {
    if (aSelector == null)
      throw new NullPointerException ("selector");
    m_aSelector = aSelector;
  }

  public boolean isAnimation ()
  {
    return m_bAnimation;
  }

  @Nonnull
  public BootstrapTooltip setAnimation (final boolean bAnimation)
  {
    m_bAnimation = bAnimation;
    return this;
  }

  public boolean isHTML ()
  {
    return m_bHTML;
  }

  @Nonnull
  public BootstrapTooltip setHTML (final boolean bHTML)
  {
    m_bHTML = bHTML;
    return this;
  }

  @Nullable
  public EBootstrapTooltipPosition getPlacementPosition ()
  {
    return m_ePlacement;
  }

  public boolean isPlacementAuto ()
  {
    return m_bPlacementAuto;
  }

  @Nullable
  public JSAnonymousFunction getPlacementFunction ()
  {
    return m_aPlacementFunc;
  }

  @Nonnull
  public BootstrapTooltip setPlacement (@Nonnull final EBootstrapTooltipPosition ePosition, final boolean bAutoAlign)
  {
    if (ePosition == null)
      throw new NullPointerException ("Position");
    m_ePlacement = ePosition;
    m_bPlacementAuto = bAutoAlign;
    m_aPlacementFunc = null;
    return this;
  }

  @Nonnull
  public BootstrapTooltip setPlacement (@Nonnull final JSAnonymousFunction aFunction)
  {
    if (aFunction == null)
      throw new NullPointerException ("Function");
    m_ePlacement = null;
    m_bPlacementAuto = false;
    m_aPlacementFunc = aFunction;
    return this;
  }

  @Nonnull
  public JSInvocation invoke ()
  {
    final JSAssocArray aOptions = new JSAssocArray ();
    aOptions.add ("animation", m_bAnimation);
    aOptions.add ("html", m_bAnimation);
    if (m_ePlacement != null)
      aOptions.add ("placement", m_ePlacement.getValue () + (m_bPlacementAuto ? " auto" : ""));
    else
      aOptions.add ("placement", m_aPlacementFunc);
    return m_aSelector.invoke ().invoke ("tooltip").arg (aOptions);
  }
}
