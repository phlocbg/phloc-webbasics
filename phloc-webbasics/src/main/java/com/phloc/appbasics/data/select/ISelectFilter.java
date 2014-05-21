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
import javax.annotation.Nullable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.annotations.Nonempty;

@MustImplementEqualsAndHashcode
public interface ISelectFilter extends ISelectFilterable
{
  @Nonnull
  @Nonempty
  String getColumn ();

  @Nonnull
  ESelectFilterOperation getOperation ();

  /**
   * @return The SQL value to filter for. E.g. for LIKE it is prefixed and
   *         suffixed with a "%"
   */
  @Nullable
  Object getFilterValueSQL ();

  /**
   * @return For SQL commands that have have a second parameter (like BETWEEN)
   */
  @Nullable
  Object getFilterValueSQL2 ();
}