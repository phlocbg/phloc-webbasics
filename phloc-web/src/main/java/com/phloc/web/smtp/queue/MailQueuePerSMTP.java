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

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.callback.IThrowingRunnableWithParameter;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.concurrent.collector.ConcurrentCollectorMultiple;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.failed.FailedMailData;
import com.phloc.web.smtp.failed.FailedMailQueue;
import com.phloc.web.smtp.settings.ISMTPSettings;

/**
 * This class collects instances of {@link IEmailData} and tries to transmit
 * them using the specified SMTP settings.
 * 
 * @author philip
 */
final class MailQueuePerSMTP extends ConcurrentCollectorMultiple <IEmailData> implements IThrowingRunnableWithParameter <List <IEmailData>>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MailQueuePerSMTP.class);
  private final MailTransport m_aTransport;
  private FailedMailQueue m_aFailedMailQueue;

  public MailQueuePerSMTP (@Nonnull final ISMTPSettings aSettings, @Nonnull final FailedMailQueue aFailedMailQueue)
  {
    this (DEFAULT_MAX_QUEUE_SIZE, aSettings, aFailedMailQueue);
  }

  public MailQueuePerSMTP (@Nonnegative final int nMaxQueueSize,
                           @Nonnull final ISMTPSettings aSettings,
                           @Nonnull final FailedMailQueue aFailedMailQueue)
  {
    super (nMaxQueueSize, nMaxQueueSize / 2, null);
    if (aSettings == null)
      throw new NullPointerException ("settings");

    m_aTransport = new MailTransport (aSettings);
    setFailedMailQueue (aFailedMailQueue);
    setPerformer (this);
  }

  public void setFailedMailQueue (@Nonnull final FailedMailQueue aFailedMailQueue)
  {
    if (aFailedMailQueue == null)
      throw new NullPointerException ("failedMailQueue");
    m_aFailedMailQueue = aFailedMailQueue;
  }

  /**
   * This is the callback to be invoked everytime something is in the queue.
   * 
   * @param aMessages
   *        The non-null and non-empty list of messages to be send
   */
  public void run (@Nullable final List <IEmailData> aMessages)
  {
    // Expect the worst
    if (ContainerHelper.isNotEmpty (aMessages))
    {
      final ISMTPSettings aSettings = m_aTransport.getSettings ();
      try
      {
        s_aLogger.info ("Sending " + aMessages.size () + " mail messages!");

        // send messages
        final Map <IEmailData, MessagingException> aFailedMessages = m_aTransport.send (aMessages);

        // handle failed messages
        for (final Map.Entry <IEmailData, MessagingException> aEntry : aFailedMessages.entrySet ())
          m_aFailedMailQueue.add (new FailedMailData (aSettings, aEntry.getKey (), aEntry.getValue ()));
      }
      catch (final MailSendException ex)
      {
        s_aLogger.error ("Error sending mail: " + ex.getMessage (), ex.getCause ());

        // mark all mails as failed
        for (final IEmailData aMessage : aMessages)
          m_aFailedMailQueue.add (new FailedMailData (aSettings, aMessage, ex));
      }
      catch (final Throwable ex)
      {
        // No message specific error, but a settings specific error
        s_aLogger.error ("Error sending mail: " + ex.getMessage (), ex.getCause ());
        m_aFailedMailQueue.add (new FailedMailData (aSettings, ex));
      }
    }
  }
}
