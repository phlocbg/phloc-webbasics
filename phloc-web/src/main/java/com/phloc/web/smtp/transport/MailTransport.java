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
package com.phloc.web.smtp.transport;

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
import javax.mail.event.ConnectionListener;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.datetime.PDTFactory;
import com.phloc.web.WebExceptionHelper;
import com.phloc.web.smtp.EmailGlobalSettings;
import com.phloc.web.smtp.IEMailSendListener;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.IEmailDataTransportListener;
import com.phloc.web.smtp.ISMTPSettings;
import com.sun.mail.smtp.SMTPAddressSucceededException;
import com.sun.mail.smtp.SMTPTransport;

/**
 * The wrapper around the main javax.mail transport
 * 
 * @author Philip Helger
 */
final class MailTransport
{
  public static final String SMTP_PROTOCOL = "smtp";
  public static final String SMTPS_PROTOCOL = "smtps";

  private static final IStatisticsHandlerCounter s_aStatsCountSuccess = StatisticsManager.getCounterHandler (MailTransport.class);
  private static final IStatisticsHandlerCounter s_aStatsCountFailed = StatisticsManager.getCounterHandler (MailTransport.class.getName () +
                                                                                                            "$failed");
  private static final Logger LOG = LoggerFactory.getLogger (MailTransport.class);
  private static final String HEADER_MESSAGE_ID = "Message-ID";

  private final ISMTPSettings m_aSMTPSettings;
  private final boolean m_bSMTPS;
  private final Properties m_aMailProperties = new Properties ();
  private final Session m_aSession;

  @SuppressWarnings ("unused")
  public MailTransport (@Nonnull final ISMTPSettings aSettings)
  {
    ValueEnforcer.notNull (aSettings, "Settings");

    this.m_aSMTPSettings = aSettings;
    this.m_bSMTPS = aSettings.isSSLEnabled () || aSettings.isSTARTTLSEnabled ();

    // Enable SSL?
    if (aSettings.isSSLEnabled ())
      this.m_aMailProperties.setProperty (ESMTPTransportProperty.SSL_ENABLE.getPropertyName (this.m_bSMTPS),
                                          Boolean.TRUE.toString ());

    // Check if authentication is required
    if (StringHelper.hasText (aSettings.getUserName ()))
      this.m_aMailProperties.setProperty (ESMTPTransportProperty.AUTH.getPropertyName (this.m_bSMTPS),
                                          Boolean.TRUE.toString ());

    // Enable STARTTLS?
    if (aSettings.isSTARTTLSEnabled ())
      this.m_aMailProperties.setProperty (ESMTPTransportProperty.STARTTLS_ENABLE.getPropertyName (this.m_bSMTPS),
                                          Boolean.TRUE.toString ());

    if (this.m_bSMTPS)
    {
      this.m_aMailProperties.setProperty (ESMTPTransportProperty.SSL_SOCKETFACTORY_CLASS.getPropertyName (this.m_bSMTPS),
                                          SSLSocketFactory.class.getName ());
      this.m_aMailProperties.setProperty (ESMTPTransportProperty.SSL_SOCKETFACTORY_PORT.getPropertyName (this.m_bSMTPS),
                                          Integer.toString (aSettings.getPort ()));
    }

    // Set connection timeout
    final long nConnectionTimeoutMilliSecs = aSettings.getConnectionTimeoutMilliSecs ();
    if (nConnectionTimeoutMilliSecs > 0)
      this.m_aMailProperties.setProperty (ESMTPTransportProperty.CONNECTIONTIMEOUT.getPropertyName (this.m_bSMTPS),
                                          Long.toString (nConnectionTimeoutMilliSecs));

    // Set socket timeout
    final long nTimeoutMilliSecs = aSettings.getTimeoutMilliSecs ();
    if (nTimeoutMilliSecs > 0)
      this.m_aMailProperties.setProperty (ESMTPTransportProperty.TIMEOUT.getPropertyName (this.m_bSMTPS),
                                          Long.toString (nTimeoutMilliSecs));

    if (false)
      this.m_aMailProperties.setProperty (ESMTPTransportProperty.REPORTSUCCESS.getPropertyName (this.m_bSMTPS),
                                          Boolean.TRUE.toString ());

    // Debug flag
    this.m_aMailProperties.setProperty ("mail.debug.auth", Boolean.toString (GlobalDebug.isDebugMode ()));

    if (LOG.isDebugEnabled ())
      LOG.debug ("Mail properties: " + this.m_aMailProperties);

    // Create session based on properties
    this.m_aSession = Session.getInstance (this.m_aMailProperties);

    // Set after eventual properties are set, because in setJavaMailProperties,
    // the session is reset!
    this.m_aSession.setDebug (GlobalDebug.isDebugMode ());
  }

