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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.report.pdf.render.PDPageContentStreamWithCache;
import com.phloc.report.pdf.render.RenderPreparationContext;
import com.phloc.report.pdf.render.RenderingContext;
import com.phloc.report.pdf.spec.BorderSpec;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.SizeSpec;
import com.phloc.report.pdf.spec.WidthSpec;

/**
 * Horizontal box - groups several columns
 * 
 * @author Philip Helger
 */
public class PLHBox extends AbstractPLElement <PLHBox>
{
  private static final class Column
  {
    private final AbstractPLElement <?> m_aElement;
    private final WidthSpec m_aWidth;

    public Column (@Nonnull final AbstractPLElement <?> aElement, @Nonnull final WidthSpec aWidth)
    {
      if (aElement == null)
        throw new NullPointerException ("element");
      if (aWidth == null)
        throw new NullPointerException ("width");
      m_aElement = aElement;
      m_aWidth = aWidth;
    }

    @Nonnull
    public AbstractPLElement <?> getElement ()
    {
      return m_aElement;
    }

    @Nonnull
    public WidthSpec getWidth ()
    {
      return m_aWidth;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (PLHBox.class);

  private final List <Column> m_aColumns = new ArrayList <Column> ();
  private int m_nStarWidthItems = 0;
  private BorderSpec m_aColumnBorder = BorderSpec.BORDER0;
  private Color m_aColumnFillColor = null;
  // prepare result (without padding and margin)
  private float [] m_aWidth;
  private float [] m_aHeight;

  public PLHBox ()
  {}

  @Nonnegative
  public int getColumnCount ()
  {
    return m_aColumns.size ();
  }

  @Nonnull
  public PLHBox addColumn (@Nonnull final AbstractPLElement <?> aElement, @Nonnull final WidthSpec aWidth)
  {
    checkNotPrepared ();
    final Column aItem = new Column (aElement, aWidth);
    m_aColumns.add (aItem);
    if (aWidth.isStar ())
      m_nStarWidthItems++;
    return this;
  }

  @Nonnull
  public final PLHBox setColumnBorder (@Nullable final BorderStyleSpec aBorder)
  {
    return setColumnBorder (new BorderSpec (aBorder));
  }

  @Nonnull
  public final PLHBox setColumnBorder (@Nullable final BorderStyleSpec aBorderX,
                                       @Nullable final BorderStyleSpec aBorderY)
  {
    return setColumnBorder (new BorderSpec (aBorderX, aBorderY));
  }

  @Nonnull
  public final PLHBox setColumnBorder (@Nullable final BorderStyleSpec aBorderLeft,
                                       @Nullable final BorderStyleSpec aBorderTop,
                                       @Nullable final BorderStyleSpec aBorderRight,
                                       @Nullable final BorderStyleSpec aBorderBottom)
  {
    return setColumnBorder (new BorderSpec (aBorderLeft, aBorderTop, aBorderRight, aBorderBottom));
  }

  @Nonnull
  public final PLHBox setColumnBorder (@Nonnull final BorderSpec aBorder)
  {
    if (aBorder == null)
      throw new NullPointerException ("ColumnBorder");
    checkNotPrepared ();
    m_aColumnBorder = aBorder;
    return this;
  }

  @Nonnull
  public final BorderSpec getColumnBorder ()
  {
    return m_aColumnBorder;
  }

  @Nonnull
  public PLHBox setColumnFillColor (@Nullable final Color aColumnFillColor)
  {
    m_aColumnFillColor = aColumnFillColor;
    return this;
  }

  @Nullable
  public Color getColumnFillColor ()
  {
    return m_aColumnFillColor;
  }

  @Override
  protected SizeSpec onPrepare (@Nonnull final RenderPreparationContext aCtx) throws IOException
  {
    m_aWidth = new float [m_aColumns.size ()];
    m_aHeight = new float [m_aColumns.size ()];
    final float fAvailableWidth = aCtx.getAvailableWidth ();
    final float fAvailableHeight = aCtx.getAvailableHeight ();
    float fUsedWidth = 0;
    float fUsedHeight = 0;
    int nIndex = 0;
    float fRestWidth = fAvailableWidth;
    // 1. all non-star width items
    for (final Column aColumn : m_aColumns)
    {
      if (!aColumn.getWidth ().isStar ())
      {
        final AbstractPLElement <?> aElement = aColumn.getElement ();
        // Full width of this element
        final float fItemWidthFull = aColumn.getWidth ().getEffectiveValue (fAvailableWidth);
        // Effective content width of this element
        final float fItemWidth = fItemWidthFull - aElement.getMargin ().getXSum () - aElement.getPadding ().getXSum ();
        // Prepare child element
        final float fItemHeight = aElement.prepare (new RenderPreparationContext (fItemWidth, fAvailableHeight))
                                          .getHeight ();
        final float fItemHeightFull = fItemHeight +
                                      aElement.getMargin ().getYSum () +
                                      aElement.getPadding ().getYSum ();
        // Update used width and height
        fUsedWidth += fItemWidthFull;
        fRestWidth -= fItemWidthFull;
        fUsedHeight = Math.max (fUsedHeight, fItemHeightFull);
        // Remember width and height for element (without padding and margin)
        m_aWidth[nIndex] = fItemWidth;
        m_aHeight[nIndex] = fItemHeight;
      }
      ++nIndex;
    }
    // 2. all star widths items
    nIndex = 0;
    for (final Column aColumn : m_aColumns)
    {
      if (aColumn.getWidth ().isStar ())
      {
        final AbstractPLElement <?> aElement = aColumn.getElement ();
        // Full width of this element
        final float fItemWidthFull = fRestWidth / m_nStarWidthItems;
        // Effective content width of this element
        final float fItemWidth = fItemWidthFull - aElement.getMargin ().getXSum () - aElement.getPadding ().getXSum ();
        // Prepare child element
        final float fItemHeight = aElement.prepare (new RenderPreparationContext (fItemWidth, fAvailableHeight))
                                          .getHeight ();
        final float fItemHeightFull = fItemHeight +
                                      aElement.getMargin ().getYSum () +
                                      aElement.getPadding ().getYSum ();
        // Update used width and height
        fUsedWidth += fItemWidthFull;
        fUsedHeight = Math.max (fUsedHeight, fItemHeightFull);
        // Remember width and height for element (without padding and margin)
        m_aWidth[nIndex] = fItemWidth;
        m_aHeight[nIndex] = fItemHeight;
      }
      ++nIndex;
    }

    // Small consistency check (with rounding included)
    if (fUsedWidth - fAvailableWidth > 0.01)
      s_aLogger.warn ("HBox uses more width (" + fUsedWidth + ") than available (" + fAvailableWidth + ")!");
    if (fUsedHeight - fAvailableHeight > 0.01)
      s_aLogger.warn ("HBox uses more height (" + fUsedHeight + ") than available (" + fAvailableHeight + ")!");
    return new SizeSpec (fUsedWidth, fUsedHeight);
  }

  @Override
  protected void onPerform (@Nonnull final RenderingContext aCtx) throws IOException
  {
    final PDPageContentStreamWithCache aContentStream = aCtx.getContentStream ();
    float fCurX = aCtx.getStartLeft () + getPadding ().getLeft ();
    final float fCurY = aCtx.getStartTop () - getPadding ().getTop ();
    int nIndex = 0;
    for (final Column aColumn : m_aColumns)
    {
      final AbstractPLElement <?> aElement = aColumn.getElement ();
      final float fItemWidth = m_aWidth[nIndex];
      final float fItemWidthWithPadding = fItemWidth + aElement.getPadding ().getXSum ();
      final float fItemHeight = m_aHeight[nIndex];
      final float fItemHeightWithPadding = fItemHeight + aElement.getPadding ().getYSum ();
      final RenderingContext aItemCtx = new RenderingContext (aCtx,
                                                              fCurX + aElement.getMargin ().getLeft (),
                                                              fCurY - aElement.getMargin ().getTop (),
                                                              fItemWidthWithPadding,
                                                              fItemHeightWithPadding);

      // apply special column borders - debug: blue
      {
        // Disregard the padding of this HBox!!!
        final float fLeft = fCurX;
        final float fTop = fCurY;
        final float fWidth = fItemWidthWithPadding + aElement.getMargin ().getXSum ();
        final float fHeight = aCtx.getHeight () - getPadding ().getYSum ();

        // Fill before border
        if (m_aColumnFillColor != null)
        {
          aContentStream.setNonStrokingColor (m_aColumnFillColor);
          aContentStream.fillRect (fLeft, fTop - fHeight, fWidth, fHeight);
        }

        BorderSpec aRealBorder = m_aColumnBorder;
        if (shouldApplyDebugBorder (aRealBorder, aCtx.isDebugMode ()))
          aRealBorder = new BorderSpec (new BorderStyleSpec (new Color (0, 0, 255)));
        if (aRealBorder.hasAnyBorder ())
          renderBorder (aContentStream, fLeft, fTop, fWidth, fHeight, aRealBorder);
      }

      // Perform contained element after border
      aElement.perform (aItemCtx);

      // Update X-pos
      fCurX += fItemWidthWithPadding + aElement.getMargin ().getXSum ();
      ++nIndex;
    }
  }
}
