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
package com.phloc.web.smtp.settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.CWebCharset;
import com.phloc.web.port.DefaultNetworkPorts;

/**
 * Default implementation of the {@link ISMTPSettings} interface.
 * 
 * @author philip
 */
@Immutable
public final class SMTPSettings implements ISMTPSettings
{
  private final String m_sHost;
  private final int m_nPort;
  private final String m_sUser;
  private final String m_sPassword;
  private final String m_sCharset;
  private final boolean m_bSSLEnabled;

  /**
   * Constructor with default port, and no authentication
   * 
   * @param sHost
   *        SMTP server name or IP address. May neither be <code>null</code> nor
   *        empty.
   */
  public SMTPSettings (@Nonnull final String sHost)
  {
    this (sHost, -1, null, null, null, false);
  }

  /**
   * Constructor
   * 
   * @param sHost
   *        SMTP server name or IP address. May neither be <code>null</code> nor
   *        empty.
   * @param nPort
   *        Port to use. May be <code>-1</code> for the default port.
   * @param sUser
   *        The username to use. May be <code>null</code>.
   * @param sPassword
   *        The password to use. May be <code>null</code>.
   * @param sCharset
   *        The charset to use. May be <code>null</code>.
   * @param bSSLEnabled
   *        <code>true</code> to enable SSL communications
   */
  public SMTPSettings (@Nonnull final String sHost,
                       final int nPort,
                       @Nullable final String sUser,
                       @Nullable final String sPassword,
                       @Nullable final String sCharset,
                       final boolean bSSLEnabled)
  {
    if (sHost == null)
      throw new NullPointerException ("host");
    if (nPort != -1 && !DefaultNetworkPorts.isValidPort (nPort))
      throw new IllegalArgumentException ("Port must either be -1 or must be in the valid range!");
    m_sHost = sHost;
    m_nPort = nPort;
    m_sUser = StringHelper.hasNoText (sUser) ? null : sUser;
    m_sPassword = StringHelper.hasNoText (sPassword) ? null : sPassword;
    m_sCharset = StringHelper.hasNoText (sCharset) ? CWebCharset.CHARSET_SMTP : sCharset;
    m_bSSLEnabled = bSSLEnabled;
  }

  @Nonnull
  public String getHostName ()
  {
    return m_sHost;
  }

  public int getPort ()
  {
    return m_nPort;
  }

  @Nullable
  public String getUserName ()
  {
    return m_sUser;
  }

  @Nullable
  public String getPassword ()
  {
    return m_sPassword;
  }

  @Nonnull
  public String getCharset ()
  {
    return m_sCharset;
  }

  public boolean isSSLEnabled ()
  {
    return m_bSSLEnabled;
  }

  public boolean areRequiredFieldsSet ()
  {
    return StringHelper.hasText (m_sHost);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (!(o instanceof SMTPSettings))
      return false;
    final SMTPSettings rhs = (SMTPSettings) o;
    return m_sHost.equals (rhs.m_sHost) &&
           m_nPort == rhs.m_nPort &&
           EqualsUtils.equals (m_sUser, rhs.m_sUser) &&
           EqualsUtils.equals (m_sPassword, rhs.m_sPassword) &&
           m_sCharset.equals (rhs.m_sCharset) &&
           m_bSSLEnabled == rhs.m_bSSLEnabled;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sHost)
                                       .append (m_nPort)
                                       .append (m_sUser)
                                       .append (m_sPassword)
                                       .append (m_sCharset)
                                       .append (m_bSSLEnabled)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("host", m_sHost)
                                       .append ("port", m_nPort)
                                       .append ("user", m_sUser)
                                       .appendPassword ("password")
                                       .append ("charset", m_sCharset)
                                       .append ("useSSL", m_bSSLEnabled)
                                       .toString ();
  }
}
