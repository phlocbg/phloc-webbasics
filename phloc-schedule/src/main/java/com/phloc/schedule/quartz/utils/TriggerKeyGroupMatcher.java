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
package com.phloc.schedule.quartz.utils;

import javax.annotation.Nonnull;

import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * Typesafe implementation of GroupMatcher for {@link TriggerKey}
 * 
 * @author Philip Helger
 */
public final class TriggerKeyGroupMatcher extends GroupMatcher <TriggerKey>
{
  public TriggerKeyGroupMatcher (@Nonnull final String sCompareTo, @Nonnull final StringOperatorName eCompareWith)
  {
    super (sCompareTo, eCompareWith);
  }

  @Nonnull
  public static TriggerKeyGroupMatcher createEquals (@Nonnull final String sCompareTo)
  {
    return new TriggerKeyGroupMatcher (sCompareTo, StringOperatorName.EQUALS);
  }
}
