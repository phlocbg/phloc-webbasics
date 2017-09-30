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
package com.phloc.webbasics.ssh.connection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IServerConnectionSettingsPassword}
 * interface.
 * 
 * @author philip
 */
@Immutable
public final class ServerConnectionSettingsPassword extends AbstractServerConnectionSettings implements IServerConnectionSettingsPassword
{
  private final String m_sPassword;
  private final boolean m_bPlain;

  public ServerConnectionSettingsPassword (@Nonnull @Nonempty final String sIP,
                                           @Nonnegative final int nPort,
                                           @Nonnull @Nonempty final String sUserName,
                                           @Nonnull @Nonempty final String sPassword)
  {
    this (sIP, nPort, sUserName, sPassword, true);
  }

  public ServerConnectionSettingsPassword (@Nonnull @Nonempty final String sIP,
                                           @Nonnegative final int nPort,
                                           @Nonnull @Nonempty final String sUserName,
                                           @Nonnull @Nonempty final String sPassword,
                                           @Nonnull @Nonempty final boolean bPlain)
  {
    super (sIP, nPort, sUserName);
    if (StringHelper.hasNoText (sPassword))
      throw new IllegalArgumentException ("password"); //$NON-NLS-1$

    this.m_sPassword = sPassword;
    this.m_bPlain = bPlain;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getPassword ()
  {
    return this.m_sPassword;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).appendPassword ("password").toString (); //$NON-NLS-1$
  }

  @Override
  public boolean isPlainPassword ()
  {
    return this.m_bPlain;
  }
}
