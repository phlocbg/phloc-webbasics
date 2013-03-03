package com.phloc.appbasics.bmx;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;

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
    new BMXWriter ().getAsBytes (aDoc);
  }
}
