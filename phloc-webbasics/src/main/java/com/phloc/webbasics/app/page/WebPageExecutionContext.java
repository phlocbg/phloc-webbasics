package com.phloc.webbasics.app.page;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;

/**
 * This page is instantiated per page view, so that the thread safety of the
 * execution parameters is more clear.
 * 
 * @author philip
 */
@NotThreadSafe
public class WebPageExecutionContext extends LayoutExecutionContext
{
  private final IWebPage m_aWebPage;
  private final HCNodeList m_aNodeList = new HCNodeList ();

  public WebPageExecutionContext (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final IWebPage aWebPage)
  {
    super (aLEC.getRequestScope (), aLEC.getDisplayLocale ());
    if (aWebPage == null)
      throw new NullPointerException ("webPage");
    m_aWebPage = aWebPage;
  }

  @Nonnull
  public IWebPage getWebPage ()
  {
    return m_aWebPage;
  }

  @Nonnull
  public HCNodeList getNodeList ()
  {
    return m_aNodeList;
  }
}
