package com.phloc.webdemoapp.page.view;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.hc.html.HCH1;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webpages.AbstractWebPageExt;

public class PageDemo extends AbstractWebPageExt
{
  public PageDemo (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Demo page");
  }

  @Override
  protected void fillContent (final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    aNodeList.addChild (new HCH1 ().addChild ("This is PageDemo"));
  }
}
