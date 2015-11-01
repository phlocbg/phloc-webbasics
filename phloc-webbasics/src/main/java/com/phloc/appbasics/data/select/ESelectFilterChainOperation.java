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
package com.phloc.appbasics.data.select;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

public enum ESelectFilterChainOperation
{
  @Nonnull
  AND ("AND"),
  @Nonnull
  OR ("OR");

  @Nonnull
  private final String m_sSQL;

  private ESelectFilterChainOperation (@Nonnull @Nonempty final String sSQL)
  {
    m_sSQL = sSQL;
  }

  @Nonnull
  @Nonempty
  public String getSQL ()
  {
    return m_sSQL;
  }
}
