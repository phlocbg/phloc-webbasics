package com.phloc.webbasics.app.menu;

import javax.annotation.Nonnull;

import com.phloc.commons.name.IHasDisplayText;
import com.phloc.webbasics.app.page.IPage;

/**
 * Base interface for a single menu item.
 * 
 * @author philip
 */
public interface IMenuItem extends IMenuObject, IHasDisplayText
{
  /**
   * @return The referenced page object.
   */
  @Nonnull
  IPage getPage ();
}
