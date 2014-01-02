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

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public final class SelectFilterChainOR implements ISelectFilterChain
{
  private final List <ISelectFilterable> m_aFilters;

  public SelectFilterChainOR (@Nonnull @Nonempty final List <ISelectFilterable> aFilters)
  {
    if (ContainerHelper.isEmpty (aFilters))
      throw new IllegalArgumentException ("filters may not be empty");
    if (ContainerHelper.containsAnyNullElement (aFilters))
      throw new IllegalArgumentException ("filters may not contain null elements");
    m_aFilters = ContainerHelper.makeUnmodifiable (aFilters);
  }

  public SelectFilterChainOR (@Nonnull @Nonempty final ISelectFilterable... aFilters)
  {
    if (ArrayHelper.isEmpty (aFilters))
      throw new IllegalArgumentException ("filters may not be empty");
    if (ArrayHelper.containsAnyNullElement (aFilters))
      throw new IllegalArgumentException ("filters may not contain null elements");
    m_aFilters = ContainerHelper.newUnmodifiableList (aFilters);
  }

  @Nonnegative
  public int getFilterCount ()
  {
    return m_aFilters.size ();
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <ISelectFilterable> getFilters ()
  {
    // ESCA-JAVA0259:
    return m_aFilters;
  }

  @Nonnull
  public ESelectFilterChainOperation getChainOperation ()
  {
    return ESelectFilterChainOperation.OR;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SelectFilterChainOR))
      return false;
    final SelectFilterChainOR rhs = (SelectFilterChainOR) o;
    return m_aFilters.equals (rhs.m_aFilters);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFilters).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("filters", m_aFilters).toString ();
  }
}
