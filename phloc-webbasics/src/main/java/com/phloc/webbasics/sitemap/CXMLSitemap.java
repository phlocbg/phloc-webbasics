package com.phloc.webbasics.sitemap;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.serialize.EXMLSerializeIndent;
import com.phloc.commons.xml.serialize.IXMLWriterSettings;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * Constants for handling sitemaps.org sitemaps consistently.
 * 
 * @author philip
 */
@Immutable
public final class CXMLSitemap
{
  /** The namespace URI for XML sitemap 0.9 files - both URL set and index! */
  public static final String XML_NAMESPACE_0_9 = "http://www.sitemaps.org/schemas/sitemap/0.9";

  /** Classpath relative path to the sitemap XSD file 0.9 */
  public static final String SCHEMA_SITEMAP_0_9 = "schemas/sitemap-0.9.xsd";

  /** Classpath relative path to the siteindex XSD file 0.9 */
  public static final String SCHEMA_SITEINDEX_0_9 = "schemas/siteindex-0.9.xsd";

  /** The file name for the sitemap entry file */
  public static final String SITEMAP_ENTRY_FILENAME = "sitemap.xml";

  /** The XML writer settings to be used */
  public static final IXMLWriterSettings XML_WRITER_SETTINGS = new XMLWriterSettings ().setIndent (EXMLSerializeIndent.NONE)
                                                                                       .setIncorrectCharacterHandling (EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING);

  private CXMLSitemap ()
  {}
}
