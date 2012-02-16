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

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.IXMLWriterSettings;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.webbasics.app.WebSettings;

public final class HTMLResponseHelper
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (HTMLResponseHelper.class);

  // Avoid creating per request :)
  private static final IXMLWriterSettings XML_WRITER_SETTINGS = new XMLWriterSettings ().setFormat (EXMLSerializeFormat.HTML)
                                                                                        .setIncorrectCharacterHandling (EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING);

  private HTMLResponseHelper ()
  {}

  @Nonnull
  public static OutputStream getBestSuitableOutputStream (@Nonnull final HttpServletRequest aHttpRequest,
                                                          @Nonnull final HttpServletResponse aHttpResponse) throws IOException
  {
    final String sAcceptEncoding = aHttpRequest.getHeader ("Accept-Encoding");
    if (sAcceptEncoding.contains ("gzip"))
    {
      aHttpResponse.setHeader ("Content-Encoding", "gzip");
      return new GZIPOutputStream (aHttpResponse.getOutputStream ());
    }
    if (sAcceptEncoding.contains ("deflate"))
    {
      aHttpResponse.setHeader ("Content-Encoding", "deflate");
      final ZipOutputStream aOS = new ZipOutputStream (aHttpResponse.getOutputStream ());
      aOS.putNextEntry (new ZipEntry ("dummy name"));
      return aOS;
    }
    return aHttpResponse.getOutputStream ();
  }

  public static void createHTMLResponse (@Nonnull final HttpServletRequest aHttpRequest,
                                         @Nonnull final HttpServletResponse aHttpResponse,
                                         @Nonnull final IHTMLProvider aHtmlProvider) throws ServletException
  {
    final HCHtml aHtml = aHtmlProvider.createHTML (WebSettings.getHTMLVersion ());
    final IMicroDocument aDoc = aHtml.getAsNode ();
    createResponse (aHttpRequest, aHttpResponse, aDoc, CMimeType.TEXT_HTML);
  }

  public static void createResponse (@Nonnull final HttpServletRequest aHttpRequest,
                                     @Nonnull final HttpServletResponse aHttpResponse,
                                     @Nonnull final IMicroDocument aDoc,
                                     @Nonnull final IMimeType aMimeType) throws ServletException
  {
    try
    {
      final String sXMLCode = MicroWriter.getNodeAsString (aDoc, XML_WRITER_SETTINGS);
      final String sCharset = XML_WRITER_SETTINGS.getCharset ();

      aHttpResponse.setContentType (aMimeType.getAsStringWithEncoding (sCharset));
      aHttpResponse.setCharacterEncoding (sCharset);

      // Faster than using a PrintWriter!
      final OutputStream aOS = getBestSuitableOutputStream (aHttpRequest, aHttpResponse);
      aOS.write (sXMLCode.getBytes (sCharset));
      aOS.flush ();
      aOS.close ();
    }
    catch (final Throwable t)
    {
      // Do not show the exceptions that occur, when client cancels a request.
      if (!StreamUtils.isKnownEOFException (t))
      {
        s_aLogger.error ("Error running application", t);
        // Catch Exception and re-throw
        if (t instanceof ServletException)
          throw (ServletException) t;
        throw new ServletException (t);
      }
    }
  }
}
