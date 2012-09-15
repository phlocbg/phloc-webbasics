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
package com.phloc.webbasics.servlet;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.web.UnifiedResponse;

/**
 * A simple availability-check servlet that responds with a "pong" text message.
 * Usually this servlet should be called "ping".
 * 
 * @author philip
 */
public final class PingPongServlet extends AbstractUnifiedResponseServlet
{
  private static final IStatisticsHandlerCounter s_aStatsPingPong = StatisticsManager.getCounterHandler (PingPongServlet.class);

  /** The response string to send. */
  private static final String RESPONSE_TEXT = "pong";

  @Override
  protected void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException
  {
    aUnifiedResponse.setContentAndCharset (RESPONSE_TEXT, CCharset.CHARSET_ISO_8859_1_OBJ)
                    .setMimeType (CMimeType.TEXT_PLAIN)
                    .disableCaching ();
    s_aStatsPingPong.increment ();
  }
}
