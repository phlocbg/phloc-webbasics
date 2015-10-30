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
package com.phloc.schedule.quartz;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Misc utility methods around Quartz schedulers
 * 
 * @author Philip Helger
 */
@Immutable
public final class QuartzSchedulerHelper
{
  public static final boolean DEFAULT_START_AUTOMATICALLY = true;
  private static final SchedulerFactory s_aSchedulerFactory = new StdSchedulerFactory ();

  private QuartzSchedulerHelper ()
  {}

  /**
   * @return The single {@link Scheduler} instance that is ensured to be
   *         started. Never <code>null</code>.
   * @see #getScheduler(boolean)
   */
  @Nonnull
  public static Scheduler getScheduler ()
  {
    return getScheduler (DEFAULT_START_AUTOMATICALLY);
  }

  /**
   * Get the underlying Quartz scheduler
   * 
   * @param bStartAutomatically
   *        If <code>true</code> the returned scheduler is automatically
   *        started. If <code>false</code> the state is not changed.
   * @return The underlying Quartz scheduler. Never <code>null</code>.
   */
  @Nonnull
  public static Scheduler getScheduler (final boolean bStartAutomatically)
  {
    try
    {
      // Don't try to use a name - results in NPE
      final Scheduler aScheduler = s_aSchedulerFactory.getScheduler ();
      if (bStartAutomatically && !aScheduler.isStarted ())
        aScheduler.start ();
      return aScheduler;
    }
    catch (final SchedulerException ex)
    {
      throw new IllegalStateException ("Failed to create and start scheduler!", ex);
    }
  }

  /**
   * Get the metadata of the scheduler. The state of the scheduler is not
   * changed within this method.
   * 
   * @return The metadata of the underlying scheduler.
   */
  @Nonnull
  public static SchedulerMetaData getSchedulerMetaData ()
  {
    try
    {
      // Get the scheduler without starting it
      return s_aSchedulerFactory.getScheduler ().getMetaData ();
    }
    catch (final SchedulerException ex)
    {
      throw new IllegalStateException ("Failed to get scheduler metadata", ex);
    }
  }

  /**
   * @return The state of the scheduler. Never <code>null</code>.
   */
  @Nonnull
  public static ESchedulerState getSchedulerState ()
  {
    try
    {
      // Get the scheduler without starting it
      final Scheduler aScheduler = s_aSchedulerFactory.getScheduler ();
      if (aScheduler.isStarted ())
        return ESchedulerState.STARTED;
      if (aScheduler.isInStandbyMode ())
        return ESchedulerState.STANDBY;
      if (aScheduler.isShutdown ())
        return ESchedulerState.SHUTDOWN;
    }
    catch (final SchedulerException ex)
    {
      throw new IllegalStateException ("Error retrieving scheduler state!", ex);
    }
    throw new IllegalStateException ("Unknown scheduler state!");
  }
}
