package com.phloc.webctrls.facebook.opengraph;

import com.phloc.html.meta.MetaElement;

public class OpenGraphMetaTag extends MetaElement
{

  public OpenGraphMetaTag (final EOpenGraphMetaTag eProperty, final String sContent)
  {
    super (eProperty.getID (), sContent);
    // setNamespaceURI ("http://ogp.me/ns#");
  }

  @Override
  public boolean isProperty ()
  {
    return true;
  }
}
