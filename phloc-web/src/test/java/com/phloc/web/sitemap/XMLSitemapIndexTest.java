/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.web.sitemap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.url.SimpleURL;
import com.phloc.commons.xml.schema.XMLSchemaValidationHelper;
import com.phloc.commons.xml.transform.StringStreamSource;
import com.phloc.web.servlet.server.StaticServerInfo;

/**
 * Test class for class {@link XMLSitemapIndex}.
 * 
 * @author Philip Helger
 */
public final class XMLSitemapIndexTest
{
  static
  {
    // First set the default web server info
    if (!StaticServerInfo.isSet ())
      StaticServerInfo.init ("http", "localhost", 80, "/any");
  }

  private static void _testWriteXMLSitemapeIndex (@Nonnull final XMLSitemapIndex x)
  {
    final File aBaseDir = new File ("xmlsitemaps-testdir");
    FileOperations.createDirIfNotExisting (aBaseDir);
    try
    {
      x.writeToDisk (aBaseDir);
      assertTrue (FileUtils.existsFile (new File (aBaseDir, CXMLSitemap.SITEMAP_ENTRY_FILENAME)));
      final int nMax = x.getURLSetCount ();
      for (int i = 0; i < nMax; ++i)
        assertTrue (FileUtils.existsFile (new File (aBaseDir, x.getSitemapFilename (i))));
      assertFalse (FileUtils.existsFile (new File (aBaseDir, x.getSitemapFilename (nMax))));
    }
    finally
    {
      FileOperations.deleteDirRecursive (aBaseDir);
    }
  }

  @Test
  public void testValid ()
  {
    final XMLSitemapIndex x = new XMLSitemapIndex ();
    assertEquals (0, x.getURLSetCount ());
    assertNotNull (x.getAsDocument ());

    final XMLSitemapURLSet s1 = new XMLSitemapURLSet ();
    s1.addURL (new XMLSitemapURL (new SimpleURL ("http://www.phloc.com")));
    x.addURLSet (s1);
    assertEquals (1, x.getURLSetCount ());
    assertNotNull (x.getAsDocument ());

    final XMLSitemapURLSet s2 = new XMLSitemapURLSet ();
    s2.addURL (new XMLSitemapURL (new SimpleURL ("http://www.google.at")));
    x.addURLSet (s2);
    assertEquals (2, x.getURLSetCount ());
    assertNotNull (x.getAsDocument ());

    // Validate index against the schema
    final IResourceErrorGroup aErrors = XMLSchemaValidationHelper.validate (new ClassPathResource (CXMLSitemap.SCHEMA_SITEINDEX_0_9),
                                                                            new StringStreamSource (x.getAsXMLString ()));
    assertTrue (aErrors.toString (), aErrors.isEmpty ());

    _testWriteXMLSitemapeIndex (x);
  }

  @Test
  public void testAddMultiFileURLSet1 ()
  {
    final XMLSitemapIndex x = new XMLSitemapIndex ();

    // Create a very large URL set
    final XMLSitemapURLSet s = new XMLSitemapURLSet ();
    for (int i = 0; i < XMLSitemapURLSet.MAX_URLS_PER_FILE + 1; ++i)
      s.addURL (new XMLSitemapURL (new SimpleURL ("http://www.phloc.com?x=" + i)));

    // And this must split up into 2 URL sets!
    x.addURLSet (s);
    assertEquals (2, x.getURLSetCount ());

    _testWriteXMLSitemapeIndex (x);
  }

  @Test
  public void testAddMultiFileURLSet2 ()
  {
    final XMLSitemapIndex x = new XMLSitemapIndex ();

    // Create a very large URL set
    final XMLSitemapURLSet s = new XMLSitemapURLSet ();
    for (int i = 0; i < XMLSitemapURLSet.MAX_URLS_PER_FILE * 2; ++i)
      s.addURL (new XMLSitemapURL (new SimpleURL ("http://www.phloc.com?x=" + i)));

    // And this must split up into 2 URL sets!
    x.addURLSet (s);
    assertEquals (2, x.getURLSetCount ());

    _testWriteXMLSitemapeIndex (x);
  }
}
