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
package com.phloc.web.sitemap.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.web.servlet.server.StaticServerInfo;
import com.phloc.web.sitemap.CXMLSitemap;
import com.phloc.web.sitemap.XMLSitemapIndex;

/**
 * Test class for class {@link XMLSitemapProvider}.
 * 
 * @author Philip Helger
 */
public final class XMLSitemapProviderTest
{
  private static final boolean [] BOOLS = new boolean [] { true, false };

  static
  {
    // First set the default web server info
    if (!StaticServerInfo.isSet ())
      StaticServerInfo.init ("http", "localhost", 80, "/any");
  }

  @Test
  public void testWrite ()
  {
    for (final boolean bUseGZip : BOOLS)
    {
      // provider count
      assertEquals (4, XMLSitemapProvider.getProviderCount ());

      final File aTargetDir = new File ("xmlsitemap-provider-testdir");
      FileOperations.createDirIfNotExisting (aTargetDir);
      try
      {
        assertTrue (XMLSitemapProvider.createSitemapFiles (aTargetDir, bUseGZip).isSuccess ());
        assertTrue (FileUtils.existsFile (new File (aTargetDir, CXMLSitemap.SITEMAP_ENTRY_FILENAME)));

        // 3 URL sets are present
        final int nMax = 3;
        for (int nIndex = 0; nIndex < nMax; ++nIndex)
          assertTrue (FileUtils.existsFile (new File (aTargetDir, XMLSitemapIndex.getSitemapFilename (nIndex, bUseGZip))));
        assertFalse (FileUtils.existsFile (new File (aTargetDir, XMLSitemapIndex.getSitemapFilename (nMax, bUseGZip))));
      }
      finally
      {
        FileOperations.deleteDirRecursive (aTargetDir);
      }
    }
  }
}
