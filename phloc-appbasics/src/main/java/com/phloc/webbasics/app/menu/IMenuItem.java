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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.filter.IFilter;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.webbasics.app.page.IBasePage;

/**
 * Base interface for a single menu item.
 * 
 * @author philip
 */
public interface IMenuItem extends IMenuObject, IHasDisplayText
{
  /**
   * {@inheritDoc}
   */
  @Nonnull
  IMenuItem setDisplayFilter (@Nullable IFilter <IMenuObject> aDisplayFilter);

  /**
   * @return The referenced page object.
   */
  @Nonnull
  IBasePage getPage ();
}
