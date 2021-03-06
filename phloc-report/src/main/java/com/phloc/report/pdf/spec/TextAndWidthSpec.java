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

import java.util.Collection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class wraps a text with a specified rendering width.
 * 
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public class TextAndWidthSpec
{
  private final String m_sText;
  private final float m_fWidth;

  public TextAndWidthSpec (@Nonnull final String sText, final float fWidth)
  {
    if (sText == null)
      throw new NullPointerException ("Text");
    m_sText = sText;
    m_fWidth = fWidth;
  }

  @Nonnull
  public String getText ()
  {
    return m_sText;
  }

  @Nonnegative
  public float getWidth ()
  {
    return m_fWidth;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final TextAndWidthSpec rhs = (TextAndWidthSpec) o;
    return m_sText.equals (rhs.m_sText) && EqualsUtils.equals (m_fWidth, rhs.m_fWidth);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sText).append (m_fWidth).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("text", m_sText).append ("width", m_fWidth).toString ();
  }

  @Nonnull
  public static String getAsText (@Nonnull final Collection <? extends TextAndWidthSpec> aTexts)
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final TextAndWidthSpec aLine : aTexts)
    {
      if (aSB.length () > 0)
        aSB.append ('\n');
      aSB.append (aLine.getText ());
    }
    return aSB.toString ();
  }
}
