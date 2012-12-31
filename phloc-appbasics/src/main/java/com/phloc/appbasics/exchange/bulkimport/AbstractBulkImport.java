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
package com.phloc.appbasics.exchange.bulkimport;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.appbasics.exchange.EExchangeFileType;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.name.IHasDisplayText;

/**
 * Base class for {@link IBulkImport} implementations.
 * 
 * @author philip
 */
public abstract class AbstractBulkImport implements IBulkImport
{
  private final int m_nHeaderRowsToSkip;
  private final List <IHasDisplayText> m_aColumnNames;
  private final List <EExchangeFileType> m_aFileTypes;

  protected AbstractBulkImport (@Nonnegative final int nHeaderRowsToSkip,
                                @Nonnull @Nonempty final List <IHasDisplayText> aColumnNames,
                                @Nonnull @Nonempty final EExchangeFileType... aFileTypes)
  {
    if (nHeaderRowsToSkip < 0)
      throw new IllegalArgumentException ();
    if (ContainerHelper.isEmpty (aColumnNames))
      throw new IllegalArgumentException ("columns");
    if (ArrayHelper.isEmpty (aFileTypes))
      throw new IllegalArgumentException ("fileTypes");
    m_nHeaderRowsToSkip = nHeaderRowsToSkip;
    m_aColumnNames = aColumnNames;
    m_aFileTypes = ContainerHelper.newUnmodifiableList (ContainerHelper.newOrderedSet (aFileTypes));
  }

  @Nonnegative
  public final int getHeaderRowsToSkip ()
  {
    return m_nHeaderRowsToSkip;
  }

  @Nonnegative
  public final int getColumnCount ()
  {
    return m_aColumnNames.size ();
  }

  @Nonnull
  @Nonempty
  public final List <String> getColumnDescriptions (@Nonnull final Locale aContentLocale)
  {
    final List <String> ret = new ArrayList <String> (getColumnCount ());
    for (final IHasDisplayText aColumn : m_aColumnNames)
      ret.add (aColumn.getDisplayText (aContentLocale));
    return ret;
  }

  @Nonnull
  @Nonempty
  @ReturnsImmutableObject
  public final List <EExchangeFileType> getSupportedFileTypes ()
  {
    return m_aFileTypes;
  }
}
