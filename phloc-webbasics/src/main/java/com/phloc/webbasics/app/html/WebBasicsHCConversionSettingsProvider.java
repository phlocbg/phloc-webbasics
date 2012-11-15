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

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.EXMLSerializeIndent;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.conversion.HCConversionSettingsProvider;

/**
 * pdaf3 implementation of
 * {@link com.phloc.html.hc.conversion.IHCConversionSettingsProvider} using the
 * default {@link EHTMLVersion}
 * 
 * @author philip
 */
public final class WebBasicsHCConversionSettingsProvider extends HCConversionSettingsProvider
{
  // For internal debugging only!
  private static final boolean BEAUTIFUL_HTML = false;

  public WebBasicsHCConversionSettingsProvider ()
  {
    super (EHTMLVersion.XHTML11);
    setXMLWriterSettings (new XMLWriterSettings ().setFormat (EXMLSerializeFormat.XHTML)
                                                  .setIndent (BEAUTIFUL_HTML ? EXMLSerializeIndent.INDENT_AND_ALIGN
                                                                            : EXMLSerializeIndent.NONE)
                                                  .setIncorrectCharacterHandling (EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING));
    setConsistencyChecksEnabled (GlobalDebug.isDebugMode ());
  }
}
