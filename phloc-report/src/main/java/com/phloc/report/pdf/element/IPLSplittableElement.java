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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IPLSplittableElement
{
  public static final class SplitResult
  {
    private final PLElementWithHeight m_aFirstElement;
    private final PLElementWithHeight m_aSecondElement;

    public SplitResult (@Nonnull final PLElementWithHeight aFirstElement,
                        @Nonnull final PLElementWithHeight aSecondElement)
    {
      m_aFirstElement = aFirstElement;
      m_aSecondElement = aSecondElement;
    }

    @Nonnull
    public PLElementWithHeight getFirstElement ()
    {
      return m_aFirstElement;
    }

    @Nonnull
    public PLElementWithHeight getSecondElement ()
    {
      return m_aSecondElement;
    }
  }

  /**
   * Split this element into subelements according to the available height.
   * 
   * @param fAvailableHeight
   *        The available height without y-padding and y-margin of this element.
   *        Must be &ge; 0.
   * @return <code>null</code> if splitting makes no sense.
   */
  @Nullable
  SplitResult splitElements (@Nonnegative float fAvailableHeight);
}
