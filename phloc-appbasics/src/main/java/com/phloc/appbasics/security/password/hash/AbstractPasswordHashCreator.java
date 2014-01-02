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

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract base class of {@link IPasswordHashCreator}.
 * 
 * @author Philip Helger
 */
public abstract class AbstractPasswordHashCreator implements IPasswordHashCreator
{
  private final String m_sAlgorithm;

  public AbstractPasswordHashCreator (@Nonnull @Nonempty final String sAlgorithm)
  {
    if (StringHelper.hasNoText (sAlgorithm))
      throw new IllegalArgumentException ("algorithm");
    m_sAlgorithm = sAlgorithm;
  }

  @Nonnull
  @Nonempty
  public final String getAlgorithmName ()
  {
    return m_sAlgorithm;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("algorithm", m_sAlgorithm).toString ();
  }
}
