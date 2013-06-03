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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;

import com.phloc.commons.mutable.MutableBoolean;

/**
 * Test class for class {@link GlobalQuartzScheduler}.
 * 
 * @author Philip Helger
 */
public final class GlobalQuartzSchedulerTest
{
  static final MutableBoolean EXEC_LOG = new MutableBoolean (false);

  @Test
  public void testGetScheduler () throws Exception
  {
    final GlobalQuartzScheduler aDS = new GlobalQuartzScheduler ();
    aDS.scheduleJob ("test",
                     TriggerBuilder.newTrigger ()
                                   .startNow ()
                                   .withSchedule (SimpleScheduleBuilder.repeatMinutelyForTotalCount (1)),
                     MockJob.class,
                     null);
    Thread.sleep (100);
    assertTrue (EXEC_LOG.booleanValue ());
    aDS.onScopeDestruction ();
  }
}
