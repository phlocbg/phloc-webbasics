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
 * @author Philip Helger
 */
@Immutable
public class HTMLConfigManager
{
  private static final class SingletonHolder
  {
    static final HTMLConfigManager s_aInstance = new HTMLConfigManager ();
  }

  public static final String DEFAULT_BASE_PATH = "html/";
  /** Filename containing the CSS includes */
  public static final String FILENAME_CSS_XML = "css.xml";
  /** Filename containing the JS includes */
  public static final String FILENAME_JS_XML = "js.xml";
  /** Filename containing the meta elements */
  public static final String FILENAME_METATAGS_XML = "metatags.xml";

  private static final Logger s_aLogger = LoggerFactory.getLogger (HTMLConfigManager.class);

  private final String m_sBasePath;
  private final List <CSSFiles.Item> m_aAllCSSItems;
  private final List <JSFiles.Item> m_aAllJSItems;
  private final Map <String, String> m_aAllMetaTags = new LinkedHashMap <String, String> ();

  private HTMLConfigManager ()
  {
    this (DEFAULT_BASE_PATH);
  }

  public HTMLConfigManager (@Nonnull final String sBasePath)
  {
    if (sBasePath == null)
      throw new NullPointerException ("basePath");
    if (sBasePath.length () > 0 && !sBasePath.endsWith ("/"))
      throw new IllegalArgumentException ("BasePath must end with a '/'!");

    m_sBasePath = sBasePath;

    // read all CSS files
    m_aAllCSSItems = new CSSFiles (WebFileIO.getResource (sBasePath + FILENAME_CSS_XML)).getAllItems ();

    // read all JS files
    m_aAllJSItems = new JSFiles (WebFileIO.getResource (sBasePath + FILENAME_JS_XML)).getAllItems ();

    // read all static MetaTags
    final InputStream aIS = WebFileIO.getInputStream (sBasePath + FILENAME_METATAGS_XML);
    if (aIS != null)
      if (XMLMapHandler.readMap (aIS, m_aAllMetaTags).isFailure ())
        s_aLogger.error ("Failed to read " + sBasePath + FILENAME_METATAGS_XML);
  }

  /**
   * @return The default {@link HTMLConfigManager} instance.
   */
  @Nonnull
  public static final HTMLConfigManager getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }

  /**
   * @return The base path. Either empty, or ending with a "/".
   */
  @Nonnull
  public String getBasePath ()
  {
    return m_sBasePath;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSFiles.Item> getAllCSSItems ()
  {
    return ContainerHelper.newList (m_aAllCSSItems);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <JSFiles.Item> getAllJSItems ()
  {
    return ContainerHelper.newList (m_aAllJSItems);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAllMetaTags ()
  {
    return ContainerHelper.newMap (m_aAllMetaTags);
  }
}
