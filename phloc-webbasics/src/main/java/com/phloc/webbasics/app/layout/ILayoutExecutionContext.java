/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.app.layout;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.url.SimpleURL;
import com.phloc.webbasics.app.ISimpleWebExecutionContext;

public interface ILayoutExecutionContext extends ISimpleWebExecutionContext
{
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
