/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.report.pdf.spec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class wraps PDF Fonts and offers some sanity methods.
 * 
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public class PDFFont
{
  public static final PDFFont REGULAR = new PDFFont (PDType1Font.HELVETICA);
  public static final PDFFont REGULAR_BOLD = new PDFFont (PDType1Font.HELVETICA_BOLD);
  public static final PDFFont REGULAR_ITALIC = new PDFFont (PDType1Font.HELVETICA_OBLIQUE);
  public static final PDFFont REGULAR_BOLD_ITALIC = new PDFFont (PDType1Font.HELVETICA_BOLD_OBLIQUE);
  public static final PDFFont MONOSPACE = new PDFFont (PDType1Font.COURIER);
  public static final PDFFont MONOSPACE_BOLD = new PDFFont (PDType1Font.COURIER_BOLD);
  public static final PDFFont MONOSPACE_ITALIC = new PDFFont (PDType1Font.COURIER_OBLIQUE);
  public static final PDFFont MONOSPACE_BOLD_ITALIC = new PDFFont (PDType1Font.COURIER_BOLD_OBLIQUE);
  public static final PDFFont TIMES = new PDFFont (PDType1Font.TIMES_ROMAN);
  public static final PDFFont TIMES_BOLD = new PDFFont (PDType1Font.TIMES_BOLD);
  public static final PDFFont TIMES_ITALIC = new PDFFont (PDType1Font.TIMES_ITALIC);
  public static final PDFFont TIMES_BOLD_ITALIC = new PDFFont (PDType1Font.TIMES_BOLD_ITALIC);
  public static final PDFFont SYMBOL = new PDFFont (PDType1Font.SYMBOL);
  public static final PDFFont ZAPF_DINGBATS = new PDFFont (PDType1Font.ZAPF_DINGBATS);

  private static final Logger s_aLogger = LoggerFactory.getLogger (PDFFont.class);

  private final PDFont m_aFont;
  // Helper
  private final float m_fBBHeight;

  protected PDFFont (@Nonnull final PDFont aFont)
  {
    if (aFont == null)
      throw new NullPointerException ("font");
    m_aFont = aFont;
    m_fBBHeight = aFont.getFontDescriptor ().getFontBoundingBox ().getHeight ();
  }

  /**
   * @return The underyling font. Never <code>null</code>.
   */
  @Nonnull
  public PDFont getFont ()
  {
    return m_aFont;
  }

  @Nonnegative
  public float getTextHeight (@Nonnegative final float fFontSize)
  {
    return m_fBBHeight * fFontSize / 1000f;
  }

  @Nonnegative
  public float getLineHeight (@Nonnegative final float fFontSize)
  {
    // By default add 5% from text height line
    return getTextHeight (fFontSize) * 1.05f;
  }

  @Nonnegative
  public float getStringWidth (@Nonnull final String sText, @Nonnegative final float fFontSize) throws IOException
  {
    // Should be quicker than m_aFont.getStringWidth (sText)
    final byte [] aTextBytes = CharsetManager.getAsBytes (sText, CCharset.CHARSET_ISO_8859_1_OBJ);

    // Need to it per byte, as getFontWidth (aTextBytes, 0, aTextBytes.length)
    // does not work
    float fWidth = 0;
    for (int i = 0; i < aTextBytes.length; i++)
      fWidth += m_aFont.getFontWidth (aTextBytes, i, 1);
    return fWidth * fFontSize / 1000f;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <TextAndWidthSpec> getFitToWidth (@Nullable final String sText,
                                                @Nonnegative final float fFontSize,
                                                @Nonnegative final float fMaxWidth) throws IOException
  {
    final List <TextAndWidthSpec> ret = new ArrayList <TextAndWidthSpec> ();

    // First split by the contained line breaks
    final String [] aLines = StringHelper.getExplodedArray ('\n', sText);
    for (final String sLine : aLines)
    {
      // Now split each source line into the best matching sub-lines
      String sCurLine = sLine;
      float fCurLineWidth;
      while ((fCurLineWidth = getStringWidth (sCurLine, fFontSize)) > fMaxWidth)
      {
        // Line is too long to fit

        // Try to break line as late as possible, at a whitespace position
        boolean bFoundSpace = false;
        for (int i = sCurLine.length () - 1; i >= 0; i--)
          if (Character.isWhitespace (sCurLine.charAt (i)))
          {
            // Whitespace found
            final String sLineStart = sCurLine.substring (0, i);
            final float fLineStartWidth = getStringWidth (sLineStart, fFontSize);
            if (fLineStartWidth <= fMaxWidth)
            {
              // We found a line - continue with the rest of the line
              ret.add (new TextAndWidthSpec (sLineStart, fLineStartWidth));

              // Automatically skip the white space and continue with the rest
              // of the line
              sCurLine = sCurLine.substring (i + 1);
              bFoundSpace = true;
              break;
            }
          }

        if (!bFoundSpace)
        {
          // No word break found - split in the middle of the word
          int nIndex = 1;
          float fPrevWordPartLength = -1;
          do
          {
            final String sWordPart = sCurLine.substring (0, nIndex);
            final float fWordPartLength = getStringWidth (sWordPart, fFontSize);
            if (fWordPartLength > fMaxWidth)
            {
              // We have an overflow - take everything except the last char
              if (nIndex == 1)
              {
                s_aLogger.warn ("A single character exceeds the maximum width of " + fMaxWidth);

                // Continue anyway
                ++nIndex;
                fPrevWordPartLength = fWordPartLength;
              }
              else
              {
                // Add everything except the last character.
                final String sWordPartToUse = sCurLine.substring (0, nIndex - 1);
                ret.add (new TextAndWidthSpec (sWordPartToUse, fPrevWordPartLength));

                // Skip the current word part and continue
                sCurLine = sCurLine.substring (nIndex - 1);
                break;
              }
            }
            else
            {
              // No overflow yet
              ++nIndex;
              fPrevWordPartLength = fWordPartLength;
            }
          } while (nIndex < sCurLine.length ());

          // The rest of the string is added below!
          break;
        }
      }

      // Add the part of the line that fits
      ret.add (new TextAndWidthSpec (sCurLine, fCurLineWidth));
    }

    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof PDFFont))
      return false;
    final PDFFont rhs = (PDFFont) o;
    return m_aFont.equals (rhs.m_aFont);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFont).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("font", m_aFont).append ("bbHeight", m_fBBHeight).toString ();
  }
}
