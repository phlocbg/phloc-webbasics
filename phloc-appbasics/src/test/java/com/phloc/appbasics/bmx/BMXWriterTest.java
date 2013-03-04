package com.phloc.appbasics.bmx;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
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
    final IMicroDocument aDoc = MicroReader.readMicroXML (new ClassPathResource ("bmx/standard.xml"));
    final byte [] ret = new BMXWriter ().getAsBytes (aDoc);
    assertNotNull (ret);
    SimpleFileIO.writeFile (new File (ScopeTestRule.STORAGE_PATH, "standard.bmx"), ret);
    System.out.println (ret.length);
  }

  @Test
  public void testSpeed ()
  {
    final File aSrcFile = ClassPathResource.getAsFile ("bmx/standard.xml");
    final File aDestFile = new File (ScopeTestRule.STORAGE_PATH, "copy");

    // Warmup
    assertTrue (SimpleFileIO.copyFile (aSrcFile, aDestFile).isSuccess ());
    FileOperations.deleteFile (aDestFile);
    assertTrue (FileOperations.copyFile (aSrcFile, aDestFile).isSuccess ());
    FileOperations.deleteFile (aDestFile);
    StopWatch aSW;

    aSW = new StopWatch (true);
    for (int i = 0; i < 10; ++i)
    {
      assertTrue (FileOperations.copyFile (aSrcFile, aDestFile).isSuccess ());
      FileOperations.deleteFile (aDestFile);
    }
    System.out.println ("FileOperations.copyFile: " + aSW.stopAndGetNanos ());

    aSW = new StopWatch (true);
    for (int i = 0; i < 10; ++i)
    {
      assertTrue (SimpleFileIO.copyFile (aSrcFile, aDestFile).isSuccess ());
      FileOperations.deleteFile (aDestFile);
    }
    System.out.println ("SimpleFileIO.copyFile:   " + aSW.stopAndGetNanos ());
  }
}
