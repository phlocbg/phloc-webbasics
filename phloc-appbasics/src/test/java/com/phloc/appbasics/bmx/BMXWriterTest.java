package com.phloc.appbasics.bmx;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.timing.StopWatch;
import com.phloc.scopes.mock.ScopeTestRule;

/**
 * Test class for class {@link BMXWriter}
 * 
 * @author philip
 */
public final class BMXWriterTest
{
  @Test
  public void testBasic ()
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (new File ("pom.xml"));
    final byte [] ret = new BMXWriter ().getAsBytes (aDoc);
    assertNotNull (ret);
    SimpleFileIO.writeFile (new File (ScopeTestRule.STORAGE_PATH, "test.bmx"), ret);
    System.out.println (ret.length);
  }

  @Test
  public void testStandardXML ()
  {
    final StopWatch aSW = new StopWatch (true);
    final IMicroDocument aDoc = MicroReader.readMicroXML (new ClassPathResource ("bmx/standard.xml"));
    System.out.println ("Reading via SAX took " + aSW.stopAndGetMillis () + "ms");

    aSW.restart ();
    MicroWriter.writeToFile (aDoc, new File (ScopeTestRule.STORAGE_PATH, "standard.xml"));
    System.out.println ("Writing via MicroWriter took " + aSW.stopAndGetMillis () + "ms");

    aSW.restart ();
    final BMXSettings aSettings = BMXSettings.createDefault ().unset (EBMXSetting.LZW_ENCODING);
    final BMXWriter aWriter = new BMXWriter (aSettings);
    aWriter.writeToFile (aDoc, new File (ScopeTestRule.STORAGE_PATH, "standard.bmx"));
    System.out.println ("Writing BMX took " + aSW.stopAndGetMillis () + "ms");
  }
}
