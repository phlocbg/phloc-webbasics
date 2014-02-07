/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.sysmon.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;

import com.phloc.commons.deadlock.ThreadDeadlockDetector;
import com.phloc.schedule.job.AbstractScopeAwareJob;
import com.phloc.schedule.quartz.GlobalQuartzScheduler;
import com.phloc.sysmon.CSystemMonitor;

/**
 * Check whether a deadlock occurred
 * 
 * @author Philip Helger
 */
@DisallowConcurrentExecution
public final class CheckDeadlockJob extends AbstractScopeAwareJob {
  @Override
  protected String getApplicationScopeID (final JobDataMap aJobDataMap) {
    return CSystemMonitor.APP_ID;
  }

  @Override
  protected void onExecute (final JobExecutionContext aContext) throws JobExecutionException {
    final ThreadDeadlockDetector aTDD = new ThreadDeadlockDetector ();
    aTDD.addListener (new MailingDeadlockListener ());
    aTDD.run ();
  }

  /**
   * Call this method to schedule the check disk usage job initially.
   */
  public static void schedule () {
    GlobalQuartzScheduler.getInstance ()
                         .scheduleJob (CheckDeadlockJob.class.getName (),
                                       TriggerBuilder.newTrigger ()
                                                     .startNow ()
                                                     .withSchedule (SimpleScheduleBuilder.repeatMinutelyForever (1)),
                                       CheckDeadlockJob.class,
                                       null);
  }
}
