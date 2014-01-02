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
package com.phloc.webctrls.datatables;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCHasCSSClasses;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.webctrls.datatables.comparator.AbstractComparatorTable;

public class DataTablesColumn implements IHCHasCSSClasses <DataTablesColumn>
{
  public static final boolean DEFAULT_SEARCHABLE = true;
  public static final boolean DEFAULT_SORTABLE = true;
  public static final boolean DEFAULT_VISIBLE = true;

  private final int [] m_aTargets;
  private boolean m_bSearchable = DEFAULT_SEARCHABLE;
  private boolean m_bSortable = DEFAULT_SORTABLE;
  private boolean m_bVisible = DEFAULT_VISIBLE;
  // Must be a LinkedHashSet:
  private Set <ICSSClassProvider> m_aCSSClassProviders;
  private String m_sName;
  private String m_sWidth;
  private int [] m_aDataSort;
  private AbstractComparatorTable m_aComparator;

  public DataTablesColumn (@Nonnegative final int nTarget)
  {
    m_aTargets = new int [] { nTarget };
  }

  public DataTablesColumn (@Nonnull @Nonempty final int... aTargets)
  {
    if (ArrayHelper.isEmpty (aTargets))
      throw new IllegalArgumentException ("targets may not be empty");
    for (final int nTarget : aTargets)
      if (nTarget < 0)
        throw new IllegalArgumentException ("Target must be >= 0: " + nTarget);
    m_aTargets = ArrayHelper.getCopy (aTargets);
  }

  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public int [] getAllTargets ()
  {
    return ArrayHelper.getCopy (m_aTargets);
  }

  public boolean hasTarget (final int nTarget)
  {
    return ArrayHelper.contains (m_aTargets, nTarget);
  }

  public boolean isSearchable ()
  {
    return m_bSearchable;
  }

  @Nonnull
  public DataTablesColumn setSearchable (final boolean bSearchable)
  {
    m_bSearchable = bSearchable;
    return this;
  }

  public boolean isSortable ()
  {
    return m_bSortable;
  }

  @Nonnull
  public DataTablesColumn setSortable (final boolean bSortable)
  {
    m_bSortable = bSortable;
    return this;
  }

  public boolean isVisible ()
  {
    return m_bVisible;
  }

  @Nonnull
  public DataTablesColumn setVisible (final boolean bVisible)
  {
    m_bVisible = bVisible;
    return this;
  }

  public boolean containsClass (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    return m_aCSSClassProviders != null &&
           aCSSClassProvider != null &&
           m_aCSSClassProviders.contains (aCSSClassProvider);
  }

  @Nonnull
  public DataTablesColumn addClass (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    if (aCSSClassProvider != null)
    {
      if (m_aCSSClassProviders == null)
        m_aCSSClassProviders = new LinkedHashSet <ICSSClassProvider> ();
      m_aCSSClassProviders.add (aCSSClassProvider);
    }
    return this;
  }

  @Deprecated
  @Nonnull
  public DataTablesColumn addClasses (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    return addClass (aCSSClassProvider);
  }

  @Nonnull
  public DataTablesColumn addClasses (@Nullable final ICSSClassProvider... aCSSClassProviders)
  {
    if (aCSSClassProviders != null)
      for (final ICSSClassProvider aProvider : aCSSClassProviders)
        addClass (aProvider);
    return this;
  }

  @Nonnull
  public DataTablesColumn addClasses (@Nullable final Iterable <? extends ICSSClassProvider> aCSSClassProviders)
  {
    if (aCSSClassProviders != null)
      for (final ICSSClassProvider aProvider : aCSSClassProviders)
        addClass (aProvider);
    return this;
  }

  @Nonnull
  public DataTablesColumn removeClass (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    if (m_aCSSClassProviders != null && aCSSClassProvider != null)
      m_aCSSClassProviders.remove (aCSSClassProvider);
    return this;
  }

  @Nonnull
  public DataTablesColumn removeAllClasses ()
  {
    if (m_aCSSClassProviders != null)
      m_aCSSClassProviders.clear ();
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <ICSSClassProvider> getAllClasses ()
  {
    return ContainerHelper.newOrderedSet (m_aCSSClassProviders);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllClassNames ()
  {
    final Set <String> ret = new LinkedHashSet <String> ();
    if (m_aCSSClassProviders != null)
      for (final ICSSClassProvider aCSSClassProvider : m_aCSSClassProviders)
      {
        final String sCSSClass = aCSSClassProvider.getCSSClass ();
        if (StringHelper.hasText (sCSSClass))
          ret.add (sCSSClass);
      }
    return ret;
  }

  @Nullable
  public String getAllClassesAsString ()
  {
    if (m_aCSSClassProviders == null || m_aCSSClassProviders.isEmpty ())
      return null;

    final StringBuilder aSB = new StringBuilder ();
    for (final ICSSClassProvider aCSSClassProvider : m_aCSSClassProviders)
    {
      final String sCSSClass = aCSSClassProvider.getCSSClass ();
      if (StringHelper.hasText (sCSSClass))
      {
        if (aSB.length () > 0)
          aSB.append (' ');
        aSB.append (sCSSClass);
      }
    }
    return aSB.toString ();
  }

  public boolean hasAnyClass ()
  {
    return m_aCSSClassProviders != null && !m_aCSSClassProviders.isEmpty ();
  }

  @Nullable
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public DataTablesColumn setName (@Nullable final String sName)
  {
    m_sName = sName;
    return this;
  }

  @Nullable
  public String getWidth ()
  {
    return m_sWidth;
  }

  @Nonnull
  public DataTablesColumn setWidth (@Nullable final String sWidth)
  {
    m_sWidth = sWidth;
    return this;
  }

  @Nullable
  @ReturnsMutableCopy
  public int [] getDataSort ()
  {
    return ArrayHelper.getCopy (m_aDataSort);
  }

  /**
   * Set the column indices to sort, when this column is sorted
   * 
   * @param aDataSort
   *        The sorting column (incl. this column!)
   * @return this
   */
  @Nonnull
  public DataTablesColumn setDataSort (@Nullable final int... aDataSort)
  {
    m_aDataSort = ArrayHelper.getCopy (aDataSort);
    return this;
  }

  @Nullable
  public AbstractComparatorTable getComparator ()
  {
    return m_aComparator;
  }

  @Nonnull
  public DataTablesColumn setComparator (@Nullable final AbstractComparatorTable aComparator)
  {
    m_aComparator = aComparator;
    return this;
  }

  @Nonnull
  public JSAssocArray getAsJS ()
  {
    final JSAssocArray ret = new JSAssocArray ();
    ret.add ("aTargets", new JSArray ().addAll (m_aTargets));
    if (m_bSearchable != DEFAULT_SEARCHABLE)
      ret.add ("bSearchable", m_bSearchable);
    if (m_bSortable != DEFAULT_SORTABLE)
      ret.add ("bSortable", m_bSortable);
    if (m_bVisible != DEFAULT_VISIBLE)
      ret.add ("bVisible", m_bVisible);
    final String sClasses = getAllClassesAsString ();
    if (StringHelper.hasText (sClasses))
      ret.add ("sClass", sClasses);
    if (StringHelper.hasText (m_sName))
      ret.add ("sName", m_sName);
    if (StringHelper.hasText (m_sWidth))
      ret.add ("sWidth", m_sWidth);
    if (ArrayHelper.isNotEmpty (m_aDataSort))
      ret.add ("aDataSort", new JSArray ().addAll (m_aDataSort));
    return ret;
  }
}
