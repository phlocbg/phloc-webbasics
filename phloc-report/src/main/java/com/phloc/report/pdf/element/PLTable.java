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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.report.pdf.spec.SizeSpec;
import com.phloc.report.pdf.spec.WidthSpec;

/**
 * A special table with a repeating header
 * 
 * @author Philip Helger
 */
public class PLTable extends PLVBox implements IPLSplittableElement
{
  private final List <WidthSpec> m_aWidths;
  private int m_nHeaderRowCount = 0;

  public PLTable (@Nonnull @Nonempty final Iterable <? extends WidthSpec> aWidths)
  {
    if (ContainerHelper.isEmpty (aWidths))
      throw new IllegalArgumentException ("Widths may not be empty");
    if (ContainerHelper.containsAnyNullElement (aWidths))
      throw new IllegalArgumentException ("Width may not contain null elements");
    m_aWidths = ContainerHelper.newList (aWidths);
  }

  /**
   * @return A copy of the list with all widths as specified in the constructor.
   *         Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public List <WidthSpec> getWidths ()
  {
    return ContainerHelper.newList (m_aWidths);
  }

  /**
   * @return The number of columns in the table. Always &ge; 0.
   */
  @Nonnegative
  public int getColumnCount ()
  {
    return m_aWidths.size ();
  }

  /**
   * Add a new table row. All contained elements are added with the specified
   * width in the constructor. <code>null</code> elements are represented as
   * empty cells.
   * 
   * @param aElements
   *        The elements to add. May be <code>null</code>.
   * @return The added row and never <code>null</code>.
   */
  @Nonnull
  public PLHBox addTableRow (@Nullable final AbstractPLElement <?>... aElements)
  {
    return addTableRow (ContainerHelper.newList (aElements));
  }

  /**
   * Add a new table row. All contained elements are added with the specified
   * width in the constructor. <code>null</code> elements are represented as
   * empty cells.
   * 
   * @param aElements
   *        The elements to add. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public PLHBox addTableRow (@Nonnull final List <? extends AbstractPLElement <?>> aElements)
  {
    if (aElements == null)
      throw new NullPointerException ("elements");
    if (aElements.size () > m_aWidths.size ())
      throw new IllegalArgumentException ("More elements in row (" +
                                          aElements.size () +
                                          ") than defined in the table (" +
                                          m_aWidths.size () +
                                          ")!");

    final PLHBox aHBox = new PLHBox ();
    for (int i = 0; i < aElements.size (); ++i)
    {
      AbstractPLElement <?> aElement = aElements.get (i);
      if (aElement == null)
      {
        // null elements end as a spacer
        aElement = new PLSpacerX ();
      }
      final WidthSpec aWidth = m_aWidths.get (i);
      aHBox.addColumn (aElement, aWidth);
    }
    super.addRow (aHBox);
    return aHBox;
  }

  /**
   * @return The number of header rows. By default 0. Always &ge; 0.
   */
  @Nonnegative
  public int getHeaderRowCount ()
  {
    return m_nHeaderRowCount;
  }

  /**
   * Set the number of header rows in this table.
   * 
   * @param nHeaderRowCount
   *        The number of header rows, to be repeated by page. Must be &ge; 0.
   * @return this
   */
  @Nonnull
  public PLTable setHeaderRowCount (@Nonnegative final int nHeaderRowCount)
  {
    if (nHeaderRowCount < 0)
      throw new IllegalArgumentException ("HeaderRowCount must be >= 0: " + nHeaderRowCount);
    m_nHeaderRowCount = nHeaderRowCount;
    return this;
  }

