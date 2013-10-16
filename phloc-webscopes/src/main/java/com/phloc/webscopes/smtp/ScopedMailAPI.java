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
package com.phloc.webscopes.smtp;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.state.ESuccess;
import com.phloc.scopes.singleton.GlobalSingleton;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.ISMTPSettings;
import com.phloc.web.smtp.failed.FailedMailQueue;
import com.phloc.web.smtp.transport.MailAPI;

/**
 * Scope aware wrapper around {@link MailAPI} class so that it is gracefully
 * stopped when the global scope is stopped.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class ScopedMailAPI extends GlobalSingleton
{
  public static final boolean DEFAULT_STOP_IMMEDIATLY = false;

  private final AtomicBoolean m_aStopImmediately = new AtomicBoolean (DEFAULT_STOP_IMMEDIATLY);

  @UsedViaReflection
  @Deprecated
  public ScopedMailAPI ()
  {}

  @Nonnull
  public static ScopedMailAPI getInstance ()
  {
    return getGlobalSingleton (ScopedMailAPI.class);
  }

  /**
   * @return The current failed mail queue. Never <code>null</code>.
   */
  @Nonnull
  public FailedMailQueue getFailedMailQueue ()
  {
    return MailAPI.getFailedMailQueue ();
  }

  /**
   * Set a new global failed mail queue. Updates all existing queues.
   * 
   * @param aFailedMailQueue
   *        The new failed mail queue to set. May not be <code>null</code>.
   */
  public void setFailedMailQueue (@Nonnull final FailedMailQueue aFailedMailQueue)
  {
    MailAPI.setFailedMailQueue (aFailedMailQueue);
  }

  /**
   * Unconditionally queue a mail
   * 
   * @param aSMTPSettings
   *        The SMTP settings to use. May not be <code>null</code>.
   * @param aMailData
   *        The data of the email to be send. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public ESuccess queueMail (@Nonnull final ISMTPSettings aSMTPSettings, @Nonnull final IEmailData aMailData)
  {
    return MailAPI.queueMail (aSMTPSettings, aMailData);
  }

  /**
   * Queue multiple mails at once.
   * 
   * @param aSMTPSettings
   *        The SMTP settings to be used.
   * @param aMailDataList
   *        The mail messages to queue. May not be <code>null</code>.
   * @return The number of queued emails. Always &ge; 0. Maximum value is the
   *         number of {@link IEmailData} objects in the argument.
   */
  @Nonnegative
  public int queueMails (@Nonnull final ISMTPSettings aSMTPSettings,
                         @Nonnull final Collection <? extends IEmailData> aMailDataList)
  {
    return MailAPI.queueMails (aSMTPSettings, aMailDataList);
  }

  @Nonnegative
  public int getTotalQueueLength ()
  {
    return MailAPI.getTotalQueueLength ();
  }

  /**
   * @return <code>true</code> if all mails currently in the queue should be
   *         removed and put in the failed mail queue. Only the emails currently
   *         in sending are continued to be sent out.
   */
  public boolean isStopImmediately ()
  {
    return m_aStopImmediately.get ();
  }

  /**
   * Determine whether to stop immediately or not
   * 
   * @param bStopImmediately
   *        <code>true</code> if all mails currently in the queue should be
   *        removed and put in the failed mail queue. Only the emails currently
   *        in sending are continued to be sent out.
   */
  public void setStopImmediatly (final boolean bStopImmediately)
  {
    m_aStopImmediately.set (bStopImmediately);
  }

  @Override
  protected void onDestroy ()
  {
    // Stop mail queues
    MailAPI.stop (isStopImmediately ());
  }
}
