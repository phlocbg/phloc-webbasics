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
package com.phloc.webctrls.datetime;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public interface IDateFormatBuilder
{
  /**
   * @return A non-<code>null</code> list with {@link EDateTimeFormatToken} and
   *         {@link Character} objects.
   */
  @Nonnull
  List <Object> getAllInternalObjects ();

  @Nonnull
  String getJSCalendarFormatString ();

  @Nonnull
  String getJavaFormatString ();

  @Nonnull
  LocalDate getDateFormatted (@Nullable String sDate);

  @Nonnull
  LocalTime getTimeFormatted (@Nullable String sTime);

  @Nonnull
  LocalDateTime getLocalDateTimeFormatted (@Nullable String sDateTime);

  @Nonnull
  DateTime getDateTimeFormatted (@Nullable String sDateTime);
}
