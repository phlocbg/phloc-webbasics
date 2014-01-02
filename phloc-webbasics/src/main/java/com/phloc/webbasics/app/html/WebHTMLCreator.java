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
package com.phloc.webbasics.app.html;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mime.MimeType;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.css.ECSSVersion;
import com.phloc.css.writer.CSSWriterSettings;
import com.phloc.html.CHTMLCharset;
import com.phloc.html.hc.conversion.HCConversionSettingsProvider;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.js.builder.JSPrinter;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.app.ApplicationWebSettings;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * A utility class that handles a request, based on a
 * {@link IRequestWebScopeWithoutResponse}, a {@link UnifiedResponse} and an
 * {@link IHTMLProvider}.
 * 
 * @author Philip Helger
 */
@Immutable
public final class WebHTMLCreator
{
  static
  {
    final HCConversionSettingsProvider aCSP = ((HCConversionSettingsProvider) HCSettings.getConversionSettingsProvider ());
    aCSP.setXMLWriterSettings (new XMLWriterSettings ().setFormat (EXMLSerializeFormat.XHTML)
                                                       .setIncorrectCharacterHandling (EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING)
                                                       .setCharset (CHTMLCharset.CHARSET_HTML_OBJ));
    aCSP.setCSSWriterSettings (new CSSWriterSettings (ECSSVersion.CSS30));
    aCSP.setConsistencyChecksEnabled (GlobalDebug.isDebugMode ());
  }

  private WebHTMLCreator ()
  {}

  public static boolean isIndentAndAlign ()
  {
    return GlobalDebug.isDebugMode ();
  }

  /**
   * Get the HTML MIME type to use
   * 
   * @param aRequestScope
   *        The request scope
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static IMimeType getMimeType (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    // Add the charset to the MIME type
    return new MimeType (CMimeType.TEXT_HTML).addParameter (CMimeType.PARAMETER_NAME_CHARSET, CHTMLCharset.CHARSET_HTML);
  }

  public static void createHTMLResponse (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                         @Nonnull final UnifiedResponse aUnifiedResponse,
                                         @Nonnull final IHTMLProvider aHTMLProvider)
  {
    final boolean bIndentAndAlign = isIndentAndAlign ();

    // Build the HC conversion settings to use
    final IHCConversionSettings aCS = HCSettings.getConversionSettings (bIndentAndAlign)
                                                .getCloneIfNecessary (ApplicationWebSettings.getHTMLVersion ());

    // Setup JavaScript printer
    JSPrinter.setIndentAndAlign (bIndentAndAlign);

    // Build the main HC tree
    final HCHtml aHtml = aHTMLProvider.createHTML (aRequestScope);

    // Convert HTML to String
    final String sXMLCode = HCSettings.getAsHTMLString (aHtml, aCS);

    // Write to response
    final IMimeType aMimeType = getMimeType (aRequestScope);
    aUnifiedResponse.setMimeType (aMimeType)
                    .setContentAndCharset (sXMLCode, aCS.getXMLWriterSettings ().getCharsetObj ())
                    .disableCaching ();
  }
}
