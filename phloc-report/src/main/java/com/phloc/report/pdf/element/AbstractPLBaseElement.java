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

import java.awt.Color;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.report.pdf.render.PDPageContentStreamWithCache;
import com.phloc.report.pdf.spec.BorderSpec;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.MarginSpec;
import com.phloc.report.pdf.spec.PaddingSpec;

/**
 * Abstract base class for a PDF layout (=PL) element that has margin, padding
 * and border.
 * 
 * @author Philip Helger
 * @param <IMPLTYPE>
 *        The implementation type of this class.
 */
public abstract class AbstractPLBaseElement <IMPLTYPE extends AbstractPLBaseElement <IMPLTYPE>>
{
  private MarginSpec m_aMargin = MarginSpec.MARGIN0;
  private PaddingSpec m_aPadding = PaddingSpec.PADDING0;
  private BorderSpec m_aBorder = BorderSpec.BORDER0;
  private Color m_aFillColor = null;

  public AbstractPLBaseElement ()
  {}

  @SuppressWarnings ("unchecked")
  @Nonnull
  protected final IMPLTYPE thisAsT ()
  {
    return (IMPLTYPE) this;
  }

  /**
   * Throw an exception, if this object is already prepared.
   * 
   * @throws IllegalStateException
   *         if already prepared
   */
  @OverrideOnDemand
  protected void checkNotPrepared ()
  {}

  /**
   * Set all margin values (left, top, right, bottom) to the same value. This
   * method may not be called after an element got prepared!
   * 
   * @param fMargin
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setMargin (final float fMargin)
  {
    return setMargin (fMargin, fMargin);
  }

  /**
   * Set all margin values. This method may not be called after an element got
   * prepared!
   * 
   * @param fMarginX
   *        The X-value to use (for left and right).
   * @param fMarginY
   *        The Y-value to use (for top and bottom).
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setMargin (final float fMarginX, final float fMarginY)
  {
    return setMargin (fMarginX, fMarginY, fMarginX, fMarginY);
  }

  /**
   * Set all margin values to potentially different values. This method may not
   * be called after an element got prepared!
   * 
   * @param fMarginLeft
   *        Left
   * @param fMarginTop
   *        Top
   * @param fMarginRight
   *        Right
   * @param fMarginBottom
   *        Bottom
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setMargin (final float fMarginLeft,
                                   final float fMarginTop,
                                   final float fMarginRight,
                                   final float fMarginBottom)
  {
    return setMargin (new MarginSpec (fMarginLeft, fMarginTop, fMarginRight, fMarginBottom));
  }

  /**
   * Set the margin values. This method may not be called after an element got
   * prepared!
   * 
   * @param aMargin
   *        Margin to use. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setMargin (@Nonnull final MarginSpec aMargin)
  {
    if (aMargin == null)
      throw new NullPointerException ("Margin");
    checkNotPrepared ();
    m_aMargin = aMargin;
    return thisAsT ();
  }

  /**
   * @return The current margin. Never <code>null</code>.
   */
  @Nonnull
  public final MarginSpec getMargin ()
  {
    return m_aMargin;
  }

  /**
   * Set all padding values (left, top, right, bottom) to the same value. This
   * method may not be called after an element got prepared!
   * 
   * @param fPadding
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setPadding (final float fPadding)
  {
    return setPadding (fPadding, fPadding);
  }

  /**
   * Set all padding values. This method may not be called after an element got
   * prepared!
   * 
   * @param fPaddingX
   *        The X-value to use (for left and right).
   * @param fPaddingY
   *        The Y-value to use (for top and bottom).
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setPadding (final float fPaddingX, final float fPaddingY)
  {
    return setPadding (fPaddingX, fPaddingY, fPaddingX, fPaddingY);
  }

  /**
   * Set all padding values to potentially different values. This method may not
   * be called after an element got prepared!
   * 
   * @param fPaddingLeft
   *        Left
   * @param fPaddingTop
   *        Top
   * @param fPaddingRight
   *        Right
   * @param fPaddingBottom
   *        Bottom
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setPadding (final float fPaddingLeft,
                                    final float fPaddingTop,
                                    final float fPaddingRight,
                                    final float fPaddingBottom)
  {
    return setPadding (new PaddingSpec (fPaddingLeft, fPaddingTop, fPaddingRight, fPaddingBottom));
  }

  /**
   * Set the padding values. This method may not be called after an element got
   * prepared!
   * 
   * @param aPadding
   *        Padding to use. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setPadding (@Nonnull final PaddingSpec aPadding)
  {
    if (aPadding == null)
      throw new NullPointerException ("Padding");
    checkNotPrepared ();
    m_aPadding = aPadding;
    return thisAsT ();
  }

  /**
   * @return The current padding. Never <code>null</code>.
   */
  @Nonnull
  public final PaddingSpec getPadding ()
  {
    return m_aPadding;
  }

