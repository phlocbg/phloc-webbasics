/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.servlet;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import jakarta.servlet.ServletException;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.servlets.AbstractUnifiedResponseServlet;

/**
 * A simple availability-check servlet that responds with a "pong" text message.
 * Usually this servlet should be called "ping".
 * 
 * @author Philip Helger
 */
public final class PingPongServlet extends AbstractUnifiedResponseServlet
{
  /** The response string to send. */
  public static final String RESPONSE_TEXT = "pong";

  /** The response charset */
  public static final Charset RESPONSE_CHARSET = CCharset.CHARSET_ISO_8859_1_OBJ;

  /** The response MIME type */
  public static final IMimeType RESPONSE_MIMETYPE = CMimeType.TEXT_PLAIN;

  private static final IStatisticsHandlerCounter s_aStatsPingPong = StatisticsManager.getCounterHandler (PingPongServlet.class);

  @Override
  protected void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException
  {
    aUnifiedResponse.setContentAndCharset (RESPONSE_TEXT, RESPONSE_CHARSET)
                    .setMimeType (RESPONSE_MIMETYPE)
                    .disableCaching ();
    s_aStatsPingPong.increment ();
  }
}
