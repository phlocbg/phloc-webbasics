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
package com.phloc.appbasics.security.login;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.joda.time.DateTime;

import com.phloc.appbasics.security.user.IUser;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.datetime.PDTFactory;

/**
 * Represents the information of a single logged in user.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class LoginInfo extends MapBasedAttributeContainer
{
  private final IUser m_aUser;
  private final DateTime m_aLoginDT;
  private DateTime m_aLastAccessDT;

  LoginInfo (@Nonnull final IUser aUser)
  {
    if (aUser == null)
      throw new NullPointerException ("user");
    m_aUser = aUser;
    m_aLoginDT = PDTFactory.getCurrentDateTime ();
    m_aLastAccessDT = m_aLoginDT;
  }

  /**
   * @return The user to which this login info belongs. Never <code>null</code>.
   */
  @Nonnull
  public IUser getUser ()
  {
    return m_aUser;
  }

  /**
   * @return The ID of the user to which this login info belongs. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getUserID ()
  {
    return m_aUser.getID ();
  }

  /**
   * @return The login data and time. Never <code>null</code>.
   */
  @Nonnull
  public DateTime getLoginDT ()
  {
    return m_aLoginDT;
  }

  /**
   * @return The last access data and time. Never <code>null</code>.
   */
  @Nonnull
  public DateTime getLastAccessDT ()
  {
    return m_aLastAccessDT;
  }

  /**
   * Update the last access date time to the current date and time.
   */
  public void setLastAccessDTNow ()
  {
    m_aLastAccessDT = PDTFactory.getCurrentDateTime ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final LoginInfo rhs = (LoginInfo) o;
    return m_aUser.equals (rhs.m_aUser) &&
           m_aLoginDT.equals (rhs.m_aLoginDT) &&
           m_aLastAccessDT.equals (rhs.m_aLastAccessDT);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_aUser)
                            .append (m_aLoginDT)
                            .append (m_aLastAccessDT)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("user", m_aUser)
                            .append ("loginDT", m_aLoginDT)
                            .append ("lastAccessDT", m_aLastAccessDT)
                            .toString ();
  }
}