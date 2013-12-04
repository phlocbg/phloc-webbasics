package com.phloc.report.pdf.element;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface IPLSplittableElement
{
  public static final class SplitResult
  {
    private final AbstractPLElement <?> m_aFirstElement;
    private final AbstractPLElement <?> m_aSecondElement;

    public SplitResult (@Nonnull final AbstractPLElement <?> aFirstElement,
                        @Nonnull final AbstractPLElement <?> aSecondElement)
    {
      m_aFirstElement = aFirstElement;
      m_aSecondElement = aSecondElement;
    }

    @Nonnull
    public AbstractPLElement <?> getFirstElement ()
    {
      return m_aFirstElement;
    }

    @Nonnull
    public AbstractPLElement <?> getSecondElement ()
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
   * @return Never <code>null</code>.
   */
  @Nonnull
  SplitResult splitElements (@Nonnegative float fAvailableHeight);
}
