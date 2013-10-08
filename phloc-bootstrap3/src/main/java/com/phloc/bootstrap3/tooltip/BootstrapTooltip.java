package com.phloc.bootstrap3.tooltip;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.EBootstrapIcon;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.EHTMLElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.html.js.builder.jquery.JQuerySelector;

public class BootstrapTooltip
{
  public static final boolean DEFAULT_ANIMATION = true;
  public static final boolean DEFAULT_HTML = false;
  public static final EBootstrapTooltipPosition DEFAULT_PLACEMENT = EBootstrapTooltipPosition.TOP;
  public static final boolean DEFAULT_PLACEMENT_AUTO = false;
  public static final Set <EBootstrapTooltipTrigger> DEFAULT_TRIGGER = ContainerHelper.newUnmodifiableSortedSet (EBootstrapTooltipTrigger.HOVER,
                                                                                                                 EBootstrapTooltipTrigger.FOCUS);

  private final IJQuerySelector m_aSelector;
  private boolean m_bAnimation = DEFAULT_ANIMATION;
  private boolean m_bHTML = DEFAULT_HTML;
  private EBootstrapTooltipPosition m_ePlacement = DEFAULT_PLACEMENT;
  private boolean m_bPlacementAuto = DEFAULT_PLACEMENT_AUTO;
  private JSAnonymousFunction m_aPlacementFunc;
  private String m_sSelector;
  private String m_sTitle;
  private JSAnonymousFunction m_aTitleFunc;
  private Set <EBootstrapTooltipTrigger> m_aTrigger = DEFAULT_TRIGGER;
  private int m_nShowDelay = 0;
  private int m_nHideDelay = 0;
  private IJQuerySelector m_aContainer;

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

  /**
   * @param aFunction
   *        Callback function with 3 parameters:
   *        <code>(this, $tip[0], this.$element[0])</code>
   * @return this
   */
  @Nonnull
  public BootstrapTooltip setPlacement (@Nonnull final JSAnonymousFunction aFunction)
  {
    if (aFunction == null)
      throw new NullPointerException ("Function");
    m_ePlacement = null;
    m_bPlacementAuto = DEFAULT_PLACEMENT_AUTO;
    m_aPlacementFunc = aFunction;
    return this;
  }

  @Nullable
  public String getSelector ()
  {
    return m_sSelector;
  }

  @Nonnull
  public BootstrapTooltip setSelector (@Nullable final String sSelector)
  {
    m_sSelector = sSelector;
    return this;
  }

  @Nullable
  public String getTitleString ()
  {
    return m_sTitle;
  }

  @Nullable
  public JSAnonymousFunction getTitleFunction ()
  {
    return m_aTitleFunc;
  }

  @Nonnull
  public BootstrapTooltip setTitle (@Nullable final String sTitle)
  {
    m_sTitle = sTitle;
    m_aTitleFunc = null;
    return this;
  }

  @Nonnull
  public BootstrapTooltip setTitle (@Nullable final IHCNode aTitle)
  {
    setHTML (true);
    m_sTitle = aTitle == null ? null : HCSettings.getAsHTMLStringWithoutNamespaces (aTitle);
    m_aTitleFunc = null;
    return this;
  }

  /**
   * @param aFunction
   *        Callback function with 1 parameter: <code>(this.$element[0])</code>
   * @return this
   */
  @Nonnull
  public BootstrapTooltip setTitle (@Nullable final JSAnonymousFunction aFunction)
  {
    m_sTitle = null;
    m_aTitleFunc = aFunction;
    return this;
  }

  @Nullable
  @ReturnsMutableCopy
  public List <EBootstrapTooltipTrigger> getTrigger ()
  {
    return ContainerHelper.newList (m_aTrigger);
  }

  @Nonnull
  public BootstrapTooltip setTrigger (@Nullable final EBootstrapTooltipTrigger... aTrigger)
  {
    // Avoid duplicates!
    m_aTrigger = ContainerHelper.newSortedSet (aTrigger);
    return this;
  }

  @Nonnull
  public BootstrapTooltip setTrigger (@Nullable final Collection <EBootstrapTooltipTrigger> aTrigger)
  {
    // Avoid duplicates!
    m_aTrigger = ContainerHelper.newSortedSet (aTrigger);
    return this;
  }

  @Nonnegative
  public int getShowDelay ()
  {
    return m_nShowDelay;
  }

  @Nonnegative
  public int getHideDelay ()
  {
    return m_nHideDelay;
  }

  @Nonnull
  public BootstrapTooltip setDelay (@Nonnegative final int nDelay)
  {
    return setDelay (nDelay, nDelay);
  }

