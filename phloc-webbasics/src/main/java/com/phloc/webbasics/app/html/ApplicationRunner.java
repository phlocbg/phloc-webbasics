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

import com.phloc.commons.mime.CMimeType;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.app.ApplicationWebSettings;
import com.phloc.webbasics.web.UnifiedResponse;

public final class ApplicationRunner
{
  static
  {
    HCSettings.setConversionSettingsProvider (new WebBasicsHCConversionSettingsProvider ());
  }

  private ApplicationRunner ()
  {}

  public static void createHTMLResponse (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                         @Nonnull final UnifiedResponse aUnifiedResponse,
                                         @Nonnull final IHTMLProvider aHTMLProvider)
  {
    final IHCConversionSettings aCS = HCSettings.getConversionSettings (false)
                                                .getClone (ApplicationWebSettings.getHTMLVersion ());
    // Build the HC tree
    final HCHtml aHtml = aHTMLProvider.createHTML (aRequestScope);
    // Convert to String
    final String sXMLCode = HCSettings.getAsHTMLString (aHtml, aCS);
    // Write to response
    aUnifiedResponse.setMimeType (CMimeType.TEXT_HTML)
                    .setContentAndCharset (sXMLCode, HCSettings.getHTMLCharset (false))
                    .disableCaching ();
  }
}
