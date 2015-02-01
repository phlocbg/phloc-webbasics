package com.phloc.webctrls.slider.html;

import javax.annotation.Nonnull;

import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.webctrls.slider.skin.EThumbnailSkin;

//u required  thumbnavigator  u="thumbnavigator"
//position  required  absolute  style="position:absolute;"
//top optional  [integer]px style="top:0px;"
//left  optional  [integer]px style="left:0px;"
//right optional  [integer]px style="right:0px;"
//bottom  optional  [integer]px style="bottom:0px;"
//width required  [integer]px style="width:600px;"
//height  required  [integer]px style="heght:60px;"
public class SliderThumbnailNavigator extends AbstractSliderHTMLElement
{
  private static final ICSSClassProvider CLASS = DefaultCSSClassProvider.create ("SliderThumbnailNavigator");
  private final EThumbnailSkin m_eSkin;

  public SliderThumbnailNavigator (@Nonnull final EThumbnailSkin eSkin)
  {
    super ("thumbnavigator"); //$NON-NLS-1$
    if (eSkin == null)
    {
      throw new NullPointerException ("eSkin"); //$NON-NLS-1$
    }
    this.m_eSkin = eSkin;
  }

  @Override
  protected void onBuildNode (final HCDiv aNode)
  {
    aNode.addClass (CLASS);
    aNode.addClass (DefaultCSSClassProvider.create (this.m_eSkin.getID ()));
    aNode.addStyle (CCSSProperties.CURSOR.newValue (CCSSValue.DEFAULT));
    aNode.addStyle (CCSSProperties.POSITION.newValue (CCSSValue.ABSOLUTE));
    aNode.addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (0)));
    aNode.addStyle (CCSSProperties.BOTTOM.newValue (ECSSUnit.px (0)));
    aNode.addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (this.m_eSkin.getWidth ())));
    aNode.addStyle (CCSSProperties.HEIGHT.newValue (ECSSUnit.px (this.m_eSkin.getHeight ())));

    this.m_eSkin.addOptionalMask (aNode);

    final HCDiv aSlides = aNode.addAndReturnChild (new HCDiv ());
    aSlides.setCustomAttr (this.ATTRIBUTE_USAGE, "slides"); //$NON-NLS-1$
    aSlides.addStyle (CCSSProperties.CURSOR.newValue (CCSSValue.MOVE));

    final HCDiv aPrototype = aSlides.addAndReturnChild (new HCDiv ());
    aPrototype.addClass (DefaultCSSClassProvider.create ("p")); //$NON-NLS-1$
    aPrototype.setCustomAttr (this.ATTRIBUTE_USAGE, "prototype"); //$NON-NLS-1$
    aPrototype.addStyle (CCSSProperties.POSITION.newValue (CCSSValue.ABSOLUTE));
    aPrototype.addStyle (CCSSProperties.TOP.newValue (ECSSUnit.px (0)));
    aPrototype.addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (0)));
    aPrototype.addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (this.m_eSkin.getPrototypeWidth ())));
    aPrototype.addStyle (CCSSProperties.HEIGHT.newValue (ECSSUnit.px (this.m_eSkin.getPrototypeHeight ())));
    this.m_eSkin.addPrototypeContent (aPrototype);
  }
}
