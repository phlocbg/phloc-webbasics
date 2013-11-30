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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class defines a dependent width of an elements:
 * <ul>
 * <li>absolute - element has a fixed width</li>
 * <li>percentage - element width is a certain percentage of the surrounding
 * element</li>
 * <li>star - element width is a relative part of the unused width of the
 * surrounding element</li>
 * </ul>
 * 
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public class WidthSpec
{
  public static enum EWidthType
  {
    ABSOLUTE,
    PERCENTAGE,
    STAR;
  }

  private final EWidthType m_eType;
  private final float m_fValue;

  private WidthSpec (@Nonnull final EWidthType eType, final float fValue)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    m_eType = eType;
    m_fValue = fValue;
  }

  /**
   * @return The width type. Never <code>null</code>.
   */
  @Nonnull
  public EWidthType getType ()
  {
    return m_eType;
  }

  /**
   * @return <code>true</code> if type is 'star'.
   */
  public boolean isStar ()
  {
    return m_eType == EWidthType.STAR;
  }

  /**
   * @return The width value - is either an absolute value or a percentage value
   *         - depending on {@link #getType()}. For star width elements this is
   *         0.
   */
  @Nonnegative
  public float getValue ()
  {
    return m_fValue;
  }

  /**
   * Get the effective width based on the passed available width. This may not
   * be called for star width elements.
   * 
   * @param fAvailableWidth
   *        The available width.
   * @return The effective width to use.
   */
  @Nonnegative
  public float getEffectiveValue (final float fAvailableWidth)
  {
    switch (m_eType)
    {
      case ABSOLUTE:
        return Math.min (m_fValue, fAvailableWidth);
      case PERCENTAGE:
        return fAvailableWidth * m_fValue / 100;
      default:
        throw new IllegalStateException ("Unsupported: " + m_eType + " - must be calculated outside!");
    }
  }

  @Override
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final WidthSpec rhs = (WidthSpec) o;
    return m_eType.equals (rhs.m_eType) && EqualsUtils.equals (m_fValue, rhs.m_fValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eType).append (m_fValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("type", m_eType).append ("value", m_fValue).toString ();
  }

  /**
   * Create a width element with an absolute value.
   * 
   * @param fValue
   *        The width to use. Must be &gt; 0.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static WidthSpec abs (@Nonnegative final float fValue)
  {
    if (fValue <= 0)
      throw new IllegalArgumentException ("Value must be > 0!");
    return new WidthSpec (EWidthType.ABSOLUTE, fValue);
  }

  /**
   * Create a width element with an percentage value.
   * 
   * @param fPerc
   *        The width percentage to use. Must be &gt; 0.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static WidthSpec perc (@Nonnegative final float fPerc)
  {
    if (fPerc <= 0)
      throw new IllegalArgumentException ("Perc must be > 0!");
    return new WidthSpec (EWidthType.PERCENTAGE, fPerc);
  }

  /**
   * Create a new star width element.
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static WidthSpec star ()
  {
    return new WidthSpec (EWidthType.STAR, 0);
  }
}
