/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webpages;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.xml.serialize.SAXReaderSettings;
import com.phloc.html.entities.HTMLEntityResolver;
import com.phloc.html.hc.impl.HCDOMWrapper;
import com.phloc.webbasics.app.html.WebHTMLCreator;
import com.phloc.webbasics.app.page.WebPageExecutionContext;

/**
 * Renders a page with HTML code that is provided from an external resource
 * (e.g. for static pages)
 *
 * @author Philip Helger
 */
public class PageViewExternal extends AbstractWebPageExt
{
  private final IReadableResource m_aRes;
  private boolean m_bReadEveryTime = false;
  private final IMicroElement m_aDocElem;

  @Nonnull
  private static IMicroElement _readPage (@Nonnull final IReadableResource aResource)
  {
    // Read content once
    final String sContent = StreamUtils.getAllBytesAsString (aResource, CCharset.CHARSET_UTF_8_OBJ);
    if (sContent == null)
      throw new IllegalStateException ("Failed to read " + aResource.toString ());

    // Parse content once
    final String sParsable = "<x xmlns='" +
                             WebHTMLCreator.getHTMLVersion ().getNamespaceURI () +
                             "'>" +
                             sContent +
                             "</x>";
    final IMicroDocument aDoc = MicroReader.readMicroXML (sParsable,
                                                          new SAXReaderSettings ().setEntityResolver (HTMLEntityResolver.getInstance ()));
    if (aDoc == null || aDoc.getDocumentElement () == null)
      throw new IllegalStateException ("Failed to parse code for page " + aResource.toString ());
    return aDoc.getDocumentElement ();
  }

  public PageViewExternal (@Nonnull @Nonempty final String sID,
                           @Nonnull final String sName,
                           @Nonnull final IReadableResource aResource)
  {
    super (sID, sName);
    m_aRes = ValueEnforcer.notNull (aResource, "Resource");

    // Read once anyway to check if the resource is readable
    m_aDocElem = _readPage (aResource);
  }

  public PageViewExternal (@Nonnull @Nonempty final String sID,
                           @Nonnull final IReadonlyMultiLingualText aName,
                           @Nonnull final IReadableResource aResource)
  {
    super (sID, aName);
    m_aRes = ValueEnforcer.notNull (aResource, "Resource");

    // Read once anyway to check if the resource is readable
    m_aDocElem = _readPage (aResource);
  }

  @Nonnull
  public IReadableResource getResource ()
  {
    return m_aRes;
  }

  public boolean isReadEveryTime ()
  {
    return m_bReadEveryTime;
  }

  @Nonnull
  public PageViewExternal setReadEveryTime (final boolean bReadEveryTime)
  {
    m_bReadEveryTime = bReadEveryTime;
    return this;
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final IMicroElement aElement = m_bReadEveryTime ? _readPage (m_aRes) : m_aDocElem;

    if (aElement.hasChildren ())
      for (final IMicroNode aChild : aElement.getChildren ())
        aWPEC.getNodeList ().addChild (new HCDOMWrapper (aChild));
  }
}
