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
package com.phloc.schedule.longrun;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.state.ESuccess;
import com.phloc.scopes.singleton.GlobalSingleton;

public final class LongRunningJobManager extends GlobalSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LongRunningJobManager.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, LongRunningJobData> m_aRunningJobs = new HashMap <String, LongRunningJobData> ();

  @Deprecated
  @UsedViaReflection
  public LongRunningJobManager ()
  {}

  @Nonnull
  public static LongRunningJobManager getInstance ()
  {
    return getGlobalSingleton (LongRunningJobManager.class);
  }

  /**
   * Start a long running job
   * 
   * @param aJob
   *        The job that is to be started
   * @param sStartingUserID
   *        The ID of the user who started the job
   * @return The internal long running job ID
   */
  @Nonnull
  @Nonempty
  public String startJob (@Nonnull final ILongRunningJob aJob, @Nullable final String sStartingUserID)
  {
    if (aJob == null)
      throw new NullPointerException ("job");

    // Create a new unique in-memory ID
    final String sJobID = GlobalIDFactory.getNewStringID ();
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aRunningJobs.put (sJobID, new LongRunningJobData (sJobID, aJob.getJobDescription (), sStartingUserID));
      return sJobID;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * End a job.
   * 
   * @param sJobID
   *        The internal long running job ID created from
   *        {@link #startJob(ILongRunningJob,String)}.
   * @param eExecSucess
   *        Was the job execution successful or not from a technical point of
   *        view? May not be <code>null</code>. If a JobExecutionException was
   *        thrown, this should be {@link ESuccess#FAILURE}.
   * @param aResult
   *        The main job results.
   */
  public void endJob (@Nullable final String sJobID,
                      @Nonnull final ESuccess eExecSucess,
                      @Nonnull final LongRunningJobResult aResult)
  {
    if (eExecSucess == null)
      throw new NullPointerException ("execSuccess");

    // Remove from running job list
    LongRunningJobData aJobData;
    m_aRWLock.writeLock ().lock ();
    try
    {
      aJobData = m_aRunningJobs.remove (sJobID);
      if (aJobData == null)
        throw new IllegalArgumentException ("Illegal job ID '" + sJobID + "' passed!");

      // End the job - inside the writeLock
      aJobData.onJobEnd (eExecSucess, aResult);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    // Remember it
    LongRunningJobResultManager.getInstance ().addResult (aJobData);
  }

  @Nonnegative
  public int getRunningJobCount ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aRunningJobs.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @Nonempty
  public Collection <LongRunningJobData> getAllRunningJobs ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aRunningJobs.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  protected void onDestroy () throws InterruptedException
  {
    // Wait until all long running jobs are finished
    final int nWaitSeconds = 1;
    while (true)
    {
      final int nRunningJobCount = getRunningJobCount ();
      if (nRunningJobCount == 0)
        break;

      s_aLogger.error ("There are still " +
                       nRunningJobCount +
                       " long running jobs in the background! Waiting for them to finish...");

      // Wait some time
      Thread.sleep (nWaitSeconds * CGlobal.MILLISECONDS_PER_SECOND);
    }
  }
}
