/**
 * Copyright (C) 2013 phloc systems
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

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class represents a border around a single element. Each side can be
 * styled separately.
 * 
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public class BorderSpec
{
  /** Represents no border at all! */
  public static final BorderSpec BORDER0 = new BorderSpec (null);

  private final BorderStyleSpec m_aLeft;
  private final BorderStyleSpec m_aTop;
  private final BorderStyleSpec m_aRight;
  private final BorderStyleSpec m_aBottom;

  /**
   * Constructor.
   * 
   * @param aBorder
   *        The border to set for all sides (left, top, right, bottom). Maybe
   *        <code>null</code>.
   */
  public BorderSpec (@Nullable final BorderStyleSpec aBorder)
  {
    this (aBorder, aBorder);
  }

  /**
   * Constructor.
   * 
   * @param aBorderX
   *        The border to set for left and right. Maybe <code>null</code>.
   * @param aBorderY
   *        The border to set for top and bottom. Maybe <code>null</code>.
   */
  public BorderSpec (@Nullable final BorderStyleSpec aBorderX, @Nullable final BorderStyleSpec aBorderY)
  {
    this (aBorderX, aBorderY, aBorderX, aBorderY);
  }

  /**
   * Constructor.
   * 
   * @param aBorderLeft
   *        The border to set for left. Maybe <code>null</code>.
   * @param aBorderTop
   *        The border to set for top. Maybe <code>null</code>.
   * @param aBorderRight
   *        The border to set for right. Maybe <code>null</code>.
   * @param aBorderBottom
   *        The border to set for bottom. Maybe <code>null</code>.
   */
  public BorderSpec (@Nullable final BorderStyleSpec aBorderLeft,
                     @Nullable final BorderStyleSpec aBorderTop,
                     @Nullable final BorderStyleSpec aBorderRight,
                     @Nullable final BorderStyleSpec aBorderBottom)
  {
    m_aLeft = aBorderLeft;
    m_aTop = aBorderTop;
    m_aRight = aBorderRight;
    m_aBottom = aBorderBottom;
  }

  /**
   * @return <code>true</code> if all borders are defined, <code>false</code>
   *         otherwise.
   */
  public boolean hasAllBorders ()
  {
    return m_aLeft != null && m_aTop != null && m_aRight != null && m_aBottom != null;
  }

  /**
   * @return <code>true</code> if at least one border is defined,
   *         <code>false</code> if no border is defined at all.
   */
  public boolean hasAnyBorder ()
  {
    return m_aLeft != null || m_aTop != null || m_aRight != null || m_aBottom != null;
  }

  /**
   * @return <code>true</code> if all border sides are equal. This is
   *         <code>true</code> for <code>null</code> borders as well as for
   *         defined borders.
   */
  public boolean areAllBordersEqual ()
  {
    return EqualsUtils.equals (m_aLeft, m_aTop) &&
           EqualsUtils.equals (m_aLeft, m_aRight) &&
           EqualsUtils.equals (m_aLeft, m_aBottom);
  }

  /**
   * @return The left border style. May be <code>null</code>.
   */
  @Nullable
  public BorderStyleSpec getLeft ()
  {
    return m_aLeft;
  }

  /**
   * @return The top border style. May be <code>null</code>.
   */
  @Nullable
  public BorderStyleSpec getTop ()
  {
    return m_aTop;
  }

  /**
   * @return The right border style. May be <code>null</code>.
   */
  @Nullable
  public BorderStyleSpec getRight ()
  {
    return m_aRight;
  }

  /**
   * @return The bottom border style. May be <code>null</code>.
   */
  @Nullable
  public BorderStyleSpec getBottom ()
  {
    return m_aBottom;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final BorderSpec rhs = (BorderSpec) o;
    return EqualsUtils.equals (m_aLeft, rhs.m_aLeft) &&
           EqualsUtils.equals (m_aTop, rhs.m_aTop) &&
           EqualsUtils.equals (m_aRight, rhs.m_aRight) &&
           EqualsUtils.equals (m_aBottom, rhs.m_aBottom);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aLeft)
                                       .append (m_aTop)
                                       .append (m_aRight)
                                       .append (m_aBottom)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("left", m_aLeft)
                                       .appendIfNotNull ("top", m_aTop)
                                       .appendIfNotNull ("right", m_aRight)
                                       .appendIfNotNull ("bottom", m_aBottom)
                                       .toString ();
  }
}
