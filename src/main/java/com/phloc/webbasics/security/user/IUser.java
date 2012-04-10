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
package com.phloc.webbasics.security.user;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.name.IHasDisplayName;

/**
 * Interface for a single user
 * 
 * @author philip
 */
public interface IUser extends IHasID <String>, IHasDisplayName
{
  /**
   * @return The email address of the user. Also the login name.
   */
  @Nonnull
  @Nonempty
  String getEmailAddress ();

  /**
   * @return The hashed password of the user.
   */
  @Nonnull
  @Nonempty
  String getPasswordHash ();

  /**
   * @return The first name of the user.
   */
  @Nullable
  String getFirstName ();

  /**
   * @return The last name of the user.
   */
  @Nullable
  String getLastName ();

  /**
   * @return The desired locale of the user.
   */
  @Nullable
  Locale getDesiredLocale ();
}
