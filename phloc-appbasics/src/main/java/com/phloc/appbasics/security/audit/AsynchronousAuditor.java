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
package com.phloc.appbasics.security.audit;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.security.login.ICurrentUserIDProvider;
import com.phloc.commons.callback.IThrowingRunnableWithParameter;
import com.phloc.commons.concurrent.collector.ConcurrentCollectorMultiple;
import com.phloc.commons.state.EChange;

/**
 * The class handles all audit items
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class AsynchronousAuditor extends AbstractAuditor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AsynchronousAuditor.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final ConcurrentCollectorMultiple <IAuditItem> m_aCollector;
  private final ExecutorService s_aSenderThreadPool;

  public AsynchronousAuditor (@Nonnull final ICurrentUserIDProvider aUserIDProvider,
                              @Nonnull final IThrowingRunnableWithParameter <List <IAuditItem>> aPerformer)
  {
    super (aUserIDProvider);
    if (aPerformer == null)
      throw new NullPointerException ("performer");

    m_aCollector = new ConcurrentCollectorMultiple <IAuditItem> (aPerformer);
    s_aSenderThreadPool = Executors.newSingleThreadExecutor ();
    s_aSenderThreadPool.submit (m_aCollector);
  }

  @Override
  protected void handleAuditItem (@Nonnull final IAuditItem aAuditItem)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aCollector.queueObject (aAuditItem);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnegative
  public int getQueueLength ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aCollector.getQueueLength ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * When using this auditor, it is important to call this stop method before
   * shutdown. It avoids further queuing of objects and waits until all items
   * are handled. This method blocks until all remaining objects are handled.
   * 
   * @return {@link EChange#CHANGED} if the shutdown was performed,
   *         {@link EChange#UNCHANGED} if the auditor was already shut down.
   */
  @Nonnull
  public EChange stop ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      // Check if the thread pool is already shut down
      if (s_aSenderThreadPool.isShutdown ())
        return EChange.UNCHANGED;

      // don't take any more actions
      s_aSenderThreadPool.shutdown ();

      // stop all specific queues
      m_aCollector.stopQueuingNewObjects ();

      s_aLogger.info ("Stopping auditor queues with " + m_aCollector.getQueueLength () + " items");
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
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
      s_aLogger.error ("Error stopping auditor queue", ex);
    }
    return EChange.CHANGED;
  }
}
