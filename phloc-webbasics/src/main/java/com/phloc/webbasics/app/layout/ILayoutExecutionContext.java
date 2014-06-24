package com.phloc.webbasics.app.layout;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.url.SimpleURL;
import com.phloc.webbasics.app.ISimpleWebExecutionContext;

public interface ILayoutExecutionContext extends ISimpleWebExecutionContext
{
  /**
   * @return The menu tree to be used for this layout execution context. Never
   *         <code>null</code>.
   */
  @Nonnull
  IMenuTree getMenuTree ();

  /**
   * @return The selected menu item as specified in the constructor. Never
   *         <code>null</code>.
   */
  @Nonnull
  IMenuItemPage getSelectedMenuItem ();

  /**
   * @return The ID of the selected menu item as specified in the constructor.
   *         Neiter <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  String getSelectedMenuItemID ();

  /**
   * Get the URL to the current page.
   * 
   * @return The non-<code>null</code> URL to the current page (selected menu
   *         item) with the passed parameters.
   */
  @Nonnull
  SimpleURL getSelfHref ();

  /**
   * Get the URL to the current page with the provided set of parameters.
   * 
   * @param aParams
   *        The optional request parameters to be used. May be <code>null</code>
   *        or empty.
   * @return The non-<code>null</code> URL to the current page (selected menu
   *         item) with the passed parameters.
   */
  @Nonnull
  SimpleURL getSelfHref (@Nullable Map <String, String> aParams);
}
