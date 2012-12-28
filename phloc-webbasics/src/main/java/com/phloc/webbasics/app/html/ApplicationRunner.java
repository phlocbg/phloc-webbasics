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
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.html.hc.conversion.HCConversionSettingsProvider;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.js.builder.JSPrinter;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.app.ApplicationWebSettings;
import com.phloc.webbasics.web.UnifiedResponse;

@Immutable
public final class ApplicationRunner
{
  static
  {
    final HCConversionSettingsProvider aCSP = ((HCConversionSettingsProvider) HCSettings.getConversionSettingsProvider ());
    aCSP.setXMLWriterSettings (new XMLWriterSettings ().setFormat (EXMLSerializeFormat.XHTML)
                                                       .setIncorrectCharacterHandling (EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING));
    aCSP.setConsistencyChecksEnabled (GlobalDebug.isDebugMode ());
  }

  private ApplicationRunner ()
  {}

  public static boolean isIndentAndAlign ()
  {
    return true;
  }

  @Nonnull
  public static IMimeType getMimeType ()
  {
    return CMimeType.TEXT_HTML;
  }

  public static void createHTMLResponse (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                         @Nonnull final UnifiedResponse aUnifiedResponse,
                                         @Nonnull final IHTMLProvider aHTMLProvider)
  {
    final boolean bIndentAndAlign = isIndentAndAlign ();
    final IHCConversionSettings aCS = HCSettings.getConversionSettings (bIndentAndAlign)
                                                .getCloneIfNecessary (ApplicationWebSettings.getHTMLVersion ());
    JSPrinter.setIndentAndAlign (bIndentAndAlign);

    // Build the HC tree
    final HCHtml aHtml = aHTMLProvider.createHTML (aRequestScope);
    // Convert to String
    final String sXMLCode = HCSettings.getAsHTMLString (aHtml, aCS);
    // Write to response
    aUnifiedResponse.setMimeType (getMimeType ())
                    .setContentAndCharset (sXMLCode, aCS.getXMLWriterSettings ().getCharsetObj ())
                    .disableCaching ();
  }
}
