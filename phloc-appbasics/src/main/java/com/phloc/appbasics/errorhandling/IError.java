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
package com.phloc.appbasics.errorhandling;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;

/**
 * Base interface for a single error.
 * 
 * @author Philip Helger
 */
public interface IError
{
  /**
   * @return The unique error ID. May be <code>null</code>.
   */
  @Nullable
  String getID ();

  /**
   * @return <code>true</code> if an error ID is present, <code>false</code>
   *         otherwise
   */
  boolean hasID ();

  /**
   * @return The level of this error. May not be <code>null</code>.
   */
  @Nonnull
  EFormErrorLevel getLevel ();

  /**
   * @return The field for which the error occurred. May be <code>null</code>.
   */
  @Nullable
  String getFieldName ();

  /**
   * @return <code>true</code> if a field name is present, <code>false</code>
   *         otherwise
   */
  boolean hasFieldName ();

  /**
   * @return The message of this form error. The error text is always locale
   *         specific because this error is meant to be for a single form
   *         instance represented in a fixed locale. The result may neither be
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  String getText ();
}
