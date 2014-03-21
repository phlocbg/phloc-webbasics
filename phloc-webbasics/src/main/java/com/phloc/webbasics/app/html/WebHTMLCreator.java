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
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mime.MimeType;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.conversion.HCConversionSettingsProvider;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSPrinter;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * A utility class that handles a request, based on a
 * {@link IRequestWebScopeWithoutResponse}, a {@link UnifiedResponse} and an
 * {@link IHTMLProvider}.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public final class WebHTMLCreator
{
  private static EHTMLVersion s_eHTMLVersion = EHTMLVersion.DEFAULT;

  private WebHTMLCreator ()
  {}

  /**
   * @return The HTML version to use. Never <code>null</code>.
   */
  @Nonnull
  public static EHTMLVersion getHTMLVersion ()
  {
    return s_eHTMLVersion;
  }

  /**
   * Set the default HTML version to use. This implicitly creates a new
   * {@link HCConversionSettingsProvider} that will be used in
   * {@link HCSettings}. So if you are customizing the settings ensure that this
   * is done after setting the HTML version!
   *
   * @param eHTMLVersion
   *        The HTML version. May not be <code>null</code>.
   */
  public static void setHTMLVersion (@Nonnull final EHTMLVersion eHTMLVersion)
  {
    ValueEnforcer.notNull (eHTMLVersion, "HTMLVersion");
    if (eHTMLVersion != s_eHTMLVersion)
    {
      // Update the HCSettings
      final HCConversionSettingsProvider aCSP = new HCConversionSettingsProvider (eHTMLVersion);
      if (eHTMLVersion.isAtLeastHTML5 ())
      {
        // No need to put anything in a comment
        HCScript.setDefaultMode (HCScript.EMode.PLAIN_TEXT_NO_ESCAPE);
      }
      HCSettings.setConversionSettingsProvider (aCSP);
    }
  }

  /**
   * @return <code>true</code> if the HTML output should be indented and aligned
   */
  @Deprecated
  public static boolean isIndentAndAlign ()
  {
    return HCSettings.isDefaultPrettyPrint ();
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
    return new MimeType (CMimeType.TEXT_HTML).addParameter (CMimeType.PARAMETER_NAME_CHARSET,
                                                            HCSettings.getHTMLCharset ().name ());
  }

  public static void createHTMLResponse (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                         @Nonnull final UnifiedResponse aUnifiedResponse,
                                         @Nonnull final IHTMLProvider aHTMLProvider)
  {
    // Setup JavaScript printer
    JSPrinter.setIndentAndAlign (HCSettings.isDefaultPrettyPrint ());

    // Build the main HC tree
    final HCHtml aHtml = aHTMLProvider.createHTML (aRequestScope);

    // Convert HTML to String
    final String sXMLCode = HCSettings.getAsHTMLString (aHtml);

    // Write to response
    final IMimeType aMimeType = getMimeType (aRequestScope);
    aUnifiedResponse.setMimeType (aMimeType)
                    .setContentAndCharset (sXMLCode, HCSettings.getHTMLCharset ())
                    .disableCaching ();
  }
}
