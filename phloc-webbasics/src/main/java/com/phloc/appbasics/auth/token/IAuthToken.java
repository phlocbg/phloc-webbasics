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
package com.phloc.appbasics.auth.token;

import javax.annotation.Nonnull;

import org.joda.time.DateTime;

import com.phloc.appbasics.auth.identify.IAuthIdentification;
import com.phloc.commons.id.IHasID;

/**
 * Interface for an auth token.
 * 
 * @author Philip Helger
 */
public interface IAuthToken extends IHasID <String>
{
  /**
   * @return The secret key token representing a session of a subject. Never
   *         <code>null</code>.
   */
  @Nonnull
  String getID ();

  /**
   * @return The underlying identification object. Never <code>null</code>.
   */
  @Nonnull
  IAuthIdentification getIdentification ();

  /**
   * @return The date and time when the token was created. Never
   *         <code>null</code>.
   */
  @Nonnull
  DateTime getCreationDate ();

  /**
   * @return The date and time when the token was last accessed. If the token
   *         was never accessed before, the creation date time is returned.
   *         Never <code>null</code>.
   */
  @Nonnull
  DateTime getLastAccessDate ();

  /**
   * Check if the token is expired. Expired tokens are considered invalid.
   * 
   * @return <code>true</code> if the token is already expired.
   */
  boolean isExpired ();
}
