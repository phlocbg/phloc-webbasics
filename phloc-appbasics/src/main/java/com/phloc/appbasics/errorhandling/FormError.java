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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents an overall form error. Default implementation of
 * {@link IFormError}.
 * 
 * @author Philip Helger
 */
@Immutable
public class FormError implements IFormError
{
  private final String m_sErrorID;
  private final EFormErrorLevel m_eLevel;
  private final String m_sFieldName;
  private final String m_sErrorText;

  public FormError (@Nonnull final EFormErrorLevel eLevel, @Nonnull @Nonempty final String sErrorText)
  {
    this (null, eLevel, null, sErrorText);
  }

  public FormError (@Nonnull final EFormErrorLevel eLevel,
                    @Nullable final String sFieldName,
                    @Nonnull @Nonempty final String sErrorText)
  {
    this (null, eLevel, sFieldName, sErrorText);
  }

  public FormError (@Nullable final String sErrorID,
                    @Nonnull final EFormErrorLevel eLevel,
                    @Nullable final String sFieldName,
                    @Nonnull @Nonempty final String sErrorText)
  {
    if (eLevel == null)
      throw new NullPointerException ("level");
    if (StringHelper.hasNoText (sErrorText))
      throw new IllegalArgumentException ("errorText");
    m_sErrorID = sErrorID;
    m_eLevel = eLevel;
    m_sFieldName = sFieldName;
    m_sErrorText = sErrorText;
  }

  @Nullable
  public String getErrorID ()
  {
    return m_sErrorID;
  }

  public boolean hasErrorID ()
  {
    return StringHelper.hasText (m_sErrorID);
  }

  @Nonnull
  public EFormErrorLevel getLevel ()
  {
    return m_eLevel;
  }

  @Nullable
  public String getFieldName ()
  {
    return m_sFieldName;
  }

  public boolean hasFieldName ()
  {
    return StringHelper.hasText (m_sFieldName);
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
    if (!(o instanceof FormError))
      return false;
    final FormError rhs = (FormError) o;
    return EqualsUtils.equals (m_sErrorID, rhs.m_sErrorID) &&
           m_eLevel.equals (rhs.m_eLevel) &&
           EqualsUtils.equals (m_sFieldName, rhs.m_sFieldName) &&
           m_sErrorText.equals (rhs.m_sErrorText);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sErrorID)
                                       .append (m_eLevel)
                                       .append (m_sFieldName)
                                       .append (m_sErrorText)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("errorID", m_sErrorID)
                                       .append ("level", m_eLevel)
                                       .appendIfNotNull ("fieldName", m_sFieldName)
                                       .append ("errorText", m_sErrorText)
                                       .toString ();
  }
}
