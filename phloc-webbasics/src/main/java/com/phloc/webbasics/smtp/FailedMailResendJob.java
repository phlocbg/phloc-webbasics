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
package com.phloc.webbasics.smtp;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.schedule.job.AbstractScopeAwareJob;
import com.phloc.schedule.quartz.GlobalQuartzScheduler;
import com.phloc.web.smtp.failed.FailedMailData;
import com.phloc.webbasics.mgr.MetaSystemManager;
import com.phloc.webscopes.smtp.ScopedMailAPI;

/**
 * A Quartz job, that tries to resend failed mails.
 * 
 * @author Philip Helger
 */
@DisallowConcurrentExecution
public class FailedMailResendJob extends AbstractScopeAwareJob
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FailedMailResendJob.class);

  private static String s_sAppID;

  protected static void setApplicationScopeID (@Nonnull @Nonempty final String sApplicationID)
  {
    ValueEnforcer.notEmpty (sApplicationID, "ApplicationID");
    if (s_sAppID != null)
      throw new IllegalStateException ("This job is already scheduled!");

    s_sAppID = sApplicationID;
  }

  @Override
  protected final String getApplicationScopeID (final JobDataMap aJobDataMap)
  {
    return s_sAppID;
  }

  @Override
  protected void onExecute (final JobExecutionContext aContext) throws JobExecutionException
  {
    final List <FailedMailData> aFailedMails = MetaSystemManager.getFailedMailQueue ().removeAll ();
    if (!aFailedMails.isEmpty ())
    {
      s_aLogger.info ("Trying to resend " + aFailedMails.size () + " failed mails!");
      for (final FailedMailData aFailedMailData : aFailedMails)
        ScopedMailAPI.getInstance ().queueMail (aFailedMailData.getSMTPSettings (), aFailedMailData.getEmailData ());
    }
  }

  public static void scheduleMe (@Nonnull @Nonempty final String sApplicationID, @Nonnegative final int nPollingMinutes)
  {
    ValueEnforcer.isGT0 (nPollingMinutes, "PollingMinutes");

    setApplicationScopeID (sApplicationID);
    GlobalQuartzScheduler.getInstance ()
                         .scheduleJob (FailedMailResendJob.class.getName (),
                                       TriggerBuilder.newTrigger ()
                                                     .startNow ()
                                                     .withSchedule (SimpleScheduleBuilder.repeatMinutelyForever (nPollingMinutes)),
                                       FailedMailResendJob.class,
                                       null);
  }
}
