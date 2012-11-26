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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.data.provider.AbstractPagedDataProviderWithFilter;
import com.phloc.appbasics.data.select.ISelectFilter;
import com.phloc.appbasics.data.select.ISelectFilterLike;
import com.phloc.appbasics.data.select.ISelectFilterable;
import com.phloc.appbasics.data.select.SelectFilterChainOR;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;

/**
 * This is an abstract base class for a table data provider (
 * {@link IPUIDataTableDataProvider}) taking care of basic functionality like
 * filtering.
 * 
 * @author Boris Gregorcic
 * @param <T>
 *        The object type this data provider operates on
 */
public abstract class AbstractTableDataProvider <T> extends AbstractPagedDataProviderWithFilter <T> implements
                                                                                                   IPUIDataTableDataProvider <T>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractTableDataProvider.class);

  /**
   * Returns the value of the passed object corresponding to the passed column
   * (property) key in a form how it should be used for filtering. Generally
   * this will be the formatted rendering form.
   * 
   * @param aObject
   * @param sColumnKey
   * @param aDisplayLocale
   * @return The property value to be used for filtering
   */
  protected abstract String getFilterValue (final T aObject, final String sColumnKey, final Locale aDisplayLocale);

  /**
   * Returns the vale of the passed object corresponding to the passed column
   * (property) key as it should be rendered. This value can be XHTML mark-up.
   * 
   * @param aObject
   * @param sColumnKey
   * @param aDisplayLocale
   * @return The XHTML mark-up to render the object property
   */
  protected abstract IHCNode getRenderValue (final T aObject, final String sColumnKey, final Locale aDisplayLocale);

  /**
   * This method decides whether a certain entry matches the applied filter or
   * not. The filter algorithm is "smart" which means multiple filter words can
   * be used. AN entry is considered matching if all of the passed filter words
   * occur case insensitive in any of the columns.
   * 
   * @param aObject
   * @param aFilter
   * @param aDisplayLocale
   * @return <code>true</code> if the entry is matching, <code>false</code>
   *         otherwise
   */
  protected boolean passesFilter (final T aObject, final ISelectFilterable aFilter, final Locale aDisplayLocale)
  {
    if (aFilter instanceof SelectFilterChainOR)
    {
      final SelectFilterChainOR aFilterChain = (SelectFilterChainOR) aFilter;
      final String sFilterValue = ((ISelectFilterLike) aFilterChain.getFilters ().get (0)).getFilterValue ();

      // filter for all entered words separately (ignoring empty words of
      // course)
      final Set <String> aUnmatchedFilterWords = new HashSet <String> ();
      for (final String sWord : StringHelper.getExplodedToSet (" ", sFilterValue))
        if (StringHelper.hasText (sWord))
          aUnmatchedFilterWords.add (sWord);

      for (final ISelectFilterable aChainedFilterable : aFilterChain.getFilters ())
      {
        final ISelectFilter aChainedFilter = (ISelectFilter) aChainedFilterable;
        final String sEntryValue = getFilterValue (aObject, aChainedFilter.getColumn (), aDisplayLocale);

        // check which words are found in the current column
        final Set <String> aMatchedFilterWords = new HashSet <String> ();
        for (final String sFilterWord : aUnmatchedFilterWords)
          if (StringHelper.containsIgnoreCase (sEntryValue, sFilterWord, aDisplayLocale))
            aMatchedFilterWords.add (sFilterWord);

        // remove the found words since they do not have to be looked for in
        // other columns any more
        aUnmatchedFilterWords.removeAll (aMatchedFilterWords);
        // all filter words found?
        if (aUnmatchedFilterWords.isEmpty ())
          return true;
      }
      return false;
    }

    if (aFilter != null)
      s_aLogger.error ("Unsupported filter type: " + aFilter.toString ());
    return true;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IJSONObject> convertMatches (final List <T> aMatches,
                                            final List <String> aColumnKeys,
                                            final Locale aDisplayLocale)
  {
    final List <IJSONObject> aRows = new ArrayList <IJSONObject> ();
    for (final T aObject : aMatches)
    {
      final IJSONObject aRow = new JSONObject ();
      for (final String sColumnKey : aColumnKeys)
      {
        final String sValue = HCSettings.getAsHTMLString (getRenderValue (aObject, sColumnKey, aDisplayLocale), false);
        aRow.setStringProperty (sColumnKey, sValue);
      }
      aRows.add (aRow);
    }
    return aRows;
  }
}
