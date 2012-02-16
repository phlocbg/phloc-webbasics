package com.phloc.webbasics.app.html;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCNode;

/**
 * Interface for an object that provides content (e.g. to an application layout
 * area) based on a given locale.
 * 
 * @author philip
 */
public interface IContentProvider
{
  /**
   * @param aContentLocale
   *        The locale to be used for displaying.
   * @return The content for the given locale.
   */
  @Nullable
  IHCNode getContent (@Nonnull Locale aContentLocale);
}
