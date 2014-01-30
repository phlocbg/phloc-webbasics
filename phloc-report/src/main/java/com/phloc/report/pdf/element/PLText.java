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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
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
  public static final boolean DEFAULT_TOP_DOWN = true;

  private final String m_sText;
  private final Charset m_aCharset;
  private final FontSpec m_aFont;
  private final float m_fLineHeight;
  private EHorzAlignment m_eHorzAlign = EHorzAlignment.DEFAULT;
  private boolean m_bTopDown = DEFAULT_TOP_DOWN;

  // prepare result
  protected List <TextAndWidthSpec> m_aPreparedLines;

  public PLText (@Nullable final String sText, @Nonnull final FontSpec aFont)
  {
    this (sText, CCharset.CHARSET_ISO_8859_1_OBJ, aFont);
  }

  public PLText (@Nullable final String sText, @Nonnull final Charset aCharset, @Nonnull final FontSpec aFont)
  {
    if (aCharset == null)
      throw new NullPointerException ("Charset");
    if (aFont == null)
      throw new NullPointerException ("Font");
    m_sText = StringHelper.getNotNull (sText);
    m_aCharset = aCharset;
    m_aFont = aFont;
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
  public EHorzAlignment getHorzAlign ()
  {
    return m_eHorzAlign;
  }

  @Nonnull
  public PLText setHorzAlign (@Nonnull final EHorzAlignment eHorzAlign)
  {
    if (eHorzAlign == null)
      throw new NullPointerException ("horzAlign");
    m_eHorzAlign = eHorzAlign;
    return this;
  }

  public boolean isTopDown ()
  {
    return m_bTopDown;
  }

  @Nonnull
  public PLText setTopDown (final boolean bTopDown)
  {
    m_bTopDown = bTopDown;
    return this;
  }

  final void internalSetPreparedLines (@Nonnull final List <TextAndWidthSpec> aLines)
  {
    m_aPreparedLines = aLines;

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

    return new SizeSpec (aCtx.getAvailableWidth (), m_aPreparedLines.size () * m_fLineHeight);
  }

  /**
   * Get the text to draw, in case it is different from the stored text (e.g.
   * for page numbers)
   * 
   * @param sText
   *        Original text
   * @param aCtx
   *        The current rendering context
   * @return The real text to draw
   */
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