  @Nullable
  public SplitResult splitElements (final float fAvailableHeight)
  {
    final PLTable aTable1 = new PLTable (m_aWidths).setHeaderRowCount (m_nHeaderRowCount);
    final PLTable aTable2 = new PLTable (m_aWidths).setHeaderRowCount (m_nHeaderRowCount);

    // Copy all header rows
    float fTable1Width = 0;
    float fTable1Height = 0;
    for (int i = 0; i < m_nHeaderRowCount; ++i)
    {
      final AbstractPLElement <?> aHeaderRow = getRowElementAtIndex (i);
      aTable1.addRow (aHeaderRow);
      aTable2.addRow (aHeaderRow);

      fTable1Width = Math.max (fTable1Width, m_aPreparedWidth[i] + aHeaderRow.getMarginPlusPaddingXSum ());
      fTable1Height += m_aPreparedHeight[i] + aHeaderRow.getMarginPlusPaddingYSum ();
    }
    float fTable2Width = fTable1Width;
    float fTable2Height = fTable1Height;

    // Copy all content rows
    final int nTotalRows = getRowCount ();
    boolean bOnTable1 = true;
    for (int i = m_nHeaderRowCount; i < nTotalRows; ++i)
    {
      final AbstractPLElement <?> aRow = getRowElementAtIndex (i);
      final float fRowHeightFull = m_aPreparedHeight[i] + aRow.getMarginPlusPaddingYSum ();
      if (bOnTable1 && fTable1Height + fRowHeightFull <= fAvailableHeight)
      {
        aTable1.addRow (aRow);
        fTable1Width = Math.max (fTable1Width, m_aPreparedWidth[i] + aRow.getMarginPlusPaddingXSum ());
        fTable1Height += fRowHeightFull;
      }
      else
      {
        bOnTable1 = false;
        aTable2.addRow (aRow);
        fTable2Width = Math.max (fTable2Width, m_aPreparedWidth[i] + aRow.getMarginPlusPaddingXSum ());
        fTable2Height += fRowHeightFull;
      }
    }

    if (aTable1.getRowCount () == m_nHeaderRowCount)
    {
      // Splitting makes no sense!
      return null;
    }

    // Including padding/margin
    aTable1.markAsPrepared (new SizeSpec (fTable1Width, fTable1Height));
    aTable2.markAsPrepared (new SizeSpec (fTable2Width, fTable2Height));

    final float [] aWidth1 = new float [aTable1.getRowCount ()];
    final float [] aHeight1 = new float [aTable1.getRowCount ()];
    final float [] aWidth2 = new float [aTable2.getRowCount ()];
    final float [] aHeight2 = new float [aTable2.getRowCount ()];
    for (int i = 0; i < m_nHeaderRowCount; ++i)
    {
      aWidth1[i] = aWidth2[i] = m_aPreparedWidth[i];
      aHeight1[i] = aHeight2[i] = m_aPreparedHeight[i];
    }

    for (int i = m_nHeaderRowCount; i < aTable1.getRowCount (); ++i)
    {
      aWidth1[i] = m_aPreparedWidth[i];
      aHeight1[i] = m_aPreparedHeight[i];
    }
    aTable1.m_aPreparedWidth = aWidth1;
    aTable1.m_aPreparedHeight = aHeight1;

    final int nDelta = nTotalRows - aTable2.getRowCount () - m_nHeaderRowCount + 1;
    for (int i = m_nHeaderRowCount; i < aTable2.getRowCount (); ++i)
    {
      aWidth2[i] = m_aPreparedWidth[i + nDelta];
      aHeight2[i] = m_aPreparedHeight[i + nDelta];
    }
    aTable2.m_aPreparedWidth = aWidth2;
    aTable2.m_aPreparedHeight = aHeight2;

    return new SplitResult (new PLElementWithHeight (aTable1, fTable1Height), new PLElementWithHeight (aTable2,
                                                                                                       fTable2Height));
  }

  /**
   * Create a new table with the specified percentages.
   * 
   * @param aPercentages
   *        The array to use. The sum of all percentages should be &le; 100. May
   *        neither be <code>null</code> nor empty.
   * @return The created {@link PLTable} and never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static PLTable createWithPercentage (@Nonnull @Nonempty final float... aPercentages)
  {
    if (ArrayHelper.isEmpty (aPercentages))
      throw new IllegalArgumentException ("Percentages");
    final List <WidthSpec> aWidths = new ArrayList <WidthSpec> (aPercentages.length);
    for (final float fPercentage : aPercentages)
      aWidths.add (WidthSpec.perc (fPercentage));
    return new PLTable (aWidths);
  }

  /**
   * Create a new table with evenly sized columns.
   * 
   * @param nColumnCount
   *        The number of columns to use. Must be &gt; 0.
   * @return The created {@link PLTable} and never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static PLTable createWithEvenlySizedColumns (@Nonnegative final int nColumnCount)
  {
    if (nColumnCount < 1)
      throw new IllegalArgumentException ("Columns must be >= 1: " + nColumnCount);
    final List <WidthSpec> aWidths = new ArrayList <WidthSpec> (nColumnCount);
    for (int i = 0; i < nColumnCount; ++i)
      aWidths.add (WidthSpec.star ());
    return new PLTable (aWidths);
  }
}
