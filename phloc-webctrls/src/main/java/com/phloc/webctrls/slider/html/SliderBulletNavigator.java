package com.phloc.webctrls.slider.html;

import javax.annotation.Nullable;

import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.webctrls.slider.skin.EBulletSkin;

//u required  navigator u="navigator"
//position  required  absolute  style="position:absolute;"
//top optional  [integer]px style="top:12px;"
//left  optional  [integer]px style="left:12px;"
//right optional  [integer]px style="right:12px;"
//bottom  optional  [integer]px style="bottom:12px;"

public class SliderBulletNavigator extends AbstractSliderHTMLElement
{
  private static final ICSSClassProvider CLASS = DefaultCSSClassProvider.create ("SliderBulletNavigator");
  private final EBulletSkin m_eSkin;
  private Integer m_nTop = null;
  private Integer m_nLeft = null;
  private Integer m_nRight = null;
  private Integer m_nBottom = null;

  public SliderBulletNavigator (final EBulletSkin eSkin)
  {
    this (eSkin, null, null, null, null);
  }

  public SliderBulletNavigator (final EBulletSkin eSkin,
                                @Nullable final Integer nTop,
                                @Nullable final Integer nBottom,
                                @Nullable final Integer nLeft,
                                @Nullable final Integer nRight)
  {
    super ("navigator"); //$NON-NLS-1$
    this.m_eSkin = eSkin;
    this.m_nBottom = nBottom;
    this.m_nTop = nTop;
    this.m_nLeft = nLeft;
    this.m_nRight = nRight;

  }

  @Override
  protected void onBuildNode (final HCDiv aNode)
  {
    aNode.addClass (CLASS);
    aNode.addClass (DefaultCSSClassProvider.create ("jssorb" + this.m_eSkin.getID ())); //$NON-NLS-1$
    aNode.addStyle (CCSSProperties.POSITION.newValue (CCSSValue.ABSOLUTE));
    if (this.m_nTop != null)
    {
      aNode.addStyle (CCSSProperties.TOP.newValue (ECSSUnit.px (this.m_nTop.intValue ())));
    }
    if (this.m_nBottom != null)
    {
      aNode.addStyle (CCSSProperties.BOTTOM.newValue (ECSSUnit.px (this.m_nBottom.intValue ())));
    }
    if (this.m_nRight != null)
    {
      aNode.addStyle (CCSSProperties.RIGHT.newValue (ECSSUnit.px (this.m_nRight.intValue ())));
    }
    if (this.m_nLeft != null)
    {
      aNode.addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (this.m_nLeft.intValue ())));
    }
    final HCDiv aPrototype = aNode.addAndReturnChild (new HCDiv ());
    aPrototype.setCustomAttr (this.ATTRIBUTE_USAGE, "prototype"); //$NON-NLS-1$
    this.m_eSkin.applyStyles (aPrototype);
  }
}
