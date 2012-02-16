package com.phloc.webbasics.app.html;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCNode;

/**
 * Interface for an object that provides content to an application layout area.
 * 
 * @author philip
 */
public interface IAreaContentProvider
{
  /**
   * @param aDisplayLocale
   *        The display locale to be used for rendering. May not be
   *        <code>null</code>.
   * @return The content of the area based on the current state.
   */
  @Nullable
  IHCNode getContent (@Nonnull Locale aDisplayLocale);
}
