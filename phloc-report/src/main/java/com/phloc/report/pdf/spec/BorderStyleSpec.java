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

import java.awt.Color;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class contains the styling of a single border part. Currently only the
 * color can be set.
 * 
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public class BorderStyleSpec
{
  /** The default border color: black */
  public static final Color DEFAULT_COLOR = Color.BLACK;

  private final Color m_aColor;

  public BorderStyleSpec ()
  {
    this (DEFAULT_COLOR);
  }

  public BorderStyleSpec (@Nonnull final Color aColor)
  {
    if (aColor == null)
      throw new NullPointerException ("color");

    m_aColor = aColor;
  }

  /**
   * @return The border color to use. Never <code>null</code>.
   */
  @Nonnull
  public Color getColor ()
  {
    return m_aColor;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final BorderStyleSpec rhs = (BorderStyleSpec) o;
    return m_aColor.equals (rhs.m_aColor);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aColor).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("color", m_aColor).toString ();
  }
}
