/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.exchange.bulkexport;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of {@link IExportRecord}.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class ExportRecord implements IExportRecord
{
  private final List <IExportRecordField> m_aFields = new ArrayList <IExportRecordField> ();

  public ExportRecord ()
  {}

  public ExportRecord (@Nonnull final IExportRecordField... aFields)
  {
    ValueEnforcer.notNull (aFields, "Fields");

    for (final IExportRecordField aField : aFields)
      addField (aField);
  }

  public ExportRecord (@Nonnull final Iterable <? extends IExportRecordField> aFields)
  {
    ValueEnforcer.notNull (aFields, "Fields");

    for (final IExportRecordField aField : aFields)
      addField (aField);
  }

  @Nonnull
  public EChange removeFieldAtIndex (@Nonnegative final int nIndex)
  {
    return ContainerHelper.removeElementAtIndex (m_aFields, nIndex);
  }

  @Nonnull
  public ExportRecord addField (@Nonnull final IExportRecordField aField)
  {
    ValueEnforcer.notNull (aField, "Field");
    m_aFields.add (aField);
    return this;
  }

  @Nonnull
  public ExportRecord addField (@Nullable final Object aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (@Nullable final String sValue)
  {
    return addField (ExportRecordField.create (sValue));
  }

  @Nonnull
  public ExportRecord addField (@Nullable final LocalDate aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (@Nullable final LocalTime aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (@Nullable final LocalDateTime aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (@Nullable final DateTime aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (final boolean bValue)
  {
    return addField (ExportRecordField.create (bValue));
  }

  @Nonnull
  public ExportRecord addField (final Boolean aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (final int nValue)
  {
    return addField (ExportRecordField.create (nValue));
  }

  @Nonnull
  public ExportRecord addField (final Integer aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (final long nValue)
  {
    return addField (ExportRecordField.create (nValue));
  }

  @Nonnull
  public ExportRecord addField (@Nonnull final Long aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (@Nullable final BigInteger aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (final double dValue)
  {
    return addField (ExportRecordField.create (dValue));
  }

  @Nonnull
  public ExportRecord addField (@Nonnull final Double aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Nonnull
  public ExportRecord addField (@Nullable final BigDecimal aValue)
  {
    return addField (ExportRecordField.create (aValue));
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <IExportRecordField> getAllFields ()
  {
    return ContainerHelper.newList (m_aFields);
  }

  @Override
  public boolean hasFields ()
  {
    return !m_aFields.isEmpty ();
  }

  @Override
  @Nonnegative
  public int getFieldCount ()
  {
    return m_aFields.size ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ExportRecord))
      return false;
    final ExportRecord rhs = (ExportRecord) o;
    return m_aFields.equals (rhs.m_aFields);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFields).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("fields", m_aFields).toString ();
  }
}
