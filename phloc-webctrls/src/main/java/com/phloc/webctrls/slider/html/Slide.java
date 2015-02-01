package com.phloc.webctrls.slider.html;

import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCImg;

public class Slide extends AbstractSliderHTMLElement
{
  private static final ICSSClassProvider CLASS = DefaultCSSClassProvider.create ("SliderSlide");
  private static final String LAZY_LOAD_SOURCE = "src2"; //$NON-NLS-1$
  private static final String USAGE_IMAGE = "image"; //$NON-NLS-1$

  private final ISimpleURL m_aImgSrc;
  private final ISimpleURL m_aImgLink;
  private final boolean m_bLazyLoadImg;
  private final IHCNode m_aContent;

  public Slide (@Nullable final ISimpleURL aImgSrc,
                @Nullable final ISimpleURL aImgLink,
                final boolean bLazyLoad,
                @Nullable final IHCNode aContent)
  {
    if (aImgSrc == null && aContent == null)
    {
      throw new IllegalArgumentException ("one of image or content needs to be specified!"); //$NON-NLS-1$
    }
    this.m_aImgSrc = aImgSrc;
    this.m_aImgLink = aImgLink;
    this.m_bLazyLoadImg = bLazyLoad;
    this.m_aContent = aContent;
  }

  @Override
  protected void onBuildNode (final HCDiv aNode)
  {
    if (this.m_aImgSrc != null)
    {
      HCA aLink = null;
      if (this.m_aImgLink != null)
      {
        aLink = aNode.addAndReturnChild (new HCA (this.m_aImgLink));
        aLink.setCustomAttr (this.ATTRIBUTE_USAGE, USAGE_IMAGE);
      }

      final HCImg aImage = new HCImg ();
      if (this.m_bLazyLoadImg)
      {
        aImage.setCustomAttr (LAZY_LOAD_SOURCE, this.m_aImgSrc.getAsString ());
      }
      else
      {
        aImage.setSrc (this.m_aImgSrc);
      }
      if (aLink == null)
      {
        aNode.addChild (aImage);
        aImage.setCustomAttr (this.ATTRIBUTE_USAGE, USAGE_IMAGE);
      }
      else
      {
        aLink.addChild (aImage);
      }
    }
    if (this.m_aContent != null)
    {
      aNode.addChild (this.m_aContent);
    }
    aNode.addClass (CLASS);
  }
}
