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
package com.phloc.webctrls.page;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.html.entities.HTMLEntityResolver;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.impl.HCDOMWrapper;
import com.phloc.webbasics.app.page.WebPageExecutionContext;

/**
 * Renders a page with HTML code that is provided from an external resource
 * (e.g. for static pages)
 * 
 * @author philip
 */
public class PageViewExternal extends AbstractWebPageExt
{
  private final IMicroElement m_aDocElem;

  public PageViewExternal (@Nonnull @Nonempty final String sID,
                           @Nonnull final String sName,
                           @Nonnull final IReadableResource aRes)
  {
    super (sID, sName);

    // Read content once
    final String sContent = StreamUtils.getAllBytesAsString (aRes, CCharset.CHARSET_UTF_8_OBJ);
    if (sContent == null)
      throw new IllegalStateException ("Failed to read " + aRes.toString ());

    // Parse content once
    final String sParsable = "<x xmlns='" +
                             HCSettings.getConversionSettings (false).getHTMLVersion ().getNamespaceURI () +
                             "'>" +
                             sContent +
                             "</x>";
    final IMicroDocument aDoc = MicroReader.readMicroXML (sParsable, HTMLEntityResolver.getInstance ());
    if (aDoc == null || aDoc.getDocumentElement () == null)
      throw new IllegalStateException ("Failed to parse code for page " + aRes.toString ());
    m_aDocElem = aDoc.getDocumentElement ();
  }

  @Override
  protected void fillContent (final WebPageExecutionContext aWPEC)
  {
    if (m_aDocElem.hasChildren ())
      for (final IMicroNode aChild : m_aDocElem.getChildren ())
        aWPEC.getNodeList ().addChild (new HCDOMWrapper (aChild.getClone ()));
  }
}
