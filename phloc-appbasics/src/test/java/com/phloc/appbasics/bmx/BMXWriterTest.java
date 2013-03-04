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
package com.phloc.appbasics.bmx;

import java.io.File;

import org.junit.Test;

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

    final File aFile1 = new File (ScopeTestRule.STORAGE_PATH, "test.bmx");
    new BMXWriter ().writeToFile (aDoc, aFile1);

    final File aFile2 = new File (ScopeTestRule.STORAGE_PATH, "test.deflate.bmx");
    new BMXWriter (BMXSettings.createDefault ().set (EBMXSetting.DEFLATE)).writeToFile (aDoc, aFile2);

    BMXReader.readFromFile (aFile1);
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
    final File aFile = new File (ScopeTestRule.STORAGE_PATH, "standard.bmx");
    final BMXWriter aWriter = new BMXWriter (BMXSettings.createDefault ());
    aWriter.writeToFile (aDoc, aFile);
    System.out.println ("Writing BMX took " + aSW.stopAndGetMillis () + "ms");

    aSW.restart ();
    BMXReader.readFromFile (aFile);
    System.out.println ("Reading BMX took " + aSW.stopAndGetMillis () + "ms");
  }
}
