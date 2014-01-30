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
import com.phloc.report.pdf.spec.FontSpec;
import com.phloc.report.pdf.spec.SizeSpec;
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
    final float fLineHeight = getLineHeight ();

    // Get the lines in the correct order from top to bottom
    final List <TextAndWidthSpec> aLines = isTopDown () ? m_aPreparedLines
                                                       : ContainerHelper.getReverseList (m_aPreparedLines);

    final int nLines1 = (int) (fAvailableHeight / fLineHeight);
    if (nLines1 <= 0)
    {
      // Splitting makes no sense
      return null;
    }

    final List <TextAndWidthSpec> aLines1 = ContainerHelper.newList (aLines.subList (0, nLines1));
    final List <TextAndWidthSpec> aLines2 = ContainerHelper.newList (aLines.subList (nLines1, aLines.size ()));

    // Excluding padding/margin
    final SizeSpec aSize1 = new SizeSpec (fElementWidth, aLines1.size () * fLineHeight);
    final SizeSpec aSize2 = new SizeSpec (fElementWidth, aLines2.size () * fLineHeight);

    // Text1 does not need to be splittable anymore
    final PLText aNewText1 = new PLTextSplittable (TextAndWidthSpec.getAsText (aLines1), getFontSpec ());
    aNewText1.setBasicDataFrom (this).setHorzAlign (getHorzAlign ()).setTopDown (isTopDown ()).markAsPrepared (aSize1);
    aNewText1.internalSetPreparedLines (aLines1);

    // Text2 needs to be splittable again
    final PLTextSplittable aNewText2 = new PLTextSplittable (TextAndWidthSpec.getAsText (aLines2), getFontSpec ());
    aNewText2.setBasicDataFrom (this).setHorzAlign (getHorzAlign ()).setTopDown (isTopDown ()).markAsPrepared (aSize2);
    aNewText2.internalSetPreparedLines (aLines2);

    return new PLSplitResult (new PLElementWithSize (aNewText1, aSize1), new PLElementWithSize (aNewText2, aSize2));
  }
}
