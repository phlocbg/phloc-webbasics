package com.phloc.webbasics.sitemap.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.scopes.web.mock.AbstractWebScopeAwareTestCase;
import com.phloc.web.servlet.server.StaticServerInfo;
import com.phloc.webbasics.sitemap.CXMLSitemap;
import com.phloc.webbasics.sitemap.XMLSitemapIndex;

/**
 * Test class for class {@link XMLSitemapProvider}.
 * 
 * @author philip
 */
public final class XMLSitemapProviderTest extends AbstractWebScopeAwareTestCase
{
  static
  {
    // First set the default web server info
    if (!StaticServerInfo.isSet ())
      StaticServerInfo.init ("http", "localhost", 80, "/any");
  }

  @Test
  public void testWrite ()
  {
    for (int i = 0; i < 2; ++i)
    {
      final boolean bUseGZip = i == 0;

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
