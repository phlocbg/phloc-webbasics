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
package com.phloc.appbasics.auth.credentials.userpw;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IUserNamePasswordCredentials} interface.
 * 
 * @author philip
 */
@Immutable
public final class UserNamePasswordCredentials implements IUserNamePasswordCredentials
{
  private final String m_sUserName;
  private final String m_sPassword;

  public UserNamePasswordCredentials (@Nullable final String sUserName, @Nullable final String sPassword)
  {
    m_sUserName = sUserName;
    m_sPassword = sPassword;
  }

  @Nullable
  public String getUserName ()
  {
    return m_sUserName;
  }

  @Nullable
  public String getPassword ()
  {
    return m_sPassword;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof UserNamePasswordCredentials))
      return false;
    final UserNamePasswordCredentials rhs = (UserNamePasswordCredentials) o;
    return EqualsUtils.equals (m_sUserName, rhs.m_sUserName) && EqualsUtils.equals (m_sPassword, rhs.m_sPassword);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sUserName).append (m_sPassword).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("userName", m_sUserName).appendPassword ("password").toString ();
  }
}
