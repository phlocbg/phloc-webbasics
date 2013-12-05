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
import com.phloc.report.pdf.element.PLPageSet;
import com.phloc.report.pdf.element.PLTable;
import com.phloc.report.pdf.element.PLText;
import com.phloc.report.pdf.spec.EHorzAlignment;
import com.phloc.report.pdf.spec.FontSpec;
import com.phloc.report.pdf.spec.PDFFont;

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
    final PLPageSet aPS1 = new PLPageSet (PDPage.PAGE_SIZE_A4).setMargin (30)
                                                              .setPadding (10)
                                                              .setFillColor (new Color (0xeeeeee));
    final PLTable aTable = PLTable.createWithPercentage (10, 40, 25, 25).setHeaderRowCount (1);
    aTable.addTableRow (new PLText ("ID", r10),
                        new PLText ("Name", r10),
                        new PLText ("Sum1", r10).setHorzAlign (EHorzAlignment.RIGHT),
                        new PLText ("Sum2", r10).setHorzAlign (EHorzAlignment.RIGHT));
    aTable.getRowAtIndex (0).setFillColor (Color.WHITE);
    for (int i = 0; i < 1000; ++i)
      aTable.addTableRow (new PLText (Integer.toString (i), r10),
                          new PLText ("Name " + i,
                                      r10.getCloneWithDifferentColor (i % 3 == 0 ? Color.RED : Color.BLACK)),
                          new PLText (Integer.toString (i * i), r10).setHorzAlign (EHorzAlignment.RIGHT),
                          new PLText (Integer.toString (i + i), r10).setHorzAlign (EHorzAlignment.RIGHT));
    aPS1.addElement (aTable);

    final PageLayoutPDF aPageLayout = new PageLayoutPDF ().setDebug (false);
    aPageLayout.addPageSet (aPS1);
    aPageLayout.renderTo (new FileOutputStream ("pdf/test-table.pdf"));
  }
}
