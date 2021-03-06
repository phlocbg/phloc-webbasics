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
package com.phloc.webdemoapp.app.ajax.config;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.html.hc.IHCNode;
import com.phloc.webbasics.ajax.AbstractAjaxHandler;
import com.phloc.webbasics.ajax.AjaxDefaultResponse;
import com.phloc.webbasics.ajax.IAjaxResponse;
import com.phloc.webdemoapp.app.layout.config.RendererConfig;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public final class AjaxHandlerConfigUpdateMenuView extends AbstractAjaxHandler
{
  @Override
  @Nonnull
  protected IAjaxResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {
    // Get the rendered content of the menu area
    final IHCNode aRoot = RendererConfig.getMenuContent (ApplicationRequestManager.getInstance ()
                                                                                  .getRequestDisplayLocale ());

    // Set as result property
    return AjaxDefaultResponse.createSuccess (aRoot);
  }
}
