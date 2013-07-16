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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.RegEx;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * A simple read only form field error list interface.
 * 
 * @author Philip Helger
 */
public interface IFormFieldErrorList extends IBaseFormErrorList <IFormFieldError>
{
  /**
   * Get a sub-list with all entries for the specified field name
   * 
   * @param sSearchFieldName
   *        The field name to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  IFormFieldErrorList getListOfField (@Nullable String sSearchFieldName);

  /**
   * Get a sub-list with all entries for the specified field names
   * 
   * @param aSearchFieldNames
   *        The field names to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  IFormFieldErrorList getListOfFields (@Nullable String... aSearchFieldNames);

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
  IFormFieldErrorList getListOfFieldsStartingWith (@Nullable String... aSearchFieldNames);

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
  IFormFieldErrorList getListOfFieldsRegExp (@Nonnull @Nonempty @RegEx String sRegExp);

  /**
   * Get a list with all texts for the specified field name.
   * 
   * @param sSearchFieldName
   *        The field name to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllTextsOfField (@Nullable String sSearchFieldName);

  /**
   * Get a list with all texts for the specified field names
   * 
   * @param aSearchFieldNames
   *        The field names to search.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllTextsOfFields (@Nullable String... aSearchFieldNames);

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
  List <String> getAllTextsOfFieldsStartingWith (@Nullable String... aSearchFieldNames);

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
  List <String> getAllTextsOfFieldsRegExp (@Nonnull @Nonempty @RegEx String sRegExp);
}
