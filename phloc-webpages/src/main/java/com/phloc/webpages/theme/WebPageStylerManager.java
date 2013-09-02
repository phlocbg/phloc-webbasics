package com.phloc.webpages.theme;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.scopes.singleton.GlobalSingleton;

public class WebPageStylerManager extends GlobalSingleton
{
  private IWebPageStyler m_aStyle = new WebPageStylerSimple ();

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

  public void setStyler (@Nonnull final IWebPageStyler aStyler)
  {
    if (aStyler == null)
      throw new NullPointerException ("Styler");
    m_aStyle = aStyler;
  }
}
