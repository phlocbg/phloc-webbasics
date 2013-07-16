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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.RegEx;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;

/**
 * Handles a list of form field specific errors.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class FormFieldErrorList extends AbstractFormErrorList <IFormFieldError> implements IFormFieldErrorList
{
  /**
   * Check if any entry for the specified field is present
   * 
   * @param sSearchFieldName
   *        The field name to search.
   * @return <code>true</code> if an entry for the specified field is present
   */
  public boolean hasEntryForField (@Nullable final String sSearchFieldName)
  {
    if (StringHelper.hasText (sSearchFieldName))
      for (final IFormFieldError aError : m_aItems)
        if (aError.getFieldName ().equals (sSearchFieldName))
          return true;
    return false;
  }

  /**
   * Check if any entry for the specified field and the specified error level is
   * present
   * 
   * @param sSearchFieldName
   *        The field name to search.
   * @param eFormErrorLevel
   *        The form error level to search. May not be <code>null</code>
   * @return <code>true</code> if an entry for the specified field is present
   *         that has exactly the specified form error level
   */
  public boolean hasEntryForField (@Nullable final String sSearchFieldName,
                                   @Nullable final EFormErrorLevel eFormErrorLevel)
  {
    if (StringHelper.hasText (sSearchFieldName) && eFormErrorLevel != null)
      for (final IFormFieldError aError : m_aItems)
        if (aError.getLevel ().equals (eFormErrorLevel) && aError.getFieldName ().equals (sSearchFieldName))
          return true;
    return false;
  }

  @Nonnull
  @ReturnsMutableCopy
  public FormFieldErrorList getListOfField (@Nullable final String sSearchFieldName)
  {
    final FormFieldErrorList ret = new FormFieldErrorList ();
    for (final IFormFieldError aError : m_aItems)
      if (aError.getFieldName ().equals (sSearchFieldName))
        ret.add (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public FormFieldErrorList getListOfFields (@Nullable final String... aSearchFieldNames)
  {
    final FormFieldErrorList ret = new FormFieldErrorList ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IFormFieldError aError : m_aItems)
        if (ArrayHelper.contains (aSearchFieldNames, aError.getFieldName ()))
          ret.add (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public FormFieldErrorList getListOfFieldsStartingWith (@Nullable final String... aSearchFieldNames)
  {
    final FormFieldErrorList ret = new FormFieldErrorList ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IFormFieldError aError : m_aItems)
      {
        final String sErrorFieldName = aError.getFieldName ();
        for (final String sSearchField : aSearchFieldNames)
          if (sErrorFieldName.startsWith (sSearchField))
          {
            ret.add (aError);
            break;
          }
      }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public FormFieldErrorList getListOfFieldsRegExp (@Nonnull @Nonempty @RegEx final String sRegExp)
  {
    if (StringHelper.hasNoText (sRegExp))
      throw new IllegalArgumentException ("Empty RegExp");

    final FormFieldErrorList ret = new FormFieldErrorList ();
    for (final IFormFieldError aError : m_aItems)
      if (RegExHelper.stringMatchesPattern (sRegExp, aError.getFieldName ()))
        ret.add (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllTextsOfField (@Nullable final String sSearchFieldName)
  {
    final List <String> ret = new ArrayList <String> ();
    for (final IFormFieldError aError : m_aItems)
      if (aError.getFieldName ().equals (sSearchFieldName))
        ret.add (aError.getErrorText ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllTextsOfFields (@Nullable final String... aSearchFieldNames)
  {
    final List <String> ret = new ArrayList <String> ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IFormFieldError aError : m_aItems)
        if (ArrayHelper.contains (aSearchFieldNames, aError.getFieldName ()))
          ret.add (aError.getErrorText ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllTextsOfFieldsStartingWith (@Nullable final String... aSearchFieldNames)
  {
    final List <String> ret = new ArrayList <String> ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IFormFieldError aError : m_aItems)
      {
        final String sErrorFieldName = aError.getFieldName ();
        for (final String sSearchField : aSearchFieldNames)
          if (sErrorFieldName.startsWith (sSearchField))
          {
            ret.add (aError.getErrorText ());
            break;
          }
      }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllTextsOfFieldsRegExp (@Nonnull @Nonempty @RegEx final String sRegExp)
  {
    if (StringHelper.hasNoText (sRegExp))
      throw new IllegalArgumentException ("Empty RegExp");

    final List <String> ret = new ArrayList <String> ();
    for (final IFormFieldError aError : m_aItems)
      if (RegExHelper.stringMatchesPattern (sRegExp, aError.getFieldName ()))
        ret.add (aError.getErrorText ());
    return ret;
  }
}
