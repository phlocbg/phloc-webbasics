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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.state.ESuccess;
import com.phloc.schedule.longrun.ILongRunningJob;
import com.phloc.schedule.longrun.LongRunningJobManager;
import com.phloc.schedule.longrun.LongRunningJobResult;

/**
 * Abstract scope aware long running job.
 * 
 * @author Philip Helger
 */
public abstract class AbstractScopeAwareLongRunningJob extends AbstractScopeAwareJob implements ILongRunningJob
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractScopeAwareLongRunningJob.class);

  /** Predefined key into the job data map */
  private static final String KEY_LONG_RUNNING_JOB_ID = "$phloc$longrunningjobid";

  /**
   * Get the ID of the current user who executes the job.
   * 
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   * @return The user ID to be used. May be <code>null</code> but it is
   *         recommended to deliver "guest" or the like instead.
   */
  @Nullable
  protected abstract String getCurrentUserID (@Nonnull final JobDataMap aJobDataMap);

  /**
   * @return The {@link LongRunningJobManager} to be used. May not return
   *         <code>null</code>.
   */
  @Nonnull
  protected abstract LongRunningJobManager getLongRunningJobManager ();

  @Override
  @OverridingMethodsMustInvokeSuper
  protected void beforeExecute (@Nonnull final JobDataMap aJobDataMap)
  {
    final String sUserID = getCurrentUserID (aJobDataMap);
    final String sLongRunningJobID = getLongRunningJobManager ().startJob (this, sUserID);

    // Store in JobDataMap
    aJobDataMap.put (KEY_LONG_RUNNING_JOB_ID, sLongRunningJobID);
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  protected void afterExecuteInScope (@Nonnull final JobDataMap aJobDataMap, @Nonnull final ESuccess eExecSuccess)
  {
    // End long running job before the request scope is closed
    try
    {
      // Get long running job ID from JobDataMap
      final String sLongRunningJobID = aJobDataMap.getString (KEY_LONG_RUNNING_JOB_ID);
      if (sLongRunningJobID != null)
      {
        // Create the main result
        final LongRunningJobResult aJobResult = createResult ();
        getLongRunningJobManager ().endJob (sLongRunningJobID, eExecSuccess, aJobResult);
      }
      else
        s_aLogger.error ("Failed to retrieve long running job ID from JobDataMap " + aJobDataMap);
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Failed to end long running job", t);

      // Notify custom exception handler
      _triggerCustomExceptionHandler (t, getClass ().getName (), true);
    }
  }
}
