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
package com.phloc.appbasics.app.menu.filter;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This filter wraps a set of several filters and ensures that at least one
 * contained filter ist matching.
 *
 * @author Philip Helger
 */
public final class MenuItemFilterListOne extends AbstractMenuObjectFilter
{
  private final List <AbstractMenuObjectFilter> m_aFilters;

  public MenuItemFilterListOne (@Nonnull @Nonempty final Collection <? extends AbstractMenuObjectFilter> aFilters)
  {
    ValueEnforcer.notEmpty (aFilters, "Filters");
    m_aFilters = ContainerHelper.newList (aFilters);
  }

  public MenuItemFilterListOne (@Nonnull @Nonempty final AbstractMenuObjectFilter... aFilters)
  {
    ValueEnforcer.notEmpty (aFilters, "Filters");
    m_aFilters = ContainerHelper.newList (aFilters);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <AbstractMenuObjectFilter> getAllFilters ()
  {
    return ContainerHelper.newList (m_aFilters);
  }

  public boolean matchesFilter (@Nullable final IMenuObject aValue)
  {
    for (final AbstractMenuObjectFilter aFilter : m_aFilters)
      if (aFilter.matchesFilter (aValue))
        return true;
    return false;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("filters", m_aFilters).toString ();
  }
}
