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
package com.phloc.schedule.quartz;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.quartz.SchedulerException;

/**
 * Test class for class {@link QuartzSchedulerHelper}.
 * 
 * @author Philip Helger
 */
public final class QuartzSchedulerHelperTest
{
  @Test
  public void testGetScheduler () throws SchedulerException
  {
    assertNotNull (QuartzSchedulerHelper.getScheduler (false));
    assertSame (ESchedulerState.STANDBY, QuartzSchedulerHelper.getSchedulerState ());
    assertNotNull (QuartzSchedulerHelper.getScheduler ());
    assertSame (ESchedulerState.STARTED, QuartzSchedulerHelper.getSchedulerState ());
    assertNotNull (QuartzSchedulerHelper.getSchedulerMetaData ());
    QuartzSchedulerHelper.getScheduler ().shutdown (true);
    assertSame (ESchedulerState.STANDBY, QuartzSchedulerHelper.getSchedulerState ());
    assertNotNull (QuartzSchedulerHelper.getScheduler ());
    assertSame (ESchedulerState.STARTED, QuartzSchedulerHelper.getSchedulerState ());
    QuartzSchedulerHelper.getScheduler ().shutdown (true);
  }
}
