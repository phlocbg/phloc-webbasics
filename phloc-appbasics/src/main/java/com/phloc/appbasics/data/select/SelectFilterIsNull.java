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
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public final class SelectFilterIsNull implements ISelectFilter
{
  private final String m_sColumn;

  public SelectFilterIsNull (@Nonnull @Nonempty final String sColumn)
  {
    if (StringHelper.hasNoText (sColumn))
      throw new IllegalArgumentException ("column name may not be empty");
    m_sColumn = sColumn;
  }

  @Nonnull
  @Nonempty
  public String getColumn ()
  {
    return m_sColumn;
  }

  @Nonnull
  public ESelectFilterOperation getOperation ()
  {
    return ESelectFilterOperation.IS_NULL;
  }

  @UnsupportedOperation
  public Object getFilterValueSQL ()
  {
    throw new UnsupportedOperationException ();
  }

  @UnsupportedOperation
  public Object getFilterValueSQL2 ()
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SelectFilterIsNull))
      return false;
    final SelectFilterIsNull rhs = (SelectFilterIsNull) o;
    return m_sColumn.equals (rhs.m_sColumn);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sColumn).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("column", m_sColumn).toString ();
  }
}
