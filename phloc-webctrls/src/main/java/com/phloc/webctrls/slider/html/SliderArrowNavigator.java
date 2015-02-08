package com.phloc.webctrls.slider.html;

import javax.annotation.Nullable;

import com.phloc.commons.string.StringHelper;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webctrls.slider.skin.EArrowSkin;

//u required  arrowleft|arrowright  u="arrowleft" | u="arrowright"
//position  required  absolute  style="position:absolute;"
//top optional  [integer]px style="top:130px;"
//left  optional  [integer]px style="left:8px;"
//right optional  [integer]px style="right:8px;"
//bottom  optional  [integer]px style="bottom:8px;"
//width required  [integer]px style="width:50px;"
//height  required  [integer]px style="heght:50px;"

public class SliderArrowNavigator implements IHCNodeBuilder
{
  private static final ICSSClassProvider CLASS = DefaultCSSClassProvider.create ("SliderArrowNavigator"); //$NON-NLS-1$
  private final boolean m_bLeft;
  private final EArrowSkin m_eSkin;

  private int m_nTop = 123;
  private int m_nLeftRight = 8;

  protected static final String ATTRIBUTE_USAGE = "u"; //$NON-NLS-1$
  private final String m_sUsage;
  private IHCNode m_aContent = null;

  public SliderArrowNavigator (final boolean bLeft, final EArrowSkin eSkin)
  {
    if (eSkin == null)
    {
      throw new NullPointerException ("eSkin"); //$NON-NLS-1$
    }
    this.m_sUsage = (bLeft ? "arrowleft" : "arrowright"); //$NON-NLS-1$ //$NON-NLS-2$
    this.m_bLeft = bLeft;
    this.m_eSkin = eSkin;
  }

  public void setTop (final int nTop)
  {
    this.m_nTop = nTop;
  }

  public void setLeftRight (final int nLeftRight)
  {
    this.m_nLeftRight = nLeftRight;
  }

  public void setContent (@Nullable final IHCNode aContent)
  {
    this.m_aContent = aContent;
  }

  @Override
  public final HCSpan build ()
  {
    final HCSpan aNode = new HCSpan ();
    if (this.m_aContent != null)
    {
      aNode.addChild (this.m_aContent);
    }
    aNode.addClass (CLASS);
    if (StringHelper.hasText (this.m_sUsage))
    {
      aNode.setCustomAttr (this.ATTRIBUTE_USAGE, this.m_sUsage);
    }
    aNode.addClass (DefaultCSSClassProvider.create ("jssora" + this.m_eSkin.getID () + (this.m_bLeft ? "l" : "r"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    aNode.addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (this.m_eSkin.getWidth ())));
    aNode.addStyle (CCSSProperties.HEIGHT.newValue (ECSSUnit.px (this.m_eSkin.getHeight ())));
    aNode.addStyle (CCSSProperties.TOP.newValue (ECSSUnit.px (this.m_nTop)));
    if (this.m_bLeft)
    {
      aNode.addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (this.m_nLeftRight)));
    }
    else
    {
      aNode.addStyle (CCSSProperties.RIGHT.newValue (ECSSUnit.px (this.m_nLeftRight)));
    }
    return aNode;
  }

}
