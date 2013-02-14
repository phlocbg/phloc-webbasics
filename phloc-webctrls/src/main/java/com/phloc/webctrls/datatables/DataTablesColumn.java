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
package com.phloc.webctrls.datatables;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;

public class DataTablesColumn
{
  public static final boolean DEFAULT_SEARCHABLE = true;
  public static final boolean DEFAULT_SORTABLE = true;
  public static final boolean DEFAULT_VISIBLE = true;

  private final int [] m_aTargets;
  private boolean m_bSearchable = DEFAULT_SEARCHABLE;
  private boolean m_bSortable = DEFAULT_SORTABLE;
  private boolean m_bVisible = DEFAULT_VISIBLE;
  private ICSSClassProvider m_aCSSClassProvider;
  private String m_sName;
  private IDataTablesColumnType m_aType;
  private String m_sWidth;
  private int [] m_aDataSort;

  public DataTablesColumn (final int nTarget)
  {
    m_aTargets = new int [] { nTarget };
  }

  public DataTablesColumn (@Nonnull @Nonempty final int... aTargets)
  {
    if (ArrayHelper.isEmpty (aTargets))
      throw new IllegalArgumentException ("targets may not be empty");
    m_aTargets = ArrayHelper.getCopy (aTargets);
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

  @Nullable
  public ICSSClassProvider getCSSClass ()
  {
    return m_aCSSClassProvider;
  }

  @Nonnull
  public DataTablesColumn setCSSClass (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    m_aCSSClassProvider = aCSSClassProvider;
    return this;
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
  public IDataTablesColumnType getType ()
  {
    return m_aType;
  }

  @Nonnull
  public DataTablesColumn setType (@Nullable final IDataTablesColumnType aType)
  {
    m_aType = aType;
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
    if (m_aCSSClassProvider != null)
      ret.add ("sClass", m_aCSSClassProvider.getCSSClass ());
    if (StringHelper.hasText (m_sName))
      ret.add ("sName", m_sName);
    if (m_aType != null)
      ret.add ("sType", m_aType.getName ());
    if (StringHelper.hasText (m_sWidth))
      ret.add ("sWidth", m_sWidth);
    if (ArrayHelper.isNotEmpty (m_aDataSort))
      ret.add ("aDataSort", new JSArray ().addAll (m_aDataSort));
    return ret;
  }
}
