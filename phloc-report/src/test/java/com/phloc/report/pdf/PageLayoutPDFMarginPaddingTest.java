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
package com.phloc.report.pdf;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.commons.mock.DebugModeTestRule;
import com.phloc.report.pdf.PDFCreationException;
import com.phloc.report.pdf.PageLayoutPDF;
import com.phloc.report.pdf.element.PLHBox;
import com.phloc.report.pdf.element.PLPageSet;
import com.phloc.report.pdf.element.PLText;
import com.phloc.report.pdf.element.PLTextWithPlaceholders;
import com.phloc.report.pdf.element.PLVBox;
import com.phloc.report.pdf.render.ERenderingOption;
import com.phloc.report.pdf.spec.BorderSpec;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.EHorzAlignment;
import com.phloc.report.pdf.spec.FontSpec;
import com.phloc.report.pdf.spec.PDFFont;
import com.phloc.report.pdf.spec.WidthSpec;

/**
 * Test class for class {@link PageLayoutPDF}.
 * 
 * @author Philip Helger
 */
public class PageLayoutPDFMarginPaddingTest
{
  @Rule
  public TestRule m_aRule = new DebugModeTestRule ();

  @Test
  public void testBasic () throws FileNotFoundException, PDFCreationException
  {
    final String sLID = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
    final String sLIDShort = sLID.substring (0, sLID.length () / 3);

    final FontSpec r10 = new FontSpec (PDFFont.REGULAR, 10);
    final PLPageSet aPS1 = new PLPageSet (PDPage.PAGE_SIZE_A4).setMargin (10, 20, 30, 40)
                                                              .setPadding (10, 20, 30, 40)
                                                              .setFillColor (new Color (0xeeeeee));
    aPS1.setPageHeader (new PLText ("Headline", r10).setBorder (new BorderSpec (new BorderStyleSpec (Color.BLACK)))
                                                    .setPadding (0, 4)
                                                    .setHorzAlign (EHorzAlignment.CENTER));
    aPS1.setPageFooter (new PLTextWithPlaceholders ("Page " +
                                                    ERenderingOption.TOTAL_PAGENUM_CURRENT.getPlaceholder () +
                                                    " of " +
                                                    ERenderingOption.TOTAL_PAGENUM_TOTAL.getPlaceholder (), r10).setBorder (new BorderSpec (new BorderStyleSpec (Color.BLACK)))
                                                                                                                .setMargin (0,
                                                                                                                            10,
                                                                                                                            0,
                                                                                                                            0)
                                                                                                                .setPadding (10,
                                                                                                                             4)
                                                                                                                .setHorzAlign (EHorzAlignment.RIGHT));
    {
      final PLHBox h = new PLHBox ().setMargin (0, -20, -30, 10)
                                    .setPadding (5)
                                    .setColumnFillColor (new Color (0xbbbbbb));
      if (false)
        h.setBorder (new BorderStyleSpec (Color.RED)).setColumnBorder (new BorderStyleSpec (Color.GREEN));

      h.addColumn (new PLText (sLID, r10).setHorzAlign (EHorzAlignment.CENTER).setPadding (20, 0), WidthSpec.star ());

      final PLVBox v1 = new PLVBox ().setPadding (5).setFillColor (new Color (0xabcdef));
      if (false)
        v1.setBorder (new BorderStyleSpec (Color.RED)).setRowBorder (new BorderStyleSpec (Color.GREEN));
      v1.addRow (new PLText (sLIDShort, r10).setMargin (0).setPadding (10).setBorder (new BorderStyleSpec (Color.BLUE)));
      v1.addRow (new PLText (sLIDShort, r10).setMargin (5).setPadding (5).setBorder (new BorderStyleSpec (Color.BLUE)));
      v1.addRow (new PLText (sLIDShort, r10).setMargin (10).setPadding (0).setBorder (new BorderStyleSpec (Color.BLUE)));
      h.addColumn (v1, WidthSpec.star ());

      final PLVBox v2 = new PLVBox ();
      v2.addRow (new PLText (sLIDShort, r10).setHorzAlign (EHorzAlignment.RIGHT));
      v2.addRow (new PLText (sLIDShort, r10).setHorzAlign (EHorzAlignment.RIGHT).setFillColor (new Color (0xdddddd)));
      v2.addRow (new PLText (sLIDShort, r10).setHorzAlign (EHorzAlignment.RIGHT));
      h.addColumn (v2, WidthSpec.star ());
      aPS1.addElement (h);
    }
    if (true)
    {
      aPS1.addElement (new PLText (sLID, r10.getCloneWithDifferentColor (Color.WHITE)).setHorzAlign (EHorzAlignment.RIGHT)
                                                                                      .setBorder (new BorderSpec (new BorderStyleSpec (Color.BLACK)))
                                                                                      .setFillColor (Color.RED)
                                                                                      .setMargin (0)
                                                                                      .setPadding (10));
      aPS1.addElement (new PLText (sLID, r10.getCloneWithDifferentColor (Color.RED)).setHorzAlign (EHorzAlignment.RIGHT)
                                                                                    .setBorder (new BorderSpec (new BorderStyleSpec (Color.BLACK)))
                                                                                    .setFillColor (Color.GREEN)
                                                                                    .setMargin (5)
                                                                                    .setPadding (5));
      aPS1.addElement (new PLText (sLID, r10.getCloneWithDifferentColor (Color.WHITE)).setHorzAlign (EHorzAlignment.RIGHT)
                                                                                      .setBorder (new BorderSpec (new BorderStyleSpec (Color.BLACK)))
                                                                                      .setFillColor (Color.BLUE)
                                                                                      .setMargin (10)
                                                                                      .setPadding (0));
      aPS1.addElement (new PLText (sLID, r10).setFillColor (new Color (0xabcdef)));
    }

    final PageLayoutPDF aPageLayout = new PageLayoutPDF ().setDebug (false);
    aPageLayout.addPageSet (aPS1);
    aPageLayout.renderTo (new FileOutputStream ("pdf/test2.pdf"));
  }
}
