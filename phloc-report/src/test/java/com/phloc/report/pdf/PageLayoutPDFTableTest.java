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
package com.phloc.report.pdf;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.commons.mock.DebugModeTestRule;
import com.phloc.report.pdf.element.PLHBox;
import com.phloc.report.pdf.element.PLPageBreak;
import com.phloc.report.pdf.element.PLPageSet;
import com.phloc.report.pdf.element.PLTable;
import com.phloc.report.pdf.element.PLText;
import com.phloc.report.pdf.spec.BorderSpec;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.EHorzAlignment;
import com.phloc.report.pdf.spec.FontSpec;
import com.phloc.report.pdf.spec.PDFFont;
import com.phloc.report.pdf.spec.PaddingSpec;

/**
 * Test class for class {@link PageLayoutPDF}.
 * 
 * @author Philip Helger
 */
public class PageLayoutPDFTableTest
{
  @Rule
  public TestRule m_aRule = new DebugModeTestRule ();

  @Test
  public void testBasic () throws FileNotFoundException, PDFCreationException
  {
    final FontSpec r10 = new FontSpec (PDFFont.REGULAR, 10);
    final FontSpec r14b = new FontSpec (PDFFont.REGULAR_BOLD, 14);
    final PaddingSpec aPadding = new PaddingSpec (2);

    final PLPageSet aPS1 = new PLPageSet (PDPage.PAGE_SIZE_A4).setMargin (30)
                                                              .setPadding (10, 0, 20, 0)
                                                              .setFillColor (new Color (0xddffff));
    aPS1.setPageHeader (new PLText ("Headline", r10).setBorder (new BorderSpec (new BorderStyleSpec (Color.BLACK)))
                                                    .setPadding (0, 4)
                                                    .setHorzAlign (EHorzAlignment.CENTER));
    aPS1.addElement (new PLText ("Erste Dummy Zeile", r10));

    // Start table
    final PLTable aTable = true ? PLTable.createWithEvenlySizedColumns (4) : PLTable.createWithPercentage (10,
                                                                                                           40,
                                                                                                           25,
                                                                                                           25);
    aTable.setHeaderRowCount (1);

    // Add row
    PLHBox aRow = aTable.addTableRow (new PLText ("ID", r14b).setPadding (aPadding),
                                      new PLText ("Name", r14b).setPadding (aPadding),
                                      new PLText ("Sum1", r14b).setPadding (aPadding)
                                                               .setHorzAlign (EHorzAlignment.CENTER),
                                      new PLText ("Sum2", r14b).setPadding (aPadding)
                                                               .setHorzAlign (EHorzAlignment.RIGHT));
    aRow.setColumnBorder (new BorderStyleSpec (Color.GRAY)).setFillColor (Color.WHITE);

    // Add content lines
    for (int i = 0; i < 185; ++i)
    {
      // Width is determined by the width passed to the table creating method
      aRow = aTable.addTableRow (new PLText (Integer.toString (i), r10).setPadding (aPadding),
                                 new PLText ("Name " + i, r10.getCloneWithDifferentColor (i % 3 == 0 ? Color.RED
                                                                                                    : Color.BLACK)).setPadding (aPadding),
                                 new PLText (Integer.toString (i * i), r10).setPadding (aPadding)
                                                                           .setHorzAlign (EHorzAlignment.CENTER),
                                 new PLText (Integer.toString (i + i), r10).setPadding (aPadding)
                                                                           .setHorzAlign (EHorzAlignment.RIGHT));
      if ((i % 4) == 0)
        aRow.setColumnBorder (new BorderStyleSpec (Color.GREEN));
    }
    aPS1.addElement (aTable);

    // Start a new page
    aPS1.addElement (new PLPageBreak (false));
    aPS1.addElement (new PLText ("First line on new page", r10));
    // Next page
    aPS1.addElement (new PLPageBreak (false));
    // empty page by using forced page break
    aPS1.addElement (new PLPageBreak (true));
    aPS1.addElement (new PLText ("First line on last page", r10));

    final PageLayoutPDF aPageLayout = new PageLayoutPDF ().setDebug (false);
    aPageLayout.addPageSet (aPS1);
    aPageLayout.renderTo (new FileOutputStream ("pdf/test-table.pdf"));
  }
}
