package com.phloc.report.pdf.util;

import org.apache.fontbox.ttf.CMAPEncodingEntry;
import org.apache.fontbox.ttf.CMAPTable;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.string.StringHelper;

public class AnalyzeTTF
{
  public static void main (final String [] args) throws Exception
  {
    final IReadableResource aRes = new ClassPathResource ("decomp_stream_0x8FE.ttf");
    final TTFParser parser = new TTFParser ();
    final TrueTypeFont ttf = parser.parseTTF (aRes.getInputStream ());
    final CMAPTable cmapTable = ttf.getCMAP ();
    final CMAPEncodingEntry [] cmaps = cmapTable.getCmaps ();
    CMAPEncodingEntry uniMap = null;
    for (final CMAPEncodingEntry cmap : cmaps)
    {
      if (cmap.getPlatformId () == CMAPTable.PLATFORM_WINDOWS)
      {
        final int platformEncoding = cmap.getPlatformEncodingId ();
        if (CMAPTable.ENCODING_UNICODE == platformEncoding)
        {
          uniMap = cmap;
          break;
        }
      }
    }
    if (uniMap == null)
      throw new IllegalStateException ();
    final int [] g2c = uniMap.getGlyphIdToCharacterCode ();
    System.out.println ("Glyph count: " + g2c.length + " (=" + StringHelper.getHexString (g2c.length) + ")");
    int gid = 0;
    for (final int cid : g2c)
    {
      if (cid != 0)
        System.out.println ("Glyph " +
            StringHelper.getHexStringLeadingZero (gid, 4) +
            " -> char " +
            StringHelper.getHexStringLeadingZero (cid, 4) +
            " = " +
            (char) cid);
      ++gid;
    }
  }
}
