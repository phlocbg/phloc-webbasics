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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.CGlobal;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.report.pdf.render.PDPageContentStreamWithCache;
import com.phloc.report.pdf.render.PreparationContext;
import com.phloc.report.pdf.render.RenderingContext;
import com.phloc.report.pdf.spec.EHorzAlignment;
import com.phloc.report.pdf.spec.FontSpec;
import com.phloc.report.pdf.spec.SizeSpec;
import com.phloc.report.pdf.spec.TextAndWidthSpec;

/**
 * Render text
 *
 * @author Philip Helger
 */
public class PLText extends AbstractPLElement <PLText>
{
  public static final EHorzAlignment DEFAULT_HORZ_ALIGNMENT = EHorzAlignment.DEFAULT;
  public static final boolean DEFAULT_TOP_DOWN = true;
  public static final int DEFAULT_MAX_ROWS = CGlobal.ILLEGAL_UINT;

  private final String m_sText;
  private final Charset m_aCharset;
  private final FontSpec m_aFont;
  private final float m_fLineHeight;
  private EHorzAlignment m_eHorzAlign = DEFAULT_HORZ_ALIGNMENT;
  private boolean m_bTopDown = DEFAULT_TOP_DOWN;
  private int m_nMaxRows = DEFAULT_MAX_ROWS;

  // prepare result
  protected int m_nPreparedLineCountUnmodified = CGlobal.ILLEGAL_UINT;
  protected List <TextAndWidthSpec> m_aPreparedLinesUnmodified;
  protected List <TextAndWidthSpec> m_aPreparedLines;

  public PLText (@Nullable final String sText, @Nonnull final FontSpec aFont)
  {
    this (sText, CCharset.CHARSET_ISO_8859_1_OBJ, aFont);
  }

  public PLText (@Nullable final String sText, @Nonnull final Charset aCharset, @Nonnull final FontSpec aFont)
  {
    m_sText = StringHelper.getNotNull (sText);
    m_aCharset = ValueEnforcer.notNull (aCharset, "Charset");
    m_aFont = ValueEnforcer.notNull (aFont, "Font");
    m_fLineHeight = m_aFont.getLineHeight ();
  }

  @Nonnull
  public String getText ()
  {
    return m_sText;
  }

  @Nonnull
  public FontSpec getFontSpec ()
  {
    return m_aFont;
  }

  @Nonnegative
  public float getLineHeight ()
  {
    return m_fLineHeight;
  }

  @Nonnull
  @OverridingMethodsMustInvokeSuper
  public PLText setBasicDataFrom (@Nonnull final PLText aSource)
  {
    super.setBasicDataFrom (aSource);
    setHorzAlign (aSource.m_eHorzAlign);
    setTopDown (aSource.m_bTopDown);
    setMaxRows (m_nMaxRows);
    return this;
  }

  /**
   * @return The horizontal alignment of the text. By default it is
   *         {@link #DEFAULT_HORZ_ALIGNMENT}. Never <code>null</code>.
   */
  @Nonnull
  public EHorzAlignment getHorzAlign ()
  {
    return m_eHorzAlign;
  }

  /**
   * Set the horizontal alignment of the text.
   *
   * @param eHorzAlign
   *        The new horizontal alignment. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public PLText setHorzAlign (@Nonnull final EHorzAlignment eHorzAlign)
  {
    if (eHorzAlign == null)
      throw new NullPointerException ("horzAlign");
    m_eHorzAlign = eHorzAlign;
    return this;
  }

  /**
   * @return <code>true</code> if the text is rendered from top to bottom, or
   *         <code>false</code> if the text is rendered from bottom to top. The
   *         default value is {@link #DEFAULT_TOP_DOWN}.
   */
  public boolean isTopDown ()
  {
    return m_bTopDown;
  }

  /**
   * Set the rendering direction: top-down or bottom-up.
   *
   * @param bTopDown
   *        <code>true</code> to render top-down, <code>false</code> to render
   *        bottom-up.
   * @return this
   */
  @Nonnull
  public PLText setTopDown (final boolean bTopDown)
  {
    m_bTopDown = bTopDown;
    return this;
  }

  /**
   * @return The maximum number of rows to be rendered. If this value is &le; 0
   *         than all rows are rendered. The default value is
   *         {@link #DEFAULT_MAX_ROWS}.
   */
  @CheckForSigned
  public int getMaxRows ()
  {
    return m_nMaxRows;
  }

  /**
   * Set the maximum number of rows to render.
   *
   * @param nMaxRows
   *        Maximum number of rows. If &le; 0 than all lines are rendered.
   * @return this
   */
  @Nonnull
  public PLText setMaxRows (final int nMaxRows)
  {
    m_nMaxRows = nMaxRows;
    return this;
  }

  final void internalSetPreparedLines (@Nonnull final List <TextAndWidthSpec> aLines)
  {
    final int nLines = aLines.size ();
    m_nPreparedLineCountUnmodified = nLines;
    m_aPreparedLinesUnmodified = aLines;
    if (m_nMaxRows <= 0)
    {
      // Use all lines
      m_aPreparedLines = aLines;
    }
    else
    {
      // Use only a certain maximum number of rows
      if (nLines <= m_nMaxRows)
      {
        // We have less lines than the maximum
        m_aPreparedLines = aLines;
      }
      else
      {
        // Maximum number of lines exceeded
        m_aPreparedLines = aLines.subList (0, m_nMaxRows);
      }
    }

    if (!m_bTopDown)
    {
      // Reverse order only once
      m_aPreparedLines = ContainerHelper.getReverseInlineList (m_aPreparedLines);
    }
  }

