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
package com.phloc.appbasics.longrun;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ISimpleMultiLingualText;
import com.phloc.datetime.PDTFactory;
import com.phloc.schedule.longrun.LongRunningJobResult;

/**
 * This class contains the data for a single long running job.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class LongRunningJobData implements IHasID <String>
{
  private final String m_sID;

  // Initial job data
  private final ISimpleMultiLingualText m_aJobDescription;
  private final DateTime m_aStartDateTime;
  private final String m_sStartingUserID;

  // Data set on job end:
  private DateTime m_aEndDateTime;
  private ESuccess m_eExecSuccess;
  private LongRunningJobResult m_aResult;

  public LongRunningJobData (@Nonnull @Nonempty final String sJobID,
                             @Nonnull final ISimpleMultiLingualText aJobDescription,
                             @Nullable final String sStartingUserID)
  {
    m_sID = ValueEnforcer.notEmpty (sJobID, "JobID");
    m_aJobDescription = ValueEnforcer.notNull (aJobDescription, "JobDescription");
    m_aStartDateTime = PDTFactory.getCurrentDateTime ();
    m_sStartingUserID = sStartingUserID;
  }

  LongRunningJobData (@Nonnull @Nonempty final String sID,
                      @Nonnull final DateTime aStartDateTime,
                      @Nonnull final DateTime aEndDateTime,
                      @Nonnull final ESuccess eExecSuccess,
                      @Nullable final String sStartingUserID,
                      @Nonnull final IReadonlyMultiLingualText aJobDescription,
                      @Nonnull final LongRunningJobResult aResult)
  {
    m_sID = ValueEnforcer.notEmpty (sID, "ID");
    m_aStartDateTime = ValueEnforcer.notNull (aStartDateTime, "StartDateTime");
    m_aEndDateTime = ValueEnforcer.notNull (aEndDateTime, "EndDateTime");
    m_eExecSuccess = ValueEnforcer.notNull (eExecSuccess, "ExecSuccess");
    m_sStartingUserID = sStartingUserID;
    m_aJobDescription = ValueEnforcer.notNull (aJobDescription, "JobDescription");
    m_aResult = ValueEnforcer.notNull (aResult, "Result");
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  /**
   * @return The description of the underlying job. Never <code>null</code>.
   */
  @Nonnull
  public ISimpleMultiLingualText getJobDescription ()
  {
    return m_aJobDescription;
  }

  /**
   * @return The date time when the job was started. Never <code>null</code>.
   */
  @Nonnull
  public DateTime getStartDateTime ()
  {
    return m_aStartDateTime;
  }

  /**
   * @return The user who started the job. May be <code>null</code>.
   */
  @Nullable
  public String getStartingUserID ()
  {
    return m_sStartingUserID;
  }

  void onJobEnd (@Nonnull final ESuccess eExecSucess, @Nonnull final LongRunningJobResult aResult)
  {
    if (eExecSucess == null)
      throw new NullPointerException ("execSuccess");
    if (aResult == null)
      throw new NullPointerException ("result");
    if (isEnded ())
      throw new IllegalStateException ("Job was already ended");

    // Save the date
    m_aEndDateTime = PDTFactory.getCurrentDateTime ();
    m_eExecSuccess = eExecSucess;
    // Build the main results
    m_aResult = aResult;
    if (m_aResult == null)
      throw new IllegalStateException ("Failed to create job results object!");
  }

  /**
   * @return <code>true</code> if this job was already ended
   */
  public boolean isEnded ()
  {
    return m_aEndDateTime != null;
  }

  /**
   * @return The date and time when the job execution finished
   */
  @Nullable
  public DateTime getEndDateTime ()
  {
    return m_aEndDateTime;
  }

  /**
   * @return The execution duration
   */
  @Nonnull
  public Duration getDuration ()
  {
    if (!isEnded ())
      throw new IllegalStateException ("Job is still running!");
    return new Duration (m_aStartDateTime, getEndDateTime ());
  }

  /**
   * @return The technical success indicator, whether the scheduled job ran
   *         without an exception.
   */
  @Nullable
  public ESuccess getExecutionSuccess ()
  {
    return m_eExecSuccess;
  }

  /**
   * @return The semantic result of the execution.
   */
  @Nullable
  public LongRunningJobResult getResult ()
  {
    return m_aResult;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("jobDescription", m_aJobDescription)
                                       .append ("startDateTime", m_aStartDateTime)
                                       .append ("startingUserID", m_sStartingUserID)
                                       .append ("endDateTime", m_aEndDateTime)
                                       .append ("execSucces", m_eExecSuccess)
                                       .append ("result", m_aResult)
                                       .toString ();

  }
}
