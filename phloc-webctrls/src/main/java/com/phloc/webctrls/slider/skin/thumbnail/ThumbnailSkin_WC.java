package com.phloc.webctrls.slider.skin.thumbnail;

import java.util.List;
import java.util.Map;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.property.ICSSProperty;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.webctrls.slider.html.HCThumbnailTemplate;

public class ThumbnailSkin_WC implements IThumbnailSkin
{
  private final ICSSProperty [] m_aProperties;
  private final String [] m_aValues;

  public ThumbnailSkin_WC ()
  {
    this (null, null);
  }

  public ThumbnailSkin_WC (final ICSSProperty [] aProperties, final String [] aValues)
  {
    this.m_aProperties = ArrayHelper.getCopy (aProperties);
    this.m_aValues = ArrayHelper.getCopy (aValues);
  }

  @Override
  public List <IHCNode> getPrototypeContent ()
  {
    final List <IHCNode> aContent = ContainerHelper.newList ();
    final HCDiv aW = new HCDiv ().addClass (DefaultCSSClassProvider.create ("w")); //$NON-NLS-1$
    aW.addAndReturnChild (new HCThumbnailTemplate ())
      .addStyle (CCSSProperties.WIDTH.newValue (CCSSValue.PERC100))
      .addStyle (CCSSProperties.HEIGHT.newValue (CCSSValue.PERC100))
      .addStyle (CCSSProperties.BORDER.newValue (CCSSValue.NONE))
      .addStyle (CCSSProperties.POSITION.newValue (CCSSValue.ABSOLUTE))
      .addStyle (CCSSProperties.TOP.newValue (ECSSUnit.px (0)))
      .addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (0)));
    aContent.add (aW);
    final HCDiv aC = new HCDiv ().addClass (DefaultCSSClassProvider.create ("c")); //$NON-NLS-1$
    for (final Map.Entry <ICSSProperty, String> aEntry : ContainerHelper.newMap (this.m_aProperties, this.m_aValues)
                                                                        .entrySet ())
    {
      aC.addStyle (aEntry.getKey ().newValue (aEntry.getValue ()));
    }
    aContent.add (aC);
    return aContent;
  }
}
