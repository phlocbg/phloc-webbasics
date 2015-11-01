/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.security.password.constraint;

import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Password constraint defining that at least a certain amount of numbers must
 * be contained
 * 
 * @author Philip Helger
 * @since 2.4.2
 * @deprecated Use {@link PasswordConstraintMustContainDigit} instead
 */
@Deprecated
public class PasswordConstraintMustContainNumber implements IPasswordConstraint
{
  private static final String ATTR_MIN_NUMBERS = "minnumbers";

  private final int m_nMinNumbers;

  /**
   * Ctor
   * 
   * @param nMinNumbers
   *        The minimum number of digits that must occur in a password. Must be
   *        &gt; 0.
   */
  public PasswordConstraintMustContainNumber (@Nonnegative final int nMinNumbers)
  {
    m_nMinNumbers = ValueEnforcer.isGT0 (nMinNumbers, "MinNumbers");
  }

  @Nonnegative
  public int getMinNumbers ()
  {
    return m_nMinNumbers;
  }

  public boolean isPasswordValid (@Nullable final String sPlainTextPassword)
  {
    int nNumbers = 0;
    if (sPlainTextPassword != null)
      for (final char c : sPlainTextPassword.toCharArray ())
        if (Character.isDigit (c))
          ++nNumbers;
    return nNumbers >= m_nMinNumbers;
  }

  @Nullable
  public String getDescription (@Nonnull final Locale aContentLocale)
  {
    return EPasswordConstraintText.DESC_MUST_CONTAIN_DIGITS.getDisplayTextWithArgs (aContentLocale,
                                                                                    Integer.valueOf (m_nMinNumbers));
  }

  public void fillMicroElement (@Nonnull final IMicroElement aElement)
  {
    aElement.setAttribute (ATTR_MIN_NUMBERS, m_nMinNumbers);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final PasswordConstraintMustContainNumber rhs = (PasswordConstraintMustContainNumber) o;
    return m_nMinNumbers == rhs.m_nMinNumbers;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_nMinNumbers).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("minNumbers", m_nMinNumbers).toString ();
  }
}
