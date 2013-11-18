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
package com.phloc.webctrls.typeahead;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json2.impl.JsonArray;
import com.phloc.json2.impl.JsonObject;
import com.phloc.webbasics.ajax.AbstractAjaxHandler;
import com.phloc.webbasics.ajax.AjaxDefaultResponse;
import com.phloc.webbasics.ajax.IAjaxResponse;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Abstract AJAX handler that can be used as the source for a Bootstrap
 * typeahead control.
 * 
 * @author Philip Helger
 */
public abstract class AbstractAjaxHandlerTypeaheadFinder extends AbstractAjaxHandler
{
  public static final String PARAM_QUERY = "query";

  /**
   * A simple finder
   * 
   * @author Philip Helger
   */
  @NotThreadSafe
  public static class Finder
  {
    private final String [] m_aSearchTerms;
    private final Locale m_aSortLocale;

    /**
     * Constructor.
     * 
     * @param sSearchTerms
     *        The search term string. It is internally separated into multiple
     *        tokens by using a "\s+" regular expression.
     * @param aSortLocale
     *        The sort locale to use. May not be <code>null</code>.
     */
    public Finder (@Nonnull @Nonempty final String sSearchTerms, @Nonnull final Locale aSortLocale)
    {
      if (StringHelper.hasNoTextAfterTrim (sSearchTerms))
        throw new IllegalArgumentException ("SearchTerms");
      if (aSortLocale == null)
        throw new NullPointerException ("sortLocale");

      // Split search terms by white spaces
      m_aSearchTerms = RegExHelper.getSplitToArray (sSearchTerms.trim (), "\\s+");
      if (m_aSearchTerms.length == 0)
        throw new IllegalStateException ("Weird - splitting of '" + sSearchTerms.trim () + "' failed!");
      for (final String sSearchTerm : m_aSearchTerms)
        if (sSearchTerm.length () == 0)
          throw new IllegalArgumentException ("Weird - empty search term present!");
      m_aSortLocale = aSortLocale;
    }

    /**
     * @return An array with all search terms. Never <code>null</code> nor
     *         empty.
     */
    @Nonnull
    @ReturnsMutableCopy
    @Nonempty
    public final String [] getAllSearchTerms ()
    {
      return ArrayHelper.getCopy (m_aSearchTerms);
    }

    /**
     * @return The sort locale provided in the constructor. Never
     *         <code>null</code>.
     */
    @Nonnull
    public final Locale getSortLocale ()
    {
      return m_aSortLocale;
    }

    /**
     * Default matches function: match all search terms!
     * 
     * @param sSource
     *        Source string. May be <code>null</code>.
     * @return <code>true</code> if the source is not <code>null</code> and if
     *         all search terms are contained, <code>false</code> otherwise.
     * @see #matchesAll(String)
     */
    public boolean matches (@Nullable final String sSource)
    {
      return matchesAll (sSource);
    }

    /**
     * Match method for a single string. By default a case-insensitive lookup is
     * performed.
     * 
     * @param sSource
     *        The source string to search the search term in. Never
     *        <code>null</code>.
     * @param sSearchTerm
     *        The search term to be searched. Never <code>null</code>.
     * @return <code>true</code> if the source string contains the search term,
     *         <code>false</code> otherwise.
     */
    @OverrideOnDemand
    protected boolean isSingleStringMatching (@Nonnull final String sSource, @Nonnull final String sSearchTerm)
    {
      return StringHelper.containsIgnoreCase (sSource, sSearchTerm, m_aSortLocale);
    }

    /**
     * Check if the passed source string matches all query terms.
     * 
     * @param sSource
     *        Source string. May be <code>null</code>.
     * @return <code>true</code> if the source is not <code>null</code> and if
     *         all search terms are contained, <code>false</code> otherwise.
     */
    public boolean matchesAll (@Nullable final String sSource)
    {
      if (sSource == null)
        return false;
      for (final String sSearchTerm : m_aSearchTerms)
        if (!isSingleStringMatching (sSource, sSearchTerm))
          return false;
      return true;
    }

    /**
     * Check if the passed source string matches at least one query term.
     * 
     * @param sSource
     *        Source string. May be <code>null</code>.
     * @return <code>true</code> if the source is not <code>null</code> and if
     *         one search term is contained, <code>false</code> otherwise.
     */
    public boolean matchesAny (@Nullable final String sSource)
    {
      if (sSource == null)
        return false;
      for (final String sSearchTerm : m_aSearchTerms)
        if (isSingleStringMatching (sSource, sSearchTerm))
          return true;
      return false;
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("searchTerms", m_aSearchTerms)
                                         .append ("sortLocale", m_aSortLocale)
                                         .toString ();
    }
  }

  /**
   * Create a new {@link Finder} object.
   * 
   * @param sOriginalQuery
   *        The original query string. Never <code>null</code>.
   * @param aSortLocale
   *        The sort locale to be used. Never <code>null</code>.
   * @return The non-<code>null</code> {@link Finder} object.
   */
  @Nonnull
  @OverrideOnDemand
  protected Finder createFinder (@Nonnull final String sOriginalQuery, @Nonnull final Locale aSortLocale)
  {
    return new Finder (sOriginalQuery, aSortLocale);
  }

  /**
   * This is the main searcher method. It must filter all objects matching the
   * criteria in the finder.
   * 
   * @param aFinder
   *        The finder. Never <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @return A non-<code>null</code> list with all datums to use.
   */
  @Nonnull
  protected abstract List <? extends TypeaheadDatum> getAllMatchingDatums (@Nonnull Finder aFinder,
                                                                           @Nonnull Locale aDisplayLocale);

  @Override
  @Nonnull
  protected final IAjaxResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                   @Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {
    final Locale aDisplayLocale = ApplicationRequestManager.getInstance ().getRequestDisplayLocale ();

    final String sOriginalQuery = aParams.getAttributeAsString (PARAM_QUERY);
    if (StringHelper.hasNoTextAfterTrim (sOriginalQuery))
    {
      // May happen when the user enters "  " (only spaces)
      return AjaxDefaultResponse.createSuccess (new JsonObject ());
    }

    // Create the main Finder object
    final Finder aFinder = createFinder (sOriginalQuery, aDisplayLocale);

    // Map from ID to name
    final List <? extends TypeaheadDatum> aMatchingDatums = getAllMatchingDatums (aFinder, aDisplayLocale);

    // Convert to JSON, sorted by display name using the current display locale
    final JsonArray ret = new JsonArray ();
    for (final TypeaheadDatum aDatum : aMatchingDatums)
      ret.add (aDatum.getAsJson ());

    // Set as result property
    return AjaxDefaultResponse.createSuccess (ret);
  }
}
