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
import com.phloc.schedule.longrun.LongRunningJobManager;
import com.phloc.schedule.longrun.LongRunningJobResult;
import com.phloc.scopes.mgr.ScopeManager;
import com.phloc.web.mock.MockHttpServletResponse;
import com.phloc.web.mock.OfflineHttpServletRequest;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Abstract {@link Job} implementation that handles request scopes correctly.
 * This is required, because each scheduled job runs in its own thread so that
 * no default {@link ScopeManager} information would be available.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public abstract class AbstractScopeAwareJob implements Job
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractScopeAwareJob.class);
  private static final IStatisticsHandlerKeyedTimer s_aStatsTimer = StatisticsManager.getKeyedTimerHandler (AbstractScopeAwareJob.class);
  private static final IStatisticsHandlerKeyedCounter s_aStatsCounterSuccess = StatisticsManager.getKeyedCounterHandler (AbstractScopeAwareJob.class +
                                                                                                                         "$success");
  private static final IStatisticsHandlerKeyedCounter s_aStatsCounterFailure = StatisticsManager.getKeyedCounterHandler (AbstractScopeAwareJob.class +
                                                                                                                         "$failure");
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static IJobExceptionHandler s_aCustomExceptionHandler;

  public AbstractScopeAwareJob ()
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
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aCustomExceptionHandler = aCustomExceptionHandler;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return The custom exception handler set. May be <code>null</code>.
   */
  @Nullable
  public static IJobExceptionHandler getCustomExceptionHandler ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aCustomExceptionHandler;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   * @return The application scope ID to be used. May not be <code>null</code>.
   */
  @Nonnull
  protected abstract String getApplicationScopeID (@Nonnull final JobDataMap aJobDataMap);

  /**
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   * @return The user ID to be used. May not be <code>null</code>.
   */
  @Nonnull
  protected abstract String getCurrentUsedID (@Nonnull final JobDataMap aJobDataMap);

  @Nonnull
  protected abstract LongRunningJobManager getLongRunningJobManager ();

  /**
   * Called before the job gets executed.
   * 
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void beforeExecute (@Nonnull final JobDataMap aJobDataMap)
  {}

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
   * Called after the job gets executed.
   * 
   * @param aJobDataMap
   *        The current job data map.
   */
  @OverrideOnDemand
  protected void afterExecute (@Nonnull final JobDataMap aJobDataMap)
  {}

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
  private static void _triggerCustomExceptionHandler (@Nonnull final Throwable t,
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
        s_aLogger.error ("Exception in custom scheduled job exception handler " +
                         aCustomExceptionHandler +
                         " for job class '" +
                         sJobClassName +
                         "'", t2);
      }
  }

  public final void execute (@Nonnull final JobExecutionContext aContext) throws JobExecutionException
  {
    // State variables
    ESuccess eExecSucess = ESuccess.FAILURE;
    String sLongRunningJobID = null;
    final ILongRunningJob aLongRunningJob = this instanceof ILongRunningJob ? (ILongRunningJob) this : null;
    final JobDataMap aJobDataMap = aContext.getJobDetail ().getJobDataMap ();
    final String sScopeApplicationID = getApplicationScopeID (aJobDataMap);

    beforeExecute (aJobDataMap);
    try
    {
      // Scopes (ensure to create a new scope each time!)
      WebScopeManager.onRequestBegin (sScopeApplicationID,
                                      new OfflineHttpServletRequest (WebScopeManager.getGlobalScope ()
                                                                                    .getServletContext (), false),
                                      new MockHttpServletResponse ());
      final String sJobClassName = getClass ().getName ();
      try
      {
        if (s_aLogger.isDebugEnabled ())
          s_aLogger.debug ("Executing scheduled job " + sJobClassName);

        // Do the long running job start inside the scopes, so that we can
        // access the current user!
        if (aLongRunningJob != null)
        {
          sLongRunningJobID = getLongRunningJobManager ().startJob (aLongRunningJob, getCurrentUsedID (aJobDataMap));
        }

        final StopWatch aSW = new StopWatch (true);

        // Main execution
        onExecute (aContext);

        // Execution without exception
        eExecSucess = ESuccess.SUCCESS;

        // Increment statistics
        s_aStatsTimer.addTime (sJobClassName, aSW.stopAndGetMillis ());
        s_aStatsCounterSuccess.increment (sJobClassName);
      }
      catch (final Throwable t)
      {
        // Increment statistics
        s_aStatsCounterFailure.increment (sJobClassName);

        // Notify custom exception handler
        _triggerCustomExceptionHandler (t, sJobClassName, aLongRunningJob != null);

        if (t instanceof JobExecutionException)
          throw (JobExecutionException) t;
        throw new JobExecutionException ("Internal job execution error", t);
      }
      finally
      {
        if (sLongRunningJobID != null)
        {
          // End long running job before the request scope is closed
          try
          {
            final LongRunningJobResult aJobResult = aLongRunningJob.createResult ();
            getLongRunningJobManager ().endJob (sLongRunningJobID, eExecSucess, aJobResult);
          }
          catch (final Throwable t)
          {
            s_aLogger.error ("Failed to end long running job", t);

            // Notify custom exception handler
            _triggerCustomExceptionHandler (t, sJobClassName, aLongRunningJob != null);
          }
        }

        // Close request scope
        WebScopeManager.onRequestEnd ();
      }
    }
    finally
    {
      afterExecute (aJobDataMap);
    }
  }
}
