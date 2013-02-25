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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.vendor.VendorInfo;
import com.phloc.datetime.PDTFactory;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.settings.ISMTPSettings;

/**
 * This class simplifies the task of sending an email.<br>
 * Note: do NOT use directly in pDAF3 - use the ScopedMailAPI instead.
 * 
 * @author philip
 */
@Immutable
public final class MailAPI
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MailAPI.class);
  private static final IStatisticsHandlerCounter s_aQueuedMailHdl = StatisticsManager.getCounterHandler (MailAPI.class.getName () +
                                                                                                         "$mails.queued");

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final Map <ISMTPSettings, MailQueuePerSMTP> s_aQueueCache = new HashMap <ISMTPSettings, MailQueuePerSMTP> ();
  private static final ExecutorService s_aSenderThreadPool = Executors.newCachedThreadPool ();

  private MailAPI ()
  {}

  @Nonnull
  private static MailQueuePerSMTP _getOrCreateMailQueuePerSMTP (@Nonnull final ISMTPSettings aSettings)
  {
    if (aSettings == null)
      throw new NullPointerException ("smtpSettings");
    if (s_aSenderThreadPool.isShutdown ())
      throw new IllegalStateException ("Cannot submit to mailqueues that are already stopped!");

    // get queue per SMTP
    MailQueuePerSMTP aSMTPQueue = s_aQueueCache.get (aSettings);
    if (aSMTPQueue == null)
    {
      // create a new queue
      aSMTPQueue = new MailQueuePerSMTP (aSettings);

      // put queue in cache
      s_aQueueCache.put (aSettings, aSMTPQueue);

      // and start running the queue
      s_aSenderThreadPool.submit (aSMTPQueue);
    }
    return aSMTPQueue;
  }

  /**
   * Queue a single mail.
   * 
   * @param aSMTPSettings
   *        The SMTP settings to be used.
   * @param aMailData
   *        The mail message to queue. May not be <code>null</code>.
   */
  public static void queueMail (@Nonnull final ISMTPSettings aSMTPSettings, @Nonnull final IEmailData aMailData)
  {
    queueMails (aSMTPSettings, ContainerHelper.newList (aMailData));
  }

  /**
   * Queue more than one mail.
   * 
   * @param aSMTPSettings
   *        The SMTP settings to be used.
   * @param aMailDataList
   *        The mail messages to queue. May not be <code>null</code>.
   */
  public static void queueMails (@Nonnull final ISMTPSettings aSMTPSettings,
                                 @Nonnull final Collection <? extends IEmailData> aMailDataList)
  {
    if (aMailDataList == null)
      throw new NullPointerException ("mailDataList");
    if (aMailDataList.isEmpty ())
      throw new IllegalArgumentException ("At least one message has to be supplied!");

    MailQueuePerSMTP aSMTPQueue;
    s_aRWLock.writeLock ().lock ();
    try
    {
      // get queue per SMTP settings
      aSMTPQueue = _getOrCreateMailQueuePerSMTP (aSMTPSettings);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }

    // submit all messages
    for (final IEmailData aMailData : aMailDataList)
    {
      // queue the mail
      s_aQueuedMailHdl.increment ();

      // Do some consistency checks to ensure this particular email can be
      // send
      boolean bCanQueue = true;

      if (aMailData.getFrom () == null)
      {
        s_aLogger.warn ("Mail data has no sender address: " + aMailData);
        bCanQueue = false;
      }

      if (aMailData.getToCount () == 0)
      {
        s_aLogger.warn ("Mail data has no receiver address: " + aMailData);
        bCanQueue = false;
      }

      if (GlobalDebug.isDebugMode ())
      {
        // In the debug version we can only send to phloc addresses!
        for (final IEmailAddress aReceiver : aMailData.getTo ())
          if (!aReceiver.getAddress ().endsWith (VendorInfo.getVendorEmailSuffix ()))
          {
            bCanQueue = false;
            s_aLogger.warn ("Ignoring mail to " + aMailData.getTo ());
            break;
          }
      }

      if (bCanQueue)
      {
        // Uses UTC timezone!
        aMailData.setSentDate (PDTFactory.getCurrentDateTime ());
        aSMTPQueue.queueObject (aMailData);
      }
    }
  }

  @Nonnegative
  public static int getTotalQueueLength ()
  {
    int ret = 0;
    s_aRWLock.readLock ().lock ();
    try
    {
      // count over all queues
      for (final MailQueuePerSMTP aQueue : s_aQueueCache.values ())
        ret += aQueue.getQueueLength ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
    return ret;
  }

  /**
   * Stop taking new mails, and wait until all mails already in the queue are
   * delivered.
   */
  public static void stop ()
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      // Check if the thread pool is already shut down
      if (!s_aSenderThreadPool.isShutdown ())
      {
        int nTotalMailsQueued = 0;
        for (final MailQueuePerSMTP aQueue : s_aQueueCache.values ())
          nTotalMailsQueued += aQueue.getQueueLength ();
        s_aLogger.info ("Stopping central mail queues: " +
                        s_aQueueCache.size () +
                        " queues with " +
                        nTotalMailsQueued +
                        " mails");

        try
        {
          // don't take any more actions
          s_aSenderThreadPool.shutdown ();

          // stop all specific queues
          for (final MailQueuePerSMTP aQueue : s_aQueueCache.values ())
            aQueue.stopQueuingNewObjects ();

          while (!s_aSenderThreadPool.awaitTermination (1, TimeUnit.SECONDS))
          {
            // wait until we're done
          }
        }
        catch (final InterruptedException ex)
        {
          s_aLogger.error ("Error stopping mail queue", ex);
        }
      }
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }
}
