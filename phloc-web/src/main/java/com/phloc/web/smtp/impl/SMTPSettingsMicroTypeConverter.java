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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ContainsSoftMigration;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringParser;
import com.phloc.web.CWeb;
import com.phloc.web.CWebCharset;
import com.phloc.web.smtp.EmailGlobalSettings;
import com.phloc.web.smtp.ISMTPSettings;

public final class SMTPSettingsMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_HOST = "host"; //$NON-NLS-1$
  private static final String ATTR_HOSTNAME = "hostname"; //$NON-NLS-1$
  private static final String ATTR_PORT = "port"; //$NON-NLS-1$
  private static final String ATTR_USER = "user"; //$NON-NLS-1$
  private static final String ATTR_PASSWORD = "password"; //$NON-NLS-1$
  private static final String ATTR_CHARSET = "charset"; //$NON-NLS-1$
  private static final String ATTR_SSLENABLED = "sslenabled"; //$NON-NLS-1$
  private static final String ATTR_STARTTLSENABLED = "starttlsenabled"; //$NON-NLS-1$
  private static final String ATTR_CONNECTIONTIMEOUT = "connectiontimeout"; //$NON-NLS-1$
  private static final String ATTR_TIMEOUT = "timeout"; //$NON-NLS-1$

  @Nonnull
  public static IMicroElement convertToMicroElement (@Nonnull final ISMTPSettings aSMTPSettings,
                                                     @Nullable final String sNamespaceURI,
                                                     @Nonnull final String sTagName)
  {
    final IMicroElement eSMTPSettings = new MicroElement (sNamespaceURI, sTagName);
    eSMTPSettings.setAttribute (ATTR_HOST, aSMTPSettings.getHostName ());
    eSMTPSettings.setAttribute (ATTR_PORT, Integer.toString (aSMTPSettings.getPort ()));
    eSMTPSettings.setAttribute (ATTR_USER, aSMTPSettings.getUserName ());
    eSMTPSettings.setAttribute (ATTR_PASSWORD, aSMTPSettings.getPassword ());
    eSMTPSettings.setAttribute (ATTR_CHARSET, aSMTPSettings.getCharset ());
    eSMTPSettings.setAttribute (ATTR_SSLENABLED, Boolean.toString (aSMTPSettings.isSSLEnabled ()));
    eSMTPSettings.setAttribute (ATTR_STARTTLSENABLED, Boolean.toString (aSMTPSettings.isSTARTTLSEnabled ()));
    eSMTPSettings.setAttribute (ATTR_CONNECTIONTIMEOUT, aSMTPSettings.getConnectionTimeoutMilliSecs ());
    eSMTPSettings.setAttribute (ATTR_TIMEOUT, aSMTPSettings.getTimeoutMilliSecs ());
    return eSMTPSettings;
  }

  @Override
  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aSource,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull final String sTagName)
  {
    final ISMTPSettings aSMTPSettings = (ISMTPSettings) aSource;
    return convertToMicroElement (aSMTPSettings, sNamespaceURI, sTagName);
  }

  /*
   * The alternative attributes are used to be consistent with old failed mail
   * conversions, as they did the transformation manually!
   */
  @Nonnull
  @ContainsSoftMigration
  public static SMTPSettings convertToSMTPSettings (@Nonnull final IMicroElement eSMTPSettings)
  {
    String sHost = eSMTPSettings.getAttribute (ATTR_HOST);
    if (sHost == null)
      sHost = eSMTPSettings.getAttribute (ATTR_HOSTNAME);

    final String sPort = eSMTPSettings.getAttribute (ATTR_PORT);
    final int nPort = StringParser.parseInt (sPort, CWeb.DEFAULT_PORT_SMTP);

    String sUser = eSMTPSettings.getAttribute (ATTR_USER);
    if (sUser == null)
      sUser = eSMTPSettings.getAttribute ("username"); //$NON-NLS-1$

    final String sPassword = eSMTPSettings.getAttribute (ATTR_PASSWORD);

    String sCharset = eSMTPSettings.getAttribute (ATTR_CHARSET);
    if (sCharset == null)
      sCharset = CWebCharset.CHARSET_SMTP;
    String sSSLEnabled = eSMTPSettings.getAttribute (ATTR_SSLENABLED);

    if (sSSLEnabled == null)
      sSSLEnabled = eSMTPSettings.getAttribute ("usessl"); //$NON-NLS-1$
    final boolean bSSLEnabled = StringParser.parseBool (sSSLEnabled, EmailGlobalSettings.isUseSSL ());

    final String sSTARTTLSEnabled = eSMTPSettings.getAttribute (ATTR_STARTTLSENABLED);
    final boolean bSTARTTLSEnabled = StringParser.parseBool (sSTARTTLSEnabled, EmailGlobalSettings.isUseSTARTTLS ());

    final String sConnectionTimeoutMilliSecs = eSMTPSettings.getAttribute (ATTR_CONNECTIONTIMEOUT);
    final long nConnectionTimeoutMilliSecs = StringParser.parseLong (sConnectionTimeoutMilliSecs,
                                                                     EmailGlobalSettings.getConnectionTimeoutMilliSecs ());

    final String sTimeoutMilliSecs = eSMTPSettings.getAttribute (ATTR_TIMEOUT);
    final long nTimeoutMilliSecs = StringParser.parseLong (sTimeoutMilliSecs,
                                                           EmailGlobalSettings.getTimeoutMilliSecs ());

    return new SMTPSettings (sHost,
                             nPort,
                             sUser,
                             sPassword,
                             sCharset,
                             bSSLEnabled,
                             bSTARTTLSEnabled,
                             nConnectionTimeoutMilliSecs,
                             nTimeoutMilliSecs);
  }

  /*
   * The alternative attributes are used to be consistent with old failed mail
   * conversions, as they did the transformation manually!
   */
  @Override
  @Nonnull
  @ContainsSoftMigration
  public SMTPSettings convertToNative (@Nonnull final IMicroElement eSMTPSettings)
  {
    return convertToSMTPSettings (eSMTPSettings);
  }
}
