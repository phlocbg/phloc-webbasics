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
package com.phloc.web.smtp.queue;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.datetime.PDTFactory;
import com.phloc.web.WebExceptionHelper;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.settings.ESMTPTransportProperty;
import com.phloc.web.smtp.settings.ISMTPSettings;

/**
 * The wrapper around the main javax.mail transport
 * 
 * @author philip
 */
final class MailTransport
{
  private static final IStatisticsHandlerCounter s_aStatsCount = StatisticsManager.getCounterHandler (MailTransport.class);
  private static final Logger s_aLogger = LoggerFactory.getLogger (MailTransport.class);
  private static final String SMTP_PROTOCOL = "smtp";
  private static final String HEADER_MESSAGE_ID = "Message-ID";

  private final ISMTPSettings m_aSettings;
  private final Properties m_aMailProperties = new Properties ();
  private final Session m_aSession;

  public MailTransport (@Nonnull final ISMTPSettings aSettings)
  {
    if (aSettings == null)
      throw new NullPointerException ("settings");

    m_aSettings = aSettings;

    // Check if authentication is required
    // Note: Starkl server requires authentication!
    if (StringHelper.hasText (aSettings.getUserName ()))
      m_aMailProperties.setProperty (ESMTPTransportProperty.AUTH.getSMTPPropertyName (), Boolean.TRUE.toString ());

    // Enable SSL?
    if (aSettings.isSSLEnabled ())
      m_aMailProperties.setProperty (ESMTPTransportProperty.SSL_ENABLE.getSMTPPropertyName (), Boolean.TRUE.toString ());

    // Set connection timeout
    final long nConnectionTimeoutMilliSecs = MailTransportSettings.getConnectTimeoutMilliSecs ();
    if (nConnectionTimeoutMilliSecs > 0)
      m_aMailProperties.setProperty (ESMTPTransportProperty.CONNECTIONTIMEOUT.getSMTPPropertyName (),
                                     Long.toString (nConnectionTimeoutMilliSecs));

    // Set socket timeout
    final long nTimeoutMilliSecs = MailTransportSettings.getTimeoutMilliSecs ();
    if (nTimeoutMilliSecs > 0)
      m_aMailProperties.setProperty (ESMTPTransportProperty.TIMEOUT.getSMTPPropertyName (),
                                     Long.toString (nTimeoutMilliSecs));

    m_aSession = Session.getInstance (m_aMailProperties);

    // Set after eventual properties are set, because in setJavaMailProperties,
    // the session is reset!
    m_aSession.setDebug (GlobalDebug.isDebugMode ());
  }

  @Nonnull
  public ISMTPSettings getSettings ()
  {
    return m_aSettings;
  }

  /**
   * Actually send the given array of MimeMessages via JavaMail.
   * 
   * @param aMessages
   *        Email data objects to send. May be <code>null</code>.
   * @return A non-<code>null</code> map of the failed messages
   */
  @Nonnull
  public Map <IEmailData, MessagingException> send (@Nullable final Collection <IEmailData> aMessages) throws MailSendException
  {
    final Map <IEmailData, MessagingException> aFailedMessages = new LinkedHashMap <IEmailData, MessagingException> ();
    if (aMessages != null)
    {
      try
      {
        final Transport aTransport = m_aSession.getTransport (SMTP_PROTOCOL);
        aTransport.connect (m_aSettings.getHostName (),
                            m_aSettings.getPort (),
                            m_aSettings.getUserName (),
                            m_aSettings.getPassword ());
        try
        {
          // For all messages
          for (final IEmailData aMessage : aMessages)
          {
            try
            {
              // convert from IEmailData to MimeMessage
              final MimeMessage aMimeMessage = new MimeMessage (m_aSession);
              MailConverter.fillMimeMesage (aMimeMessage, aMessage, m_aSettings.getCharset ());

              s_aLogger.info ("Delivering mail to " +
                              Arrays.toString (aMimeMessage.getRecipients (RecipientType.TO)) +
                              " with subject '" +
                              aMimeMessage.getSubject () +
                              "'");

              // Ensure a sent date is present
              if (aMimeMessage.getSentDate () == null)
                aMimeMessage.setSentDate (PDTFactory.getCurrentDateTime ().toDate ());

              final String sMessageID = aMimeMessage.getMessageID ();
              aMimeMessage.saveChanges ();

              if (sMessageID != null)
              {
                // Preserve explicitly specified message id...
                aMimeMessage.setHeader (HEADER_MESSAGE_ID, sMessageID);
              }
              aTransport.sendMessage (aMimeMessage, aMimeMessage.getAllRecipients ());
              s_aStatsCount.increment ();
            }
            catch (final MessagingException ex)
            {
              // Sending exactly THIS messages failed
              aFailedMessages.put (aMessage, ex);
            }
          }
        }
        finally
        {
          try
          {
            aTransport.close ();
          }
          catch (final MessagingException ex)
          {
            throw new MailSendException ("Failed to close mail transport", ex);
          }
        }
      }
      catch (final AuthenticationFailedException ex)
      {
        // problem with the credentials
        throw new MailSendException ("Mail server authentication failed", ex);
      }
      catch (final MessagingException ex)
      {
        if (WebExceptionHelper.isServerNotReachableConnection (ex.getCause ()))
          throw new MailSendException ("Failed to connect to mail server: " + ex.getCause ().getMessage ());
        throw new MailSendException ("Mail server connection failed", ex);
      }
    }
    return aFailedMessages;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MailTransport))
      return false;
    final MailTransport rhs = (MailTransport) o;
    return m_aSettings.equals (rhs.m_aSettings);
  }

  @Override
  public int hashCode ()
  {
    // Compare only settings - session and properties are derived
    return new HashCodeGenerator (this).append (m_aSettings).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("settings", m_aSettings)
                                       .append ("properties", m_aMailProperties)
                                       .append ("session", m_aSession)
                                       .toString ();
  }
}
