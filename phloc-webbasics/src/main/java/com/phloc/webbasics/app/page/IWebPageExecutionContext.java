package com.phloc.webbasics.app.page;

import javax.annotation.Nonnull;

import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.layout.ILayoutExecutionContext;

public interface IWebPageExecutionContext extends ILayoutExecutionContext
{
  /**
   * @return The invoked web page. Never <code>null</code>.
   */
  @Nonnull
  IWebPage <? extends IWebPageExecutionContext> getWebPage ();

  /**
   * @return The node list to be filled with content. Never <code>null</code>.
   */
  @Nonnull
  HCNodeList getNodeList ();
}
