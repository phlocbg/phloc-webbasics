package com.phloc.webpages.theme;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.scopes.singleton.GlobalSingleton;

public class WebPageStylerManager extends GlobalSingleton
{
  private IWebPageStyler m_aStyle = new WebPageStylerBootstrap2 ();

  @Deprecated
  @UsedViaReflection
  public WebPageStylerManager ()
  {}

  @Nonnull
  public static WebPageStylerManager getInstance ()
  {
    return getGlobalSingleton (WebPageStylerManager.class);
  }

  @Nonnull
  public static IWebPageStyler getStyler ()
  {
    return getInstance ().m_aStyle;
  }

  public void setStyle (@Nonnull final IWebPageStyler aStyle)
  {
    if (aStyle == null)
      throw new NullPointerException ("Style");
    m_aStyle = aStyle;
  }
}
