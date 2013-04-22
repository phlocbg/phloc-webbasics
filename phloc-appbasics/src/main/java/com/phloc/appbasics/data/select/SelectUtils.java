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
package com.phloc.appbasics.data.select;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ContainerHelper;

/**
 * Handles the application of object filters.
 * 
 * @author Philip Helger
 */
@Immutable
public final class SelectUtils
{
  private SelectUtils ()
  {}

  /**
   * Apply a comparator and a filter on that list.
   * 
   * @param <T>
   *        The type of the elements
   * @param aStartCollection
   *        The initial collection. May be <code>null</code>.
   * @param aComparator
   *        The comparator be used. May be <code>null</code>.
   * @param aLimit
   *        The limitation to be used. May be <code>null</code>.
   * @return The original collection, if it was empty or no comparator or no
   *         limit is provided, a new collection otherwise.
   */
  @Nullable
  public static <T> List <T> sortAndLimit (@Nullable final List <T> aStartCollection,
                                           @Nullable final Comparator <T> aComparator,
                                           @Nullable final SelectLimit aLimit)
  {
    if ((aComparator == null && aLimit == null) || ContainerHelper.isEmpty (aStartCollection))
      return aStartCollection;

    List <T> c = new ArrayList <T> (aStartCollection);

    // apply sorting
    if (aComparator != null)
      ContainerHelper.getSortedInline (c, aComparator);

    // apply limit
    if (aLimit != null)
    {
      int nFromIndex = aLimit.getStartIndex ();
      int nToIndex = Math.min (nFromIndex + aLimit.getResultCount (), c.size ());

      // handle overflow
      if (nFromIndex > nToIndex)
      {
        nFromIndex = 0;
        nToIndex = Math.min (aLimit.getResultCount (), c.size ());
      }

      if (nFromIndex == nToIndex)
        c.clear ();
      else
      {
        final List <T> c2 = new ArrayList <T> (nToIndex - nFromIndex - 1);
        for (int i = nFromIndex; i < nToIndex; ++i)
          c2.add (c.get (i));
        c = c2;
      }
    }
    return c;
  }
}
