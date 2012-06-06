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

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Interface for a user manager
 * 
 * @author philip
 */
public interface IUserManager
{
  /**
   * Create a new user.
   * 
   * @param sLoginName
   *        Login name of the user. May neither be <code>null</code> nor
   *        empty.This login name must be unique over all existing users.
   * @param sEmailAddress
   *        The email address. May neither be <code>null</code> nor empty.
   * @param sPlainTextPassword
   *        The plain text password to be used. May neither be <code>null</code>
   *        nor empty.
   * @param sFirstName
   *        The users first name. May be <code>null</code>.
   * @param sLastName
   *        The users last name. May be <code>null</code>.
   * @param aDesiredLocale
   *        The users default locale. May be <code>null</code>.
   * @param aCustomAttrs
   *        Custom attributes. May be <code>null</code>.
   * @return The created user or <code>null</code> if another user with the same
   *         email address is already present.
   */
  @Nullable
  IUser createNewUser (@Nonnull @Nonempty String sLoginName,
                       @Nonnull @Nonempty String sEmailAddress,
                       @Nonnull @Nonempty String sPlainTextPassword,
                       @Nullable String sFirstName,
                       @Nullable String sLastName,
                       @Nullable Locale aDesiredLocale,
                       @Nullable Map <String, String> aCustomAttrs);

  /**
   * Create a predefined user.
   * 
   * @param sID
   *        The ID to use
   * @param sLoginName
   *        Login name of the user. May neither be <code>null</code> nor empty.
   *        This login name must be unique over all existing users.
   * @param sEmailAddress
   *        The email address. May neither be <code>null</code> nor empty.
   * @param sPlainTextPassword
   *        The plain text password to be used. May neither be <code>null</code>
   *        nor empty.
   * @param sFirstName
   *        The users first name. May be <code>null</code>.
   * @param sLastName
   *        The users last name. May be <code>null</code>.
   * @param aDesiredLocale
   *        The users default locale. May be <code>null</code>.
   * @param aCustomAttrs
   *        Custom attributes. May be <code>null</code>.
   * @return The created user or <code>null</code> if another user with the same
   *         email address is already present.
   */
  @Nullable
  IUser createPredefinedUser (@Nonnull @Nonempty String sID,
                              @Nonnull @Nonempty String sLoginName,
                              @Nonnull @Nonempty String sEmailAddress,
                              @Nonnull @Nonempty String sPlainTextPassword,
                              @Nullable String sFirstName,
                              @Nullable String sLastName,
                              @Nullable Locale aDesiredLocale,
                              @Nullable Map <String, String> aCustomAttrs);

  /**
   * Delete the user with the specified ID.
   * 
   * @param sUserID
   *        The ID of the user to delete
   * @return {@link EChange#CHANGED} if the user was deleted,
   *         {@link EChange#UNCHANGED} if no such user ID exists.
   */
  @Nonnull
  EChange deleteUser (@Nullable String sUserID);

  /**
   * Change the modifiable data of a user
   * 
   * @param sUserID
   *        The ID of the user to be modified. May be <code>null</code>.
   * @param sNewFirstName
   *        The new first name. May be <code>null</code>.
   * @param sNewLastName
   *        The new last name. May be <code>null</code>.
   * @param aNewDesiredLocale
   *        The new desired locale. May be <code>null</code>.
   * @return {@link EChange}
   */
  @Nonnull
  EChange setUserData (@Nullable String sUserID,
                       @Nullable String sNewFirstName,
                       @Nullable String sNewLastName,
                       @Nullable Locale aNewDesiredLocale);

  /**
   * Check if the passed combination of user ID and password matches.
   * 
   * @param sUserID
   *        The ID of the user
   * @param sPlainTextPassword
   *        The plan text password to validate.
   * @return <code>true</code> if the password hash matches the stored hash for
   *         the specified user, <code>false</code> otherwise.
   */
  boolean areUserIDAndPasswordValid (@Nullable String sUserID, @Nullable String sPlainTextPassword);

  /**
   * Check if a user with the specified ID is present.
   * 
   * @param sUserID
   *        The user ID to resolve. May be <code>null</code>.
   * @return <code>true</code> if such user exists, <code>false</code>
   *         otherwise.
   */
  boolean containsUserWithID (@Nullable String sUserID);

  /**
   * Get the user with the specified ID.
   * 
   * @param sUserID
   *        The user ID to resolve. May be <code>null</code>.
   * @return <code>null</code> if no such user exists
   */
  @Nullable
  IUser getUserOfID (@Nullable String sUserID);

  /**
   * Get the user with the specified email address
   * 
   * @param sLoginName
   *        The email address to be checked. May be <code>null</code>.
   * @return <code>null</code> if no such user exists
   */
  @Nullable
  IUser getUserOfLoginName (@Nullable String sLoginName);

  /**
   * @return A non-<code>null</code> collection of all contained users
   */
  @Nonnull
  @ReturnsMutableCopy
  Collection <? extends IUser> getAllUsers ();
}
