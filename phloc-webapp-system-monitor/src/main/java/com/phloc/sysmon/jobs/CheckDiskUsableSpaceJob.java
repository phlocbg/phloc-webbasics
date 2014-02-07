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

import java.io.File;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.CGlobal;
import com.phloc.commons.io.misc.SizeHelper;
import com.phloc.schedule.job.AbstractScopeAwareJob;
import com.phloc.schedule.quartz.GlobalQuartzScheduler;
import com.phloc.sysmon.CSystemMonitor;
import com.phloc.web.smtp.EEmailType;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.impl.EmailData;
import com.phloc.webbasics.app.error.InternalErrorHandler;
import com.phloc.webscopes.smtp.ScopedMailAPI;

/**
 * Check whether at least 1GB of usable space is present on the web app file
 * system.
 * 
 * @author Philip Helger
 */
@DisallowConcurrentExecution
public final class CheckDiskUsableSpaceJob extends AbstractScopeAwareJob {
  private static final Logger s_aLogger = LoggerFactory.getLogger (CheckDiskUsableSpaceJob.class);

  @Override
  protected String getApplicationScopeID (final JobDataMap aJobDataMap) {
    return CSystemMonitor.APP_ID;
  }

  @Override
  protected void onExecute (final JobExecutionContext aContext) throws JobExecutionException {
    final File fBaseDir = WebFileIO.getBasePathFile ();
    final long nUsableSpace = fBaseDir.getUsableSpace ();
    if (nUsableSpace < CGlobal.BYTES_PER_GIGABYTE) {
      final String sUsableFormatted = SizeHelper.getSizeHelperOfLocale (CGlobal.LOCALE_FIXED_NUMBER_FORMAT)
                                                .getAsMatching (nUsableSpace, 1);
      s_aLogger.warn ("File system has less than 1GB of usable space: " + sUsableFormatted);

      // Main error thread dump
      final String sMailBody = InternalErrorHandler.fillInternalErrorMetaData (null, null, null).getAsString ();

      final IEmailData aEmailData = new EmailData (EEmailType.TEXT);
      aEmailData.setFrom (InternalErrorHandler.getSMTPSenderAddress ());
      aEmailData.setTo (InternalErrorHandler.getSMTPReceiverAddresses ());
      aEmailData.setSubject ("[system-monitor] Disk Space is low: " + sUsableFormatted);
      aEmailData.setBody (sMailBody);
      ScopedMailAPI.getInstance ().queueMail (InternalErrorHandler.getSMTPSettings (), aEmailData);
    }
  }

  /**
   * Call this method to schedule the check disk usage job initially.
   */
  public static void schedule () {
    GlobalQuartzScheduler.getInstance ()
                         .scheduleJob (CheckDiskUsableSpaceJob.class.getName (),
                                       TriggerBuilder.newTrigger ()
                                                     .startNow ()
                                                     .withSchedule (SimpleScheduleBuilder.repeatMinutelyForever (60)),
                                       CheckDiskUsableSpaceJob.class,
                                       null);
  }
}
