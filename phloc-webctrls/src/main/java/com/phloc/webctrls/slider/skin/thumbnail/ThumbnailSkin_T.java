package com.phloc.webctrls.slider.skin.thumbnail;

import java.util.List;

import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.webctrls.slider.html.HCThumbnailTemplate;

public class ThumbnailSkin_T implements IThumbnailSkin
{
  private final int m_nLineHeight;
  private final int m_nFontSize;
  private final String m_sClass;
  private final String m_sWidth;
  private final String m_sHeight;
  private final boolean m_bExtended;

  public ThumbnailSkin_T (final String sWidth,
                          final String sHeight,
                          final int nLineHeight,
                          final int nFontSize,
                          @Nullable final String sClass,
                          final boolean bExtended)
  {
    this.m_sWidth = sWidth;
    this.m_sHeight = sHeight;
    this.m_nLineHeight = nLineHeight;
    this.m_nFontSize = nFontSize;
    this.m_sClass = sClass;
    this.m_bExtended = bExtended;
  }

  @Override
  public List <IHCNode> getPrototypeContent ()
  {
    final List <IHCNode> aContent = ContainerHelper.newList ();
    final HCThumbnailTemplate aT = new HCThumbnailTemplate ().addStyle (CCSSProperties.POSITION.newValue (CCSSValue.ABSOLUTE))
                                                             .addStyle (CCSSProperties.WIDTH.newValue (this.m_sWidth))
                                                             .addStyle (CCSSProperties.HEIGHT.newValue (this.m_sHeight))
                                                             .addStyle (CCSSProperties.TOP.newValue (ECSSUnit.px (0)))
                                                             .addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (0)));
    if (this.m_bExtended)
    {
      aT.addStyle (CCSSProperties.FONT_WEIGHT.newValue (CCSSValue.NORMAL))
        .addStyle (CCSSProperties.COLOR.newValue ("#fff")) //$NON-NLS-1$
        .addStyle (CCSSProperties.LINE_HEIGHT.newValue (ECSSUnit.px (this.m_nLineHeight)))
        .addStyle (CCSSProperties.FONT_SIZE.newValue (ECSSUnit.px (this.m_nFontSize)))
        .addStyle (CCSSProperties.PADDING_LEFT.newValue (ECSSUnit.px (10)));
    }
    if (StringHelper.hasText (this.m_sClass))
    {
      aT.addClass (DefaultCSSClassProvider.create (this.m_sClass));
    }
    aContent.add (aT);
    return aContent;
  }
}
