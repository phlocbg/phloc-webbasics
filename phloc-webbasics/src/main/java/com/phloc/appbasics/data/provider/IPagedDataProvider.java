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
package com.phloc.appbasics.data.provider;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.appbasics.data.select.IHasOrderAndLimit;

public interface IPagedDataProvider <DATATYPE> extends Serializable
{
  /**
   * @return the total number of entries to be contained in the current table.
   *         This is according to any possible applied filters, but independent
   *         of sorting of page size.
   */
  @Nonnegative
  long getTotalEntryCount ();

  /**
   * All matching entries, in the correct order. Applies the filter, the order
   * and the limit.
   * 
   * @param aTable
   *        The source table
   * @param aDisplayLocale
   *        The display locale
   * @return The sorted list of items to display.
   */
  @Nonnull
  List <DATATYPE> getMatchingEntries (IHasOrderAndLimit aTable, Locale aDisplayLocale);
}
