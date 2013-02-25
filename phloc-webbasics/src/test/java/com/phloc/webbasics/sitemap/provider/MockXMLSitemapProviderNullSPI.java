package com.phloc.webbasics.sitemap.provider;

import javax.annotation.Nullable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.webbasics.sitemap.XMLSitemapURLSet;

@IsSPIImplementation
public final class MockXMLSitemapProviderNullSPI implements IXMLSitemapProviderSPI
{
  @Nullable
  public XMLSitemapURLSet createURLSet ()
  {
    return null;
  }
}
