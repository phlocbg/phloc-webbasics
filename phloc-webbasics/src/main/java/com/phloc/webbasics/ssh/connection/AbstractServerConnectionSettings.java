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
package com.phloc.webbasics.ssh.connection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.port.DefaultNetworkPorts;

/**
 * Default abstract implementation of the {@link IBaseServerConnectionSettings}
 * interface.
 * 
 * @author philip
 */
@Immutable
public abstract class AbstractServerConnectionSettings implements IBaseServerConnectionSettings
{
  private final String m_sIP;
  private final int m_nPort;
  private final String m_sUserName;

  public AbstractServerConnectionSettings (@Nonnull @Nonempty final String sIP,
                                           @Nonnegative final int nPort,
                                           @Nonnull @Nonempty final String sUserName)
  {
    if (StringHelper.hasNoText (sIP))
      throw new IllegalArgumentException ("IP");
    if (!DefaultNetworkPorts.isValidPort (nPort))
      throw new IllegalArgumentException ("port is invalid: " + nPort);
    if (StringHelper.hasNoText (sUserName))
      throw new IllegalArgumentException ("username");

    m_sIP = sIP;
    m_nPort = nPort;
    m_sUserName = sUserName;
  }

  @Nonnull
  @Nonempty
  public final String getServerAddress ()
  {
    return m_sIP;
  }

  @Nonnegative
  public final int getServerPort ()
  {
    return m_nPort;
  }

  @Nonnull
  @Nonempty
  public final String getUserName ()
  {
    return m_sUserName;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("IP", m_sIP)
                                       .append ("port", m_nPort)
                                       .append ("username", m_sUserName)
                                       .toString ();
  }
}
