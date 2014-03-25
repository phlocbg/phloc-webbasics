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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.report.pdf.render.PDPageContentStreamWithCache;
import com.phloc.report.pdf.render.PageSetupContext;
import com.phloc.report.pdf.render.PreparationContext;
import com.phloc.report.pdf.render.RenderingContext;
import com.phloc.report.pdf.spec.BorderSpec;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.SizeSpec;
import com.phloc.report.pdf.spec.WidthSpec;

/**
 * Horizontal box - groups several columns.
 *
 * @author Philip Helger
 */
public class AbstractPLHBox <IMPLTYPE extends AbstractPLHBox <IMPLTYPE>> extends AbstractPLElement <IMPLTYPE>
{
  @NotThreadSafe
  public static final class Column
  {
    private AbstractPLElement <?> m_aElement;
    private final WidthSpec m_aWidth;

    public Column (@Nonnull final AbstractPLElement <?> aElement, @Nonnull final WidthSpec aWidth)
    {
      setElement (aElement);
      m_aWidth = ValueEnforcer.notNull (aWidth, "Width");
    }

    @Nonnull
    public AbstractPLElement <?> getElement ()
    {
      return m_aElement;
    }

    @Nonnull
    public Column setElement (@Nonnull final AbstractPLElement <?> aElement)
    {
      m_aElement = ValueEnforcer.notNull (aElement, "Element");
      return this;
    }

    @Nonnull
    public WidthSpec getWidth ()
    {
      return m_aWidth;
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("element", m_aElement).append ("width", m_aWidth).toString ();
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractPLHBox.class);

  protected final List <Column> m_aColumns = new ArrayList <Column> ();
  private int m_nStarWidthItems = 0;
  private BorderSpec m_aColumnBorder = BorderSpec.BORDER0;
  private Color m_aColumnFillColor = null;
  /** prepare width (without padding and margin) */
  protected float [] m_aPreparedWidth;
  /** prepare height (without padding and margin) */
  protected float [] m_aPreparedHeight;

  public AbstractPLHBox ()
  {}

  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public IMPLTYPE setBasicDataFrom (@Nonnull final AbstractPLHBox <?> aSource)
  {
    super.setBasicDataFrom (aSource);
    setColumnBorder (aSource.m_aColumnBorder);
    setColumnFillColor (aSource.m_aColumnFillColor);
    return thisAsT ();
  }

  /**
   * @return The number of columns. Always &ge; 0.
   */
  @Nonnegative
  public int getColumnCount ()
  {
    return m_aColumns.size ();
  }

  /**
   * @return All columns. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <Column> getAllColumns ()
  {
    return ContainerHelper.newList (m_aColumns);
  }

  @Nullable
  public Column getColumnAtIndex (@Nonnegative final int nIndex)
  {
    return ContainerHelper.getSafe (m_aColumns, nIndex);
  }

  @Nullable
  public Column getFirstColumn ()
  {
    return ContainerHelper.getFirstElement (m_aColumns);
  }

  @Nullable
  public Column getLastColumn ()
  {
    return ContainerHelper.getLastElement (m_aColumns);
  }

  @Nullable
  public AbstractPLElement <?> getColumnElementAtIndex (@Nonnegative final int nIndex)
  {
    final Column aColumn = getColumnAtIndex (nIndex);
    return aColumn == null ? null : aColumn.getElement ();
  }

  @Nullable
  public AbstractPLElement <?> getFirstColumnElement ()
  {
    final Column aColumn = getFirstColumn ();
    return aColumn == null ? null : aColumn.getElement ();
  }

  @Nullable
  public AbstractPLElement <?> getLastColumnElement ()
  {
    final Column aColumn = getLastColumn ();
    return aColumn == null ? null : aColumn.getElement ();
  }

  @Nonnull
  public IMPLTYPE addColumn (@Nonnull final AbstractPLElement <?> aElement, @Nonnull final WidthSpec aWidth)
  {
    checkNotPrepared ();
    final Column aItem = new Column (aElement, aWidth);
    m_aColumns.add (aItem);
    if (aWidth.isStar ())
      m_nStarWidthItems++;
    return thisAsT ();
  }

  /**
   * Set the border around each contained column.
   *
   * @param aBorder
   *        The border style to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setColumnBorder (@Nullable final BorderStyleSpec aBorder)
  {
    return setColumnBorder (new BorderSpec (aBorder));
  }

  /**
   * Set the border around each contained column.
   *
   * @param aBorderX
   *        The border to set for left and right. Maybe <code>null</code>.
   * @param aBorderY
   *        The border to set for top and bottom. Maybe <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setColumnBorder (@Nullable final BorderStyleSpec aBorderX,
                                         @Nullable final BorderStyleSpec aBorderY)
  {
    return setColumnBorder (new BorderSpec (aBorderX, aBorderY));
  }

  /**
   * Set the border around each contained column.
   *
   * @param aBorderLeft
   *        The border to set for left. Maybe <code>null</code>.
   * @param aBorderTop
   *        The border to set for top. Maybe <code>null</code>.
   * @param aBorderRight
   *        The border to set for right. Maybe <code>null</code>.
   * @param aBorderBottom
   *        The border to set for bottom. Maybe <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setColumnBorder (@Nullable final BorderStyleSpec aBorderLeft,
                                         @Nullable final BorderStyleSpec aBorderTop,
                                         @Nullable final BorderStyleSpec aBorderRight,
                                         @Nullable final BorderStyleSpec aBorderBottom)
  {
    return setColumnBorder (new BorderSpec (aBorderLeft, aBorderTop, aBorderRight, aBorderBottom));
  }

  /**
   * Set the border around each contained column.
   *
   * @param aBorder
   *        The border to set. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setColumnBorder (@Nonnull final BorderSpec aBorder)
  {
    ValueEnforcer.notNull (aBorder, "ColumnBorder");
    checkNotPrepared ();
    m_aColumnBorder = aBorder;
    return thisAsT ();
  }

  /**
   * Set the left border value around each contained column. This method may not
   * be called after an element got prepared!
   *
   * @param aBorder
   *        The value to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setColumnBorderLeft (@Nullable final BorderStyleSpec aBorder)
  {
    return setColumnBorder (m_aColumnBorder.getCloneWithLeft (aBorder));
  }

  /**
   * Set the top border value around each contained column. This method may not
   * be called after an element got prepared!
   *
   * @param aBorder
   *        The value to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setColumnBorderTop (@Nullable final BorderStyleSpec aBorder)
  {
    return setColumnBorder (m_aColumnBorder.getCloneWithTop (aBorder));
  }

  /**
   * Set the right border value around each contained column. This method may
   * not be called after an element got prepared!
   *
   * @param aBorder
   *        The value to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setColumnBorderRight (@Nullable final BorderStyleSpec aBorder)
  {
    return setColumnBorder (m_aColumnBorder.getCloneWithRight (aBorder));
  }

  /**
   * Set the bottom border value around each contained column. This method may
   * not be called after an element got prepared!
   *
   * @param aBorder
   *        The value to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final IMPLTYPE setColumnBorderBottom (@Nullable final BorderStyleSpec aBorder)
  {
    return setColumnBorder (m_aColumnBorder.getCloneWithBottom (aBorder));
  }

  /**
   * Get the border around each contained column. By default
   * {@link BorderSpec#BORDER0} which means no border is used.
   *
   * @return Never <code>null</code>.
   */
  @Nonnull
  public final BorderSpec getColumnBorder ()
  {
    return m_aColumnBorder;
  }

