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
package com.phloc.appbasics.app.menu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.filter.IFilter;
import com.phloc.commons.url.ISimpleURL;

/**
 * Base interface for a single menu item referencing an arbitrary URL.
 *
 * @author Philip Helger
 */
public interface IMenuItemExternal extends IMenuItem
{
  /**
   * {@inheritDoc}
   */
  @Nonnull
  IMenuItemExternal setDisplayFilter (@Nullable IFilter <IMenuObject> aDisplayFilter);

  /**
   * @return The referenced external URL.
   */
  @Nonnull
  ISimpleURL getURL ();

  /**
   * @return The (HTML) target of the link
   */
  @Nullable
  String getTarget ();

  /**
   * Set the (HTML) target of the link.
   *
   * @param eTarget
   *        The target window. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  MenuItemExternal setTarget (@Nullable EMenuItemExternalTarget eTarget);

  /**
   * Set the (HTML) target of the link.
   *
   * @param sTarget
   *        The name of the target window. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  IMenuItemExternal setTarget (@Nullable String sTarget);
}
