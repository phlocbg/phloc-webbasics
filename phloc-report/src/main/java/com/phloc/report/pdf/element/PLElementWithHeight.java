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
package com.phloc.report.pdf.element;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;

/**
 * Wraps an {@link AbstractPLElement} and stores the height.
 * 
 * @author Philip Helger
 */
public final class PLElementWithHeight
{
  private final AbstractPLElement <?> m_aElement;
  private final float m_fHeight;
  private final float m_fHeightFull;

  public PLElementWithHeight (@Nonnull final AbstractPLElement <?> aElement, final float fHeight)
  {
    if (aElement == null)
      throw new NullPointerException ("element");
    m_aElement = aElement;
    m_fHeight = fHeight;
    m_fHeightFull = fHeight + aElement.getMarginPlusPaddingYSum ();
  }

  /**
   * @return The contained element.
   */
  @Nonnull
  public AbstractPLElement <?> getElement ()
  {
    return m_aElement;
  }

  /**
   * @return Height without padding or border
   */
  public float getHeight ()
  {
    return m_fHeight;
  }

  /**
   * @return Height with padding and border
   */
  public float getHeightFull ()
  {
    return m_fHeightFull;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("element", m_aElement)
                                       .append ("height", m_fHeight)
                                       .append ("heightFull", m_fHeightFull)
                                       .toString ();
  }
}
