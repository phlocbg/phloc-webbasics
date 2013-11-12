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
package com.phloc.report.pdf.spec;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.ToStringGenerator;

/**
 * Defines a rectangular padding.
 * 
 * @author Philip Helger
 */
@Immutable
public final class PaddingSpec
{
  public static final PaddingSpec PADDING0 = new PaddingSpec (0, 0, 0, 0);

  private final float m_fLeft;
  private final float m_fTop;
  private final float m_fRight;
  private final float m_fBottom;
  private final float m_fXSum;
  private final float m_fYSum;

  public PaddingSpec (@Nonnull final PaddingSpec aOther)
  {
    this (aOther.m_fLeft, aOther.m_fTop, aOther.m_fRight, aOther.m_fBottom);
  }

  public PaddingSpec (final float f)
  {
    this (f, f);
  }

  public PaddingSpec (final float fX, final float fY)
  {
    this (fX, fY, fX, fY);
  }

  public PaddingSpec (final float fLeft, final float fTop, final float fRight, final float fBottom)
  {
    m_fLeft = fLeft;
    m_fTop = fTop;
    m_fRight = fRight;
    m_fBottom = fBottom;
    m_fXSum = fLeft + fRight;
    m_fYSum = fTop + fBottom;
  }

  public boolean hasAnyPadding ()
  {
    return m_fLeft != 0 || m_fTop != 0 || m_fRight != 0 || m_fBottom != 0;
  }

  /**
   * @return Left value
   */
  public float getLeft ()
  {
    return m_fLeft;
  }

  /**
   * @return Top value
   */
  public float getTop ()
  {
    return m_fTop;
  }

  /**
   * @return Right value
   */
  public float getRight ()
  {
    return m_fRight;
  }

  /**
   * @return Bottom value
   */
  public float getBottom ()
  {
    return m_fBottom;
  }

  /**
   * @return Left + right value
   */
  public float getXSum ()
  {
    return m_fXSum;
  }

  /**
   * @return Top + bottom value
   */
  public float getYSum ()
  {
    return m_fYSum;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("paddingLeft", m_fLeft)
                                       .append ("paddingTop", m_fTop)
                                       .append ("paddingRight", m_fRight)
                                       .append ("paddingBottom", m_fBottom)
                                       .toString ();
  }
}
