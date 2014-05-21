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
package com.phloc.appbasics.data.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class aggregates the different select constraints.<br>
 * They have to be applied in the following order:
 * <ul>
 * <li>Filter</li>
 * <li>Sort</li>
 * <li>Limit</li>
 * </ul>
 * 
 * @author Philip Helger
 */
public final class SelectConstraints implements IHasOrderAndLimit
{
  private ISelectFilterable m_aFilter;
  private SelectOrderBy m_aOrderBy;
  private SelectLimit m_aLimit;

  public SelectConstraints ()
  {}

  @Nonnull
  public SelectConstraints setFilter (@Nullable final ISelectFilterable aFilter)
  {
    m_aFilter = aFilter;
    return this;
  }

  @Nullable
  public ISelectFilterable getFilter ()
  {
    return m_aFilter;
  }

  @Nonnull
  public SelectConstraints setOrderBy (@Nullable final SelectOrderBy aOrderBy)
  {
    m_aOrderBy = aOrderBy;
    return this;
  }

  @Nullable
  public SelectOrderBy getOrderBy ()
  {
    return m_aOrderBy;
  }

  @Nonnull
  public SelectConstraints setLimit (@Nonnull final SelectLimit aLimit)
  {
    m_aLimit = ValueEnforcer.notNull (aLimit, "Limit");
    return this;
  }

  @Nullable
  public SelectLimit getLimit ()
  {
    return m_aLimit;
  }

  public boolean isAtLeastOneConstraintDefined ()
  {
    return m_aFilter != null || m_aOrderBy != null || m_aLimit != null;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("filter", m_aFilter)
                                       .append ("orderBy", m_aOrderBy)
                                       .append ("limit", m_aLimit)
                                       .toString ();
  }
}
