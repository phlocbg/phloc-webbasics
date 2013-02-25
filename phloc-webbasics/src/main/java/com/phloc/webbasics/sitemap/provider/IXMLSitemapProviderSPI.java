package com.phloc.webbasics.sitemap.provider;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.IsSPIInterface;
import com.phloc.webbasics.sitemap.XMLSitemapURLSet;

/**
 * SPI interface for components that offer sitemap entries.
 * 
 * @author philip
 */
@IsSPIInterface
public interface IXMLSitemapProviderSPI
{
  /**
   * Create a new URL set with all URLs relevant .
   * 
   * @return A non-<code>null</code> URL set with all elements.
   */
  @Nonnull
  XMLSitemapURLSet createURLSet ();
}
