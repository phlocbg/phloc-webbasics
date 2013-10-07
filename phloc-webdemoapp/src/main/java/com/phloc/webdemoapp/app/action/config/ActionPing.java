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
package com.phloc.webdemoapp.app.action.config;

import javax.annotation.Nonnull;

import com.phloc.commons.mime.CMimeType;
import com.phloc.web.CWebCharset;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.action.AbstractActionExecutor;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public class ActionPing extends AbstractActionExecutor
{
  @Override
  public void execute (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                       @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    aUnifiedResponse.setContentAndCharset ("pong", CWebCharset.CHARSET_XML_OBJ)
                    .setMimeType (CMimeType.TEXT_PLAIN)
                    .disableCaching ();
  }
}