  @Nonnull
  public ISMTPSettings getSMTPSettings ()
  {
    return this.m_aSMTPSettings;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <Object, Object> getMailProperties ()
  {
    return ContainerHelper.newMap (this.m_aMailProperties);
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
      final IEMailSendListener aEmailSendListener = EmailGlobalSettings.getEmailSendListener ();
      try
      {
        final Transport aTransport = this.m_aSession.getTransport (this.m_bSMTPS ? SMTPS_PROTOCOL : SMTP_PROTOCOL);

        // Add global listeners (if present)
        final ConnectionListener aConnectionListener = EmailGlobalSettings.getConnectionListener ();
        if (aConnectionListener != null)
          aTransport.addConnectionListener (aConnectionListener);

        final TransportListener aGlobalTransportListener = EmailGlobalSettings.getTransportListener ();

        final IEmailDataTransportListener aEmailDataTransportListener = EmailGlobalSettings.getEmailDataTransportListener ();
        if (aGlobalTransportListener != null && aEmailDataTransportListener == null)
        {
          // Set only the global transport listener
          aTransport.addTransportListener (aGlobalTransportListener);
        }
        if (aTransport instanceof SMTPTransport)
        {
          ((SMTPTransport) aTransport).setReportSuccess (EmailGlobalSettings.isReportSuccess ());
        }

        // Connect
        aTransport.connect (this.m_aSMTPSettings.getHostName (),
                            this.m_aSMTPSettings.getPort (),
                            this.m_aSMTPSettings.getUserName (),
                            this.m_aSMTPSettings.getPassword ());

        try
        {
          // For all messages
          for (final IEmailData aMessage : aMessages)
          {
            TransportListener aPerMailListener = null;
            try
            {
              // Set email data specific listeners
              if (aEmailDataTransportListener != null)
              {
                aPerMailListener = new TransportListener ()
                {
                  @Override
                  public void messageDelivered (@Nonnull final TransportEvent aEvent)
                  {
                    aEmailDataTransportListener.messageDelivered (MailTransport.this.m_aSMTPSettings, aMessage, aEvent);
                    if (aGlobalTransportListener != null)
                      aGlobalTransportListener.messageDelivered (aEvent);
                  }

                  @Override
                  public void messageNotDelivered (@Nonnull final TransportEvent aEvent)
                  {
                    aEmailDataTransportListener.messageNotDelivered (MailTransport.this.m_aSMTPSettings,
                                                                     aMessage,
                                                                     aEvent);
                    if (aGlobalTransportListener != null)
                      aGlobalTransportListener.messageNotDelivered (aEvent);
                  }

                  @Override
                  public void messagePartiallyDelivered (@Nonnull final TransportEvent aEvent)
                  {
                    aEmailDataTransportListener.messagePartiallyDelivered (MailTransport.this.m_aSMTPSettings,
                                                                           aMessage,
                                                                           aEvent);
                    if (aGlobalTransportListener != null)
                      aGlobalTransportListener.messagePartiallyDelivered (aEvent);
                  }
                };
                aTransport.addTransportListener (aPerMailListener);
              }

              // convert from IEmailData to MimeMessage
              final MimeMessage aMimeMessage = new MimeMessage (this.m_aSession);
              MailConverter.fillMimeMesage (aMimeMessage, aMessage, this.m_aSMTPSettings.getCharsetObj ());

              // Ensure a sent date is present
              if (aMimeMessage.getSentDate () == null)
                aMimeMessage.setSentDate (PDTFactory.getCurrentDateTime ().toDate ());

              // Get an explicitly specified message ID
              final String sMessageID = aMimeMessage.getMessageID ();

              // This creates a new message ID (besides other things)
              aMimeMessage.saveChanges ();

              if (sMessageID != null)
              {
                // Preserve explicitly specified message id...
                aMimeMessage.setHeader (HEADER_MESSAGE_ID, sMessageID);
              }

              LOG.info ("Delivering mail to {} with subject '{}' and message ID '{}'",
                        Arrays.toString (aMimeMessage.getRecipients (RecipientType.TO)),
                        aMimeMessage.getSubject (),
                        aMimeMessage.getMessageID ());

              // Start transmitting
              aTransport.sendMessage (aMimeMessage, aMimeMessage.getAllRecipients ());
              s_aStatsCountSuccess.increment ();
              aEmailSendListener.onSuccess (aMessage);
            }
            catch (final SMTPAddressSucceededException aEx)
            {
              s_aStatsCountSuccess.increment ();
              aEmailSendListener.onSuccess (aMessage);
            }
            catch (final MessagingException aEx)
            {
              s_aStatsCountFailed.increment ();
              aEmailSendListener.onError (aMessage, aEx);
              // Sending exactly THIS messages failed
              aFailedMessages.put (aMessage, aEx);
            }
            finally
            {
              // Remove per-mail listener again
              if (aPerMailListener != null)
                aTransport.removeTransportListener (aPerMailListener);
            }
          }
        }
        finally
        {
          try
          {
            aTransport.close ();
          }
          catch (final MessagingException aEx)
          {
            throw new MailSendException ("Failed to close mail transport", aEx);
          }
        }
      }
      catch (final AuthenticationFailedException aEx)
      {
        // problem with the credentials
        if (aEmailSendListener != null)
        {
          for (final IEmailData aMessage : aMessages)
          {
            aEmailSendListener.onError (aMessage, aEx);
          }
        }
        throw new MailSendException ("Mail server authentication failed", aEx);
      }
      catch (final MessagingException aEx)
      {
        if (aEmailSendListener != null)
        {
          for (final IEmailData aMessage : aMessages)
          {
            aEmailSendListener.onError (aMessage, aEx);
          }
        }
        if (WebExceptionHelper.isServerNotReachableConnection (aEx.getCause ()))
        {
          throw new MailSendException ("Failed to connect to mail server: " + aEx.getCause ().getMessage ());
        }
        throw new MailSendException ("Mail server connection failed", aEx);
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
    return this.m_aSMTPSettings.equals (rhs.m_aSMTPSettings);
  }

  @Override
  public int hashCode ()
  {
    // Compare only settings - session and properties are derived
    return new HashCodeGenerator (this).append (this.m_aSMTPSettings).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("settings", this.m_aSMTPSettings)
                                       .append ("properties", this.m_aMailProperties)
                                       .append ("session", this.m_aSession)
                                       .toString ();
  }
}
