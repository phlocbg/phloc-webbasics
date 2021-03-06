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
package com.phloc.appbasics.security.password.hash;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

/**
 * Interface for a password hash creator.
 * 
 * @author Philip Helger
 */
public interface IPasswordHashCreator
{
  /**
   * @return The name of the algorithm used in this creator. May neither be
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  String getAlgorithmName ();

  /**
   * The method to create a message digest hash from a password.
   * 
   * @param sPlainTextPassword
   *        Plain text password. May not be <code>null</code>.
   * @return The String representation of the password hash. Must be valid to
   *         encode in UTF-8.
   */
  @Nonnull
  String createPasswordHash (@Nonnull String sPlainTextPassword);
}
