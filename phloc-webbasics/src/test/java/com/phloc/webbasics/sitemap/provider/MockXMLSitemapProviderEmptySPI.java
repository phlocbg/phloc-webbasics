package com.phloc.webbasics.sitemap.provider;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.webbasics.sitemap.XMLSitemapURLSet;

@IsSPIImplementation
public final class MockXMLSitemapProviderEmptySPI implements IXMLSitemapProviderSPI
{
  @Nonnull
  public XMLSitemapURLSet createURLSet ()
  {
    // Empty URL set
    return new XMLSitemapURLSet ();
  }
}
