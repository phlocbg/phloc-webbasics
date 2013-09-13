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
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;

/**
 * Bootstrap3 grid columns. Small - Tablets (≥768px)
 * 
 * @author Philip Helger
 */
public enum EBootstrap3GridSM implements IBootstrap3GridElementExtended
{
  SM_1 (1, CBootstrap3CSS.COL_SM_1, CBootstrap3CSS.COL_SM_OFFSET_1, CBootstrap3CSS.COL_SM_PUSH_1, CBootstrap3CSS.COL_SM_PULL_1),
  SM_2 (2, CBootstrap3CSS.COL_SM_2, CBootstrap3CSS.COL_SM_OFFSET_2, CBootstrap3CSS.COL_SM_PUSH_2, CBootstrap3CSS.COL_SM_PULL_2),
  SM_3 (3, CBootstrap3CSS.COL_SM_3, CBootstrap3CSS.COL_SM_OFFSET_3, CBootstrap3CSS.COL_SM_PUSH_3, CBootstrap3CSS.COL_SM_PULL_3),
  SM_4 (4, CBootstrap3CSS.COL_SM_4, CBootstrap3CSS.COL_SM_OFFSET_4, CBootstrap3CSS.COL_SM_PUSH_4, CBootstrap3CSS.COL_SM_PULL_4),
  SM_5 (5, CBootstrap3CSS.COL_SM_5, CBootstrap3CSS.COL_SM_OFFSET_5, CBootstrap3CSS.COL_SM_PUSH_5, CBootstrap3CSS.COL_SM_PULL_5),
  SM_6 (6, CBootstrap3CSS.COL_SM_6, CBootstrap3CSS.COL_SM_OFFSET_6, CBootstrap3CSS.COL_SM_PUSH_6, CBootstrap3CSS.COL_SM_PULL_6),
  SM_7 (7, CBootstrap3CSS.COL_SM_7, CBootstrap3CSS.COL_SM_OFFSET_7, CBootstrap3CSS.COL_SM_PUSH_7, CBootstrap3CSS.COL_SM_PULL_7),
  SM_8 (8, CBootstrap3CSS.COL_SM_8, CBootstrap3CSS.COL_SM_OFFSET_8, CBootstrap3CSS.COL_SM_PUSH_8, CBootstrap3CSS.COL_SM_PULL_8),
  SM_9 (9, CBootstrap3CSS.COL_SM_9, CBootstrap3CSS.COL_SM_OFFSET_9, CBootstrap3CSS.COL_SM_PUSH_9, CBootstrap3CSS.COL_SM_PULL_9),
  SM_10 (10, CBootstrap3CSS.COL_SM_10, CBootstrap3CSS.COL_SM_OFFSET_10, CBootstrap3CSS.COL_SM_PUSH_10, CBootstrap3CSS.COL_SM_PULL_10),
  SM_11 (11, CBootstrap3CSS.COL_SM_11, CBootstrap3CSS.COL_SM_OFFSET_11, CBootstrap3CSS.COL_SM_PUSH_11, CBootstrap3CSS.COL_SM_PULL_11),
  SM_12 (12, CBootstrap3CSS.COL_SM_12, null, null, null);

  private final int m_nParts;
  private final ICSSClassProvider m_aCSSClass;
  private final ICSSClassProvider m_aCSSClassOffset;
  private final ICSSClassProvider m_aCSSClassPush;
  private final ICSSClassProvider m_aCSSClassPull;

  private EBootstrap3GridSM (@Nonnegative final int nParts,
                             @Nonnull final ICSSClassProvider aCSSClass,
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

  @Nonnull
  @Nonempty
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
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
