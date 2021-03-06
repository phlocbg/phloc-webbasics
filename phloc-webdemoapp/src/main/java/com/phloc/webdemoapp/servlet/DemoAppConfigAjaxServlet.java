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
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.SimpleURL;
import com.phloc.webbasics.ajax.servlet.AbstractApplicationAjaxServlet;
import com.phloc.webdemoapp.app.CDemoApp;
import com.phloc.webscopes.mgr.WebScopeManager;

public final class DemoAppConfigAjaxServlet extends AbstractApplicationAjaxServlet
{
  public static final String SERVLET_DEFAULT_NAME = "configajax";
  public static final String SERVLET_DEFAULT_PATH = '/' + SERVLET_DEFAULT_NAME;

  @Override
  @Nonnull
  @Nonempty
  protected String getApplicationID ()
  {
    return CDemoApp.APP_CONFIG_ID;
  }

  @Nonnull
  public static SimpleURL getAjaxPath (@Nonnull @Nonempty final String sFunction)
  {
    if (StringHelper.hasNoText (sFunction))
      throw new IllegalArgumentException ("function");
    return new SimpleURL (WebScopeManager.getRequestScope ().getContextPath () + SERVLET_DEFAULT_PATH + "/" + sFunction);
  }
}
