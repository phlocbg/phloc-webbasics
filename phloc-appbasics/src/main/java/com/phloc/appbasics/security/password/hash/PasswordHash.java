/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.appbasics.security.password.hash;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class combines password hash and the used algorithm.
 * 
 * @author Philip Helger
 */
@Immutable
public final class PasswordHash implements Serializable
{
  private final String m_sAlgorithmName;
  private final String m_sPasswordHashValue;

  public PasswordHash (@Nonnull @Nonempty final String sAlgorithmName,
                       @Nonnull @Nonempty final String sPasswordHashValue)
  {
    if (StringHelper.hasNoText (sAlgorithmName))
      throw new IllegalArgumentException ("algorithmName");
    if (StringHelper.hasNoText (sPasswordHashValue))
      throw new IllegalArgumentException ("passwordHashValue");
    m_sAlgorithmName = sAlgorithmName;
    m_sPasswordHashValue = sPasswordHashValue;
  }

  @Nonnull
  @Nonempty
  public String getAlgorithmName ()
  {
    return m_sAlgorithmName;
  }

  @Nonnull
  @Nonempty
  public String getPasswordHashValue ()
  {
    return m_sPasswordHashValue;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof PasswordHash))
      return false;
    final PasswordHash rhs = (PasswordHash) o;
    return m_sAlgorithmName.equals (rhs.m_sAlgorithmName) && m_sPasswordHashValue.equals (rhs.m_sPasswordHashValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sAlgorithmName).append (m_sPasswordHashValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("algorithmName", m_sAlgorithmName)
                                       .append ("passwordHashValue", m_sPasswordHashValue)
                                       .toString ();
  }
}
