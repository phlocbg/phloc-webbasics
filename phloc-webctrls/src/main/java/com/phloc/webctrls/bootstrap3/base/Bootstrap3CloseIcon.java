package com.phloc.webctrls.bootstrap3.base;

import com.phloc.html.CHTMLAttributes;
import com.phloc.html.entities.EHTMLEntity;
import com.phloc.html.hc.html.HCButton;
import com.phloc.html.hc.impl.HCEntityNode;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;

public class Bootstrap3CloseIcon extends HCButton
{
  public Bootstrap3CloseIcon ()
  {
    addClass (CBootstrap3CSS.CLOSE);
    setCustomAttr (CHTMLAttributes.ARIA_HIDDEN, "true");
    addChild (new HCEntityNode (EHTMLEntity.times, "x"));
  }
}
