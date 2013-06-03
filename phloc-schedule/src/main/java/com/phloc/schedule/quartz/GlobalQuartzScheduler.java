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
package com.phloc.schedule.quartz;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.EverythingMatcher;

import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.exceptions.LoggedRuntimeException;
import com.phloc.schedule.quartz.listener.StatisticsJobListener;
import com.phloc.scopes.singleton.GlobalSingleton;

/**
 * Global scheduler instance.
 * 
 * @author Philip Helger
 */
public class GlobalQuartzScheduler extends GlobalSingleton
{
  public static final String GROUP_NAME = "phloc";

  private final Scheduler m_aScheduler;

  @UsedViaReflection
  @DevelopersNote ("Used from derived classes")
  public GlobalQuartzScheduler ()
  {
    // main scheduler
    m_aScheduler = QuartzSchedulerHelper.getScheduler ();

    // Always add the statistics listener
    addJobListener (new StatisticsJobListener ());
  }

  @Nonnull
  public static final GlobalQuartzScheduler getInstance ()
  {
    return getGlobalSingleton (GlobalQuartzScheduler.class);
  }

  /**
   * Add a job listener for all jobs.
   * 
   * @param aJobListener
   *        The job listener to be added. May not be <code>null</code>.
   */
  public void addJobListener (@Nonnull final JobListener aJobListener)
  {
    if (aJobListener == null)
      throw new NullPointerException ("jobListener");

    try
    {
      m_aScheduler.getListenerManager ().addJobListener (aJobListener, EverythingMatcher.allJobs ());
    }
    catch (final SchedulerException ex)
    {
      throw new IllegalStateException ("Failed to add job listener " + aJobListener, ex);
    }
  }

  /**
   * @return The underlying Quartz scheduler object.
   */
  @Nonnull
  protected final Scheduler getScheduler ()
  {
    return m_aScheduler;
  }

  /**
   * Modify the JobData map in derived classes. This is e.g. used in pdaf3 to
   * set the correct state data in executed jobs.
   * 
   * @param aJobDataMap
   *        The job data map to modify. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void modifyJobDataMap (@Nonnull final JobDataMap aJobDataMap)
  {}

  /**
   * This method is only for testing purposes.
   * 
   * @param sJobName
   *        Name of the backup job. Needs to be passed in for testing purposes
   *        since no two job details with the same name may exist.
   * @param aTriggerBuilder
   *        The trigger builder instance to schedule the job
   * @param aJobClass
   *        Class to execute
   * @param aJobData
   *        Additional parameters. May be <code>null</code>.
   */
  public final void scheduleJob (@Nonnull final String sJobName,
                                 @Nonnull final TriggerBuilder <? extends Trigger> aTriggerBuilder,
                                 @Nonnull final Class <? extends Job> aJobClass,
                                 @Nullable final Map <String, ? extends Object> aJobData)
  {
    if (sJobName == null)
      throw new NullPointerException ("name");
    if (aTriggerBuilder == null)
      throw new NullPointerException ("triggerBuilder");
    if (aJobClass == null)
      throw new NullPointerException ("class");

    // what to do
    final JobDetail aJobDetail = JobBuilder.newJob (aJobClass)
                                           .withIdentity (sJobName, GlobalQuartzScheduler.GROUP_NAME)
                                           .build ();

    // add custom parameters
    final JobDataMap aJobDataMap = aJobDetail.getJobDataMap ();
    if (aJobData != null)
      for (final Map.Entry <String, ? extends Object> aEntry : aJobData.entrySet ())
        aJobDataMap.put (aEntry.getKey (), aEntry.getValue ());

    // Add parameters via callback as well
    modifyJobDataMap (aJobDataMap);

    try
    {
      // Schedule now
      m_aScheduler.scheduleJob (aJobDetail, aTriggerBuilder.build ());
    }
    catch (final SchedulerException ex)
    {
      throw LoggedRuntimeException.newException (ex);
    }
  }

  /**
   * Shutdown the scheduler and wait for all jobs to complete.
   * 
   * @throws SchedulerException
   *         If something goes wrong
   */
  public final void shutdown () throws SchedulerException
  {
    // Shutdown but wait for jobs to complete
    m_aScheduler.shutdown (true);
  }

  @Override
  protected final void onDestroy () throws Exception
  {
    shutdown ();
  }
}
