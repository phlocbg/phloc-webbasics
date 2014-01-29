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

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.mock.DebugModeTestRule;
import com.phloc.report.pdf.PDFCreationException;
import com.phloc.report.pdf.PageLayoutPDF;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.FontSpec;
import com.phloc.report.pdf.spec.PDFFont;
import com.phloc.report.pdf.spec.WidthSpec;

public class PLImageTest
{
  @Rule
  public TestRule m_aRule = new DebugModeTestRule ();

  @Test
  public void testWithWordBreak () throws PDFCreationException, IOException
  {
    final FontSpec r10 = new FontSpec (PDFFont.REGULAR, 10);

    final PLPageSet aPS1 = new PLPageSet (PDPage.PAGE_SIZE_A4).setMargin (30);
    aPS1.addElement (new PLText ("First line", r10).setBorder (new BorderStyleSpec (Color.RED)));
    aPS1.addElement (new PLImage (ImageIO.read (ClassPathResource.getInputStream ("images/test1.jpg")), 50, 50));
    aPS1.addElement (new PLText ("Second line", r10).setBorder (new BorderStyleSpec (Color.BLUE)));
    final PLHBox aHBox = new PLHBox ();
    aHBox.addColumn (new PLText ("Col1", r10), WidthSpec.perc (10));
    aHBox.addColumn (new PLImage (new ClassPathResource ("images/test1.jpg"), 50, 50), WidthSpec.abs (50));
    aHBox.addColumn (new PLText ("Col2", r10), WidthSpec.star ());
    aHBox.addColumn (new PLImage (new ClassPathResource ("images/test1.jpg"), 50, 50), WidthSpec.abs (50));
    aHBox.addColumn (new PLText ("Col3", r10), WidthSpec.perc (10));
    aPS1.addElement (aHBox);
    aPS1.addElement (new PLText ("Third line", r10).setBorder (new BorderStyleSpec (Color.GREEN)));

    final PageLayoutPDF aPageLayout = new PageLayoutPDF ().setDebug (false);
    aPageLayout.addPageSet (aPS1);
    aPageLayout.renderTo (new FileOutputStream ("pdf/test-plimage.pdf"));
  }
}
