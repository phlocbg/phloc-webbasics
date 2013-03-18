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
package com.phloc.web.http.digestauth;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Credentials for HTTP digest authentication
 * 
 * @author philip
 */
@Immutable
public final class DigestAuthCredentials
{
  private final String m_sUserName;

  /**
   * Create credentials .
   * 
   * @param sUserName
   *        The user name to use. May neither be <code>null</code> nor empty.
   */
  public DigestAuthCredentials (@Nonnull @Nonempty final String sUserName)
  {
    if (StringHelper.hasNoText (sUserName))
      throw new IllegalArgumentException ("UserName may not be empty!");
    m_sUserName = sUserName;
  }

  /**
   * @return The user name. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getUserName ()
  {
    return m_sUserName;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof DigestAuthCredentials))
      return false;
    final DigestAuthCredentials rhs = (DigestAuthCredentials) o;
    return m_sUserName.equals (rhs.m_sUserName);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sUserName).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("userName", m_sUserName).toString ();
  }
}
