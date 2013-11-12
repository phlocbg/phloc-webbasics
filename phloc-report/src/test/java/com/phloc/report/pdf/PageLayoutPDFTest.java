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
public class PageLayoutPDFTest
{
  @Rule
  public TestRule m_aRule = new DebugModeTestRule ();

  @Test
  public void testBasic () throws FileNotFoundException, PDFCreationException
  {
    final FontSpec r10 = new FontSpec (PDFFont.REGULAR, 10);
    final FontSpec r12 = r10.getCloneWithDifferentFontSize (12);
    final PLPageSet aPS1 = new PLPageSet (PDPage.PAGE_SIZE_A4).setMargin (30, 50).setPadding (15);
    aPS1.setPageHeader (new PLText ("Das ist die Kopfzeile", r10).setBorder (new BorderSpec (null,
                                                                                             null,
                                                                                             null,
                                                                                             new BorderStyleSpec (Color.BLACK)))
                                                                 .setHorzAlign (EHorzAlignment.CENTER));
    aPS1.setPageFooter (new PLTextWithPlaceholders ("Das ist die Fusszeile, Seite " +
                                                    ERenderingOption.TOTAL_PAGENUM_CURRENT.getPlaceholder () +
                                                    " von " +
                                                    ERenderingOption.TOTAL_PAGENUM_TOTAL.getPlaceholder (), r10).setBorder (new BorderSpec (null,
                                                                                                                                            new BorderStyleSpec (Color.BLACK),
                                                                                                                                            null,
                                                                                                                                            null))
                                                                                                                .setHorzAlign (EHorzAlignment.CENTER));
    aPS1.addElement (new PLText ("Zeile 1", r10));
    {
      final PLHBox aHBox = new PLHBox ();
      // First column 30%
      aHBox.addColumn (new PLText ("Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text Spalte 1 mit Text ",
                                   r10).setMargin (10)
                                       .setPadding (5)
                                       .setHorzAlign (EHorzAlignment.LEFT)
                                       .setBorder (new BorderSpec (new BorderStyleSpec (Color.RED),
                                                                   new BorderStyleSpec (Color.GREEN),
                                                                   new BorderStyleSpec (Color.BLUE),
                                                                   new BorderStyleSpec (Color.CYAN))),
                       WidthSpec.perc (30));
      // Remaining columns use each the same part of the space: WidthSpec.star()
      aHBox.addColumn (new PLText ("Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text Spalte 2 mit Text ",
                                   r10.getCloneWithDifferentColor (Color.BLUE)).setBorder (new BorderSpec (new BorderStyleSpec (Color.RED)))
                                                                               .setHorzAlign (EHorzAlignment.CENTER),
                       WidthSpec.star ());
      aHBox.addColumn (new PLText ("Spalte 3 mit Text Spalte 3 mit Text Spalte 3 mit Text Ende", r10).setMargin (0,
                                                                                                                 10,
                                                                                                                 0,
                                                                                                                 0)
                                                                                                     .setPadding (5)
                                                                                                     .setBorder (new BorderSpec (new BorderStyleSpec (Color.GREEN)))
                                                                                                     .setHorzAlign (EHorzAlignment.RIGHT),
                       WidthSpec.star ());
      aHBox.addColumn (new PLText ("Spalte 4 mit Text Spalte 4 mit Text Spalte 4 mit Text Ende",
                                   r10.getCloneWithDifferentFont (PDFFont.REGULAR_ITALIC)).setBorder (new BorderSpec (new BorderStyleSpec (Color.RED)))
                                                                                          .setHorzAlign (EHorzAlignment.LEFT),
                       WidthSpec.star ());
      aPS1.addElement (aHBox);
    }
    {
      final PLHBox h = new PLHBox ().setColumnBorder (new BorderStyleSpec (Color.RED))
                                    .setColumnFillColor (new Color (0xbbbbbb));
      h.addColumn (new PLText ("Column 1", r10.getCloneWithDifferentFontSize (24)).setHorzAlign (EHorzAlignment.CENTER),
                   WidthSpec.star ());
      final PLVBox v = new PLVBox ().setRowBorder (new BorderStyleSpec (Color.GREEN));
      v.addRow (new PLText ("Column 2, Row 1", r10));
      v.addRow (new PLText ("Column 2, Row 2", r12.getCloneWithDifferentColor (Color.RED)).setFillColor (new Color (0xdddddd)));
      v.addRow (new PLText ("Column 2, Row 3", r12));
      h.addColumn (v, WidthSpec.star ());
      final PLVBox v2 = new PLVBox ();
      v2.addRow (new PLText ("Column 3, Row 1", r10));
      v2.addRow (new PLText ("Column 3, Row 2", r10));
      v2.addRow (new PLText ("Column 3, Row 3", r10).setFillColor (new Color (0xdddddd)));
      h.addColumn (v2, WidthSpec.star ());
      aPS1.addElement (h);
    }
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r10).setHorzAlign (EHorzAlignment.CENTER)
                                     .setBorder (new BorderSpec (new BorderStyleSpec (Color.BLACK))));
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r12).setHorzAlign (EHorzAlignment.RIGHT)
                                     .setFillColor (new Color (0xff0000))
                                     .setPadding (5, 0));
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 new FontSpec (PDFFont.MONOSPACE, 14)));
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r12).setHorzAlign (EHorzAlignment.CENTER));
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r10).setHorzAlign (EHorzAlignment.RIGHT));
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r12));
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r10).setHorzAlign (EHorzAlignment.CENTER));
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r12).setHorzAlign (EHorzAlignment.RIGHT));
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r10));
    aPS1.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r12).setHorzAlign (EHorzAlignment.CENTER));

    final PLPageSet aPS2 = new PLPageSet (PDPage.PAGE_SIZE_A4.getWidth (), PDPage.PAGE_SIZE_A4.getWidth ()).setMargin (30,
                                                                                                                       50)
                                                                                                           .setPadding (15);
    aPS2.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r10).setHorzAlign (EHorzAlignment.RIGHT));
    aPS2.addElement (new PLText ("Zeile 2\nZeile 3\nTäst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort Täst Täst Täst Täst Täst Tästlangeswort ",
                                 r12));

    final PLPageSet aPS3 = new PLPageSet (PDPage.PAGE_SIZE_A4.getHeight (), PDPage.PAGE_SIZE_A4.getWidth ()).setMargin (30,
                                                                                                                        50)
                                                                                                            .setPadding (15);
    aPS3.setPageHeader (new PLText ("Das ist die Kopfzeile3", r10).setBorder (new BorderSpec (null,
                                                                                              null,
                                                                                              null,
                                                                                              new BorderStyleSpec (Color.BLACK)))
                                                                  .setHorzAlign (EHorzAlignment.CENTER));
    aPS3.setPageFooter (new PLTextWithPlaceholders ("Das ist die Fusszeile3, Seite " +
                                                    ERenderingOption.PAGESET_PAGENUM_CURRENT.getPlaceholder () +
                                                    " von " +
                                                    ERenderingOption.PAGESET_PAGENUM_TOTAL.getPlaceholder () +
                                                    " bzw. " +
                                                    ERenderingOption.TOTAL_PAGENUM_CURRENT.getPlaceholder () +
                                                    " von " +
                                                    ERenderingOption.TOTAL_PAGENUM_TOTAL.getPlaceholder (), r10).setBorder (new BorderSpec (null,
                                                                                                                                            new BorderStyleSpec (Color.BLACK),
                                                                                                                                            null,
                                                                                                                                            null))
                                                                                                                .setHorzAlign (EHorzAlignment.CENTER));
    aPS3.addElement (new PLText ("Zeile 1\n\nZeile 3", r10));

    final PageLayoutPDF aPageLayout = new PageLayoutPDF ().setDebug (false);
    aPageLayout.addPageSet (aPS1);
    aPageLayout.addPageSet (aPS2);
    aPageLayout.addPageSet (aPS3);
    aPageLayout.renderTo (new FileOutputStream ("pdf/test1.pdf"));
  }
}
