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
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
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
public final class FormError implements IFormError
{
  private final EFormErrorLevel m_eLevel;
  private final String m_sErrorText;

  public FormError (@Nonnull final EFormErrorLevel eLevel, @Nonnull @Nonempty final String sErrorText)
  {
    if (eLevel == null)
      throw new NullPointerException ("level");
    if (StringHelper.hasNoText (sErrorText))
      throw new IllegalArgumentException ("errorText");
    m_eLevel = eLevel;
    m_sErrorText = sErrorText;
  }

  @Nonnull
  public EFormErrorLevel getLevel ()
  {
    return m_eLevel;
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
    return m_eLevel.equals (rhs.m_eLevel) && m_sErrorText.equals (rhs.m_sErrorText);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eLevel).append (m_sErrorText).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("level", m_eLevel).append ("errorText", m_sErrorText).toString ();
  }
}