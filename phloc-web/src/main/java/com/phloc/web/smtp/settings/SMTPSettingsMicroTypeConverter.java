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

import com.phloc.commons.annotations.ContainsSoftMigration;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringParser;
import com.phloc.web.CWeb;
import com.phloc.web.CWebCharset;

public final class SMTPSettingsMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_HOST = "host";
  private static final String ATTR_PORT = "port";
  private static final String ATTR_USER = "user";
  private static final String ATTR_PASSWORD = "password";
  private static final String ATTR_CHARSET = "charset";
  private static final String ATTR_SSLENABLED = "sslenabled";
  private static final String ATTR_STARTTLSENABLED = "starttlsenabled";
  private static final String ATTR_CONNECTIONTIMEOUT = "connectiontimeout";
  private static final String ATTR_TIMEOUT = "timeout";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aSource,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull final String sTagName)
  {
    final ISMTPSettings aSMTPSettings = (ISMTPSettings) aSource;
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

  /*
   * The alternative attributes are used to be consistent with old failed mail
   * conversions, as they did the transformation manually!
   */
  @Nonnull
  @ContainsSoftMigration
  public SMTPSettings convertToNative (@Nonnull final IMicroElement eSMTPSettings)
  {
    String sHost = eSMTPSettings.getAttribute (ATTR_HOST);
    if (sHost == null)
      sHost = eSMTPSettings.getAttribute ("hostname");

    final String sPort = eSMTPSettings.getAttribute (ATTR_PORT);
    final int nPort = StringParser.parseInt (sPort, CWeb.DEFAULT_PORT_SMTP);

    String sUser = eSMTPSettings.getAttribute (ATTR_USER);
    if (sUser == null)
      sUser = eSMTPSettings.getAttribute ("username");

    final String sPassword = eSMTPSettings.getAttribute (ATTR_PASSWORD);

    String sCharset = eSMTPSettings.getAttribute (ATTR_CHARSET);
    if (sCharset == null)
      sCharset = CWebCharset.CHARSET_SMTP;
    String sSSLEnabled = eSMTPSettings.getAttribute (ATTR_SSLENABLED);

    if (sSSLEnabled == null)
      sSSLEnabled = eSMTPSettings.getAttribute ("usessl");
    final boolean bSSLEnabled = StringParser.parseBool (sSSLEnabled, ISMTPSettings.DEFAULT_SSL_ENABLED);

    final String sSTARTTLSEnabled = eSMTPSettings.getAttribute (ATTR_STARTTLSENABLED);
    final boolean bSTARTTLSEnabled = StringParser.parseBool (sSTARTTLSEnabled, ISMTPSettings.DEFAULT_STARTTLS_ENABLED);

    final String sConnectionTimeoutMilliSecs = eSMTPSettings.getAttribute (ATTR_CONNECTIONTIMEOUT);
    final long nConnectionTimeoutMilliSecs = StringParser.parseLong (sConnectionTimeoutMilliSecs,
                                                                     MailTransportSettings.getConnectionTimeoutMilliSecs ());

    final String sTimeoutMilliSecs = eSMTPSettings.getAttribute (ATTR_TIMEOUT);
    final long nTimeoutMilliSecs = StringParser.parseLong (sTimeoutMilliSecs,
                                                           MailTransportSettings.getTimeoutMilliSecs ());

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
}
