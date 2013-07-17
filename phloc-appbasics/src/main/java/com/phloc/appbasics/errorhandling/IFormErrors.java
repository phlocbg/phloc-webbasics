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

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.RegEx;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.multimap.IMultiMapListBased;

/**
 * Read-only base interface for {@link FormErrors} implementation
 * 
 * @author Philip Helger
 */
public interface IFormErrors
{
  /**
   * Check if no message is contained overall.
   * 
   * @return <code>true</code> if neither a form-global nor a form-field
   *         specific message is present, <code>false</code> otherwise.
   */
  boolean isEmpty ();

  /**
   * @return <code>true</code> if form-global errors or warnings are present.
   */
  boolean hasGlobalErrorsOrWarnings ();

  /**
   * @return <code>true</code> if form-field errors or warnings are present.
   */
  boolean hasFormFieldErrorsOrWarnings ();

  /**
   * @return <code>true</code> if form-global OR form-field errors or warnings
   *         are present.
   */
  boolean hasErrorsOrWarnings ();

  /**
   * @return The number of global items. Always &ge; 0.
   */
  @Nonnegative
  int getGlobalItemCount ();

  /**
   * @return The number of form-field-specific items. Always &ge; 0.
   */
  @Nonnegative
  int getFieldItemCount ();

  /**
   * Get the total number of items for both form-global and form-field-specific
   * items
   * 
   * @return The total item count. Always &ge; 0.
   */
  @Nonnegative
  int getItemCount ();

  /**
   * Get the most severe error level that was recorded. This considers
   * form-global and form-field-specific messages.
   * 
   * @return <code>null</code> if no message was recorded at all, the non-
   *         <code>null</code> {@link EFormErrorLevel} otherwise.
   */
  @Nullable
  EFormErrorLevel getMostSevereErrorLevel ();

  /**
   * @return A non-<code>null</code> list of form global errors.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IFormError> getAllGlobalItems ();

  /**
   * @return A non-<code>null</code> list of all form global error texts.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllGlobalItemTexts ();

  boolean hasErrorsOrWarningsForField (@Nullable String sSearchFieldName);

  boolean hasErrorForField (@Nullable String sSearchFieldName);

  boolean hasEntryForField (@Nullable String sSearchFieldName, @Nonnull EFormErrorLevel eFormErrorLevel);

  @Nonnull
  @ReturnsMutableCopy
  IFormErrorList getListOfField (@Nullable String sSearchFieldName);

  @Nonnull
  @ReturnsMutableCopy
  IFormErrorList getListOfFields (@Nullable String... aSearchFieldNames);

  @Nonnull
  @ReturnsMutableCopy
  IFormErrorList getListOfFieldsStartingWith (@Nullable String... aSearchFieldNames);

  @Nonnull
  @ReturnsMutableCopy
  IFormErrorList getListOfFieldsRegExp (@Nonnull @Nonempty @RegEx String sRegExp);

  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTextsOfField (@Nullable String sSearchFieldName);

  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTextsOfFields (@Nullable String... aSearchFieldNames);

  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTextsOfFieldsStartingWith (@Nullable String... aSearchFieldNames);

  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTextsOfFieldsRegExp (@Nonnull @Nonempty @RegEx String sRegExp);

  @Nonnull
  @ReturnsMutableCopy
  List <IFormError> getAllFieldItems ();

  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllFieldItemTexts ();

  /**
   * @return A map with all field-specific items mapped from field name to its
   *         occurrences.
   */
  @Nonnull
  @ReturnsMutableCopy
  IMultiMapListBased <String, IFormError> getStructuredByFieldName ();
}
