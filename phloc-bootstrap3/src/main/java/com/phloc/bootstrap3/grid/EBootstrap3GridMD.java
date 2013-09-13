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
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.html.css.ICSSClassProvider;

/**
 * Bootstrap3 grid columns. Medium - Desktops (≥992px)
 * 
 * @author Philip Helger
 */
public enum EBootstrap3GridMD implements IBootstrap3GridElementExtended
{
  MD_0 (0, null, CBootstrap3CSS.COL_MD_OFFSET_0, CBootstrap3CSS.COL_MD_PUSH_0, CBootstrap3CSS.COL_MD_PULL_0),
  MD_1 (1, CBootstrap3CSS.COL_MD_1, CBootstrap3CSS.COL_MD_OFFSET_1, CBootstrap3CSS.COL_MD_PUSH_1, CBootstrap3CSS.COL_MD_PULL_1),
  MD_2 (2, CBootstrap3CSS.COL_MD_2, CBootstrap3CSS.COL_MD_OFFSET_2, CBootstrap3CSS.COL_MD_PUSH_2, CBootstrap3CSS.COL_MD_PULL_2),
  MD_3 (3, CBootstrap3CSS.COL_MD_3, CBootstrap3CSS.COL_MD_OFFSET_3, CBootstrap3CSS.COL_MD_PUSH_3, CBootstrap3CSS.COL_MD_PULL_3),
  MD_4 (4, CBootstrap3CSS.COL_MD_4, CBootstrap3CSS.COL_MD_OFFSET_4, CBootstrap3CSS.COL_MD_PUSH_4, CBootstrap3CSS.COL_MD_PULL_4),
  MD_5 (5, CBootstrap3CSS.COL_MD_5, CBootstrap3CSS.COL_MD_OFFSET_5, CBootstrap3CSS.COL_MD_PUSH_5, CBootstrap3CSS.COL_MD_PULL_5),
  MD_6 (6, CBootstrap3CSS.COL_MD_6, CBootstrap3CSS.COL_MD_OFFSET_6, CBootstrap3CSS.COL_MD_PUSH_6, CBootstrap3CSS.COL_MD_PULL_6),
  MD_7 (7, CBootstrap3CSS.COL_MD_7, CBootstrap3CSS.COL_MD_OFFSET_7, CBootstrap3CSS.COL_MD_PUSH_7, CBootstrap3CSS.COL_MD_PULL_7),
  MD_8 (8, CBootstrap3CSS.COL_MD_8, CBootstrap3CSS.COL_MD_OFFSET_8, CBootstrap3CSS.COL_MD_PUSH_8, CBootstrap3CSS.COL_MD_PULL_8),
  MD_9 (9, CBootstrap3CSS.COL_MD_9, CBootstrap3CSS.COL_MD_OFFSET_9, CBootstrap3CSS.COL_MD_PUSH_9, CBootstrap3CSS.COL_MD_PULL_9),
  MD_10 (10, CBootstrap3CSS.COL_MD_10, CBootstrap3CSS.COL_MD_OFFSET_10, CBootstrap3CSS.COL_MD_PUSH_10, CBootstrap3CSS.COL_MD_PULL_10),
  MD_11 (11, CBootstrap3CSS.COL_MD_11, CBootstrap3CSS.COL_MD_OFFSET_11, CBootstrap3CSS.COL_MD_PUSH_11, CBootstrap3CSS.COL_MD_PULL_11),
  MD_12 (12, CBootstrap3CSS.COL_MD_12, null, null, null);

  private final int m_nParts;
  private final ICSSClassProvider m_aCSSClass;
  private final ICSSClassProvider m_aCSSClassOffset;
  private final ICSSClassProvider m_aCSSClassPush;
  private final ICSSClassProvider m_aCSSClassPull;

  private EBootstrap3GridMD (@Nonnegative final int nParts,
                             @Nullable final ICSSClassProvider aCSSClass,
                             @Nullable final ICSSClassProvider aCSSClassOffset,
                             @Nullable final ICSSClassProvider aCSSClassPush,
                             @Nullable final ICSSClassProvider aCSSClassPull)
  {
    m_nParts = nParts;
    m_aCSSClass = aCSSClass;
    m_aCSSClassOffset = aCSSClassOffset;
    m_aCSSClassPush = aCSSClassPush;
    m_aCSSClassPull = aCSSClassPull;
  }

  @Nonnegative
  public int getParts ()
  {
    return m_nParts;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass == null ? null : m_aCSSClass.getCSSClass ();
  }

  @Nullable
  public ICSSClassProvider getCSSClassOffset ()
  {
    return m_aCSSClassOffset;
  }

  @Nullable
  public ICSSClassProvider getCSSClassPush ()
  {
    return m_aCSSClassPush;
  }

  @Nullable
  public ICSSClassProvider getCSSClassPull ()
  {
    return m_aCSSClassPull;
  }
}
