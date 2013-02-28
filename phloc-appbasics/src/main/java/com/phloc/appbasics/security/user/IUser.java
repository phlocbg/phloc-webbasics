/**
 * Copyright (C) 2006-2013 phloc systems
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

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.auth.subject.IAuthSubject;
import com.phloc.appbasics.security.CSecurity;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.name.IHasDisplayName;
import com.phloc.commons.type.ITypedObject;
import com.phloc.datetime.IHasCreationDateTime;
import com.phloc.datetime.IHasDeletionDateTime;
import com.phloc.datetime.IHasLastModificationDateTime;

/**
 * Interface for a single user
 * 
 * @author philip
 */
public interface IUser extends ITypedObject <String>, IHasDisplayName, IHasCreationDateTime, IHasLastModificationDateTime, IHasDeletionDateTime, Serializable, IAuthSubject
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

  /**
   * @return A set of custom attributes in an ordered map. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Map <String, String> getCustomAttrs ();

  /**
   * @param sKey
   *        The name of the custom attribute to check. May be <code>null</code>.
   * @return <code>true</code> if an attribute with the given name is contained,
   *         <code>false</code> otherwise
   */
  boolean containsCustomAttr (@Nullable String sKey);

  /**
   * @param sKey
   *        The name of the custom attribute to query. May be <code>null</code>.
   * @return <code>null</code> if no such attribute is present.
   */
  @Nullable
  String getCustomAttrValue (@Nullable String sKey);

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
