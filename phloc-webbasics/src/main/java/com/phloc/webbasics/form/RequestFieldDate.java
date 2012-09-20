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
package com.phloc.webbasics.form;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.datetime.format.PDTToString;

/**
 * Special request field specially for dates.
 * 
 * @author philip
 */
public final class RequestFieldDate extends RequestField
{
  private final LocalDate m_aDefaultValue;
  private final Locale m_aDisplayLocale;

  public RequestFieldDate (@Nonnull @Nonempty final String sFieldName,
                           @Nullable final LocalDate aDefaultValue,
                           @Nonnull final Locale aDisplayLocale)
  {
    super (sFieldName, PDTToString.getAsString (aDefaultValue, aDisplayLocale));
    m_aDefaultValue = aDefaultValue;
    m_aDisplayLocale = aDisplayLocale;
  }

  @Nonnull
  public LocalDate getDateDefaultValue ()
  {
    return m_aDefaultValue;
  }

  @Nonnull
  public Locale getDisplayLocale ()
  {
    return m_aDisplayLocale;
  }
}
