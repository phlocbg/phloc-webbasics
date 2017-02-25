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
package com.phloc.web.smtp.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ICloneable;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.serialize.convert.SerializationConverter;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.CWebCharset;
import com.phloc.web.port.DefaultNetworkPorts;
import com.phloc.web.smtp.EmailGlobalSettings;
import com.phloc.web.smtp.ISMTPSettings;

/**
 * Writable implementation of the {@link ISMTPSettings} interface.
 * 
 * @author Philip Helger
 */
@Immutable
public final class SMTPSettings implements ISMTPSettings, ICloneable <SMTPSettings>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SMTPSettings.class);

  private String m_sHostName;
  private int m_nPort;
  private String m_sUserName;
  private String m_sPassword;
  private Charset m_aCharset;
  private boolean m_bSSLEnabled;
  private boolean m_bSTARTTLSEnabled;
  private long m_nConnectionTimeoutMilliSecs;
  private long m_nTimeoutMilliSecs;

  private void writeObject (@Nonnull final ObjectOutputStream aOOS) throws IOException
  {
    aOOS.writeUTF (this.m_sHostName);
    aOOS.writeInt (this.m_nPort);
    aOOS.writeUTF (this.m_sUserName);
    aOOS.writeUTF (this.m_sPassword);
    SerializationConverter.writeConvertedObject (this.m_aCharset, aOOS);
    aOOS.writeBoolean (this.m_bSSLEnabled);
    aOOS.writeBoolean (this.m_bSTARTTLSEnabled);
    aOOS.writeLong (this.m_nConnectionTimeoutMilliSecs);
    aOOS.writeLong (this.m_nTimeoutMilliSecs);
  }

  private void readObject (@Nonnull final ObjectInputStream aOIS) throws IOException
  {
    this.m_sHostName = aOIS.readUTF ();
    this.m_nPort = aOIS.readInt ();
    this.m_sUserName = aOIS.readUTF ();
    this.m_sPassword = aOIS.readUTF ();
    this.m_aCharset = SerializationConverter.readConvertedObject (aOIS, Charset.class);
    this.m_bSSLEnabled = aOIS.readBoolean ();
    this.m_bSTARTTLSEnabled = aOIS.readBoolean ();
    this.m_nConnectionTimeoutMilliSecs = aOIS.readLong ();
    this.m_nTimeoutMilliSecs = aOIS.readLong ();
  }

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
          aOther.isSSLEnabled (),
          aOther.isSTARTTLSEnabled (),
          aOther.getConnectionTimeoutMilliSecs (),
          aOther.getTimeoutMilliSecs ());
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
    this (sHost, -1, null, null, null, EmailGlobalSettings.isUseSSL ());
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
    this (sHostName,
          nPort,
          sUserName,
          sPassword,
          sCharset,
          bSSLEnabled,
          EmailGlobalSettings.isUseSTARTTLS (),
          EmailGlobalSettings.getConnectionTimeoutMilliSecs (),
          EmailGlobalSettings.getTimeoutMilliSecs ());
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
   * @param bSTARTTLSEnabled
   *        <code>true</code> to enable STARTTLS communications
   * @param nConnectionTimeoutMilliSecs
   *        the connection timeout in milliseconds.
   * @param nTimeoutMilliSecs
   *        the socket timeout in milliseconds.
   */
  public SMTPSettings (@Nonnull final String sHostName,
                       final int nPort,
                       @Nullable final String sUserName,
                       @Nullable final String sPassword,
                       @Nullable final String sCharset,
                       final boolean bSSLEnabled,
                       final boolean bSTARTTLSEnabled,
                       final long nConnectionTimeoutMilliSecs,
                       final long nTimeoutMilliSecs)
  {
    setHostName (sHostName);
    setPort (nPort);
    setUserName (sUserName);
    setPassword (sPassword);
    setCharset (sCharset);
    setSSLEnabled (bSSLEnabled);
    setSTARTTLSEnabled (bSTARTTLSEnabled);
    setConnectionTimeoutMilliSecs (nConnectionTimeoutMilliSecs);
    setTimeoutMilliSecs (nTimeoutMilliSecs);
  }

  @Override
  @Nonnull
  public String getHostName ()
  {
    return this.m_sHostName;
  }

  @Nonnull
  public EChange setHostName (@Nonnull final String sHostName)
  {
    if (sHostName == null)
    {
      throw new NullPointerException ("sHostName"); //$NON-NLS-1$
    }
    if (sHostName.equals (this.m_sHostName))
      return EChange.UNCHANGED;
    this.m_sHostName = sHostName;
    return EChange.CHANGED;
  }

  @Override
  public int getPort ()
  {
    return this.m_nPort;
  }

  @Nonnull
  public EChange setPort (final int nPort)
  {
    if (nPort != -1 && !DefaultNetworkPorts.isValidPort (nPort))
      throw new IllegalArgumentException ("Port must either be -1 or must be in the valid range!"); //$NON-NLS-1$

    if (nPort == this.m_nPort)
      return EChange.UNCHANGED;
    this.m_nPort = nPort;
    return EChange.CHANGED;
  }

  @Override
  @Nullable
  public String getUserName ()
  {
    return this.m_sUserName;
  }

  @Nonnull
  public EChange setUserName (@Nullable final String sUserName)
  {
    final String sRealUserName = StringHelper.hasNoText (sUserName) ? null : sUserName;
    if (EqualsUtils.equals (sRealUserName, this.m_sUserName))
      return EChange.UNCHANGED;
    this.m_sUserName = sRealUserName;
    return EChange.CHANGED;
  }

  @Override
  @Nullable
  public String getPassword ()
  {
    return this.m_sPassword;
  }

  @Nonnull
  public EChange setPassword (@Nullable final String sPassword)
  {
    final String sRealPassword = StringHelper.hasNoText (sPassword) ? null : sPassword;
    if (EqualsUtils.equals (sRealPassword, this.m_sPassword))
      return EChange.UNCHANGED;
    this.m_sPassword = sRealPassword;
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  public String getCharset ()
  {
    return this.m_aCharset.name ();
  }

  @Override
  @Nonnull
  public Charset getCharsetObj ()
  {
    return this.m_aCharset;
  }

  @Nonnull
  public EChange setCharset (@Nullable final String sCharset)
  {
    try
    {
      final String sRealCharset = StringHelper.hasNoText (sCharset) ? CWebCharset.CHARSET_SMTP : sCharset;
      final Charset aRealCharset = CharsetManager.getCharsetFromName (sRealCharset);
      if (EqualsUtils.equals (aRealCharset, this.m_aCharset))
        return EChange.UNCHANGED;
      this.m_aCharset = aRealCharset;
      return EChange.CHANGED;
    }
    catch (final IllegalArgumentException ex)
    {
      s_aLogger.error (ex.getMessage ());
      return EChange.UNCHANGED;
    }
  }

  @Override
  public boolean isSSLEnabled ()
  {
    return this.m_bSSLEnabled;
  }

  @Nonnull
  public EChange setSSLEnabled (final boolean bSSLEnabled)
  {
    if (this.m_bSSLEnabled == bSSLEnabled)
      return EChange.UNCHANGED;
    this.m_bSSLEnabled = bSSLEnabled;
    return EChange.CHANGED;
  }

  @Override
  public boolean isSTARTTLSEnabled ()
  {
    return this.m_bSTARTTLSEnabled;
  }

  @Nonnull
  public EChange setSTARTTLSEnabled (final boolean bSTARTTLSEnabled)
  {
    if (this.m_bSTARTTLSEnabled == bSTARTTLSEnabled)
      return EChange.UNCHANGED;
    this.m_bSTARTTLSEnabled = bSTARTTLSEnabled;
    return EChange.CHANGED;
  }

  @Override
  public long getConnectionTimeoutMilliSecs ()
  {
    return this.m_nConnectionTimeoutMilliSecs;
  }

  /**
   * Set the connection timeout in milliseconds. Values &le; 0 are interpreted
   * as indefinite timeout which is not recommended!
   * 
   * @param nMilliSecs
   *        The milliseconds timeout
   * @return {@link EChange}
   */
  @Nonnull
  public EChange setConnectionTimeoutMilliSecs (final long nMilliSecs)
  {
    if (this.m_nConnectionTimeoutMilliSecs == nMilliSecs)
      return EChange.UNCHANGED;
    this.m_nConnectionTimeoutMilliSecs = nMilliSecs;
    return EChange.CHANGED;
  }

  @Override
  public long getTimeoutMilliSecs ()
  {
    return this.m_nTimeoutMilliSecs;
  }

  /**
   * Set the socket timeout in milliseconds. Values &le; 0 are interpreted as
   * indefinite timeout which is not recommended!
   * 
   * @param nMilliSecs
   *        The milliseconds timeout
   * @return {@link EChange}
   */
  @Nonnull
  public EChange setTimeoutMilliSecs (final long nMilliSecs)
  {
    if (this.m_nTimeoutMilliSecs == nMilliSecs)
      return EChange.UNCHANGED;
    this.m_nTimeoutMilliSecs = nMilliSecs;
    return EChange.CHANGED;
  }

  @Override
  public boolean areRequiredFieldsSet ()
  {
    return StringHelper.hasText (this.m_sHostName);
  }

  @Override
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
    return this.m_sHostName.equals (rhs.m_sHostName) &&
           this.m_nPort == rhs.m_nPort &&
           EqualsUtils.equals (this.m_sUserName, rhs.m_sUserName) &&
           EqualsUtils.equals (this.m_sPassword, rhs.m_sPassword) &&
           this.m_aCharset.equals (rhs.m_aCharset) &&
           this.m_bSSLEnabled == rhs.m_bSSLEnabled &&
           this.m_bSTARTTLSEnabled == rhs.m_bSTARTTLSEnabled &&
           this.m_nConnectionTimeoutMilliSecs == rhs.m_nConnectionTimeoutMilliSecs &&
           this.m_nTimeoutMilliSecs == rhs.m_nTimeoutMilliSecs;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_sHostName)
                                       .append (this.m_nPort)
                                       .append (this.m_sUserName)
                                       .append (this.m_sPassword)
                                       .append (this.m_aCharset)
                                       .append (this.m_bSSLEnabled)
                                       .append (this.m_bSTARTTLSEnabled)
                                       .append (this.m_nConnectionTimeoutMilliSecs)
                                       .append (this.m_nTimeoutMilliSecs)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("hostName", this.m_sHostName) //$NON-NLS-1$
                                       .append ("port", this.m_nPort) //$NON-NLS-1$
                                       .append ("userName", this.m_sUserName) //$NON-NLS-1$
                                       .appendPassword ("password") //$NON-NLS-1$
                                       .append ("charset", this.m_aCharset) //$NON-NLS-1$
                                       .append ("SSL", this.m_bSSLEnabled) //$NON-NLS-1$
                                       .append ("STARTTLS", this.m_bSTARTTLSEnabled) //$NON-NLS-1$
                                       .append ("connectionTimeout", this.m_nConnectionTimeoutMilliSecs) //$NON-NLS-1$
                                       .append ("timeout", this.m_nTimeoutMilliSecs) //$NON-NLS-1$
                                       .toString ();
  }
}
