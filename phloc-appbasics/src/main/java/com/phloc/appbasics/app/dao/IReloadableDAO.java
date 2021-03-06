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
package com.phloc.appbasics.app.dao;

import com.phloc.appbasics.app.dao.impl.DAOException;

/**
 * Special interface for reloadable DAOs
 * 
 * @author Philip Helger
 */
public interface IReloadableDAO extends IDAO
{
  /**
   * Call this method to reload the content from the original source.
   * 
   * @throws DAOException
   *         in case reloading fails
   */
  void reload () throws DAOException;
}