  /**
   * Set the fill color to be used to fill the whole column. <code>null</code>
   * means no fill color.
   *
   * @param aColumnFillColor
   *        The fill color to use. May be <code>null</code> to indicate no fill
   *        color (which is also the default).
   * @return this
   */
  @Nonnull
  public IMPLTYPE setColumnFillColor (@Nullable final Color aColumnFillColor)
  {
    m_aColumnFillColor = aColumnFillColor;
    return thisAsT ();
  }

  /**
   * Get the fill color to be used to fill the whole column. <code>null</code>
   * means no fill color.
   *
   * @return May be <code>null</code>.
   */
  @Nullable
  public Color getColumnFillColor ()
  {
    return m_aColumnFillColor;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  protected SizeSpec onPrepare (@Nonnull final PreparationContext aCtx) throws IOException
  {
    m_aPreparedWidth = new float [m_aColumns.size ()];
    m_aPreparedHeight = new float [m_aColumns.size ()];
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
        final float fItemWidth = fItemWidthFull - aElement.getMarginPlusPaddingXSum ();
        // Prepare child element
        final float fItemHeight = aElement.prepare (new PreparationContext (fItemWidth, fAvailableHeight)).getHeight ();
        final float fItemHeightFull = fItemHeight + aElement.getMarginPlusPaddingYSum ();
        // Update used width and height
        fUsedWidth += fItemWidthFull;
        fRestWidth -= fItemWidthFull;
        fUsedHeight = Math.max (fUsedHeight, fItemHeightFull);
        // Remember width and height for element (without padding and margin)
        m_aPreparedWidth[nIndex] = fItemWidth;
        m_aPreparedHeight[nIndex] = fItemHeight;
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
        final float fItemWidth = fItemWidthFull - aElement.getMarginPlusPaddingXSum ();
        // Prepare child element
        final float fItemHeight = aElement.prepare (new PreparationContext (fItemWidth, fAvailableHeight)).getHeight ();
        final float fItemHeightFull = fItemHeight + aElement.getMarginPlusPaddingYSum ();
        // Update used width and height
        fUsedWidth += fItemWidthFull;
        fUsedHeight = Math.max (fUsedHeight, fItemHeightFull);
        // Remember width and height for element (without padding and margin)
        m_aPreparedWidth[nIndex] = fItemWidth;
        m_aPreparedHeight[nIndex] = fItemHeight;
      }
      ++nIndex;
    }

    // Small consistency check (with rounding included)
    if (GlobalDebug.isDebugMode ())
    {
      if (fUsedWidth - fAvailableWidth > 0.01)
        s_aLogger.warn ("HBox uses more width (" + fUsedWidth + ") than available (" + fAvailableWidth + ")!");
      if (fUsedHeight - fAvailableHeight > 0.01)
        if (!isSplittable ())
          s_aLogger.warn ("HBox uses more height (" + fUsedHeight + ") than available (" + fAvailableHeight + ")!");
    }

    return new SizeSpec (fUsedWidth, fUsedHeight);
  }

  @Override
  public void doPageSetup (@Nonnull final PageSetupContext aCtx)
  {
    for (final Column aColumn : m_aColumns)
      aColumn.getElement ().doPageSetup (aCtx);
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
      final float fItemWidth = m_aPreparedWidth[nIndex];
      final float fItemWidthWithPadding = fItemWidth + aElement.getPadding ().getXSum ();
      final float fItemHeight = m_aPreparedHeight[nIndex];
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

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("columns", m_aColumns)
                            .append ("startWidthItems", m_nStarWidthItems)
                            .append ("columnBorder", m_aColumnBorder)
                            .appendIfNotNull ("columnFillColor", m_aColumnFillColor)
                            .appendIfNotNull ("preparedWidth", m_aPreparedWidth)
                            .appendIfNotNull ("preparedHeight", m_aPreparedHeight)
                            .toString ();
  }
}