  /**
   * Set all border values (left, top, right, bottom) to the same value. This
   * method may not be called after an element got prepared!
   * 
   * @param aBorder
   *        The border style specification to use. May be <code>null</code> to
   *        indicate no border.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setBorder (@Nullable final BorderStyleSpec aBorder)
  {
    return setBorder (new BorderSpec (aBorder));
  }

  /**
   * Set all border values. This method may not be called after an element got
   * prepared!
   * 
   * @param aBorderX
   *        The X-value to use (for left and right). May be <code>null</code> to
   *        indicate no border.
   * @param aBorderY
   *        The Y-value to use (for top and bottom). May be <code>null</code> to
   *        indicate no border.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setBorder (@Nullable final BorderStyleSpec aBorderX, @Nullable final BorderStyleSpec aBorderY)
  {
    return setBorder (new BorderSpec (aBorderX, aBorderY));
  }

  /**
   * Set all border values to potentially different values. This method may not
   * be called after an element got prepared!
   * 
   * @param aBorderLeft
   *        Left. May be <code>null</code> to indicate no border.
   * @param aBorderTop
   *        Top. May be <code>null</code> to indicate no border.
   * @param aBorderRight
   *        Right. May be <code>null</code> to indicate no border.
   * @param aBorderBottom
   *        Bottom. May be <code>null</code> to indicate no border.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setBorder (@Nullable final BorderStyleSpec aBorderLeft,
                                   @Nullable final BorderStyleSpec aBorderTop,
                                   @Nullable final BorderStyleSpec aBorderRight,
                                   @Nullable final BorderStyleSpec aBorderBottom)
  {
    return setBorder (new BorderSpec (aBorderLeft, aBorderTop, aBorderRight, aBorderBottom));
  }

  /**
   * Set the border values. This method may not be called after an element got
   * prepared!
   * 
   * @param aBorder
   *        Border to use. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setBorder (@Nonnull final BorderSpec aBorder)
  {
    if (aBorder == null)
      throw new NullPointerException ("Border");
    checkNotPrepared ();
    m_aBorder = aBorder;
    return thisAsT ();
  }

  /**
   * @return The current border. Never <code>null</code>.
   */
  @Nonnull
  public final BorderSpec getBorder ()
  {
    return m_aBorder;
  }

  public static boolean shouldApplyDebugBorder (@Nonnull final BorderSpec aBorder, final boolean bDebug)
  {
    return !aBorder.hasAnyBorder () && bDebug;
  }

  protected static void renderBorder (@Nonnull final PDPageContentStreamWithCache aContentStream,
                                      final float fLeft,
                                      final float fTop,
                                      final float fWidth,
                                      final float fHeight,
                                      @Nonnull final BorderSpec aBorder) throws IOException
  {
    final float fRight = fLeft + fWidth;
    final float fBottom = fTop - fHeight;

    if (false)
      System.out.println (fLeft + "/" + fBottom + " - " + fRight + "/" + fTop + " (= " + fWidth + "/" + fHeight + ")");

    if (aBorder.hasAllBorders () && aBorder.areAllBordersEqual ())
    {
      // draw full rect
      aContentStream.setStrokingColor (aBorder.getLeft ().getColor ());
      aContentStream.addRect (fLeft, fBottom, fWidth, fHeight);
      aContentStream.stroke ();
    }
    else
    {
      // partially
      if (aBorder.getLeft () != null)
      {
        aContentStream.setStrokingColor (aBorder.getLeft ().getColor ());
        aContentStream.drawLine (fLeft, fTop, fLeft, fBottom);
      }
      if (aBorder.getTop () != null)
      {
        aContentStream.setStrokingColor (aBorder.getTop ().getColor ());
        aContentStream.drawLine (fLeft, fTop, fRight, fTop);
      }
      if (aBorder.getRight () != null)
      {
        aContentStream.setStrokingColor (aBorder.getRight ().getColor ());
        aContentStream.drawLine (fRight, fTop, fRight, fBottom);
      }
      if (aBorder.getBottom () != null)
      {
        aContentStream.setStrokingColor (aBorder.getBottom ().getColor ());
        aContentStream.drawLine (fLeft, fBottom, fRight, fBottom);
      }
    }
  }

  @Nonnull
  public IMPLTYPE setFillColor (@Nullable final Color aFillColor)
  {
    m_aFillColor = aFillColor;
    return thisAsT ();
  }

  @Nullable
  public Color getFillColor ()
  {
    return m_aFillColor;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("margin", m_aMargin)
                                       .append ("padding", m_aPadding)
                                       .append ("border", m_aBorder)
                                       .appendIfNotNull ("fillColor", m_aFillColor)
                                       .toString ();
  }
}
