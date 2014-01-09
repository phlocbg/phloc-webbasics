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
package com.phloc.appbasics.security.user;

import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.phloc.appbasics.auth.subject.IAuthSubject;
import com.phloc.appbasics.security.CSecurity;
import com.phloc.appbasics.security.password.hash.PasswordHash;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.attrs.IReadonlyAttributeContainer;
import com.phloc.commons.name.IHasDisplayName;
import com.phloc.commons.type.ITypedObject;
import com.phloc.datetime.IHasCreationDateTime;
import com.phloc.datetime.IHasDeletionDateTime;
import com.phloc.datetime.IHasLastModificationDateTime;

/**
 * Interface for a single user
 * 
 * @author Philip Helger
 */
public interface IUser extends ITypedObject <String>, IHasDisplayName, IHasCreationDateTime, IHasLastModificationDateTime, IHasDeletionDateTime, IAuthSubject, IReadonlyAttributeContainer
{
  /**
   * @return <code>true</code> if the user has the ID
   *         {@link CSecurity#USER_ADMINISTRATOR_ID}, <code>false</code>
   *         otherwise
   */
  boolean isAdministrator ();

  /**
   * @return The login name of the user.
   */
  @Nonnull
  @Nonempty
  String getLoginName ();

  /**
   * @return The email address of the user.
   */
  @Nonnull
  @Nonempty
  String getEmailAddress ();

  /**
   * @return The hashed password of the user. Never <code>null</code>.
   */
  @Nonnull
  PasswordHash getPasswordHash ();

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

  /**
   * @return The date time when the user last logged in.
   * @since 2.4.2
   */
  @Nonnull
  DateTime getLastLoginDateTime ();

  /**
   * @return The number of times the user logged in. Always &ge; 0.
   * @since 2.4.2
   */
  @Nonnegative
  int getLoginCount ();

  /**
   * @return The number of consecutive failed logins of this user.
   * @since 2.6.3
   */
  @Nonnegative
  int getConsecutiveFailedLoginCount ();

  /**
   * @return <code>true</code> if this user is deleted, <code>false</code> if it
   *         is active
   */
  boolean isDeleted ();

  /**
   * @return <code>true</code> if this user is enabled, <code>false</code> if it
   *         is disabled
   * @see #isDisabled()
   */
  boolean isEnabled ();

  /**
   * @return <code>true</code> if this user is disabled, <code>false</code> if
   *         it is enabled
   * @see #isEnabled()
   */
  boolean isDisabled ();
}
