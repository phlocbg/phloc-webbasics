/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.appbasics.app.dao.xml;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.dao.IDAODataProvider;
import com.phloc.commons.CGlobal;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.vendor.VendorInfo;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.datetime.PDTFactory;

/**
 * Wraps an XML DAO data provider into an {@link IDAODataProvider}.
 * 
 * @author philip
 */
final class DefaultDAODataProviderXML implements IDAODataProvider
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultDAODataProviderXML.class);

  private IXMLDAODataProvider m_aXMLDataProvider;

  public DefaultDAODataProviderXML ()
  {}

  public void setXMLDataProvider (@Nonnull final IXMLDAODataProvider aXMLDataProvider)
  {
    if (m_aXMLDataProvider != null)
      throw new IllegalStateException ("The XML data provider is already initialized");
    if (aXMLDataProvider == null)
      throw new NullPointerException ("An empty XML data provider has been passed");
    m_aXMLDataProvider = aXMLDataProvider;
  }

  @Nullable
  public IXMLDAODataProvider getXMLDataProvider ()
  {
    return m_aXMLDataProvider;
  }

  @Nonnull
  public Charset getCharset ()
  {
    return XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ;
  }

  @Nonnull
  public EChange initForFirstTimeUsage () throws Exception
  {
    return m_aXMLDataProvider.initForFirstTimeUsage ();
  }

  @Nonnull
  public EChange readFromStream (@WillClose final InputStream aIS) throws Exception
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aIS);
    if (aDoc == null)
      s_aLogger.warn ("Failed to read XML from input stream " + aIS);
    return m_aXMLDataProvider.readXML (aDoc);
  }

  public void fillStringBuilderForSaving (@Nonnull final StringBuilder aSB) throws Exception
  {
    // convert data structures to XML
    final IMicroDocument aDoc = new MicroDocument ();
    aDoc.appendComment ("This file was automatically generated - do NOT edit");
    aDoc.appendComment ("This file is part of a " +
                        VendorInfo.getVendorName () +
                        " application - " +
                        VendorInfo.getVendorURL ());
    aDoc.appendComment ("File was last written: " +
                        DateTimeFormat.longDateTime ()
                                      .withLocale (CGlobal.DEFAULT_LOCALE)
                                      .print (PDTFactory.getCurrentDateTimeUTC ()));
    m_aXMLDataProvider.fillXMLDocument (aDoc);

    // serialize document to string
    // Use the version with the improved XML incorrect character handling
    aSB.append (MicroWriter.getNodeAsString (aDoc, XMLWriterSettings.DEFAULT_XML_SETTINGS));
  }

  public boolean isContentValidForSaving (@Nullable final String sContent)
  {
    // empty content is not allowed
    if (StringHelper.hasNoText (sContent))
      return false;

    if (GlobalDebug.isDebugMode ())
    {
      // Parse full XML to ensure content is valid
      return MicroReader.readMicroXML (sContent) != null;
    }

    // The non-debug version allows this for performance reasons!
    return true;
  }
}
