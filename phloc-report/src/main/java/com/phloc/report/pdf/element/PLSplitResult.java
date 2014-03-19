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
package com.phloc.report.pdf.element;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;

/**
 * This class represents the result of splitting as defined in
 * {@link IPLSplittableElement}.
 * 
 * @author Philip Helger
 */
public final class PLSplitResult
{
  private final PLElementWithSize m_aFirstElement;
  private final PLElementWithSize m_aSecondElement;

  public PLSplitResult (@Nonnull final PLElementWithSize aFirstElement, @Nonnull final PLElementWithSize aSecondElement)
  {
    if (aFirstElement == null)
      throw new NullPointerException ("firstElement");
    if (aSecondElement == null)
      throw new NullPointerException ("secondElement");
    m_aFirstElement = aFirstElement;
    m_aSecondElement = aSecondElement;
  }

  @Nonnull
  public PLElementWithSize getFirstElement ()
  {
    return m_aFirstElement;
  }

  @Nonnull
  public PLElementWithSize getSecondElement ()
  {
    return m_aSecondElement;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("firstElement", m_aFirstElement)
                                       .append ("secondElement", m_aSecondElement)
                                       .toString ();
  }
}
