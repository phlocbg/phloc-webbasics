package com.phloc.appbasics.bmx;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
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
}