  @Override
  protected SizeSpec onPrepare (@Nonnull final PreparationContext aCtx) throws IOException
  {
    // Split text into rows
    internalSetPreparedLines (m_aFont.getFitToWidth (m_sText, m_aCharset, aCtx.getAvailableWidth ()));

    // Determine height by number of lines
    return new SizeSpec (aCtx.getAvailableWidth (), m_aPreparedLines.size () * m_fLineHeight);
  }

  /**
   * @return The total number of prepared lines, not taking the maxRows into
   *         consideration. Always &ge; 0.
   */
  @Nonnegative
  public int getPreparedLineCountUnmodified ()
  {
    if (m_nPreparedLineCountUnmodified == CGlobal.ILLEGAL_UINT)
      throw new IllegalStateException ("Preparation is not yet done");
    return m_nPreparedLineCountUnmodified;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <TextAndWidthSpec> getAllPreparedLinesUnmodified ()
  {
    if (m_aPreparedLinesUnmodified == null)
      throw new IllegalStateException ("Preparation is not yet done");
    return ContainerHelper.newList (m_aPreparedLinesUnmodified);
  }

  /**
   * Get the text to draw, in case it is different from the stored text (e.g.
   * for page numbers in {@link PLTextWithPlaceholders})
   *
   * @param sText
   *        Original text. Never <code>null</code>.
   * @param aCtx
   *        The current rendering context. Never <code>null</code>.
   * @return The real text to draw. May not be <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected String getTextToDraw (@Nonnull final String sText, @Nonnull final RenderingContext aCtx)
  {
    return sText;
  }

  @Override
  protected void onPerform (@Nonnull final RenderingContext aCtx) throws IOException
  {
    final PDPageContentStreamWithCache aContentStream = aCtx.getContentStream ();
    aContentStream.beginText ();

    // Set font if changed
    aContentStream.setFont (m_aFont);

    final float fLeft = getPadding ().getLeft ();
    final float fUsableWidth = aCtx.getWidth () - getPadding ().getXSum ();
    int nIndex = 0;
    final int nMax = m_aPreparedLines.size ();
    for (final TextAndWidthSpec aTW : m_aPreparedLines)
    {
      // Replace text (if any)
      float fWidth = aTW.getWidth ();
      final String sOrigText = aTW.getText ();

      // get the real text to draw
      final String sDrawText = getTextToDraw (sOrigText, aCtx);
      if (!sOrigText.equals (sDrawText))
      {
        // Text changed - recalculate width!
        fWidth = m_aFont.getStringWidth (sDrawText, m_aCharset);
      }

      float fIndentX;
      switch (m_eHorzAlign)
      {
        case LEFT:
          fIndentX = fLeft;
          break;
        case CENTER:
          fIndentX = fLeft + (fUsableWidth - fWidth) / 2;
          break;
        case RIGHT:
          fIndentX = fLeft + fUsableWidth - fWidth;
          break;
        default:
          throw new IllegalStateException ("Unsupported horizontal alignment " + m_eHorzAlign);
      }

      if (nIndex == 0)
      {
        // Initial move - only partial line height!
        aContentStream.moveTextPositionByAmount (aCtx.getStartLeft () + fIndentX, aCtx.getStartTop () -
                                                                                  getPadding ().getTop () -
                                                                                  (m_fLineHeight * 0.75f));
      }
      else
        if (fIndentX != 0)
        {
          // Indent subsequent line
          aContentStream.moveTextPositionByAmount (fIndentX, 0);
        }

      // Main draw string
      aContentStream.drawString (sDrawText);
      ++nIndex;

      // Goto next line
      if (nIndex < nMax)
      {
        if (m_bTopDown)
        {
          // Outdent and one line down, except for last line
          aContentStream.moveTextPositionByAmount (-fIndentX, -m_fLineHeight);
        }
        else
        {
          // Outdent and one line up, except for last line
          aContentStream.moveTextPositionByAmount (-fIndentX, m_fLineHeight);
        }
      }
    }
    aContentStream.endText ();
  }

  @Nonnull
  public PLElementWithSize getCopy (final float fElementWidth,
                                    @Nonnull @Nonempty final List <TextAndWidthSpec> aLines,
                                    final boolean bSplittableCopy)
  {
    ValueEnforcer.notEmpty (aLines, "Lines");

    // Create a copy to be independent!
    final List <TextAndWidthSpec> aLineCopy = ContainerHelper.newList (aLines);

    // What is our line height?
    final float fLineHeight = getLineHeight ();

    // Excluding padding/margin
    final SizeSpec aSize = new SizeSpec (fElementWidth, aLineCopy.size () * fLineHeight);

    final String sTextContent = TextAndWidthSpec.getAsText (aLineCopy);
    final PLText aNewText = bSplittableCopy ? new PLTextSplittable (sTextContent, getFontSpec ())
                                           : new PLText (sTextContent, getFontSpec ());
    aNewText.setBasicDataFrom (this).markAsPrepared (aSize).internalSetPreparedLines (aLineCopy);

    return new PLElementWithSize (aNewText, aSize);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("text", m_sText)
                            .append ("font", m_aFont)
                            .append ("lineHeight", m_fLineHeight)
                            .append ("horzAlign", m_eHorzAlign)
                            .append ("topDown", m_bTopDown)
                            .toString ();
  }
}
