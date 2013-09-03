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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.concurrent.ExtendedDefaultThreadFactory;
import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.vendor.VendorInfo;
import com.phloc.datetime.PDTFactory;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.failed.FailedMailQueue;
import com.phloc.web.smtp.settings.ISMTPSettings;
import com.phloc.web.smtp.settings.ReadonlySMTPSettings;

/**
 * This class simplifies the task of sending an email.<br>
 * Note: do NOT use directly in pDAF3 - use the ScopedMailAPI instead.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class MailAPI
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MailAPI.class);
  private static final IStatisticsHandlerCounter s_aQueuedMailHdl = StatisticsManager.getCounterHandler (MailAPI.class.getName () +
                                                                                                         "$mails.queued");

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final Map <ISMTPSettings, MailQueuePerSMTP> s_aQueueCache = new HashMap <ISMTPSettings, MailQueuePerSMTP> ();
  // Just to have custom named threads....
  private static final ThreadFactory s_aThreadFactory = new ExtendedDefaultThreadFactory ("MailAPI");
  private static final ExecutorService s_aSenderThreadPool = new ThreadPoolExecutor (0,
                                                                                     Integer.MAX_VALUE,
                                                                                     60L,
                                                                                     TimeUnit.SECONDS,
                                                                                     new SynchronousQueue <Runnable> (),
                                                                                     s_aThreadFactory);
  private static FailedMailQueue s_aFailedMailQueue = new FailedMailQueue ();
  private static final int s_nMaxMailQueueLen = 500;

  private MailAPI ()
  {}

  /**
   * @return The current failed mail queue. Never <code>null</code>.
   */
  @Nonnull
  public static FailedMailQueue getFailedMailQueue ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aFailedMailQueue;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set a new global failed mail queue. Updates all existing queues.
   * 
   * @param aFailedMailQueue
   *        The new failed mail queue to set. May not be <code>null</code>.
   */
  public static void setFailedMailQueue (@Nonnull final FailedMailQueue aFailedMailQueue)
  {
    if (aFailedMailQueue == null)
      throw new NullPointerException ("FailedMailQueue");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aFailedMailQueue = aFailedMailQueue;

      // Update all existing queues
      for (final MailQueuePerSMTP aMailQueue : s_aQueueCache.values ())
        aMailQueue.setFailedMailQueue (aFailedMailQueue);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }

    s_aLogger.info ("Set FailedMailQueue to " + aFailedMailQueue);
  }

  @Nonnull
  private static MailQueuePerSMTP _getOrCreateMailQueuePerSMTP (@Nonnull final ISMTPSettings aSMTPSettings)
  {
    if (aSMTPSettings == null)
      throw new NullPointerException ("smtpSettings");
    if (s_aSenderThreadPool.isShutdown ())
      throw new IllegalStateException ("Cannot submit to mailqueues that are already stopped!");

    // Ensure that always the same type is used!
    final ReadonlySMTPSettings aRealSMTPSettings = new ReadonlySMTPSettings (aSMTPSettings);

    // get queue per SMTP
    MailQueuePerSMTP aSMTPQueue = s_aQueueCache.get (aRealSMTPSettings);
    if (aSMTPQueue == null)
    {
      // create a new queue
      aSMTPQueue = new MailQueuePerSMTP (s_nMaxMailQueueLen, aRealSMTPSettings, s_aFailedMailQueue);

      // put queue in cache
      s_aQueueCache.put (aRealSMTPSettings, aSMTPQueue);

      // and start running the queue
      s_aSenderThreadPool.submit (aSMTPQueue);
    }
    return aSMTPQueue;
  }

  public static boolean hasNonVendorEmailAddress (@Nullable final List <? extends IEmailAddress> aAddresses)
  {
    if (aAddresses != null)
    {
      final String sVendorEmailSuffix = VendorInfo.getVendorEmailSuffix ();
      for (final IEmailAddress aAddress : aAddresses)
        if (!aAddress.getAddress ().endsWith (sVendorEmailSuffix))
          return true;
    }
    return false;
  }

  /**
   * Queue a single mail.
   * 
   * @param aSMTPSettings
   *        The SMTP settings to be used.
   * @param aMailData
   *        The mail message to queue. May not be <code>null</code>.
   * @return {@link ESuccess}.
   */
  @Nonnull
  public static ESuccess queueMail (@Nonnull final ISMTPSettings aSMTPSettings, @Nonnull final IEmailData aMailData)
  {
    final int nQueuedMails = queueMails (aSMTPSettings, ContainerHelper.newList (aMailData));
    return ESuccess.valueOf (nQueuedMails == 1);
  }

  /**
   * Queue more than one mail.
   * 
   * @param aSMTPSettings
   *        The SMTP settings to be used.
   * @param aMailDataList
   *        The mail messages to queue. May not be <code>null</code>.
   * @return The number of queued emails. Always &ge; 0. Maximum value is the
   *         number of {@link IEmailData} objects in the argument.
   */
  @Nonnegative
  public static int queueMails (@Nonnull final ISMTPSettings aSMTPSettings,
                                @Nonnull final Collection <? extends IEmailData> aMailDataList)
  {
    if (aSMTPSettings == null)
      throw new NullPointerException ("smtpSettings");
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

    int nQueuedMails = 0;
    final boolean bSendVendorOnlyMails = GlobalDebug.isDebugMode ();

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
        s_aLogger.error ("Mail data has no sender address: " + aMailData + " - not queuing!");
        bCanQueue = false;
      }

      if (aMailData.getToCount () == 0)
      {
        s_aLogger.error ("Mail data has no receiver address: " + aMailData + " - not queuing!");
        bCanQueue = false;
      }

      if (StringHelper.hasNoText (aMailData.getSubject ()))
        s_aLogger.warn ("Mail data has no subject: " + aMailData);

      if (StringHelper.hasNoText (aMailData.getBody ()))
        s_aLogger.warn ("Mail data has no body: " + aMailData);

      if (bSendVendorOnlyMails)
      {
        // In the debug version we can only send to vendor addresses!
        if (hasNonVendorEmailAddress (aMailData.getTo ()) ||
            hasNonVendorEmailAddress (aMailData.getCc ()) ||
            hasNonVendorEmailAddress (aMailData.getBcc ()))
        {
          bCanQueue = false;
          s_aLogger.warn ("Debug mode: ignoring mail TO '" +
                          aMailData.getTo () +
                          "'" +
                          (aMailData.getCcCount () > 0 ? " and CC '" + aMailData.getCc () + "'" : "") +
                          (aMailData.getBccCount () > 0 ? " and BCC '" + aMailData.getBcc () + "'" : "") +
                          " because at least one address is not targeted to the vendor domain");
          break;
        }
      }

      if (bCanQueue)
      {
        if (bSendVendorOnlyMails)
        {
          aMailData.setSubject ("[DEBUG] " + aMailData.getSubject ());
          s_aLogger.info ("Sending only-to-vendor mail in debug version:\n" + aSMTPSettings + "\n" + aMailData);
        }

        // Uses UTC timezone!
        aMailData.setSentDate (PDTFactory.getCurrentDateTime ());
        if (aSMTPQueue.queueObject (aMailData).isSuccess ())
          ++nQueuedMails;
        // else an error message was already logged
      }
    }
    return nQueuedMails;
  }

  @Nonnegative
  private static int _getTotalQueueLength ()
  {
    int ret = 0;
    // count over all queues
    for (final MailQueuePerSMTP aQueue : s_aQueueCache.values ())
      ret += aQueue.getQueueLength ();
    return ret;
  }

  @Nonnegative
  public static int getTotalQueueLength ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return _getTotalQueueLength ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Stop taking new mails, and wait until all mails already in the queue are
   * delivered.
   * 
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange stop ()
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      // Check if the thread pool is already shut down
      if (s_aSenderThreadPool.isShutdown ())
        return EChange.UNCHANGED;

      // don't take any more actions
      s_aSenderThreadPool.shutdown ();

      // stop all specific queues
      for (final MailQueuePerSMTP aQueue : s_aQueueCache.values ())
        aQueue.stopQueuingNewObjects ();

      final int nQueues = s_aQueueCache.size ();
      final int nQueueLength = _getTotalQueueLength ();
      if (nQueues > 0 || nQueueLength > 0)
        s_aLogger.info ("Stopping central mail queues: " +
                        nQueues +
                        " queue" +
                        (nQueues == 1 ? "" : "s") +
                        " with " +
                        nQueueLength +
                        " mail" +
                        (nQueueLength == 1 ? "" : "s"));
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }

    // Don't wait in a writeLock!
    try
    {
      while (!s_aSenderThreadPool.awaitTermination (1, TimeUnit.SECONDS))
      {
        // wait until we're done
      }
    }
    catch (final InterruptedException ex)
    {
      s_aLogger.error ("Error stopping mail queue", ex);
    }
    return EChange.CHANGED;
  }
}
