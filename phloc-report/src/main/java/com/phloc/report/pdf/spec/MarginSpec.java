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

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Defines a rectangular margin.
 * 
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public class MarginSpec
{
  public static final MarginSpec MARGIN0 = new MarginSpec (0, 0, 0, 0);

  private final float m_fLeft;
  private final float m_fTop;
  private final float m_fRight;
  private final float m_fBottom;
  // Helper vars
  private final float m_fXSum;
  private final float m_fYSum;

  public MarginSpec (@Nonnull final MarginSpec aOther)
  {
    this (aOther.m_fLeft, aOther.m_fTop, aOther.m_fRight, aOther.m_fBottom);
  }

  public MarginSpec (final float f)
  {
    this (f, f);
  }

  public MarginSpec (final float fX, final float fY)
  {
    this (fX, fY, fX, fY);
  }

  public MarginSpec (final float fLeft, final float fTop, final float fRight, final float fBottom)
  {
    m_fLeft = fLeft;
    m_fTop = fTop;
    m_fRight = fRight;
    m_fBottom = fBottom;
    m_fXSum = fLeft + fRight;
    m_fYSum = fTop + fBottom;
  }

  public boolean hasAnyMargin ()
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
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final MarginSpec rhs = (MarginSpec) o;
    return EqualsUtils.equals (m_fLeft, rhs.m_fLeft) &&
           EqualsUtils.equals (m_fTop, rhs.m_fTop) &&
           EqualsUtils.equals (m_fRight, rhs.m_fRight) &&
           EqualsUtils.equals (m_fBottom, rhs.m_fBottom);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_fLeft)
                                       .append (m_fTop)
                                       .append (m_fRight)
                                       .append (m_fBottom)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("marginLeft", m_fLeft)
                                       .append ("marginTop", m_fTop)
                                       .append ("marginRight", m_fRight)
                                       .append ("marginBottom", m_fBottom)
                                       .toString ();
  }
}
