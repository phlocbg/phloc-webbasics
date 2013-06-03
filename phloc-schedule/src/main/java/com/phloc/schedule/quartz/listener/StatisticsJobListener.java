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
package com.phloc.schedule.quartz.listener;

import javax.annotation.Nonnull;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.stats.StatisticsManager;

/**
 * A Quartz job listener that handles statistics for job executions. It handles
 * vetoed job executions as well as job executions.
 * 
 * @author Philip Helger
 */
public class StatisticsJobListener implements JobListener
{
  @Nonnull
  @Nonempty
  public String getName ()
  {
    return "StatisticsJobListener";
  }

  @Nonnull
  @Nonempty
  protected String getStatisticsName (@Nonnull final JobExecutionContext aContext)
  {
    return "quartz." + CGStringHelper.getClassLocalName (aContext.getJobDetail ().getJobClass ());
  }

  public void jobToBeExecuted (@Nonnull final JobExecutionContext aContext)
  {}

  public void jobExecutionVetoed (@Nonnull final JobExecutionContext aContext)
  {
    StatisticsManager.getCounterHandler (getStatisticsName (aContext) + "$VETOED").increment ();
  }

  public void jobWasExecuted (@Nonnull final JobExecutionContext aContext, final JobExecutionException aJobException)
  {
    StatisticsManager.getCounterHandler (getStatisticsName (aContext) + "$EXEC").increment ();
    if (aJobException != null)
      StatisticsManager.getCounterHandler (getStatisticsName (aContext) + "$ERROR").increment ();
  }
}
