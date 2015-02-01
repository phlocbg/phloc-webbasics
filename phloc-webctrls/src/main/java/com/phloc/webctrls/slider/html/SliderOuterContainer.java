package com.phloc.webctrls.slider.html;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.string.StringHelper;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.webctrls.slider.skin.EArrowSkin;
import com.phloc.webctrls.slider.skin.EBulletSkin;
import com.phloc.webctrls.slider.skin.EThumbnailSkin;

public class SliderOuterContainer extends AbstractSliderHTMLElement
{
  private static final ICSSClassProvider CLASS = DefaultCSSClassProvider.create ("SliderOuterContainer");
  private final String m_sID;
  private int m_nHeight = 300;
  private int m_nWidth = 600;
  private boolean m_bAbsolute = false;

  private SliderArrowNavigator m_aArrowNavigatorLeft = null;
  private SliderArrowNavigator m_aArrowNavigatorRight = null;
  private SliderBulletNavigator m_aBulletNavigator = null;
  private SliderThumbnailNavigator m_aThumbnailNavigator = null;

  public SliderOuterContainer (final String sID)
  {
    super ();
    if (StringHelper.hasNoText (sID))
    {
      throw new IllegalArgumentException ("sID must not be null or empty!"); //$NON-NLS-1$
    }
    this.m_sID = sID;
  }

  public SliderOuterContainer setArrowNavigator (@Nonnull final EArrowSkin eSkin)
  {
    this.m_aArrowNavigatorLeft = new SliderArrowNavigator (true, eSkin);
    this.m_aArrowNavigatorRight = new SliderArrowNavigator (false, eSkin);
    return this;
  }

  public SliderOuterContainer setBulletNavigator (@Nonnull final EBulletSkin eSkin,
                                                  @Nullable final Integer nTop,
                                                  @Nullable final Integer nBottom,
                                                  @Nullable final Integer nLeft,
                                                  @Nullable final Integer nRight)
  {
    this.m_aBulletNavigator = new SliderBulletNavigator (eSkin, nTop, nBottom, nLeft, nRight);
    return this;
  }

  public SliderOuterContainer setThumbnailNavigator (@Nonnull final EThumbnailSkin eSkin)
  {
    this.m_aThumbnailNavigator = new SliderThumbnailNavigator (eSkin);
    return this;
  }

  public SliderOuterContainer setAbsolute (final boolean bAbsolute)
  {
    this.m_bAbsolute = bAbsolute;
    return this;
  }

  public SliderOuterContainer setWidth (@Nonnegative final int nWidth)
  {
    if (nWidth < 0)
    {
      throw new IllegalArgumentException ("nWidth must not be negative!"); //$NON-NLS-1$
    }
    this.m_nWidth = nWidth;
    return this;
  }

  public SliderOuterContainer setHeight (@Nonnegative final int nHeight)
  {
    if (nHeight < 0)
    {
      throw new IllegalArgumentException ("nHeight must not be negative!"); //$NON-NLS-1$
    }
    this.m_nHeight = nHeight;
    return this;
  }

  @Override
  protected void onBuildNode (final HCDiv aNode)
  {
    aNode.setID (this.m_sID);
    aNode.addClass (CLASS);
    aNode.addStyle (CCSSProperties.POSITION.newValue (this.m_bAbsolute ? CCSSValue.ABSOLUTE : CCSSValue.RELATIVE));
    aNode.addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (this.m_nWidth)));
    aNode.addStyle (CCSSProperties.HEIGHT.newValue (ECSSUnit.px (this.m_nHeight)));

    if (this.m_aArrowNavigatorLeft != null)
    {
      aNode.addChild (this.m_aArrowNavigatorLeft);
    }
    if (this.m_aArrowNavigatorRight != null)
    {
      aNode.addChild (this.m_aArrowNavigatorRight);
    }
    if (this.m_aBulletNavigator != null)
    {
      aNode.addChild (this.m_aBulletNavigator);
    }
    if (this.m_aThumbnailNavigator != null)
    {
      aNode.addChild (this.m_aThumbnailNavigator);
    }
  }
}
