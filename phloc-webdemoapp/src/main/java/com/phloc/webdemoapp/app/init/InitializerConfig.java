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
import com.phloc.webbasics.form.ajax.AjaxHandlerSaveFormState;
import com.phloc.webdemoapp.app.CDemoApp;
import com.phloc.webdemoapp.app.action.config.ActionPing;
import com.phloc.webdemoapp.app.action.config.CDemoAppActionConfig;
import com.phloc.webdemoapp.app.ajax.config.AjaxHandlerConfigUpdateMenuView;
import com.phloc.webdemoapp.app.ajax.config.CDemoAppAjaxConfig;
import com.phloc.webdemoapp.app.layout.config.LayoutConfig;
import com.phloc.webdemoapp.app.menu.config.MenuConfig;

public class InitializerConfig extends DefaultApplicationInitializer
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
    LayoutConfig.init (aLayoutMgr);
  }

  @Override
  public void initMenu (@Nonnull final IMenuTree aMenuTree)
  {
    MenuConfig.init (aMenuTree);
  }

  @Override
  public void initAjax (@Nonnull final IAjaxInvoker aAjaxInvoker)
  {
    aAjaxInvoker.addHandlerFunction (CDemoAppAjaxConfig.SAVE_FORM_STATE, AjaxHandlerSaveFormState.class);
    aAjaxInvoker.addHandlerFunction (CDemoAppAjaxConfig.UPDATE_MENU_VIEW, AjaxHandlerConfigUpdateMenuView.class);
  }

  @Override
  public void initActions (@Nonnull final IActionInvoker aActionInvoker)
  {
    aActionInvoker.addAction (CDemoAppActionConfig.PING, new ActionPing ());
  }

  @Override
  public void initRest ()
  {
    // Copy all resources to the outside
    for (final String sFilename : new String [] { HTMLConfigManager.FILENAME_CSS_XML,
                                                 HTMLConfigManager.FILENAME_JS_XML,
                                                 HTMLConfigManager.FILENAME_METATAGS_XML })
    {
      final String sFullFilename = HTMLConfigManager.DEFAULT_BASE_PATH + sFilename;
      final File aDestFile = WebFileIO.getFile (sFullFilename);
      WebFileIO.getFileOpMgr ().deleteFileIfExisting (aDestFile);
      StreamUtils.copyInputStreamToOutputStreamAndCloseOS (ClassPathResource.getInputStream (sFullFilename),
                                                           FileUtils.getOutputStream (aDestFile));
    }
  }
}
