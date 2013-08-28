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
package com.phloc.webctrls.bootstrap3.grid;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;

/**
 * Bootstrap spans
 * 
 * @author Philip Helger
 */
public enum EBootstrap3GridLG implements ICSSClassProvider
{
  // Large - Desktops (≥1200px)
  LG_1 (1, CBootstrap3CSS.COL_LG_1),
  LG_2 (2, CBootstrap3CSS.COL_LG_2),
  LG_3 (3, CBootstrap3CSS.COL_LG_3),
  LG_4 (4, CBootstrap3CSS.COL_LG_4),
  LG_5 (5, CBootstrap3CSS.COL_LG_5),
  LG_6 (6, CBootstrap3CSS.COL_LG_6),
  LG_7 (7, CBootstrap3CSS.COL_LG_7),
  LG_8 (8, CBootstrap3CSS.COL_LG_8),
  LG_9 (9, CBootstrap3CSS.COL_LG_9),
  LG_10 (10, CBootstrap3CSS.COL_LG_10),
  LG_11 (11, CBootstrap3CSS.COL_LG_11),
  LG_12 (12, CBootstrap3CSS.COL_LG_12);

  private final int m_nParts;
  private final ICSSClassProvider m_aCSSClass;

  private EBootstrap3GridLG (@Nonnegative final int nParts, @Nonnull final ICSSClassProvider aCSSClass)
  {
    m_nParts = nParts;
    m_aCSSClass = aCSSClass;
  }

  @Nonnegative
  public int getParts ()
  {
    return m_nParts;
  }

  @Nonnull
  @Nonempty
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }
}
