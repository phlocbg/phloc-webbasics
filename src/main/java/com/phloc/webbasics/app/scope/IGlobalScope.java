package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

public interface IGlobalScope extends IScope {
  @Nonnull
  ServletContext getServletContext ();
}
