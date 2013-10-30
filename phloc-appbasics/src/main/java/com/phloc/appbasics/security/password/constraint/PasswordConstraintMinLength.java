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
package com.phloc.appbasics.security.password.constraint;

import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Password constraint defining the minimum length (incl.)
 * 
 * @author Philip Helger
 */
public class PasswordConstraintMinLength implements IPasswordConstraint
{
  private static final String ATTR_MIN_LENGTH = "minlength";

  private final int m_nMinLength;

  public PasswordConstraintMinLength (@Nonnegative final int nMinLength)
  {
    if (nMinLength < 1)
      throw new IllegalArgumentException ("MinLength is too small: " + nMinLength);
    m_nMinLength = nMinLength;
  }

  @Nonnegative
  public int getMinLength ()
  {
    return m_nMinLength;
  }

  public boolean isPasswordValid (@Nullable final String sPlainTextPassword)
  {
    return StringHelper.getLength (sPlainTextPassword) >= m_nMinLength;
  }

  @Nullable
  public String getDescription (@Nonnull final Locale aContentLocale)
  {
    return EPasswordConstraintText.DESC_MIN_LENGTH.getDisplayTextWithArgs (aContentLocale,
                                                                           Integer.valueOf (m_nMinLength));
  }

  public void fillMicroElement (@Nonnull final IMicroElement aElement)
  {
    aElement.setAttribute (ATTR_MIN_LENGTH, m_nMinLength);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("minLength", m_nMinLength).toString ();
  }
}