  @Nonnull
  public BootstrapTooltip setDelay (@Nonnegative final int nShowDelay, @Nonnegative final int nHideDelay)
  {
    if (nShowDelay < 0)
      throw new IllegalArgumentException ("showDelay: " + nShowDelay);
    if (nHideDelay < 0)
      throw new IllegalArgumentException ("hideDelay: " + nHideDelay);
    m_nShowDelay = nShowDelay;
    m_nHideDelay = nHideDelay;
    return this;
  }

  @Nullable
  public IJQuerySelector getContainer ()
  {
    return m_aContainer;
  }

  @Nonnull
  public BootstrapTooltip setContainer (@Nullable final EHTMLElement eContainer)
  {
    return setContainer (JQuerySelector.element (eContainer));
  }

  @Nonnull
  public BootstrapTooltip setContainer (@Nullable final IJQuerySelector aContainer)
  {
    m_aContainer = aContainer;
    return this;
  }

  @Nonnull
  public JSAssocArray getJSOptions ()
  {
    final JSAssocArray aOptions = new JSAssocArray ();
    if (m_bAnimation != DEFAULT_ANIMATION)
      aOptions.add ("animation", m_bAnimation);
    if (m_bHTML != DEFAULT_HTML)
      aOptions.add ("html", m_bHTML);
    if (m_ePlacement != null)
    {
      if (m_ePlacement != DEFAULT_PLACEMENT || m_bPlacementAuto != DEFAULT_PLACEMENT_AUTO)
        aOptions.add ("placement", m_ePlacement.getValue () + (m_bPlacementAuto ? " auto" : ""));
    }
    else
      aOptions.add ("placement", m_aPlacementFunc);
    if (StringHelper.hasText (m_sSelector))
      aOptions.add ("selector", m_sSelector);
    if (StringHelper.hasText (m_sTitle))
      aOptions.add ("title", m_sTitle);
    else
      if (m_aTitleFunc != null)
        aOptions.add ("title", m_aTitleFunc);
    if (ContainerHelper.isNotEmpty (m_aTrigger) && !DEFAULT_TRIGGER.equals (m_aTrigger))
    {
      final StringBuilder aSB = new StringBuilder ();
      for (final EBootstrapTooltipTrigger eTrigger : m_aTrigger)
      {
        if (aSB.length () > 0)
          aSB.append (' ');
        aSB.append (eTrigger.getValue ());
      }
      aOptions.add ("trigger", aSB.toString ());
    }
    if (m_nShowDelay > 0 || m_nHideDelay > 0)
    {
      if (m_nShowDelay == m_nHideDelay)
        aOptions.add ("delay", m_nShowDelay);
      else
        aOptions.add ("delay", new JSAssocArray ().add ("show", m_nShowDelay).add ("hide", m_nHideDelay));
    }
    if (m_aContainer != null)
      aOptions.add ("container", m_aContainer.getJSCode ());
    return aOptions;
  }

  @Nonnull
  public JSInvocation jsAttach ()
  {
    return m_aSelector.invoke ().invoke ("tooltip").arg (getJSOptions ());
  }

  @Nonnull
  public JSInvocation jsShow ()
  {
    return m_aSelector.invoke ().invoke ("tooltip").arg ("show");
  }

  @Nonnull
  public JSInvocation jsHide ()
  {
    return m_aSelector.invoke ().invoke ("tooltip").arg ("hide");
  }

  @Nonnull
  public JSInvocation jsToggle ()
  {
    return m_aSelector.invoke ().invoke ("tooltip").arg ("toggle");
  }

  @Nonnull
  public JSInvocation jsDestroy ()
  {
    return m_aSelector.invoke ().invoke ("tooltip").arg ("destroy");
  }

  @Nonnull
  public static IHCNode createSimpleTooltip (@Nonnull final String sTitle)
  {
    final String sID = GlobalIDFactory.getNewStringID ();
    final HCSpan aSpan = new HCSpan ().setID (sID);
    aSpan.addChild (EBootstrapIcon.QUESTION_SIGN.getAsNode ());
    final BootstrapTooltip aTooltip = new BootstrapTooltip (JQuerySelector.id (sID)).setTitle (sTitle);
    final IHCNode aScript = new HCScriptOnDocumentReady (aTooltip.jsAttach ());
    return HCNodeList.create (aSpan, aScript);
  }

  @Nonnull
  public static IHCNode createSimpleTooltip (@Nonnull final IHCNode aTitle)
  {
    final String sID = GlobalIDFactory.getNewStringID ();
    final HCSpan aSpan = new HCSpan ().setID (sID);
    aSpan.addChild (EBootstrapIcon.QUESTION_SIGN.getAsNode ());
    final BootstrapTooltip aTooltip = new BootstrapTooltip (JQuerySelector.id (sID)).setTitle (aTitle);
    final IHCNode aScript = new HCScriptOnDocumentReady (aTooltip.jsAttach ());
    return HCNodeList.create (aSpan, aScript);
  }
}
