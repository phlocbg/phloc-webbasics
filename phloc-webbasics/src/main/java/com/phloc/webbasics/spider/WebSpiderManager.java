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
package com.phloc.webbasics.spider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.reader.XMLListHandler;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.utils.MicroUtils;

/**
 * Provides a list of known web spiders.
 * 
 * @author philip
 */
public final class WebSpiderManager
{
  private static final class SingletonHolder
  {
    static final WebSpiderManager s_aInstance = new WebSpiderManager ();
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (WebSpiderManager.class);
  private final Map <String, WebSpiderInfo> m_aMap = new HashMap <String, WebSpiderInfo> ();

  private WebSpiderManager ()
  {
    _readSpiderList ("codelists/spiderlist.xml");
    _readSearchSpiders ("codelists/spiders_vbulletin.xml");
    _readListPhloc ("codelists/spiderlist-phloc.xml");
  }

  @Nonnull
  private static String _getUnifiedID (@Nonnull final String sID)
  {
    return sID.toLowerCase (Locale.US);
  }

  private void _readSpiderList (final String sPath)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (new ClassPathResource (sPath));
    for (final IMicroElement eSpider : aDoc.getDocumentElement ().getChildElements ("spider"))
    {
      final WebSpiderInfo aSpider = new WebSpiderInfo (eSpider.getAttribute ("ident"));
      aSpider.setName (MicroUtils.getChildTextContent (eSpider, "name"));
      final String sType = MicroUtils.getChildTextContent (eSpider, "type");
      final EWebSpiderType eType = EWebSpiderType.getFromIDOrNull (sType);
      if (sType != null && eType == null)
        s_aLogger.warn ("Unknown web spider type '" + sType + "'");
      aSpider.setType (eType);
      aSpider.setInfo (MicroUtils.getChildTextContent (eSpider, "info"));
      m_aMap.put (_getUnifiedID (aSpider.getID ()), aSpider);
    }
  }

  private void _readSearchSpiders (final String sPath)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (new ClassPathResource (sPath));
    for (final IMicroElement eSpider : aDoc.getDocumentElement ().getChildElements ("spider"))
    {
      final String sID = eSpider.getAttribute ("ident");
      WebSpiderInfo aSpider = m_aMap.get (_getUnifiedID (sID));
      if (aSpider == null)
      {
        aSpider = new WebSpiderInfo (sID);
        aSpider.setName (MicroUtils.getChildTextContent (eSpider, "name"));
        m_aMap.put (_getUnifiedID (aSpider.getID ()), aSpider);
      }
    }
  }

  private void _readListPhloc (final String sPath)
  {
    final List <String> aList = new ArrayList <String> ();
    if (XMLListHandler.readList (new ClassPathResource (sPath), aList).isFailure ())
      throw new IllegalStateException ("Failed to read spiderlist-phloc from " + sPath);
    for (final String sSpider : aList)
    {
      final String sID = _getUnifiedID (sSpider);
      if (!m_aMap.containsKey (sID))
      {
        final WebSpiderInfo aSpider = new WebSpiderInfo (sID);
        aSpider.setName (sSpider);
        m_aMap.put (sID, aSpider);
      }
    }
  }

  public static WebSpiderManager getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }

  @Nonnull
  @ReturnsImmutableObject
  public Collection <WebSpiderInfo> getAllKnownSpiders ()
  {
    return ContainerHelper.makeUnmodifiable (m_aMap.values ());
  }

  @Nullable
  public WebSpiderInfo getWebSpiderFromUserAgent (@Nonnull final String sUserAgent)
  {
    // Search case insensitive (key set is lowercase!)
    final String sUserAgentLC = _getUnifiedID (sUserAgent);
    for (final Map.Entry <String, WebSpiderInfo> aEntry : m_aMap.entrySet ())
      if (sUserAgentLC.contains (aEntry.getKey ()))
        return aEntry.getValue ();

    return null;
  }
}
