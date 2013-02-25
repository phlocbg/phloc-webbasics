package com.phloc.webbasics.sitemap.provider;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.url.SimpleURL;
import com.phloc.webbasics.sitemap.XMLSitemapURL;
import com.phloc.webbasics.sitemap.XMLSitemapURLSet;

@IsSPIImplementation
public final class MockXMLSitemapProviderSPI implements IXMLSitemapProviderSPI
{
  @Nonnull
  public XMLSitemapURLSet createURLSet ()
  {
    final XMLSitemapURLSet ret = new XMLSitemapURLSet ();
    for (int i = 0; i < 10; ++i)
      ret.addURL (new XMLSitemapURL (new SimpleURL ("http://www.phloc.com?xx=" + i)));
    return ret;
  }
}
