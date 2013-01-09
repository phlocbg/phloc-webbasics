package com.phloc.webbasics.app.ui;

import javax.annotation.concurrent.Immutable;

import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;

/**
 * WebBasics predefined CSS classes
 * 
 * @author philip
 */
@Immutable
public final class WebBasicsCSS
{
  // Align text right
  public static final ICSSClassProvider CSS_CLASS_RIGHT = DefaultCSSClassProvider.create ("right");

  private WebBasicsCSS ()
  {}
}
