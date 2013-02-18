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
package com.phloc.webctrls.datatables.ajax;

import javax.annotation.Nullable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.webctrls.datatables.DataTables;

/**
 * The search/filter state of a {@link DataTables} object.
 * 
 * @author philip
 */
final class ServerFilterState
{
  private final String m_sSearchText;
  private final boolean m_bSearchRegEx;

  public ServerFilterState ()
  {
    this (null, false);
  }

  public ServerFilterState (@Nullable final String sSearchText, final boolean bSearchRegEx)
  {
    m_sSearchText = StringHelper.hasNoText (sSearchText) ? null : sSearchText;
    m_bSearchRegEx = bSearchRegEx;
  }

  public boolean hasSearchText ()
  {
    return StringHelper.hasText (m_sSearchText);
  }

  @Nullable
  public String getSearchText ()
  {
    return m_sSearchText;
  }

  public boolean isSearchRegEx ()
  {
    return m_bSearchRegEx;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ServerFilterState))
      return false;
    final ServerFilterState rhs = (ServerFilterState) o;
    return EqualsUtils.equals (m_sSearchText, rhs.m_sSearchText) && m_bSearchRegEx == rhs.m_bSearchRegEx;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sSearchText).append (m_bSearchRegEx).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("searchText", m_sSearchText)
                                       .append ("searchRegEx", m_bSearchRegEx)
                                       .toString ();
  }
}
