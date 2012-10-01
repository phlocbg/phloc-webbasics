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
package com.phloc.webbasics.app.html;

import javax.annotation.Nonnull;

import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.IXMLWriterSettings;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.app.ApplicationWebSettings;
import com.phloc.webbasics.web.UnifiedResponse;

public final class ApplicationRunner
{
  private static final IXMLWriterSettings XML_WRITER_SETTINGS = new XMLWriterSettings ().setFormat (EXMLSerializeFormat.HTML)
                                                                                        .setIncorrectCharacterHandling (EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING);

  private ApplicationRunner ()
  {}

  public static void createHTMLResponse (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                         @Nonnull final UnifiedResponse aUnifiedResponse,
                                         @Nonnull final IHTMLProvider aHTMLProvider)
  {
    // Build the HC tree
    final HCHtml aHtml = aHTMLProvider.createHTML (aRequestScope);
    // Convert to XML tree
    final IMicroDocument aDoc = aHtml.getAsNode (HCSettings.getConversionSettings (false)
                                                           .getClone (ApplicationWebSettings.getHTMLVersion ()));
    // Convert to String
    final String sXMLCode = MicroWriter.getNodeAsString (aDoc, XML_WRITER_SETTINGS);
    // Write to response
    aUnifiedResponse.setMimeType (CMimeType.TEXT_HTML)
                    .setContentAndCharset (sXMLCode, XML_WRITER_SETTINGS.getCharsetObj ())
                    .disableCaching ();
  }
}
