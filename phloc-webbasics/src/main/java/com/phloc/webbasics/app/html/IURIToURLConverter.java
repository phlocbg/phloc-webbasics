package com.phloc.webbasics.app.html;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.url.ISimpleURL;

public interface IURIToURLConverter
{
  /**
   * Convert the passed URI to a URL.
   * 
   * @param sURI
   *        The URI to be converted.
   * @return The created URL.
   */
  @Nonnull
  ISimpleURL getAsURL (@Nonnull @Nonempty String sURI);
}
