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

/**
 * Vertical box - groups several rows.
 * 
 * @author Philip Helger
 */
public class PLVBox extends AbstractPLElement <PLVBox>
{
  private static final class Row
  {
    private final AbstractPLElement <?> m_aElement;

    public Row (@Nonnull final AbstractPLElement <?> aElement)
    {
      if (aElement == null)
        throw new NullPointerException ("element");
      m_aElement = aElement;
    }

    @Nonnull
    public AbstractPLElement <?> getElement ()
    {
      return m_aElement;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (PLVBox.class);

  private final List <Row> m_aRows = new ArrayList <Row> ();
  private BorderSpec m_aRowBorder = BorderSpec.BORDER0;
  private Color m_aRowFillColor = null;
  // prepare result (without padding and margin)
  private float [] m_aWidth;
  private float [] m_aHeight;

  public PLVBox ()
  {}

  @Nonnegative
  public int getRowCount ()
  {
    return m_aRows.size ();
  }

  @Nonnull
  public PLVBox addRow (@Nonnull final AbstractPLElement <?> aElement)
  {
    checkNotPrepared ();
    final Row aItem = new Row (aElement);
    m_aRows.add (aItem);
    return this;
  }

  @Nonnull
  public final PLVBox setRowBorder (@Nullable final BorderStyleSpec aBorder)
  {
    return setRowBorder (new BorderSpec (aBorder));
  }

  @Nonnull
  public final PLVBox setRowBorder (@Nullable final BorderStyleSpec aBorderX, @Nullable final BorderStyleSpec aBorderY)
  {
    return setRowBorder (new BorderSpec (aBorderX, aBorderY));
  }

  @Nonnull
  public final PLVBox setRowBorder (@Nullable final BorderStyleSpec aBorderLeft,
                                    @Nullable final BorderStyleSpec aBorderTop,
                                    @Nullable final BorderStyleSpec aBorderRight,
                                    @Nullable final BorderStyleSpec aBorderBottom)
  {
    return setRowBorder (new BorderSpec (aBorderLeft, aBorderTop, aBorderRight, aBorderBottom));
  }

  @Nonnull
  public final PLVBox setRowBorder (@Nonnull final BorderSpec aBorder)
  {
    if (aBorder == null)
      throw new NullPointerException ("RowBorder");
    checkNotPrepared ();
    m_aRowBorder = aBorder;
    return this;
  }

  @Nonnull
  public final BorderSpec getRowBorder ()
  {
    return m_aRowBorder;
  }

  @Nonnull
  public PLVBox setRowFillColor (@Nullable final Color aRowFillColor)
  {
    m_aRowFillColor = aRowFillColor;
    return this;
  }

  @Nullable
  public Color getRowFillColor ()
  {
    return m_aRowFillColor;
  }

  @Override
  protected SizeSpec onPrepare (@Nonnull final RenderPreparationContext aCtx) throws IOException
  {
    m_aWidth = new float [m_aRows.size ()];
    m_aHeight = new float [m_aRows.size ()];
    final float fAvailableWidth = aCtx.getAvailableWidth ();
    final float fAvailableHeight = aCtx.getAvailableHeight ();
    float fUsedWidth = 0;
    float fUsedHeight = 0;
    int nIndex = 0;
    for (final Row aRow : m_aRows)
    {
      final AbstractPLElement <?> aElement = aRow.getElement ();
      // Full width of this element
      final float fItemWidthFull = fAvailableWidth;
      // Effective content width of this element
      final float fItemWidth = fItemWidthFull - aElement.getMargin ().getXSum () - aElement.getPadding ().getXSum ();
      // Prepare child element
      final float fItemHeight = aElement.prepare (new RenderPreparationContext (fItemWidth, fAvailableHeight))
                                        .getHeight ();
      final float fItemHeightFull = fItemHeight + aElement.getMargin ().getYSum () + aElement.getPadding ().getYSum ();
      // Update used width and height
      fUsedWidth = Math.max (fUsedWidth, fItemWidthFull);
      fUsedHeight += fItemHeightFull;
      // Remember width and height for element (without padding and margin)
      m_aWidth[nIndex] = fItemWidth;
      m_aHeight[nIndex] = fItemHeight;
      ++nIndex;
    }

    // Small consistency check (with rounding included)
    if (fUsedWidth - fAvailableWidth > 0.01)
      s_aLogger.warn ("VBox uses more width (" + fUsedWidth + ") than available (" + fAvailableWidth + ")!");
    if (fUsedHeight - fAvailableHeight > 0.01)
      s_aLogger.warn ("VBox uses more height (" + fUsedHeight + ") than available (" + fAvailableHeight + ")!");
    return new SizeSpec (fUsedWidth, fUsedHeight);
  }

  @Override
  protected void onPerform (@Nonnull final RenderingContext aCtx) throws IOException
  {
    final PDPageContentStreamWithCache aContentStream = aCtx.getContentStream ();
    final float fCurX = aCtx.getStartLeft () + getPadding ().getLeft ();
    float fCurY = aCtx.getStartTop () - getPadding ().getTop ();
    int nIndex = 0;
    for (final Row aRow : m_aRows)
    {
      final AbstractPLElement <?> aElement = aRow.getElement ();
      final float fItemWidth = m_aWidth[nIndex];
      final float fItemWidthWithPadding = fItemWidth + aElement.getPadding ().getXSum ();
      final float fItemHeight = m_aHeight[nIndex];
      final float fItemHeightWithPadding = fItemHeight + aElement.getPadding ().getYSum ();
      final RenderingContext aItemCtx = new RenderingContext (aCtx,
                                                              fCurX + aElement.getMargin ().getLeft (),
                                                              fCurY - aElement.getMargin ().getTop (),
                                                              fItemWidthWithPadding,
                                                              fItemHeightWithPadding);

      // apply special row borders - debug: blue
      {
        // Disregard the padding of this VBox!!!
        final float fLeft = fCurX;
        final float fTop = fCurY;
        final float fWidth = aCtx.getWidth () - getPadding ().getXSum ();
        final float fHeight = fItemHeightWithPadding + aElement.getMargin ().getYSum ();

        // Fill before border
        if (m_aRowFillColor != null)
        {
          aContentStream.setNonStrokingColor (m_aRowFillColor);
          aContentStream.fillRect (fLeft, fTop - fHeight, fWidth, fHeight);
        }

        BorderSpec aRealBorder = m_aRowBorder;
        if (shouldApplyDebugBorder (aRealBorder, aCtx.isDebugMode ()))
          aRealBorder = new BorderSpec (new BorderStyleSpec (new Color (0, 0, 255)));
        if (aRealBorder.hasAnyBorder ())
          renderBorder (aContentStream, fLeft, fTop, fWidth, fHeight, aRealBorder);
      }

      // Perform contained element after border
      aElement.perform (aItemCtx);

      // Update Y-pos
      fCurY -= fItemHeightWithPadding + aElement.getMargin ().getYSum ();
      ++nIndex;
    }
  }
}
