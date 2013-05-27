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
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.joda.time.DateTime;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
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
  private final String m_sUserID;
  private final DateTime m_aLoginDT;
  private DateTime m_aLastAccessDT;

  public LoginInfo (@Nonnull @Nonempty final String sUserID)
  {
    m_sUserID = sUserID;
    m_aLoginDT = PDTFactory.getCurrentDateTime ();
    m_aLastAccessDT = m_aLoginDT;
  }

  @Nonnull
  @Nonempty
  public String getUserID ()
  {
    return m_sUserID;
  }

  @Nonnull
  public DateTime getLoginDT ()
  {
    return m_aLoginDT;
  }

  @Nullable
  public DateTime getLastAccessDT ()
  {
    return m_aLastAccessDT;
  }

  public void setLastAccessDTNow ()
  {
    m_aLastAccessDT = PDTFactory.getCurrentDateTime ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("userID", m_sUserID)
                            .append ("loginDT", m_aLoginDT)
                            .append ("lastAccessDT", m_aLastAccessDT)
                            .toString ();
  }
}
