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
package com.phloc.appbasics.data.select;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;

@Immutable
public final class SelectFilterLikeCaseInsensitive extends SelectFilterLike
{
  /**
   * @param sColumn
   *        The column to filter. May neither be <code>null</code> nor empty.
   * @param sFilterValue
   *        The value to filter. May not be <code>null</code>.
   * @param aLocale
   *        the locale to use for upper/lower case conversions. May not be
   *        <code>null</code>.
   */
  public SelectFilterLikeCaseInsensitive (@Nonnull @Nonempty final String sColumn,
                                          @Nonnull final String sFilterValue,
                                          @Nonnull final Locale aLocale)
  {
    super (sColumn, sFilterValue.toUpperCase (aLocale));
  }
}
