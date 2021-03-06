/**
 * Copyright (C) 2014 phloc systems
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

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class defines a size.
 * 
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public class SizeSpec
{
  public static final SizeSpec SIZE0 = new SizeSpec (0, 0);

  private final float m_fWidth;
  private final float m_fHeight;

  /**
   * Constructor
   * 
   * @param fWidth
   *        Width. Must be &ge; 0.
   * @param fHeight
   *        Height. Must be &ge; 0.
   */
  public SizeSpec (@Nonnegative final float fWidth, @Nonnegative final float fHeight)
  {
    ValueEnforcer.isGE0 (fWidth, "Width");
    ValueEnforcer.isGE0 (fHeight, "Height");

    m_fWidth = fWidth;
    m_fHeight = fHeight;
  }

  /**
   * @return Width. Always &ge; 0.
   */
  @Nonnegative
  public float getWidth ()
  {
    return m_fWidth;
  }

  /**
   * @return Height. Always &ge; 0.
   */
  @Nonnegative
  public float getHeight ()
  {
    return m_fHeight;
  }

  @Nonnull
  public PDRectangle getAsRectangle ()
  {
    return new PDRectangle (m_fWidth, m_fHeight);
  }

  @Nonnull
  public SizeSpec plus (final float fWidth, final float fHeight)
  {
    return new SizeSpec (m_fWidth + fWidth, m_fHeight + fHeight);
  }

  @Nonnull
  public SizeSpec minus (final float fWidth, final float fHeight)
  {
    return new SizeSpec (m_fWidth - fWidth, m_fHeight - fHeight);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final SizeSpec rhs = (SizeSpec) o;
    return EqualsUtils.equals (m_fWidth, rhs.m_fWidth) && EqualsUtils.equals (m_fHeight, rhs.m_fHeight);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_fWidth).append (m_fHeight).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("width", m_fWidth).append ("height", m_fHeight).toString ();
  }

  @Nonnull
  public static SizeSpec create (@Nonnull final PDRectangle aRect)
  {
    return new SizeSpec (aRect.getWidth (), aRect.getHeight ());
  }
}
