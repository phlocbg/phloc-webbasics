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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;

public enum ESelectFilterOperation
{
  EQUALS ("=", null, 1),
  LIKE ("LIKE", null, 1),
  BETWEEN ("BETWEEN", "AND", 2),
  IS_NULL ("IS NULL", null, 0),
  NOT_NULL ("IS NOT NULL", null, 0);

  private final String m_sSQL;
  private final String m_sSQL2;
  private final int m_nParamCount;

  private ESelectFilterOperation (@Nonnull @Nonempty final String sSQL,
                                  @Nullable final String sSQL2,
                                  @Nonnegative final int nParamCount)
  {
    m_sSQL = sSQL;
    m_sSQL2 = sSQL2;
    m_nParamCount = nParamCount;
  }

  /**
   * @return The main SQL statement
   */
  @Nonnull
  @Nonempty
  public String getSQL ()
  {
    return m_sSQL;
  }

  /**
   * @return An optional second SQL statement (e.g. for BETWEEN ... AND ...)
   */
  @Nullable
  public String getSQL2 ()
  {
    return m_sSQL2;
  }

  public boolean hasParameter ()
  {
    return m_nParamCount > 0;
  }

  @Nonnegative
  public int getParameterCount ()
  {
    return m_nParamCount;
  }
}
