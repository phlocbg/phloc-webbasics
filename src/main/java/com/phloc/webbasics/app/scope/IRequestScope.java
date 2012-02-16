package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.annotations.Nonempty;

public interface IRequestScope extends IScope {
  @Nonnull
  HttpServletRequest getRequest ();

  @Nonnull
  HttpServletResponse getResponse ();

  @Nullable
  String getUserAgent ();

  @Nonnull
  @Nonempty
  String getFullContextPath ();
}
