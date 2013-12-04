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

  @Nonnull
  public SplitResult splitElements (final float fAvailableHeight)
  {
    // TODO
    return null;
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
