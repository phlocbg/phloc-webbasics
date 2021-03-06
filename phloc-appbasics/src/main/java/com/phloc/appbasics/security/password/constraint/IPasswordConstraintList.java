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
package com.phloc.appbasics.security.password.constraint;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * This interface defines a list of password constraints.
 * 
 * @author Philip Helger
 */
public interface IPasswordConstraintList extends ICloneable <IPasswordConstraintList>
{
  /**
   * @return <code>true</code> if at least one constraint is present
   */
  boolean hasConstraints ();

  /**
   * @return The number of contained constraints. Always &ge; 0.
   */
  @Nonnegative
  int getConstraintCount ();

  /**
   * @return A list of all contained password constraints. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IPasswordConstraint> getAllPasswordConstraints ();

  /**
   * Check if the passed plaintext password is valid. Breaks after the first
   * unfulfilled constrained
   * 
   * @param sPlainTextPassword
   *        The password to check. May be <code>null</code>.
   * @return <code>true</code> if no constraint was unfulfilled (meaning that if
   *         no constrained is defined, every password is valid).
   */
  boolean isPasswordValid (@Nullable String sPlainTextPassword);

  /**
   * Check if the passed password is valid. The descriptions of all failed
   * constraints are returned.
   * 
   * @param sPlainTextPassword
   *        The password to check. May be <code>null</code>.
   * @param aContentLocale
   *        The content locale to be used to determine the descriptions.
   * @return A non-<code>null</code> but empty list if no constraint was
   *         unfulfilled (meaning that if no constrained is defined, every
   *         password is valid). If the returned list is not empty than the
   *         password is invalid.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getInvalidPasswordDescriptions (@Nullable String sPlainTextPassword, @Nonnull Locale aContentLocale);

  /**
   * Get a list of all password constraint descriptions in the specified locale
   * (e.g. for hinting a user)
   * 
   * @param aContentLocale
   *        The locale to be used for text resolving.
   * @return A non-<code>null</code> list with all constraint descriptions. If
   *         the returned list is empty, it means that no constraint is defined.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllPasswordConstraintDescriptions (@Nonnull Locale aContentLocale);
}
