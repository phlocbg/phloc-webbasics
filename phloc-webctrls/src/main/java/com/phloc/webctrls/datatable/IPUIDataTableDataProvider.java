/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webctrls.datatable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.data.provider.IPagedDataProviderWithFilter;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.json.IJSONObject;

public interface IPUIDataTableDataProvider <T> extends IPagedDataProviderWithFilter <T>
{
  ISimpleURL getAJAXURL ();

  /**
   * Converts a set of matching entries to the final rendering form as JSON
   * structure
   * 
   * @param aMatches
   * @param aColumnKeys
   * @param aDisplayLocale
   * @return The JSON objects representing the matches
   */
  Collection <IJSONObject> convertMatches (List <T> aMatches, List <String> aColumnKeys, @Nonnull Locale aDisplayLocale);

  /**
   * @param aDisplayLocale
   * @return The total number of matching data sets (if no filter is applied,
   *         this would be all existing data sets)
   */
  int getAllMatchingEntriesCount (@Nonnull Locale aDisplayLocale);
}
