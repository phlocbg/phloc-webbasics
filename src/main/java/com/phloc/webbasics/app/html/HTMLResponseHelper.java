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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.mime.CMimeType;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.webbasics.app.WebSettings;
import com.phloc.webbasics.servlet.HTTPResponseHelper;

/**
 * Some HTTP utility methods
 * 
 * @author philip
 */
public final class HTMLResponseHelper
{
  private HTMLResponseHelper ()
  {}

  public static void createHTMLResponse (@Nonnull final HttpServletRequest aHttpRequest,
                                         @Nonnull final HttpServletResponse aHttpResponse,
                                         @Nonnull final IHTMLProvider aHtmlProvider) throws ServletException
  {
    final HCHtml aHtml = aHtmlProvider.createHTML (WebSettings.getHTMLVersion ());
    final IMicroDocument aDoc = aHtml.getAsNode ();
    HTTPResponseHelper.createResponse (aHttpRequest, aHttpResponse, aDoc, CMimeType.TEXT_HTML);
  }
}
