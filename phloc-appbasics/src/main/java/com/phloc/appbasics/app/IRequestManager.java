/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.appbasics.app;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.scopes.nonweb.domain.IRequestScope;

public interface IRequestManager
{
  public static final String REQUEST_PARAMETER_MENUITEM = "menuitem";
  public static final String REQUEST_PARAMETER_DISPLAY_LOCALE = "locale";

  /**
   * To be called upon the beginning of each request. Checks for the content of
   * the request parameter {@value #REQUEST_PARAMETER_MENUITEM} to determine the
   * selected menu item. Checks for the content of the request parameter
   * {@value #REQUEST_PARAMETER_DISPLAY_LOCALE} to determine any changes in the
   * display locale.
   */
  void onRequestBegin (@Nonnull IRequestScope aRequestScope);

  /**
   * @return The ID of the last requested menu item, or <code>null</code> if the
   *         corresponding session parameter is not present.
   */
  @Nullable
  IMenuItemPage getSessionMenuItem ();

  /**
   * @return The specified default menu item. May be <code>null</code> if no
   *         default menu item is specified.
   */
  @Nullable
  IMenuItemPage getDefaultMenuItem ();

  /**
   * Resolve the request parameter for the menu item to an {@link IMenuItem}
   * object. If no parameter is present, return the default menu item.
   * 
   * @return The resolved menu item object from the request parameter. Never
   *         <code>null</code>.
   */
  @Nonnull
  IMenuItemPage getRequestMenuItem ();

  /**
   * @return The ID of the current request menu item. May not be
   *         <code>null</code>.
   */
  @Nonnull
  String getRequestMenuItemID ();

  /**
   * Get the locale to be used for this request. If no parameter is present, the
   * one from the session is used.
   * 
   * @return The locale to be used for the current request.
   */
  @Nonnull
  Locale getRequestDisplayLocale ();

  /**
   * @return The country-Locale of the request display locale
   * @see #getRequestDisplayLocale()
   */
  @Nonnull
  Locale getRequestDisplayCountry ();
}
