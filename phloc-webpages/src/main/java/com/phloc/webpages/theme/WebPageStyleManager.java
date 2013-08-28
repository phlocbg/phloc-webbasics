package com.phloc.webpages.theme;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.scopes.singleton.GlobalSingleton;

public class WebPageStyleManager extends GlobalSingleton
{
  private IWebPageStyle m_aStyle = new WebPageStyleBootstrap2 ();

  @Deprecated
  @UsedViaReflection
  public WebPageStyleManager ()
  {}

  @Nonnull
  public static WebPageStyleManager getInstance ()
  {
    return getGlobalSingleton (WebPageStyleManager.class);
  }

  @Nonnull
  public static IWebPageStyle getStyle ()
  {
    return getInstance ().m_aStyle;
  }

  public void setStyle (@Nonnull final IWebPageStyle aStyle)
  {
    if (aStyle == null)
      throw new NullPointerException ("Style");
    m_aStyle = aStyle;
  }
}
