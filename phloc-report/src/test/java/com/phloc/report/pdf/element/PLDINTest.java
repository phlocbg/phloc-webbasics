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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.commons.mock.DebugModeTestRule;
import com.phloc.report.pdf.PDFCreationException;
import com.phloc.report.pdf.PageLayoutPDF;
import com.phloc.report.pdf.spec.FontSpec;
import com.phloc.report.pdf.spec.PDFFont;
import com.phloc.report.pdf.spec.WidthSpec;

public class PLDINTest
{
  @Rule
  public TestRule m_aRule = new DebugModeTestRule ();

  @Test
  public void testDINLetter () throws FileNotFoundException, PDFCreationException
  {
    final float fMMToUnits = 1 / (10 * 2.54f) * 72;
    final FontSpec r10 = new FontSpec (PDFFont.REGULAR, 10);

    final PLPageSet aPS1 = new PLPageSet (PDPage.PAGE_SIZE_A4);
    {
      final PLVBox aVBox = new PLVBox ();
      final PLHBox aHBox = new PLHBox ();
      aHBox.addColumn (new PLSpacerX (), WidthSpec.abs (20 * fMMToUnits));
      {
        final PLVBox aWindow = new PLVBox ();
        aWindow.addRow (new PLSpacerY (42 * fMMToUnits));
        aWindow.addRow (new PLText ("Hr. MaxMustermann\nMusterstraße 15\nA-1010 Wien", r10).setMinSize (90 * fMMToUnits,
                                                                                                        45 * fMMToUnits));
        aWindow.addRow (new PLSpacerY (12 * fMMToUnits));
        aHBox.addColumn (aWindow, WidthSpec.abs (90 * fMMToUnits));
      }
      aHBox.addColumn (new PLSpacerX (), WidthSpec.star ());
      aVBox.addRow (aHBox);
      aPS1.addElement (aVBox);
    }
    aPS1.addElement (new PLSpacerY (99 * fMMToUnits));
    aPS1.addElement (new PLSpacerY (98.5f * fMMToUnits));

    final PageLayoutPDF aPageLayout = new PageLayoutPDF ().setDebug (true);
    aPageLayout.addPageSet (aPS1);
    aPageLayout.renderTo (new FileOutputStream ("pdf/test-din-letter.pdf"));
  }
}
