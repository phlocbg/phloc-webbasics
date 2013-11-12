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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * This class wraps a text with a specified rendering width.
 * 
 * @author Philip Helger
 */
@Immutable
public final class TextAndWidthSpec
{
  private final String m_sText;
  private final float m_fWidth;

  public TextAndWidthSpec (@Nonnull final String sText, final float fWidth)
  {
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
}
