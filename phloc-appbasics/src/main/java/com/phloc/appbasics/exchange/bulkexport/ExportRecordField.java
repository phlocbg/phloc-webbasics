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
package com.phloc.appbasics.exchange.bulkexport;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.types.EBaseType;

/**
 * A single field for exporting.
 * 
 * @author philip
 */
@Immutable
public final class ExportRecordField implements IExportRecordField
{
  private final EBaseType m_eFieldType;
  private final Object m_aValue;

  private ExportRecordField (@Nonnull final EBaseType eFieldType, @Nullable final Object aValue)
  {
    if (eFieldType == null)
      throw new NullPointerException ("fieldType");
    m_eFieldType = eFieldType;
    m_aValue = aValue;
  }

  @Nonnull
  public EBaseType getFieldType ()
  {
    return m_eFieldType;
  }

  @Nullable
  public Object getFieldValue ()
  {
    return m_aValue;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ExportRecordField))
      return false;
    final ExportRecordField rhs = (ExportRecordField) o;
    return m_eFieldType.equals (rhs.m_eFieldType) && EqualsUtils.equals (m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eFieldType).append (m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("fieldType", m_eFieldType).append ("value", m_aValue).toString ();
  }

  @Nonnull
  private static EBaseType _autoDetermineType (@Nullable final Object aValue)
  {
    if (aValue != null)
    {
      if (aValue instanceof LocalDate)
        return EBaseType.DATE;
      if (aValue instanceof LocalTime)
        return EBaseType.TIME;
      if (aValue instanceof LocalDateTime || aValue instanceof DateTime)
        return EBaseType.DATETIME;
      if (aValue instanceof Boolean)
        return EBaseType.BOOLEAN;
      if (aValue instanceof Byte ||
          aValue instanceof Short ||
          aValue instanceof Integer ||
          aValue instanceof Long ||
          aValue instanceof BigInteger)
        return EBaseType.INT;
      if (aValue instanceof Float || aValue instanceof Double || aValue instanceof BigDecimal)
        return EBaseType.DOUBLE;
    }

    // Default to text
    return EBaseType.TEXT;
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final Object aValue)
  {
    return new ExportRecordField (_autoDetermineType (aValue), aValue);
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final String sValue)
  {
    return new ExportRecordField (EBaseType.TEXT, sValue);
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final LocalDate aValue)
  {
    return new ExportRecordField (EBaseType.DATE, aValue);
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final LocalTime aValue)
  {
    return new ExportRecordField (EBaseType.TIME, aValue);
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final LocalDateTime aValue)
  {
    return new ExportRecordField (EBaseType.DATETIME, aValue);
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final DateTime aValue)
  {
    return new ExportRecordField (EBaseType.DATETIME, aValue);
  }

  @Nonnull
  public static ExportRecordField create (final boolean bValue)
  {
    return create (Boolean.valueOf (bValue));
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final Boolean aValue)
  {
    return new ExportRecordField (EBaseType.BOOLEAN, aValue);
  }

  @Nonnull
  public static ExportRecordField create (final int nValue)
  {
    return create (Integer.valueOf (nValue));
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final Integer aValue)
  {
    return new ExportRecordField (EBaseType.INT, aValue);
  }

  @Nonnull
  public static ExportRecordField create (final long nValue)
  {
    return create (Long.valueOf (nValue));
  }

  @Nonnull
  public static ExportRecordField create (@Nonnull final Long aValue)
  {
    return new ExportRecordField (EBaseType.INT, aValue);
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final BigInteger aValue)
  {
    return new ExportRecordField (EBaseType.INT, aValue);
  }

  @Nonnull
  public static ExportRecordField create (final double dValue)
  {
    return create (Double.valueOf (dValue));
  }

  @Nonnull
  public static ExportRecordField create (@Nonnull final Double aValue)
  {
    return new ExportRecordField (EBaseType.DOUBLE, aValue);
  }

  @Nonnull
  public static ExportRecordField create (@Nullable final BigDecimal aValue)
  {
    return new ExportRecordField (EBaseType.DOUBLE, aValue);
  }
}
