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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Contains the description of a single form field error. Default implementation
 * of {@link IFormFieldError}.
 * 
 * @author Philip Helger
 */
@Immutable
public final class FormFieldError implements IFormFieldError
{
  private final EFormErrorLevel m_eLevel;
  private final String m_sFieldName;
  private final String m_sErrorText;

  public FormFieldError (@Nonnull final EFormErrorLevel eLevel,
                         @Nonnull final String sFieldName,
                         @Nonnull final String sErrorText)
  {
    if (eLevel == null)
      throw new NullPointerException ("level");
    if (StringHelper.hasNoText (sFieldName))
      throw new IllegalArgumentException ("fieldName");
    if (StringHelper.hasNoText (sErrorText))
      throw new IllegalArgumentException ("errorText");
    m_eLevel = eLevel;
    m_sFieldName = sFieldName;
    m_sErrorText = sErrorText;
  }

  @Nonnull
  public EFormErrorLevel getLevel ()
  {
    return m_eLevel;
  }

  @Nonnull
  public String getFieldName ()
  {
    return m_sFieldName;
  }

  @Nonnull
  public String getErrorText ()
  {
    return m_sErrorText;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FormFieldError))
      return false;
    final FormFieldError rhs = (FormFieldError) o;
    return m_eLevel.equals (rhs.m_eLevel) &&
           m_sFieldName.equals (rhs.m_sFieldName) &&
           m_sErrorText.equals (rhs.m_sErrorText);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eLevel).append (m_sFieldName).append (m_sErrorText).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("level", m_eLevel)
                                       .append ("fieldName", m_sFieldName)
                                       .append ("errorText", m_sErrorText)
                                       .toString ();
  }
}
