package com.phloc.webctrls.slider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSExprDirect;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.JSVar;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webctrls.slider.html.SliderOuterContainer;
import com.phloc.webctrls.slider.html.SliderSlidesContainer;
import com.phloc.webctrls.slider.options.SliderOptions;
import com.phloc.webctrls.slider.skin.EArrowSkin;
import com.phloc.webctrls.slider.skin.EBulletSkin;
import com.phloc.webctrls.slider.skin.EThumbnailSkin;

public class Slider implements IHCNodeBuilder
{
  private static final String JS_CLASS_JSSOR_SLIDER = "$JssorSlider$"; //$NON-NLS-1$
  private static final String JS_VAR_SLIDERS = "g_aSliders"; //$NON-NLS-1$
  private static final String JS_VAR_OPTIONS = "aOptions"; //$NON-NLS-1$
  private final String m_sID;
  private final SliderOuterContainer m_aOuterContainer;
  private final SliderSlidesContainer m_aSlidesContainer;
  private final SliderOptions m_aOptions;
  private final JSPackage m_aJSPackageEvents = new JSPackage ();
  private final JSPackage m_aJSPackageOnReady = new JSPackage ();

  public Slider (@Nonnull final SliderOptions aOptions)
  {
    this (GlobalIDFactory.getNewStringID (), aOptions);
  }

  public Slider (final String sID, @Nonnull final SliderOptions aOptions)
  {
    if (StringHelper.hasNoText (sID))
    {
      throw new IllegalArgumentException ("sID must not be null or empty!"); //$NON-NLS-1$
    }
    this.m_sID = sID;
    this.m_aOuterContainer = new SliderOuterContainer (sID);
    this.m_aSlidesContainer = new SliderSlidesContainer ();
    this.m_aOptions = aOptions;
  }

  public SliderOuterContainer getOuterContainer ()
  {
    return this.m_aOuterContainer;
  }

  public SliderSlidesContainer getSlidesContainer ()
  {
    return this.m_aSlidesContainer;
  }

  public Slider setArrowNavigator (@Nonnull final EArrowSkin eSkin)
  {
    this.m_aOuterContainer.setArrowNavigator (eSkin);
    return this;
  }

  public Slider setArrowContent (@Nullable final IHCNode aContentLeft, @Nullable final IHCNode aContentRight)
  {
    this.m_aOuterContainer.setArrowContent (aContentLeft, aContentRight);
    return this;
  }

  public Slider setBulletNavigator (@Nonnull final EBulletSkin eSkin,
                                    @Nullable final Integer nTop,
                                    @Nullable final Integer nBottom,
                                    @Nullable final Integer nLeft,
                                    @Nullable final Integer nRight)
  {
    this.m_aOuterContainer.setBulletNavigator (eSkin, nTop, nBottom, nLeft, nRight);
    return this;
  }

  public Slider setThumbnailNavigator (@Nonnull final EThumbnailSkin eSkin)
  {
    this.m_aOuterContainer.setThumbnailNavigator (eSkin);
    return this;
  }

  public Slider setWidth (final int nWidth)
  {
    this.m_aOuterContainer.setWidth (nWidth);
    this.m_aSlidesContainer.setWidth (nWidth);
    return this;
  }

  public Slider setHeight (final int nHeight)
  {
    this.m_aOuterContainer.setHeight (nHeight);
    this.m_aSlidesContainer.setHeight (nHeight);
    return this;
  }

  @Override
  public IHCNode build ()
  {
    registerExternalResources ();
    final HCNodeList aResult = new HCNodeList ();

    // initialization script
    final JSPackage aJSPackageGlobal = new JSPackage ();
    final JSPackage aJSPackageOnReady = new JSPackage ();
    aJSPackageOnReady.var (JS_VAR_OPTIONS, JSExpr.json (this.m_aOptions.getAsJSON ()));
    final JSVar aSliders = aJSPackageGlobal.var (JS_VAR_SLIDERS, new JSExprDirect (JS_VAR_SLIDERS + "||{}")); //$NON-NLS-1$
    aJSPackageOnReady.var ("sID", JSExpr.lit (this.m_sID)); //$NON-NLS-1$
    aJSPackageOnReady.assign (aSliders.component (JSExpr.ref ("sID")), new JSInvocation ("new " + JS_CLASS_JSSOR_SLIDER).arg (this.m_sID) //$NON-NLS-1$ //$NON-NLS-2$
                                                                                                                        .arg (JSExpr.ref (JS_VAR_OPTIONS)));
    if (!this.m_aJSPackageEvents.isEmpty ())
    {
      aJSPackageOnReady.add (this.m_aJSPackageEvents);
    }
    if (!this.m_aJSPackageOnReady.isEmpty ())
    {
      aJSPackageOnReady.add (this.m_aJSPackageOnReady);
    }
    aResult.addChild (new HCScript (aJSPackageGlobal));
    aResult.addChild (new HCScriptOnDocumentReady (aJSPackageOnReady));

    // html container
    final HCDiv aOuterContainer = aResult.addAndReturnChild (this.m_aOuterContainer.build ());
    aOuterContainer.addChild (0, this.m_aSlidesContainer.build ());
    return aResult;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  public SliderOptions getOptions ()
  {
    return this.m_aOptions;
  }

  public IJSCodeProvider getJSGoTo (final int nIndex)
  {
    return JSExpr.ref (JS_VAR_SLIDERS).component (this.m_sID).invoke ("$GoTo").arg (nIndex); //$NON-NLS-1$
  }

  public IJSCodeProvider getJSPlayTo (final int nIndex)
  {
    return JSExpr.ref (JS_VAR_SLIDERS).component (this.m_sID).invoke ("$PlayTo").arg (nIndex); //$NON-NLS-1$
  }

  public void on (final ESliderEvent eEvent, final IJSExpression aFunction)
  {
    this.m_aJSPackageEvents.add (JSExpr.ref (JS_VAR_SLIDERS)
                                       .component (this.m_sID)
                                       .invoke ("$On").arg (JSExpr.ref ("$JssorSlider$").ref (eEvent.getID ())).arg (aFunction)); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public void onReady (final IJSCodeProvider aCode)
  {
    this.m_aJSPackageOnReady.add (aCode);
  }

  public static final void registerExternalResources ()
  {
    for (final ESliderJSPathProvider eJS : ESliderJSPathProvider.values (GlobalDebug.isDebugMode ()))
    {
      PerRequestJSIncludes.registerJSIncludeForThisRequest (eJS);
    }
    for (final ESliderCSSPathProvider eCSS : ESliderCSSPathProvider.values ())
    {
      PerRequestCSSIncludes.registerCSSIncludeForThisRequest (eCSS);
    }
  }
}
