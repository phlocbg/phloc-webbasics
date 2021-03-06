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
package com.phloc.appbasics.app.dao.container;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.dao.IAutoSaveAware;
import com.phloc.appbasics.app.dao.IDAO;

/**
 * A marker interface for objects containing other DAOs.
 * 
 * @author Philip Helger
 */
public interface IDAOContainer extends IAutoSaveAware
{
  /**
   * @return A list of all contained DAOs. The returned list may contain
   *         <code>null</code> elements!
   */
  @Nonnull
  Collection <IDAO> getContainedDAOs ();
}
