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
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.compare.ESortOrder;

/**
 * Compare {@link LongRunningJobData} objects by their start date
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class ComparatorLongRunningJobDataStartDate extends AbstractComparator <LongRunningJobData>
{
  /**
   * Comparator with default sort order.
   */
  public ComparatorLongRunningJobDataStartDate ()
  {}

  /**
   * Constructor with sort order.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public ComparatorLongRunningJobDataStartDate (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  @Override
  protected int mainCompare (@Nonnull final LongRunningJobData aData1, @Nonnull final LongRunningJobData aData2)
  {
    return CompareUtils.nullSafeCompare (aData1.getStartDateTime (), aData2.getStartDateTime ());
  }
}
