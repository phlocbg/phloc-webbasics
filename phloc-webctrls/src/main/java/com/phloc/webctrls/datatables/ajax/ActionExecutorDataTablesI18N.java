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
package com.phloc.webctrls.datatables.ajax;

import java.nio.charset.Charset;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.CGlobal;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.MimeType;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSPrinter;
import com.phloc.web.servlet.response.ResponseHelperSettings;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.action.AbstractActionExecutor;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Action executor for providing the DataTables translated texts
 * 
 * @author Philip Helger
 */
public class ActionExecutorDataTablesI18N extends AbstractActionExecutor
{
  // This parameter must contain the locale - otherwise English is returned
  public static final String LANGUAGE_ID = "language";

  private final Locale m_aDefaultLocale;

  public ActionExecutorDataTablesI18N ()
  {
    this (CGlobal.DEFAULT_LOCALE);
  }

  public ActionExecutorDataTablesI18N (@Nonnull final Locale aDefaultLocale)
  {
    if (aDefaultLocale == null)
      throw new NullPointerException ("defaultLocale");
    if (StringHelper.hasNoText (aDefaultLocale.getLanguage ()))
      throw new IllegalArgumentException ("defaultLocale muts have a language: " + aDefaultLocale);
    m_aDefaultLocale = aDefaultLocale;
  }

  public void execute (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                       @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    // Resolve language
    final String sLanguage = aRequestScope.getAttributeAsString (LANGUAGE_ID);
    Locale aLanguage = LocaleCache.getLocale (sLanguage);
    if (aLanguage == null)
      aLanguage = m_aDefaultLocale;

    final JSAssocArray aData = DataTables.createLanguageJson (aLanguage);

    final Charset aCharset = XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ;
    aUnifiedResponse.setContentAndCharset (JSPrinter.getAsString (aData), aCharset)
                    .setMimeType (new MimeType (CMimeType.APPLICATION_JSON).addParameter (CMimeType.PARAMETER_NAME_CHARSET,
                                                                                          aCharset.name ()))
                    .enableCaching (ResponseHelperSettings.getExpirationSeconds ());
  }
}
