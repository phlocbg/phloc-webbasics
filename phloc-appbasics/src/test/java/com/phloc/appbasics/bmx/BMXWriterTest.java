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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.collections.trove.TroveInit;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroNode;
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
  static
  {
    TroveInit.initTrove (false);
  }

  @Test
  public void testBasic ()
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (new File ("pom.xml"));

    final File aFile1 = new File (ScopeTestRule.STORAGE_PATH, "test.bmx");
    new BMXWriter ().writeToFile (aDoc, aFile1);

    IMicroNode aNode = BMXReader.readFromFile (aFile1);
    assertNotNull (aNode);
    assertTrue (aDoc.isEqualContent (aNode));

    final File aFile2 = new File (ScopeTestRule.STORAGE_PATH, "test.nost.bmx");
    new BMXWriter (BMXSettings.createDefault ().set (EBMXSetting.NO_STRINGTABLE)).writeToFile (aDoc, aFile2);

    aNode = BMXReader.readFromFile (aFile2);
    assertNotNull (aNode);
    assertTrue (aDoc.isEqualContent (aNode));
  }

  @Test
  public void testStandardXML ()
  {
    final StopWatch aSW = new StopWatch ();
    final File aFile1 = new File (ScopeTestRule.STORAGE_PATH, "standard.bmx");
    final File aFile2 = new File (ScopeTestRule.STORAGE_PATH, "standard2.bmx");
    final int nMax = 1;

    for (int i = 0; i < nMax; ++i)
    {
      aSW.restart ();
      IMicroDocument aDoc = MicroReader.readMicroXML (new ClassPathResource ("bmx/standard.xml"));
      assertNotNull (aDoc);
      System.out.println ("Reading via SAX took " + aSW.stopAndGetMillis () + "ms");

      aSW.restart ();
      MicroWriter.writeToFile (aDoc, new File (ScopeTestRule.STORAGE_PATH, "standard.xml"));
      System.out.println ("Writing via MicroWriter took " + aSW.stopAndGetMillis () + "ms");

      aSW.restart ();
      BMXWriter aWriter = new BMXWriter (BMXSettings.createDefault ());
      aWriter.writeToFile (aDoc, aFile1);
      System.out.println ("Writing BMX took " + aSW.stopAndGetMillis () + "ms");

      aSW.restart ();
      aWriter = new BMXWriter (BMXSettings.createDefault ().set (EBMXSetting.NO_STRINGTABLE));
      aWriter.writeToFile (aDoc, aFile2);
      System.out.println ("Writing BMX without ST took " + aSW.stopAndGetMillis () + "ms");

      aDoc = null;
      System.gc ();
    }
    System.gc ();
    System.out.println ("Start reading");

    for (int i = 0; i < nMax; ++i)
    {
      aSW.restart ();
      final IMicroNode aNode = BMXReader.readFromFile (aFile1);
      assertNotNull (aNode);
      System.out.println ("Reading BMX took " + aSW.stopAndGetMillis () + "ms");
    }

    for (int i = 0; i < nMax; ++i)
    {
      aSW.restart ();
      final IMicroNode aNode = BMXReader.readFromFile (aFile2);
      assertNotNull (aNode);
      System.out.println ("Reading BMX without ST took " + aSW.stopAndGetMillis () + "ms");
    }
  }

  @Test
  public void testStandardReadXML ()
  {
    final StopWatch aSW = new StopWatch ();
    final File aFile1 = new File (ScopeTestRule.STORAGE_PATH, "standard.bmx");
    final File aFile2 = new File (ScopeTestRule.STORAGE_PATH, "standard2.bmx");
    final int nMax = 5;

    for (int i = 0; i < nMax; ++i)
    {
      aSW.restart ();
      final IMicroNode aNode = BMXReader.readFromFile (aFile2);
      assertNotNull (aNode);
      System.out.println ("Reading BMX without ST took " + aSW.stopAndGetMillis () + "ms");
    }

    for (int i = 0; i < nMax; ++i)
    {
      aSW.restart ();
      final IMicroNode aNode = BMXReader.readFromFile (aFile1);
      assertNotNull (aNode);
      System.out.println ("Reading BMX took " + aSW.stopAndGetMillis () + "ms");
    }
  }
}
