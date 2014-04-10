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
package com.phloc.webbasics.form;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.datetime.format.PDTToString;
import com.phloc.datetime.xml.PDTXMLConverter;

/**
 * Special request field specially for dates.
 *
 * @author Philip Helger
 */
public class RequestFieldDate extends RequestField
{
  private final Locale m_aDisplayLocale;

  private RequestFieldDate (@Nonnull @Nonempty final String sFieldName,
                            @Nullable final String sDefaultValue,
                            @Nonnull final Locale aDisplayLocale)
  {
    super (sFieldName, sDefaultValue);
    m_aDisplayLocale = ValueEnforcer.notNull (aDisplayLocale, "DisplayLocale");
  }

  public RequestFieldDate (@Nonnull @Nonempty final String sFieldName, @Nonnull final Locale aDisplayLocale)
  {
    this (sFieldName, (String) null, aDisplayLocale);
  }

  public RequestFieldDate (@Nonnull @Nonempty final String sFieldName,
                           @Nullable final LocalDate aDefaultValue,
                           @Nonnull final Locale aDisplayLocale)
  {
    this (sFieldName, PDTToString.getAsString (aDefaultValue, aDisplayLocale), aDisplayLocale);
  }

  public RequestFieldDate (@Nonnull @Nonempty final String sFieldName,
                           @Nullable final LocalDateTime aDefaultValue,
                           @Nonnull final Locale aDisplayLocale)
  {
    this (sFieldName, PDTToString.getAsString (aDefaultValue, aDisplayLocale), aDisplayLocale);
  }

  public RequestFieldDate (@Nonnull @Nonempty final String sFieldName,
                           @Nullable final DateTime aDefaultValue,
                           @Nonnull final Locale aDisplayLocale)
  {
    this (sFieldName, PDTToString.getAsString (aDefaultValue, aDisplayLocale), aDisplayLocale);
  }

  public RequestFieldDate (@Nonnull @Nonempty final String sFieldName,
                           @Nullable final XMLGregorianCalendar aDefaultValue,
                           @Nonnull final Locale aDisplayLocale)
  {
    this (sFieldName, PDTXMLConverter.getDateTime (aDefaultValue), aDisplayLocale);
  }

  @Nonnull
  public Locale getDisplayLocale ()
  {
    return m_aDisplayLocale;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final RequestFieldDate rhs = (RequestFieldDate) o;
    return m_aDisplayLocale.equals (rhs.m_aDisplayLocale);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aDisplayLocale).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("displayLocale", m_aDisplayLocale).toString ();
  }
}
