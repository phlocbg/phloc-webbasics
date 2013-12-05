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
