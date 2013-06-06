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
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;

/**
 * Handles form field specific and form global error messages.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class FormErrors
{
  private final FormErrorList m_aGlobalErrs = new FormErrorList ();
  private final FormFieldErrorList m_aFormFieldErrs = new FormFieldErrorList ();

  public void addGlobalWarning (@Nonnull final String sWarnText)
  {
    m_aGlobalErrs.add (new FormError (EFormErrorLevel.WARN, sWarnText));
  }

  public void addGlobalWarning (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addGlobalWarning (aTextProvider.getText ());
  }

  public void addGlobalError (@Nonnull final String sErrorText)
  {
    m_aGlobalErrs.add (new FormError (EFormErrorLevel.ERROR, sErrorText));
  }

  public void addGlobalError (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addGlobalError (aTextProvider.getText ());
  }

  public void addFieldInfo (@Nonnull final String sFieldName, @Nonnull final String sInfoText)
  {
    m_aFormFieldErrs.add (new FormFieldError (EFormErrorLevel.INFO, sFieldName, sInfoText));
  }

  public void addFieldInfo (@Nonnull final String sFieldName, @Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addFieldInfo (sFieldName, aTextProvider.getText ());
  }

  public void addFieldWarning (@Nonnull final String sFieldName, @Nonnull final String sWarnText)
  {
    m_aFormFieldErrs.add (new FormFieldError (EFormErrorLevel.WARN, sFieldName, sWarnText));
  }

  public void addFieldWarning (@Nonnull final String sFieldName,
                               @Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addFieldWarning (sFieldName, aTextProvider.getText ());
  }

  public void addFieldError (@Nonnull final String sFieldName, @Nonnull final String sErrorText)
  {
    m_aFormFieldErrs.add (new FormFieldError (EFormErrorLevel.ERROR, sFieldName, sErrorText));
  }

  public void addFieldError (@Nonnull final String sFieldName,
                             @Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addFieldError (sFieldName, aTextProvider.getText ());
  }

  public boolean isEmpty ()
  {
    return m_aGlobalErrs.isEmpty () && m_aFormFieldErrs.isEmpty ();
  }

  public boolean hasGlobalErrorsOrWarnings ()
  {
    return m_aGlobalErrs.hasErrorsOrWarnings ();
  }

  public boolean hasFormFieldErrorsOrWarnings ()
  {
    return m_aFormFieldErrs.hasErrorsOrWarnings ();
  }

  public boolean hasErrorsOrWarnings ()
  {
    return m_aGlobalErrs.hasErrorsOrWarnings () || m_aFormFieldErrs.hasErrorsOrWarnings ();
  }

  @Nullable
  public EFormErrorLevel getMostSevereErrorLevel ()
  {
    final EFormErrorLevel ret = m_aGlobalErrs.getMostSevereErrorLevel ();
    final EFormErrorLevel ret2 = m_aFormFieldErrs.getMostSevereErrorLevel ();
    return ret == null ? ret2 : ret2 == null ? ret : ret.isMoreSevereThan (ret2) ? ret : ret2;
  }

  /**
   * @return A non-<code>null</code> list of form global errors.
   */
  @Nonnull
  @ReturnsImmutableObject
  public List <IFormError> getAllGlobalItems ()
  {
    return m_aGlobalErrs.getAllItems ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllGlobalItemTexts ()
  {
    return m_aGlobalErrs.getAllItemTexts ();
  }

  public boolean hasErrorsOrWarningsForField (@Nullable final String sSearchFieldName)
  {
    return m_aFormFieldErrs.hasErrorsOrWarningsForField (sSearchFieldName);
  }

  public boolean hasEntryForField (@Nullable final String sSearchFieldName,
                                   @Nonnull final EFormErrorLevel eFormErrorLevel)
  {
    return m_aFormFieldErrs.hasEntryForField (sSearchFieldName, eFormErrorLevel);
  }

  public boolean hasErrorForField (@Nullable final String sSearchFieldName)
  {
    return hasEntryForField (sSearchFieldName, EFormErrorLevel.ERROR);
  }

  @Nonnull
  @ReturnsMutableCopy
  public IFormFieldErrorList getListOfField (@Nullable final String sSearchFieldName)
  {
    return m_aFormFieldErrs.getListOfField (sSearchFieldName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public IFormFieldErrorList getListOfFields (@Nullable final String... aSearchFieldNames)
  {
    return m_aFormFieldErrs.getListOfFields (aSearchFieldNames);
  }

  @Nonnull
  @ReturnsMutableCopy
  public IFormFieldErrorList getListOfFieldsRegExp (@Nonnull @Nonempty @RegEx final String sRegExp)
  {
    return m_aFormFieldErrs.getListOfFieldsRegExp (sRegExp);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfField (@Nullable final String sSearchFieldName)
  {
    return m_aFormFieldErrs.getAllTextsOfField (sSearchFieldName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfFields (@Nullable final String... aSearchFieldNames)
  {
    return m_aFormFieldErrs.getAllTextsOfFields (aSearchFieldNames);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllTextsOfFieldsRegExp (@Nonnull @Nonempty @RegEx final String sRegExp)
  {
    return m_aFormFieldErrs.getAllTextsOfFieldsRegExp (sRegExp);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <IFormFieldError> getAllFieldItems ()
  {
    return m_aFormFieldErrs.getAllItems ();
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <String> getAllFieldItemTexts ()
  {
    return m_aFormFieldErrs.getAllItemTexts ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FormErrors))
      return false;
    final FormErrors rhs = (FormErrors) o;
    return m_aGlobalErrs.equals (rhs.m_aGlobalErrs) && m_aFormFieldErrs.equals (rhs.m_aFormFieldErrs);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aGlobalErrs).append (m_aFormFieldErrs).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("globalErrors", m_aGlobalErrs)
                                       .append ("formFieldErrors", m_aFormFieldErrs)
                                       .toString ();
  }
}
