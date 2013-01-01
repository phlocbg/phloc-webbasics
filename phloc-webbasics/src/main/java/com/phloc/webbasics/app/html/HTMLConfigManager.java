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
package com.phloc.webbasics.app.html;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.reader.XMLListHandler;
import com.phloc.commons.microdom.reader.XMLMapHandler;

/**
 * This class holds the central configuration settings.
 * <ul>
 * <li>All global page CSS files</li>
 * <li>All global page CSS files (print only)</li>
 * <li>All global page CSS files (IE specific)</li>
 * <li>All global page JavaScript files</li>
 * <li>All global meta tags</li>
 * </ul>
 * 
 * @author philip
 */
@Immutable
public final class HTMLConfigManager
{
  public static final String FILENAME_CSS_XML = "html/css.xml";
  public static final String FILENAME_CSS_PRINT_XML = "html/css_print.xml";
  public static final String FILENAME_CSS_IE_XML = "html/css_ie.xml";
  public static final String FILENAME_JS_XML = "html/js.xml";
  public static final String FILENAME_METATAGS_XML = "html/metatags.xml";
  private static final Logger s_aLogger = LoggerFactory.getLogger (HTMLConfigManager.class);
  private static final HTMLConfigManager s_aInstance = new HTMLConfigManager ();

  private final List <String> m_aAllCSSFiles = new ArrayList <String> ();
  private final List <String> m_aAllCSSPrintFiles = new ArrayList <String> ();
  private final List <String> m_aAllCSSIEFiles = new ArrayList <String> ();
  private final List <String> m_aAllJSFiles = new ArrayList <String> ();
  private final Map <String, String> m_aAllMetaTags = new LinkedHashMap <String, String> ();

  /**
   * Do not call manually! Use {@link #getInstance()} instead.
   */
  private HTMLConfigManager ()
  {
    // read all CSS files
    InputStream aIS = WebFileIO.getInputStream (FILENAME_CSS_XML);
    if (aIS != null)
      if (XMLListHandler.readList (aIS, m_aAllCSSFiles).isFailure ())
        s_aLogger.error ("Failed to read " + FILENAME_CSS_XML);

    // read all print CSS files
    aIS = WebFileIO.getInputStream (FILENAME_CSS_PRINT_XML);
    if (aIS != null)
      if (XMLListHandler.readList (aIS, m_aAllCSSPrintFiles).isFailure ())
        s_aLogger.error ("Failed to read " + FILENAME_CSS_PRINT_XML);

    // read all IE CSS files
    aIS = WebFileIO.getInputStream (FILENAME_CSS_IE_XML);
    if (aIS != null)
      if (XMLListHandler.readList (aIS, m_aAllCSSIEFiles).isFailure ())
        s_aLogger.error ("Failed to read " + FILENAME_CSS_IE_XML);

    // read all JS files
    aIS = WebFileIO.getInputStream (FILENAME_JS_XML);
    if (aIS != null)
      if (XMLListHandler.readList (aIS, m_aAllJSFiles).isFailure ())
        s_aLogger.error ("Failed to read " + FILENAME_JS_XML);

    // read all static MetaTags
    aIS = WebFileIO.getInputStream (FILENAME_METATAGS_XML);
    if (aIS != null)
      if (XMLMapHandler.readMap (aIS, m_aAllMetaTags).isFailure ())
        s_aLogger.error ("Failed to read " + FILENAME_METATAGS_XML);
  }

  @Nonnull
  public static HTMLConfigManager getInstance ()
  {
    return s_aInstance;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllCSSFiles ()
  {
    return ContainerHelper.newList (m_aAllCSSFiles);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllCSSPrintFiles ()
  {
    return ContainerHelper.newList (m_aAllCSSPrintFiles);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllCSSIEFiles ()
  {
    return ContainerHelper.newList (m_aAllCSSIEFiles);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllJSFiles ()
  {
    return ContainerHelper.newList (m_aAllJSFiles);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAllMetaTags ()
  {
    return ContainerHelper.newMap (m_aAllMetaTags);
  }
}
