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
package com.phloc.schedule.job;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.IStatisticsHandlerKeyedTimer;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.timing.StopWatch;
import com.phloc.schedule.longrun.ILongRunningJob;

/**
 * Abstract {@link Job} implementation with an exception handler etc.
 * 
 * @author Boris Gregorcic
 */
@ThreadSafe
public abstract class AbstractJob implements Job
{
  private static final Logger LOG = LoggerFactory.getLogger (AbstractJob.class);
  private static final IStatisticsHandlerKeyedTimer STATS_TIMER = StatisticsManager.getKeyedTimerHandler (AbstractJob.class);
  private static final IStatisticsHandlerKeyedCounter STATS_COUNTER_SUCCESS = StatisticsManager.getKeyedCounterHandler (AbstractJob.class +
                                                                                                                        "$success"); //$NON-NLS-1$
  private static final IStatisticsHandlerKeyedCounter STATS_COUNTER_FAILURE = StatisticsManager.getKeyedCounterHandler (AbstractJob.class +
                                                                                                                        "$failure"); //$NON-NLS-1$
  private static final ReadWriteLock LOCK = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static IJobExceptionHandler s_aCustomExceptionHandler;

  public AbstractJob ()
  {}

  /**
   * Set a custom exception handler that is invoked in case an exception is
   * thrown. This exception handler is invoked additional to the regular
   * exception logging!
   * 
   * @param aCustomExceptionHandler
   *        The custom handler. May be <code>null</code> to indicate that no
   *        handler is needed.
   */
  public static void setCustomExceptionHandler (@Nullable final IJobExceptionHandler aCustomExceptionHandler)
  {
    LOCK.writeLock ().lock ();
    try
    {
      s_aCustomExceptionHandler = aCustomExceptionHandler;
    }
    finally
    {
      LOCK.writeLock ().unlock ();
    }
  }

  /**
   * @return The custom exception handler set. May be <code>null</code>.
   */
  @Nullable
  public static IJobExceptionHandler getCustomExceptionHandler ()
  {
    LOCK.readLock ().lock ();
    try
    {
      return s_aCustomExceptionHandler;
    }
    finally
    {
      LOCK.readLock ().unlock ();
    }
  }

  /**
   * Called before the job gets executed. This method is called before the
   * scopes are initialized!
   * 
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void beforeExecute (@SuppressWarnings ("unused") @Nonnull final JobDataMap aJobDataMap)
  {
    // override on demand
  }

  /**
   * This is the method with the main actions to be executed.
   * 
   * @param aContext
   *        The Quartz context
   * @throws JobExecutionException
   *         In case of an error in execution
   */
  protected abstract void onExecute (@Nonnull final JobExecutionContext aContext) throws JobExecutionException;

  /**
   * Called after the job gets executed. This method is called after the scopes
   * are destroyed.
   * 
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   * @param eExecSuccess
   *        The execution success state. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void afterExecute (@SuppressWarnings ("unused") @Nonnull final JobDataMap aJobDataMap,
                               @SuppressWarnings ("unused") @Nonnull final ESuccess eExecSuccess)
  {
    // override on demand
  }

  /**
   * Called when an exception of the specified type occurred
   * 
   * @param t
   *        The exception. Never <code>null</code>.
   * @param sJobClassName
   *        The name of the job class
   * @param bIsLongRunning
   *        <code>true</code> if it is a long running job
   */
  protected static void triggerCustomExceptionHandler (@Nonnull final Throwable t,
                                                       @Nullable final String sJobClassName,
                                                       final boolean bIsLongRunning)
  {
    final IJobExceptionHandler aCustomExceptionHandler = getCustomExceptionHandler ();
    if (aCustomExceptionHandler != null)
      try
      {
        aCustomExceptionHandler.onScheduledJobException (t, sJobClassName, bIsLongRunning);
      }
      catch (final Throwable t2)
      {
        LOG.error ("Exception in custom scheduled job exception handler " + //$NON-NLS-1$
                         aCustomExceptionHandler +
                   " for job class '" + //$NON-NLS-1$
                         sJobClassName +
                   "'", t2); //$NON-NLS-1$
      }
  }

  @Override
  public final void execute (@Nonnull final JobExecutionContext aContext) throws JobExecutionException
  {
    // State variables
    ESuccess eExecSuccess = ESuccess.FAILURE;

    // Create a local copy of the job data map to allow for modifications and
    // alteration
    final JobDataMap aJobDataMap = new JobDataMap (aContext.getMergedJobDataMap ());

    beforeExecute (aJobDataMap);
    try
    {
      final String sJobClassName = getClass ().getName ();
      try
      {
        if (LOG.isDebugEnabled ())
          LOG.debug ("Executing scheduled job " + sJobClassName); //$NON-NLS-1$

        final StopWatch aSW = new StopWatch (true);

        // Main execution
        onExecute (aContext);

        // Execution without exception -> success
        eExecSuccess = ESuccess.SUCCESS;

        // Increment statistics
        STATS_TIMER.addTime (sJobClassName, aSW.stopAndGetMillis ());
        STATS_COUNTER_SUCCESS.increment (sJobClassName);

        if (LOG.isDebugEnabled ())
          LOG.debug ("Successfully finished executing scheduled job " + sJobClassName); //$NON-NLS-1$
      }
      catch (final Throwable t)
      {
        // Increment statistics
        STATS_COUNTER_FAILURE.increment (sJobClassName);

        // Notify custom exception handler
        triggerCustomExceptionHandler (t, sJobClassName, this instanceof ILongRunningJob);

        if (t instanceof JobExecutionException)
        {
          throw (JobExecutionException) t;
        }
        throw new JobExecutionException ("Internal job execution error of " + sJobClassName, t); //$NON-NLS-1$
      }
    }
    finally
    {
      // Invoke callback
      afterExecute (aJobDataMap, eExecSuccess);
    }
  }
}
