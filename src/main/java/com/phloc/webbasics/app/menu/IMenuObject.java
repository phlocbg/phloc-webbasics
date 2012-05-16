/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.app.menu;

import javax.annotation.Nullable;

import com.phloc.commons.filter.IFilter;
import com.phloc.commons.id.IHasID;

/**
 * Marker interface for menu items.
 * 
 * @author philip
 */
public interface IMenuObject extends IHasID <String>
{
  /**
   * @return An optional filter that toggles visibility.
   */
  @Nullable
  IFilter <IMenuObject> getDisplayFilter ();

  /**
   * Set a new display filter for this menu object.
   * 
   * @param aDisplayFilter
   *        The new display filter to set. Maybe <code>null</code> to indicate
   *        that no filter is required.
   */
  void setDisplayFilter (@Nullable IFilter <IMenuObject> aDisplayFilter);

  /**
   * @return <code>true</code> if either no display filter is installed, or if
   *         the installed filter matches, <code>false</code> otherwise.
   */
  boolean matchesDisplayFilter ();
}
