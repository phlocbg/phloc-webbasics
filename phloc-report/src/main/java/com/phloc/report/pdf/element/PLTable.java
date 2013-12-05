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

  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public List <WidthSpec> getWidths ()
  {
    return ContainerHelper.newList (m_aWidths);
  }

  @Nonnegative
  public int getColumnCount ()
  {
    return m_aWidths.size ();
  }

  @Nonnull
  public PLTable addTableRow (@Nullable final AbstractPLElement <?>... aElements)
  {
    return addTableRow (ContainerHelper.newList (aElements));
  }

  @Nonnull
  public PLTable addTableRow (@Nonnull final List <? extends AbstractPLElement <?>> aElements)
  {
    if (aElements == null)
      throw new NullPointerException ("elements");
    if (aElements.size () > m_aWidths.size ())
      throw new IllegalArgumentException ("More elements in row than defined in the table!");

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
    return this;
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

    float fTable1Width = 0;
    float fTable1Height = 0;
    for (int i = 0; i < m_nHeaderRowCount; ++i)
    {
      final AbstractPLElement <?> aHeaderRow = getRowAtIndex (i);
      aTable1.addRow (aHeaderRow);
      aTable2.addRow (aHeaderRow);

      fTable1Width = Math.max (fTable1Width, m_aPreparedWidth[i] + aHeaderRow.getMarginPlusPaddingYSum ());
      fTable1Height += m_aPreparedHeight[i] + aHeaderRow.getMarginPlusPaddingYSum ();
    }
    float fTable2Width = fTable1Width;
    float fTable2Height = fTable1Height;

    final int nMaxRows = getRowCount ();
    boolean bOnTable1 = true;
    for (int i = m_nHeaderRowCount; i < nMaxRows; ++i)
    {
      final AbstractPLElement <?> aRow = getRowAtIndex (i);
      final float fRowHeightFull = m_aPreparedHeight[i] + aRow.getMarginPlusPaddingYSum ();
      if (bOnTable1 && fTable1Height + fRowHeightFull <= fAvailableHeight)
      {
        aTable1.addRow (aRow);
        fTable1Width = Math.max (fTable1Width, m_aPreparedWidth[i] + aRow.getMarginPlusPaddingYSum ());
        fTable1Height += fRowHeightFull;
      }
      else
      {
        bOnTable1 = false;
        aTable2.addRow (aRow);
        fTable2Width = Math.max (fTable2Width, m_aPreparedWidth[i] + aRow.getMarginPlusPaddingYSum ());
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

    final int nDelta = nMaxRows - aTable2.getRowCount () - m_nHeaderRowCount;
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
}
