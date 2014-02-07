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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
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
    if (StringHelper.hasNoText (sApplicationID))
      throw new IllegalStateException ("No applicationID present!");
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
    if (nPollingMinutes <= 0)
      throw new IllegalArgumentException ("Polling minutes must be > 0!");
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
