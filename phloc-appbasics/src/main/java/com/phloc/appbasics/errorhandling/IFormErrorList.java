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
 * A simple read only form error list interface. For a field specific list look
 * at {@link IFormErrorList}.
 * 
 * @author Philip Helger
 */
public interface IFormErrorList extends Iterable <IFormError>
{
  /**
   * @return <code>true</code> if this list has no items, <code>false</code> if
   *         at least one item is contained
   */
  boolean isEmpty ();

  /**
   * @return The number of contained items. Always &ge; 0.
   */
  @Nonnegative
  int getItemCount ();

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
   * @return An immutable list of all contained entries. Never <code>null</code>
   *         .
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IFormError> getAllItems ();

  /**
   * @return The error texts of all contained {@link IFormError} objects. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTexts ();

  // --- field specific methods ---

  boolean hasEntryForField (@Nullable String sSearchFieldName);

  boolean hasEntryForField (@Nullable String sSearchFieldName, @Nullable EFormErrorLevel eFormErrorLevel);

  /**
   * Get a sub-list with all entries for the specified field name
   * 
   * @param sSearchFieldName
   *        The field name to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  IFormErrorList getListOfField (@Nullable String sSearchFieldName);

  /**
   * Get a sub-list with all entries for the specified field names
   * 
   * @param aSearchFieldNames
   *        The field names to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  IFormErrorList getListOfFields (@Nullable String... aSearchFieldNames);

  /**
   * Get a sub-list with all entries that have field names starting with one of
   * the supplied names.
   * 
   * @param aSearchFieldNames
   *        The field names to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  IFormErrorList getListOfFieldsStartingWith (@Nullable String... aSearchFieldNames);

  /**
   * Get a sub-list with all entries that have field names matching the passed
   * regular expression.
   * 
   * @param sRegExp
   *        The regular expression to compare the entries against.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  IFormErrorList getListOfFieldsRegExp (@Nonnull @Nonempty @RegEx String sRegExp);

  /**
   * Get a list with all texts for the specified field name.
   * 
   * @param sSearchFieldName
   *        The field name to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTextsOfField (@Nullable String sSearchFieldName);

  /**
   * Get a list with all texts for the specified field names
   * 
   * @param aSearchFieldNames
   *        The field names to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTextsOfFields (@Nullable String... aSearchFieldNames);

  /**
   * Get a list with all texts of entries that have field names starting with
   * one of the supplied names.
   * 
   * @param aSearchFieldNames
   *        The field names to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTextsOfFieldsStartingWith (@Nullable String... aSearchFieldNames);

  /**
   * Get a list with all texts of entries that have field names matching the
   * passed regular expression.
   * 
   * @param sRegExp
   *        The regular expression to compare the entries against.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllItemTextsOfFieldsRegExp (@Nonnull @Nonempty @RegEx String sRegExp);

  /**
   * @return A map with all field-specific items mapped from field name to its
   *         occurrences.
   */
  @Nonnull
  @ReturnsMutableCopy
  IMultiMapListBased <String, IFormError> getStructuredByFieldName ();
}
