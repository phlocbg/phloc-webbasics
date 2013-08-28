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
public enum EBootstrap3GridMD implements ICSSClassProvider
{
  // Medium - Desktops (≥992px)
  MD_1 (1, CBootstrap3CSS.COL_MD_1),
  MD_2 (2, CBootstrap3CSS.COL_MD_2),
  MD_3 (3, CBootstrap3CSS.COL_MD_3),
  MD_4 (4, CBootstrap3CSS.COL_MD_4),
  MD_5 (5, CBootstrap3CSS.COL_MD_5),
  MD_6 (6, CBootstrap3CSS.COL_MD_6),
  MD_7 (7, CBootstrap3CSS.COL_MD_7),
  MD_8 (8, CBootstrap3CSS.COL_MD_8),
  MD_9 (9, CBootstrap3CSS.COL_MD_9),
  MD_10 (10, CBootstrap3CSS.COL_MD_10),
  MD_11 (11, CBootstrap3CSS.COL_MD_11),
  MD_12 (12, CBootstrap3CSS.COL_MD_12);

  private final int m_nParts;
  private final ICSSClassProvider m_aCSSClass;

  private EBootstrap3GridMD (@Nonnegative final int nParts, @Nonnull final ICSSClassProvider aCSSClass)
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
