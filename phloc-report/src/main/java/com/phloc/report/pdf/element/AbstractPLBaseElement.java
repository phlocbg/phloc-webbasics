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

import java.awt.Color;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.report.pdf.render.PDPageContentStreamWithCache;
import com.phloc.report.pdf.spec.BorderSpec;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.MarginSpec;
import com.phloc.report.pdf.spec.PaddingSpec;

/**
 * Abstract base class for a PDF layout (=PL) element that has margin, padding,
 * border and a fill color. It does not directly support rendering.
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

  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public IMPLTYPE setBasicDataFrom (@Nonnull final AbstractPLBaseElement <?> aSource)
  {
    setMargin (aSource.m_aMargin);
    setPadding (aSource.m_aPadding);
    setBorder (aSource.m_aBorder);
    setFillColor (aSource.m_aFillColor);
    return thisAsT ();
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
    ValueEnforcer.notNull (aMargin, "Mergin");
    checkNotPrepared ();
    m_aMargin = aMargin;
    return thisAsT ();
  }

  /**
   * Set the left margin value. This method may not be called after an element
   * got prepared!
   *
   * @param fMargin
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setMarginLeft (final float fMargin)
  {
    return setMargin (m_aMargin.getCloneWithLeft (fMargin));
  }

  /**
   * Set the top margin value. This method may not be called after an element
   * got prepared!
   *
   * @param fMargin
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setMarginTop (final float fMargin)
  {
    return setMargin (m_aMargin.getCloneWithTop (fMargin));
  }

  /**
   * Set the right margin value. This method may not be called after an element
   * got prepared!
   *
   * @param fMargin
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setMarginRight (final float fMargin)
  {
    return setMargin (m_aMargin.getCloneWithRight (fMargin));
  }

  /**
   * Set the bottom margin value. This method may not be called after an element
   * got prepared!
   *
   * @param fMargin
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setMarginBottom (final float fMargin)
  {
    return setMargin (m_aMargin.getCloneWithBottom (fMargin));
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
    ValueEnforcer.notNull (aPadding, "Padding");
    checkNotPrepared ();
    m_aPadding = aPadding;
    return thisAsT ();
  }

  /**
   * Set the left padding value. This method may not be called after an element
   * got prepared!
   *
   * @param fPadding
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setPaddingLeft (final float fPadding)
  {
    return setPadding (m_aPadding.getCloneWithLeft (fPadding));
  }

  /**
   * Set the top padding value. This method may not be called after an element
   * got prepared!
   *
   * @param fPadding
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setPaddingTop (final float fPadding)
  {
    return setPadding (m_aPadding.getCloneWithTop (fPadding));
  }

  /**
   * Set the right padding value. This method may not be called after an element
   * got prepared!
   *
   * @param fPadding
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setPaddingRight (final float fPadding)
  {
    return setPadding (m_aPadding.getCloneWithRight (fPadding));
  }

  /**
   * Set the bottom padding value. This method may not be called after an
   * element got prepared!
   *
   * @param fPadding
   *        The value to use.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setPaddingBottom (final float fPadding)
  {
    return setPadding (m_aPadding.getCloneWithBottom (fPadding));
  }

  /**
   * @return The current padding. Never <code>null</code>.
   */
  @Nonnull
  public final PaddingSpec getPadding ()
  {
    return m_aPadding;
  }

  public float getMarginPlusPaddingLeft ()
  {
    return m_aMargin.getLeft () + m_aPadding.getLeft ();
  }

  public float getMarginPlusPaddingTop ()
  {
    return m_aMargin.getTop () + m_aPadding.getTop ();
  }

  public float getMarginPlusPaddingRight ()
  {
    return m_aMargin.getRight () + m_aPadding.getRight ();
  }

  public float getMarginPlusPaddingBottom ()
  {
    return m_aMargin.getBottom () + m_aPadding.getBottom ();
  }

  public float getMarginPlusPaddingXSum ()
  {
    return m_aMargin.getXSum () + m_aPadding.getXSum ();
  }

  public float getMarginPlusPaddingYSum ()
  {
    return m_aMargin.getYSum () + m_aPadding.getYSum ();
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
    ValueEnforcer.notNull (aBorder, "Border");
    checkNotPrepared ();
    m_aBorder = aBorder;
    return thisAsT ();
  }

  /**
   * Set the left border value. This method may not be called after an element
   * got prepared!
   *
   * @param aBorder
   *        The value to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setBorderLeft (@Nullable final BorderStyleSpec aBorder)
  {
    return setBorder (m_aBorder.getCloneWithLeft (aBorder));
  }

  /**
   * Set the top border value. This method may not be called after an element
   * got prepared!
   *
   * @param aBorder
   *        The value to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setBorderTop (@Nullable final BorderStyleSpec aBorder)
  {
    return setBorder (m_aBorder.getCloneWithTop (aBorder));
  }

  /**
   * Set the right border value. This method may not be called after an element
   * got prepared!
   *
   * @param aBorder
   *        The value to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setBorderRight (@Nullable final BorderStyleSpec aBorder)
  {
    return setBorder (m_aBorder.getCloneWithRight (aBorder));
  }

  /**
   * Set the bottom border value. This method may not be called after an element
   * got prepared!
   *
   * @param aBorder
   *        The value to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setBorderBottom (@Nullable final BorderStyleSpec aBorder)
  {
    return setBorder (m_aBorder.getCloneWithBottom (aBorder));
  }

  /**
   * @return The current border. Never <code>null</code>.
   */
  @Nonnull
  public final BorderSpec getBorder ()
  {
    return m_aBorder;
  }

  /**
   * Set the element fill color.
   *
   * @param aFillColor
   *        The fill color to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public IMPLTYPE setFillColor (@Nullable final Color aFillColor)
  {
    m_aFillColor = aFillColor;
    return thisAsT ();
  }

  /**
   * @return The current fill color. May be <code>null</code>.
   */
  @Nullable
  public Color getFillColor ()
  {
    return m_aFillColor;
  }

  /**
   * Should a debug border be drawn? Only if no other border is present.
   *
   * @param aBorder
   *        The element border. May not be <code>null</code>.
   * @param bDebug
   *        <code>true</code> if debug mode is enabled
   * @return <code>true</code> if a debug border should be drawn
   */
  public static boolean shouldApplyDebugBorder (@Nonnull final BorderSpec aBorder, final boolean bDebug)
  {
    return !aBorder.hasAnyBorder () && bDebug;
  }

  /**
   * Render a single border
   *
   * @param aContentStream
   *        Content stream
   * @param fLeft
   *        Left position
   * @param fTop
   *        Top position
   * @param fWidth
   *        Width
   * @param fHeight
   *        Height
   * @param aBorder
   *        Border to use. May not be <code>null</code>.
   * @throws IOException
   */
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
      final BorderStyleSpec aAll = aBorder.getLeft ();
      aContentStream.setStrokingColor (aAll.getColor ());
      aContentStream.setLineDashPattern (aAll.getLineDashPattern ());
      aContentStream.addRect (fLeft, fBottom, fWidth, fHeight);
      aContentStream.stroke ();
    }
    else
    {
      // partially
      final BorderStyleSpec aLeft = aBorder.getLeft ();
      if (aLeft != null)
      {
        aContentStream.setStrokingColor (aLeft.getColor ());
        aContentStream.setLineDashPattern (aLeft.getLineDashPattern ());
        aContentStream.drawLine (fLeft, fTop, fLeft, fBottom);
      }

      final BorderStyleSpec aTop = aBorder.getTop ();
      if (aTop != null)
      {
        aContentStream.setStrokingColor (aTop.getColor ());
        aContentStream.setLineDashPattern (aTop.getLineDashPattern ());
        aContentStream.drawLine (fLeft, fTop, fRight, fTop);
      }

      final BorderStyleSpec aRight = aBorder.getRight ();
      if (aRight != null)
      {
        aContentStream.setStrokingColor (aRight.getColor ());
        aContentStream.setLineDashPattern (aRight.getLineDashPattern ());
        aContentStream.drawLine (fRight, fTop, fRight, fBottom);
      }

      final BorderStyleSpec aBottom = aBorder.getBottom ();
      if (aBottom != null)
      {
        aContentStream.setStrokingColor (aBottom.getColor ());
        aContentStream.setLineDashPattern (aBottom.getLineDashPattern ());
        aContentStream.drawLine (fLeft, fBottom, fRight, fBottom);
      }
    }
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
