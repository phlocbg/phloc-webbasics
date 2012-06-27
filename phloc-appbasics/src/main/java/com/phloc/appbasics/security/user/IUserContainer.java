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
package com.phloc.appbasics.security.user;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Base read-only interface for objects containing users.
 * 
 * @author philip
 */
public interface IUserContainer
{
  /**
   * @return A non-<code>null</code>but maybe empty set of all assigned user
   *         IDs.
   */
  @Nonnull
  Set <String> getAllContainedUserIDs ();

  /**
   * Check if the passed user is contained in this container.
   * 
   * @param sUserID
   *        The user ID to check. May be <code>null</code>.
   * @return <code>true</code> if the user is contained in this container,
   *         <code>false</code> otherwise.
   */
  boolean containsUserID (String sUserID);
}
