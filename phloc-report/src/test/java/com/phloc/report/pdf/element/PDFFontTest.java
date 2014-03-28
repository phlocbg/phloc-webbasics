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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

import org.apache.fontbox.ttf.TTFSubFont;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.encoding.DictionaryEncoding;
import org.apache.pdfbox.encoding.Encoding;
import org.apache.pdfbox.encoding.StandardEncoding;
import org.apache.pdfbox.encoding.WinAnsiEncoding;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.util.ResourceLoader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.streams.StreamUtils;
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

  private static final class E extends Encoding
  {
    private static final Logger s_aLogger = LoggerFactory.getLogger (E.class);
    private static final Map <String, String> NAME_TO_CHARACTER = new HashMap <String, String> ();
    private static final Map <String, String> CHARACTER_TO_NAME = new HashMap <String, String> ();

    private static void _loadGlyphList (final String location)
    {
      BufferedReader glyphStream = null;
      try
      {
        final InputStream resource = ResourceLoader.loadResource (location);
        if (resource == null)
        {
          throw new MissingResourceException ("Glyphlist not found: " + location, Encoding.class.getName (), location);
        }
        glyphStream = new BufferedReader (new InputStreamReader (resource));
        String sLine = null;
        while ((sLine = glyphStream.readLine ()) != null)
        {
          sLine = sLine.trim ();
          // lines starting with # are comments which we can ignore.
          if (!sLine.startsWith ("#"))
          {
            final int nSemicolonIndex = sLine.indexOf (';');
            if (nSemicolonIndex >= 0)
            {
              String sUnicodeValue = null;
              try
              {
                final String sCharacterName = sLine.substring (0, nSemicolonIndex);
                sUnicodeValue = sLine.substring (nSemicolonIndex + 1);
                final StringTokenizer tokenizer = new StringTokenizer (sUnicodeValue, " ", false);
                final StringBuilder aValue = new StringBuilder ();
                while (tokenizer.hasMoreTokens ())
                {
                  final int nCharacterCode = Integer.parseInt (tokenizer.nextToken (), 16);
                  aValue.append ((char) nCharacterCode);
                }
                if (NAME_TO_CHARACTER.containsKey (sCharacterName))
                {
                  s_aLogger.warn ("duplicate value for characterName=" + sCharacterName + "," + aValue);
                }
                else
                {
                  NAME_TO_CHARACTER.put (sCharacterName, aValue.toString ());
                }
              }
              catch (final NumberFormatException nfe)
              {
                s_aLogger.error ("Ealformed unicode value " + sUnicodeValue, nfe);
              }
            }
          }
        }
      }
      catch (final IOException io)
      {
        s_aLogger.error ("Error while reading the glyph list from '" + location + "'.", io);
      }
      finally
      {
        StreamUtils.close (glyphStream);
      }
    }

    static
    {
      // Loads the official Adobe Glyph List
      _loadGlyphList ("org/apache/pdfbox/resources/glyphlist.txt");
      // Loads some additional glyph mappings
      _loadGlyphList ("org/apache/pdfbox/resources/additional_glyphlist.txt");

      // Load an external glyph list file that user can give as JVM property
      final String location = System.getProperty ("glyphlist_ext");
      if (location != null)
      {
        final File external = new File (location);
        if (external.exists ())
          _loadGlyphList (location);
      }

      NAME_TO_CHARACTER.put (NOTDEF, "");
      NAME_TO_CHARACTER.put ("fi", "fi");
      NAME_TO_CHARACTER.put ("fl", "fl");
      NAME_TO_CHARACTER.put ("ffi", "ffi");
      NAME_TO_CHARACTER.put ("ff", "ff");
      NAME_TO_CHARACTER.put ("pi", "pi");

      for (final Map.Entry <String, String> aEntry : NAME_TO_CHARACTER.entrySet ())
        CHARACTER_TO_NAME.put (aEntry.getValue (), aEntry.getKey ());
    }

    public E ()
    {
      // Map all single char entries
      for (final Map.Entry <String, String> aEntry : CHARACTER_TO_NAME.entrySet ())
      {
        final String sChars = aEntry.getKey ();
        final String sName = aEntry.getValue ();
        if (sChars.length () == 1)
        {
          final char c = sChars.charAt (0);
          boolean bEquals = false;
          try
          {
            final String sStdName = StandardEncoding.INSTANCE.getName (c);
            bEquals = sName.equals (sStdName);
          }
          catch (final IOException ex)
          {}
          if (!bEquals)
            addCharacterEncoding (c, sName);
        }
      }
    }

    public COSBase getCOSObject ()
    {
      final COSDictionary ret = new COSDictionary ();
      ret.setItem (COSName.TYPE, COSName.ENCODING);
      if (false)
        ret.setItem (COSName.BASE_ENCODING, COSName.STANDARD_ENCODING);
      final COSArray aArray = new COSArray ();
      int nLastCode = Integer.MIN_VALUE;
      for (final Map.Entry <Integer, String> aEntry : ContainerHelper.newSortedMap (codeToName).entrySet ())
      {
        final int nCode = aEntry.getKey ().intValue ();
        if (nCode != nLastCode + 1)
          aArray.add (COSInteger.get (nCode));
        aArray.add (COSName.getPDFName (aEntry.getValue ()));
        nLastCode = nCode;
      }
      ret.setItem (COSName.DIFFERENCES, aArray);

      return ret;
    }
  }

  @Test
  public void testEuroSign () throws Exception
  {
    String s = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
        + "™↓↑αΠΦ";

    if (false)
      s = FakeWinAnsiHelper.convertJavaStringToWinAnsi2 (s);

    Encoding aEncoding = false ? new E () : WinAnsiEncoding.INSTANCE;

    if (false)
    {
      for (final char c : s.toCharArray ())
        System.out.print (aEncoding.getNameFromCharacter (c) + "[" + Integer.toHexString (c) + "] ");
      System.out.println ();
    }

    if (false)
    {
      final char [] cs = s.toCharArray ();
      final StringBuilder sencoded = new StringBuilder ();
      for (final char c : cs)
      {
        final String sName = aEncoding.getNameFromCharacter (c);
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
    final PDStream fontStream = new PDStream (aDummy, EFontResource.LATO2_NORMAL.getInputStream (), false);
    fontStream.getStream ().setInt (COSName.LENGTH1, fontStream.getByteArray ().length);
    fontStream.addCompression ();
    final PDTrueTypeFont aFont = PDTrueTypeFont.loadTTF (fontStream, aEncoding);
    {
      final String sCID = "/CIDInit /ProcSet findresource begin\n"
          + "12 dict begin\n"
          + "begincmap\n"
          + "/CIDSystemInfo\n"
          + "<< /Registry (Adobe)\n"
          + "/Ordering (UCS)\n"
          + "/Supplement 0\n"
          + ">> def\n"
          + "/CMapName /Adobe-Identity-UCS def\n"
          + "/CMapType 2 def\n"
          + "1 begincodespacerange\n"
          + "<0000> <FFFF>\n"
          + "endcodespacerange\n"
          + "1 beginbfchar\n"
          + "<008C> <2122>\n"
          + "endbfchar\n"
          + "1 beginbfrange\n"
          + "<012D> <012E> [<03A6> <03B1>]\n"
          + "endbfrange\n"
          + "1 beginbfchar\n"
          + "<01B4> <03A0>\n"
          + "endbfchar\n"
          + "endcmap\n"
          + "CMapName currentdict /CMap defineresource pop\n"
          + "end\n"
          + "end";
      final COSStream aToUnicode = new COSStream (new RandomAccessBuffer ());
      final OutputStream out = aToUnicode.createUnfilteredStream ();
      out.write (sCID.getBytes (CCharset.CHARSET_ISO_8859_1_OBJ));
      out.close ();
      aToUnicode.setFilters (COSName.FLATE_DECODE);
      aFont.setToUnicode (aToUnicode);
      final TTFSubFont t;
    }
    aDummy.close ();

    final FontSpec r10 = new FontSpec (false ? PDFFont.REGULAR : new PDFFont (aFont), 10);

    final PLPageSet aPS1 = new PLPageSet (PDPage.PAGE_SIZE_A4).setMargin (30);
    final PLTable aTable = PLTable.createWithPercentage (50, 50);
    final PLHBox aHBox = new PLHBox ();
    aHBox.addColumn (new PLText (s, r10).setBorder (new BorderStyleSpec (Color.RED)), WidthSpec.abs (180));
    aTable.addRow (aHBox);
    aPS1.addElement (aTable);

    final PageLayoutPDF aPageLayout = new PageLayoutPDF ().setDebug (true);
    aPageLayout.addPageSet (aPS1);
    aPageLayout.renderTo (new FileOutputStream ("pdf/test-font.pdf"));
  }
}
