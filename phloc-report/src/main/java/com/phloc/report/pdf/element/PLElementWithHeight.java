package com.phloc.report.pdf.element;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;

public final class PLElementWithHeight
{
  private final AbstractPLElement <?> m_aElement;
  private final float m_fHeight;
  private final float m_fHeightFull;

  PLElementWithHeight (@Nonnull final AbstractPLElement <?> aElement, @Nonnull final float fHeight)
  {
    m_aElement = aElement;
    m_fHeight = fHeight;
    m_fHeightFull = fHeight + aElement.getMarginPlusPaddingYSum ();
  }

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
