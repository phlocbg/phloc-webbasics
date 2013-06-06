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
  @Nonnull
  @ReturnsMutableCopy
  IFormFieldErrorList getListOfField (@Nullable String sSearchFieldName);

  @Nonnull
  @ReturnsMutableCopy
  IFormFieldErrorList getListOfFields (@Nullable String... aSearchFieldNames);

  @Nonnull
  @ReturnsMutableCopy
  IFormFieldErrorList getListOfFieldsRegExp (@Nonnull @Nonempty @RegEx String sRegExp);

  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllTextsOfField (@Nullable String sSearchFieldName);

  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllTextsOfFields (@Nullable String... aSearchFieldNames);

  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllTextsOfFieldsRegExp (@Nonnull @Nonempty @RegEx String sRegExp);
}
