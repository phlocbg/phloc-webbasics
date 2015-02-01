package com.phloc.webctrls.slider.skin.thumbnail;

import java.util.List;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.webctrls.slider.html.HCThumbnailTemplate;

public class ThumbnailSkin_OBIF implements IThumbnailSkin
{
  private final int m_nWidth;
  private final int m_nHeight;
  private final int m_nOTop;
  private final int m_nOLeft;

  public ThumbnailSkin_OBIF (final int nWidth, final int nHeight, final int nOTop, final int nOLeft)
  {
    this.m_nWidth = nWidth;
    this.m_nHeight = nHeight;
    this.m_nOTop = nOTop;
    this.m_nOLeft = nOLeft;
  }

  @Override
  public List <IHCNode> getPrototypeContent ()
  {
    final List <IHCNode> aContent = ContainerHelper.newList ();
    final HCDiv aO = new HCDiv ().addClass (DefaultCSSClassProvider.create ("o")); //$NON-NLS-1$
    aO.addStyle (CCSSProperties.POSITION.newValue (CCSSValue.ABSOLUTE))
      .addStyle (CCSSProperties.TOP.newValue (ECSSUnit.px (this.m_nOTop)))
      .addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (this.m_nOLeft)))
      .addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (this.m_nWidth)))
      .addStyle (CCSSProperties.HEIGHT.newValue (ECSSUnit.px (this.m_nHeight)))
      .addStyle (CCSSProperties.OVERFLOW.newValue (CCSSValue.HIDDEN));

    aO.addAndReturnChild (new HCThumbnailTemplate ()).addClass (DefaultCSSClassProvider.create ("b")) //$NON-NLS-1$
      .addStyle (CCSSProperties.POSITION.newValue (CCSSValue.ABSOLUTE))
      .addStyle (CCSSProperties.TOP.newValue (ECSSUnit.px (0)))
      .addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (0)))
      .addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (this.m_nWidth)))
      .addStyle (CCSSProperties.HEIGHT.newValue (ECSSUnit.px (this.m_nHeight)))
      .addStyle (CCSSProperties.BORDER.newValue (CCSSValue.NONE));

    aO.addAndReturnChild (new HCDiv ()).addClass (DefaultCSSClassProvider.create ("i")); //$NON-NLS-1$

    aO.addAndReturnChild (new HCThumbnailTemplate ()).addClass (DefaultCSSClassProvider.create ("f")) //$NON-NLS-1$
      .addStyle (CCSSProperties.POSITION.newValue (CCSSValue.ABSOLUTE))
      .addStyle (CCSSProperties.TOP.newValue (ECSSUnit.px (0)))
      .addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (0)))
      .addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (this.m_nWidth)))
      .addStyle (CCSSProperties.HEIGHT.newValue (ECSSUnit.px (this.m_nHeight)))
      .addStyle (CCSSProperties.BORDER.newValue (CCSSValue.NONE));

    aContent.add (aO);
    return aContent;
  }
}
