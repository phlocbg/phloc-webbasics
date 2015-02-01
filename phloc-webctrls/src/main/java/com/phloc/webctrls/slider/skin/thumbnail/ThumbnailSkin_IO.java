package com.phloc.webctrls.slider.skin.thumbnail;

import java.util.List;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.webctrls.slider.html.HCThumbnailTemplate;

public class ThumbnailSkin_IO implements IThumbnailSkin
{
  @Override
  public List <IHCNode> getPrototypeContent ()
  {
    final List <IHCNode> aContent = ContainerHelper.newList ();

    aContent.add (new HCThumbnailTemplate ().addClass (DefaultCSSClassProvider.create ("i")) //$NON-NLS-1$
                                            .addStyle (CCSSProperties.POSITION.newValue (CCSSValue.ABSOLUTE)));
    aContent.add (new HCDiv ().addClass (DefaultCSSClassProvider.create ("o"))); //$NON-NLS-1$
    return aContent;
  }
}
