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
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.multimap.IMultiMapListBased;
import com.phloc.commons.collections.multimap.MultiLinkedHashMapArrayListBased;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;

/**
 * Handles form field specific and form global error messages centrally.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class FormErrors implements IFormErrors
{
  private final FormErrorList m_aFormGlobalErrs = new FormErrorList ();
  private final FormErrorList m_aFormFieldErrs = new FormErrorList ();

  public FormErrors ()
  {}

  public void addAll (@Nullable final IFormErrors aErrors)
  {
    if (aErrors != null)
    {
      for (final IFormError aFormError : aErrors.getAllGlobalItems ())
        addGlobalItem (aFormError);
      for (final IFormError aFormFieldError : aErrors.getAllFieldItems ())
        addFieldItem (aFormFieldError);
    }
  }

  /**
   * Add a form-global item
   * 
   * @param aFormError
   *        The form error object to add. May not be <code>null</code>.
   */
  public void addGlobalItem (@Nonnull final IFormError aFormError)
  {
    if (aFormError == null)
      throw new NullPointerException ("FormError");
    m_aFormGlobalErrs.addItem (aFormError);
  }

  /**
   * Add a form-global information
   * 
   * @param sText
   *        The text to use. May neither be <code>null</code> nor empty.
   */
  public void addGlobalInfo (@Nonnull @Nonempty final String sText)
  {
    addGlobalItem (new FormError (EFormErrorLevel.INFO, sText));
  }

  /**
   * Add a form-global information
   * 
   * @param aTextProvider
   *        The text provider to use. May not be <code>null</code>.
   */
  public void addGlobalInfo (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addGlobalInfo (aTextProvider.getText ());
  }

  /**
   * Add a form-global warning
   * 
   * @param sText
   *        The text to use. May neither be <code>null</code> nor empty.
   */
  public void addGlobalWarning (@Nonnull @Nonempty final String sText)
  {
    addGlobalItem (new FormError (EFormErrorLevel.WARN, sText));
  }

  /**
   * Add a form-global warning
   * 
   * @param aTextProvider
   *        The text provider to use. May not be <code>null</code>.
   */
  public void addGlobalWarning (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addGlobalWarning (aTextProvider.getText ());
  }

  /**
   * Add a form-global error
   * 
   * @param sText
   *        The text to use. May neither be <code>null</code> nor empty.
   */
  public void addGlobalError (@Nonnull @Nonempty final String sText)
  {
    addGlobalItem (new FormError (EFormErrorLevel.ERROR, sText));
  }

  /**
   * Add a form-global error
   * 
   * @param aTextProvider
   *        The text provider to use. May not be <code>null</code>.
   */
  public void addGlobalError (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addGlobalError (aTextProvider.getText ());
  }

  /**
   * Add a form field specific item
   * 
   * @param aFormFieldError
   *        The form field error object to add. May not be <code>null</code>.
   */
  public void addFieldItem (@Nonnull final IFormError aFormFieldError)
  {
    if (aFormFieldError == null)
      throw new NullPointerException ("FormError");
    m_aFormFieldErrs.addItem (aFormFieldError);
  }

  /**
   * Add a field specific information message.
   * 
   * @param sFieldName
   *        The field name for which the message is to be recorded. May neither
   *        be <code>null</code> nor empty.
   * @param sText
   *        The text to use. May neither be <code>null</code> nor empty.
   */
  public void addFieldInfo (@Nonnull @Nonempty final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    addFieldItem (new FormError (EFormErrorLevel.INFO, sFieldName, sText));
  }

  /**
   * Add a field specific information message.
   * 
   * @param sFieldName
   *        The field name for which the message is to be recorded. May neither
   *        be <code>null</code> nor empty.
   * @param aTextProvider
   *        The text provider to use. May not be <code>null</code>.
   */
  public void addFieldInfo (@Nonnull @Nonempty final String sFieldName,
                            @Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addFieldInfo (sFieldName, aTextProvider.getText ());
  }

  /**
   * Add a field specific warning message.
   * 
   * @param sFieldName
   *        The field name for which the message is to be recorded. May neither
   *        be <code>null</code> nor empty.
   * @param sText
   *        The text to use. May neither be <code>null</code> nor empty.
   */
  public void addFieldWarning (@Nonnull @Nonempty final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    addFieldItem (new FormError (EFormErrorLevel.WARN, sFieldName, sText));
  }

  /**
   * Add a field specific warning message.
   * 
   * @param sFieldName
   *        The field name for which the message is to be recorded. May neither
   *        be <code>null</code> nor empty.
   * @param aTextProvider
   *        The text provider to use. May not be <code>null</code>.
   */
  public void addFieldWarning (@Nonnull @Nonempty final String sFieldName,
                               @Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addFieldWarning (sFieldName, aTextProvider.getText ());
  }

  /**
   * Add a field specific error message.
   * 
   * @param sFieldName
   *        The field name for which the message is to be recorded. May neither
   *        be <code>null</code> nor empty.
   * @param sText
   *        The text to use. May neither be <code>null</code> nor empty.
   */
  public void addFieldError (@Nonnull @Nonempty final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    addFieldItem (new FormError (EFormErrorLevel.ERROR, sFieldName, sText));
  }

  /**
   * Add a field specific error message.
   * 
   * @param sFieldName
   *        The field name for which the message is to be recorded. May neither
   *        be <code>null</code> nor empty.
   * @param aTextProvider
   *        The text provider to use. May not be <code>null</code>.
   */
  public void addFieldError (@Nonnull @Nonempty final String sFieldName,
                             @Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    addFieldError (sFieldName, aTextProvider.getText ());
  }

  public boolean isEmpty ()
  {
    return m_aFormGlobalErrs.isEmpty () && m_aFormFieldErrs.isEmpty ();
  }

  public boolean hasGlobalErrorsOrWarnings ()
  {
    return m_aFormGlobalErrs.hasErrorsOrWarnings ();
  }

  public boolean hasFormFieldErrorsOrWarnings ()
  {
    return m_aFormFieldErrs.hasErrorsOrWarnings ();
  }

  public boolean hasErrorsOrWarnings ()
  {
    return m_aFormGlobalErrs.hasErrorsOrWarnings () || m_aFormFieldErrs.hasErrorsOrWarnings ();
  }

  @Nonnegative
  public int getGlobalItemCount ()
  {
    return m_aFormGlobalErrs.getItemCount ();
  }

  @Nonnegative
  public int getFieldItemCount ()
  {
    return m_aFormFieldErrs.getItemCount ();
  }

  @Nonnegative
  public int getItemCount ()
  {
    return m_aFormGlobalErrs.getItemCount () + m_aFormFieldErrs.getItemCount ();
  }

  @Nullable
  public EFormErrorLevel getMostSevereErrorLevel ()
  {
    final EFormErrorLevel ret = m_aFormGlobalErrs.getMostSevereErrorLevel ();
    final EFormErrorLevel ret2 = m_aFormFieldErrs.getMostSevereErrorLevel ();
    return ret == null ? ret2 : ret2 == null ? ret : ret.isMoreSevereThan (ret2) ? ret : ret2;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IFormError> getAllGlobalItems ()
  {
    return m_aFormGlobalErrs.getAllItems ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllGlobalItemTexts ()
  {
    return m_aFormGlobalErrs.getAllItemTexts ();
  }

  public boolean hasErrorsOrWarningsForField (@Nullable final String sSearchFieldName)
  {
    return m_aFormFieldErrs.hasEntryForField (sSearchFieldName);
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
  public IFormErrorList getListOfField (@Nullable final String sSearchFieldName)
  {
    return m_aFormFieldErrs.getListOfField (sSearchFieldName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public IFormErrorList getListOfFields (@Nullable final String... aSearchFieldNames)
  {
    return m_aFormFieldErrs.getListOfFields (aSearchFieldNames);
  }

  @Nonnull
  @ReturnsMutableCopy
  public IFormErrorList getListOfFieldsStartingWith (@Nullable final String... aSearchFieldNames)
  {
    return m_aFormFieldErrs.getListOfFieldsStartingWith (aSearchFieldNames);
  }

  @Nonnull
  @ReturnsMutableCopy
  public IFormErrorList getListOfFieldsRegExp (@Nonnull @Nonempty @RegEx final String sRegExp)
  {
    return m_aFormFieldErrs.getListOfFieldsRegExp (sRegExp);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfField (@Nullable final String sSearchFieldName)
  {
    return m_aFormFieldErrs.getAllItemTextsOfField (sSearchFieldName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfFields (@Nullable final String... aSearchFieldNames)
  {
    return m_aFormFieldErrs.getAllItemTextsOfFields (aSearchFieldNames);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfFieldsStartingWith (@Nullable final String... aSearchFieldNames)
  {
    return m_aFormFieldErrs.getAllItemTextsOfFieldsStartingWith (aSearchFieldNames);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfFieldsRegExp (@Nonnull @Nonempty @RegEx final String sRegExp)
  {
    return m_aFormFieldErrs.getAllItemTextsOfFieldsRegExp (sRegExp);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IFormError> getAllFieldItems ()
  {
    return m_aFormFieldErrs.getAllItems ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllFieldItemTexts ()
  {
    return m_aFormFieldErrs.getAllItemTexts ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public IMultiMapListBased <String, IFormError> getStructuredByFieldName ()
  {
    final IMultiMapListBased <String, IFormError> ret = new MultiLinkedHashMapArrayListBased <String, IFormError> ();
    for (final IFormError aFormError : m_aFormFieldErrs.getAllItems ())
      ret.putSingle (aFormError.getFieldName (), aFormError);
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FormErrors))
      return false;
    final FormErrors rhs = (FormErrors) o;
    return m_aFormGlobalErrs.equals (rhs.m_aFormGlobalErrs) && m_aFormFieldErrs.equals (rhs.m_aFormFieldErrs);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFormGlobalErrs).append (m_aFormFieldErrs).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("formGlobalErrors", m_aFormGlobalErrs)
                                       .append ("formFieldErrors", m_aFormFieldErrs)
                                       .toString ();
  }
}
