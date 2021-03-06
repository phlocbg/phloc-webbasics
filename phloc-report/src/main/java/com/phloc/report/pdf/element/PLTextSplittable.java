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

import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.report.pdf.PLDebug;
import com.phloc.report.pdf.spec.EVertAlignment;
import com.phloc.report.pdf.spec.FontSpec;
import com.phloc.report.pdf.spec.TextAndWidthSpec;

/**
 * Render text
 * 
 * @author Philip Helger
 */
public class PLTextSplittable extends PLText implements IPLSplittableElement
{
  public PLTextSplittable (@Nullable final String sText, @Nonnull final FontSpec aFont)
  {
    super (sText, aFont);
  }

  public PLTextSplittable (@Nullable final String sText, @Nonnull final Charset aCharset, @Nonnull final FontSpec aFont)
  {
    super (sText, aCharset, aFont);
  }

  @Nullable
  public PLSplitResult splitElements (final float fElementWidth, final float fAvailableHeight)
  {
    if (fAvailableHeight <= 0)
      return null;

    final float fLineHeight = getLineHeight ();

    // Get the lines in the correct order from top to bottom
    final List <TextAndWidthSpec> aLines = isTopDown () ? m_aPreparedLines
                                                       : ContainerHelper.getReverseList (m_aPreparedLines);

    int nLines = (int) (fAvailableHeight / fLineHeight);
    if (nLines <= 0)
    {
      // Splitting makes no sense
      if (PLDebug.isDebugSplit ())
        PLDebug.debugSplit ("Failed to split " +
                            CGStringHelper.getClassLocalName (this) +
                            " because the result would be " +
                            nLines +
                            " lines for available height " +
                            fAvailableHeight);
      return null;
    }

    // Calc estimated height (required because an offset is added)
    final float fExpectedHeight = getDisplayHeightOfLines (nLines);
    if (fExpectedHeight > fAvailableHeight)
    {
      // Show one line less
      --nLines;
      if (nLines <= 0)
      {
        // Splitting makes no sense
        if (PLDebug.isDebugSplit ())
          PLDebug.debugSplit ("Failed to split " +
                              CGStringHelper.getClassLocalName (this) +
                              " because the result would be " +
                              nLines +
                              " lines for available height " +
                              fAvailableHeight +
                              " and expected height " +
                              fExpectedHeight);
        return null;
      }
    }

    // First elements does not need to be splittable anymore
    final PLElementWithSize aText1 = getCopy (fElementWidth, aLines.subList (0, nLines), false);
    // Second element may need additional splitting
    final PLElementWithSize aText2 = getCopy (fElementWidth, aLines.subList (nLines, aLines.size ()), true);

    // Important: vertical alignment is in case of splitting always "TOP"
    ((PLText) aText1.getElement ()).setVertAlign (EVertAlignment.TOP);
    ((PLText) aText2.getElement ()).setVertAlign (EVertAlignment.TOP);

    return new PLSplitResult (aText1, aText2);
  }
}
