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
package com.phloc.bootstrap3.grid;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.css.ICSSClassProvider;

/**
 * Bootstrap3 grid columns. Extra small - Phones (<768px)
 * 
 * @author Philip Helger
 */
public enum EBootstrapGridXS implements IBootstrapGridElement
{
  XS_1 (1, CBootstrapCSS.COL_XS_1),
  XS_2 (2, CBootstrapCSS.COL_XS_2),
  XS_3 (3, CBootstrapCSS.COL_XS_3),
  XS_4 (4, CBootstrapCSS.COL_XS_4),
  XS_5 (5, CBootstrapCSS.COL_XS_5),
  XS_6 (6, CBootstrapCSS.COL_XS_6),
  XS_7 (7, CBootstrapCSS.COL_XS_7),
  XS_8 (8, CBootstrapCSS.COL_XS_8),
  XS_9 (9, CBootstrapCSS.COL_XS_9),
  XS_10 (10, CBootstrapCSS.COL_XS_10),
  XS_11 (11, CBootstrapCSS.COL_XS_11),
  XS_12 (12, CBootstrapCSS.COL_XS_12);

  private final int m_nParts;
  private final ICSSClassProvider m_aCSSClass;

  private EBootstrapGridXS (@Nonnegative final int nParts, @Nonnull final ICSSClassProvider aCSSClass)
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