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

import com.phloc.commons.ICloneable;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.CWebCharset;
import com.phloc.web.port.DefaultNetworkPorts;

/**
 * Writable implementation of the {@link ISMTPSettings} interface.
 * 
 * @author philip
 */
@Immutable
public final class SMTPSettings implements ISMTPSettings, ICloneable <SMTPSettings>
{
  private String m_sHostName;
  private int m_nPort;
  private String m_sUserName;
  private String m_sPassword;
  private String m_sCharset;
  private boolean m_bSSLEnabled;

  /**
   * Constructor which copies settings from another object
   * 
   * @param aOther
   *        The settings to use. May not be <code>null</code>.
   */
  public SMTPSettings (@Nonnull final ISMTPSettings aOther)
  {
    this (aOther.getHostName (),
          aOther.getPort (),
          aOther.getUserName (),
          aOther.getPassword (),
          aOther.getCharset (),
          aOther.isSSLEnabled ());
  }

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
   * @param sHostName
   *        SMTP server name or IP address. May neither be <code>null</code> nor
   *        empty.
   * @param nPort
   *        Port to use. May be <code>-1</code> for the default port.
   * @param sUserName
   *        The username to use. May be <code>null</code>.
   * @param sPassword
   *        The password to use. May be <code>null</code>.
   * @param sCharset
   *        The charset to use. May be <code>null</code>.
   * @param bSSLEnabled
   *        <code>true</code> to enable SSL communications
   */
  public SMTPSettings (@Nonnull final String sHostName,
                       final int nPort,
                       @Nullable final String sUserName,
                       @Nullable final String sPassword,
                       @Nullable final String sCharset,
                       final boolean bSSLEnabled)
  {
    setHostName (sHostName);
    setPort (nPort);
    setUserName (sUserName);
    setPassword (sPassword);
    setCharset (sCharset);
    setSSLEnabled (bSSLEnabled);
  }

  @Nonnull
  public String getHostName ()
  {
    return m_sHostName;
  }

  @Nonnull
  public EChange setHostName (@Nonnull final String sHostName)
  {
    if (sHostName == null)
      throw new NullPointerException ("host");

    if (sHostName.equals (m_sHostName))
      return EChange.UNCHANGED;
    m_sHostName = sHostName;
    return EChange.CHANGED;
  }

  public int getPort ()
  {
    return m_nPort;
  }

  @Nonnull
  public EChange setPort (final int nPort)
  {
    if (nPort != -1 && !DefaultNetworkPorts.isValidPort (nPort))
      throw new IllegalArgumentException ("Port must either be -1 or must be in the valid range!");

    if (nPort == m_nPort)
      return EChange.UNCHANGED;
    m_nPort = nPort;
    return EChange.CHANGED;
  }

  @Nullable
  public String getUserName ()
  {
    return m_sUserName;
  }

  @Nonnull
  public EChange setUserName (@Nullable final String sUserName)
  {
    final String sRealUserName = StringHelper.hasNoText (sUserName) ? null : sUserName;
    if (EqualsUtils.equals (sRealUserName, m_sUserName))
      return EChange.UNCHANGED;
    m_sUserName = sRealUserName;
    return EChange.CHANGED;
  }

  @Nullable
  public String getPassword ()
  {
    return m_sPassword;
  }

  @Nonnull
  public EChange setPassword (@Nullable final String sPassword)
  {
    final String sRealPassword = StringHelper.hasNoText (sPassword) ? null : sPassword;
    if (EqualsUtils.equals (sRealPassword, m_sPassword))
      return EChange.UNCHANGED;
    m_sPassword = sRealPassword;
    return EChange.CHANGED;
  }

  @Nonnull
  public String getCharset ()
  {
    return m_sCharset;
  }

  @Nonnull
  public EChange setCharset (@Nullable final String sCharset)
  {
    final String sRealCharset = StringHelper.hasNoText (sCharset) ? CWebCharset.CHARSET_SMTP : sCharset;
    if (EqualsUtils.equals (sRealCharset, m_sCharset))
      return EChange.UNCHANGED;
    m_sCharset = sRealCharset;
    return EChange.CHANGED;
  }

  public boolean isSSLEnabled ()
  {
    return m_bSSLEnabled;
  }

  @Nonnull
  public EChange setSSLEnabled (final boolean bSSLEnabled)
  {
    if (m_bSSLEnabled == bSSLEnabled)
      return EChange.UNCHANGED;
    m_bSSLEnabled = bSSLEnabled;
    return EChange.CHANGED;
  }

  public boolean areRequiredFieldsSet ()
  {
    return StringHelper.hasText (m_sHostName);
  }

  @Nonnull
  public SMTPSettings getClone ()
  {
    return new SMTPSettings (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (!(o instanceof SMTPSettings))
      return false;
    final SMTPSettings rhs = (SMTPSettings) o;
    return m_sHostName.equals (rhs.m_sHostName) &&
           m_nPort == rhs.m_nPort &&
           EqualsUtils.equals (m_sUserName, rhs.m_sUserName) &&
           EqualsUtils.equals (m_sPassword, rhs.m_sPassword) &&
           m_sCharset.equals (rhs.m_sCharset) &&
           m_bSSLEnabled == rhs.m_bSSLEnabled;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sHostName)
                                       .append (m_nPort)
                                       .append (m_sUserName)
                                       .append (m_sPassword)
                                       .append (m_sCharset)
                                       .append (m_bSSLEnabled)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("hostName", m_sHostName)
                                       .append ("port", m_nPort)
                                       .append ("userName", m_sUserName)
                                       .appendPassword ("password")
                                       .append ("charset", m_sCharset)
                                       .append ("useSSL", m_bSSLEnabled)
                                       .toString ();
  }
}
