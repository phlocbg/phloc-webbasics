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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.string.StringHelper;

/**
 * Handles form field specific error messages.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class FormFieldErrorList extends AbstractFormErrorList <IFormFieldError> implements IFormFieldErrorList
{
  public boolean hasErrorsOrWarningsForField (@Nullable final String sSearchFieldName)
  {
    if (StringHelper.hasText (sSearchFieldName))
      for (final IFormFieldError aError : m_aItems)
        if (aError.getFieldName ().equals (sSearchFieldName))
          return true;
    return false;
  }

  public boolean hasEntryForField (@Nullable final String sSearchFieldName,
                                   @Nonnull final EFormErrorLevel eFormErrorLevel)
  {
    if (StringHelper.hasText (sSearchFieldName))
      for (final IFormFieldError aError : m_aItems)
        if (aError.getLevel ().equals (eFormErrorLevel) && aError.getFieldName ().equals (sSearchFieldName))
          return true;
    return false;
  }

  @Nonnull
  @ReturnsMutableCopy
  public IFormFieldErrorList getListOfField (@Nullable final String sSearchFieldName)
  {
    final FormFieldErrorList ret = new FormFieldErrorList ();
    for (final IFormFieldError aError : m_aItems)
      if (aError.getFieldName ().equals (sSearchFieldName))
        ret.add (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public IFormFieldErrorList getListOfFields (@Nullable final String... aSearchFieldNames)
  {
    final FormFieldErrorList ret = new FormFieldErrorList ();
    if (aSearchFieldNames != null)
      for (final IFormFieldError aError : m_aItems)
        if (ArrayHelper.contains (aSearchFieldNames, aError.getFieldName ()))
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
    if (aSearchFieldNames != null)
      for (final IFormFieldError aError : m_aItems)
        if (ArrayHelper.contains (aSearchFieldNames, aError.getFieldName ()))
          ret.add (aError.getErrorText ());
    return ret;
  }
}
