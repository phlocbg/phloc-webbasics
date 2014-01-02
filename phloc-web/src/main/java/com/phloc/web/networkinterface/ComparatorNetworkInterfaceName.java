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
package com.phloc.web.networkinterface;

import java.net.NetworkInterface;
import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.ESortOrder;

/**
 * Comparator to compare {@link NetworkInterface} objects by their name.
 * 
 * @author Philip Helger
 */
public class ComparatorNetworkInterfaceName extends AbstractComparator <NetworkInterface>
{
  /**
   * Comparator with default sort order and no nested comparator.
   */
  public ComparatorNetworkInterfaceName ()
  {}

  /**
   * Constructor with sort order.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public ComparatorNetworkInterfaceName (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  /**
   * Comparator with default sort order and a nested comparator.
   * 
   * @param aNestedComparator
   *        The nested comparator to be invoked, when the main comparison
   *        resulted in 0.
   */
  public ComparatorNetworkInterfaceName (@Nullable final Comparator <? super NetworkInterface> aNestedComparator)
  {
    super (aNestedComparator);
  }

  /**
   * Comparator with sort order and a nested comparator.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   * @param aNestedComparator
   *        The nested comparator to be invoked, when the main comparison
   *        resulted in 0.
   */
  public ComparatorNetworkInterfaceName (@Nonnull final ESortOrder eSortOrder,
                                         @Nullable final Comparator <? super NetworkInterface> aNestedComparator)
  {
    super (eSortOrder, aNestedComparator);
  }

  @Override
  protected int mainCompare (@Nonnull final NetworkInterface aElement1, @Nonnull final NetworkInterface aElement2)
  {
    return aElement1.getName ().compareTo (aElement2.getName ());
  }
}
