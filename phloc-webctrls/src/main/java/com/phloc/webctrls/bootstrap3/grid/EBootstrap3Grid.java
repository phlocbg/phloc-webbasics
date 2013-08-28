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
public enum EBootstrap3Grid implements ICSSClassProvider
{
  // Extra small - Phones (<768px)
  XS_1 (1, CBootstrap3CSS.COL_XS_1),
  XS_2 (2, CBootstrap3CSS.COL_XS_2),
  XS_3 (3, CBootstrap3CSS.COL_XS_3),
  XS_4 (4, CBootstrap3CSS.COL_XS_4),
  XS_5 (5, CBootstrap3CSS.COL_XS_5),
  XS_6 (6, CBootstrap3CSS.COL_XS_6),
  XS_7 (7, CBootstrap3CSS.COL_XS_7),
  XS_8 (8, CBootstrap3CSS.COL_XS_8),
  XS_9 (9, CBootstrap3CSS.COL_XS_9),
  XS_10 (10, CBootstrap3CSS.COL_XS_10),
  XS_11 (11, CBootstrap3CSS.COL_XS_11),
  XS_12 (12, CBootstrap3CSS.COL_XS_12),
  // Small - Tablets (≥768px)
  SM_1 (1, CBootstrap3CSS.COL_SM_1),
  SM_2 (2, CBootstrap3CSS.COL_SM_2),
  SM_3 (3, CBootstrap3CSS.COL_SM_3),
  SM_4 (4, CBootstrap3CSS.COL_SM_4),
  SM_5 (5, CBootstrap3CSS.COL_SM_5),
  SM_6 (6, CBootstrap3CSS.COL_SM_6),
  SM_7 (7, CBootstrap3CSS.COL_SM_7),
  SM_8 (8, CBootstrap3CSS.COL_SM_8),
  SM_9 (9, CBootstrap3CSS.COL_SM_9),
  SM_10 (10, CBootstrap3CSS.COL_SM_10),
  SM_11 (11, CBootstrap3CSS.COL_SM_11),
  SM_12 (12, CBootstrap3CSS.COL_SM_12),
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
  MD_12 (12, CBootstrap3CSS.COL_MD_12),
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

  private EBootstrap3Grid (@Nonnegative final int nParts, @Nonnull final ICSSClassProvider aCSSClass)
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
