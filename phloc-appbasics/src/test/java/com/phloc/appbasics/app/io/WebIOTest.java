/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.appbasics.app.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Rule;
import org.junit.Test;

import com.phloc.appbasics.mock.AppBasicTestRule;
import com.phloc.commons.io.streams.StreamUtils;

/**
 * Test class for class {@link WebIO}
 * 
 * @author philip
 */
public final class WebIOTest
{
  @Rule
  public final AppBasicTestRule m_aRule = new AppBasicTestRule ();

  @Test
  public void testBasePath () throws IOException
  {
    final String sTestFile = "testfile";
    final String sTestContent = "Das ist der Inhalt der TestDatei";
    final String sTestFile2 = "testfile2";

    // may not exist
    assertFalse (WebIO.resourceExists (sTestFile));

    try
    {
      // write file
      final OutputStream aOS = WebIO.getOutputStream (sTestFile);
      assertNotNull (aOS);
      aOS.write (sTestContent.getBytes ());
      assertTrue (StreamUtils.close (aOS).isSuccess ());

      // rename a to b
      assertTrue (WebIO.resourceExists (sTestFile));
      assertTrue (WebIO.renameFile (sTestFile, sTestFile2).isSuccess ());

      // ensure only b is present
      assertFalse (WebIO.resourceExists (sTestFile));
      assertTrue (WebIO.resourceExists (sTestFile2));

      // rename back from b to a
      assertTrue (WebIO.renameFile (sTestFile2, sTestFile).isSuccess ());

      // ensure only a is present
      assertTrue (WebIO.resourceExists (sTestFile));
      assertFalse (WebIO.resourceExists (sTestFile2));

      // read file
      final InputStream aIS = WebIO.getReadableResource (sTestFile).getInputStream ();
      assertNotNull (aIS);
      StreamUtils.close (aIS);
    }
    finally
    {
      // ensure all files are gone :)
      if (WebIO.resourceExists (sTestFile))
        assertTrue (WebIO.deleteFile (sTestFile).isSuccess ());
      if (WebIO.resourceExists (sTestFile2))
        assertTrue (WebIO.deleteFile (sTestFile2).isSuccess ());
      assertFalse (WebIO.resourceExists (sTestFile));
    }
  }
}
