package com.phloc.webctrls.facebook.opengraph;

import java.util.Map;

import javax.annotation.Nullable;

import com.phloc.commons.annotations.IsSPIInterface;

@IsSPIInterface
public interface IFacebookObjectProvider
{
  @Nullable
  Map <EOpenGraphObjectProperty, String> getObject ();

  /**
   * @return A positive integer (default=0) where a higher number represent a
   *         more specific object
   */
  int getSpecifity ();
}
