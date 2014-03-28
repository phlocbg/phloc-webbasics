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

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.report.pdf.spec.SizeSpec;
import com.phloc.report.pdf.spec.WidthSpec;

/**
 * Horizontal box - groups several columns.
 *
 * @author Philip Helger
 */
public abstract class AbstractPLHBoxSplittable <IMPLTYPE extends AbstractPLHBoxSplittable <IMPLTYPE>> extends AbstractPLHBox <IMPLTYPE> implements IPLSplittableElement
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractPLHBoxSplittable.class);

  public AbstractPLHBoxSplittable ()
  {}

  public boolean containsAnySplittableElement ()
  {
    for (final Column aColumn : m_aColumns)
      if (aColumn.getElement ().isSplittable ())
        return true;
    return false;
  }

  @Nullable
  public PLSplitResult splitElements (final float fElementWidth, final float fAvailableHeight)
  {
    if (fAvailableHeight < 0)
    {
      return null;
    }

    if (!containsAnySplittableElement ())
    {
      // Splitting makes no sense
      return null;
    }

    final int nCols = m_aColumns.size ();

    boolean bAnySplittingPossible = false;
    for (int i = 0; i < nCols; ++i)
    {
      // Is the current element higher and splittable?
      if (m_aPreparedHeight[i] > fAvailableHeight && getColumnElementAtIndex (i).isSplittable ())
      {
        bAnySplittingPossible = true;
        break;
      }
    }

    if (!bAnySplittingPossible)
    {
      // Splitting makes no sense
      return null;
    }

    final PLHBoxSplittable aHBox1 = new PLHBoxSplittable ();
    aHBox1.setBasicDataFrom (this);
    final PLHBoxSplittable aHBox2 = new PLHBoxSplittable ();
    aHBox2.setBasicDataFrom (this);

    // Fill all columns with empty content
    for (int i = 0; i < nCols; ++i)
    {
      final Column aColumn = getColumnAtIndex (i);
      final WidthSpec aColumnWidth = aColumn.getWidth ();
      final AbstractPLElement <?> aColumnElement = aColumn.getElement ();

      // Create empty element with the same padding and margin as the original
      // element
      final PLSpacerX aEmptyElement = new PLSpacerX ();
      aEmptyElement.setPadding (aColumnElement.getPadding ());
      aEmptyElement.setMargin (aColumnElement.getMargin ());
      aEmptyElement.markAsPrepared (new SizeSpec (m_aPreparedWidth[i], 0));

      aHBox1.addColumn (aEmptyElement, aColumnWidth);
      aHBox2.addColumn (aEmptyElement, aColumnWidth);
    }

    float fHBox1MaxHeight = 0;
    float fHBox2MaxHeight = 0;
    final float [] fHBox1Heights = new float [m_aPreparedHeight.length];
    final float [] fHBox2Heights = new float [m_aPreparedHeight.length];

    // Start splitting columns
    boolean bDidSplitAnyColumn = false;
    for (int i = 0; i < nCols; i++)
    {
      final AbstractPLElement <?> aElement = getColumnElementAtIndex (i);
      final boolean bIsSplittable = aElement.isSplittable ();
      final float fColumnHeight = m_aPreparedHeight[i] + aElement.getMarginPlusPaddingYSum ();
      final float fColumnWidth = m_aPreparedWidth[i] + aElement.getMarginPlusPaddingXSum ();

      // add the column to the first hbox
      boolean bDidSplit = false;
      if (fColumnHeight > fAvailableHeight && bIsSplittable)
      {
        final PLSplitResult aSplitResult = aElement.getAsSplittable ()
            .splitElements (fColumnWidth,
                            fAvailableHeight -
                            aElement.getMarginPlusPaddingYSum ());

        if (aSplitResult != null)
        {
          aHBox1.getColumnAtIndex (i).setElement (aSplitResult.getFirstElement ().getElement ());
          aHBox2.getColumnAtIndex (i).setElement (aSplitResult.getSecondElement ().getElement ());

          fHBox1Heights[i] = aSplitResult.getFirstElement ().getHeight ();
          fHBox2Heights[i] = aSplitResult.getSecondElement ().getHeight ();
          bDidSplit = true;
          bDidSplitAnyColumn = true;

          if (s_aLogger.isInfoEnabled ())
            s_aLogger.info ("Split " +
                CGStringHelper.getClassLocalName (aElement) +
                " into pieces: " +
                aSplitResult.getFirstElement ().getHeight () +
                " and " +
                aSplitResult.getSecondElement ().getHeight ());
        }
      }

      if (!bDidSplit)
      {
        // Cell fits totally in available height
        aHBox1.getColumnAtIndex (i).setElement (aElement);

        fHBox1Heights[i] = Math.min (fColumnHeight, fAvailableHeight);
        fHBox2Heights[i] = 0;

        if (fColumnHeight > fAvailableHeight)
          if (bIsSplittable)
          {
            s_aLogger.warn ("Column " +
                i +
                " of " +
                CGStringHelper.getClassLocalName (this) +
                " contains splittable element of type " +
                CGStringHelper.getClassLocalName (aElement) +
                " which creates an overflow by " +
                (fColumnHeight - fAvailableHeight) +
                " for max height " +
                fAvailableHeight +
                "!");
          }
          else
          {
            s_aLogger.warn ("Column " +
                i +
                " of " +
                CGStringHelper.getClassLocalName (this) +
                " contains non splittable element of type " +
                CGStringHelper.getClassLocalName (aElement) +
                " which creates an overflow by " +
                (fColumnHeight - fAvailableHeight) +
                " for max height " +
                fAvailableHeight +
                "!");
          }
      }

      // calculate max column height
      fHBox1MaxHeight = Math.max (fHBox1MaxHeight, fHBox1Heights[i]);
      fHBox2MaxHeight = Math.max (fHBox2MaxHeight, fHBox2Heights[i]);
    }

    if (!bDidSplitAnyColumn)
    {
      // Nothing was splitted
      return null;
    }

    // mark new hboxes as prepared
    aHBox1.markAsPrepared (new SizeSpec (fElementWidth, fHBox1MaxHeight));
    aHBox2.markAsPrepared (new SizeSpec (fElementWidth, fHBox2MaxHeight));
    // reuse prepared widths - nothing changed here
    aHBox1.m_aPreparedWidth = ArrayHelper.getCopy (m_aPreparedWidth);
    aHBox2.m_aPreparedWidth = ArrayHelper.getCopy (m_aPreparedWidth);
    // set all column heights
    aHBox1.m_aPreparedHeight = fHBox1Heights;
    aHBox2.m_aPreparedHeight = fHBox2Heights;

    return new PLSplitResult (new PLElementWithSize (aHBox1, new SizeSpec (fElementWidth, fHBox1MaxHeight)),
                              new PLElementWithSize (aHBox2, new SizeSpec (fElementWidth, fHBox2MaxHeight)));
  }
}
