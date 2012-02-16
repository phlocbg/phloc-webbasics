package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpSession;

public interface ISessionScope extends IScope {
  @Nonnull
  HttpSession getSession ();
}
