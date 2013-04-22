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
package com.phloc.webbasics.form.validation;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * A simple read only form error list interface.
 * 
 * @author Philip Helger
 */
public interface IBaseFormErrorList <T extends IFormError>
{
  /**
   * @return <code>true</code> if this list has no items, <code>false</code> if
   *         at least one item is contained
   */
  boolean isEmpty ();

  /**
   * @return <code>true</code> if at least 1 item of level warning or at least 1
   *         item of level error is contained.
   */
  boolean hasErrorsOrWarnings ();

  /**
   * @return The most severe error level contained in the list or
   *         <code>null</code> if the list is empty.
   */
  @Nullable
  EFormErrorLevel getMostSevereErrorLevel ();

  /**
   * @return An immutable list of all contained entries.
   */
  @Nonnull
  @ReturnsImmutableObject
  List <T> getAllItems ();

  /**
   * @return The error texts of all contained {@link IFormError} objects.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTexts ();
}
