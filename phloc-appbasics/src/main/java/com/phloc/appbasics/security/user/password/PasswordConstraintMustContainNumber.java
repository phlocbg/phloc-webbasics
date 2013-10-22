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
package com.phloc.appbasics.security.user.password;

import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Password constraint defining that at least a certain amount of numbers must
 * be contained
 * 
 * @author Philip Helger
 */
public class PasswordConstraintMustContainNumber implements IPasswordConstraint
{
  private static final String ATTR_MIN_NUMBERS = "minnumbers";

  private final int m_nMinNumbers;

  public PasswordConstraintMustContainNumber (@Nonnegative final int nMinNumbers)
  {
    if (nMinNumbers < 1)
      throw new IllegalArgumentException ("MinNumbers is too small: " + nMinNumbers);
    m_nMinNumbers = nMinNumbers;
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
    return EPasswordConstraintText.DESC_MUST_CONTAIN_NUMBERS.getDisplayTextWithArgs (aContentLocale,
                                                                                     Integer.valueOf (m_nMinNumbers));
  }

  @Nonnull
  public void fillMicroElement (@Nonnull final IMicroElement aElement)
  {
    aElement.setAttribute (ATTR_MIN_NUMBERS, m_nMinNumbers);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("minNumbers", m_nMinNumbers).toString ();
  }
}
