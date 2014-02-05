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

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.encoding.DictionaryEncoding;
import org.apache.pdfbox.encoding.Encoding;
import org.apache.pdfbox.encoding.WinAnsiEncoding;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.mock.DebugModeTestRule;
import com.phloc.fonts.EFontResource;
import com.phloc.report.pdf.PageLayoutPDF;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.FontSpec;
import com.phloc.report.pdf.spec.PDFFont;
import com.phloc.report.pdf.spec.WidthSpec;

public class PDFFontTest
{
  @Rule
  public TestRule m_aRule = new DebugModeTestRule ();

  @Test
  public void testEuroSign () throws Exception
  {
    String s = "Test that costs 5€. Special chars: äöüÄÖÜß áàéèíìóòúù ÁÀÈÍÌÓÒÚÙ - end.";
    s += s;
    s += s;
    if (false)
      s = "€";

    if (true)
      s = FakeWinAnsiHelper.convertJavaStringToWinAnsi2 (s);

    Encoding aEncoding = WinAnsiEncoding.INSTANCE;

    if (false)
    {
      final char [] cs = s.toCharArray ();
      final StringBuilder sencoded = new StringBuilder ();
      for (final char c : cs)
      {
        String sName = aEncoding.getNameFromCharacter (c);
        if ("spacehackarabic".equals (sName))
          sName = "space";
        sencoded.appendCodePoint (aEncoding.getCode (sName));
      }
      s = sencoded.toString ();
    }

    if (false)
    {
      final COSArray aDiff = new COSArray ();
      aDiff.add (COSInteger.get (0x20ac));
      aDiff.add (COSName.getPDFName ("Euro"));
      final COSDictionary aFontDict = new COSDictionary ();
      aFontDict.setItem (COSName.BASE_ENCODING, COSName.WIN_ANSI_ENCODING);
      aFontDict.setItem (COSName.DIFFERENCES, aDiff);
      aEncoding = new DictionaryEncoding (aFontDict);
    }

    final PDDocument aDummy = new PDDocument ();
    final PDTrueTypeFont aFont = PDTrueTypeFont.loadTTF (aDummy,
                                                         EFontResource.ANAHEIM_REGULAR.getInputStream (),
                                                         aEncoding);
    aDummy.close ();

    final FontSpec r10 = new FontSpec (true ? PDFFont.REGULAR : new PDFFont (aFont), 10);

    final PLPageSet aPS1 = new PLPageSet (PDPage.PAGE_SIZE_A4).setMargin (30);
    final PLTable aTable = PLTable.createWithPercentage (50, 50);
    final PLHBox aHBox = new PLHBox ();
    aHBox.addColumn (new PLText (s, CCharset.CHARSET_UTF_16BE_OBJ, r10).setBorder (new BorderStyleSpec (Color.RED)),
                     WidthSpec.abs (180));
    aTable.addRow (aHBox);
    aPS1.addElement (aTable);

    final PageLayoutPDF aPageLayout = new PageLayoutPDF ().setDebug (true);
    aPageLayout.addPageSet (aPS1);
    aPageLayout.renderTo (new FileOutputStream ("pdf/test-font.pdf"));
  }
}
