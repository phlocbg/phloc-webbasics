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
package com.phloc.webdemoapp.app.init;

import java.io.File;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ILocaleManager;
import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.webbasics.action.IActionInvoker;
import com.phloc.webbasics.ajax.IAjaxInvoker;
import com.phloc.webbasics.app.html.HTMLConfigManager;
import com.phloc.webbasics.app.init.DefaultApplicationInitializer;
import com.phloc.webbasics.app.layout.ILayoutManager;
import com.phloc.webctrls.datatables.ajax.AjaxHandlerDataTables;
import com.phloc.webdemoapp.app.CDemoApp;
import com.phloc.webdemoapp.app.ajax.view.AjaxHandlerViewLogin;
import com.phloc.webdemoapp.app.ajax.view.AjaxHandlerViewUpdateMenuView;
import com.phloc.webdemoapp.app.ajax.view.CDemoAppAjaxView;
import com.phloc.webdemoapp.app.layout.view.LayoutView;
import com.phloc.webdemoapp.app.menu.view.MenuView;

public class InitializerView extends DefaultApplicationInitializer
{
  @Override
  public void initLocales (@Nonnull final ILocaleManager aLocaleMgr)
  {
    aLocaleMgr.registerLocale (CDemoApp.DEFAULT_LOCALE);
    aLocaleMgr.setDefaultLocale (CDemoApp.DEFAULT_LOCALE);
  }

  @Override
  public void initLayout (@Nonnull final ILayoutManager aLayoutMgr)
  {
    LayoutView.init (aLayoutMgr);
  }

  @Override
  public void initMenu (@Nonnull final IMenuTree aMenuTree)
  {
    MenuView.init (aMenuTree);
  }

  @Override
  public void initAjax (@Nonnull final IAjaxInvoker aAjaxInvoker)
  {
    aAjaxInvoker.addHandlerFunction (CDemoAppAjaxView.VIEW_DATATABLES, AjaxHandlerDataTables.class);
    aAjaxInvoker.addHandlerFunction (CDemoAppAjaxView.VIEW_LOGIN, AjaxHandlerViewLogin.class);
    aAjaxInvoker.addHandlerFunction (CDemoAppAjaxView.VIEW_UPDATE_MENU_VIEW, AjaxHandlerViewUpdateMenuView.class);
  }

  @Override
  public void initActions (@Nonnull final IActionInvoker aActionInvoker)
  {}

  @Override
  public void initRest ()
  {
    // Copy all resources to the outside
    for (final String sFilename : new String [] { HTMLConfigManager.FILENAME_CSS_XML,
                                                 HTMLConfigManager.FILENAME_JS_XML,
                                                 HTMLConfigManager.FILENAME_METATAGS_XML })
    {
      final File aDestFile = WebFileIO.getFile (sFilename);
      WebFileIO.getFileOpMgr ().deleteFileIfExisting (aDestFile);
      StreamUtils.copyInputStreamToOutputStreamAndCloseOS (ClassPathResource.getInputStream (sFilename),
                                                           FileUtils.getOutputStream (aDestFile));
    }
  }
}
