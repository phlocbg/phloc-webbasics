package com.phloc.webbasics.app.html;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.reader.XMLListHandler;
import com.phloc.commons.microdom.reader.XMLMapHandler;
import com.phloc.webbasics.servlet.WebFileIO;

/**
 * This class holds the central configuration settings.
 * <ul>
 * <li>All page CSS files</li>
 * <li>All page CSS files (IE specific)</li>
 * <li>All page JavaScript files</li>
 * <li>All meta tags</li>
 * </ul>
 * 
 * @author philip
 */
public final class SimpleWebHTMLConfigManager {
  private static final SimpleWebHTMLConfigManager s_aInstance = new SimpleWebHTMLConfigManager ();
  private static final Logger s_aLogger = LoggerFactory.getLogger (SimpleWebHTMLConfigManager.class);
  private final List <String> m_aAllCSSFiles = new ArrayList <String> ();
  private final List <String> m_aAllCSSPrintFiles = new ArrayList <String> ();
  private final List <String> m_aAllCSSIEFiles = new ArrayList <String> ();
  private final List <String> m_aAllJSFiles = new ArrayList <String> ();
  private final Map <String, String> m_aAllMetaTags = new LinkedHashMap <String, String> ();

  /**
   * Do not call manually! Use {@link #getInstance()} instead.
   */
  private SimpleWebHTMLConfigManager () {
    // read all CSS files
    InputStream aIS = WebFileIO.getRegistryInputStream ("css.xml");
    if (aIS != null)
      if (XMLListHandler.readList (aIS, m_aAllCSSFiles).isFailure ())
        s_aLogger.warn ("Failed to read css.xml");

    // read all print CSS files
    aIS = WebFileIO.getRegistryInputStream ("css_print.xml");
    if (aIS != null)
      if (XMLListHandler.readList (aIS, m_aAllCSSPrintFiles).isFailure ())
        s_aLogger.warn ("Failed to read css_print.xml");

    // read all IE CSS files
    aIS = WebFileIO.getRegistryInputStream ("css_ie.xml");
    if (aIS != null)
      if (XMLListHandler.readList (aIS, m_aAllCSSIEFiles).isFailure ())
        s_aLogger.warn ("Failed to read css_ie.xml");

    // read all JS files
    aIS = WebFileIO.getRegistryInputStream ("js.xml");
    if (aIS != null)
      if (XMLListHandler.readList (aIS, m_aAllJSFiles).isFailure ())
        s_aLogger.warn ("Failed to read js.xml");

    // read all static MetaTags
    aIS = WebFileIO.getRegistryInputStream ("metatags.xml");
    if (aIS != null)
      if (XMLMapHandler.readMap (aIS, m_aAllMetaTags).isFailure ())
        s_aLogger.warn ("Failed to read metatags.xml");
  }

  @Nonnull
  public static SimpleWebHTMLConfigManager getInstance () {
    return s_aInstance;
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <String> getAllCSSFiles () {
    return ContainerHelper.makeUnmodifiable (m_aAllCSSFiles);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <String> getAllCSSPrintFiles () {
    return ContainerHelper.makeUnmodifiable (m_aAllCSSPrintFiles);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <String> getAllCSSIEFiles () {
    return ContainerHelper.makeUnmodifiable (m_aAllCSSIEFiles);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <String> getAllJSFiles () {
    return ContainerHelper.makeUnmodifiable (m_aAllJSFiles);
  }

  @Nonnull
  @ReturnsImmutableObject
  public Map <String, String> getAllMetaTags () {
    return ContainerHelper.makeUnmodifiable (m_aAllMetaTags);
  }
}
