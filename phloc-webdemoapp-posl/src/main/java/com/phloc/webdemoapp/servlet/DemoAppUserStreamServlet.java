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
package com.phloc.webdemoapp.servlet;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.url.URLUtils;
import com.phloc.webbasics.userdata.UserDataManager;
import com.phloc.webbasics.userdata.UserDataObject;
import com.phloc.webdemoapp.app.CDemoApp;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.servlets.AbstractStreamServlet;

public final class DemoAppUserStreamServlet extends AbstractStreamServlet
{
  public static final String SERVLET_DEFAULT_NAME = "user";
  public static final String SERVLET_DEFAULT_PATH = '/' + SERVLET_DEFAULT_NAME;

  @Override
  @Nonnull
  @Nonempty
  protected String getApplicationID ()
  {
    return CDemoApp.APP_CONFIG_ID;
  }

  @Override
  @Nonnull
  protected IReadableResource getResource (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                           @Nonnull final String sFilename)
  {
    // URL decode is required because requests contain e.g. "%20"
    final String sFilename1 = URLUtils.urlDecode (sFilename);

    return UserDataManager.getResource (new UserDataObject (sFilename1));
  }
}
