/**
 * Copyright (C) 2006-2014 phloc systems
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

import com.phloc.appbasics.app.page.IPage;
import com.phloc.commons.filter.IFilter;

/**
 * Base interface for a single menu item referencing an internal page.
 * 
 * @author Philip Helger
 */
public interface IMenuItemPage extends IMenuItem
{
  /**
   * {@inheritDoc}
   */
  @Nonnull
  IMenuItemPage setDisplayFilter (@Nullable IFilter <IMenuObject> aDisplayFilter);

  /**
   * @return The referenced page object.
   */
  @Nonnull
  IPage getPage ();
}
